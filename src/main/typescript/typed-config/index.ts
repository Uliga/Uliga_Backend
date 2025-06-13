import * as Joi from 'joi';

export enum Environment {
  Development = 'development',
  Production = 'production',
  Test = 'test',
}

export interface EnvironmentVariables {
  NODE_ENV: Environment;
  PORT: number;
  DATABASE_URL: string;
  DATABASE_WRITE_URL: string;
  DATABASE_READ_URL: string;
  REDIS_HOST: string;
  REDIS_PORT: number;
  LOG_PRISMA_QUERY: boolean;
}

export const configValidationSchema = Joi.object({
  NODE_ENV: Joi.string()
    .valid(...Object.values(Environment))
    .default(Environment.Development),

  PORT: Joi.number().port().default(3000),

  DATABASE_URL: Joi.string().required(),
  DATABASE_WRITE_URL: Joi.string().required(),
  DATABASE_READ_URL: Joi.string().required(),

  // JWT_SECRET: Joi.string().min(32).required(),

  // JWT_EXPIRATION_TIME: Joi.number().positive().default(3600),

  REDIS_HOST: Joi.string().required(),
  REDIS_PORT: Joi.number().default(6379),

  // API_KEY: Joi.string().required(),

  LOG_PRISMA_QUERY: Joi.boolean().default(false),
});

export const validateConfig = (
  config: Record<string, unknown>,
): EnvironmentVariables => {
  // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
  const { error, value } = configValidationSchema.validate(config, {
    allowUnknown: true,
    abortEarly: false,
  });

  if (error) {
    const errorMessages = error.details
      .map((detail) => detail.message)
      .join(', ');
    throw new Error(`Configuration validation failed: ${errorMessages}`);
  }

  return value as EnvironmentVariables;
};
