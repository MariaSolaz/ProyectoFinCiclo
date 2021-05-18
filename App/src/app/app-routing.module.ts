import { importExpr } from '@angular/compiler/src/output/output_ast';
import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
 
  { path: 'tabs', loadChildren: './pages/tabs/tabs.module#TabsPageModule' },
  { path: '', loadChildren: './pages/login/login.module#LoginPageModule' },
  { path: 'signup', loadChildren: './pages/signup/signup.module#SignupPageModule' },
  { path: 'accessdenied', redirectTo: '', pathMatch: 'full' },
  {path: 'cliente', loadChildren: () => import('./pages/entities/Cliente/cliente.module').then(m => m.ClienteModule)},
  {path: 'factura', loadChildren:() => import('./pages/entities/Facturas/factura.module').then(m => m.FacturaModule)},
  {path: 'vehiculo', loadChildren:() => import('./pages/entities/Vehiculos/vehiculo.module').then(m => m.VehiculoModule)},
  {path: 'registro', loadChildren:() => import('./pages/entities/Registro/registro.module').then(m => m.RegistroModule)},

];
@NgModule({
  imports: [RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
