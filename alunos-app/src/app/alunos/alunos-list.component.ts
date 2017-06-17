import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AlunosService } from './alunos.service';
import { Aluno } from './aluno';
import { Config } from './common/config';

@Component({
    selector: 'alunos-list',
    styleUrls: ['alunos-list.component.css'],
    providers: [AlunosService],
    templateUrl: './alunos-list.component.html'
})
export class AlunosListComponent {

    alunos: Aluno[];
    errorMessage: string;
    pesquisa: string;

    constructor(private _service: AlunosService,
                private _router: Router,
                private _config: Config){}


    private getAlunos() {
		this._service.getAlunos()
			.subscribe(alunos => this.alunos = alunos,
			error => this.errorMessage = <any>error);
    }

    ngOnInit(): void {
        this.getAlunos();
	}

    onSelect(id) {    
        this._router.navigate( ["alunos/form/", id] );
    }

    onPesquisar() {
        if(this.pesquisa == null || this.pesquisa === '') {
            this.getAlunos();
        } else {
            this.alunos = this.alunos.filter(t => t.nome.startsWith(this.pesquisa));
        } 
    }

}
