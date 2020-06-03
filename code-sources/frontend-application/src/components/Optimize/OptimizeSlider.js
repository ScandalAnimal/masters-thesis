import 'rc-slider/assets/index.css';
import React from 'react';
import Slider from 'rc-slider/lib/Slider';

const OptimizeSlider = ({ setValue, min, max, withWildcard, selected }) => {
  const markStyle = {
    color: '#fff',
    fontSize: 16,
  };
  let marks = {};
  for (let i = min; i <= max; i++) {
    marks[i] = {
      label: i.toString(),
      style: markStyle,
    };
  }
  if (withWildcard) {
    marks[max + 1] = {
      label: 'Wildcard',
      style: markStyle,
    };
  }
  return (
    <div className='optimize-slider'>
      <Slider
        min={min}
        max={withWildcard ? max + 1 : max}
        step={1}
        onChange={setValue}
        defaultValue={selected !== undefined ? selected : min}
        marks={marks}
        railStyle={{ height: 8, backgroundColor: '#eee' }}
        trackStyle={{ height: 8, backgroundColor: '#a9c8c0' }}
        handleStyle={{
          border: 'none',
          height: 16,
          width: 16,
          backgroundColor: '#a9c8c0',
        }}
        dotStyle={{ display: 'none' }}
      />
    </div>
  );
};

export default OptimizeSlider;
