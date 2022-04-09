import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IPayment } from 'app/pay-gov-confrm/payment';
import { Observable, Subscription, timer } from 'rxjs';
import { finalize, map, take } from 'rxjs/operators';
import { PaymentService } from '../pay-gov-confrm/pay-gov.service';
import { PayGovService } from '../pay-gov/pay-gov.service';
import { ConfrmService } from './confrm.service';
import { ActivatedRoute, Router } from '@angular/router';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Component({
  selector: 'jhi-confrm',
  templateUrl: './confrm.component.html',
  styleUrls: ['./confrm.component.scss'],
})
export class ConfrmComponent implements OnInit {
  isSaving = false;
  payRaw: any;
  payJs: any;
  pay: any;
  det: any;
  counter$: Observable<number>;
  count = 5;

  itemsDetails: any[] | undefined;
  mysub: Subscription | undefined;
  @ViewChild('mymo') mo: any;
  @ViewChild('mymodal') mod: any;
  @ViewChild('mymodet') modal: any;

  webSocketEndPoint = this.applicationConfigService.getEndpointFor('api/ws');
  topic = 'queue';
  stompClient: any;
  recivedM: any;
  _this: any;
  httpOptions: any = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Headers': 'Content-Type',
      'Access-Control-Allow-Methods': 'GET',
      'Access-Control-Allow-Origin': '*',
    }),
  };

  constructor(
    private paymentService: PaymentService,
    private modalService: NgbModal,
    private paygovServ: PayGovService,
    private confServ: ConfrmService,
    private route: Router,
    private applicationConfigService: ApplicationConfigService
  ) {
    this.counter$ = timer(0, 1000).pipe(
      take(this.count),
      map(() => --this.count)
    );
  }

  // eslint-disable-next-line @angular-eslint/no-empty-lifecycle-method
  ngOnInit(): void {
    // this.pay = this.confServ.getFormD();

    this.payRaw = localStorage.getItem('myData');
    // this.pay = localStorage.getItem('myData');
    this.payJs = JSON.parse(JSON.stringify(this.payRaw));
    this.pay = JSON.parse(this.payJs);
    // this.openM();
    // eslint-disable-next-line no-console
    // console.log(this.pay);
    localStorage.removeItem('myData');

    this.confServ.fetchDetailPay().subscribe((detail: any) => {
      this.det = detail;

      /* this.opendetail();
      // eslint-disable-next-line no-console
      //  console.log(this.det); */
    });

    this.confServ.fetchStatusPay().subscribe((stat: any) => {
      const status = stat;
    });

    this.confServ.callSave(this.pay).subscribe((res: any) => {
      const result = res;
      if (result.body.status === 'Success') {
        this.openM();
        sessionStorage.removeItem('frm');
      } else {
        this.openE();
      }
      // eslint-disable-next-line no-console
      console.log(' WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW');
      // eslint-disable-next-line no-console
      console.log(res);
    });
    /*   setInterval(() => {
      this.confServ.fetchUpdatedQuee().subscribe((result:any)=>{
          //this.pay = result;
          this.itemsDetails = result.body;
          // eslint-disable-next-line no-console
          console.log("Received Data ========  ",result);
      });
  },1000) */
  }

  getStat(): any {
    this.confServ.fetchStatusPay().subscribe((stat: any) => {
      const status = stat;
      // eslint-disable-next-line no-console
      console.log(status);
    });
  }
  opendetail(): void {
    this.modalService.open(this.modal);
  }

  openM(): void {
    this.mysub?.unsubscribe();
    this.modalService.open(this.mod);
  }

  openE(): void {
    // this.mysub?.unsubscribe();
    this.modalService.open(this.mo);
  }

  getData(): void {
    this.pay = this.confServ.getFormD();
  }
  save(): void {
    this.isSaving = true;
    const payment = this.pay;
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.openM();
    sessionStorage.removeItem('frm');
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
    this.previousState();
    this.openE();
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  previousState(): void {
    // window.history.back();
    this.route.navigate(['/']);
  }
}
