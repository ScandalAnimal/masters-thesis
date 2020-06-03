import { POSITIONS } from '../../constants';
import PlayerIcon from './PlayerIcon';
import React from 'react';

const Bench = ({ bench }) => {
  function sortBench() {
    const newBench = [];
    const gk = bench.find(player => player.element_type === POSITIONS.GK);
    if (gk !== undefined) {
      newBench.push(gk);
    }
    bench.forEach(player => {
      if (player.element_type !== POSITIONS.GK) {
        newBench.push(player);
      }
    });
    return newBench;
  }

  const sortedBench = sortBench();
  function renderPlayers() {
    return (
      <div className='d-flex flex-column players'>
        <div className='players-row row'>
          {sortedBench.map(player => {
            return <PlayerIcon player={player} key={player.id} />;
          })}
        </div>
      </div>
    );
  }

  return (
    <div className='bench-wrapper'>
      <div className='bench-players'>{renderPlayers()}</div>
    </div>
  );
};

export default Bench;
