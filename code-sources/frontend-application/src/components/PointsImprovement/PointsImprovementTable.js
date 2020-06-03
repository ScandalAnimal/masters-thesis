import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import playerService from '../../service/playerService';

const PointsImprovementTable = () => {
  const dispatch = useDispatch();
  const intl = useIntl();
  const projections = useSelector(state => state.app.projections);
  const allCombinedPlayers = useSelector(state => state.app.allCombinedPlayers);
  const originalTeam = useSelector(state => state.app.originalTeam);
  const { currentTeam, additions } = useSelector(state => state.app.edit);
  const [loading, setLoading] = useState(true);
  const [totalPointsOld, setTotalPointsOld] = useState(0);
  const [totalPointsNew, setTotalPointsNew] = useState(0);
  const [teamOld, setTeamOld] = useState([]);
  const [teamNew, setTeamNew] = useState([]);
  const [toShowOld, setToShowOld] = useState([]);
  const [toShowNew, setToShowNew] = useState([]);

  useEffect(() => {
    if (projections.length > 0 && originalTeam.length > 0) {
      setLoading(false);
      setTeamAndPoints();
    }
  }, [projections, originalTeam]);

  useEffect(() => {
    if (projections.length > 0 && originalTeam.length > 0) {
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

    const current = Object.entries(remapped).filter(([name, value], i) => {
      const splitName = name.split('_');
      const player = getPlayer(splitName[0], splitName[1]);
      if (player !== undefined) {
        return isPlayerInCurrentTeam(player.id);
      }
      return false;
    });

    const original = Object.entries(remapped).filter(([name, value], i) => {
      const splitName = name.split('_');
      const player = getPlayer(splitName[0], splitName[1]);
      if (player !== undefined) {
        return isPlayerInOriginalTeam(player.id);
      }
      return false;
    });

    let tmPoints = 0;
    current.forEach(([name, value], i) => {
      const splitName = name.split('_');
      const player = getPlayer(splitName[0], splitName[1]);
      const isCaptain = player.is_captain === 'true';
      const isViceCaptain = player.is_vice_captain === 'true';
      tmPoints +=
        isCaptain || isViceCaptain ? value[0].predicted_points * 2 : value[0].predicted_points;
    });
    setTotalPointsNew(tmPoints);
    tmPoints = 0;
    original.forEach(([name, value], i) => {
      const splitName = name.split('_');
      const player = getPlayer(splitName[0], splitName[1]);
      const isCaptain = player.is_captain === 'true';
      const isViceCaptain = player.is_vice_captain === 'true';
      tmPoints +=
        isCaptain || isViceCaptain ? value[0].predicted_points * 2 : value[0].predicted_points;
    });
    setTotalPointsOld(tmPoints);
    countToShow(current, original);
  };

  function countToShow(current, original) {
    let currentRes = {};
    let originalRes = {};

    current.forEach(([name, value], i) => {
      currentRes[name] = i;
    });
    original.forEach(([name, value], i) => {
      originalRes[name] = i;
    });
    let resultNew = current.reduce((prev, c) => {
      if (!originalRes.hasOwnProperty(c[0])) {
        prev.push(c);
      }
      return prev;
    }, []);
    let resultOld = original.reduce((prev, c) => {
      if (!currentRes.hasOwnProperty(c[0])) {
        prev.push(c);
      }
      return prev;
    }, []);

    const origCaptain = originalTeam.filter(p => p.is_captain === 'true');
    const currCaptain = currentTeam.filter(p => p.is_captain === 'true');
    getCaptainsDiff(origCaptain, currCaptain);
    const origViceCaptain = originalTeam.filter(p => p.is_vice_captain === 'true');
    const currViceCaptain = currentTeam.filter(p => p.is_vice_captain === 'true');
    getCaptainsDiff(origViceCaptain, currViceCaptain);

    const oldTeam = original.filter(value => !resultOld.includes(value));
    const newTeam = current.filter(value => !resultNew.includes(value));
    setToShowNew(resultNew);
    setToShowOld(resultOld);
    setTeamNew(newTeam);
    setTeamOld(oldTeam);

    function getCaptainsDiff(origCaptain, currCaptain) {
      if (origCaptain.length > 0 && currCaptain.length > 0) {
        if (origCaptain[0].id !== currCaptain[0].id) {
          const filterCaptainCurr = current.filter(([name, value], i) => {
            return name === currCaptain[0].first_name + '_' + currCaptain[0].second_name;
          });
          const filterCaptainOrig = original.filter(([name, value], i) => {
            return name === origCaptain[0].first_name + '_' + origCaptain[0].second_name;
          });
          if (!resultNew.includes(filterCaptainCurr[0])) {
            resultNew.push(filterCaptainCurr[0]);
          }
          if (!resultOld.includes(filterCaptainOrig[0])) {
            resultOld.push(filterCaptainOrig[0]);
          }
        }
      }
    }
  }

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

  const isPlayerInCurrentTeam = id => {
    const elem = currentTeam.find(item => item.id === id);
    return elem !== undefined;
  };

  const isPlayerInOriginalTeam = id => {
    const elem = originalTeam.find(item => item.id === id);
    return elem !== undefined;
  };

  function isCaptain(id, isOld) {
    const c = isOld
      ? originalTeam.find(item => item.id === id)
      : currentTeam.find(item => item.id === id);
    if (c !== undefined) {
      if (c.is_captain === 'true') {
        return true;
      }
    }
    return false;
  }

  function isViceCaptain(id, isOld) {
    const c = isOld
      ? originalTeam.find(item => item.id === id)
      : currentTeam.find(item => item.id === id);
    if (c !== undefined) {
      if (c.is_vice_captain === 'true') {
        return true;
      }
    }
    return false;
  }

  const renderProjectionsTable = (team, isOld) => {
    return (
      <>
        {team.map(([name, value], i) => {
          const splitName = name.split('_');
          const player = getPlayer(splitName[0], splitName[1]);
          const isC = isCaptain(player.id, isOld);
          const isVC = isViceCaptain(player.id, isOld);
          const points = value[0].predicted_points;
          let formattedName = playerService.getPlayerName(player);
          return (
            <div className='player-row row' key={i} onClick={() => openPlayerInfo(player)}>
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

  const renderTotalPoints = totalPoints => {
    return (
      <div className='all-projections-total'>
        {intl.messages['table.total']} {totalPoints}
      </div>
    );
  };

  return (
    <div className='points-improvement__wrapper'>
      {!loading && (
        <>
          <div className='points-improvement__row'>
            {toShowOld.length === 0 && toShowNew.length === 0 && (
              <div className='all-projections-total'>{intl.messages['table.no']}</div>
            )}
            {toShowOld.length !== 0 && toShowNew.length !== 0 && (
              <>
                <div className='points-improvement__col'>
                  <div className='points-improvement__title'>{intl.messages['table.original']}</div>
                  {renderTotalPoints(totalPointsOld)}
                  {displayHeader()}
                  {renderProjectionsTable(toShowOld, true)}
                </div>
                <div className='points-improvement__col'>
                  <div className='points-improvement__title'>{intl.messages['table.current']}</div>
                  {renderTotalPoints(totalPointsNew)}
                  {displayHeader()}
                  {renderProjectionsTable(toShowNew, false)}
                </div>
              </>
            )}
          </div>
          {toShowOld.length !== 0 && toShowNew.length !== 0 && (
            <div className='points-improvement__row'>
              <div className='points-improvement__col'>
                <hr />
                {renderProjectionsTable(teamOld, true)}
              </div>
              <div className='points-improvement__col'>
                <hr />
                {renderProjectionsTable(teamNew, false)}
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default PointsImprovementTable;
