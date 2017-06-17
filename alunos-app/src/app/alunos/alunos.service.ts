import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable'

import 'rxjs';

import { Aluno } from './aluno';
import { Config } from './common/config';

@Injectable()
export class AlunosService { 

  constructor(private _http: Http,
  				private _config: Config) {}


  	getAlunos(): Observable<Aluno[]> {
		return this._http.get(this._config.BASEURI)
			.map((response: Response) => <Aluno[]>response.json())
			.do(data => console.log("All: " + JSON.stringify(data)))
			.catch(this.handleError);
	}

	getAluno(id: number): Observable<Aluno> {
		return this._http.get(this._config.BASEURI+id)
			.map((response: Response) => <Aluno>response.json())
			.do(data => console.log("Aluno:" + JSON.stringify(data)))
			.catch(this.handleError);

	}

	salvar(aluno: Aluno): Observable<Aluno> {
		var headers = new Headers();
		headers.append('Content-Type', 'application/json');

		if(aluno.id == null) {
			return this._http.post(this._config.BASEURI, JSON.stringify(aluno), {headers: headers})
            	.map((response: Response) => <Aluno>response.json())
            	.catch(this.handleError);
		} else {
			return this._http.put(this._config.BASEURI+"id", JSON.stringify(aluno), {headers: headers})
            	.map((response: Response) => <Aluno>response.json())
            	.catch(this.handleError);
		}		
	}
	
	private handleError(error: Response) {
		console.error(error);
		return Observable.throw(error.json().error || error.text());
	}

}
