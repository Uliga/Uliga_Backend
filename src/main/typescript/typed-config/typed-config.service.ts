import { ConfigService } from '@nestjs/config';

export class TypedConfigService extends ConfigService {
  getOrThrow<T = string>(key: string): T {
    const value = this.get<T>(key);
    if (value === undefined || value === null) {
      throw new Error(`Missing config value for key: ${key}`);
    }
    return value;
  }
}
