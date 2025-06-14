import { ValidationPipe, VersioningType } from '@nestjs/common';
import { NestFactory } from '@nestjs/core';
import helmet from 'helmet';
import { AppModule } from './app.module';
import { CORS_ORIGIN } from './common/constants/cors-origin.constant';
import { ALLOWED_HEADERS } from './common/constants/http-allowed-headers.constant';
import { HttpExceptionFilter } from './common/filter/http-exception.filter';
import { PrismaExceptionFilter } from './common/filter/prisma-exception.filter';
import { UnhandledExceptionFilter } from './common/filter/unhandled-exception.filter';
import { ValidationExceptionFilter } from './common/filter/validation-exception.filter';
import { setupSwagger } from './swagger';
import { TypedConfigService } from './typed-config/typed-config.service';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  const typedConfigService = app.get<TypedConfigService>(TypedConfigService);

  const port = typedConfigService.port;

  app.use(helmet());

  app.enableCors({
    origin: CORS_ORIGIN,
    allowedHeaders: ALLOWED_HEADERS,
    methods: 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
    credentials: true,
  });

  app.enableVersioning({
    prefix: 'apiV',
    type: VersioningType.URI,
    defaultVersion: '1',
  });

  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
      transformOptions: {
        enableImplicitConversion: true,
      },
    }),
  );

  app.useGlobalFilters(
    new UnhandledExceptionFilter(),
    new HttpExceptionFilter(),
    new PrismaExceptionFilter(),
    new ValidationExceptionFilter(),
  );

  setupSwagger(app);

  await app.listen(port, () => {
    console.log(`Application is running on: http://localhost:${port}`);
    console.log(
      `Swagger V1 available at: http://localhost:${port}/api/docs/v1`,
    );
    console.log(
      `Swagger V2 available at: http://localhost:${port}/api/docs/v2`,
    );
  });
}

bootstrap().catch((error) => {
  console.log(error);
});
