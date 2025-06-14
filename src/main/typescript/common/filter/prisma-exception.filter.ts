import {
  ArgumentsHost,
  Catch,
  ExceptionFilter,
  HttpStatus,
  Logger,
} from '@nestjs/common';
import {
  PrismaClientInitializationError,
  PrismaClientKnownRequestError,
  PrismaClientRustPanicError,
  PrismaClientUnknownRequestError,
  PrismaClientValidationError,
} from '@prisma/client/runtime/library';
import { Response } from 'express';
import {
  AuthenticatedRequest,
  HttpExceptionJsonResponse,
} from './../interface/http.interface';

@Catch(
  PrismaClientKnownRequestError,
  PrismaClientValidationError,
  PrismaClientUnknownRequestError,
  PrismaClientInitializationError,
  PrismaClientRustPanicError,
)
export class PrismaExceptionFilter implements ExceptionFilter {
  private readonly logger = new Logger(PrismaExceptionFilter.name);

  catch(
    exception:
      | PrismaClientKnownRequestError
      | PrismaClientValidationError
      | PrismaClientUnknownRequestError
      | PrismaClientInitializationError
      | PrismaClientRustPanicError,
    host: ArgumentsHost,
  ): void {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response<HttpExceptionJsonResponse>>();
    const request = ctx.getRequest<AuthenticatedRequest>();

    const { status, message, error } = this.mapPrismaError(exception);

    const errorDetails: HttpExceptionJsonResponse = {
      statusCode: status,
      timestamp: new Date().toISOString(),
      path: request.url,
      method: request.method,
      message: Array.isArray(message) ? message : [message],
      error,
      userId: request.user?.id,
    };

    // Log the error (exclude 4xx client errors from error logs)
    if (status >= 500) {
      this.logger.error(
        `${request.method} ${request.url} - ${status} - ${error} - ${exception.message}`,
        exception.stack,
      );
    } else {
      this.logger.warn(
        `${request.method} ${request.url} - ${status} - ${error} - ${exception.message}`,
      );
    }

    response.status(status).json(errorDetails);
  }

  private mapPrismaError(
    exception:
      | PrismaClientKnownRequestError
      | PrismaClientValidationError
      | PrismaClientUnknownRequestError
      | PrismaClientInitializationError
      | PrismaClientRustPanicError,
  ): { status: HttpStatus; message: string; error: string } {
    if (exception instanceof PrismaClientKnownRequestError) {
      return this.mapKnownRequestError(exception);
    }

    if (exception instanceof PrismaClientValidationError) {
      return {
        status: HttpStatus.BAD_REQUEST,
        message: 'Invalid request data',
        error: 'ValidationError',
      };
    }

    if (exception instanceof PrismaClientUnknownRequestError) {
      return {
        status: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Unknown database error occurred',
        error: 'UnknownRequestError',
      };
    }

    if (exception instanceof PrismaClientInitializationError) {
      return {
        status: HttpStatus.SERVICE_UNAVAILABLE,
        message: 'Database connection failed',
        error: 'InitializationError',
      };
    }

    if (exception instanceof PrismaClientRustPanicError) {
      return {
        status: HttpStatus.INTERNAL_SERVER_ERROR,
        message: 'Internal database engine error',
        error: 'RustPanicError',
      };
    }

    // Fallback for unknown Prisma errors
    return {
      status: HttpStatus.INTERNAL_SERVER_ERROR,
      message: 'Database error occurred',
      error: 'PrismaError',
    };
  }

  private mapKnownRequestError(exception: PrismaClientKnownRequestError): {
    status: HttpStatus;
    message: string;
    error: string;
  } {
    switch (exception.code) {
      case 'P2002':
        return {
          status: HttpStatus.CONFLICT,
          message: 'A record with this data already exists',
          error: 'UniqueConstraintViolation',
        };

      case 'P2014':
        return {
          status: HttpStatus.BAD_REQUEST,
          message: 'Required relation is missing',
          error: 'RequiredRelationViolation',
        };

      case 'P2003':
        return {
          status: HttpStatus.BAD_REQUEST,
          message: 'Foreign key constraint failed',
          error: 'ForeignKeyConstraintViolation',
        };

      case 'P2025':
        return {
          status: HttpStatus.NOT_FOUND,
          message: 'Record not found',
          error: 'RecordNotFound',
        };

      case 'P2016':
        return {
          status: HttpStatus.BAD_REQUEST,
          message: 'Query interpretation error',
          error: 'QueryInterpretationError',
        };

      case 'P2017':
        return {
          status: HttpStatus.BAD_REQUEST,
          message: 'Records are not connected',
          error: 'RecordsNotConnected',
        };

      case 'P1001':
        return {
          status: HttpStatus.SERVICE_UNAVAILABLE,
          message: 'Cannot connect to database server',
          error: 'DatabaseConnectionError',
        };

      case 'P1008':
        return {
          status: HttpStatus.REQUEST_TIMEOUT,
          message: 'Database operation timed out',
          error: 'OperationTimeout',
        };

      case 'P1017':
        return {
          status: HttpStatus.SERVICE_UNAVAILABLE,
          message: 'Database server is not running',
          error: 'DatabaseNotFound',
        };

      default:
        return {
          status: HttpStatus.INTERNAL_SERVER_ERROR,
          message: `Database error: ${exception.code}`,
          error: 'DatabaseError',
        };
    }
  }
}
