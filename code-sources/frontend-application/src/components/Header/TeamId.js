import { useIntl } from 'react-intl';
import { useSelector } from 'react-redux';
import React from 'react';

const TeamId = () => {
  const teamId = useSelector(state => state.app.teamId);
  const intl = useIntl();
  if (teamId === 'manual') {
    return null;
  }
  return (
    <div className='header-link'>
      <div>
        {intl.messages['menu.teamid']} {teamId}
      </div>
    </div>
  );
};

export default TeamId;
