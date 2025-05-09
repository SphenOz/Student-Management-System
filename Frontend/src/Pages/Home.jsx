import { use } from "react"
import CourseCard from "../Components/CourseCard"
import { useEffect, useState } from "react"
import { useGlobalState } from "../GlobalState"
import axios from "axios"
import { useNavigate } from "react-router-dom"

export default function Home() {

    const navigate = useNavigate()
    const {user, isProfessor, isStudent} = useGlobalState()
    const [enrollmentDetails, setEnrollmentDetails] = useState([])

    const fetchEnrollmentDetails = async () => {
        console.log("Fetching courses for user ID:", user.id);
        if(isStudent) {
            try {
                const response = await axios.get('http://localhost:8080/student-courses', {
                    params: { studentID: user.id }
                });
                setEnrollmentDetails(response.data);
            } catch (error) {
                console.error('Error fetching enrollment details:', error);
            }
        }
        if(isProfessor) {
            try {
                const response = await axios.get(`http://localhost:8080/courses/prof/${user.id}`, {
                    params: { professorID: user.id }
                });
                setEnrollmentDetails(response.data);
            }
            catch (error) {
                console.error('Error fetching courses for professor:', error);
            }
            console.log("Fetching courses for professor ID:", user.id);
        }
    }

    useEffect(() => {
        fetchEnrollmentDetails()
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
    const manageCourse = (e, courseid) => {
        e.preventDefault()
        console.log("Managing course with ID:", courseid);
        navigate(`/manage/${courseid}`)

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
                        {isStudent ? 
                        (enrollmentDetails ? enrollmentDetails.map((course) => (
                            <div key={course.enrollmentInfo.courseId} className="flex flex-row place-items-center w-full">
                                {console.log(course.grade)}
                                <CourseCard coursedetails={course.enrollmentInfo} grade={course.grade}/> 
                                <button className={`${isStudent ? "bg-red-200 hover:bg-red-400" : "bg-blue-400 hover:bg-blue-500"} transform transition-transform hover:scale-105 duration-300 border-2 rounded-full cursor-pointer text-black text-3xl h-[90%] m-4 w-[18%]`}
                        onClick={isStudent ? (e) => handleDrop(e,course.enrollmentInfo.courseId) : (e) => manageCourse(e,course.enrollmentInfo.courseId)}>{isStudent? "Drop Course" : "Manage"} </button>
                            </div> ))
                            : <p>No courses available</p>
                        ) : 
                        (enrollmentDetails ? enrollmentDetails.map((course) => (
                            <div className="flex flex-row place-items-center w-full">
                                <CourseCard key={course.courseID} coursedetails={course}/>
                                <button className={`${isStudent ? "bg-red-200 hover:bg-red-400" : "bg-blue-400 hover:bg-blue-500"} transform transition-transform hover:scale-105 duration-300 border-2 rounded-full cursor-pointer text-black text-3xl h-[90%] m-4 w-[18%]`}
                        onClick={isStudent ? (e) => handleDrop(e,course.courseID) : (e) => manageCourse(e,course.courseID)}>{isStudent? "Drop Course" : "Manage"} </button>
                            </div> ))
                            : <p>No courses available</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    )
}

