import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {MdButtonModule, MdCardModule, MdIconModule, MdToolbarModule, MdListModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlexLayoutModule} from '@angular/flex-layout';
import {AppComponent} from './app.component';
import {RouterModule} from "@angular/router";
import 'rxjs/Rx';
import 'rxjs/add/operator/map'

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        MdCardModule,
        MdButtonModule,
        MdIconModule,
        MdToolbarModule,
        MdListModule,
        BrowserAnimationsModule,
        FlexLayoutModule,
        RouterModule.forRoot([
            {path: '', component: AppComponent}
        ])

    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
