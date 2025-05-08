// Layout.js
import Shoulderbar from './Components/Shoulderbar';

export default function Layout({ children }) {
    return (
        <div className="flex w-full min-h-screen overflow-hidden">
            <Shoulderbar />
            <div className='flex-1'>
                {children}
            </div>
        </div>
    );
}