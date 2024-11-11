import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const useAxiosInterceptor = () => {
  const navigate = useNavigate();

  useEffect(() => {}, [navigate]);
};
