import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Environment, type EnvironmentVariables } from '.';

@Injectable()
export class TypedConfigService {
  constructor(
    private readonly configService: ConfigService<EnvironmentVariables, true>,
  ) {}

  get nodeEnv(): Environment {
    return this.configService.get('NODE_ENV');
  }

  get port(): number {
    return this.configService.get('PORT');
  }

  get databaseUrl(): string {
    return this.configService.get('DATABASE_URL');
  }

  get databaseWriteUrl(): string {
    return this.configService.get('DATABASE_WRITE_URL');
  }

  get databaseReadUrl(): string {
    return this.configService.get('DATABASE_READ_URL');
  }

  get redisHost(): string {
    return this.configService.get('REDIS_HOST');
  }

  get redisPort(): number {
    return this.configService.get('REDIS_PORT');
  }

  get logPrismaQuery(): boolean {
    return this.configService.get('LOG_PRISMA_QUERY');
  }

  get isDevelopment(): boolean {
    return this.nodeEnv === Environment.Development;
  }

  get isProduction(): boolean {
    return this.nodeEnv === Environment.Production;
  }

  get isTest(): boolean {
    return this.nodeEnv === Environment.Test;
  }

  get databaseConfig() {
    return {
      url: this.databaseUrl,
    };
  }

  get redisConfig() {
    return {
      host: this.redisHost,
      port: this.redisPort,
    };
  }
}
