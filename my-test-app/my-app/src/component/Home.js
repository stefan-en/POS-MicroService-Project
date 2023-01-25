import React, { useState } from 'react';
import Cookies from 'js-cookie';
import {useNavigate} from 'react-router-dom'

function Home() {
  const [selectedOption, setSelectedOption] = useState("");
  const [selectedOption2, setSelectedOption2] = useState("");
  const [responseData, setResponseData] = useState(null);
  const [dataInput, setDataInput] = useState("");
  let Navigates = useNavigate()

  const handleServiceChange = (event) => {
    setSelectedOption(event.target.value);
    
  }
  const handleOperationChange = (event) =>{
    setSelectedOption2(event.target.value)
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    var token = Cookies.get('loginResult')
    switch(selectedOption) {
      case "artists":
        switch(selectedOption2) {
          case "GET":
            console.log("jvt:" + token)
            fetch("http://localhost:8002/artists",{
              method: "GET",
              headers: {
                "Content-Type": "application/json", 
                "Authorization" : token ,
              },
               
            })
            
            .then(response => response.text())
            .then(data => {
              setResponseData(data);
              console.log(data)
            })
            .catch(error => {
              console.log(error);
            });
            break;
          case "POST":
            fetch("http://localhost:8002/artists", {
              method: "POST",
              body: JSON.stringify({data: dataInput}),
              headers: { "Content-Type": "application/json", "Authorization" : token }
            })
            .then(response => response.json())
            .then(data => {
              setResponseData(data);
            })
            .catch(error => {
              console.log(error);
            });
            break;
          case "DELETE":
            fetch("http://localhost:8002/artists",{method: "DELETE"}, {
              method: "DELETE",
              body: JSON.stringify({data: dataInput}),
              headers: { "Content-Type": "application/json", "Authorization" : token }
            })
            .then(response => response.json())
            .then(data => {
              setResponseData(data);
            })
            .catch(error => {
              console.log(error);
            });
            break;
          default:
            console.log("Invalid operation");
        }
        break;
      case "songs":
        switch(selectedOption2) {
          case "GET":
            fetch("http://localhost:8002/songs",{
            method: "GET",
            headers: { "Content-Type": "application/json", "Authorization" : token }
          })
            .then(response => response.json())
            .then(data => {
              setResponseData(data);
              console.log(data)
            })
            .catch(error => {
              console.log(error);
            });
            break;
          case "POST":
            <input type="text" name="data" id="data" onChange={event => setDataInput(event.target.value)} />
            fetch("http://localhost:8002/songs", {
              method: "POST",
              body: JSON.stringify({data: dataInput}),
              headers: { "Content-Type": "application/json", "Authorization" : token }
            })
            .then(response => response.json())
            .then(data => {
              setResponseData(data);
            })
            .catch(error => {
              console.log(error);
            });
            break;
          case "DELETE":
            <input type="text" name="data" id="data" onChange={event => setDataInput(event.target.value)} />
            fetch("http://localhost:8002/songs",{method: "DELETE"}, {
              method: "DELETE",
              body: JSON.stringify({data: dataInput}),
              headers: { "Content-Type": "application/json", "Authorization" : token}
            })
            .then(response => response.json())
            .then(data => {
              setResponseData(data);
            })
            .catch(error => {
              console.log(error);
            });
            break;
          default:
            console.log("Invalid operation");
        }
        break;
      case "playlists":
        switch(selectedOption2) {
          case "GET":
            fetch("http://localhost:8002/playlists",{
            method: "GET",
            headers: { "Content-Type": "application/json", "Authorization" : token }
          })
            .then(response => response.json())
            .then(data => {
              setResponseData(data);
              console.log(data)
            })
            .catch(error => {
              console.log(error);
            });
            break;
          case "POST":
            <input type="text" name="data" id="data" onChange={event => setDataInput(event.target.value)} />
            fetch("http://localhost:8002/playlists", {
              method: "POST",
              body: JSON.stringify({data: dataInput}),
              headers: { "Content-Type": "application/json", "Authorization" : token }
            })
            .then(response => response.json())
            .then(data => {
              setResponseData(data);
            })
            .catch(error => {
              console.log(error);
            });
            break;
          case "DELETE":
            <input type="text" name="data" id="data" onChange={event => setDataInput(event.target.value)} />
            fetch("http://localhost:8002/playlists",{method: "DELETE"}, {
              method: "DELETE",
              body: JSON.stringify({data: dataInput}),
              headers: { "Content-Type": "application/json", "Authorization" : token }
            })
            .then(response => response.json())
            .then(data => {
              setResponseData(data);
            })
            .catch(error => {
              console.log(error);
            });
            break;
          default:
            console.log("Invalid operation");
        }
        break;
      default:
        console.log("Invalid service");
    }
  }

  const handleLogout = () => {
    console.log(Cookies.remove('loginResult'));
    Cookies.remove('loginResult');
    Navigates('/')
  }
  return (
    <div>
    <form onSubmit={handleSubmit}>
      <p>
      <label>
        Ce serviciu doriti facem?
        <select value={selectedOption} onChange={handleServiceChange}>
          <option value="">Selectează</option>
          <option value="artists">artisti</option>
          <option value="songs">melodii</option>
          <option value="playlists">playlist</option>
        </select>
        </label>
        </p>
        <p>
        <label>
      Ce operatie doriti facem?
        <select value={selectedOption2} onChange={handleOperationChange}>
          <option value="">Selectează</option>
          <option value="GET">GET</option>
          <option value="POST">POST</option>
          <option value="DELETE">DELETE</option>
          
        </select>
        
      </label>
      </p>
      <p><input type="text" name="data" id="data" onChange={event => setDataInput(event.target.value)} />
      <input type="submit" value="Trimite" /></p>
      
    </form>
    
    <p><button onClick={handleLogout}>Logout</button></p>
    
    </div>
  );
}

export default Home;