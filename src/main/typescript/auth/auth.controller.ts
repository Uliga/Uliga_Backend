import {
  Body,
  Controller,
  Get,
  Post,
  Req,
  Res,
  UnauthorizedException,
  UseGuards,
} from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';
import { User } from '@prisma/client';
import { Request, Response } from 'express';
import {
  CLIENT_TYPE_HEADER,
  USER_AGENT_HEADER,
} from '../common/constants/http-allowed-headers.constant';
import { ApiDoc } from '../common/decorators/api-doc.decorator';
import { MessageResponseDto } from '../common/dto/response.dto';
import { Serialize } from '../common/interceptor/serialize.interceptor';
import { AuthenticatedRequest } from '../common/interface/http.interface';
import { getSessionId } from '../common/utils/cookie.util';
import { JwtAuthGuard } from '../guard/jwt-auth.guard';
import { LocalAuthGuard } from '../guard/local-auth.guard';
import { BaseUserDto } from '../user/dto/user.dto';
import { AuthService } from './auth.service';
import { SignUpUserDto } from './dto/req/sign-up.dto';
import { TokenResponseDto } from './dto/res/token.dto';

@ApiTags('Auth API')
@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('sign-up')
  @ApiDoc({
    summary: ' 회원 가입 API',
    description: '이메일, 비밀번호로 회원가입 API 입니다.',
    type: BaseUserDto,
  })
  @Serialize(BaseUserDto)
  async signUpUser(@Body() dto: SignUpUserDto): Promise<User> {
    return this.authService.signUp(dto);
  }

  @Post('sign-in')
  @ApiDoc({
    summary: '로그인 API',
    description: '이메일, 비밀번호로 로그인 API 입니다.',
    type: TokenResponseDto,
  })
  @UseGuards(LocalAuthGuard)
  async signInUser(
    @Req() req: AuthenticatedRequest,
    @Res({ passthrough: true }) res: Response,
  ): Promise<TokenResponseDto> {
    const userAgent = req.headers[USER_AGENT_HEADER];
    const ipAddress = req.ip;
    const clientType = req.headers[CLIENT_TYPE_HEADER] as string;

    const { accessToken, sessionId } = await this.authService.createUserSession(
      req.user,
      userAgent,
      ipAddress,
    );

    if (clientType === 'app') {
      return { accessToken, sessionId };
    } else {
      res.cookie('sessionId', sessionId, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'strict',
        maxAge: 7 * 24 * 60 * 60 * 1000,
      });

      return { accessToken };
    }
  }

  @Post('refresh')
  @ApiDoc({
    summary: '토큰 갱신 API',
    description: '세션을 이용하여 새로운 엑세스 토큰을 발급합니다.',
    type: TokenResponseDto,
  })
  async refreshToken(@Req() req: Request): Promise<TokenResponseDto> {
    const sessionId = getSessionId(req);

    if (!sessionId) {
      throw new UnauthorizedException('세션이 존재하지 않습니다.');
    }

    return this.authService.refreshToken(sessionId);
  }

  @Post('sign-out')
  @ApiDoc({
    summary: '로그아웃 API',
    description: '현재 세션을 무효화합니다.',
    type: MessageResponseDto,
  })
  async signOut(
    @Req() req: Request,
    @Res({ passthrough: true }) res: Response,
  ): Promise<MessageResponseDto> {
    const sessionId = getSessionId(req);

    if (sessionId) {
      await this.authService.signOut(sessionId);
    }

    res.clearCookie('sessionId');
    return { message: '로그아웃되었습니다.' };
  }

  @Get('profile')
  @UseGuards(JwtAuthGuard)
  @ApiDoc({
    summary: '사용자 프로필 조회',
    description: 'JWT 토큰으로 인증된 사용자의 프로필을 조회합니다.',
    type: BaseUserDto,
  })
  @Serialize(BaseUserDto)
  getProfile(@Req() req: AuthenticatedRequest): User {
    return req.user;
  }

  @Post('sign-out-all')
  @UseGuards(JwtAuthGuard)
  @ApiDoc({
    summary: '전체 세션 로그아웃 API',
    description: '사용자의 모든 세션을 무효화합니다.',
    type: MessageResponseDto,
  })
  async signOutAll(
    @Req() req: AuthenticatedRequest,
    @Res({ passthrough: true }) res: Response,
  ): Promise<MessageResponseDto> {
    await this.authService.signOutAll(req.user.id);

    res.clearCookie('sessionId');

    return { message: '모든 세션에서 로그아웃되었습니다.' };
  }
}
