import { ApiProperty } from '@nestjs/swagger';
import { IsEmail, IsNotEmpty, IsString, MinLength } from 'class-validator';

export class SignInDto {
  @ApiProperty({
    example: 'user@example.com',
    description: '사용자 이메일',
  })
  @IsEmail({}, { message: '유효한 이메일을 입력해주셔야 합니다.' })
  @IsNotEmpty({ message: '이메일은 필수입니다.' })
  email!: string;

  @ApiProperty({
    example: 'password123',
    description: '사용자 비밀번호, 최소 8글자',
  })
  @IsString({ message: '비밀번호는 문자열입니다.' })
  @MinLength(8, { message: '비밀번호는 최소 8글자입니다.' })
  @IsNotEmpty({ message: '비밀번호는 필수 입니다.' })
  password!: string;
}
