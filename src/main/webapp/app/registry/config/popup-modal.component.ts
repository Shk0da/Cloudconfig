import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-popup-modal',
    templateUrl: './popup-modal.component.html'
})
export class JhiPopupModalComponent {
    message: any;

    constructor(public activeModal: NgbActiveModal) {
    }
}
