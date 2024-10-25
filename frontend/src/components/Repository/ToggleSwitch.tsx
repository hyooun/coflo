const ToggleSwitch = ({ checked, onChange }: ToggleSwitchProps) => {
  return (
    <label className="relative inline-block w-14 h-8">
      <input
        type="checkbox"
        checked={checked}
        onChange={onChange}
        className="absolute opacity-0 w-0 h-0"
      />
      <span
        className={`block absolute inset-0 transition-all duration-300 ease-in-out rounded-full ${checked ? 'bg-primary-500' : 'bg-[#D9D9D9]'}`}
      >
        <span
          className={`block w-6 h-6 bg-white rounded-full shadow transform transition-transform duration-300 ease-in-out ${checked ? 'translate-x-6' : 'translate-x-0'}`}
        ></span>
      </span>
    </label>
  );
};

export default ToggleSwitch;
