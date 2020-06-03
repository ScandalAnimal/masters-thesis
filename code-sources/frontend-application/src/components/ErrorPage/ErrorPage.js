import { useIntl } from 'react-intl';
import React from 'react';

function ErrorPage() {
  const intl = useIntl();
  return <div className='header-link'>{intl.messages['error.page']}</div>;
}

export default ErrorPage;
