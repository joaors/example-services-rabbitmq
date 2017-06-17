import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { MaterializeModule } from 'angular2-materialize';

import { AppComponent } from './app.component';

import { AlunosListComponent } from './alunos/alunos-list.component';
import { AlunoFormComponent } from './alunos/aluno-form/aluno-form.component';
import { HomeComponent } from './home/home.component';
import { routing } from './app.routing';
import { Config } from './alunos/common/config'

@NgModule({
  declarations: [
    AppComponent,
    AlunosListComponent,
    AlunoFormComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    routing

  ],
  providers: [Config],
  bootstrap: [AppComponent]
})
export class AppModule { }
