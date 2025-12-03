import React from 'react';
// ========================================
// 결과 페이지
// ========================================
export default function ResultPage({ onNavigate, selectedProducts, mixResult }) {

  const status = mixResult?.status || "UNKNOWN";
  const aiResult = mixResult?.aiResult || '분석 결과를 불러오는데 실패했습니다.';
  const level = mixResult?.airResult?.startsWith("위험")
  ? "danger"
  : mixResult?.airResult?.startsWith("주의")
  ? "warning"
  : "safe";

  const levelColor = {
  danger: "#ff4d4d",
  warning: "#ffb300",
  safe: "#4caf50"
}[level];

  return (
    <div style={{
      width: '403px',
      minHeight: '100vh',
      margin: '0 auto',
      background: 'linear-gradient(180deg, #0f9aff 0%, #0880d6 100%)',
      padding: '60px 20px',
      color: 'white'
    }}>
      <div 
        onClick={() => onNavigate('home')}
        style={{
          position: 'absolute',
          top: '20px',
          left: '20px',
          cursor: 'pointer',
          fontSize: '24px'
        }}
      >
        ←
      </div>

      <div style={{
        textAlign: 'center',
        marginTop: '40px'
      }}>
        <h1 style={{
          fontFamily: '"Oi", cursive',
          fontSize: '28px',
          marginBottom: '20px'
        }}>
          MIX SAFE
        </h1>

        <div style={{
          fontSize: '24px',
          fontWeight: '700',
          marginBottom: '30px'
        }}>
          ⚠️ 혼합 결과
        </div>

        <div style={{
          background: 'rgba(255,255,255,0.2)',
          padding: '20px',
          borderRadius: '15px',
          marginBottom: '20px'
        }}>
          <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            gap: '15px',
            marginBottom: '10px'
          }}>
            <span style={{ fontSize: '16px', fontWeight: '600' }}>
              {selectedProducts[0]?.name || '제품 1'}
            </span>
            <span style={{ fontSize: '20px' }}>+</span>
            <span style={{ fontSize: '16px', fontWeight: '600' }}>
              {selectedProducts[1]?.name || '제품 2'}
            </span>
          </div>
        </div>

        <div style={{
          background: 'white',
          color: '#414141',
          padding: '30px',
          borderRadius: '20px',
          marginBottom: '20px',
          textAlign: 'left'
        }}>
          <div style={{ color: levelColor, fontWeight: "700", fontSize: "18px" }}>
            {level === "danger" && "🚨 위험!"}
            {level === "warning" && "⚠️ 주의"}
            {level === "safe" && "✅ 안전"}
          </div>
          <div style={{
            fontSize: '14px',
            lineHeight: '1.8',
            color: '#666'
          }}>
            <div style={{ fontSize: "14px", lineHeight: "1.8", whiteSpace: "pre-wrap" }}>
              {aiResult}
            </div>
          </div>
        </div>

        <button
          onClick={() => onNavigate('home')}
          style={{
            width: '100%',
            padding: '15px',
            background: 'white',
            color: '#0f9aff',
            border: 'none',
            borderRadius: '12px',
            fontSize: '16px',
            fontWeight: '700',
            cursor: 'pointer',
            marginBottom: '10px'
          }}
        >
          새로운 검색하기
        </button>

        <button
          style={{
            width: '100%',
            padding: '15px',
            background: 'rgba(255,255,255,0.2)',
            color: 'white',
            border: '2px solid white',
            borderRadius: '12px',
            fontSize: '14px',
            fontWeight: '600',
            cursor: 'pointer'
          }}
        >
          자세한 정보 보기
        </button>
      </div>
    </div>
  );
}