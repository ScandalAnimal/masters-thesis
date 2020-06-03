import {
  getAllInjuries,
  getAllPlayerIds,
  getAllPlayers,
  getAllTeams,
} from '../../reducers/appActions';
import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import Card from '../Common/Card';
import Loader from './Loader';
import PointsImprovementTable from '../PointsImprovement/PointsImprovementTable';
import ProjectedPerformanceTable from '../ProjectedPerformance/ProjectedPerformanceTable';
import React, { useEffect, useState } from 'react';
import TeamOverview from '../TeamOverview/TeamOverview';
import TransferMarket from '../TransferMarket/TransferMarket';

function findPlayerById(allPlayerIds, allPlayers, id) {
  for (let i = 0; i < allPlayerIds.length; i++) {
    const playerId = allPlayerIds[i];
    if (playerId.id === id) {
      for (let j = 0; j < allPlayers.length; j++) {
        const player = allPlayers[j];
        if (
          player.second_name === playerId.second_name &&
          player.first_name === playerId.first_name
        ) {
          return Object.assign(playerId, player);
        }
      }
    }
  }
  return null;
}

const Homepage = () => {
  const dispatch = useDispatch();
  const intl = useIntl();
  const { teamPicks, allPlayers, allPlayerIds, teams } = useSelector(state => state.app);
  const currentTeam = useSelector(state => state.app.edit.currentTeam);
  const allCombinedPlayers = useSelector(state => state.app.allCombinedPlayers);
  const teamId = useSelector(state => state.app.teamId);
  const injuries = useSelector(state => state.app.injuries);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    window.scrollTo(0, 0);
    if (allPlayers === null) {
      getAllPlayers(dispatch);
    }
    if (allPlayerIds === null) {
      getAllPlayerIds(dispatch);
    }
    if (teams === null) {
      getAllTeams(dispatch);
    }
    if (injuries.length === 0) {
      getAllInjuries(dispatch);
    }
  }, [dispatch]);

  function getLoaderText() {
    if (allPlayers === null) {
      return intl.messages['loading.players'];
    }
    if (allPlayerIds === null) {
      return intl.messages['loading.ids'];
    }
    if (teams === null) {
      return intl.messages['loading.teams'];
    }
    if (teamId !== 'manual' && currentTeam.length === 0) {
      return intl.messages['loading.current'];
    }
    return null;
  }

  useEffect(() => {
    if (
      allPlayers !== null &&
      allPlayerIds !== null &&
      teams !== null &&
      currentTeam.length > 0 &&
      injuries.length > 0
    ) {
      setLoading(false);
    }
    if (
      allPlayers !== null &&
      allPlayerIds !== null &&
      teams !== null &&
      teamId === 'manual' &&
      injuries.length > 0
    ) {
      setLoading(false);
    }
    let combinedPlayers = [];
    let combinedAllPlayers = [];

    if (
      allPlayers !== null &&
      allPlayerIds !== null &&
      teams !== null &&
      teamPicks !== null &&
      injuries.length !== 0 &&
      currentTeam.length === 0 &&
      allCombinedPlayers.length === 0
    ) {
      let players = [];
      for (let i = 0; i < teamPicks.length; i++) {
        const player = Object.assign(
          findPlayerById(allPlayerIds, allPlayers, teamPicks[i].element),
          teamPicks[i]
        );
        players.push(player);
      }
      combinedPlayers = players;
      dispatch({
        type: 'SET_CURRENT_TEAM',
        payload: {
          value: combinedPlayers,
        },
      });

      let playersAll = [];
      for (let i = 0; i < allPlayerIds.length; i++) {
        const player = findPlayerById(allPlayerIds, allPlayers, allPlayerIds[i].id);
        playersAll.push(player);
      }
      combinedAllPlayers = playersAll;
      dispatch({
        type: 'SET_ALL_COMBINED_PLAYERS',
        payload: {
          value: combinedAllPlayers,
        },
      });
    }
  }, [allPlayers, allPlayerIds, teams, teamPicks, currentTeam, allCombinedPlayers, dispatch]);

  return (
    <div className='main container homepage'>
      {loading ? (
        <Loader text={getLoaderText()} />
      ) : (
        <div className='row'>
          <div className='col-xl-6 d-flex flex-column'>
            <Card title={intl.messages['menu.overview']}>
              <TeamOverview />
            </Card>
            <Card title={intl.messages['menu.projected']}>
              <ProjectedPerformanceTable />
            </Card>
          </div>
          <div className='col-xl-6 d-flex flex-column'>
            <Card title={intl.messages['menu.market']}>
              <TransferMarket />
            </Card>
            {teamId !== 'manual' && (
              <Card title={intl.messages['menu.diff']}>
                <PointsImprovementTable />
              </Card>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default Homepage;
