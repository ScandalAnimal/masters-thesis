import { Form } from 'react-bootstrap';
import { login, manualTeam } from '../../reducers/appActions';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory, useParams } from 'react-router-dom';
import { useIntl } from 'react-intl';
import Button from '../Button/Button';
import React, { useEffect, useState } from 'react';
import manLogo from '../../assets/images/man.png';
import plLogo from '../../assets/images/p1trans.png';

function Login() {
  const dispatch = useDispatch();
  const history = useHistory();
  const intl = useIntl();
  const loginError = useSelector(state => state.app.loginError);

  const params = useParams();
  const [inputEmail, setInputEmail] = useState('');
  const [inputPassword, setInputPassword] = useState('');

  const handleChangeEmail = e => {
    setInputEmail(e.target.value);
  };

  const handleChangePassword = e => {
    setInputPassword(e.target.value);
  };

  function handleManualInput(e) {
    e.preventDefault();
    manualTeam(dispatch, history, params);
  }

  function handleSubmit(e) {
    e.preventDefault();
    login(dispatch, history, params, inputEmail, inputPassword);
  }

  useEffect(() => {
    window.scrollTo(0, 0);
    dispatch({
      type: 'CLEAR',
    });
  }, [dispatch]);

  return (
    <div className='login'>
      <div className='homepage-logo'>
        <img src={plLogo} alt='premier league logo' />
      </div>
      <div className='login-bottom-section row-cols-2'>
        <div className='login-bottom-col header-link col-md-4'>
          <Form>
            <Form.Group controlId='formEmail' className={'login-input'}>
              {loginError && (
                <Form.Label className='error-text'>{intl.messages['login.fail']}</Form.Label>
              )}
              <Form.Label>{intl.messages['login.email']}</Form.Label>
              <Form.Control type='email' value={inputEmail} onChange={e => handleChangeEmail(e)} />
              <Form.Text className='text-muted'>{intl.messages['login.email.hint']}</Form.Text>
            </Form.Group>
            <Form.Group controlId='formPassword' className={'login-input'}>
              <Form.Label>{intl.messages['login.pwd']}</Form.Label>
              <Form.Control
                type='password'
                value={inputPassword}
                onChange={e => handleChangePassword(e)}
              />
              <Form.Text className='text-muted'>{intl.messages['login.pwd.hint']}</Form.Text>
            </Form.Group>
            <div className='login-disclaimer'>
              {intl.messages['login.info.1']}
              <br />
              {intl.messages['login.info.2']}
            </div>
            <div className='login-buttons'>
              <Button
                variant='lightPrimary'
                text={intl.messages['submit']}
                type='submit'
                onClick={handleSubmit}
              />
              <Button
                variant='lightPrimary'
                text={intl.messages['manual']}
                type='submit'
                onClick={handleManualInput}
              />
            </div>
          </Form>
        </div>
        <div className='login-bottom-col login-bottom-right col-md-8'>
          <div className='login-bottom-title'>{intl.messages['title']}</div>
          <div className='login-bottom-text'>{intl.messages['subtitle']}</div>
          <div className='homepage-logo-bottom'>
            <img src={manLogo} alt='logo' />
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
