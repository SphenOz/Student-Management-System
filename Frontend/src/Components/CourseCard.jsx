export default function CourseCard({coursedetails, grade}) {
    const {credits, professorID, courseName, courseId, courseCode, enrollmentId, profLastName, semester, studentId, year } = coursedetails;
    return (
        <div className="flex flex-col items-start bg-slate-600 p-4 rounded-lg w-[80%]">
            <h2 className="text-xl font-bold">{courseName}</h2>
            <p className="text-white text-l">ClassID: {courseId} -- Grade: {grade}</p>
        </div>
    )
}