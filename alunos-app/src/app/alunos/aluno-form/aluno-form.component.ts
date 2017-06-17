import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute} from '@angular/router';


import { Aluno } from '../aluno';
import { AlunosService } from '../alunos.service';

@Component({
    selector: 'aluno-form',
    templateUrl: './aluno-form.component.html',
    providers: [AlunosService]
})

export class AlunoFormComponent implements OnInit {

  private sub: any;
  isLoading: boolean = false;
  showMessage: boolean = false;
  itemEdit: Aluno;
  message: String = '';
  private classMessage = 'bg-info';

  constructor(private activateRoute: ActivatedRoute,
                private service: AlunosService) {
   }

  ngOnInit() {
      let id: number;
      this.itemEdit = new Aluno();
      this.sub = this.activateRoute.params.subscribe(params => {
            id = params['id'];            
        });
      if (id != null) {
          this.getDadosRegistro(id);
      }      
  }

    private getDadosRegistro(id: number) {
        this.service.getAluno(id)
            .subscribe(t => this.itemEdit = t,
            error => {
                this.message = <any>error;
                this.showMessage = true;
                this.classMessage = 'bg-warning'}
            )
    }

    onSalvar() {
        this.isLoading = true;
        this.service.salvar(this.itemEdit)
                            .subscribe(
                                data => {
                                    this.message = 'Registro salvo com sucesso';
                                    this.classMessage = 'bg-success',
                                    this.showMessage = true
                                    this.itemEdit = new Aluno();
                                },
                                err => {
                                    this.classMessage = 'bg-warning', 
                                    this.message = <any>err,
                                    this.showMessage = true
                                }
                            );
    }

    getClassMessage() {
        return this.classMessage;
    }
  

}