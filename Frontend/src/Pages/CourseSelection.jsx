import { useState,useEffect, use } from "react";
import CourseCard from "../Components/CourseCard";
import axios from "axios";

export default function CourseSelection() {

    const [courseDetails, setCourseDetails] = useState([]);

    
    const fetchCourseDetails = async () => {
        try {
            const response = await axios.get('http://localhost:8080/courses');
            setCourseDetails(response.data);
        } catch (error) {
            console.error('Error fetching course details:', error);
        }
    }
    useEffect(() => {
        fetchCourseDetails();
    },[])

    useEffect(() => {
        console.log(courseDetails);
    }
    , [courseDetails])
    return (
        <div className="flex flex-row items-center h-full w-full">
            {/*Course Listing*/}
            <div className="flex flex-col items-start h-full w-[100%] shadow-black shadow-lg p-4">
                <h1 className="text-2xl font-bold">Course Selection</h1>
                <div className="flex flex-col items-start mt-5 bg-slate-600 p-4 rounded-lg">
                    <h2 className="text-2xl font-bold">Term: </h2>
                </div>
                <div className="flex flex-col items-start w-[90%] mt-5 space-y-4 bg-slate-500 p-4 rounded-lg overflow-y-auto max-h-[80vh]">
                {courseDetails ? courseDetails.map((course) => (
                    <div className="flex flex-row place-items-center w-full">
                        <CourseCard key={course.courseId} coursedetails={course}/>
                        <button className="bg-green-200 hover:bg-green-400 transform transition-transform hover:scale-105 duration-300 border-2 rounded-full cursor-pointer text-black text-3xl h-[90%] m-4 w-[18%]">Add Course</button>
                    </div> ))
                    : <p>No courses available</p>
                }
                </div>
            </div>
        </div>
    )
}
