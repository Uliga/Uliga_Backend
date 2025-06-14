import {
  Injectable,
  Logger,
  OnModuleDestroy,
  OnModuleInit,
} from '@nestjs/common';
import { PrismaClient } from '@prisma/client';
import { TypedConfigService } from '../typed-config/typed-config.service';

@Injectable()
export class PrismaService
  extends PrismaClient
  implements OnModuleInit, OnModuleDestroy
{
  private readonly logger = new Logger(PrismaService.name);

  constructor(private readonly configService: TypedConfigService) {
    super({
      datasources: {
        db: { url: configService.databaseWriteUrl },
      },
      ...(configService.logPrismaQuery && {
        log: ['query', 'info', 'warn', 'error'],
      }),
    });
  }

  async onModuleInit(): Promise<void> {
    await this.$connect();
    this.logger.log('PrismaWriteService connected');
  }

  async onModuleDestroy(): Promise<void> {
    await this.$disconnect();
    this.logger.log('PrismaWriteService disconnected');
  }
}

@Injectable()
export class PrismaReadService
  extends PrismaClient
  implements OnModuleInit, OnModuleDestroy
{
  private readonly logger = new Logger(PrismaReadService.name);

  constructor(private readonly configService: TypedConfigService) {
    super({
      datasources: {
        db: { url: configService.databaseReadUrl },
      },
      ...(configService.logPrismaQuery && {
        log: ['query', 'info', 'warn', 'error'],
      }),
    });
  }

  async onModuleInit(): Promise<void> {
    await this.$connect();
    this.logger.log('PrismaReadService connected');
  }

  async onModuleDestroy(): Promise<void> {
    await this.$disconnect();
    this.logger.log('PrismaReadService disconnected');
  }
}
