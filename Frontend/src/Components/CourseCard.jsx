export default function CourseCard({coursedetails}) {
    const {credits, professorID, courseName, courseID, courseCode } = coursedetails;
    return (
        <div className="flex flex-col items-start bg-slate-600 p-4 rounded-lg w-[80%]">
            <h2 className="text-xl font-bold">{courseName}</h2>
            <p className="text-gray-500">{courseID}</p>
        </div>
    )
}