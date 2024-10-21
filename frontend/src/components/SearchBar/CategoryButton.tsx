const CategoryButton = ({
  category,
  onClick,
  className,
}: CategoryButtonProps) => {
  return (
    <button
      onClick={() => onClick(category)}
      className={`rounded-full bg-blue-500 text-white py-2 px-4 m-2 hover:bg-blue-600 transition ${className}`}
    >
      {category}
    </button>
  );
};

export default CategoryButton;
