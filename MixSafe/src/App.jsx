import { useState } from 'react';
import HomePage from './pages/HomePage';
import LoadingPage from './components/LoadingPage';
import ResultPage from './pages/ResultPage';

// ========================================
// 🎯 메인 App 컴포넌트
// ========================================
export default function App() {
  const [currentPage, setCurrentPage] = useState('home');
  const [selectedProducts, setSelectedProducts] = useState([null, null]);

  const handleNavigate = (page) => {
    setCurrentPage(page);

    if (page === 'home') {
      setSelectedProducts([null, null]);
    }
  };

  return (
    <div>
      {currentPage === 'home' && (
        <HomePage 
          onNavigate={handleNavigate}
          selectedProducts={selectedProducts}
          setSelectedProducts={setSelectedProducts}
        />
      )}
      {currentPage === 'loading' && (
        <LoadingPage onNavigate={handleNavigate} />
      )}
      {currentPage === 'result' && (
        <ResultPage 
          onNavigate={handleNavigate}
          selectedProducts={selectedProducts}
        />
      )}
    </div>
  );
}