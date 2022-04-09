import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { proute } from './pay-gov.route';

@NgModule({
  declarations: [],
  imports: [CommonModule, SharedModule, RouterModule.forChild(proute)],
})
export class PayGovModule {}
