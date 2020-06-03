import { Link, useParams } from 'react-router-dom';
import React from 'react';

const HeaderLink = ({ title, url, action }) => {
  const params = useParams();

  return (
    <div className='header-link' onClick={action}>
      <Link to={`/${params.langId}/${url}`}>
        <span>{title}</span>
      </Link>
    </div>
  );
};

export default HeaderLink;
