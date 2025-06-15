import { applyDecorators } from '@nestjs/common';
import { ApiExtraModels, ApiOkResponse, getSchemaPath } from '@nestjs/swagger';
import { ClassConstructor } from 'class-transformer';
import { PaginatedDto } from '../dto/paginated.dto';

export const ApiOkResponsePaginated = <T>(
  dataDto: ClassConstructor<T>,
): MethodDecorator =>
  applyDecorators(
    ApiExtraModels(PaginatedDto, dataDto),
    ApiOkResponse({
      description: '조회 성공',
      schema: {
        allOf: [
          { $ref: getSchemaPath(PaginatedDto) },
          {
            properties: {
              nodes: {
                type: 'array',
                description: '페이지에 해당하는 데이터',
                items: { $ref: getSchemaPath(dataDto) },
              },
            },
          },
        ],
      },
    }),
  );
