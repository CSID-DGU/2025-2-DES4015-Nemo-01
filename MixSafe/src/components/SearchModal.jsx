import React from 'react';
import { useState } from 'react';
import { PRODUCTS } from '../data/products';
import { searchProduct, searchSubstance } from "../hooks/mixApi";

// ========================================
// 검색 모달 컴포넌트
// ========================================
export default function SearchModal({ isOpen, onClose, onSelect, selectedSlot }) {

  console.log("SearchModal isOpen:", isOpen);

  const [searchText, setSearchText] = useState("");
  const [showCamera, setShowCamera] = useState(false);
  const [searchResults, setSearchResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false);

  if (!isOpen) return null;

  const filteredProducts = PRODUCTS.filter(product =>
    product.name.includes(searchText)
  );

  const handleSearch = async () => {
    if (!searchText.trim()) {
      setSearchResults([]);
      return;
    }

    setIsSearching(true);

    try {
      const [substanceResult, productResult] = await Promise.all([
        searchSubstance(searchText),
        searchProduct(searchText)
      ]);

      const results = [];

      // ✅ substance도 prd
      if (substanceResult) {
        results.push({ ...substanceResult, source: 'prd' });
      }

      // ✅ API 제품은 무조건 prd
      if (productResult) {
        results.push({ ...productResult, source: 'prd' });
      }

      setSearchResults(results);
    } catch (error) {
      console.error('검색 오류:', error);
      setSearchResults([]);
    } finally {
      setIsSearching(false);
    }
  };

  const filteredLocalProducts = PRODUCTS.filter(product =>
    product.name.includes(searchText)
  );

  // ✅ 표시할 리스트 결정
  const displayProducts = (() => {
    if (searchResults.length > 0) return searchResults;
    if (filteredLocalProducts.length > 0) return filteredLocalProducts;

    // ✅ 입력만 해도 선택 가능, prd 처리
    if (searchText.trim()) {
      return [{
        id: `temp-${Date.now()}`,
        name: searchText,
        image: 'https://via.placeholder.com/100',
        source: 'prd'
      }];
    }

    return [];
  })();

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
        maxWidth: '360px',
        minHeight: '300px',
        borderRadius: '20px',
        padding: '20px',
        boxSizing: 'border-box',
        position: 'relative'
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
              onKeyDown={(e) => {
                if (e.key === 'Enter') handleSearch();
              }}
            />
            <button
              onClick={handleSearch}
              style={{ background: "none", border: "none", color: "white", fontSize: "20px", cursor: "pointer" }}
            >
              🔍
            </button>
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
              cursor: 'pointer'
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
            "{searchText}" 검색 결과: {displayProducts.length}개
          </div>
        )}

        <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
          {displayProducts.map((product) => {
            const isDefault = PRODUCTS.some(p => p.id === product.id);

            return (
              <div
                key={product.id}
                onClick={() => {
                  onSelect({
                    id: product.productId ?? product.id,
                    name: product.productName ?? product.name,
                    image: product.image || 'https://via.placeholder.com/100',
                    source: isDefault ? "default" : "prd"
                  }, selectedSlot);

                  onClose();
                  setSearchText('');
                }}
                style={{
                  padding: '15px',
                  background: '#f0f9ff',
                  borderRadius: '12px',
                  cursor: 'pointer'
                }}
              >
                <div style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
                  <img
                    src={product.image || 'https://via.placeholder.com/100'}
                    alt={product.name}
                    style={{ width: '50px', height: '50px', borderRadius: '8px' }}
                  />
                  <div style={{ fontWeight: '600', color: '#414141' }}>
                    {product.name}
                  </div>
                </div>
              </div>
            );
          })}
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
            borderRadius: '12px',
            border: 'none',
            cursor: 'pointer'
          }}
        >
          닫기
        </button>
      </div>
    </div>
  );
}
