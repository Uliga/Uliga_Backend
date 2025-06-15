import { Module } from '@nestjs/common';
import { UserV2Controller } from '../user/user-v2.controller';
import { UserModule } from '../user/user.module';

@Module({
  imports: [UserModule],
  controllers: [UserV2Controller],
})
export class V2Module {}
