import { getProposedTransfersAndPredictions } from '../../reducers/appActions';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory, useParams } from 'react-router';
import { useIntl } from 'react-intl';
import Button from '../Button/Button';
import Card from '../Common/Card';
import Loader from '../Homepage/Loader';
import OptimizeOptions from './OptimizeOptions';
import ProposedTransfers from '../ProposedTransfers/ProposedTransfers';
import React, { useEffect, useState } from 'react';

const Optimize = () => {
  const [inProgress, setInProgress] = useState(false);
  const [hidden, setHidden] = useState(undefined);
  const [predictionsLoaded, setPredictionsLoaded] = useState(false);
  const { currentTeam } = useSelector(state => state.app.edit);
  const { proposedTeams } = useSelector(state => state.app);
  const dispatch = useDispatch();
  const history = useHistory();
  const params = useParams();
  const intl = useIntl();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  function onOptimizeClick(transfers, selectionTechnique, gameWeeks, tips) {
    setInProgress(true);
    setPredictionsLoaded(false);
    dispatch({
      type: 'SET_PROPOSED_TEAMS',
      payload: {
        value: [],
      },
    });
    setHidden(true);
    const currentTeamIds = getCurrentTeam();
    const options = {
      transfers,
      selectionTechnique,
      gameWeeks,
      tips,
    };
    getProposedTransfersAndPredictions(dispatch, currentTeamIds, options)
      .then(json => {
        const body = json.data.squads;
        dispatch({
          type: 'SET_LOADING',
          payload: {
            value: false,
          },
        });
        dispatch({
          type: 'SET_PROPOSED_TEAMS',
          payload: {
            value: body,
          },
        });
        setPredictionsLoaded(true);
      })
      .catch(e => {
        console.log(e);
      });
  }

  function getCurrentTeam() {
    return currentTeam.map(player => {
      return {
        id: player.id,
        sellingPrice: player.selling_price,
        nowCost: player.now_cost,
        purchasePrice: player.purchase_price,
        playerName: player.first_name + '_' + player.second_name,
      };
    });
  }

  const renderFullSquad = () => {
    return (
      <>
        <div className='col-xl-12 d-flex flex-column'>
          <Card title={intl.messages['optim.options']} hidden={hidden}>
            <OptimizeOptions onClick={onOptimizeClick} />
          </Card>
        </div>
        {inProgress && !predictionsLoaded && (
          <div className='col-xl-12 d-flex flex-column'>
            <Card>
              <Loader text={intl.messages['loading.pred']} />
            </Card>
          </div>
        )}
        {(predictionsLoaded || proposedTeams.length > 0) && (
          <div className='col-xl-12 d-flex flex-column'>
            <Card title={intl.messages['optim.proposed']}>
              <ProposedTransfers />
            </Card>
          </div>
        )}
      </>
    );
  };

  const renderInfo = () => {
    return (
      <div className='col-xl-12 d-flex flex-column align-items-center'>
        <div className='error-text'>{intl.messages['optim.disabled']}</div>
        <Button
          onClick={() => {
            history.push({
              pathname: `/${params.langId}/home`,
            });
          }}
          text={intl.messages['optim.fill']}
          variant='lightPrimary'
        />
      </div>
    );
  };

  const isFullSquad = () => {
    return currentTeam.length === 15;
  };

  return (
    <div className='main container optimize-container'>
      <div className='row'>{isFullSquad() ? renderFullSquad() : renderInfo()}</div>
    </div>
  );
};

export default Optimize;
