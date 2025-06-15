import { Controller, Get } from '@nestjs/common';
import { ApiResponse, ApiTags } from '@nestjs/swagger';
import { User } from '@prisma/client';
import { ApiDoc } from '../common/decorators/api-doc.decorator';
import { PaginatedDto } from '../common/dto/paginated.dto';
import { Serialize } from '../common/interceptor/serialize.interceptor';
import { BaseUserDto } from './dto/user.dto';

@ApiTags('users')
@Controller({
  path: 'users',
  version: '1',
})
export class UserController {
  @Get()
  @ApiDoc({
    summary: '테스트',
    description: '테스트',
    type: BaseUserDto,
    isArray: true,
  })
  @Serialize(BaseUserDto)
  @ApiResponse({ status: 200, description: 'Users retrieved successfully' })
  getUsers(): User[] {
    return [
      {
        id: 1,
        email: 'john@example.com',
        password: '',
        appPassword: '',
        authority: 'USER',
        userLoginType: 'EMAIL',
        userName: '',
        nickName: '',
        isActive: true,
        createdAt: new Date('2025-06-01T00:00:00.000Z'),
        updatedAt: new Date('2025-06-01T00:00:00.000Z'),
        profileImageUrl: null,
      },
      {
        id: 2,
        email: 'jane@example.com',
        password: '',
        appPassword: '',
        authority: 'USER',
        userLoginType: 'EMAIL',
        userName: '',
        nickName: '',
        isActive: false,
        createdAt: new Date('2025-06-01T00:00:00.000Z'),
        updatedAt: new Date('2025-06-01T00:00:00.000Z'),
        profileImageUrl: null,
      },
    ];
  }

  @Get('page')
  @ApiDoc({
    summary: '테스트',
    description: '테스트',
    type: BaseUserDto,
    paginated: true,
  })
  @Serialize(PaginatedDto<BaseUserDto>)
  getPaginatedUser(): PaginatedDto<User> {
    return {
      nodes: [
        {
          id: 1,
          email: 'john@example.com',
          password: '',
          appPassword: '',
          authority: 'USER',
          userLoginType: 'EMAIL',
          userName: '',
          nickName: '',
          isActive: true,
          createdAt: new Date('2025-06-01T00:00:00.000Z'),
          updatedAt: new Date('2025-06-01T00:00:00.000Z'),
          profileImageUrl: null,
        },
        {
          id: 2,
          email: 'jane@example.com',
          password: '',
          appPassword: '',
          authority: 'USER',
          userLoginType: 'EMAIL',
          userName: '',
          nickName: '',
          isActive: false,
          createdAt: new Date('2025-06-01T00:00:00.000Z'),
          updatedAt: new Date('2025-06-01T00:00:00.000Z'),
          profileImageUrl: null,
        },
      ],
      totalCount: 2,
      type: BaseUserDto,
    };
  }
}
