import { Global, Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { validateConfig } from '.';
import { TypedConfigService } from './typed-config.service';

@Global()
@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      validate: validateConfig,
      envFilePath: ['.env'],
      cache: true,
      expandVariables: true, // Support variable expansion like ${PORT}
    }),
  ],
  providers: [TypedConfigService],
  exports: [TypedConfigService],
})
export class TypedConfigModule {}
