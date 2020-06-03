import React from 'react';
import kitLogoService from '../../../service/kitLogoService';
import playerService from '../../../service/playerService';

const PlayerSmallIcon = ({ player, onClick }) => {
  const name = playerService.getPlayerName(player);

  return (
    <div className='player-small col col-xl-4' key={player.id} onClick={onClick}>
      {player.team !== undefined && (
        <>
          <div className='player-small__img'>
            <img src={kitLogoService.getTeamKit(parseInt(player.team))} alt='img' />
          </div>
          <div className='player-small__label'>
            <div>{name}</div>
          </div>
        </>
      )}
    </div>
  );
};

export default PlayerSmallIcon;
