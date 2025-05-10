import { useState } from 'react'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './App.css'
import Login from './Pages/Login'
import Home from './Pages/Home'
import Layout from './Layout'
import CourseSelection from './Pages/CourseSelection'
import Manage from './Pages/Manage'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <BrowserRouter>
        <Layout>
            <Routes>
              <Route path="/" element={<Login />} />
              <Route path="/home" element={<Home />} />
              <Route path="/course" element={<CourseSelection/>}/>
              <Route path='/manage/:courseid' element={<Manage/>}/>
            </Routes>
          </Layout>
      </BrowserRouter>
    </>
  )
}

export default App
