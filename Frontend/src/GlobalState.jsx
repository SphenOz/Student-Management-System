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
    console.log("Login successful")
  }

  return (
    <GlobalStateContext.Provider value={{ user, setUser, password, setPassword, ID, setID, isProfessor, setIsProfessor, isStudent, setIsStudent, loginUser }}>
      {children}
    </GlobalStateContext.Provider>
  );
};

export const useGlobalState = () => useContext(GlobalStateContext);
