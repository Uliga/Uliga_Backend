import { ApiProperty } from '@nestjs/swagger';

export class TokenResponseDto {
  @ApiProperty({ example: 'jwt-token', description: '엑세스 토큰' })
  accessToken!: string;

  @ApiProperty({
    example: 'uuid-session-id',
    description: '세션 ID (앱 클라이언트용)',
    required: false,
  })
  sessionId?: string;
}
