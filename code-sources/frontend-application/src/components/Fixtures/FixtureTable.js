import { mockFixtures } from '../../mocks/mockFixtures';
import { useSelector } from 'react-redux';
import Button from '../Button/Button';
import React from 'react';
import teamLogoService from '../../service/teamLogoService';

function renderTeamName(teams, id, isHome) {
  const className = isHome ? 'd-flex flex-row' : 'd-flex flex-row-reverse';
  return (
    <div className={className}>
      <div className='fixture-team-icon'>
        <img src={teamLogoService.getTeamLogo(id)} alt='Team logo' width={25} />
      </div>
      <div className='fixture-team-name'>{teams[id].name}</div>
    </div>
  );
}

function renderFixture(fixture, teams) {
  return (
    <div className='fixture-wrapper row' key={fixture.home + '-' + fixture.away}>
      <div className='col-xl-4 text-left'>{renderTeamName(teams, fixture.home, true)}</div>
      <div className='col-xl-4 text-center'> - </div>
      <div className='col-xl-4 text-right'>{renderTeamName(teams, fixture.away, false)}</div>
    </div>
  );
}

function renderFixtureHeader(gameWeek) {
  const next = gameWeek >= 38 ? null : gameWeek + 1;
  const prev = gameWeek === 1 ? null : gameWeek - 1;

  return (
    <div className='fixture-header row'>
      <div className='col-xl-4 text-left'>
        <Button onClick={() => {}} text={'Week ' + prev} variant='darkPrimary' />
      </div>
      <div className='col-xl-4 text-center fixture-header-title'>
        {'Week ' + gameWeek + ' fixtures'}
      </div>
      <div className='col-xl-4 text-right'>
        <Button onClick={() => {}} text={'Week ' + next} variant='darkPrimary' />
      </div>
    </div>
  );
}

const FixtureTable = () => {
  const fixtures = mockFixtures; // TODO replace with API call
  const gameWeek = 30; // TODO replace with API call
  const teams = useSelector(state => state.app.teams);

  return (
    <div className='fixtures col'>
      {renderFixtureHeader(gameWeek)}
      {fixtures.map(fixture => {
        return renderFixture(fixture, teams);
      })}
    </div>
  );
};

export default FixtureTable;
