import {Component, OnDestroy, OnInit} from '@angular/core';
import {JhiConfigService} from './config.service';
import {JhiConfigComponent} from './config.component';

@Component({
    selector: 'jhi-config-selector',
    templateUrl: './config-selector.component.html',
    styleUrls: ['config-selector.component.scss']
})
export class JhiConfigSelectorComponent implements OnInit, OnDestroy {

    applications: string[];
    profiles: string[];
    application: string;
    profile: string;
    label: string;
    isNative: boolean;

    constructor(private configService: JhiConfigService, private configComponent: JhiConfigComponent) {
        this.applications = [];
        this.profiles = [];
        this.application = configComponent.application;
        this.profile = configComponent.profile;
        this.label = configComponent.label;
        this.isNative = configComponent.isNative;
    }

    ngOnInit() {
        this.load();
    }

    ngOnDestroy() {
    }

    load() {
        const applications = [];
        const profiles = ['prod', 'psi', 'hotfix', 'nt', 'ift', 'st', 'dev'];
        this.configService.getAvailableConfigList().toPromise().then((response) => {
            for (const config of Object.keys(response)) {
                const appToProfile = config.split('-');
                if (appToProfile.length >= 2) {
                    const app = config.substr(0, config.lastIndexOf('-'));
                    if (!applications.includes(app)) {
                        applications.push(app);
                    }
                    const profile = appToProfile[appToProfile.length - 1];
                    if (!profiles.includes(profile)) {
                        profiles.push(profile);
                    }
                }
            }
        });
        this.applications = applications;
        this.profiles = profiles;
    }

    refresh() {
        this.configComponent.application = this.application;
        this.configComponent.profile = this.profile;
        this.configComponent.label = this.label;
        this.configComponent.refresh();
    }
}
