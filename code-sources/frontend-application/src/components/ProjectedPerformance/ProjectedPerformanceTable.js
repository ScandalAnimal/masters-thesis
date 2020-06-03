import { getAllProjections } from '../../reducers/appActions';
import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import playerService from '../../service/playerService';

const ProjectedPerformanceTable = () => {
  const dispatch = useDispatch();
  const projections = useSelector(state => state.app.projections);
  const allCombinedPlayers = useSelector(state => state.app.allCombinedPlayers);
  const { currentTeam, additions } = useSelector(state => state.app.edit);
  const [loading, setLoading] = useState(true);
  const [totalPoints, setTotalPoints] = useState(0);
  const [team, setTeam] = useState([]);
  const intl = useIntl();

  useEffect(() => {
    if (projections.length === 0) {
      Promise.all([
        getAllProjections(dispatch, 1),
        getAllProjections(dispatch, 2),
        getAllProjections(dispatch, 3),
      ]).then(values => {
        dispatch({
          type: 'SET_LOADING',
          payload: {
            value: false,
          },
        });
        values.forEach((v, i) => {
          dispatch({
            type: 'SET_PROJECTIONS',
            payload: {
              weekId: i + 1,
              value: v.data,
            },
          });
        });
        setLoading(false);
      });
    }
  }, []);

  useEffect(() => {
    if (projections !== []) {
      setLoading(false);
      setTeamAndPoints();
    }
  }, [projections]);

  useEffect(() => {
    if (projections.length > 0) {
      setTeamAndPoints();
    }
  }, [currentTeam, additions]);

  const setTeamAndPoints = () => {
    const displayedProjections = projections.find(projection => projection.id === 1);
    if (displayedProjections === undefined) {
      return null;
    }
    const remapped = _.mapValues(_.groupBy(displayedProjections.value, 'player_name'), x =>
      x.map(y => _.omit(y, 'player_name'))
    );

    const team = Object.entries(remapped).filter(([name, value], i) => {
      const splitName = name.split('_');
      const player = getPlayer(splitName[0], splitName[1]);
      if (player !== undefined) {
        return isPlayerInTeam(player.id);
      }
      return false;
    });

    let tmPoints = 0;
    team.forEach(([name, value], i) => {
      const splitName = name.split('_');
      const player = getPlayer(splitName[0], splitName[1]);
      const isCaptain = player.is_captain === 'true';
      const isViceCaptain = player.is_vice_captain === 'true';
      tmPoints +=
        isCaptain || isViceCaptain ? value[0].predicted_points * 2 : value[0].predicted_points;
    });
    setTotalPoints(tmPoints);
    setTeam(team);
  };

  const openPlayerInfo = player => {
    dispatch({
      type: 'OPEN_PLAYER_INFO',
      payload: {
        value: player,
      },
    });
  };

  const displayHeader = () => {
    return (
      <div className='player-row player-row-heading row'>
        <div className='all-projections-id'>#</div>
        <div className='all-projections-name'>{intl.messages['table.name']}</div>
        <div className='all-projections-weeks-single'>{intl.messages['table.next']}</div>
      </div>
    );
  };

  const getPlayer = (firstName, secondName) => {
    return allCombinedPlayers.find(
      playerIdObject =>
        playerIdObject.first_name === firstName && playerIdObject.second_name === secondName
    );
  };

  const isPlayerInTeam = id => {
    const elem = currentTeam.find(item => item.id === id);
    return elem !== undefined;
  };

  function isCaptain(id) {
    const c = currentTeam.find(item => item.id === id);
    if (c !== undefined) {
      if (c.is_captain === 'true') {
        return true;
      }
    }
    return false;
  }

  function isViceCaptain(id) {
    const c = currentTeam.find(item => item.id === id);
    if (c !== undefined) {
      if (c.is_vice_captain === 'true') {
        return true;
      }
    }
    return false;
  }

  const renderProjectionsTable = () => {
    return (
      <>
        {displayHeader()}
        {team.map(([name, value], i) => {
          const splitName = name.split('_');
          const player = getPlayer(splitName[0], splitName[1]);
          let formattedName = playerService.getPlayerName(player);
          const isC = isCaptain(player.id);
          const isVC = isViceCaptain(player.id);
          const points = value[0].predicted_points;
          return (
            <div className='player-row row' key={i} onClick={() => openPlayerInfo(player)}>
              <div className='all-projections-id'>{i}</div>
              <div className='all-projections-name'>
                {formattedName} {isC && `(C)`}
                {isVC && `(V)`}
              </div>
              <div className='all-projections-weeks-single'>{points}</div>
            </div>
          );
        })}
      </>
    );
  };

  const renderTotalPoints = () => {
    return (
      <div className='all-projections-total'>
        {intl.messages['table.total']} {totalPoints}
      </div>
    );
  };

  return (
    <div className='all-projections'>
      {!loading && (
        <>
          {renderTotalPoints()}
          {renderProjectionsTable()}
        </>
      )}
    </div>
  );
};

export default ProjectedPerformanceTable;
