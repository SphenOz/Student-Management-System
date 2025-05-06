export default function CourseCard(coursedetails) {
    // const { courseName, professor, credits, courseDescription } = coursedetails;
    return (
        <div className="flex flex-col items-start bg-slate-600 p-4 rounded-lg w-[80%]">
            <h2 className="text-xl font-bold">Course Name</h2>
            <p className="text-gray-500">Course Description</p>
        </div>
    )
}