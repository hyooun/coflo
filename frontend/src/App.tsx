// import ProjectCard from '@components/Project/ProjectCard';
// import { Plus } from '@components/Plus/Plus';
import Navbar from '@components/Nav/Navbar/Navbar';
import { Sidebar } from '@components/Sidebar/Sidebar';
import { Outlet } from 'react-router-dom';

const App = () => {
  return (
    // <div className="w-full bg-gray-100">
    //   <div className="w-full mx-auto p-4">
    //     <div className="flex justify-between items-center mb-6" style={{ marginLeft: '16px' }}>
    //       <h1 className="text-2xl font-bold mr-4">내 연동된 프로젝트 목록</h1>
    //       <Plus />
    //     </div>

    //     <ProjectCard />
    //   </div>
    // </div>
    <div className="flex flex-row h-full w-full">
      <Navbar></Navbar>
      <Sidebar></Sidebar>

      <Outlet />
    </div>
  );
};

export default App;
