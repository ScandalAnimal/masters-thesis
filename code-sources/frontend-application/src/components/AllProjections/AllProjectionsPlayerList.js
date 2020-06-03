import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import React, { useEffect, useState } from 'react';
import ReactPaginate from 'react-paginate';
import playerService from '../../service/playerService';

function PlayerRow({ player, formattedName, i, correctValues }) {
  const dispatch = useDispatch();
  const { currentTeam } = useSelector(state => state.app.edit);

  const openPlayerInfo = () => {
    dispatch({
      type: 'OPEN_PLAYER_INFO',
      payload: {
        value: player,
      },
    });
  };

  const isCaptain = id => {
    const c = currentTeam.find(item => item.id === id);
    if (c !== undefined) {
      if (c.is_captain === 'true') {
        return true;
      }
    }
    return false;
  };

  const isViceCaptain = id => {
    const c = currentTeam.find(item => item.id === id);
    if (c !== undefined) {
      if (c.is_vice_captain === 'true') {
        return true;
      }
    }
    return false;
  };
  const isC = isCaptain(player.id);
  const isVC = isViceCaptain(player.id);

  return (
    <div className='player-row row' onClick={() => openPlayerInfo(player)}>
      <div className='all-projections-id'>{i}</div>
      <div className='all-projections-name'>
        {formattedName} {isC && `(C)`}
        {isVC && `(V)`}
      </div>
      <div className='all-projections-weeks'>
        {correctValues.map((value, j) => {
          const points = value.predicted_points;
          return <div key={j}>{points}</div>;
        })}
      </div>
    </div>
  );
}

function ItemList({ items, gameWeekCount }) {
  const intl = useIntl();
  const allCombinedPlayers = useSelector(state => state.app.allCombinedPlayers);
  const getPlayer = (firstName, secondName) => {
    return allCombinedPlayers.find(
      playerIdObject =>
        playerIdObject.first_name === firstName && playerIdObject.second_name === secondName
    );
  };

  return (
    <>
      <div className='player-row player-row-heading row'>
        <div className='all-projections-id'>#</div>
        <div className='all-projections-name'>{intl.messages['table.name']}</div>
        <div className='all-projections-weeks'>
          <div>1 {intl.messages['table.gw']}</div>
          {gameWeekCount > 1 && <div>2 {intl.messages['table.gw.plural']}</div>}
          {gameWeekCount > 2 && <div>3 {intl.messages['table.gw.plural']}</div>}
        </div>
      </div>
      {items.map(([name, values], i) => {
        let correctValues = values;
        if (values.length < gameWeekCount) {
          correctValues.push(values[0]);
          if (values.length < gameWeekCount) {
            correctValues.push(values[0]);
          }
        }
        const splitName = name.split('_');
        const player = getPlayer(splitName[0], splitName[1]);
        let formattedName = playerService.getPlayerName(player);
        return (
          <PlayerRow
            player={player}
            key={i}
            i={i}
            formattedName={formattedName}
            correctValues={correctValues}
          />
        );
      })}
    </>
  );
}

function AllProjectionsPlayerList({ players, gameWeekCount }) {
  let perPage = 10;
  let [currentData, setCurrentData] = useState(Object.entries(players).slice(0, perPage));

  useEffect(() => {
    setCurrentData(Object.entries(players).slice(0, perPage));
  }, [players, perPage]);

  const pageCount = Math.ceil(Object.entries(players).length / perPage);

  function handlePageClick(data) {
    let selected = data.selected;
    let offset = Math.ceil(selected * perPage);
    setCurrentData(Object.entries(players).slice(offset, offset + perPage));
  }

  return (
    <div className='player-list'>
      <ItemList items={currentData} gameWeekCount={gameWeekCount} />
      <ReactPaginate
        previousClassName={'d-none'}
        nextClassName={'d-none'}
        breakLabel={'...'}
        breakClassName={'break-me'}
        pageCount={pageCount}
        marginPagesDisplayed={1}
        pageRangeDisplayed={3}
        onPageChange={handlePageClick}
        containerClassName={'pagination'}
        subContainerClassName={'pages pagination'}
        activeClassName={'pagination-active'}
      />
    </div>
  );
}

export default AllProjectionsPlayerList;
