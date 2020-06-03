import React from 'react';

function Checkbox({ text, action, checked }) {
  return (
    <div>
      <label className='checkbox-container'>
        {text}
        <input type='checkbox' checked={checked} onChange={action} />
        <span className='checkbox-checkmark' />
      </label>
    </div>
  );
}

export default Checkbox;
