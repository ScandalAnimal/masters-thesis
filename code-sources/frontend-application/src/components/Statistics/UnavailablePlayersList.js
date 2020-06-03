import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import React, { useEffect, useState } from 'react';
import ReactPaginate from 'react-paginate';

const PlayerRow = ({ player }) => {
  const dispatch = useDispatch();
  const teams = useSelector(state => state.app.teams);
  const name = player.web_name;
  const news = player.news;
  const yellows = player.yellow_cards;
  const reds = player.red_cards;
  const teamShortName = teams.find(team => team.code === player.team_code).shortName;

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
      <div className='unavailable-name'>{name}</div>
      <div className='unavailable-team'>{teamShortName}</div>
      <div className='unavailable-news'>{news}</div>
      <div className='unavailable-cards'>
        {yellows}/{reds}
      </div>
    </div>
  );
};

function ItemList({ items }) {
  const intl = useIntl();
  return (
    <>
      <div className='player-row player-row-heading row'>
        <div className='unavailable-name'>{intl.messages['table.name']}</div>
        <div className='unavailable-team'>{intl.messages['table.team']}</div>
        <div className='unavailable-news'>{intl.messages['table.reason']}</div>
        <div className='unavailable-cards'>{intl.messages['table.cards']}</div>
      </div>
      {items.map((item, i) => {
        return <PlayerRow player={item} key={i} />;
      })}
    </>
  );
}

function UnavailablePlayersList({ filteredPlayers }) {
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

export default UnavailablePlayersList;
