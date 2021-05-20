import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RegistroPage} from '../registro.page';


const registroroutes: Routes = [
    {
      path: '',
      component: RegistroPage,
    },

    
];

@NgModule({
    imports: [RouterModule.forChild(registroroutes)],
    exports: [RouterModule],
    
})

export class RegistroRoutingModule {}