import { TestBed, inject } from '@angular/core/testing';

import { AlunosListService } from './alunos-list.service';

describe('AlunosListService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AlunosListService]
    });
  });

  it('should ...', inject([AlunosListService], (service: AlunosListService) => {
    expect(service).toBeTruthy();
  }));
});
