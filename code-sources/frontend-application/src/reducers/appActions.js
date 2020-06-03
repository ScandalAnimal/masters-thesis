import { API_URL } from '../constants';

export function getAllPlayers(dispatch) {
  dispatch({
    type: 'SET_LOADING',
    payload: {
      value: true,
    },
  });
  const url = API_URL + '/api/player';
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(response => response.json())
    .then(json => {
      const body = json.data;
      // const body = mockApiPlayers.data;
      dispatch({
        type: 'SET_LOADING',
        payload: {
          value: false,
        },
      });
      dispatch({
        type: 'SET_ALL_PLAYERS',
        payload: {
          value: body,
        },
      });
    })
    .catch(e => {
      console.log(e);
    });
}

export function getAllPlayerIds(dispatch) {
  dispatch({
    type: 'SET_LOADING',
    payload: {
      value: true,
    },
  });
  const url = API_URL + '/api/player/ids';
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(response => response.json())
    .then(json => {
      const body = json.data;
      // const body = mockPlayerIds.data;
      dispatch({
        type: 'SET_LOADING',
        payload: {
          value: false,
        },
      });
      dispatch({
        type: 'SET_ALL_PLAYER_IDS',
        payload: {
          value: body,
        },
      });
    })
    .catch(e => {
      console.log(e);
    });
}

export function getAllTeams(dispatch) {
  dispatch({
    type: 'SET_LOADING',
    payload: {
      value: true,
    },
  });
  const url = API_URL + '/api/team';
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(response => response.json())
    .then(json => {
      const body = json.data;
      // const body = mockTeams.data;
      dispatch({
        type: 'SET_LOADING',
        payload: {
          value: false,
        },
      });
      dispatch({
        type: 'SET_TEAMS',
        payload: {
          value: body,
        },
      });
    })
    .catch(e => {
      console.log(e);
    });
}

export function login(dispatch, history, params, email, password) {
  // const data = {
  //   login: 'mvasilisin@gmail.com',
  //   password: '0p9J2O75jRkWjVW',
  // };
  const data = {
    login: email,
    password: password,
  };
  dispatch({
    type: 'SET_LOADING',
    payload: {
      value: true,
    },
  });

  const url = API_URL + '/api/login';
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })
    .then(response => response.json())
    .then(json => {
      const body = json.data;
      // const body = mockLoginResponse.data;
      dispatch({
        type: 'SET_LOADING',
        payload: {
          value: false,
        },
      });
      dispatch({
        type: 'SET_TEAM_ID',
        payload: {
          value: body.teamId,
        },
      });
      dispatch({
        type: 'SET_TEAM_PICKS',
        payload: {
          value: body.myTeam.picks,
        },
      });
      history.push({
        pathname: `/${params.langId}/home`,
      });
      dispatch({
        type: 'UNSET_LOGIN_ERROR',
      });
    })
    .catch(e => {
      dispatch({
        type: 'SET_LOGIN_ERROR',
      });
    });
}

export function manualTeam(dispatch, history, params) {
  dispatch({
    type: 'SET_TEAM_ID',
    payload: {
      value: 'manual',
    },
  });
  dispatch({
    type: 'SET_TEAM_PICKS',
    payload: {
      value: [],
    },
  });
  history.push({
    pathname: `/${params.langId}/home`,
  });
}

export async function getAllProjections(dispatch, weekId) {
  dispatch({
    type: 'SET_LOADING',
    payload: {
      value: true,
    },
  });
  const url = API_URL + '/api/player/projected-points/' + weekId;
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return response.json();
}

export function getAllInjuries(dispatch) {
  dispatch({
    type: 'SET_LOADING',
    payload: {
      value: true,
    },
  });
  const url = API_URL + '/api/player/injuries';
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(response => response.json())
    .then(json => {
      const body = json.data;
      // let body = mockInjuries.data;
      dispatch({
        type: 'SET_LOADING',
        payload: {
          value: false,
        },
      });
      dispatch({
        type: 'SET_INJURIES',
        payload: {
          value: body,
        },
      });
    })
    .catch(e => {
      console.log(e);
    });
}

export function getPlayerDetails(dispatch, id, name) {
  dispatch({
    type: 'SET_LOADING',
    payload: {
      value: true,
    },
  });
  const url = API_URL + '/api/player/detail/' + id;
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(response => response.json())
    .then(json => {
      const body = json.data;
      // let body = mockMustafiData.data;
      dispatch({
        type: 'SET_LOADING',
        payload: {
          value: false,
        },
      });
      dispatch({
        type: 'SET_PLAYER_DETAILS',
        payload: {
          value: body,
          playerCode: name,
        },
      });
    })
    .catch(e => {
      console.log(e);
    });
}

export async function getProposedTransfersAndPredictions(dispatch, playerIds, options) {
  dispatch({
    type: 'SET_LOADING',
    payload: {
      value: true,
    },
  });
  const data = {
    team: playerIds,
    transfers: options.transfers,
    technique: options.selectionTechnique,
    gameWeeks: options.gameWeeks,
    tips: options.tips,
  };
  const url = API_URL + '/api/player/optimize';
  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  });
  return response.json();
}
