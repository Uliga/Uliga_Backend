import {
  ArgumentsHost,
  BadRequestException,
  Catch,
  ExceptionFilter,
} from '@nestjs/common';
import { Response } from 'express';
import {
  AuthenticatedRequest,
  HttpExceptionJsonResponse,
} from '../interface/http.interface';

@Catch(BadRequestException)
export class ValidationExceptionFilter implements ExceptionFilter {
  catch(exception: BadRequestException, host: ArgumentsHost): void {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response<HttpExceptionJsonResponse>>();
    const request = ctx.getRequest<AuthenticatedRequest>();
    const status = exception.getStatus();
    const exceptionResponse = exception.getResponse();
    const errorMessage =
      typeof exceptionResponse === 'object' && 'message' in exceptionResponse
        ? (exceptionResponse.message as string)
        : exception.message;

    const errorDetails: HttpExceptionJsonResponse = {
      statusCode: status,
      timestamp: new Date().toISOString(),
      path: request.url,
      method: request.method,
      message: Array.isArray(errorMessage) ? errorMessage : [errorMessage],
      error: exception.name,
      userId: request.user?.id,
    };

    response.status(status).json(errorDetails);
  }
}
