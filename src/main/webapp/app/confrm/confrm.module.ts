import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { confRoute } from './confirm.route';
import { ConfrmComponent } from './confrm.component';

@NgModule({
  declarations: [ConfrmComponent],
  imports: [CommonModule, SharedModule, RouterModule.forChild([confRoute])],
})
export class ConfrmModule {}
