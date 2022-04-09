import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
        },

        {
          path: 'pay-gov-confrm',
          loadChildren: () => import('./pay-gov-confrm/pay-gov-confrm.module').then(m => m.PayGovConfrmModule),
        },

        {
          path: 'modal',
          loadChildren: () => import('./modal/modal.module').then(m => m.ModalModule),
        },

        {
          path: 'confrm',
          loadChildren: () => import('./confrm/confrm.module').then(m => m.ConfrmModule),
        },

        {
          path: 'pay-gov',
          loadChildren: () => import('./pay-gov/pay-gov.module').then(m => m.PayGovModule),
        },

        ...LAYOUT_ROUTES,
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
