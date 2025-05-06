import { useState, useEffect } from "react"
import { useNavigate, useLocation } from "react-router-dom"

export default function Shoulderbar() {
    const location = useLocation();
    const [active, setActive] = useState(true)
    const [collapsed, setCollapsed] = useState(false)
    useEffect(() => {
        if (location.pathname === "/") {
            setActive(false)
        }
        else {
            setActive(true)
        }
    }
    , [location.pathname])

    const navigate = useNavigate()
    const buttonStyle = "bg-gray-200 hover:bg-gray-300 border-2 rounded-full hover: text-black h-[3rem] w-[80%] cursor-pointer"
    return (
        <>
            {/* Left Sidebar */}
            {active && (
                collapsed ? 
                    (
                        <div className={`flex flex-col items-start ${collapsed ? 'w-[2%]' : 'w-[20%]'} bg-slate-700 shadow-black shadow-lg p-4 transition-all duration-300 ease-in-out`}>
                            <span className="cursor-pointer" onClick={() => setCollapsed(!collapsed)}> {'>'} </span>
                        </div>
                    )
                    :
                    (
                        <div className="flex flex-col items-start relative h-screen w-[20%] bg-slate-700 shadow-black shadow-lg p-4">
                            <div className="flex flex-col items-start h-[30%] w-full space-y-4">
                                <h2 className="text-xl font-bold">Student Name</h2>
                                <span className="self-end text-gray-500 cursor-pointer" onClick={() => setCollapsed(!collapsed)}> Collapse </span>
                                <img src="src\assets\SJSU_Seal.svg.png" alt="SJSU Seal" className="h-50 w-50 mt-5 mb-5" />
                            </div>
                            {!collapsed && (
                                <div className="flex flex-col items-start w-[100%] justify-start mt-5 space-y-4">
                                    <button onClick={() => navigate('/home')} className={buttonStyle}>Home</button>
                                    <button onClick={() => navigate('/course')} className={buttonStyle}>Course Enrollment</button>
                                    <button className={buttonStyle}>Settings</button>
                                    <button className={buttonStyle}>Logout</button>
                                </div>
                            )}
                        </div>
                    )
            )}
        </>
    )
}