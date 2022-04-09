import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfrmComponent } from './confrm.component';

describe('ConfrmComponent', () => {
  let component: ConfrmComponent;
  let fixture: ComponentFixture<ConfrmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConfrmComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfrmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
