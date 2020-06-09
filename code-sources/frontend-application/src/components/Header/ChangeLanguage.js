import { Link } from 'react-router-dom';
import React from 'react';

const ChangeLanguage = ({ action }) => {
  return (
    <div className='language-chooser'>
      <Link to={location => `/en/${location.pathname.substring(4)}`} onClick={action}>
        EN
      </Link>{' '}
      /{' '}
      <Link to={location => `/cs/${location.pathname.substring(4)}`} onClick={action}>
        CS
      </Link>
    </div>
  );
};

export default ChangeLanguage;
