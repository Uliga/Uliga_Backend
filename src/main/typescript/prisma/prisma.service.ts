import {
  Injectable,
  Logger,
  OnModuleDestroy,
  OnModuleInit,
} from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { PrismaClient } from '@prisma/client';

/**
 * Prisma service for write operations.
 * Establishes connection on module init and disconnects on destroy.
 */
@Injectable()
export class PrismaService
  extends PrismaClient
  implements OnModuleInit, OnModuleDestroy
{
  private readonly logger = new Logger(PrismaService.name);

  constructor(private readonly configService: ConfigService) {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    super({
      datasources: {
        db: { url: configService.get<string>('DATABASE_WRITE_URL') },
      },
      ...(configService.getOrThrow<boolean>('LOG_PRISMA_QUERY') && {
        log: ['query', 'info', 'warn', 'error'],
      }),
    });
  }

  /**
   * Connect to the database on module initialization
   */
  async onModuleInit(): Promise<void> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    await this.$connect();
    this.logger.log('PrismaWriteService connected');
  }

  /**
   * Disconnect from the database on module destroy
   */
  async onModuleDestroy(): Promise<void> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    await this.$disconnect();
    this.logger.log('PrismaWriteService disconnected');
  }
}

/**
 * Prisma service for read operations (read-replica).
 * Uses a separate read-only database connection.
 */
@Injectable()
export class PrismaReadService
  extends PrismaClient
  implements OnModuleInit, OnModuleDestroy
{
  private readonly logger = new Logger(PrismaReadService.name);

  constructor(private readonly configService: ConfigService) {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    super({
      datasources: {
        db: { url: configService.get<string>('DATABASE_READ_URL') },
      },
      ...(configService.getOrThrow<boolean>('LOG_PRISMA_QUERY') && {
        log: ['query', 'info', 'warn', 'error'],
      }),
    });
  }

  /**
   * Connect to the read-replica on module initialization
   */
  async onModuleInit(): Promise<void> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    await this.$connect();
    this.logger.log('PrismaReadService connected');
  }

  /**
   * Disconnect from the read-replica on module destroy
   */
  async onModuleDestroy(): Promise<void> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call
    await this.$disconnect();
    this.logger.log('PrismaReadService disconnected');
  }
}
