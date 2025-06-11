import { Module } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TypedConfigService } from './typed-config/typed-config.service';

@Module({
  imports: [],
  controllers: [AppController],
  providers: [
    AppService,
    {
      provide: ConfigService,
      useClass: TypedConfigService,
    },
  ],
})
export class AppModule {}
