import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PayGovService {
  data: any;

  setOption(val: any): void {
    this.data = val;
  }

  getOption(): any {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return this.data;
  }
}
