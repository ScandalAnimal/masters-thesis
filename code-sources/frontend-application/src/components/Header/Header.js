import { useIntl } from 'react-intl';
import { useSelector } from 'react-redux';
import ChangeLanguage from './ChangeLanguage';
import HamburgerIcon from './HamburgerIcon';
import HeaderLink from './HeaderLink';
import HomeLogo from './HomeLogo';
import Logout from '../Login/Logout';
import React, { useState } from 'react';
import TeamId from './TeamId';

const Header = () => {
  const [mobileMenuOpened, setMobileMenuOpened] = useState(false);
  const teamId = useSelector(state => state.app.teamId);
  const intl = useIntl();

  const toggleMobileMenu = () => {
    setMobileMenuOpened(!mobileMenuOpened);
  };

  const closeMobileMenu = () => {
    setMobileMenuOpened(false);
  };

  const mobileMenuClass = mobileMenuOpened ? 'nav-hidden--opened' : 'nav-hidden';
  return (
    <header className='header'>
      <div className='container'>
        <div className='row justify-content-space-between'>
          <div className='col-auto d-flex align-items-center' onClick={closeMobileMenu}>
            <HomeLogo />
          </div>
          {teamId === null && (
            <div className='col align-items-center justify-content-end nav-link'>
              <ChangeLanguage />
            </div>
          )}
          {teamId !== null && (
            <>
              <div className='col justify-content-end nav-hamburger'>
                <HamburgerIcon action={toggleMobileMenu} />
              </div>
              <div className='col-auto align-items-center nav-link'>
                <HeaderLink title={intl.messages['menu.optimize']} url={'optimize'} />
              </div>
              <div className='col-auto align-items-center nav-link'>
                <HeaderLink title={intl.messages['menu.projections']} url={'all-projections'} />
              </div>
              <div className='col-auto align-items-center nav-link'>
                <HeaderLink title={intl.messages['menu.stats']} url={'statistics'} />
              </div>
              <div className='col align-items-center justify-content-end nav-link'>
                <TeamId />
              </div>
              <div className='col-auto align-items-center justify-content-end nav-link'>
                <ChangeLanguage />
              </div>
              <div className='col-auto align-items-center justify-content-end nav-link'>
                <Logout />
              </div>
            </>
          )}
        </div>
        <div className={`row justify-content-space-between ` + mobileMenuClass}>
          <div className='col-auto align-items-center'>
            <HeaderLink
              title={intl.messages['menu.optimize']}
              url={'optimize'}
              action={closeMobileMenu}
            />
          </div>
          <div className='col-auto align-items-center'>
            <HeaderLink
              title={intl.messages['menu.projections']}
              url={'all-projections'}
              action={closeMobileMenu}
            />
          </div>
          <div className='col-auto align-items-center'>
            <HeaderLink
              title={intl.messages['menu.stats']}
              url={'statistics'}
              action={closeMobileMenu}
            />
          </div>
          <div className='col-auto align-items-center'>
            <TeamId />
          </div>
          <div className='col-auto align-items-center'>
            <ChangeLanguage action={closeMobileMenu} />
          </div>
          <div className='col-auto align-items-center'>
            <Logout action={closeMobileMenu} />
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
