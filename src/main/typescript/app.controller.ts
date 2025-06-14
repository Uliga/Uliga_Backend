import { Controller, Get, Res, Version, VERSION_NEUTRAL } from '@nestjs/common';
import { Response } from 'express';

type ServerUptime = {
  name: string;
  uptime: number;
};

@Controller()
export class AppController {
  @Get()
  @Version('1')
  getUptimeV1(): ServerUptime {
    return {
      name: 'uliga-api',
      uptime: process.uptime(),
    };
  }

  @Get()
  @Version('2')
  getUptimeV2(): ServerUptime & { version: string; timestamp: string } {
    return {
      name: 'uliga-api',
      uptime: process.uptime(),
      version: '2.0',
      timestamp: new Date().toISOString(),
    };
  }

  @Get('favicon.ico')
  @Version(VERSION_NEUTRAL)
  getFavicon(@Res() response: Response): void {
    response.status(204).end();
  }
}
