import LandingPage from '@pages/Landing/LandingPage';
import { useAxiosInterceptor } from './config/error';

const App = () => {
  useAxiosInterceptor();
  return (
    <div>
      <LandingPage />
    </div>
  );
};

export default App;
