import axios from "axios"
import  {use, useState, useEffect } from "react"
import { Link, useNavigate } from "react-router-dom"
import { useGlobalState } from "../GlobalState"

export default function Login() {
    const [ID, setID] = useState("")
    const [Password, setPassword] = useState("")
    const [failedID, setFailedID] = useState(false)
    const navigate = useNavigate()
    const [failedPassword, setFailedPassword] = useState(false)
    const {loginUser} = useGlobalState()
    const [plogin, setPLogin] = useState(false)
    const [executeLogin, setExecuteLogin] = useState()

    const handleSubmit = async (e) => {
        
        e.preventDefault()
        await login(e)
        // navigate("/home")
    }


    const login = async (e) => {
        if(!plogin){
            try {
                const response = await axios.get("http://localhost:8080/students", {
                    params: {id:ID}
                })

                setExecuteLogin(response.data)
            }
            catch (error) {
                console.error("Error logging in:", error)
                if (error.response.status === 401) {
                    setFailedID(true)
                }
                else if (error.response.status === 403) {
                    setFailedPassword(true)
                }
            }
        }
        else {
            try {
                const response = await axios.get("http://localhost:8080/professors", {
                    params: {id:ID}
                })
                setExecuteLogin(response.data)
            }
            catch (error) {
                console.error("Error logging in:", error)
                if (error.response.status === 401) {
                    setFailedID(true)
                }
                else if (error.response.status === 403) {
                    setFailedPassword(true)
                }
            }
        }
    }
    useEffect(() => {
        if (executeLogin) {
            if (plogin) {
                loginUser(executeLogin)
                navigate("/home")
            }
            else {
                loginUser(executeLogin)
                navigate("/home")
            }
        }
    }, [executeLogin])

    const inputStyle = "background-gray-200 border-2 border-gray-300 rounded-lg p-2 h-[3rem] focus:outline-none focus:border-amber-200 "
    return(
        <>
            <div className="flex flex-col items-center justify-center h-full w-full">
                <div className ='border-amber-200 border-2 items-start rounded-lg flex flex-col min-h-[50vh] max-h-[50%] m-5 px-5 min-w-[400px] space-y-4'>    
                    <h1 className="m-5">Login {plogin ? " Professor" : " Student" }</h1>
                        <form  className= "flex flex-col w-full mt-5 space-y-7" onSubmit={handleSubmit}>
                            <input className={inputStyle}
                                type="text" placeholder="ID"value={ID} onFocus={(e) => setFailedID(false)} onChange={(e) => setID(e.target.value)} />
                            {failedID && <span className="text-red-500">Invalid ID</span>}
                            <input className={inputStyle}
                                type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)}/>
                            <button className="bg-amber-200 hover:bg-amber-400 mt-10 border-2 rounded-full text-black h-[3rem]" type="submit">Login</button>
                        </form>
                        <div className="flex flex-row self-center items-center justify-center mt-5 space-x-2">
                            <span className="text-blue-500 hover:underline cursor-pointer" onClick={() => setPLogin(!plogin)}>{!plogin ? "Professor Login" : "Student Login"}</span>
                        </div>
                </div>
            </div>
        </>
    )
}


