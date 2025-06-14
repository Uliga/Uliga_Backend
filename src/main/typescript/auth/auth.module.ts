import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { TypedConfigModule } from '../typed-config/typed-config.module';
import { TypedConfigService } from '../typed-config/typed-config.service';
import { AuthService } from './auth.service';

@Module({
  imports: [
    JwtModule.registerAsync({
      imports: [TypedConfigModule],
      inject: [TypedConfigService],
      useFactory: (typedConfigService: TypedConfigService) => ({
        secret: typedConfigService.jwtSecret,
      }),
      global: true,
    }),
  ],
  providers: [AuthService],
  exports: [AuthService],
})
export class AuthModule {}
