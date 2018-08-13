import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class JhiConfigService {
    constructor(private http: HttpClient) {}

    getConfigAsYaml(application: string, profile: string, label: string): Observable<any> {
        return this.http.get('config/' + label + '/' + application + '-' + profile + '.yml', { responseType: 'text' });
    }

    getConfigAsProperties(application: string, profile: string, label: string): Observable<any> {
        return this.http.get('config/' + label + '/' + application + '-' + profile + '.properties', { responseType: 'text' });
    }

    getConfigAsJson(application: string, profile: string, label: string): Observable<any> {
        return this.http.get('config/' + label + '/' + application + '-' + profile + '.json', { responseType: 'text' });
    }

    getConfigForEdit(application: string, profile: string): Observable<any> {
        return this.http.get('api/config/content?application=' + application + '&profile=' + profile, { responseType: 'text' });
    }

    saveConfig(application: string, profile: string, content: string): Observable<HttpResponse<any>> {
        return this.http.post('api/config/content?application=' + application + '&profile=' + profile, content, { observe: 'response' });
    }

    getAvailableConfigList(): Observable<any> {
        return this.http.get('api/config/available', { responseType: 'json' });
    }
}
