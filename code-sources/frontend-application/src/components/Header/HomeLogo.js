import { Link, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import React from 'react';
import logo from '../../assets/images/logoNew.png';

const HomeLogo = () => {
  const teamId = useSelector(state => state.app.teamId);
  const params = useParams();
  const path = teamId === null ? '/' : '/home';
  return (
    <Link to={`/${params.langId}${path}`}>
      <img src={logo} alt='App logo' width={175} />
    </Link>
  );
};

export default HomeLogo;
