/*
  Warnings:

  - A unique constraint covering the columns `[email]` on the table `User` will be added. If there are existing duplicate values, this will fail.

*/
-- AlterTable
ALTER TABLE `User` ADD COLUMN `profileImageUrl` VARCHAR(500) NULL,
    MODIFY `password` VARCHAR(255) NULL,
    MODIFY `appPassword` VARCHAR(255) NULL,
    MODIFY `userLoginType` ENUM('EMAIL', 'GOOGLE', 'KAKAO', 'GITHUB', 'OIDC') NOT NULL;

-- CreateTable
CREATE TABLE `OAuthProvider` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `userId` INTEGER NOT NULL,
    `provider` ENUM('EMAIL', 'GOOGLE', 'KAKAO', 'GITHUB', 'OIDC') NOT NULL,
    `providerId` VARCHAR(255) NOT NULL,
    `accessToken` TEXT NULL,
    `refreshToken` TEXT NULL,
    `idToken` TEXT NULL,
    `metadata` TEXT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `OAuthProvider_userId_idx`(`userId`),
    UNIQUE INDEX `OAuthProvider_provider_providerId_key`(`provider`, `providerId`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateIndex
CREATE UNIQUE INDEX `User_email_key` ON `User`(`email`);
