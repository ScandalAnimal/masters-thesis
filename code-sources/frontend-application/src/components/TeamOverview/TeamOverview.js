import { POSITIONS } from '../../constants';
import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import Bench from './Bench';
import Button from '../Button/Button';
import FootballField from './FootballField';
import React, { useEffect, useState } from 'react';
import RemovedPlayers from './RemovedPlayers';

const TeamOverview = () => {
  const intl = useIntl();
  const { currentTeam, additions, removedPlayers } = useSelector(state => state.app.edit);
  const dispatch = useDispatch();
  const gks = [];
  const defs = [];
  const mids = [];
  const fwds = [];
  const bench = [];
  let loading = true;
  const [totalCost, setTotalCost] = useState(0);

  if (currentTeam !== null) {
    for (let i = 0; i < currentTeam.length; i++) {
      const player = currentTeam[i];
      if (player.position > 11) {
        bench.push(player);
      } else if (player.element_type === POSITIONS.GK) {
        gks.push(player);
      } else if (player.element_type === POSITIONS.DF) {
        defs.push(player);
      } else if (player.element_type === POSITIONS.MF) {
        mids.push(player);
      } else if (player.element_type === POSITIONS.FW) {
        fwds.push(player);
      }
    }
    loading = false;
  }

  const calcCost = () => {
    let tmCost = 0;
    currentTeam.forEach(elem => {
      if (elem.purchase_price !== undefined) {
        tmCost += elem.purchase_price;
      } else {
        tmCost += elem.now_cost;
      }
    });
    removedPlayers.forEach(elem => {
      if (elem.purchase_price !== undefined) {
        tmCost += elem.purchase_price;
        tmCost -= elem.selling_price;
      }
    });
    let v = tmCost / Math.pow(10, 1); // new value
    setTotalCost(v);
  };

  useEffect(() => {
    calcCost();
  }, [currentTeam, additions]);

  const renderTotalCost = () => {
    return (
      <div className='team-overview-cost'>
        {intl.messages['table.cost'] + ' ' + totalCost.toFixed(1) + '/100M'}
      </div>
    );
  };

  function resetChanges() {
    dispatch({
      type: 'RESET_TEAM_CHANGES',
    });
  }

  return (
    <div>
      {!loading && (
        <>
          <div className='hint'>{intl.messages['team.hint']}</div>
          {renderTotalCost()}
          <FootballField gks={gks} defs={defs} mids={mids} fwds={fwds} />
          <Bench bench={bench} />
          <RemovedPlayers />
          <div className='reset-changes-wrapper'>
            <Button
              onClick={resetChanges}
              text={intl.messages['team.reset']}
              variant='darkPrimary'
            />
          </div>
        </>
      )}
    </div>
  );
};

export default TeamOverview;
