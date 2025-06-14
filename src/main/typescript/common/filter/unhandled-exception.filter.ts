import { ArgumentsHost, Catch, ExceptionFilter, Logger } from '@nestjs/common';
import { Response } from 'express';
import {
  AuthenticatedRequest,
  HttpExceptionJsonResponse,
} from '../interface/http.interface';

@Catch()
export class UnhandledExceptionFilter implements ExceptionFilter {
  private readonly logger = new Logger(UnhandledExceptionFilter.name);

  catch(exception: Error, host: ArgumentsHost): void {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response<HttpExceptionJsonResponse>>();
    const request = ctx.getRequest<AuthenticatedRequest>();
    const status = 500;
    const errorMessage = exception.message;
    const exceptionName = exception.name;

    this.logger.error({
      name: `Exception name: ${exceptionName}`,
      message: `Unhandled exception: ${errorMessage}`,
      stack: exception.stack,
      url: request.url,
      method: request.method,
      headers: request.headers,
      // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
      body: request.body,
    });

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
