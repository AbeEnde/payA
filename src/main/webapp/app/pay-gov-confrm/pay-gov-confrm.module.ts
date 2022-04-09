import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PayGovConfrmComponent } from './pay-gov-confrm.component';
import { confrmroute } from './pay-gov-confrm.route';
import { SharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [PayGovConfrmComponent],
  imports: [CommonModule, SharedModule, RouterModule.forChild([confrmroute])],
})
export class PayGovConfrmModule {}
