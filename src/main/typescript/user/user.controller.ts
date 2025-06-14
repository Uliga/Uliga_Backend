import { Controller, Get } from '@nestjs/common';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';

interface UserV1 {
  id: number;
  email: string;
  name: string;
}
@ApiTags('users')
@Controller({
  path: 'users',
  version: '1',
})
export class UserController {
  @Get()
  @ApiOperation({
    summary: 'Get users (V1)',
    description: 'Returns list of users in V1 format',
  })
  @ApiResponse({ status: 200, description: 'Users retrieved successfully' })
  getUsersV1(): UserV1[] {
    return [
      {
        id: 1,
        email: 'john@example.com',
        name: 'John Doe',
      },
      {
        id: 2,
        email: 'jane@example.com',
        name: 'Jane Smith',
      },
    ];
  }
}
