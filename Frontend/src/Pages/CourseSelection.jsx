import { useState,useEffect, use } from "react";
import CourseCard from "../Components/CourseCard";
import axios from "axios";
import { useGlobalState } from "../GlobalState";
import { useNavigate } from "react-router-dom";

export default function CourseSelection() {

    const [courseDetails, setCourseDetails] = useState([]);
    const {user, isStudent, isProfessor} = useGlobalState()
    const [courseID, setCourseID] = useState(null)
    const [courseCode, setCourseCode] = useState(null)
    const [courseName, setCourseName] = useState(null)
    const [credits, setCredits] = useState(null)

    const navigate = useNavigate()

    
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

    const handleAddition = async (e,courseID) => {
        e.preventDefault()
        console.log(courseID)
        console.log(user.id)
        try {
            const response = await axios.post('http://localhost:8080/enroll', {
                studentId: user.id, // Replace with the actual student ID
                courseId: courseID // Replace with the actual course ID
            })
            console.log(response.data);
        } catch (error) {
            console.error('Error enrolling in course:', error);
        }

    }

    const createCourse = async (e) => {
        e.preventDefault()
        console.log(courseName, courseCode, credits)
        try {
            const response = await axios.post('http://localhost:8080/add-course', {
                courseName: courseName,
                courseCode: courseCode,
                professorID: user.id,
                credits: parseInt(credits),
                
            })
            console.log(response.data);
        } catch (error) {
            console.error('Error creating course:', error);
        }
        navigate("/home")
    }

    useEffect(() => {
        console.log(courseDetails);
    }
    , [courseDetails])
    return (
        <div className="flex flex-row items-center h-full w-full">
            {/*Course Listing*/}
            {isStudent ? (
            <div className="flex flex-col items-start h-full w-[100%] shadow-black shadow-lg p-4">
                <h1 className="text-2xl font-bold">Course Selection</h1>
                <div className="flex flex-col items-start mt-5 bg-slate-600 p-4 rounded-lg">
                    <h2 className="text-2xl font-bold">Term: </h2>
                </div>
                <div className="flex flex-col items-start w-[90%] mt-5 space-y-4 bg-slate-500 p-4 rounded-lg overflow-y-auto max-h-[80vh]">
                {courseDetails ? courseDetails.map((course) => (
                    <div className="flex flex-row place-items-center w-full">
                        <CourseCard key={course.courseID} coursedetails={course}/>
                        <button className="bg-green-200 hover:bg-green-400 transform transition-transform hover:scale-105 duration-300 border-2 rounded-full cursor-pointer text-black text-3xl h-[90%] m-4 w-[18%]"
                        onClick={(e) => handleAddition(e,course.courseID)}>Add Course</button>
                    </div> ))
                    : <p>No courses available</p>
                }
                </div>
            </div>
            )    :  (
            <div className="flex flex-col items-start h-full w-[100%] shadow-black shadow-lg p-4">    
                <h1 className="text-2xl font-bold">Course Creation</h1>
                <div className="flex flex-col items-start mt-5 bg-slate-600 p-4 rounded-lg">
                    <h2 className="text-2xl font-bold">Term: </h2>
                </div>
                <div className="flex flex-col items-start w-[90%] mt-5 space-y-4 bg-slate-500 p-4 rounded-lg overflow-y-auto max-h-[80vh]">
                    <form className="flex flex-col items-center w-full mt-5 space-y-4 bg-slate-500 p-4 rounded-lg overflow-y-auto max-h-[80vh]">
                        <input onChange={(e) => setCourseName(e.target.value)}className="bg-slate-700 border-2 border-slate-300 rounded-lg p-2 h-[3rem] focus:outline-none focus:border-amber-400 w-[50%]" type="text" placeholder="Course Name" />
                        <input onChange={(e) => setCourseCode(e.target.value)}className="bg-slate-700 border-2 border-slate-300 rounded-lg p-2 h-[3rem] focus:outline-none focus:border-amber-400 w-[50%]" type="text" placeholder="Course Code" />
                        <input onChange={(e) => setCredits(parseInt(e.target.value))}className="bg-slate-700 border-2 border-slate-300 rounded-lg p-2 h-[3rem] focus:outline-none focus:border-amber-400 w-[50%]" type="text" placeholder="Credits" />
                        <button onClick={(e) => createCourse(e)} className="bg-green-200 hover:bg-green-400 mt-10 border-2 rounded-full text-black h-[3rem] w-[20%]" type="submit">Create Course</button>
                    </form>
                </div>
            </div>
            )}
        </div>
    )
}
