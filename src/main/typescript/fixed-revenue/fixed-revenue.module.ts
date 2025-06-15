import { Module } from '@nestjs/common';
import { FixedRevenueService } from './fixed-revenue.service';

@Module({
  providers: [FixedRevenueService],
  exports: [FixedRevenueService],
})
export class FixedRevenueModule {}
