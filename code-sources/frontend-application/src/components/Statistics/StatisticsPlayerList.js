import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import React, { useEffect, useState } from 'react';
import ReactPaginate from 'react-paginate';
import playerService from '../../service/playerService';

const PlayerRow = ({ player }) => {
  const dispatch = useDispatch();
  const teams = useSelector(state => state.app.teams);
  const name = playerService.getPlayerName(player);
  const points = player.total_points;
  const bps = player.bps;
  const selectedByPercent = player.selected_by_percent;

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
      <div className='all-stats-name'>{name}</div>
      <div className='all-stats-team'>{teams[player.team - 1].shortName}</div>
      <div className='all-stats-selected'>{selectedByPercent} %</div>
      <div className='all-stats-points'>{points}</div>
      <div className='all-stats-bps'>{bps}</div>
    </div>
  );
};

function ItemList({ items }) {
  const intl = useIntl();
  return (
    <>
      <div className='player-row player-row-heading row'>
        <div className='all-stats-name'>{intl.messages['table.name']}</div>
        <div className='all-stats-team'>{intl.messages['table.team']}</div>
        <div className='all-stats-selected'>{intl.messages['table.selected']}</div>
        <div className='all-stats-points'>{intl.messages['table.points']}</div>
        <div className='all-stats-bps'>{intl.messages['table.bps']}</div>
      </div>
      {items.map((item, i) => {
        return <PlayerRow player={item} key={i} />;
      })}
    </>
  );
}

function StatisticsPlayerList({ filteredPlayers }) {
  let perPage = 10;
  let [currentData, setCurrentData] = useState(filteredPlayers.slice(0, perPage));

  useEffect(() => {
    setCurrentData(filteredPlayers.slice(0, perPage));
  }, [filteredPlayers, perPage]);

  const pageCount = Math.ceil(filteredPlayers.length / perPage);

  function handlePageClick(data) {
    let selected = data.selected;
    let offset = Math.ceil(selected * perPage);
    setCurrentData(filteredPlayers.slice(offset, offset + perPage));
  }

  return (
    <div className='player-list'>
      <ItemList items={currentData} />
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

export default StatisticsPlayerList;
