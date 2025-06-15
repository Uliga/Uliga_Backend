import {
  CallHandler,
  ExecutionContext,
  NestInterceptor,
  UseInterceptors,
} from '@nestjs/common';
import { ClassConstructor, plainToInstance } from 'class-transformer';
import { map, Observable } from 'rxjs';

export function Serialize<T>(dto: ClassConstructor<T>): MethodDecorator {
  return UseInterceptors(new SerializeInterceptor(dto));
}

export class SerializeInterceptor<T> implements NestInterceptor {
  constructor(private readonly dto: ClassConstructor<T>) {}

  intercept(
    context: ExecutionContext,
    next: CallHandler<ClassConstructor<T>>,
  ): Observable<T> | Promise<Observable<T>> {
    return next.handle().pipe(
      map((data) => {
        return plainToInstance(this.dto, data, {});
      }),
    );
  }
}
