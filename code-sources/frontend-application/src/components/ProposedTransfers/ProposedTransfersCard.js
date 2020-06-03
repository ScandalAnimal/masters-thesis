import { useDispatch, useSelector } from 'react-redux';
import { useHistory, useParams } from 'react-router';
import { useIntl } from 'react-intl';
import Button from '../Button/Button';
import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import playerService from '../../service/playerService';

const ProposedTransfersCard = ({ team, i }) => {
  const { currentTeam } = useSelector(state => state.app.edit);
  const { allCombinedPlayers } = useSelector(state => state.app);
  const [playersToRemove, setPlayersToRemove] = useState([]);
  const [playersToAdd, setPlayersToAdd] = useState([]);
  const projections = useSelector(state => state.app.projections);
  const [totalPointsNew, setTotalPointsNew] = useState(0);
  const [totalPointsOld, setTotalPointsOld] = useState(0);
  const dispatch = useDispatch();
  const params = useParams();
  const history = useHistory();
  const intl = useIntl();

  useEffect(() => {
    const newTeamIds = team.team;
    const oldTeamIds = currentTeam.map(player => player.id);

    const newIdsFiltered = newTeamIds.filter(id => oldTeamIds.indexOf(id) === -1);
    const oldIdsFiltered = oldTeamIds.filter(id => newTeamIds.indexOf(id) === -1);

    setPlayersToRemove(oldIdsFiltered);
    setPlayersToAdd(newIdsFiltered);
    setTeamAndPoints();
  }, []);

  function getPlayerById(id) {
    return allCombinedPlayers.find(player => player.id === id);
  }

  const isPlayerInCurrentTeam = id => {
    const elem = team.team.find(item => item === id);
    return elem !== undefined;
  };

  const isPlayerInOriginalTeam = id => {
    const elem = currentTeam.find(item => item.id === id);
    return elem !== undefined;
  };

  const getPlayer = (firstName, secondName) => {
    return allCombinedPlayers.find(
      playerIdObject =>
        playerIdObject.first_name === firstName && playerIdObject.second_name === secondName
    );
  };

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
      return isPlayerInCurrentTeam(player.id);
    });

    const original = Object.entries(remapped).filter(([name, value], i) => {
      const splitName = name.split('_');
      const player = getPlayer(splitName[0], splitName[1]);
      return isPlayerInOriginalTeam(player.id);
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
  };

  const openPlayerInfo = player => {
    dispatch({
      type: 'OPEN_PLAYER_INFO',
      payload: {
        value: player,
      },
    });
  };

  function getPlayerElement(id, i = 0) {
    const player = getPlayerById(id);
    return (
      <div
        className='player-row'
        key={i}
        onClick={() => {
          openPlayerInfo(player);
        }}
      >
        {playerService.getPlayerName(player)}
      </div>
    );
  }

  function showTeamWithChanges() {
    const newTeamIds = team.team;
    const newTeam = newTeamIds.map(id => getPlayerById(id));
    const newTeamWithCaps = newTeam.map(player => {
      const isCaptain = team.captain === player.id;
      const isViceCaptain = team.viceCaptain === player.id;
      return {
        ...player,
        is_captain: isCaptain ? 'true' : 'false',
        is_vice_captain: isViceCaptain ? 'true' : 'false',
      };
    });
    dispatch({
      type: 'REPLACE_TEAM',
      payload: {
        value: newTeamWithCaps,
      },
    });
    history.push({
      pathname: `/${params.langId}/home`,
    });
  }

  return (
    <>
      <div className='proposed-transfers-card__index'>
        {intl.messages['optim.option']} {i + 1}
      </div>
      <div className='proposed-transfers-card__wrapper'>
        <div className='proposed-transfers-card__row'>
          <div className='proposed-transfers-card__item'>
            <div className='proposed-transfers-card__title'>{intl.messages['optim.in']}</div>
            {playersToAdd.map((id, i) => {
              return getPlayerElement(id, i);
            })}
          </div>
          <div className='proposed-transfers-card__item'>
            <div className='proposed-transfers-card__title'>{intl.messages['optim.out']}</div>
            {playersToRemove.map((id, i) => {
              return getPlayerElement(id, i);
            })}
          </div>
        </div>
        <hr />
        <div className='proposed-transfers-card__row'>
          <div className='proposed-transfers-card__item'>
            <div className='proposed-transfers-card__title'>{intl.messages['optim.c']}</div>
            {getPlayerElement(team.captain)}
          </div>
          <div className='proposed-transfers-card__item'>
            <div className='proposed-transfers-card__title'>{intl.messages['optim.vc']}</div>
            {getPlayerElement(team.viceCaptain)}
          </div>
        </div>
        <hr />
        <div className='proposed-transfers-card__row'>
          <div className='proposed-transfers-card__item'>
            <div className='proposed-transfers-card__title'>{intl.messages['optim.original']}</div>
            <div className='proposed-transfers-card__number'>{totalPointsOld}</div>
          </div>
          <div className='proposed-transfers-card__item'>
            <div className='proposed-transfers-card__title'>{intl.messages['optim.suggested']}</div>
            <div className='proposed-transfers-card__number'>{totalPointsNew}</div>
          </div>
        </div>
        <div className='proposed-transfers-card__row'>
          <div className='proposed-transfers-card__button'>
            <Button
              onClick={showTeamWithChanges}
              text={intl.messages['optim.show']}
              variant='darkPrimary'
            />
          </div>
        </div>
      </div>
    </>
  );
};

export default ProposedTransfersCard;
