import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  private modals: any;
  private content = 'mymodal';

  open(id: string): void {
    this.modals.open(this.content);
  }
  close(id: string): void {
    this.modals.getDismissReason(this.content);
  }
}
