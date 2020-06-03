import { useDispatch, useSelector } from 'react-redux';
import React from 'react';
import kitLogoService from '../../service/kitLogoService';
import playerService from '../../service/playerService';

const PlayerIcon = ({ player }) => {
  const dispatch = useDispatch();
  const isCaptain = player.is_captain === 'true';
  const isViceCaptain = player.is_vice_captain === 'true';
  const name = playerService.getPlayerName(player);
  const injuries = useSelector(state => state.app.injuries);
  const isUnavailable = playerService.isPlayerUnavailable(
    injuries,
    player.first_name,
    player.second_name
  );

  const openPlayerInfo = () => {
    dispatch({
      type: 'OPEN_PLAYER_INFO',
      payload: {
        value: player,
      },
    });
  };
  return (
    <div className='player' key={player.id}>
      {player.team !== undefined && (
        <>
          <div className='player__img' onClick={openPlayerInfo}>
            <img src={kitLogoService.getTeamKit(parseInt(player.team))} alt='img' />
          </div>
          <div className='player__label'>
            <div>{name}</div>
          </div>
          <div className='player__options'>
            {isCaptain && <div className='player__option-captain'>C</div>}
            {isViceCaptain && <div className='player__option-captain'>V</div>}
            {isUnavailable && <div className='player__option-unavailable'>X</div>}
          </div>
        </>
      )}
    </div>
  );
};

export default PlayerIcon;
