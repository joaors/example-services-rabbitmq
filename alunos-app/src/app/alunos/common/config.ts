import {Injectable} from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable()
export class Config {
    public BASEURI = "http://localhost:8090/bridge/resources/alunos/";//"http://192.168.56.1:8090/bridge/resources/alunos/";

    constructor() {
        if (environment.production) {
            this.BASEURI = "http://192.168.99.100:8090/bridge/resources/alunos/";
        }
    }
}