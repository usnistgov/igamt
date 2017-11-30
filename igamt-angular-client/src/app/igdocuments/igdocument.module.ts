import {NgModule, CUSTOM_ELEMENTS_SCHEMA}     from '@angular/core';
import {CommonModule} from '@angular/common';
import { FormsModule } from '@angular/forms';
import {IgDocumentRoutingModule} from './igdocument-routing.module';
import {TabMenuModule} from 'primeng/primeng';

@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		TabMenuModule,
		IgDocumentRoutingModule
	],
  schemas : [ CUSTOM_ELEMENTS_SCHEMA ],
	declarations: []
})
export class IgDocumentModule {}