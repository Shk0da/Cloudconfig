import {LOCALE_ID, NgModule} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {GroupByPipe} from './pipe/group-by.pipe';
import {registerLocaleData} from '@angular/common';
import locale from '@angular/common/locales/ru';
import {
    JhiAlertComponent,
    JhiAlertErrorComponent,
    JHipsterRegistrySharedLibsModule,
    JhiRefreshSelectorComponent,
    JhiRouteSelectorComponent
} from './';

@NgModule({
    imports: [JHipsterRegistrySharedLibsModule],
    declarations: [
        JhiAlertComponent,
        JhiAlertErrorComponent,
        JhiRouteSelectorComponent,
        JhiRefreshSelectorComponent,
        GroupByPipe
    ],
    providers: [
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'ru-RU'
        }
    ],
    exports: [
        JHipsterRegistrySharedLibsModule,
        JhiAlertComponent,
        JhiAlertErrorComponent,
        JhiRouteSelectorComponent,
        JhiRefreshSelectorComponent,
        GroupByPipe
    ]
})
export class JHipsterRegistrySharedCommonModule {
    constructor() {
        registerLocaleData(locale);
    }
}
