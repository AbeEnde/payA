import { TestBed } from '@angular/core/testing';

import { PayGovService } from './pay-gov.service';

describe('PayGovService', () => {
  let service: PayGovService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PayGovService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
