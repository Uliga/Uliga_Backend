import { Request } from 'express';
import { SESSION_ID_HEADER } from '../constants/http-allowed-headers.constant';

export function getSessionIdFromCookies(req: Request): string | undefined {
  const cookies = req.cookies as Record<string, unknown> | undefined;

  if (!cookies || typeof cookies !== 'object') {
    return undefined;
  }

  const sessionId = cookies.sessionId;

  if (typeof sessionId === 'string' && sessionId.length > 0) {
    return sessionId;
  }

  return undefined;
}

export function getSessionId(req: Request): string | undefined {
  const headerSessionId = req.headers[SESSION_ID_HEADER] as string;

  if (
    headerSessionId &&
    typeof headerSessionId === 'string' &&
    headerSessionId.length > 0
  ) {
    return headerSessionId;
  }

  return getSessionIdFromCookies(req);
}
