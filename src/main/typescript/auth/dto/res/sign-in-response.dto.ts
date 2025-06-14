import { ApiProperty } from '@nestjs/swagger';

export class SignInResponseDto {
  @ApiProperty({ example: 'jwt-token', description: '엑세스 토큰' })
  accessToken!: string;
}
