import { CoreEnvironment } from '@angular/compiler/src/compiler_facade_interface';
import { Injectable } from '@angular/core';
// import { env } from 'process';

@Injectable({
  providedIn: 'root',
})
export class ApplicationConfigService {
  private endpointPrefix = '';

  private microfrontend = false;

  setEndpointPrefix(endpointPrefix: string): void {
    this.endpointPrefix = endpointPrefix;
  }

  setMicrofrontend(microfrontend = true): void {
    this.microfrontend = microfrontend;
  }

  isMicrofrontend(): boolean {
    return this.microfrontend;
  }

  getEndpointFor(api: string, microservice?: string): string {
    if (microservice) {
      return `${this.endpointPrefix}services/${microservice}/${api}`;
    }
    return `${this.endpointPrefix}${api}`;
  }
}
