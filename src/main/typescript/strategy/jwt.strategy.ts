import { Injectable, UnauthorizedException } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { User } from '@prisma/client';
import { ExtractJwt, Strategy } from 'passport-jwt';
import { PrismaReadService } from '../prisma/prisma.service';
import { TOKEN_ISSUER, TokenPayload, TokenType } from '../token/types';
import { TypedConfigService } from '../typed-config/typed-config.service';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
  constructor(
    private readonly configService: TypedConfigService,
    private readonly prismaReadService: PrismaReadService,
  ) {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      ignoreExpiration: false,
      secretOrKey: configService.jwtSecret,
    });
  }

  async validate(payload: TokenPayload): Promise<User> {
    if (payload.type !== TokenType.ACCESS || payload.iss !== TOKEN_ISSUER) {
      throw new UnauthorizedException('Invalid token type');
    }

    const user = await this.prismaReadService.user.findFirst({
      where: {
        id: parseInt(payload.sub),
        isActive: true,
      },
    });

    if (!user) {
      throw new UnauthorizedException('User not found');
    }

    return user;
  }
}
