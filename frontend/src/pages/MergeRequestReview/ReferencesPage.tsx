import ReferenceHeader from '@components/MergeRequest/Reference/ReferenceHeader.tsx';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Reference } from 'types/reference.ts';
import ReferencesList from '@components/MergeRequest/Reference/ReferencesList.tsx';

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
    <div className="flex flex-col p-10 w-full overflow-auto">
      <ReferenceHeader />
      <ReferencesList references={references} />
    </div>
  );
};

export default ReferencesPage;
