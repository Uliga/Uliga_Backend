import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { PassportModule } from '@nestjs/passport';
import { JwtStrategy } from '../strategy/jwt.strategy';
import { LocalStrategy } from '../strategy/local.strategy';
import { TypedConfigModule } from '../typed-config/typed-config.module';
import { TypedConfigService } from '../typed-config/typed-config.service';
import { AuthService } from './auth.service';

@Module({
  imports: [
    PassportModule,
    JwtModule.registerAsync({
      imports: [TypedConfigModule],
      inject: [TypedConfigService],
      useFactory: (typedConfigService: TypedConfigService) => ({
        secret: typedConfigService.jwtSecret,
      }),
      global: true,
    }),
  ],
  providers: [AuthService, LocalStrategy, JwtStrategy],
  exports: [AuthService],
})
export class AuthModule {}
