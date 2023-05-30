import { TestBed } from '@angular/core/testing';

import { SpringAuthorizationService } from './spring-authorization.service';

describe('SpringAuthorizationService', () => {
  let service: SpringAuthorizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpringAuthorizationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
