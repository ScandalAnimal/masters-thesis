import { useIntl } from 'react-intl';
import { useSelector } from 'react-redux';
import PlayerIcon from './PlayerIcon';
import React from 'react';

const RemovedPlayers = () => {
  const intl = useIntl();
  const removedPlayers = useSelector(state => state.app.edit.removedPlayers);

  function renderPlayers() {
    return (
      <div className='d-flex flex-column players'>
        <div className='subtitle'>{intl.messages['table.removed']}</div>
        <div className='removed-players-row row'>
          {removedPlayers.map(player => {
            return <PlayerIcon player={player} key={player.id} />;
          })}
        </div>
      </div>
    );
  }

  return (
    <div className='removed-wrapper'>
      {removedPlayers.length > 0 && <div className='removed-players'>{renderPlayers()}</div>}
    </div>
  );
};

export default RemovedPlayers;
