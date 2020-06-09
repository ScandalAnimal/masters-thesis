import { useIntl } from 'react-intl';
import Card from '../Common/Card';
import React, { useEffect } from 'react';
import StatisticsTable from './StatisticsTable';
import UnavailablePlayersTable from './UnavailablePlayersTable';

const Statistics = () => {
  const intl = useIntl();
  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);
  return (
    <div className='main container statistics-container'>
      <div className='row'>
        <div className='col-xl-12 d-flex flex-column'>
          <Card title={intl.messages['menu.playerstats']}>
            <StatisticsTable />
          </Card>
          <Card title={intl.messages['menu.unavailable']}>
            <UnavailablePlayersTable />
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Statistics;
