import { Request } from 'express';

export interface HttpExceptionJsonResponse {
  statusCode: number;
  timestamp: string;
  path: string;
  method: string;
  message: string[];
  error: string;
  userId?: number;
}

export interface AuthenticatedRequest extends Request {
  user?: {
    id: number;
    email: string;
  };
}
