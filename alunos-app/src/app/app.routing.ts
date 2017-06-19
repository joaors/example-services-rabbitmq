import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { AlunoFormComponent } from './alunos/aluno-form/aluno-form.component';
import { AlunosListComponent } from './alunos/alunos-list.component';
import { AuthGuard } from './auth/auth.guard';
import { LoginComponent } from './login/login.component';

const APP_ROUTES: Routes = [
    { 
        path: 'alunos', 
        component: AlunosListComponent,
        canActivate: [AuthGuard]
    },
    { 
        path: 'alunos/form/:id', 
        component: AlunoFormComponent,
        canActivate: [AuthGuard]
    },
    { 
        path: 'alunos/form', 
        component: AlunoFormComponent,
        canActivate: [AuthGuard]
    },
    { 
        path: 'alunos/login', 
        component: LoginComponent
    },    
    { 
        path: '', 
        component: LoginComponent
    },
     { 
        path: 'alunos/home', 
        component: HomeComponent,
        canActivate: [AuthGuard] 
    }   
];

export const routing: ModuleWithProviders = RouterModule.forRoot(APP_ROUTES);