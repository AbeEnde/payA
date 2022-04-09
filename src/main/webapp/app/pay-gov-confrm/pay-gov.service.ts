import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IPayment, getPaymentIdentifier } from './payment';
import { Imock, Ipay } from './payment';
import { finalize, share, tap } from 'rxjs/operators';

export type EntityResponseType = HttpResponse<IPayment>;
export type EntityArrayResponseType = HttpResponse<IPayment[]>;

@Injectable({ providedIn: 'root' })
export class PaymentService {
  text = 'hello Abe I alredy returned';
  data: any;
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments');
  protected mockResourceUrl = this.applicationConfigService.getEndpointFor('api/mock');
  protected redirectUrl = this.applicationConfigService.getEndpointFor('api/redirect');
  protected payUrl = this.applicationConfigService.getEndpointFor('api/pay');
  protected payPalUrl = this.applicationConfigService.getEndpointFor('api/payPal');
  protected payUrlM = this.applicationConfigService.getEndpointFor('api/callService');
  protected mrg = this.applicationConfigService.getEndpointFor('api/merge');

  private cache: any;

  // eslint-disable-next-line @typescript-eslint/member-ordering
  cachedObservable: any;
  // eslint-disable-next-line @typescript-eslint/member-ordering
  cachedObservablePay: any;

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(payment: IPayment): Observable<EntityResponseType> {
    return this.http.post<IPayment>(this.resourceUrl, payment, { observe: 'response' });
  }

  update(payment: IPayment): Observable<EntityResponseType> {
    return this.http.put<IPayment>(`${this.resourceUrl}/${getPaymentIdentifier(payment) as number}`, payment, { observe: 'response' });
  }

  fetchFromBack(): any {
    return this.http.get<Imock>(`${this.mockResourceUrl}`, { observe: 'response' });
  }

  fetchRedirect(): Observable<any> {
    let observable: Observable<any>;
    if (this.cache) {
      observable = of(this.cache);
    } else if (this.cachedObservable) {
      observable = this.cachedObservable;
    } else {
      this.cachedObservable = this.http.get<Ipay>(`${this.redirectUrl}`, { observe: 'response' }).pipe(
        // eslint-disable-next-line @typescript-eslint/no-unsafe-return
        tap(res => (this.cache = res)),
        share(),
        finalize(() => (this.cachedObservable = null))
      );
      observable = this.cachedObservable;
    }

    return observable;
  }

  send(pay: string): Observable<EntityResponseType> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return this.http.post<any>(this.payUrl, pay, { observe: 'response' });
  }
  passMessage(pay: any): Observable<EntityResponseType> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return this.http.post<any>(this.payUrlM, pay, { observe: 'response' });
  }
  callMerge(): any {
    return this.http.get<any>(`${this.mrg}`, { observe: 'response' });
    // return of(null);
  }
  /*
send(pay:IPayment): Observable<EntityResponseType>{
	
  let observable: Observable<any>;
  if (this.cachepay) {
    observable = of(this.cachepay);
// return observable;
  }  else if (this.cachedObservablePay) {
    observable = this.cachedObservablePay;
// return observable;
  } else {
   this.cachedObservablePay = this.http.post<IPayment>(this.payUrl, pay, { observe: 'response' })
      .pipe(
        // eslint-disable-next-line @typescript-eslint/no-unsafe-return
        tap(res => this.cachepay = res),
       share(),
        finalize(() => this.cachedObservablePay = null)
     );
    observable = this.cachedObservablePay;
  }
  	
// eslint-disable-next-line @typescript-eslint/no-unsafe-return
return observable;
}
*/
  fetchToken(): any {
    return this.http.get<any>(`${this.payPalUrl}`, { observe: 'response' });
    // return of(null);
  }
}
