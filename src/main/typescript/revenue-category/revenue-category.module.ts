import { Module } from '@nestjs/common';
import { RevenueCategoryService } from './revenue-category.service';

@Module({
  providers: [RevenueCategoryService],
  exports: [RevenueCategoryService],
})
export class RevenueCategoryModule {}
