import React from 'react'
import { useParams } from 'react-router-dom'
import { useGlobalState } from "../GlobalState"
import { useEffect, useState } from "react"
import axios from "axios"
export default function Manage() {
    const { isProfessor, user } = useGlobalState()
    const {courseid} = useParams()
    const [openPopup, setOpenPopup] = useState(false)
    const [courseDetails, setCourseDetails] = useState([])
    const [enrollmentDetails, setEnrollmentDetails] = useState([])
    const [grade, setGrade] = useState("")
    const [editState, setEditState] = useState({})

    const fetchCourseDetails = async () => {
        console.log("courseID: ", courseid)

        if(isProfessor) {
            try {
                const response = await axios.get(`http://localhost:8080/professor-courses`, {
                    params: { courseID: courseid }
                });
                setEnrollmentDetails(response.data);
                const reponsetwo = await axios.get(`http://localhost:8080/course`, {
                    params: { courseID: courseid }
                });
                setCourseDetails(reponsetwo.data);
            } catch (error) {
                console.error('Error fetching course details:', error);
            }
        }
    }
    useEffect(() => {
        fetchCourseDetails()
    }, [courseid])
    useEffect(() => {
        console.log("Course Details: ", courseDetails)
    }, [courseDetails])
    useEffect(() => {
        console.log("Enrollment Details: ", enrollmentDetails)
    }, [enrollmentDetails])

    const handleEditGrade = (e, studentID, courseID, professorID) => {
        e.preventDefault()
        setEditState({studentID, courseID, professorID})
        setOpenPopup(true)
    }

    const handleDrop = async (e, studentID) => {
        e.preventDefault()
        const response = await axios.delete('http://localhost:8080/professor', {
            params: {
                professorID: user?.id,
                studentID: studentID,
                courseID: courseDetails?.courseID
            }
        });
        fetchCourseDetails()
    }
    

    const editGrade = async (e) => {
        e.preventDefault()
        console.log("Editing grade with:", {
            studentID: editState.studentID,
            courseID: editState.courseID,
            professorID: editState.professorID,
            grade: grade
        });
        const response = await axios.put(`http://localhost:8080/professor/grade`, null, {
            params: {
                studentID: editState.studentID,
                courseID: editState.courseID,
                professorID: editState.professorID,
                grade: grade
            }
        })
        fetchCourseDetails()
        console.log("Grade updated: ")
        setOpenPopup(false)
    }

    const Popup = () => {
        return (
            <div className="fixed inset-0 flex items-center justify-center z-50">
                <div className="bg-slate-700 p-6 rounded-lg shadow-lg">
                    <h2 className="text-xl font-bold mb-4">Select Grade: {grade ? grade : ""}</h2>
                    <div className='flex flex-row items-center justify-between w-full space-x-4'>
                        <button className='bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded' onClick={() => setGrade("A")}>A</button>
                        <button className='bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded' onClick={() => setGrade("B")}>B</button>
                        <button className='bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded' onClick={() => setGrade("C")}>C</button>
                        <button className='bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded' onClick={() => setGrade("D")}>D</button>
                        <button className='bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded' onClick={() => setGrade("F")}>F</button>
                    </div>
                    <div className='flex flex-row items-center justify-center w-full space-x-3 mt-4'>
                        <button className='bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded' onClick={() => setOpenPopup(false)}>Cancel</button>
                        <button className='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded cursor-pointer' onClick={(e) => editGrade(e)}>Submit</button>
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div className="flex flex-row items-center h-full w-full">
                    {/*Course Listing*/}
                    <div className="flex flex-col items-start h-full w-[100%] shadow-black shadow-lg p-4">
                        <h1 className="text-2xl font-bold">Course Management</h1>
                        <div className="flex flex-col items-start mt-5 bg-slate-600 p-4 rounded-lg">
                            <h2 className="text-2xl font-bold">Term: </h2>
                        </div>
                        <div className="flex flex-col items-center w-full mt-5 space-y-4">
                            <div className="flex flex-col items-start w-[90%] mt-5 space-y-4 bg-slate-500 p-4 rounded-lg overflow-y-auto max-h-[80vh]">
                                <div className="flex flex-col items-start bg-slate-600 p-4 rounded-lg w-[80%]">
                                    <h2 className="text-xl font-bold">{courseDetails ? courseDetails?.courseName : "not found"}</h2>
                                    
                                </div>
                                <div className=" w-[80%]">{
                                        enrollmentDetails.map((student) => (
                                            <div key={student.id} className='flex flex-row items-center w-full space-x-4 space-y-4'>
                                                <span  className="text-lg w-[15%]">{student.student.firstName} {student.student.lastName}</span>
                                                <button className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" onClick={(e) => handleDrop(e, student.student.id)}>Drop</button>
                                                <button className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded" onClick={(e) => handleEditGrade(e, student.student.id, courseDetails.courseID, user.id)}>Edit Grade</button>
                                                <span className="text-lg mb-4">Grade: {student.grade}</span>
                                            </div>
                                        ))
                                    }
                                </div>
                            </div>
                        </div>
                        {openPopup && <Popup />}
                    </div>
                </div>
    )
    
}


