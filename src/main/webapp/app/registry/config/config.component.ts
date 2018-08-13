import {Component, OnDestroy, OnInit} from '@angular/core';
import {JhiConfigService} from './config.service';
import {ProfileService} from 'app/layouts/profiles/profile.service';
import {JhiApplicationsService} from '../';
import {JhiRefreshService} from 'app/shared/refresh/refresh.service';
import {Subscription} from 'rxjs/Subscription';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiPopupModalComponent} from 'app/registry';

@Component({
    selector: 'jhi-config',
    templateUrl: './config.component.html'
})
export class JhiConfigComponent implements OnInit, OnDestroy {
    application: string;
    profile: string;
    label: string;
    content: string;
    activeRegistryProfiles: any;
    isNative: boolean;
    configurationSources: Array<any>;
    configAsYaml: any;
    configAsProperties: any;
    configAsJson: any;
    configAsKeyValuePairs: any;

    refreshReloadSubscription: Subscription;

    constructor(
        private configService: JhiConfigService,
        private profileService: ProfileService,
        private applicationsService: JhiApplicationsService,
        private refreshService: JhiRefreshService,
        private modalService: NgbModal
    ) {
        this.application = '';
        this.profile = '';
        this.label = 'master';
        this.content = '';
        this.activeRegistryProfiles = [];
        this.isNative = true;
    }

    ngOnInit() {
        this.load();
        this.refresh();
    }

    ngOnDestroy() {
        this.refreshReloadSubscription.unsubscribe();
    }

    load() {
        this.profileService.getProfileInfo().then((response) => {
            this.activeRegistryProfiles = response.activeProfiles;
            this.isNative = this.activeRegistryProfiles.includes('native');
            this.configurationSources = response.configurationSources;
        });

        this.refreshReloadSubscription = this.refreshService.refreshReload$.subscribe((empty) => this.refresh());
    }

    refresh() {
        this.configService.getConfigAsYaml(this.application, this.profile, this.label).subscribe(
            (response) => {
                this.configAsYaml = response;
            },
            (error) => {
                this.configAsYaml = '';
            }
        );

        this.configService.getConfigAsProperties(this.application, this.profile, this.label).subscribe(
            (response) => {
                this.configAsProperties = response;

                const keyValueArray = [];
                this.configAsProperties.split('\n').forEach((property) => {
                    const keyValueSplit = property.split(': ');
                    keyValueArray.push({key: keyValueSplit[0], value: keyValueSplit[1]});
                });
                this.configAsKeyValuePairs = keyValueArray;
            },
            (error) => {
                this.configAsProperties = '';
            }
        );

        this.configService.getConfigForEdit(this.application, this.profile).subscribe(
            (response) => {
                this.content = response;
            },
            (error) => {
                this.content = 'example.property=value';
            }
        );

        this.configService.getConfigAsJson(this.application, this.profile, this.label).subscribe(
            (response) => {
                this.configAsJson = response;
            },
            (error) => {
                this.configAsJson = '';
            }
        );
    }

    onKeyDown($event): void {
        const charCode = String.fromCharCode($event.which).toLowerCase();
        // Action on Alt + S
        if ($event.altKey && charCode === 's') {
            const modalRef = this.modalService.open(JhiPopupModalComponent, {size: 'sm'});
            this.configService.saveConfig(this.application, this.profile, this.content).subscribe(
                (response) => {
                    modalRef.componentInstance.message = `Success`;
                    modalRef.result.then((result) => {
                            // Left blank intentionally, nothing to do here
                        },
                        (reason) => {
                            // Left blank intentionally, nothing to do here
                        });
                    this.refresh();
                },
                () => {
                    modalRef.componentInstance.message = `Failed save config`;
                    modalRef.result.then((result) => {
                            // Left blank intentionally, nothing to do here
                        },
                        (reason) => {
                            // Left blank intentionally, nothing to do here
                        });
                }
            );
        }
    }

    getKeys(obj: Object) {
        return Object.keys(obj);
    }
}
