import App from '../App';
import { render, screen } from '@testing-library/react';

test('페이지가 제대로 뜨나요?', async () => {
  render(<App />);
  const check = await screen.findByRole('check');
  expect(check).toHaveTextContent('check');
});
