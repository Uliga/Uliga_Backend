import { ApiProperty } from '@nestjs/swagger';
import { IsNotEmpty, IsString } from 'class-validator';

export class RefreshTokenDto {
  @ApiProperty({
    example: 'refresh-jwt-token',
    description: '리프레시 토큰',
  })
  @IsString({ message: '리프레시 토큰은 문자열입니다.' })
  @IsNotEmpty({ message: '리프레시 토큰은 필수입니다.' })
  refreshToken!: string;
}
