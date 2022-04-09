import { Component, ViewEncapsulation, ElementRef, Input, OnInit, OnDestroy, SecurityContext, ViewChild } from '@angular/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { PaymentService } from '../pay-gov-confrm/pay-gov.service';
import { DomSanitizer } from '@angular/platform-browser';
import { PayGovService } from '../pay-gov/pay-gov.service';
import { ConfrmService } from '../confrm/confrm.service';
import { IPayment } from 'app/pay-gov-confrm/payment';
import { finalize } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ModalComponent implements OnInit {
  @ViewChild('mymo') mo: any;
  @ViewChild('mymodal') mod: any;
  returnedc: any;
  pay: any;
  fullRedirectUrl: any;
  fUrl: any;
  title = 'appBootstrap';
  closeResult = 'ttttt';
  protocol = 'https://payment.';
  pUrl: any;
  itemsDetails: any[] | undefined;
  isSaving = false;

  constructor(
    private modalService: NgbModal,
    protected paymentService: PaymentService,
    protected sanitizer: DomSanitizer,
    private payServ: PayGovService,
    private confServ: ConfrmService
  ) {}

  ngOnInit(): void {
    setInterval(() => {
      this.confServ.fetchUpdatedQuee().subscribe((result: any) => {
        this.itemsDetails = result.body;
        // eslint-disable-next-line no-console
        console.log('Received Data ========  ', result);
      });
    }, 1000);
  }

  delete(pay: any): void {
    this.confServ.callDelete(pay).subscribe((res: any) => {
      // eslint-disable-next-line no-console
      console.log('$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$');
      // eslint-disable-next-line no-console
      console.log(res);
    });
  }
  save(pay: any): void {
    this.isSaving = true;
    const payment = pay;
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
    // eslint-disable-next-line no-console
    console.log('Received Data ======== ============= ');
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
  }

  protected onSaveError(): void {
    this.openE();
  }
  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected openM(): void {
    this.modalService.open(this.mod);
  }

  protected openE(): void {
    this.modalService.open(this.mo);
  }
}
