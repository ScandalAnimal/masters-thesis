import React from 'react';

function getClassName(variant) {
  switch (variant) {
    case 'darkPrimary':
      return 'button button-dark--primary';
    case 'lightPrimary':
      return 'button button-light--primary';
    case 'darkSecondary':
      return 'button button-dark--secondary';
    case 'lightSecondary':
      return 'button button-light--secondary';
    default:
      return 'button';
  }
}

function Button({ onClick, text, variant, ...rest }) {
  const className = getClassName(variant);

  return (
    <button onClick={onClick} className={className} {...rest}>
      {text}
    </button>
  );
}

export default Button;
