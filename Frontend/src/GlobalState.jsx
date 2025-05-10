// GlobalState.js
import React, { createContext, useState, useContext } from 'react';

const GlobalStateContext = createContext();

export const GlobalStateProvider = ({ children }) => {
  const [user, setUser] = useState(null); // example global state
  const [password, setPassword] = useState(null); // example global state
  const [ID, setID] = useState(null); // example global state
  const [isProfessor, setIsProfessor] = useState(false); // example global state
  const [isStudent, setIsStudent] = useState(false); // example global state

  function loginUser({id, firstName, lastName, password, email, major}) {
    setUser({id, firstName, lastName, password, email, major });
    if (id > 100) {
      setIsProfessor(true);
      setIsStudent(false);
    }
    else if (id < 100) {
      setIsStudent(true);
      setIsProfessor(false);
    }
    console.log("Login successful")
  }

  function logoutUser() {
    setUser(null);
    setPassword(null);
    setID(null);
    setIsProfessor(false);
    setIsStudent(false);
  }

  return (
    <GlobalStateContext.Provider value={{ user, setUser, password, setPassword, ID, setID, isProfessor, setIsProfessor, isStudent, setIsStudent, loginUser, logoutUser }}>
      {children}
    </GlobalStateContext.Provider>
  );
};

export const useGlobalState = () => useContext(GlobalStateContext);
