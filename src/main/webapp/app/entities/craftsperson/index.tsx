import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Craftsperson from './craftsperson';
import CraftspersonDetail from './craftsperson-detail';
import CraftspersonUpdate from './craftsperson-update';
import CraftspersonDeleteDialog from './craftsperson-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CraftspersonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CraftspersonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CraftspersonDetail} />
      <ErrorBoundaryRoute path={match.url} component={Craftsperson} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CraftspersonDeleteDialog} />
  </>
);

export default Routes;
