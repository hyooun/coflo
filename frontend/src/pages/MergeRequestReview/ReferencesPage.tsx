import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Reference } from 'types/reference.ts';
import ReferenceHeader from '@components/MergeRequest/Reference/ReferenceHeader.tsx';
import ReferencesList from '@components/MergeRequest/Reference/ReferencesList.tsx';
import ScrollNavigationButton from '@components/Button/ScrollNavigationButton';

const ReferencesPage = () => {
  const { id } = useParams();
  const [references, setReferences] = useState<Reference[]>([]);
  useEffect(() => {
    if (!id) return;

    const fetchReferences = async () => {
      const response = await fetch(`/api/reviews/${id}/retrivals`);
      const data = await response.json();
      setReferences(data);
    };

    fetchReferences();
  }, [id]);

  if (!references) return <div>Loading...</div>;

  return (
    <div className="flex flex-col p-10 w-full overflow-auto scroll-container">
      <ReferenceHeader />
      <ReferencesList references={references} />
      <ScrollNavigationButton />
    </div>
  );
};

export default ReferencesPage;
