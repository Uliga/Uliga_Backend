import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { PrismaModule } from './prisma/prisma.module';
import { SessionModule } from './session/session.module';
import { TypedConfigModule } from './typed-config/typed-config.module';
import { V1Module } from './v1';
import { V2Module } from './v2';

@Module({
  imports: [TypedConfigModule, PrismaModule, SessionModule, V1Module, V2Module],
  controllers: [AppController],
})
export class AppModule {}
