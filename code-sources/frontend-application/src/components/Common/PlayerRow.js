import { useDispatch, useSelector } from 'react-redux';
import React from 'react';
import playerService from '../../service/playerService';

const PlayerRow = ({ player }) => {
  const dispatch = useDispatch();
  const teams = useSelector(state => state.app.teams);
  const name = playerService.getPlayerName(player);
  const points = player.total_points;
  const price = player.now_cost;

  const openPlayerInfo = () => {
    dispatch({
      type: 'OPEN_PLAYER_INFO',
      payload: {
        value: player,
      },
    });
  };
  return (
    <div className='player-row row' onClick={openPlayerInfo}>
      <div className='col-3 text-left'>{name}</div>
      <div className='col-3 text-center'>{teams[player.team - 1].shortName}</div>
      <div className='col-3 text-center'>{points}</div>
      <div className='col-3 text-right'>{(price / 10).toFixed(1)}</div>
    </div>
  );
};

export default PlayerRow;
