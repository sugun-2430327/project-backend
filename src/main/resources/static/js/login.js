// Basic React login component
import React, { useState } from 'react';
import axios from 'axios';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [token, setToken] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username,
        password
      });
      
      setToken(response.data.token);
      setMessage(`Login successful! Welcome ${response.data.username}`);
      
      // Store token in localStorage
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));
    } catch (error) {
      setMessage('Login failed. Please check your credentials.');
      console.error('Login error:', error);
    }
  };

  const handleTestEndpoint = async (endpoint) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/test/${endpoint}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      setMessage(response.data);
    } catch (error) {
      setMessage(`Access denied to ${endpoint} endpoint`);
      console.error('API error:', error);
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div className="form-group">
          <label>Username:</label>
          <input 
            type="text" 
            value={username} 
            onChange={(e) => setUsername(e.target.value)} 
            required 
          />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input 
            type="password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
            required 
          />
        </div>
        <button type="submit">Login</button>
      </form>
      
      {token && (
        <div className="test-endpoints">
          <h3>Test Endpoints</h3>
          <button onClick={() => handleTestEndpoint('all')}>Public Content</button>
          <button onClick={() => handleTestEndpoint('customer')}>Customer Content</button>
          <button onClick={() => handleTestEndpoint('agent')}>Agent Content</button>
          <button onClick={() => handleTestEndpoint('admin')}>Admin Content</button>
        </div>
      )}
      
      {message && <div className="message">{message}</div>}
    </div>
  );
};

export default Login;
