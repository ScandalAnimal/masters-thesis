import { Link, useParams } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { useIntl } from 'react-intl';
import React from 'react';

function Logout() {
  const params = useParams();
  const dispatch = useDispatch();
  const intl = useIntl();

  const clear = () => {
    dispatch({
      type: 'CLEAR',
    });
  };

  return (
    <div className='header-link' onClick={clear}>
      <Link to={`/${params.langId}/`}>
        <span>{intl.messages['logout']}</span>
      </Link>
    </div>
  );
}

export default Logout;
