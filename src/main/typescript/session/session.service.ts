import { Injectable } from '@nestjs/common';
import { UserSession } from '@prisma/client';
import * as crypto from 'crypto';
import { PrismaService } from '../prisma/prisma.service';
import { CreateSessionData, UserSessionWithUser } from './types';

@Injectable()
export class SessionService {
  constructor(private readonly prismaService: PrismaService) {}

  async createSession(data: CreateSessionData): Promise<UserSession> {
    const sessionId = crypto.randomUUID();

    return await this.prismaService.userSession.create({
      data: {
        id: sessionId,
        userId: data.userId,
        refreshToken: data.refreshToken,
        userAgent: data.userAgent,
        ipAddress: data.ipAddress,
        expiresAt: data.expiresAt,
        isActive: true,
      },
    });
  }

  async getSession(sessionId: string): Promise<UserSessionWithUser | null> {
    return this.prismaService.userSession.findFirst({
      where: {
        id: sessionId,
        isActive: true,
        expiresAt: {
          gt: new Date(),
        },
      },
      include: {
        user: true,
      },
    });
  }

  async updateSessionRefreshToken(
    sessionId: string,
    refreshToken: string,
    expiresAt: Date,
  ): Promise<void> {
    await this.prismaService.userSession.update({
      where: { id: sessionId },
      data: {
        refreshToken,
        expiresAt,
        updatedAt: new Date(),
      },
    });
  }

  async extendSessionExpiration(
    sessionId: string,
    newExpiresAt: Date,
  ): Promise<void> {
    await this.prismaService.userSession.update({
      where: { id: sessionId },
      data: {
        expiresAt: newExpiresAt,
        updatedAt: new Date(),
      },
    });
  }

  async revokeSession(sessionId: string): Promise<void> {
    await this.prismaService.userSession.update({
      where: { id: sessionId },
      data: {
        isActive: false,
        updatedAt: new Date(),
      },
    });
  }

  async revokeAllUserSessions(userId: number): Promise<void> {
    await this.prismaService.userSession.updateMany({
      where: {
        userId,
        isActive: true,
      },
      data: {
        isActive: false,
        updatedAt: new Date(),
      },
    });
  }

  async cleanupExpiredSessions(): Promise<void> {
    await this.prismaService.userSession.updateMany({
      where: {
        expiresAt: {
          lt: new Date(),
        },
        isActive: true,
      },
      data: {
        isActive: false,
        updatedAt: new Date(),
      },
    });
  }

  async getUserActiveSessions(userId: number): Promise<UserSession[]> {
    return await this.prismaService.userSession.findMany({
      where: {
        userId,
        isActive: true,
        expiresAt: {
          gt: new Date(),
        },
      },
      orderBy: {
        createdAt: 'desc',
      },
    });
  }
}
