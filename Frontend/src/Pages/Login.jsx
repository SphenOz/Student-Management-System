import  {useState } from "react"
import { Link, useNavigate } from "react-router-dom"

export default function Login() {
    const [ID, setID] = useState("")
    const [Password, setPassword] = useState("")
    const [failedID, setFailedID] = useState(false)
    const navigate = useNavigate()
    const [failedPassword, setFailedPassword] = useState(false)

    const handleSubmit = (e) => {
        
        e.preventDefault()
        if(!/^[0-9]+$/.test(ID)) {
            setFailedID(true)
            return
        }
        
        navigate("/home")
        console.log("ID: ", ID)
    }

    const inputStyle = "background-gray-200 border-2 border-gray-300 rounded-lg p-2 h-[3rem] focus:outline-none focus:border-amber-200 "
    return(
        <>
            <div className="flex flex-col items-center justify-center h-screen w-screen">
                <div className ='border-amber-200 border-2 items-start rounded-lg flex flex-col min-h-[50vh] max-h-[50%] m-5 px-5 min-w-[400px] space-y-4'>    
                    <h1 className="m-5">Login</h1>
                        <form  className= "flex flex-col w-full mt-5 space-y-7" onSubmit={handleSubmit}>
                            <input className={inputStyle}
                                type="text" placeholder="ID" onFocus={(e) => setFailedID(false)} onChange={(e) => setID(e.target.value)} />
                            {failedID && <span className="text-red-500">Invalid ID</span>}
                            <input className={inputStyle}
                                type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)}/>
                            <button className="bg-amber-200 hover:bg-amber-400 mt-10 border-2 rounded-full text-black h-[3rem]" type="submit">Login</button>
                        </form>
                </div>
            </div>
        </>
    )
}


