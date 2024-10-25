import logo from 'assets/logo.png';

export default function Navbar() {
  return (
    <div className="w-16 bg-gradient-to-b from-[#1A2036] to-seconday h-screen shadow-2xl text-[#c1d6f2] overflow-hidden">
      <div className="w-full flex justify-center items-center mt-5">
        <img src={logo} alt="Logo" className="w-7 h-7" />
      </div>
    </div>
  );
}
