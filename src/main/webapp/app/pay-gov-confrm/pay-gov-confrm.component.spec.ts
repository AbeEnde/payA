import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayGovConfrmComponent } from './pay-gov-confrm.component';

describe('PayGovConfrmComponent', () => {
  let component: PayGovConfrmComponent;
  let fixture: ComponentFixture<PayGovConfrmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PayGovConfrmComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayGovConfrmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
