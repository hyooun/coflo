// import { useProjects } from '@hooks/useProject';
// import Loading from '@pages/Loading/Loading';
// import Error from '@pages/Error/Error';
// import ProjectCardItem from '@components/Card/ProjectCardItem';
// import { ProjectCardProps } from 'types/project';
//
// const ProjectCard = ({
//   cardWidth = '200px',
//   cardHeight = 'auto',
// }: ProjectCardProps) => {
//   const { data, isLoading, error } = useProjects();
//
//   if (isLoading) return <Loading />;
//   if (error) return <Error message={error.message} />;
//
//   return (
//     <div
//       style={{
//         position: 'relative',
//         display: 'flex',
//         justifyContent: 'center',
//         alignItems: 'flex-start',
//         height: '100vh',
//         // overflowX: 'auto',
//         marginLeft: '20px',
//       }}
//     >
//       <div style={{ display: 'flex', gap: '16px' }}>
//         {data?.map((project) => (
//           <ProjectCardItem
//             key={project.id}
//             project={project}
//             cardWidth={cardWidth}
//             cardHeight={cardHeight}
//           />
//         ))}
//       </div>
//     </div>
//   );
// };
//
// export default ProjectCard;
