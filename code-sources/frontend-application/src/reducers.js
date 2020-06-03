import { appReducer } from './reducers/appReducer';
import { combineReducers } from 'redux';
import { connectRouter } from 'connected-react-router';

export default history =>
  combineReducers({
    router: connectRouter(history),
    app: appReducer,
  });
