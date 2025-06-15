export const ACCEPT_HEADER = 'Accept';
export const CONTENT_TYPE_HEADER = 'Content-Type';
export const X_CSRF_TOKEN_HEADER = 'X-CSRF-Token';
export const ACCEPT_VERSION_HEADER = 'Accept-Version';
export const CONTENT_LENGTH_HEADER = 'Content-Length';
export const CONTENT_MD5_HEADER = 'Content-MD5';
export const DATE_HEADER = 'Date';
export const AUTHORIZATION_HEADER = 'Authorization';
export const USER_AGENT_HEADER = 'user-agent';
export const CLIENT_TYPE_HEADER = 'x-client-type';
export const SESSION_ID_HEADER = 'x-session-id';

export const ALLOWED_HEADERS = [
  ACCEPT_HEADER,
  ACCEPT_VERSION_HEADER,
  CONTENT_LENGTH_HEADER,
  CONTENT_MD5_HEADER,
  CONTENT_TYPE_HEADER,
  DATE_HEADER,
  X_CSRF_TOKEN_HEADER,
  AUTHORIZATION_HEADER,
  USER_AGENT_HEADER,
  CLIENT_TYPE_HEADER,
  SESSION_ID_HEADER,
].join(', ');
