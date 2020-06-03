import { Form } from 'react-bootstrap';
import { getPlayerDetails } from '../../../reducers/appActions';
import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import Loader from '../../Homepage/Loader';
import PlayerHistoryList from './PlayerHistoryList';
import React, { useEffect, useState } from 'react';

const PlayerHistoryTable = ({ player }) => {
  const name = player.first_name + '_' + player.second_name;
  const id = player.id;
  const playerDetails = useSelector(state => state.app.playerDetails);
  const [playerHistory, setPlayerHistory] = useState({});
  const [selectedSeason, setSelectedSeason] = useState(0);
  const [filtered, setFiltered] = useState([]);
  const dispatch = useDispatch();
  const [loading, setLoading] = useState(true);
  const intl = useIntl();

  useEffect(() => {
    const detail = playerDetails.find(d => d.playerName === name);
    if (detail) {
      setPlayerHistory(detail.value);
      setFiltered(detail.value);
      setLoading(false);
    }
  }, [playerDetails, name]);

  useEffect(() => {
    getPlayerDetails(dispatch, id, name);
  }, []);

  function renderSelectBoxes() {
    const seasons = [
      intl.messages['detail.all.seasons'],
      '2016-17',
      '2017-18',
      '2018-19',
      '2019-20',
    ];

    function filterPlayers(v) {
      let tmpFiltered = [];

      if (parseInt(v) === 0) {
        tmpFiltered = playerHistory;
      } else {
        tmpFiltered = playerHistory.filter(item => {
          return item.season === seasons[parseInt(v)];
        });
      }

      setFiltered(tmpFiltered);
    }

    function changeSeason(e) {
      const v = e.target.value;
      setSelectedSeason(v);
      filterPlayers(v);
    }

    return (
      <div className='detail-selectbox-wrapper'>
        <Form>
          <Form.Group
            controlId='detailForm-season'
            className={'d-flex flex-column custom-dropdown'}
          >
            <Form.Label>{intl.messages['detail.season']}</Form.Label>
            <Form.Control as='select' onChange={changeSeason} value={selectedSeason}>
              {seasons.map((season, i) => {
                return (
                  <option key={season} value={i}>
                    {season}
                  </option>
                );
              })}
            </Form.Control>
          </Form.Group>
        </Form>
      </div>
    );
  }

  function renderDataList() {
    return <PlayerHistoryList filteredData={filtered} selectedSeason={selectedSeason > 0} />;
  }

  return (
    <div className='player-history'>
      {loading ? (
        <Loader text={intl.messages['loading.detail']} />
      ) : (
        <>
          {renderSelectBoxes()}
          {renderDataList()}
        </>
      )}
    </div>
  );
};

export default PlayerHistoryTable;
