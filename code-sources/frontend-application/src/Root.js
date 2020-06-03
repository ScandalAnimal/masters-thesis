/* eslint-disable react/no-children-prop */
import 'react-toastify/dist/ReactToastify.css';
import { IntlProvider } from 'react-intl';
import {
  Redirect,
  Route,
  BrowserRouter as Router,
  Switch,
  useParams,
  useRouteMatch,
} from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import { useDispatch, useSelector } from 'react-redux';
import AllProjections from './components/AllProjections/AllProjections';
import ErrorPage from './components/ErrorPage/ErrorPage';
import Fixtures from './components/Fixtures/Fixtures';
import Footer from './components/Footer/Footer';
import Header from './components/Header/Header.js';
import Homepage from './components/Homepage/Homepage';
import Login from './components/Login/Login';
import Optimize from './components/Optimize/Optimize';
import PlayerDetailPopup from './components/TeamOverview/PlayerDetail/PlayerDetailPopup';
import React from 'react';
import Statistics from './components/Statistics/Statistics';
import csTranslations from './translations/cs.json';
import enTranslations from './translations/en.json';

const translationsForUsersLocale = {
  en: enTranslations,
  cs: csTranslations,
};

const RedirectToLang = () => {
  return (
    <Redirect
      to={{
        pathname: '/en',
      }}
    />
  );
};
const Root = () => {
  return (
    <Router>
      <div>
        <Switch>
          <Route exact path='/' children={<RedirectToLang />} />
          <Route path='/:langId' children={<DIPRoutes />} />
        </Switch>
      </div>
    </Router>
  );
};

const DIPRoutes = () => {
  const params = useParams();
  const match = useRouteMatch();
  const langId = params.langId;
  const dispatch = useDispatch();

  function closeModal() {
    dispatch({
      type: 'CLOSE_MODAL',
    });
  }
  const modalShow = useSelector(state => state.app.modalShow);

  if (!['en', 'cs'].includes(langId)) {
    return <RedirectToLang />;
  }
  return (
    <IntlProvider locale={params.langId} messages={translationsForUsersLocale[params.langId]}>
      <ToastContainer />

      <div className='position-relative'>
        <Header />
        <div className='container app-body'>
          <Switch>
            <Route exact path={`${match.path}/`} children={<Login />} />
            <ProtectedRoute path={`${match.path}/home`} children={<Homepage />} />
            <ProtectedRoute path={`${match.path}/fixtures`} children={<Fixtures />} />
            <ProtectedRoute path={`${match.path}/optimize`} children={<Optimize />} />
            <ProtectedRoute path={`${match.path}/all-projections`} children={<AllProjections />} />
            <ProtectedRoute path={`${match.path}/statistics`} children={<Statistics />} />
            <Route children={<ErrorPage />} />
          </Switch>
        </div>
        <PlayerDetailPopup show={modalShow} onHide={closeModal} />
        <Footer />
      </div>
    </IntlProvider>
  );
};

function ProtectedRoute({ children, ...rest }) {
  const teamId = useSelector(state => state.app.teamId);
  const isAuth = teamId !== null;
  return (
    <Route
      {...rest}
      render={() =>
        isAuth ? (
          children
        ) : (
          <Redirect
            to={{
              pathname: '/',
            }}
          />
        )
      }
    />
  );
}

export default Root;
