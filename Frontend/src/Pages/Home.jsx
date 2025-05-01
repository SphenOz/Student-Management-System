export default function Home() {

    const buttonStyle = "bg-gray-200 hover:bg-gray-300 border-2 rounded-full hover: text-black h-[3rem] w-[80%]"
    return (
        <div className="flex flex-row items-center h-screen w-screen">
            {/* Left Sidebar */}
            <div className="flex flex-col items-start h-screen w-[20%] bg-slate-700 shadow-black shadow-lg p-4">
                <div className="flex flex-col items-start h-[30%] w-full space-y-4">
                    <h1 className="text-2xl font-bold">Home</h1>
                    <img src="src\assets\SJSU_Seal.svg.png" alt="SJSU Seal" className="h-50 w-50 mt-5 mb-5" />
                </div>
                <div className="flex flex-col items-start w-[100%] justify-start mt-5 space-y-4">
                    <button className={buttonStyle}>Course Enrollment</button>
                    <button className={buttonStyle}>Profile</button>
                    <button className={buttonStyle}>Settings</button>
                    <button className={buttonStyle}>Logout</button>
                </div>
            </div>
            {/*Course Listing*/}
            <div className="flex flex-col items-start h-screen w-[80%] shadow-black shadow-lg p-4">
                <h1 className="text-2xl font-bold">Course Listing</h1>
                <div className="flex flex-col items-start mt-5 bg-slate-600 p-4 rounded-lg">
                    <h2 className="text-2xl font-bold">Term: </h2>
                </div>
                <div className="flex flex-col items-center w-full mt-5 space-y-4">
                    <CourseCard />
                    <CourseCard />
                    <CourseCard />
                    <CourseCard />
                </div>
            </div>
        </div>
    )
}

function CourseCard() {
    return (
        <div className="flex flex-col items-start bg-slate-600 p-4 rounded-lg w-[80%]">
            <h2 className="text-xl font-bold">Course Name</h2>
            <p className="text-gray-500">Course Description</p>
        </div>
    )
}