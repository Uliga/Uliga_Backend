-- CreateTable
CREATE TABLE `UserSession` (
    `id` VARCHAR(191) NOT NULL,
    `userId` INTEGER NOT NULL,
    `refreshToken` TEXT NOT NULL,
    `userAgent` VARCHAR(500) NULL,
    `ipAddress` VARCHAR(45) NULL,
    `expiresAt` TIMESTAMP(0) NOT NULL,
    `isActive` BOOLEAN NOT NULL DEFAULT true,
    `createdAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updatedAt` TIMESTAMP(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),

    INDEX `UserSession_userId_idx`(`userId`),
    INDEX `UserSession_expiresAt_idx`(`expiresAt`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
