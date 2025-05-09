import { use } from "react"
import CourseCard from "../Components/CourseCard"
import { useEffect, useState } from "react"
import { useGlobalState } from "../GlobalState"
import axios from "axios"

export default function Home() {

    const {user} = useGlobalState()
    const [enrollmentDetails, setEnrollmentDetails] = useState([])

    const fetchEnrollmentDetails = async () => {
        console.log("Fetching courses for user ID:", user.id);
        try {
            const response = await axios.get('http://localhost:8080/student-courses', {
                params: { studentID: user.id }
            });
            setEnrollmentDetails(response.data);
        } catch (error) {
            console.error('Error fetching enrollment details:', error);
        }
    }

    useEffect(() => {
        fetchEnrollmentDetails()
        console.log("Enrollment Details: ", enrollmentDetails)
    }
    , [])

    useEffect(() => {
        console.log("Enrollment Details: ", enrollmentDetails)
    }, [enrollmentDetails])

    const handleDrop = async (e, cID) => {
        e.preventDefault()
        console.log("Dropping course with:", {
            studentID: user?.id,
            courseID: cID
        });
        try {
            const response = await axios.delete('http://localhost:8080/drop', {
                params: { 
                    studentID: user.id, 
                    courseID: cID }
            });
        } catch (error) {
            console.error('Error dropping course:', error);
        }
        fetchEnrollmentDetails()
    }


    return (
        <div className="flex flex-row items-center h-full w-full">
            {/*Course Listing*/}
            <div className="flex flex-col items-start h-full w-[100%] shadow-black shadow-lg p-4">
                <h1 className="text-2xl font-bold">Course Listing</h1>
                <div className="flex flex-col items-start mt-5 bg-slate-600 p-4 rounded-lg">
                    <h2 className="text-2xl font-bold">Term: </h2>
                </div>
                <div className="flex flex-col items-center w-full mt-5 space-y-4">
                    <div className="flex flex-col items-start w-[90%] mt-5 space-y-4 bg-slate-500 p-4 rounded-lg overflow-y-auto max-h-[80vh]">
                        {enrollmentDetails ? enrollmentDetails.map((course) => (
                            <div className="flex flex-row place-items-center w-full">
                                <CourseCard key={course.courseId} coursedetails={course}/>
                                <button className="bg-red-200 hover:bg-red-400 transform transition-transform hover:scale-105 duration-300 border-2 rounded-full cursor-pointer text-black text-3xl h-[90%] m-4 w-[18%]"
                        onClick={(e) => handleDrop(e,course.courseId)}>Drop Course</button>
                            </div> ))
                            : <p>No courses available</p>
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}

