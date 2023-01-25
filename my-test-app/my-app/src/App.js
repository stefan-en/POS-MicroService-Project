 import logo from './logo.svg';
import './App.css';
// import SongList from './component/SongList';
// import UserList from './component/UserList';
import LoginForm from './component/LoginForum';
import Home from './component/Home';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';

function App() {
  return (

    <div>
      <p><img src={logo} className="App-logo" alt="logo" /></p>
      <Router>
      <Routes>
        <Route path='/' element={<LoginForm/>} />
        <Route path='/home' element={<Home/>} />
      </Routes>
    </Router>
    </div>
    
  );
}

export default App;
