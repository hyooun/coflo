const Loading = () => {
  const spinnerStyle = {
    margin: 'auto',
    border: '4px solid #ccc',
    borderTop: '4px solid #007bff',
    borderRadius: '50%',
    width: '40px',
    height: '40px',
    animation: 'spin 1s linear infinite' as 'spin 1s linear infinite',
  };

  return (
    <div style={{ textAlign: 'center', padding: '20px' }}>
      <p>Loading...</p>
      <div className="spinner" style={spinnerStyle}></div>
      <style>
        {`
            @keyframes spin {
              0% { transform: rotate(0deg); }
              100% { transform: rotate(360deg); }
            }
          `}
      </style>
    </div>
  );
};

export default Loading;
