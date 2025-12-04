import React from 'react';
import { useState, useEffect } from 'react';
import { fetchMixResult } from "../hooks/mixApi";
import logo from "../assets/MIXSAFE.svg";

// ========================================
// 로딩 페이지
// ========================================
export default function LoadingPage({ onNavigate, selectedProducts, setMixResult }) {
  const [progress, setProgress] = useState(0);
  const [error, setError] = useState(null);

  useEffect(() => {
    let timer;
    
    const fetchData = async () => {
      try {
        // ✅ 유효성 검증
        if (!selectedProducts || selectedProducts.length < 2) {
          throw new Error("두 개의 제품을 선택해주세요");
        }

        if (!selectedProducts[0] || !selectedProducts[1]) {
          throw new Error("제품 정보가 없습니다");
        }

        // ✅ ID 유효성 검증
        if (!selectedProducts[0].id || !selectedProducts[1].id) {
          throw new Error("제품 ID가 없습니다. 검색 결과에서 제품을 선택해주세요.");
        }

        console.log("🚀 API 호출 시작:", selectedProducts);
        
        // API 호출
        const result = await fetchMixResult(selectedProducts[0], selectedProducts[1]);
        console.log("✅ API 응답:", result);
        
        setMixResult(result);
        
        // 프로그레스바 애니메이션
        timer = setInterval(() => {
          setProgress(prev => {
            if (prev >= 100) {
              clearInterval(timer);
              setTimeout(() => onNavigate('result'), 500);
              return 100;
            }
            return prev + 10;
          });
        }, 150);

      } catch (error) {
        console.error("❌ 분석 실패:", error);
        setError(error.message || "분석에 실패했습니다");
        
        setTimeout(() => {
          alert(error.message || "분석 실패");
          onNavigate("home");
        }, 2000);
      }
    };

    fetchData();

    return () => {
      if (timer) clearInterval(timer);
    };
  }, [selectedProducts, onNavigate, setMixResult]);

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
      <img src={logo} alt="logo" style={{ width: 180, marginBottom: 20 }} />

      <div style={{
        fontSize: '16px',
        marginBottom: '30px',
        textAlign: 'center',
        padding: '0 30px'
      }}>
        {error ? (
          <div style={{ color: '#ffeb3b', fontWeight: 'bold' }}>
            ⚠️ {error}
          </div>
        ) : (
          '입력된 제품의 성분을 분석 중 ...'
        )}
      </div>

      {!error && (
        <>
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
        </>
      )}

      <style>{`
        @keyframes bounce {
          0%, 80%, 100% { transform: scale(0); }
          40% { transform: scale(1); }
        }
      `}</style>
    </div>
  );
}