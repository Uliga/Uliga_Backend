export enum TokenType {
  ACCESS,
  REFRESH,
}

export const TOKEN_ISSUER = 'uliga-api';

export interface TokenPayload {
  sub: string;
  iss: string;
  type: TokenType;
}
