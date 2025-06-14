import { BadRequestException, Injectable } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { $Enums, User } from '@prisma/client';
import * as bcrypt from 'bcrypt';
import { PrismaReadService, PrismaService } from '../prisma/prisma.service';
import { SignInDto } from './dto/req/sign-in.dto';
import { SignUpUserDto } from './dto/req/sign-up.dto';
import { SignInResponseDto } from './dto/res/sign-in-response.dto';

@Injectable()
export class AuthService {
  constructor(
    private readonly prismaService: PrismaService,
    private readonly prismaReadService: PrismaReadService,
    private readonly jwtService: JwtService,
  ) {}

  async signUp(dto: SignUpUserDto): Promise<User> {
    const { email, password, appPassword, userName, nickName } = dto;

    const saltRounds = 12;
    const hashedPassword = await bcrypt.hash(password, saltRounds);
    const hashedAppPassword = await bcrypt.hash(appPassword, saltRounds);

    return this.prismaService.user.create({
      data: {
        email,
        password: hashedPassword,
        appPassword: hashedAppPassword,
        userName,
        nickName,
        authority: $Enums.Authority.USER,
        userLoginType: $Enums.UserLoginType.EMAIL,
        isActive: true,
      },
    });
  }

  async signIn(dto: SignInDto): Promise<SignInResponseDto> {
    const { email, password } = dto;

    const user = await this.prismaReadService.user.findFirst({
      where: {
        email,
        isActive: true,
      },
    });

    if (!user) {
      throw new BadRequestException('이메일로 존재하는 유저가 없습니다.');
    }

    const isPasswordValid = await bcrypt.compare(password, user.password);

    if (!isPasswordValid) {
      throw new BadRequestException(
        '이메일, 비밀번호로 존재하는 유저가 없습니다.',
      );
    }

    return {
      accessToken: await this.jwtService.signAsync(user.email),
    };
  }
}
