import {Injectable} from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable()
export class Config {
    public BASEURI = "http://192.168.56.1:8090/alunos/resources/alunos/";
    public TOKEN = 'token_portal';

    constructor() {
        if (environment.production) {
            this.BASEURI = "http://192.168.99.100:8090/alunos/resources/alunos/";
        }
    }
}