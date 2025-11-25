import { useState } from 'react';
import { PRODUCTS } from '../data/products';

// ========================================
// 🔍 검색 모달 컴포넌트
// ========================================
export default function SearchModal({ isOpen, onClose, onSelect, selectedSlot }) {
  const [searchText, setSearchText] = useState('');
  const [showCamera, setShowCamera] = useState(false);

  if (!isOpen) return null;

  const filteredProducts = PRODUCTS.filter(product =>
    product.name.includes(searchText)
  );

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      background: 'rgba(0,0,0,0.5)',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      zIndex: 1000
    }}>
      <div style={{
        background: 'white',
        width: '90%',
        maxWidth: '400px',
        borderRadius: '20px',
        padding: '20px',
        maxHeight: '80vh',
        overflow: 'auto'
      }}>
        <div style={{
          display: 'flex',
          gap: '10px',
          marginBottom: '20px',
          alignItems: 'center'
        }}>
          <div style={{
            flex: 1,
            display: 'flex',
            alignItems: 'center',
            background: '#0f9aff',
            borderRadius: '25px',
            padding: '10px 15px'
          }}>
            <input
              type="text"
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              placeholder="검색어를 입력하세요"
              style={{
                flex: 1,
                border: 'none',
                outline: 'none',
                background: 'transparent',
                color: 'white',
                fontSize: '16px'
              }}
            />
            <div style={{ color: 'white', fontSize: '20px' }}>🔍</div>
          </div>

          <button
            onClick={() => setShowCamera(!showCamera)}
            style={{
              width: '50px',
              height: '50px',
              borderRadius: '50%',
              border: '2px solid #0f9aff',
              background: showCamera ? '#0f9aff' : 'white',
              color: showCamera ? 'white' : '#0f9aff',
              fontSize: '24px',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}
          >
            📷
          </button>
        </div>

        {showCamera && (
          <div style={{
            marginBottom: '20px',
            padding: '20px',
            background: '#f0f9ff',
            borderRadius: '12px',
            textAlign: 'center'
          }}>
            <p style={{ margin: 0, color: '#0f9aff', fontWeight: '600' }}>
              📸 카메라로 제품을 스캔하세요
            </p>
            <small style={{ color: '#999' }}>OCR 기능이 곧 추가될 예정입니다</small>
          </div>
        )}

        {searchText && (
          <div style={{ marginBottom: '15px', color: '#666', fontSize: '14px' }}>
            "{searchText}" 검색 결과: {filteredProducts.length}개
          </div>
        )}

        <div style={{
          display: 'flex',
          flexDirection: 'column',
          gap: '10px'
        }}>
          {filteredProducts.map(product => (
            <div
              key={product.id}
              onClick={() => {
                onSelect(product, selectedSlot);
                onClose();
                setSearchText('');
              }}
              style={{
                padding: '15px',
                background: '#f0f9ff',
                borderRadius: '12px',
                cursor: 'pointer',
                border: '2px solid transparent',
                transition: 'all 0.2s'
              }}
              onMouseOver={(e) => {
                e.currentTarget.style.borderColor = '#0f9aff';
                e.currentTarget.style.background = '#e6f5ff';
              }}
              onMouseOut={(e) => {
                e.currentTarget.style.borderColor = 'transparent';
                e.currentTarget.style.background = '#f0f9ff';
              }}
            >
              <div style={{
                display: 'flex',
                alignItems: 'center',
                gap: '15px'
              }}>
                <img
                  src={product.image}
                  alt={product.name}
                  style={{
                    width: '50px',
                    height: '50px',
                    borderRadius: '8px',
                    objectFit: 'cover'
                  }}
                />
                <div style={{
                  fontSize: '16px',
                  fontWeight: '600',
                  color: '#414141'
                }}>
                  {product.name}
                </div>
              </div>
            </div>
          ))}
        </div>

        <button
          onClick={() => {
            onClose();
            setSearchText('');
          }}
          style={{
            width: '100%',
            marginTop: '20px',
            padding: '12px',
            background: '#e0e0e0',
            border: 'none',
            borderRadius: '12px',
            cursor: 'pointer',
            fontSize: '16px',
            fontWeight: '600'
          }}
        >
          닫기
        </button>
      </div>
    </div>
  );
}