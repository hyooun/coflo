export default {
  content: [
    './index.html',
    './src/*.{js,ts,jsx,tsx}',
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    screens: {
      mobile: { max: '640px' },
      tablet: { min: '641px', max: '1279px' },
      desktop: { min: '1280px' },
    },
    fontFamily: {
      notoKR: 'Noto Sans KR',
      pretendard: 'Pretendard',
    },
    extend: {
      colors: {
        'main-color': '#4CCDC6',
        'main-hover-color': '#68E0D6',
        'main-color-active': '#20ACA7',
        'footer-color': '#FAFAFA',
        'text-color': '#292A34',
        gray: '#DCDCDC',
        'gray-light': '#F3F3F3',
        purple: '#8F49DE',
      },
    },
  },
  plugins: [],
};
