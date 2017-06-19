import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Http } from '@angular/http';
import { Config } from '../alunos/common/config';
import { contentHeaders } from '../alunos/common/headers';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  withErrors: boolean;

  constructor(private router: Router, 
              private http: Http,
              private config: Config) { }

  ngOnInit() {}

  login(event, usuario, senha) {
    event.preventDefault();
    let body = JSON.stringify({ usuario, senha });
    this.http.post(this.config.BASEURI.replace("alunos","autenticacao")+'login', body, { headers: contentHeaders })
      .subscribe(
        response => {
          this._salvarToken(response);
          this.router.navigate(['/alunos/home']);
        },
          error => {this.withErrors = true; console.log(error.text());
        }
      );
  }  

  private _salvarToken(res) {
      if (res.status !== 200) {
          return;
      }
      console.log(res.json().token);
      if (res.json().token) {
          localStorage.setItem('id_token', res.json().token);
      }
  }   

}
