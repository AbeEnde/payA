import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
@Injectable({
  providedIn: 'root',
})
export class ConfrmService {
  data: any;
  protected paypalDetail = this.applicationConfigService.getEndpointFor('api/payPalDetail');
  protected paypalDo = this.applicationConfigService.getEndpointFor('api/payPalDo');
  protected sock = this.applicationConfigService.getEndpointFor('api/message');
  protected CallS = this.applicationConfigService.getEndpointFor('api/callSave');
  protected delet = this.applicationConfigService.getEndpointFor('api/delete');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  setFormD(val: any): void {
    this.data = val;
  }

  getFormD(): any {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return this.data;
  }

  fetchDetailPay(): any {
    return this.http.get<any>(`${this.paypalDetail}`, { observe: 'response' });
    // return of(null);
  }

  fetchStatusPay(): any {
    return this.http.get<any>(`${this.paypalDo}`, { observe: 'response' });
    // return of(null);
  }
  fetchUpdatedQuee(): any {
    return this.http.get<any>(`${this.sock}`, { observe: 'response' });
    // return of(null);
  }
  callSave(pay: any): any {
    return this.http.post<any>(this.CallS, pay, { observe: 'response' });
    // return of(null);
  }
  callDelete(pay: any): any {
    return this.http.post<any>(this.delet, pay, { observe: 'response' });
    // return of(null);
  }
}
