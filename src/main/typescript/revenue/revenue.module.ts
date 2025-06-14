import { Module } from '@nestjs/common';
import { RevenueService } from './revenue.service';

@Module({
  providers: [RevenueService],
  exports: [RevenueService],
})
export class RevenueModule {}
