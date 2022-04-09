import { Component, HostListener, OnDestroy, ViewChild } from '@angular/core';
import { OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { PayGovService } from './pay-gov.service';
import { FormGroup, FormControl, Validators, FormBuilder, FormArray } from '@angular/forms';
import { Payment } from 'app/entities/payment/payment.model';
import { ConfrmService } from '../confrm/confrm.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'jhi-pay-gov',
  templateUrl: './pay-gov.component.html',
  styleUrls: ['./pay-gov.component.scss'],
})
export class PayGovComponent implements OnInit, OnDestroy {
  flag = 'false';
  form: any;
  popup: any;
  @ViewChild('mymo') modl: any;
  mysub: Subscription | undefined;

  private payment: Payment[] | undefined;
  // eslint-disable-next-line @typescript-eslint/member-ordering

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private confServ: ConfrmService,
    private modalService: NgbModal,

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    private paygovServ: PayGovService
  ) {
    this.form = new FormGroup({
      cik: new FormControl('', Validators.compose([Validators.required, Validators.pattern('^[a-zA-Z0-9]+[@&%$#*^]+$')])),
      ccc: new FormControl(
        '',
        Validators.compose([
          Validators.required,
          // eslint-disable-next-line no-useless-escape
          Validators.min(100000),
        ])
      ),
      paymentAmount: new FormControl('', Validators.required),
      name: new FormControl('', Validators.required),
      email: new FormControl(
        '',
        Validators.compose([Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')])
      ),
      phone: new FormControl('', Validators.compose([Validators.required, Validators.pattern('^[0-9]{10}$')])),
    });
  }

  ngOnInit(): void {
    this.passdata();
    this.sendFD();

    const formValues = sessionStorage.getItem('frm');

    if (formValues) {
      this.applyFormValues(this.form, JSON.parse(formValues));
    }

    this.form.valueChanges
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      .subscribe((frm: any) => {
        sessionStorage.setItem('frm', JSON.stringify(frm));
      });
  }

  closeWin(): void {
    window.open('about:blank', '_self');
    window.close();
  }

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  ngOnDestroy() {
    // eslint-disable-next-line no-console
    console.log('deystroyed');
  }

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  private applyFormValues(group: any, formValues: any) {
    Object.keys(formValues).forEach(key => {
      const formControl = <FormControl>group.controls[key];

      if (formControl instanceof FormGroup) {
        this.applyFormValues(formControl, formValues[key]);
      } else {
        formControl.setValue(formValues[key]);
      }
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  passdata(): void {
    const pdata: any = this.form.value;

    this.paygovServ.setOption(pdata);
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  sendFD(): void {
    const fData = this.form.value;

    this.confServ.setFormD(fData);
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  saveCall(): void {
    const data: any = this.form.value;

    this.router.navigate(['/pay-gov-confrm'], {
      // queryParams:{data:JSON.stringify(data)}
    });
  }
}
