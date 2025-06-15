import { Prisma } from '@prisma/client';

export interface CreateSessionData {
  userId: number;
  refreshToken: string;
  userAgent?: string;
  ipAddress?: string;
  expiresAt: Date;
}
export type UserSessionWithUser = Prisma.UserSessionGetPayload<{
  include: { user: true };
}>;
