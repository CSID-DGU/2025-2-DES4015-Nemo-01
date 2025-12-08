import React from 'react';
import mixsafeLogo from "../assets/MIXSAFE.svg";

// ========================================
// 결과 페이지
// ========================================
export default function ResultPage({ onNavigate, selectedProducts, mixResult }) {
  const status = mixResult?.status || "UNKNOWN";
  const aiResult = mixResult?.aiResult || '분석 결과를 불러오는데 실패했습니다.';
  
  // ✅ AI 응답 시작 부분으로 위험도 판단
  const getDangerLevel = (text) => {
    if (!text) return "result";
    
    const trimmedText = text.trim();
    
    // "안전:" 으로 시작
    if (trimmedText.startsWith("안전:")) {
      return "safe";
    }
    
    // "위험:" 으로 시작
    if (trimmedText.startsWith("위험:")) {
      return "danger";
    }
    
    // "주의:" 으로 시작
    if (trimmedText.startsWith("주의:")) {
      return "warning";
    }
    
    // 그 외 모든 경우
    return "result";
  };

  const level = getDangerLevel(aiResult);

  const levelConfig = {
    danger: {
      color: "#ff4d4d",
      icon: "🚨",
      label: "위험!"
    },
    warning: {
      color: "#ffb300",
      icon: "⚠️",
      label: "주의"
    },
    safe: {
      color: "#4caf50",
      icon: "✅",
      label: "안전"
    },
    result: {
      color: "#0f9aff",
      icon: "➡️",
      label: "결과"
    }
  };

  const currentLevel = levelConfig[level];

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
        <img 
          src={mixsafeLogo}
          alt="MixSafe Logo"
          style={{
            width: "250px",
            height: "auto",
            display: "block",
            margin: "0 auto 20px"
          }}
        />

        <div style={{
          fontSize: '24px',
          fontWeight: '700',
          marginBottom: '30px'
        }}>
          {currentLevel.icon} 혼합 결과
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
          <div style={{ 
            color: currentLevel.color, 
            fontWeight: "700", 
            fontSize: "20px",
            marginBottom: "15px",
            display: "flex",
            alignItems: "center",
            gap: "8px"
          }}>
            <span>{currentLevel.icon}</span>
            <span>{currentLevel.label}</span>
          </div>
          
          <div style={{
            fontSize: '14px',
            lineHeight: '1.8',
            color: '#666',
            whiteSpace: 'pre-wrap'
          }}>
            {aiResult}
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