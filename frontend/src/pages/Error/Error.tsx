interface ErrorProps {
  message: string;
}

const Error = ({ message }: ErrorProps) => {
  return (
    <div style={{ textAlign: 'center', padding: '20px', color: 'red' }}>
      <h2>Error occurred</h2>
      <p>{message}</p>
    </div>
  );
};

export default Error;
