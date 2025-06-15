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

  get jwtSecret(): string {
    return this.configService.get('JWT_PRIVATE_KEY_BASE64');
  }

  get accessDuration(): string {
    return this.configService.get('ACCESS_DURATION');
  }

  get refreshDuration(): string {
    return this.configService.get('REFRESH_DURATION');
  }

  get sessionSecret(): string {
    return this.configService.get('SESSION_SECRET');
  }

  get sessionExtensionDuration(): string {
    return this.configService.get('SESSION_EXTENSION_DURATION');
  }
}
