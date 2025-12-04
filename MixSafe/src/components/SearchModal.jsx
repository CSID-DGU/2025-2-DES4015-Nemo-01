import React from 'react';
import { useState } from 'react';
import { PRODUCTS } from '../data/products';
import { searchProduct, searchSubstance } from "../hooks/mixApi";

export default function SearchModal({ isOpen, onClose, onSelect, selectedSlot }) {
  const [searchText, setSearchText] = useState("");
  const [showCamera, setShowCamera] = useState(false);
  const [searchResults, setSearchResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false);

  if (!isOpen) return null;

  const handleSearch = async () => {
    if (!searchText.trim()) {
      alert("검색어를 입력해주세요");
      return;
    }

    setIsSearching(true);

    try {
      // ✅ 물질 검색과 제품 검색 병렬 실행 (에러 무시)
      const [substanceResult, productResult] = await Promise.allSettled([
        searchSubstance(searchText),
        searchProduct(searchText)
      ]);

      const results = [];

      // ✅ 물질 검색 결과 (fulfilled인 경우만)
      if (substanceResult.status === 'fulfilled' && substanceResult.value) {
        results.push({
          id: substanceResult.value.substanceId,
          name: substanceResult.value.substanceName,
          image: substanceResult.value.image || null,
          source: 'default'
        });
      }

      // ✅ 제품 검색 결과 (fulfilled인 경우만)
      if (productResult.status === 'fulfilled' && productResult.value) {
        results.push({
          id: productResult.value.productId,
          name: productResult.value.productName,
          image: productResult.value.image || null,
          source: 'prd'
        });
      }

      console.log("🔍 API 검색 결과:", results);
      
      if (results.length === 0) {
        alert(`"${searchText}"에 대한 검색 결과가 없습니다.\n다른 검색어를 입력해주세요.`);
      }
      
      setSearchResults(results);
    } catch (error) {
      console.error('검색 오류:', error);
      alert('검색 중 오류가 발생했습니다. 다시 시도해주세요.');
      setSearchResults([]);
    } finally {
      setIsSearching(false);
    }
  };

  // ✅ 로컬 제품 필터링 (PRODUCTS 배열에서)
  const filteredLocalProducts = searchText.trim() 
    ? PRODUCTS.filter(product => product.name.includes(searchText))
    : PRODUCTS; // 검색어가 없으면 전체 표시

  // ✅ 표시할 리스트 결정
  const displayProducts = (() => {
    // API 검색 결과가 있으면 우선 표시
    if (searchResults.length > 0) {
      return searchResults;
    }
    
    // 로컬 제품 표시 (검색어 필터링 적용)
    return filteredLocalProducts;
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
        maxHeight: '80vh',
        borderRadius: '20px',
        padding: '20px',
        boxSizing: 'border-box',
        position: 'relative',
        display: 'flex',
        flexDirection: 'column'
      }}>
        {/* 검색 입력 영역 */}
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
              onChange={(e) => {
                setSearchText(e.target.value);
                // 입력 중에는 API 검색 결과 초기화
                setSearchResults([]);
              }}
              placeholder="제품명을 입력하세요"
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
              disabled={isSearching}
              style={{ 
                background: "none", 
                border: "none", 
                color: "white", 
                fontSize: "20px", 
                cursor: isSearching ? "wait" : "pointer",
                opacity: isSearching ? 0.5 : 1
              }}
            >
              {isSearching ? "⏳" : "🔍"}
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

        {/* 카메라 안내 */}
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

        {/* 검색 결과 안내 */}
        <div style={{ marginBottom: '15px', color: '#666', fontSize: '14px' }}>
          {searchResults.length > 0 ? (
            <div style={{ color: '#0f9aff', fontWeight: '600' }}>
              🔍 검색 결과: {searchResults.length}개
            </div>
          ) : searchText.trim() ? (
            <div>
              💡 "{searchText}"에 대한 결과는 아래 기본 물질 또는 검색 버튼을 눌러주세요
            </div>
          ) : (
            <div>
              💡 제품을 선택하거나 검색어를 입력하세요
            </div>
          )}
        </div>

        {/* 제품 목록 */}
        <div style={{ 
          display: 'flex', 
          flexDirection: 'column', 
          gap: '10px',
          overflowY: 'auto',
          flex: 1,
          marginBottom: '15px'
        }}>
          {displayProducts.length === 0 ? (
            <div style={{
              padding: '40px 20px',
              textAlign: 'center',
              color: '#999'
            }}>
              검색 결과가 없습니다
            </div>
          ) : (
            displayProducts.map((product, index) => (
              <div
                key={product.id + '-' + index}
                onClick={() => {
                  console.log("✅ 제품 선택:", product);
                  onSelect(product, selectedSlot);
                  onClose();
                  setSearchText('');
                  setSearchResults([]);
                }}
                style={{
                  padding: '15px',
                  background: '#f0f9ff',
                  borderRadius: '12px',
                  cursor: 'pointer',
                  transition: 'all 0.2s'
                }}
                onMouseEnter={(e) => {
                  e.currentTarget.style.background = '#e0f2ff';
                  e.currentTarget.style.transform = 'translateY(-2px)';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.background = '#f0f9ff';
                  e.currentTarget.style.transform = 'translateY(0)';
                }}
              >
                <div style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
                  {product.image ? (
                    <img
                      src={product.image}
                      alt={product.name}
                      style={{ 
                        width: '50px', 
                        height: '50px', 
                        borderRadius: '8px', 
                        objectFit: 'cover',
                        border: '1px solid #e0e0e0'
                      }}
                      onError={(e) => {
                        e.target.style.display = 'none';
                        e.target.nextSibling.style.display = 'flex';
                      }}
                    />
                  ) : null}
                  <div 
                    style={{ 
                      width: '50px', 
                      height: '50px', 
                      borderRadius: '8px', 
                      background: '#e0e0e0',
                      display: product.image ? 'none' : 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontSize: '24px'
                    }}
                  >
                    📦
                  </div>
                  <div style={{ flex: 1 }}>
                    <div style={{ fontWeight: '600', color: '#414141', marginBottom: '4px' }}>
                      {product.name}
                    </div>
                    <div style={{ fontSize: '12px', color: '#999' }}>
                      {product.source === 'default' ? '🧪 기본 물질' : '🛒 초록누리 제품'}
                    </div>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>

        {/* 닫기 버튼 */}
        <button
          onClick={() => {
            onClose();
            setSearchText('');
            setSearchResults([]);
          }}
          style={{
            width: '100%',
            padding: '12px',
            background: '#e0e0e0',
            borderRadius: '12px',
            border: 'none',
            cursor: 'pointer',
            fontSize: '14px',
            fontWeight: '600',
            transition: 'background 0.2s'
          }}
          onMouseEnter={(e) => e.currentTarget.style.background = '#d0d0d0'}
          onMouseLeave={(e) => e.currentTarget.style.background = '#e0e0e0'}
        >
          닫기
        </button>
      </div>
    </div>
  );
}