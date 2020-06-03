import { Form } from 'react-bootstrap';
import { useIntl } from 'react-intl';
import { useSelector } from 'react-redux';
import AllProjectionsPlayerList from './AllProjectionsPlayerList';
import Loader from '../Homepage/Loader';
import React, { useEffect, useState } from 'react';
import _ from 'lodash';
import playerService from '../../service/playerService';

const AllProjectionsTable = () => {
  const intl = useIntl();
  const teams = useSelector(state => state.app.teams);
  const projections = useSelector(state => state.app.projections);
  const [loading, setLoading] = useState(true);
  const [gameWeekCount, setGameWeekCount] = useState(1);
  const [displayedProjections, setDisplayedProjections] = useState(null);
  const [selectedPosition, setSelectedPosition] = useState(0);
  const [selectedTeam, setSelectedTeam] = useState(0);
  const combinedPlayers = useSelector(state => state.app.allCombinedPlayers);
  const [selectedSortBy, setSelectedSortBy] = useState(0);
  const sortByOptions = [
    '-',
    intl.messages['sort.name'],
    '1 ' + intl.messages['sort.gw'],
    '2 ' + intl.messages['sort.gw.plural'],
    '3 ' + intl.messages['sort.gw.plural'],
  ];

  useEffect(() => {
    if (projections.length > 0) {
      setLoading(false);
      const x = projections.find(projection => parseInt(projection.id) === parseInt(gameWeekCount));
      const remapped = _.mapValues(_.groupBy(x.value, 'player_name'), x =>
        x.map(y => _.omit(y, 'player_name'))
      );
      setDisplayedProjections(remapped);
    }
  }, [projections]);

  function renderSelectBoxes() {
    const positions = [
      intl.messages['sort.positions.all'],
      intl.messages['sort.positions.gk'],
      intl.messages['sort.positions.df'],
      intl.messages['sort.positions.md'],
      intl.messages['sort.positions.fw'],
    ];
    const teamNames = [intl.messages['sort.teams.all']];
    const gameWeekCounts = ['1', '2', '3'];

    for (let i = 0; i < teams.length; i++) {
      teamNames.push(teams[i].name);
    }

    function filterPlayers(type, v) {
      let combined = projections.find(
        projection => parseInt(projection.id) === parseInt(gameWeekCount)
      );
      const remapped = _.mapValues(_.groupBy(combined.value, 'player_name'), x =>
        x.map(y => _.omit(y, 'player_name'))
      );

      let tmpFiltered = [];

      if (type === 1) {
        if (parseInt(v) === 0) {
          tmpFiltered = Object.entries(remapped);
        } else {
          tmpFiltered = Object.entries(remapped).filter(([name, values]) => {
            const player = combinedPlayers.find(item => {
              let p = item.first_name + '_' + item.second_name;
              return p === name;
            });
            return parseInt(player.element_type) === parseInt(v);
          });
        }
        if (parseInt(selectedTeam) !== 0) {
          tmpFiltered = tmpFiltered.filter(([name, values]) => {
            const player = combinedPlayers.find(item => {
              let p = item.first_name + '_' + item.second_name;
              return p === name;
            });
            return parseInt(player.team) === parseInt(selectedTeam);
          });
        }
      } else if (type === 2) {
        if (parseInt(v) === 0) {
          tmpFiltered = Object.entries(remapped);
        } else {
          tmpFiltered = Object.entries(remapped).filter(([name, values]) => {
            const player = combinedPlayers.find(item => {
              let p = item.first_name + '_' + item.second_name;
              return p === name;
            });
            return parseInt(player.team) === parseInt(v);
          });
        }
        if (parseInt(selectedPosition) !== 0) {
          tmpFiltered = tmpFiltered.filter(([name, values]) => {
            const player = combinedPlayers.find(item => {
              let p = item.first_name + '_' + item.second_name;
              return p === name;
            });
            return parseInt(player.element_type) === parseInt(selectedPosition);
          });
        }
      }

      let reduced = {};
      tmpFiltered.forEach(item => {
        reduced[item[0]] = item[1];
      });
      setDisplayedProjections(reduced);
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
        const tmpPlayers = Object.entries(displayedProjections).map(([name, values]) => {
          const player = combinedPlayers.find(item => {
            let p = item.first_name + '_' + item.second_name;
            return p === name;
          });
          return {
            name: name,
            values: values,
            display_name: playerService.getPlayerName(player) + player.first_name,
          };
        });

        let sorted = tmpPlayers.sort(compareNames);
        let reduced = {};
        sorted.forEach(item => {
          reduced[item.name] = item.values;
        });
        return reduced;
      } else if (option === 2) {
        // 1 GW
        const tmpPlayers = Object.entries(displayedProjections).map(([name, values]) => {
          const player = combinedPlayers.find(item => {
            let p = item.first_name + '_' + item.second_name;
            return p === name;
          });
          return {
            name: name,
            values: values,
            display_name: playerService.getPlayerName(player) + player.first_name,
          };
        });
        let sorted = tmpPlayers.sort((a, b) => {
          return b.values[0].predicted_points - a.values[0].predicted_points;
        });
        let reduced = {};
        sorted.forEach(item => {
          reduced[item.name] = item.values;
        });

        return reduced;
      } else if (option === 3 && gameWeekCount >= 2) {
        // 2 GW
        const tmpPlayers = Object.entries(displayedProjections).map(([name, values]) => {
          const player = combinedPlayers.find(item => {
            let p = item.first_name + '_' + item.second_name;
            return p === name;
          });
          return {
            name: name,
            values: values,
            display_name: playerService.getPlayerName(player) + player.first_name,
          };
        });
        let sorted = tmpPlayers.sort((a, b) => {
          if (a.values.length < gameWeekCount) {
            a.values.push(a.values[0]);
          }
          if (b.values.length < gameWeekCount) {
            b.values.push(b.values[0]);
          }
          return b.values[1].predicted_points - a.values[1].predicted_points;
        });
        let reduced = {};
        sorted.forEach(item => {
          reduced[item.name] = item.values;
        });

        return reduced;
      } else if (option === 4 && gameWeekCount >= 3) {
        // 3 GW
        const tmpPlayers = Object.entries(displayedProjections).map(([name, values]) => {
          const player = combinedPlayers.find(item => {
            let p = item.first_name + '_' + item.second_name;
            return p === name;
          });
          return {
            name: name,
            values: values,
            display_name: playerService.getPlayerName(player) + player.first_name,
          };
        });
        let sorted = tmpPlayers.sort((a, b) => {
          if (a.values.length < gameWeekCount) {
            a.values.push(a.values[0]);
            if (a.values.length < gameWeekCount) {
              a.values.push(a.values[0]);
            }
          }
          if (b.values.length < gameWeekCount) {
            b.values.push(b.values[0]);
            if (b.values.length < gameWeekCount) {
              b.values.push(b.values[0]);
            }
          }
          return b.values[2].predicted_points - a.values[2].predicted_points;
        });
        let reduced = {};
        sorted.forEach(item => {
          reduced[item.name] = item.values;
        });

        return reduced;
      }
      return displayedProjections;
    }

    function changePosition(e) {
      const v = e.target.value;
      setSelectedPosition(v);
      filterPlayers(1, v);
    }

    function changeTeam(e) {
      const v = e.target.value;
      setSelectedTeam(v);
      filterPlayers(2, v);
    }

    function changeSortBy(e) {
      const v = e.target.value;
      setSelectedSortBy(v);
      const sortedPlayers = sortPlayers(parseInt(v));
      setDisplayedProjections(sortedPlayers);
    }

    function changeGameWeekCount(e) {
      const v = e.target.value;
      setGameWeekCount(v);
      const x = projections.find(projection => projection.id === parseInt(v));
      const remapped = _.mapValues(_.groupBy(x.value, 'player_name'), x =>
        x.map(y => _.omit(y, 'player_name'))
      );
      let tmpFiltered = Object.entries(remapped);
      if (parseInt(selectedTeam) !== 0) {
        tmpFiltered = tmpFiltered.filter(([name, values]) => {
          const player = combinedPlayers.find(item => {
            let p = item.first_name + '_' + item.second_name;
            return p === name;
          });
          return parseInt(player.team) === parseInt(selectedTeam);
        });
      }
      if (parseInt(selectedPosition) !== 0) {
        tmpFiltered = tmpFiltered.filter(([name, values]) => {
          const player = combinedPlayers.find(item => {
            let p = item.first_name + '_' + item.second_name;
            return p === name;
          });
          return parseInt(player.element_type) === parseInt(selectedPosition);
        });
      }
      let reduced = {};
      tmpFiltered.forEach(item => {
        reduced[item[0]] = item[1];
      });
      setDisplayedProjections(reduced);
    }
    return (
      <div className='all-projections-selectbox-wrapper'>
        <Form>
          <Form.Group
            controlId='allProjectionsForm-gameweekCount'
            className={'d-flex flex-column custom-dropdown'}
          >
            <Form.Label>{intl.messages['sort.gw.plural']}</Form.Label>
            <Form.Control as='select' onChange={changeGameWeekCount} value={gameWeekCount}>
              {gameWeekCounts.map((gw, i) => {
                return (
                  <option key={gw} value={i + 1}>
                    {gw}
                  </option>
                );
              })}
            </Form.Control>
          </Form.Group>
          <Form.Group
            controlId='allProjectionsForm-position'
            className={'d-flex flex-column custom-dropdown'}
          >
            <Form.Label>{intl.messages['sort.position']}</Form.Label>
            <Form.Control as='select' onChange={changePosition} value={selectedPosition}>
              {positions.map((position, i) => {
                return (
                  <option key={position} value={i}>
                    {position}
                  </option>
                );
              })}
            </Form.Control>
          </Form.Group>
          <Form.Group
            controlId='allProjectionsForm-team'
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
            controlId='allProjectionsForm-sort'
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

  function renderPlayerList() {
    return (
      <AllProjectionsPlayerList players={displayedProjections} gameWeekCount={gameWeekCount} />
    );
  }

  return (
    <div className='all-projections'>
      {loading ? (
        <Loader text={intl.messages['loading.pred']} />
      ) : (
        <>
          {renderSelectBoxes()}
          {renderPlayerList()}
        </>
      )}
    </div>
  );
};

export default AllProjectionsTable;
