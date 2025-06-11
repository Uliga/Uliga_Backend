import { Module } from '@nestjs/common';
import { PrismaReadService, PrismaService } from './prisma.service';

/**
 * Module that provides separate Prisma write and read services.
 */
@Module({
  providers: [PrismaService, PrismaReadService],
  exports: [PrismaService, PrismaReadService],
})
export class PrismaModule {}
