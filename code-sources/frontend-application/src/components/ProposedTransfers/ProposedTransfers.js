import { useIntl } from 'react-intl';
import { useSelector } from 'react-redux';
import ProposedTransfersCard from './ProposedTransfersCard';
import React from 'react';

function ProposedTransfers() {
  const intl = useIntl();
  const { proposedTeams } = useSelector(state => state.app);

  if (proposedTeams.length === 0) {
    return null;
  }
  return (
    <>
      <div className='proposed-transfers-description'>
        {intl.messages['optim.info.1']}
        <br />
        {intl.messages['optim.info.2']}
      </div>
      {proposedTeams.map((team, i) => {
        return <ProposedTransfersCard team={team} key={i} i={i} />;
      })}
    </>
  );
}

export default ProposedTransfers;
