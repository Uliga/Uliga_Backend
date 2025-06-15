/* eslint-disable @typescript-eslint/no-unsafe-return */
import { ApiProperty } from '@nestjs/swagger';
import { ClassConstructor, Exclude, Expose, Type } from 'class-transformer';

export class PaginatedDto<T> {
  constructor(nodes: T[], totalCount: number, type: ClassConstructor<T>) {
    this.nodes = nodes;
    this.totalCount = totalCount;
    this.type = type;
  }

  @Expose()
  @Type((options) => options?.object.type)
  nodes: T[];

  @ApiProperty({
    example: 1000,
    description: '전체 개수',
  })
  @Expose()
  totalCount: number;

  @Exclude()
  type: ClassConstructor<T>;
}
