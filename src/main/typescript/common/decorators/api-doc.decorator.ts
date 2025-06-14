import { applyDecorators } from '@nestjs/common';
import { ApiBearerAuth, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { ClassConstructor } from 'class-transformer';
import { ApiOkResponsePaginated } from './paginated-response.decorator';

export function ApiDoc<T>(option: {
  summary: string;
  description: string;
  type: ClassConstructor<T>;
  paginated?: boolean;
  status?: number;
  isArray?: boolean;
  deprecated?: boolean;
  authRequired?: boolean;
}): MethodDecorator {
  const {
    summary,
    description,
    type,
    paginated = false,
    status = 200,
    isArray = false,
    deprecated = false,
    authRequired = false,
  } = option;

  const decorators = [ApiOperation({ summary, description, deprecated })];

  if (authRequired) {
    decorators.push(ApiBearerAuth());
  }

  if (paginated) {
    decorators.push(ApiOkResponsePaginated(type));
  } else {
    decorators.push(
      ApiResponse({
        status,
        description: summary + ' 성공',
        type: type,
        isArray,
      }),
    );
  }

  return applyDecorators(...decorators);
}
