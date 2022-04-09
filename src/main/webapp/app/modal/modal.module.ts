import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalComponent } from './modal.component';
import { mdroute } from './modalRoute';
import { SharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [ModalComponent],
  imports: [CommonModule, SharedModule, RouterModule.forChild([mdroute])],
})
export class ModalModule {}
