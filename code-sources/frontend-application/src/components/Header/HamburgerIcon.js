import React from 'react';

const HamburgerIcon = ({ action }) => {
  return (
    <div className='nav-icon' onClick={action}>
      <div />
    </div>
  );
};

export default HamburgerIcon;
