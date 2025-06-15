import { Global, Module } from '@nestjs/common';
import { PrismaModule } from '../prisma/prisma.module';
import { SessionService } from './session.service';

@Global()
@Module({
  imports: [PrismaModule],
  providers: [SessionService],
  exports: [SessionService],
})
export class SessionModule {}
