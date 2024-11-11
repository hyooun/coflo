import { useAxiosInterceptor } from './config/error';

const App = () => {
  useAxiosInterceptor();
  return <div>aaa</div>;
};

export default App;
