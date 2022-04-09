import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IPayment } from 'app/entities/payment/payment.model';
import { Observable, timer, interval, Subscription } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { PaymentService } from './pay-gov.service';
import { FormBuilder } from '@angular/forms';
import { PayGovService } from '../pay-gov/pay-gov.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { ViewChild } from '@angular/core';
import { take, map } from 'rxjs/operators';
import { DomSanitizer } from '@angular/platform-browser';
import { ConfrmService } from '../confrm/confrm.service';

@Component({
  selector: 'jhi-pay-gov-confrm',
  templateUrl: './pay-gov-confrm.component.html',
  styleUrls: ['./pay-gov-confrm.component.scss'],
})
export class PayGovConfrmComponent implements OnInit, OnDestroy {
  @ViewChild('mymodal') mod: any;
  @ViewChild('mymo') modl: any;
  timeFlag = true;
  fb: FormBuilder | undefined;
  data: any;
  mockData: any;
  mymoFlag = false;
  mymoValue = 'mymode';
  tracing_id: any;
  name: any;
  email: any;
  address: any;
  creditCard: any;
  loading = true;
  mysub: Subscription | undefined;

  isSaving = false;
  pay: any;
  id = 'amid';
  title = 'appBootstrap';
  closeResult = 'ttttt';
  counter$: Observable<number> | undefined;
  formV: any;
  count = 10;
  tick = 1000;
  returnedc: any;
  fullRedirectUrl: any;
  pUrl: any;
  protocol = 'https://payment.';
  fUrl: any;
  reteurnedToken: any;
  jstoken: any;
  msg: any;
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments');
  // eslint-disable-next-line @typescript-eslint/member-ordering
  payment: IPayment | null = null;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private router: Router,
    private paygovServ: PayGovService,
    private route: ActivatedRoute,
    private confService: ConfrmService,
    private modalService: NgbModal,
    protected http: HttpClient,
    protected paymentService: PaymentService,
    protected applicationConfigService: ApplicationConfigService,
    protected sanitizer: DomSanitizer
  ) {
    this.counter$ = timer(0, 1000).pipe(
      take(this.count),
      map(() => --this.count)
    );
  }

  ngOnInit(): void {
    this.formV = sessionStorage.getItem('frm');

    this.getData();
    this.pay = JSON.parse(this.formV);

    this.paymentService.passMessage(this.pay).subscribe((messg: any) => {
      // eslint-disable-next-line no-console
      console.log(messg);
    });
    this.paymentService.callMerge().subscribe((messg: any) => {
      this.msg = messg.body;
      // eslint-disable-next-line no-console
      console.log('&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&');
      // eslint-disable-next-line no-console
      console.log(this.msg);
    });

    // eslint-disable-next-line no-console
    console.log(this.pay);

    this.paymentService.send(this.pay.paymentAmount).subscribe((messg: any) => {
      // eslint-disable-next-line no-console
      console.log(messg);
    });

    this.paymentService.fetchToken().subscribe((token: any) => {
      this.reteurnedToken = token;
      const stringified = JSON.stringify(this.reteurnedToken);
      const parsedObj = JSON.parse(stringified);

      this.pUrl = 'https://www.sandbox.paypal.com/webscr?cmd=_express-checkout&useraction=commit&token=';
      this.fUrl = this.pUrl.concat(this.reteurnedToken.body);
      // eslint-disable-next-line no-console
      console.log(this.reteurnedToken.body);
    });

    this.mysub = interval(10000).subscribe(x => {
      this.loading = false;

      if (this.msg == null) {
        this.openE();
      } else {
        this.fetchPop();
      }
    });

    this.setData(this.pay);
  }

  getpredurl(): void {
    const protocol = 'https://payment.';
    const pUrl = this.returnedc.body.partialRedirectUrl;
    const fUrl = protocol.concat(pUrl);
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands

    this.fullRedirectUrl = this.sanitizer.bypassSecurityTrustResourceUrl(fUrl);
  }

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  ngOnDestroy() {
    this.mysub?.unsubscribe();
  }

  fetchPop(): void {
    window.location.href = this.fUrl;

    this.mysub?.unsubscribe();
  }

  openE(): void {
    this.modalService.open(this.modl);
  }

  getData(): void {
    this.pay = this.paygovServ.getOption();
  }

  setData(data: any): void {
    const jsonData = JSON.stringify(data);
    localStorage.setItem('myData', jsonData);
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  previousState(): void {
    window.history.back();
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      // eslint-disable-next-line @typescript-eslint/restrict-template-expressions
      return `with: ${reason}`;
    }
  }
}

@Component({
  selector: 'jhi-my-modal',
  template: `
    <div class="modal-header">
      <h4 class="modal-title"><strong>Confirmation Message</strong></h4>
      <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>

    <div class="modal-body">
      <h2>You payed successfully !</h2>
    </div>

    <div class="modal-footer">
      <button type="button" class="btn btn-outline-dark" (click)="activeModal.close('Close click')">OK</button>
    </div>
  `,
})
export class SuccessComponent {
  data: any;

  constructor(private router: Router, private route: ActivatedRoute, private modalService: NgbModal, public activeModal: NgbActiveModal) {}
}

@Component({
  selector: 'jhi-my-modal',
  template: `
    <div class="modal-header">
      <h4 class="modal-title"><strong>Error Message</strong></h4>
      <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>

    <div class="modal-body">
      <h2>Some thing wnet wrong</h2>
    </div>

    <div class="modal-footer">
      <button type="button" class="btn btn-outline-dark" (click)="activeModal.close('Close click')">OK</button>
    </div>
  `,
})
export class ErrorComponent {
  data: any;

  constructor(private router: Router, private route: ActivatedRoute, private modalService: NgbModal, public activeModal: NgbActiveModal) {}
}
