import { Body, Controller, Post } from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';
import { User } from '@prisma/client';
import { ApiDoc } from '../common/decorators/api-doc.decorator';
import { Serialize } from '../common/interceptor/serialize.interceptor';
import { BaseUserDto } from '../user/dto/user.dto';
import { AuthService } from './auth.service';
import { SignInDto } from './dto/req/sign-in.dto';
import { SignUpUserDto } from './dto/req/sign-up.dto';
import { SignInResponseDto } from './dto/res/sign-in-response.dto';

@ApiTags('Auth API')
@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('sign-up')
  @ApiDoc({
    summary: ' 회원 가입 API',
    description: '이메일, 비밀번호로 회원가입 API 입니다.',
    type: BaseUserDto,
  })
  @Serialize(BaseUserDto)
  async signUpUser(@Body() dto: SignUpUserDto): Promise<User> {
    return this.authService.signUp(dto);
  }

  @Post('sign-in')
  @ApiDoc({
    summary: '로그인 API',
    description: '이메일, 비밀번호로 로그인 API 입니다.',
    type: SignInResponseDto,
  })
  async signInUser(@Body() dto: SignInDto): Promise<SignInResponseDto> {
    return this.authService.signIn(dto);
  }
}
