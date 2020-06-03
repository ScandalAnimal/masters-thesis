import 'rc-slider/assets/index.css';
import { useHistory, useParams } from 'react-router';
import { useIntl } from 'react-intl';
import Button from '../Button/Button';
import OptimizeSlider from './OptimizeSlider';
import React, { useState } from 'react';

const OptimizeOptions = ({ onClick }) => {
  const [transfers, setTransfers] = useState(1);
  const [gameWeek, setGameWeek] = useState(1);
  const [options, setOptions] = useState(3);
  const [selectionTechnique, setSelectionTechnique] = useState('max');
  const params = useParams();
  const history = useHistory();
  const intl = useIntl();

  function setSelection(event) {
    event.persist();
    setSelectionTechnique(event.target.value);
  }

  function onButtonClick() {
    onClick(transfers, selectionTechnique, gameWeek, options);
  }

  return (
    <div className='optimize-options col'>
      <div className='optimize-option-wrapper row'>
        <div className='col-xl-4 text-left'>{intl.messages['optim.transfers']}</div>
        <div className='col-xl-8 text-center'>
          <OptimizeSlider
            setValue={setTransfers}
            min={0}
            max={2}
            withWildcard={false}
            selected={1}
          />
        </div>
      </div>
      <div className='optimize-option-wrapper row'>
        <div className='col-xl-4 text-left'>{intl.messages['optim.tech']}</div>
        <div className='col-xl-8 text-center'>
          <div className='row d-flex align-items-center'>
            <input
              onChange={setSelection}
              type='radio'
              name='selection-technique'
              value='max'
              id='selection-technique-1'
              checked={selectionTechnique === 'max'}
            />
            <label htmlFor='selection-technique-1'>{intl.messages['optim.tech.1']}</label>
          </div>
          <div className='row d-flex align-items-center'>
            <input
              onChange={setSelection}
              type='radio'
              name='selection-technique'
              value='total'
              id='selection-technique-2'
              checked={selectionTechnique === 'total'}
            />
            <label htmlFor='selection-technique-2'>{intl.messages['optim.tech.2']}</label>
          </div>
          <div className='row d-flex align-items-center'>
            <input
              onChange={setSelection}
              type='radio'
              name='selection-technique'
              value='form'
              id='selection-technique-3'
              checked={selectionTechnique === 'form'}
            />
            <label htmlFor='selection-technique-3'>{intl.messages['optim.tech.3']}</label>
          </div>
        </div>
      </div>
      <div className='optimize-disclaimer'>{intl.messages['optim.disclaimer']}</div>
      <div className='optimize-option-wrapper row'>
        <div className='col-xl-4 text-left'>{intl.messages['optim.gws']}</div>
        <div className='col-xl-8 text-center'>
          <OptimizeSlider setValue={setGameWeek} min={1} max={3} />
        </div>
      </div>
      <div className='optimize-option-wrapper row'>
        <div className='col-xl-4 text-left'>{intl.messages['optim.tips']}</div>
        <div className='col-xl-8 text-center'>
          <OptimizeSlider setValue={setOptions} min={1} max={5} selected={3} />
        </div>
      </div>
      <div className='row d-flex justify-content-center'>
        <Button
          onClick={onButtonClick}
          text={intl.messages['optim.button']}
          variant='darkPrimary'
        />
      </div>
      <div className='row justify-content-center'>
        <Button
          onClick={() => {
            history.push({
              pathname: `/${params.langId}/home`,
            });
          }}
          text={intl.messages['cancel']}
          variant='darkSecondary'
        />
      </div>
    </div>
  );
};

export default OptimizeOptions;
