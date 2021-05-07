import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { ClientePage } from './cliente.page';

import { ClienteRoutingModule } from './cliente.routing.module';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ClienteRoutingModule
  ],
  declarations: [ClientePage]
})
export class ClienteModule {}