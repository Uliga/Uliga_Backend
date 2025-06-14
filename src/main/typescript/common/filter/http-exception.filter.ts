import {
  ArgumentsHost,
  Catch,
  ExceptionFilter,
  HttpException,
  Logger,
} from '@nestjs/common';
import { Response } from 'express';
import {
  AuthenticatedRequest,
  HttpExceptionJsonResponse,
} from './../interface/http.interface';

@Catch(HttpException)
export class HttpExceptionFilter implements ExceptionFilter {
  private readonly logger = new Logger(HttpExceptionFilter.name);

  catch(exception: HttpException, host: ArgumentsHost): void {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response<HttpExceptionJsonResponse>>();
    const request = ctx.getRequest<AuthenticatedRequest>();
    const status = exception.getStatus();
    const errorMessage = exception.message;

    const errorDetails: HttpExceptionJsonResponse = {
      statusCode: status,
      timestamp: new Date().toISOString(),
      path: request.url,
      method: request.method,
      message: Array.isArray(errorMessage) ? errorMessage : [errorMessage],
      error: exception.name,
      userId: request.user?.id,
    };

    // Log the error (exclude 4xx client errors from error logs)
    if (status >= 500) {
      this.logger.error(
        `${request.method} ${request.url} - ${status} - ${exception.name} -  ${exception.message}`,
        exception.stack,
      );
    } else {
      this.logger.warn(
        `${request.method} ${request.url} - ${status} - ${exception.name} - ${exception.message}`,
      );
    }

    response.status(status).json(errorDetails);
  }
}
