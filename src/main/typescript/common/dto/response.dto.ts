import { ApiProperty } from '@nestjs/swagger';

export class MessageResponseDto {
  @ApiProperty({ example: 'API 결과', description: 'API 결과' })
  message!: string;
}
