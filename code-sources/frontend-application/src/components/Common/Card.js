import React, { useEffect, useState } from 'react';

const Card = ({ children, title, hidden }) => {
  const [open, setOpen] = useState(true);

  useEffect(() => {
    if (hidden !== undefined) {
      setOpen(!hidden);
    }
  }, [hidden]);

  const renderDown = () => {
    return (
      <svg
        className='icon icon-chevron-bottom'
        viewBox='0 0 32 32'
        aria-hidden='true'
        fill='#6a6a6a'
      >
        <path d='M16.003 18.626l7.081-7.081L25 13.46l-8.997 8.998-9.003-9 1.917-1.916z' />
      </svg>
    );
  };

  const renderUp = () => {
    return (
      <svg viewBox='0 0 32 32' className='icon icon-chevron-top' aria-hidden='true' fill='#6a6a6a'>
        <path d='M15.997 13.374l-7.081 7.081L7 18.54l8.997-8.998 9.003 9-1.916 1.916z' />
      </svg>
    );
  };

  const toggleChevron = () => {
    setOpen(prevState => !prevState);
  };

  const renderChevron = () => {
    if (open) {
      return renderUp();
    }
    return renderDown();
  };

  return (
    <div className='container'>
      <div className='card board-shadow'>
        {title && (
          <div className='card__header'>
            <div className='card__filler--inactive' />
            <div className='card__title'>{title}</div>
            <div className='card__filler' onClick={toggleChevron}>
              {renderChevron()}
            </div>
          </div>
        )}
        <div className={open ? '' : 'invisible'}>{children}</div>
      </div>
    </div>
  );
};

export default Card;
