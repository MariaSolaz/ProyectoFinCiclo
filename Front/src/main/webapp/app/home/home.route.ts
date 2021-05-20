import { Route } from '@angular/router';

export const HOME_ROUTE: Route = {
  path: '',
  redirectTo: 'login',
  pathMatch:'full',
  data: {
    pageTitle: 'Welcome, Java Hipster!',
  },
};
