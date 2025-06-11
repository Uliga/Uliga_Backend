/*
  Warnings:

  - The primary key for the `AccountBook` table will be changed. If it partially fails, the table could be left without primary key constraint.
  - You are about to alter the column `id` on the `AccountBook` table. The data in that column could be lost. The data in that column will be cast from `BigInt` to `Int`.

*/
-- AlterTable
ALTER TABLE `AccountBook` DROP PRIMARY KEY,
    ADD COLUMN `isActive` BOOLEAN NOT NULL DEFAULT true,
    MODIFY `id` INTEGER NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`);

-- CreateTable
CREATE TABLE `Budget` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `value` INTEGER NOT NULL,
    `year` INTEGER NOT NULL,
    `month` INTEGER NOT NULL,
    `expenseCategoryId` INTEGER NOT NULL,
    `accountBookId` INTEGER NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `Budget_expenseCategoryId_idx`(`expenseCategoryId`),
    INDEX `Budget_accountBookId_idx`(`accountBookId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `RevenueCategory` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `accountBookId` INTEGER NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `RevenueCategory_accountBookId_idx`(`accountBookId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `ExpenseCategory` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `accountBookId` INTEGER NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `ExpenseCategory_accountBookId_idx`(`accountBookId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `Revenue` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `value` INTEGER NOT NULL,
    `revenueSource` VARCHAR(255) NOT NULL,
    `memo` TEXT NOT NULL,
    `date` DATE NOT NULL,
    `userId` INTEGER NOT NULL,
    `revenueCategoryId` INTEGER NOT NULL,
    `accountBookId` INTEGER NOT NULL,
    `year` VARCHAR(255) NOT NULL,
    `month` VARCHAR(255) NOT NULL,
    `week` VARCHAR(255) NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `Revenue_accountBookId_idx`(`accountBookId`),
    INDEX `Revenue_userId_idx`(`userId`),
    INDEX `Revenue_revenueCategoryId_idx`(`revenueCategoryId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `Expense` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `value` INTEGER NOT NULL,
    `expensePayee` VARCHAR(255) NOT NULL,
    `memo` TEXT NOT NULL,
    `date` DATE NOT NULL,
    `userId` INTEGER NOT NULL,
    `expenseCategoryId` INTEGER NOT NULL,
    `accountBookId` INTEGER NOT NULL,
    `year` VARCHAR(255) NOT NULL,
    `month` VARCHAR(255) NOT NULL,
    `week` VARCHAR(255) NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `Expense_accountBookId_idx`(`accountBookId`),
    INDEX `Expense_userId_idx`(`userId`),
    INDEX `Expense_expenseCategoryId_idx`(`expenseCategoryId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `User` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `appPassword` VARCHAR(255) NOT NULL,
    `authority` ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    `userLoginType` ENUM('EMAIL', 'KAKAO') NOT NULL,
    `userName` VARCHAR(255) NOT NULL,
    `nickName` VARCHAR(255) NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `FixedRevenue` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `value` INTEGER NOT NULL,
    `startDate` DATE NOT NULL,
    `frequency` ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY') NOT NULL,
    `userId` INTEGER NOT NULL,
    `accountBookId` INTEGER NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `FixedRevenue_userId_idx`(`userId`),
    INDEX `FixedRevenue_accountBookId_idx`(`accountBookId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `FixedExpense` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `value` INTEGER NOT NULL,
    `startDate` DATE NOT NULL,
    `frequency` ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY') NOT NULL,
    `userId` INTEGER NOT NULL,
    `accountBookId` INTEGER NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `FixedExpense_userId_idx`(`userId`),
    INDEX `FixedExpense_accountBookId_idx`(`accountBookId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `AccountBookUser` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `accountBookId` INTEGER NOT NULL,
    `userId` INTEGER NOT NULL,
    `profileUrl` VARCHAR(500) NOT NULL,
    `getNotification` BOOLEAN NOT NULL DEFAULT false,
    `accountBookAuthority` ENUM('USER', 'ADMIN') NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `AccountBookUser_accountBookId_idx`(`accountBookId`),
    INDEX `AccountBookUser_userId_idx`(`userId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `FixedExpenseUser` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `userId` INTEGER NOT NULL,
    `accountBookId` INTEGER NOT NULL,
    `fixedExpenseId` INTEGER NOT NULL,
    `value` INTEGER NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `FixedExpenseUser_userId_idx`(`userId`),
    INDEX `FixedExpenseUser_accountBookId_idx`(`accountBookId`),
    INDEX `FixedExpenseUser_fixedExpenseId_idx`(`fixedExpenseId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `AccountBookInvitation` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `accountBookId` INTEGER NOT NULL,
    `inviterUserId` INTEGER NOT NULL,
    `inviteeUserId` INTEGER NOT NULL,
    `invitationStatus` ENUM('PENDING', 'DECLINED', 'ACCEPTED') NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `AccountBookInvitation_accountBookId_idx`(`accountBookId`),
    INDEX `AccountBookInvitation_inviterUserId_idx`(`inviterUserId`),
    INDEX `AccountBookInvitation_inviteeUserId_idx`(`inviteeUserId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
