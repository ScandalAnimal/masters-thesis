import { useIntl } from 'react-intl';
import PlayerRow from './PlayerRow';
import React, { useEffect, useState } from 'react';
import ReactPaginate from 'react-paginate';

function ItemList({ items }) {
  const intl = useIntl();
  return (
    <>
      <div className='player-row player-row-heading row'>
        <div className='col-3 text-left'>{intl.messages['table.name']}</div>
        <div className='col-3 text-center'>{intl.messages['table.team']}</div>
        <div className='col-3 text-center'>{intl.messages['table.points']}</div>
        <div className='col-3 text-right'>{intl.messages['table.price']}</div>
      </div>
      {items.map((item, i) => {
        return <PlayerRow player={item} key={i} />;
      })}
    </>
  );
}

function PlayerList({ filteredPlayers }) {
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

export default PlayerList;
