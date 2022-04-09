import { TestBed } from '@angular/core/testing';

import { ConfrmService } from './confrm.service';

describe('ConfrmService', () => {
  let service: ConfrmService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfrmService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
