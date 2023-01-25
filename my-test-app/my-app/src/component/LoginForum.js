import React, { useState } from 'react';
import Cookies from 'js-cookie';
import {useNavigate} from 'react-router-dom'



function LoginForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    let Navigates = useNavigate()

    const handleSubmit = (e) => {
      e.preventDefault();
      fetch('http://localhost:8002/login?token', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: username,
          password: password
        })
      })
      
      .then(response => response.text())
    .then(responseText => {
    
      var token = JSON.parse(responseText)
      console.log(token["soap11env:Envelope"]["soap11env:Body"]["tns:loginResponse"]["tns:loginResult"])
      var loginResult = token["soap11env:Envelope"]["soap11env:Body"]["tns:loginResponse"]["tns:loginResult"]
      
      if (loginResult !== "False user") {
    
        Cookies.set('loginResult', loginResult);
        Navigates("/home")
      }
    })
    .catch(error => console.error(error));
  }
  
    return (
    <form onSubmit={handleSubmit}>
        <label>
        Username:
        <input type="text" value={username} onChange={e => setUsername(e.target.value)} />
        </label>
        <br />
        <label>
        Password:
        <input type="password" value={password} onChange={e => setPassword(e.target.value)} />
        </label>
        <br />
        <button type="submit">Login</button>
    </form>
      
    );
}
  
  export default LoginForm;//este