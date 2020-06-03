import { Form } from 'react-bootstrap';
import { getAllInjuries } from '../../reducers/appActions';
import { useDispatch, useSelector } from 'react-redux';
import { useIntl } from 'react-intl';
import Checkbox from '../Common/Checkbox';
import Loader from '../Homepage/Loader';
import React, { useEffect, useState } from 'react';
import UnavailablePlayersList from './UnavailablePlayersList';
import playerService from '../../service/playerService';

const UnavailablePlayersTable = () => {
  const dispatch = useDispatch();
  const intl = useIntl();
  const teams = useSelector(state => state.app.teams);
  const injuries = useSelector(state => state.app.injuries);
  const [selectedTeam, setSelectedTeam] = useState(0);
  const [selectedSortBy, setSelectedSortBy] = useState(0);
  const [filteredPlayers, setFilteredPlayers] = useState(injuries);
  const [playerList, setPlayerList] = useState(filteredPlayers);
  const [loading, setLoading] = useState(true);
  const [excludeLoans, setExcludeLoans] = useState(false);

  useEffect(() => {
    if (injuries.length === 0) {
      getAllInjuries(dispatch);
    }
  }, [dispatch]);

  useEffect(() => {
    if (teams !== null) {
      setLoading(false);
    }
  }, [teams]);

  useEffect(() => {
    filterPlayerAction(injuries);
  }, [injuries]);

  function renderSelectBoxes() {
    const teamNames = [intl.messages['sort.teams.all']];
    const sortByOptions = [
      '-',
      intl.messages['sort.name'],
      intl.messages['sort.team'],
      intl.messages['sort.yellow'],
      intl.messages['sort.red'],
    ];

    for (let i = 0; i < teams.length; i++) {
      teamNames.push(teams[i].name);
    }

    function filterPlayers(v) {
      let tmpFiltered;

      if (parseInt(v) === 0) {
        tmpFiltered = filteredPlayers;
      } else {
        const selectedT = teams.filter(team => team.id === parseInt(v));
        tmpFiltered = filteredPlayers.filter(item => {
          return parseInt(item.team_code) === parseInt(selectedT[0].code);
        });
      }
      filterPlayerAction(tmpFiltered);
    }

    function compareNames(a, b) {
      // Use toUpperCase() to ignore character casing
      const nameA = a.display_name.toUpperCase();
      const nameB = b.display_name.toUpperCase();

      let comparison = 0;
      if (nameA > nameB) {
        comparison = 1;
      } else if (nameA < nameB) {
        comparison = -1;
      }
      return comparison;
    }

    function sortPlayers(option) {
      if (option === 1) {
        // NAME
        const tmpPlayers = filteredPlayers.map(player => {
          return {
            ...player,
            display_name: playerService.getPlayerName(player) + player.first_name,
          };
        });
        return tmpPlayers.sort(compareNames);
      } else if (option === 2) {
        // TEAM
        const tmpPlayers = filteredPlayers.map(player => {
          return {
            ...player,
          };
        });
        return tmpPlayers.sort((a, b) => {
          const bTeam = teams.find(team => team.code === b.team_code);
          const aTeam = teams.find(team => team.code === a.team_code);
          return bTeam.name - aTeam.name;
        });
      } else if (option === 3) {
        // YELLOW CARDS
        const tmpPlayers = filteredPlayers.map(player => {
          return {
            ...player,
          };
        });
        return tmpPlayers.sort((a, b) => {
          return b.yellow_cards - a.yellow_cards;
        });
      } else if (option === 4) {
        // RED CARDS
        const tmpPlayers = filteredPlayers.map(player => {
          return {
            ...player,
          };
        });
        return tmpPlayers.sort((a, b) => {
          return b.red_cards - a.red_cards;
        });
      }
      return injuries;
    }

    function changeTeam(e) {
      const v = e.target.value;
      setSelectedTeam(v);
      filterPlayers(v);
    }

    function changeSortBy(e) {
      const v = e.target.value;
      setSelectedSortBy(v);
      const sortedPlayers = sortPlayers(parseInt(v));
      filterPlayerAction(sortedPlayers);
    }
    return (
      <div className='unavailable-selectbox-wrapper'>
        <Form>
          <Form.Group
            controlId='unavailableForm-team'
            className={'d-flex flex-column custom-dropdown'}
          >
            <Form.Label>{intl.messages['sort.team']}</Form.Label>
            <Form.Control as='select' onChange={changeTeam} value={selectedTeam}>
              {teamNames.map((team, i) => {
                return (
                  <option key={team} value={i}>
                    {team}
                  </option>
                );
              })}
            </Form.Control>
          </Form.Group>
          <Form.Group
            controlId='unavailableForm-sort'
            className={'d-flex flex-column custom-dropdown'}
          >
            <Form.Label>{intl.messages['sort.sortby']}</Form.Label>
            <Form.Control as='select' onChange={changeSortBy} value={selectedSortBy}>
              {sortByOptions.map((option, i) => {
                return (
                  <option key={option} value={i}>
                    {option}
                  </option>
                );
              })}
            </Form.Control>
          </Form.Group>
        </Form>
      </div>
    );
  }

  function getPlayersWithoutLoans(players) {
    return players.filter(
      player =>
        !player.news.includes('Joined') &&
        !player.news.includes('Left') &&
        !player.news.includes('terminated')
    );
  }

  function filterPlayerAction(newFilteredPlayers) {
    setFilteredPlayers(newFilteredPlayers);
    if (excludeLoans) {
      const withoutLoans = getPlayersWithoutLoans(newFilteredPlayers);
      setPlayerList(withoutLoans);
    } else {
      setPlayerList(newFilteredPlayers);
    }
  }

  const excludeLoansAction = () => {
    if (excludeLoans) {
      setPlayerList(filteredPlayers);
    } else {
      const withoutLoans = getPlayersWithoutLoans(filteredPlayers);
      setPlayerList(withoutLoans);
    }
    setExcludeLoans(!excludeLoans);
  };

  function renderCheckboxes() {
    return (
      <div className='unavailable-checkboxes'>
        <Checkbox
          action={excludeLoansAction}
          checked={excludeLoans}
          text={intl.messages['table.exclude']}
        />
      </div>
    );
  }

  function renderPlayerList() {
    return <UnavailablePlayersList filteredPlayers={playerList} />;
  }

  return (
    <div className='unavailable'>
      {loading && <Loader text={intl.messages['loading.injuries']} />}
      {!loading && filteredPlayers.length > 0 && (
        <>
          <div className='unavailable-top'>
            {renderSelectBoxes()}
            {renderCheckboxes()}
          </div>
          {renderPlayerList()}
        </>
      )}
    </div>
  );
};

export default UnavailablePlayersTable;
