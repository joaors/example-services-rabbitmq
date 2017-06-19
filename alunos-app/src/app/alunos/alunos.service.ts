import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { AuthHttp }     from 'angular2-jwt';

import 'rxjs';

import { Aluno } from './aluno';
import { Config } from './common/config';

@Injectable()
export class AlunosService { 

  constructor(public _authHttp: AuthHttp,
  				private _config: Config) {}


  	getAlunos(): Observable<Aluno[]> {
		return this._authHttp.get(this._config.BASEURI)
			.map((response: Response) => <Aluno[]>response.json())
			.do(data => console.log("All: " + JSON.stringify(data)))
			.catch(this.handleError);
	}

	getAluno(id: number): Observable<Aluno> {
		return this._authHttp.get(this._config.BASEURI+id)
			.map((response: Response) => <Aluno>response.json())
			.do(data => console.log("Aluno:" + JSON.stringify(data)))
			.catch(this.handleError);

	}

	salvar(aluno: Aluno): Observable<Aluno> {
		var headers = new Headers();
		headers.append('Content-Type', 'application/json');

		if(aluno.id == null) {
			return this._authHttp.post(this._config.BASEURI, JSON.stringify(aluno), {headers: headers})
            	.map((response: Response) => <Aluno>response.json())
            	.catch(this.handleError);
		} else {
			return this._authHttp.put(this._config.BASEURI+"id", JSON.stringify(aluno), {headers: headers})
            	.map((response: Response) => <Aluno>response.json())
            	.catch(this.handleError);
		}		
	}
	
	private handleError(error: Response) {
		console.error(error);
		return Observable.throw(error.json().error || error.text());
	}

}
