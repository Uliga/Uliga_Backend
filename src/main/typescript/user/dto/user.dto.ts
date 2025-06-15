import { ApiProperty } from '@nestjs/swagger';
import { $Enums, User } from '@prisma/client';
import { Exclude } from 'class-transformer';

export class BaseUserDto implements User {
  @ApiProperty({ example: 1, description: 'id' })
  id!: number;

  @ApiProperty({ example: 'email@com', description: 'email' })
  email!: string;

  @Exclude()
  password!: string;

  @Exclude()
  appPassword!: string;

  @ApiProperty({ example: $Enums.Authority.USER, description: 'authority' })
  authority!: $Enums.Authority;

  @ApiProperty({
    example: $Enums.UserLoginType.EMAIL,
    description: 'loginType',
  })
  userLoginType!: $Enums.UserLoginType;

  @ApiProperty({ example: '이름', description: 'userName' })
  userName!: string;

  @ApiProperty({ example: '닉네임', description: 'nickName' })
  nickName!: string;

  @ApiProperty({ example: true, description: 'isActive' })
  isActive!: boolean;

  @ApiProperty({
    example: new Date('2025-06-01T00:00:00.000Z'),
    description: 'createdAt',
  })
  createdAt!: Date;

  @ApiProperty({
    example: new Date('2025-06-01T00:00:00.000Z'),
    description: 'updatedAt',
  })
  updatedAt!: Date;

  profileImageUrl!: string | null;
}
