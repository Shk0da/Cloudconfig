import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {CommonModule} from '@angular/common';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NgJhipsterModule} from 'ng-jhipster';
import {InfiniteScrollModule} from 'ngx-infinite-scroll';

@NgModule({
    imports: [NgbModule.forRoot(), NgJhipsterModule.forRoot({noi18nMessage: ''}), InfiniteScrollModule],
    exports: [FormsModule, HttpClientModule, CommonModule, NgbModule, NgJhipsterModule, InfiniteScrollModule]
})
export class JHipsterRegistrySharedLibsModule {
}
