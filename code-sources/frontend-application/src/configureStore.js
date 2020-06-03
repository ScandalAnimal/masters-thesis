// configureStore.js

import { applyMiddleware, compose, createStore } from 'redux';
import { composeWithDevTools } from 'redux-devtools-extension';
import { createBrowserHistory } from 'history';
import createRootReducer from './reducers';
import persistState from 'redux-localstorage';
import thunk from 'redux-thunk';

export const history = createBrowserHistory();

export default function configureStore(preloadedState) {
  return createStore(
    createRootReducer(history),
    preloadedState,
    compose(composeWithDevTools(applyMiddleware(thunk), persistState(/*paths, config*/)))
  );
}
