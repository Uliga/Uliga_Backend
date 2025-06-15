import {
  BadRequestException,
  Injectable,
  UnauthorizedException,
} from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { $Enums, User } from '@prisma/client';
import * as bcrypt from 'bcrypt';
import { parseExpirationTime } from '../common/utils/time.util';
import { PrismaReadService, PrismaService } from '../prisma/prisma.service';
import { SessionService } from '../session/session.service';
import { TOKEN_ISSUER, TokenPayload, TokenType } from '../token/types';
import { TypedConfigService } from '../typed-config/typed-config.service';
import { SignInDto } from './dto/req/sign-in.dto';
import { SignUpUserDto } from './dto/req/sign-up.dto';
import { TokenResponseDto } from './dto/res/token.dto';

@Injectable()
export class AuthService {
  constructor(
    private readonly prismaService: PrismaService,
    private readonly prismaReadService: PrismaReadService,
    private readonly jwtService: JwtService,
    private readonly typedConfigService: TypedConfigService,
    private readonly sessionService: SessionService,
  ) {}

  async signUp(dto: SignUpUserDto): Promise<User> {
    const { email, password, appPassword, userName, nickName } = dto;

    const saltRounds = 12;
    const hashedPassword = await bcrypt.hash(password, saltRounds);
    const hashedAppPassword = await bcrypt.hash(appPassword, saltRounds);

    return this.prismaService.user.create({
      data: {
        email,
        password: hashedPassword,
        appPassword: hashedAppPassword,
        userName,
        nickName,
        authority: $Enums.Authority.USER,
        userLoginType: $Enums.UserLoginType.EMAIL,
        isActive: true,
      },
    });
  }

  async validateUser(email: string, password: string): Promise<User | null> {
    const user = await this.prismaReadService.user.findFirst({
      where: {
        email,
        isActive: true,
        userLoginType: $Enums.UserLoginType.EMAIL,
      },
    });

    if (!user) {
      return null;
    }

    const isPasswordValid = await bcrypt.compare(password, user.password ?? '');
    return isPasswordValid ? user : null;
  }

  async createUserSession(
    user: User,
    userAgent?: string,
    ipAddress?: string,
  ): Promise<{ accessToken: string; sessionId: string }> {
    const accessTokenPayload: TokenPayload = {
      sub: user.id.toString(),
      type: TokenType.ACCESS,
      iss: TOKEN_ISSUER,
    };

    const refreshTokenPayload: TokenPayload = {
      sub: user.id.toString(),
      type: TokenType.REFRESH,
      iss: TOKEN_ISSUER,
    };

    const accessToken = await this.jwtService.signAsync(accessTokenPayload, {
      expiresIn: this.typedConfigService.accessDuration,
    });

    const refreshToken = await this.jwtService.signAsync(refreshTokenPayload, {
      expiresIn: this.typedConfigService.refreshDuration,
    });

    const refreshExpiresAt = new Date(
      Date.now() + parseExpirationTime(this.typedConfigService.refreshDuration),
    );

    const session = await this.sessionService.createSession({
      userId: user.id,
      refreshToken,
      userAgent,
      ipAddress,
      expiresAt: refreshExpiresAt,
    });

    return {
      accessToken,
      sessionId: session.id,
    };
  }

  async signIn(
    dto: SignInDto,
    userAgent?: string,
    ipAddress?: string,
  ): Promise<{ accessToken: string; sessionId: string }> {
    const user = await this.validateUser(dto.email, dto.password);

    if (!user) {
      throw new BadRequestException(
        '이메일, 비밀번호로 존재하는 유저가 없습니다.',
      );
    }

    return this.createUserSession(user, userAgent, ipAddress);
  }

  async refreshToken(sessionId: string): Promise<TokenResponseDto> {
    const session = await this.sessionService.getSession(sessionId);

    if (!session) {
      throw new UnauthorizedException('유효하지 않은 세션입니다.');
    }

    try {
      const payload = await this.jwtService.verifyAsync<TokenPayload>(
        session.refreshToken,
      );

      if (payload.type !== TokenType.REFRESH || payload.iss !== TOKEN_ISSUER) {
        throw new UnauthorizedException('유효하지 않은 리프레시 토큰입니다.');
      }

      const user = await this.prismaReadService.user.findFirst({
        where: {
          id: session.userId,
          isActive: true,
        },
      });

      if (!user) {
        await this.sessionService.revokeSession(sessionId);
        throw new UnauthorizedException('유효하지 않은 사용자입니다.');
      }

      const newAccessPayload: TokenPayload = {
        sub: user.id.toString(),
        iss: TOKEN_ISSUER,
        type: TokenType.ACCESS,
      };

      const newRefreshPayload: TokenPayload = {
        sub: user.id.toString(),
        iss: TOKEN_ISSUER,
        type: TokenType.REFRESH,
      };

      const newAccessToken = await this.jwtService.signAsync(newAccessPayload, {
        expiresIn: this.typedConfigService.accessDuration,
      });

      const newRefreshToken = await this.jwtService.signAsync(
        newRefreshPayload,
        {
          expiresIn: this.typedConfigService.refreshDuration,
        },
      );

      const newRefreshExpiresAt = new Date(
        Date.now() +
          parseExpirationTime(this.typedConfigService.refreshDuration),
      );

      const newSessionExpiresAt = new Date(
        Date.now() +
          parseExpirationTime(this.typedConfigService.sessionExtensionDuration),
      );

      await this.sessionService.updateSessionRefreshToken(
        sessionId,
        newRefreshToken,
        newRefreshExpiresAt,
      );

      await this.sessionService.extendSessionExpiration(
        sessionId,
        newSessionExpiresAt,
      );

      return {
        accessToken: newAccessToken,
      };
    } catch {
      await this.sessionService.revokeSession(sessionId);
      throw new UnauthorizedException('유효하지 않은 리프레시 토큰입니다.');
    }
  }

  async signOut(sessionId: string): Promise<void> {
    await this.sessionService.revokeSession(sessionId);
  }

  async signOutAll(userId: number): Promise<void> {
    await this.sessionService.revokeAllUserSessions(userId);
  }
}
