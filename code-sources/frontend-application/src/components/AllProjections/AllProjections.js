import { useIntl } from 'react-intl';
import AllProjectionsTable from './AllProjectionsTable';
import Card from '../Common/Card';
import React, { useEffect } from 'react';

const AllProjections = () => {
  const intl = useIntl();
  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);
  return (
    <div className='main container'>
      <div className='row'>
        <div className='col-xl-12 d-flex flex-column'>
          <Card title={intl.messages['menu.projections.title']}>
            <AllProjectionsTable />
          </Card>
        </div>
      </div>
    </div>
  );
};

export default AllProjections;
