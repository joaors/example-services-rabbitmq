import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { AlunoFormComponent } from './alunos/aluno-form/aluno-form.component';
import { AlunosListComponent } from './alunos/alunos-list.component';

const APP_ROUTES: Routes = [
    { path: 'alunos', component: AlunosListComponent },
    { path: 'alunos/form/:id', component: AlunoFormComponent },
    { path: 'alunos/form', component: AlunoFormComponent },
    { path: '', component: HomeComponent }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(APP_ROUTES);