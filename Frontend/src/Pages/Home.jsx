import CourseCard from "../Components/CourseCard"

export default function Home() {
    return (
        <div className="flex flex-row items-center h-full w-full">
            {/*Course Listing*/}
            <div className="flex flex-col items-start h-full w-[100%] shadow-black shadow-lg p-4">
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

