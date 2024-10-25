import Header from '@components/Header/Header';
import badge1blur from '@assets/badge1blur.jpg';
import badge1 from '@assets/badge1.jpg';

const BadgePage = () => {
  const images = [
    badge1blur,
    badge1,
    badge1blur,
    badge1blur,
    badge1blur,
    badge1blur,
    badge1blur,
    badge1blur,
  ];

  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6">
      <Header title="My Badge" />
      <div className="grid grid-cols-4 grid-rows-2 px-2 ">
        {images.map((src, index) => (
          <div key={index} className="flex justify-center items-center">
            <img src={src} alt={`Badge ${index + 1}`} className="w-60 h-60 object-cover" />
          </div>
        ))}
      </div>
    </div>
  );
};

export default BadgePage;
