import { Controller, Get } from '@nestjs/common';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';

interface UserV2 {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  profile: {
    createdAt: string;
    lastLogin: string;
  };
}

@ApiTags('users')
@Controller({ path: 'users', version: '2' })
export class UserV2Controller {
  @Get()
  @ApiOperation({
    summary: 'Get users (V2)',
    description:
      'Returns list of users in V2 format with enhanced profile data',
  })
  @ApiResponse({ status: 200, description: 'Users retrieved successfully' })
  getUsersV2(): UserV2[] {
    return [
      {
        id: 1,
        email: 'john@example.com',
        firstName: 'John',
        lastName: 'Doe',
        profile: {
          createdAt: '2024-01-15T10:30:00Z',
          lastLogin: '2024-06-14T09:15:00Z',
        },
      },
      {
        id: 2,
        email: 'jane@example.com',
        firstName: 'Jane',
        lastName: 'Smith',
        profile: {
          createdAt: '2024-02-01T14:20:00Z',
          lastLogin: '2024-06-13T16:45:00Z',
        },
      },
    ];
  }
}
