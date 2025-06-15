import { ApiProperty, OmitType } from '@nestjs/swagger';
import { IsEmail, IsNotEmpty, IsString, MinLength } from 'class-validator';
import { BaseUserDto } from '../../../user/dto/user.dto';

export class SignUpUserDto extends OmitType(BaseUserDto, [
  'createdAt',
  'authority',
  'id',
  'isActive',
  'userLoginType',
  'updatedAt',
  'password',
  'appPassword',
]) {
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

  @ApiProperty({
    example: 'apppassword123',
    description: '앱 비밀번호, 최소 4글자',
  })
  @IsString({ message: '앱 비밀번호는 문자열입니다.' })
  @MinLength(4, { message: '앱 비밀번호는 최소 4글자입니다.' })
  @IsNotEmpty({ message: '앱 비밀번호는 필수입니다.' })
  appPassword!: string;

  @ApiProperty({ example: 'John Doe', description: '사용자 이름' })
  @IsString({ message: '사용자 이름은 문자열입니다.' })
  @IsNotEmpty({ message: '사용자 이름은 필수입니다.' })
  userName!: string;

  @ApiProperty({ example: 'johndoe', description: '사용자 닉네임' })
  @IsString({ message: '닉네임은 문자열입니다.' })
  @IsNotEmpty({ message: '닉네임은 필수입니다.' })
  nickName!: string;
}
