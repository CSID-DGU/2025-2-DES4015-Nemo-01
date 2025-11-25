import { useState, useEffect } from 'react';

// ========================================
// ⏳ 로딩 페이지
// ========================================
export default function LoadingPage({ onNavigate }) {
  const [progress, setProgress] = useState(0);

  useEffect(() => {
    const timer = setInterval(() => {
      setProgress(prev => {
        if (prev >= 100) {
          clearInterval(timer);
          setTimeout(() => onNavigate('result'), 500);
          return 100;
        }
        return prev + 10;
      });
    }, 200);

    return () => clearInterval(timer);
  }, [onNavigate]);

  return (
    <div style={{
      width: '403px',
      height: '100vh',
      margin: '0 auto',
      background: '#0f9aff',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      alignItems: 'center',
      color: 'white'
    }}>
      <h1 style={{
        fontFamily: '"Oi", cursive',
        fontSize: '32px',
        marginBottom: '40px'
      }}>
        MIX SAFE
      </h1>

      <div style={{
        fontSize: '16px',
        marginBottom: '30px',
        textAlign: 'center',
        padding: '0 30px'
      }}>
        입력된 제품의 성분을 분석 중 ...
      </div>

      <div style={{
        display: 'flex',
        gap: '10px',
        marginBottom: '20px'
      }}>
        <div style={{
          width: '15px',
          height: '15px',
          background: 'white',
          borderRadius: '50%',
          animation: 'bounce 1.4s infinite ease-in-out both',
          animationDelay: '0s'
        }}></div>
        <div style={{
          width: '15px',
          height: '15px',
          background: 'white',
          borderRadius: '50%',
          animation: 'bounce 1.4s infinite ease-in-out both',
          animationDelay: '0.2s'
        }}></div>
        <div style={{
          width: '15px',
          height: '15px',
          background: 'white',
          borderRadius: '50%',
          animation: 'bounce 1.4s infinite ease-in-out both',
          animationDelay: '0.4s'
        }}></div>
      </div>

      <div style={{
        width: '200px',
        height: '4px',
        background: 'rgba(255,255,255,0.3)',
        borderRadius: '2px',
        overflow: 'hidden'
      }}>
        <div style={{
          width: `${progress}%`,
          height: '100%',
          background: 'white',
          transition: 'width 0.2s ease'
        }}></div>
      </div>

      <style>{`
        @keyframes bounce {
          0%, 80%, 100% { transform: scale(0); }
          40% { transform: scale(1); }
        }
      `}</style>
    </div>
  );
}