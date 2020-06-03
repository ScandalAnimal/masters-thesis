import React from 'react';
import ReactLoading from 'react-loading';

export default function Loader({ text }) {
  return (
    <div className='loader-wrapper'>
      <ReactLoading type='bars' color='#698396' />
      {text !== undefined && <div className='loader-text'>{text}</div>}
    </div>
  );
}
