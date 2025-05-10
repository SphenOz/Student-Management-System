export default function CourseCard({coursedetails, grade}) {
    const {credits, professorID, courseName, courseID, courseId, courseCode, enrollmentId, profLastName, semester, studentId, year } = coursedetails;
    return (
        <div className="flex flex-col items-start bg-slate-600 p-4 rounded-lg w-[80%]">
            <div className="flex flex-row justify-between space-x-4 w-full">
                <h2 className="text-lg font-bold">{courseName}</h2> 
                <h3 className="text-lg">Taught by: {professorID}</h3>
            </div>
            <p className="text-white text-l">ClassID: {courseID ? courseID : courseId} {grade && (`-- Grade:  ${grade} `)}  {credits && (`-- Credits:  ${credits}`)}</p>
        </div>
    )
}