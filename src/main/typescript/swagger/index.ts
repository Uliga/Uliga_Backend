import { INestApplication } from '@nestjs/common';
import { DocumentBuilder, OpenAPIObject, SwaggerModule } from '@nestjs/swagger';
import { V1Module } from '../v1';
import { V2Module } from '../v2';

export function setupSwagger(app: INestApplication): void {
  // V1 API Documentation
  const v1Config = new DocumentBuilder()
    .setTitle('Uliga API V1')
    .setDescription('Uliga Backend API Documentation - Version 1')
    .setVersion('1.0')
    .addBearerAuth(
      {
        type: 'http',
        scheme: 'bearer',
        bearerFormat: 'JWT',
        name: 'JWT',
        description: 'Enter JWT token',
        in: 'header',
      },
      'JWT-auth',
    )
    .addServer('http://localhost:3000', 'local V1 API Server')
    .build();

  const v1Document = SwaggerModule.createDocument(app, v1Config, {
    include: [V1Module],
    operationIdFactory: (controllerKey: string, methodKey: string) =>
      `${controllerKey}_${methodKey}`,
  });

  // Filter V1 document to only show V1 versioned endpoints
  // filterDocumentByVersion(v1Document, '1');

  SwaggerModule.setup('api/docs/v1', app, v1Document, {
    swaggerOptions: {
      persistAuthorization: true,
      tagsSorter: 'alpha',
      operationsSorter: 'alpha',
    },
    customSiteTitle: 'Uliga API V1 Documentation',
  });

  // V2 API Documentation
  const v2Config = new DocumentBuilder()
    .setTitle('Uliga API V2')
    .setDescription('Uliga Backend API Documentation - Version 2')
    .setVersion('2.0')
    .addBearerAuth(
      {
        type: 'http',
        scheme: 'bearer',
        bearerFormat: 'JWT',
        name: 'JWT',
        description: 'Enter JWT token',
        in: 'header',
      },
      'JWT-auth',
    )
    .addServer('http://localhost:3000', 'local V2 API Server')
    .build();

  const v2Document = SwaggerModule.createDocument(app, v2Config, {
    include: [V2Module],
    operationIdFactory: (controllerKey: string, methodKey: string) =>
      `${controllerKey}_${methodKey}_v2`,
  });

  // Filter V2 document to only show V2 versioned endpoints
  // filterDocumentByVersion(v2Document, '2');

  SwaggerModule.setup('api/docs/v2', app, v2Document, {
    swaggerOptions: {
      persistAuthorization: true,
      tagsSorter: 'alpha',
      operationsSorter: 'alpha',
    },
    customSiteTitle: 'Uliga API V2 Documentation',
  });

  // Future versions can be added here
  // setupV3Swagger(app);
}

function filterDocumentByVersion(
  document: OpenAPIObject,
  version: string,
): void {
  if (!document.paths) {
    return;
  }

  const paths = document.paths;

  Object.keys(paths).forEach((path) => {
    const pathItem = paths[path];
    if (!pathItem) {
      return;
    }

    // Check if the path matches the version pattern or is version neutral
    const isVersionMatch =
      path.includes(`/apiV${version}/`) || path === '/favicon.ico'; // VERSION_NEUTRAL paths

    // Remove path if it doesn't match the version
    if (!isVersionMatch) {
      delete paths[path];
    }
  });
}
