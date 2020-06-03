import { useIntl } from 'react-intl';
import React, { useEffect, useState } from 'react';
import ReactPaginate from 'react-paginate';

const PlayerRow = ({ player, selectedSeason }) => {
  return (
    <div className='player-row row'>
      {!selectedSeason && <div className='history-item'>{player.season}</div>}
      <div className='history-item'>{player.gw_index}</div>
      <div className='history-item'>{player.goals_scored}</div>
      <div className='history-item'>{player.assists}</div>
      <div className='history-item'>{player.total_points}</div>
      <div className='history-item'>
        {player.yellow_cards}/{player.red_cards}
      </div>
      <div className='history-item'>{player.saves}</div>
      <div className='history-item'>{player.goals_conceded}</div>
      <div className='history-item'>{player.own_goals}</div>
      <div className='history-item'>{player.clean_sheets}</div>
      <div className='history-item'>{player.bps}</div>
    </div>
  );
};

function ItemList({ items, selectedSeason }) {
  const intl = useIntl();
  return (
    <>
      <div className='player-row player-row-heading row'>
        {!selectedSeason && <div className='history-item'>{intl.messages['detail.season']}</div>}
        <div className='history-item'>{intl.messages['detail.gw']}</div>
        <div className='history-item'>{intl.messages['detail.goals']}</div>
        <div className='history-item'>{intl.messages['detail.assists']}</div>
        <div className='history-item'>{intl.messages['table.points']}</div>
        <div className='history-item'>{intl.messages['detail.cards']}</div>
        <div className='history-item'>{intl.messages['detail.saves']}</div>
        <div className='history-item'>{intl.messages['detail.conceded']}</div>
        <div className='history-item'>{intl.messages['detail.own']}</div>
        <div className='history-item'>{intl.messages['detail.clean']}</div>
        <div className='history-item'>{intl.messages['detail.bps']}</div>
      </div>
      {items.map((item, i) => {
        return <PlayerRow player={item} key={i} selectedSeason={selectedSeason} />;
      })}
    </>
  );
}

function PlayerHistoryList({ filteredData, selectedSeason }) {
  const intl = useIntl();
  let perPage = 10;
  let [currentData, setCurrentData] = useState(filteredData.slice(0, perPage));

  useEffect(() => {
    setCurrentData(filteredData.slice(0, perPage));
  }, [filteredData, perPage]);

  const pageCount = Math.ceil(filteredData.length / perPage);

  function handlePageClick(data) {
    let selected = data.selected;
    let offset = Math.ceil(selected * perPage);
    setCurrentData(filteredData.slice(offset, offset + perPage));
  }

  return (
    <div className='player-history-table'>
      {currentData.length === 0 ? (
        <div className='player-history-no-data'>{intl.messages['detail.no']}</div>
      ) : (
        <>
          <ItemList items={currentData} selectedSeason={selectedSeason} />
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
        </>
      )}
    </div>
  );
}

export default PlayerHistoryList;
