import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TypedConfigModule } from './typed-config/typed-config.module';

@Module({
  imports: [TypedConfigModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
