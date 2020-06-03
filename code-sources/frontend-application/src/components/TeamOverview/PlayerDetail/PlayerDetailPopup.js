import { Modal } from 'react-bootstrap';
import { POSITIONS } from '../../../constants';
import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import Button from '../../Button/Button';
import PlayerHistoryTable from './PlayerHistoryTable';
import PlayerSmallIcon from './PlayerSmallIcon';
import React, { useState } from 'react';
import playerService from '../../../service/playerService';

function PlayerDetailPopup(props) {
  const player = useSelector(state => state.app.selectedPlayer);
  const { currentTeam, removedPlayers } = useSelector(state => state.app.edit);
  const [validationMessage, setValidationMessage] = useState('');
  const [subs, setSubs] = useState([]);
  const teams = useSelector(state => state.app.teams);
  const dispatch = useDispatch();
  const [playerDataDisplayed, setPlayerDataDisplayed] = useState(false);
  const intl = useIntl();
  const injuries = useSelector(state => state.app.injuries);
  const isUnavailable =
    player === null
      ? false
      : playerService.isPlayerUnavailable(injuries, player.first_name, player.second_name);
  const injuryStatus =
    player === null
      ? ''
      : playerService.getInjuryStatus(injuries, player.first_name, player.second_name);
  function getTeamName() {
    for (let i = 0; i < teams.length; i++) {
      const team = teams[i];
      if (team.id === parseInt(player.team)) {
        return team.name;
      }
    }
    return null;
  }

  function closeModal() {
    setValidationMessage('');
    setSubs([]);
    setPlayerDataDisplayed(false);
    setPlayerDataDisplayed(false);
    props.onHide();
  }

  function onSubClick(sub) {
    makeSubstitution(sub, player);
  }

  function makeSubstitution(on, off) {
    dispatch({
      type: 'MAKE_SUBSTITUTION',
      payload: {
        on: on,
        off: off,
      },
    });
    closeModal();
  }

  function openSubs() {
    let highlightedSubs = [];
    switch (player.element_type) {
      case POSITIONS.GK:
        const on = currentTeam.find(elem => {
          return elem.position > 11 && elem.element_type === POSITIONS.GK;
        });
        makeSubstitution(on, player);
        break;
      case POSITIONS.DF:
        const defs = currentTeam.filter(elem => {
          return elem.position <= 11 && elem.element_type === POSITIONS.DF;
        });
        if (defs.length > 3) {
          highlightedSubs = currentTeam.filter(elem => {
            return elem.position > 11 && elem.element_type !== POSITIONS.GK;
          });
        } else {
          highlightedSubs = currentTeam.filter(elem => {
            return elem.position > 11 && elem.element_type === POSITIONS.DF;
          });
        }
        break;
      case POSITIONS.MF:
        highlightedSubs = currentTeam.filter(elem => {
          return elem.position > 11 && elem.element_type !== POSITIONS.GK;
        });
        break;
      case POSITIONS.FW:
        const fws = currentTeam.filter(elem => {
          return elem.position <= 11 && elem.element_type === POSITIONS.FW;
        });
        if (fws.length > 1) {
          highlightedSubs = currentTeam.filter(elem => {
            return elem.position > 11 && elem.element_type !== POSITIONS.GK;
          });
        } else {
          highlightedSubs = currentTeam.filter(elem => {
            return elem.position > 11 && elem.element_type === POSITIONS.FW;
          });
        }
        break;
      default:
        break;
    }
    const newSubs = [];
    highlightedSubs.forEach(sub => {
      newSubs.push(sub);
    });
    setSubs(newSubs);
  }

  function isPlayerInArray(p, a) {
    let result = false;
    a.forEach(value => {
      if (value.id === p.id) {
        result = true;
      }
    });
    return result;
  }

  function removeFromSquad() {
    dispatch({
      type: 'REMOVE_PLAYER',
      payload: {
        value: player,
      },
    });
    closeModal();
  }

  function addToSquadFromRemoved() {
    dispatch({
      type: 'ADD_PLAYER_TO_SQUAD_FROM_REMOVED',
      payload: {
        value: player,
      },
    });
    closeModal();
  }

  function addToSquadFromTransferMarket() {
    dispatch({
      type: 'ADD_PLAYER_TO_SQUAD_FROM_TRANSFER_MARKET',
      payload: {
        value: player,
      },
    });
    closeModal();
  }

  function selectAsCaptain() {
    dispatch({
      type: 'SELECT_CAPTAIN',
      payload: {
        value: player,
      },
    });
    closeModal();
  }

  function selectAsViceCaptain() {
    dispatch({
      type: 'SELECT_VICE_CAPTAIN',
      payload: {
        value: player,
      },
    });
    closeModal();
  }

  function isTeamFull() {
    return currentTeam.length === 15;
  }

  function getPlayerCountOnPosition(position) {
    const team = currentTeam.filter(player => player.element_type === position);
    return team.length;
  }

  function doesPlayerTeamHaveSpaces() {
    const playerTeamCodes = currentTeam.map(player => player.team);
    let occurrences = [];
    for (let i = 1; i <= 20; i++) {
      occurrences[i - 1] = playerTeamCodes.filter(code => parseInt(code) === i).length;
    }
    return occurrences[player.team - 1] < 3;
  }

  function shouldShowAddButton() {
    if (isTeamFull()) {
      return false;
    }
    if (!doesPlayerTeamHaveSpaces()) {
      return false;
    }
    const playerPosition = player.element_type;

    const count = getPlayerCountOnPosition(playerPosition);
    switch (playerPosition) {
      case POSITIONS.GK:
        return count < 2;
      case POSITIONS.DF:
      case POSITIONS.MF:
        return count < 5;
      case POSITIONS.FW:
        return count < 3;
      default:
        return false;
    }
  }

  function renderPlayerData() {
    return <PlayerHistoryTable player={player} />;
  }

  return (
    <Modal
      {...props}
      onHide={closeModal}
      aria-labelledby='contained-modal-title-vcenter'
      dialogClassName={playerDataDisplayed ? 'player-data-displayed' : ''}
    >
      <Modal.Header>
        <Modal.Title id='contained-modal-title-vcenter' className='player-detail-popup-title'>
          {intl.messages['detail.title']}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {player && (
          <div className='container player-detail-popup-body'>
            <div className='player-detail-popup-body__wrapper'>
              <div className='row'>
                <div className='col'>{intl.messages['table.name']}:</div>
                <div className='col'>{playerService.getPlayerDetailName(player)}</div>
              </div>
              <div className='row'>
                <div className='col'>{intl.messages['table.team']}:</div>
                <div className='col'>{getTeamName()}</div>
              </div>
              <div className='row'>
                <div className='col'>{intl.messages['sort.position']}:</div>
                <div className='col'>{intl.messages[playerService.getPositionLabel(player)]}</div>
              </div>
              <div className='row'>
                <div className='col'>{intl.messages['table.total']}</div>
                <div className='col'>{player.total_points}</div>
              </div>
              <div className='row'>
                <div className='col'>{intl.messages['detail.percent']}</div>
                <div className='col'>{player.selected_by_percent + '%'}</div>
              </div>
              <div className='row spacing' />
              {isUnavailable && <div className='row injury-status'>{injuryStatus}</div>}
              <div className='row spacing' />
              {!isPlayerInArray(player, removedPlayers) && player.position <= 11 && (
                <div className='row player-detail-popup-button-row'>
                  <Button
                    onClick={openSubs}
                    text={intl.messages['detail.sub']}
                    variant='lightPrimary'
                  />
                  {subs.length > 0 && (
                    <div className='player-detail-popup__subs'>
                      {subs.map(sub => {
                        return (
                          <PlayerSmallIcon
                            key={sub.id}
                            player={sub}
                            onClick={() => onSubClick(sub)}
                          />
                        );
                      })}
                    </div>
                  )}
                </div>
              )}
              {isPlayerInArray(player, currentTeam) && (
                <div className='row player-detail-popup-button-row'>
                  <Button
                    onClick={removeFromSquad}
                    text={intl.messages['detail.remove']}
                    variant='lightPrimary'
                  />
                </div>
              )}
              {isPlayerInArray(player, removedPlayers) && shouldShowAddButton() && (
                <div className='row player-detail-popup-button-row'>
                  <Button
                    onClick={addToSquadFromRemoved}
                    text={intl.messages['detail.add']}
                    variant='lightPrimary'
                  />
                </div>
              )}
              {!isPlayerInArray(player, removedPlayers) && shouldShowAddButton() && (
                <div className='row player-detail-popup-button-row'>
                  <Button
                    onClick={addToSquadFromTransferMarket}
                    text={intl.messages['detail.add']}
                    variant='lightPrimary'
                  />
                </div>
              )}
              {!isPlayerInArray(player, removedPlayers) && player.is_captain === 'false' && (
                <div className='row player-detail-popup-button-row'>
                  <Button
                    onClick={selectAsCaptain}
                    text={intl.messages['detail.add.c']}
                    variant='lightPrimary'
                  />
                </div>
              )}
              {!isPlayerInArray(player, removedPlayers) && player.is_vice_captain === 'false' && (
                <div className='row player-detail-popup-button-row'>
                  <Button
                    onClick={selectAsViceCaptain}
                    text={intl.messages['detail.add.vc']}
                    variant='lightPrimary'
                  />
                </div>
              )}
              {validationMessage !== '' && (
                <div className='row error-message'>{validationMessage}</div>
              )}
              <div className='row spacing' />
              <div className='row player-detail-popup-button-row'>
                <Button
                  onClick={() => setPlayerDataDisplayed(!playerDataDisplayed)}
                  text={
                    playerDataDisplayed
                      ? intl.messages['detail.hide']
                      : intl.messages['detail.show']
                  }
                  variant='lightPrimary'
                />
              </div>
            </div>

            {playerDataDisplayed && renderPlayerData()}
          </div>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button
          onClick={closeModal}
          text={intl.messages['detail.close']}
          variant='lightSecondary'
        />
      </Modal.Footer>
    </Modal>
  );
}

export default PlayerDetailPopup;
