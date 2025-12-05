import React, { useState, useRef } from 'react';
import { PRODUCTS } from '../data/products';
import { searchProduct, searchSubstance, searchProductByOcr } from "../hooks/mixApi";

export default function SearchModal({ isOpen, onClose, onSelect, selectedSlot, productsWithIds }) {
  const [searchText, setSearchText] = useState("");
  const [showCamera, setShowCamera] = useState(false);
  const [searchResults, setSearchResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false);
  const [isOcrProcessing, setIsOcrProcessing] = useState(false);
  const fileInputRef = useRef(null);

  const localProducts = productsWithIds || PRODUCTS;

  if (!isOpen) return null;

  const handleSearch = async () => {
    if (!searchText.trim()) {
      alert("검색어를 입력해주세요");
      return;
    }

    setIsSearching(true);

    try {
      const [substanceResult, productResult] = await Promise.allSettled([
        searchSubstance(searchText),
        searchProduct(searchText)
      ]);

      const results = [];

      if (substanceResult.status === 'fulfilled' && substanceResult.value) {
        results.push({
          id: substanceResult.value.substanceId,
          name: substanceResult.value.substanceName,
          image: substanceResult.value.image || null,
          source: 'default'
        });
      }

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

  const handleImageUpload = async (event) => {
    const file = event.target.files?.[0];
    if (!file) return;

    if (!file.type.startsWith('image/')) {
      alert('이미지 파일만 업로드 가능합니다.');
      return;
    }

    // 파일 크기 체크 (5MB)
    const maxSize = 5 * 1024 * 1024;
    if (file.size > maxSize) {
      alert('이미지 크기가 너무 큽니다.\n5MB 이하의 이미지를 사용해주세요.');
      return;
    }

    setIsOcrProcessing(true);

    try {
      console.log("📸 OCR 처리 시작:", file.name);
      
      const result = await searchProductByOcr(file);
      
      if (result && result.productId && result.productName) {
        const ocrResults = [{
          id: result.productId,
          name: result.productName,
          image: result.image || null,
          source: 'prd'
        }];
        
        console.log("✅ OCR 결과:", ocrResults);
        setSearchResults(ocrResults);
        alert(`"${result.productName}" 제품을 찾았습니다!`);
      } else {
        console.warn("OCR 응답:", result);
        alert('제품을 인식하지 못했습니다.\n제품명이 명확하게 보이는 이미지를 사용해주세요.');
      }
    } catch (error) {
      console.error('OCR 처리 오류:', error);
      
      // 에러 메시지 세분화
      let errorMessage = '이미지 인식에 실패했습니다.';
      
      if (error.message.includes('500')) {
        errorMessage = '서버에서 이미지를 처리하지 못했습니다.\n다른 이미지를 시도하거나 잠시 후 다시 시도해주세요.';
      } else if (error.message.includes('네트워크') || error.message.includes('network')) {
        errorMessage = '네트워크 연결을 확인해주세요.';
      } else if (error.message.includes('크기')) {
        errorMessage = error.message;
      }
      
      alert(errorMessage);
    } finally {
      setIsOcrProcessing(false);
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
    }
  };

  const filteredLocalProducts = searchText.trim() 
    ? localProducts.filter(product => product.name.includes(searchText))
    : localProducts;

  const displayProducts = (() => {
    if (searchResults.length > 0) {
      return searchResults;
    }
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
            onClick={() => {
              if (fileInputRef.current) {
                fileInputRef.current.click();
              }
            }}
            disabled={isOcrProcessing}
            style={{
              width: '50px',
              height: '50px',
              borderRadius: '50%',
              border: '2px solid #0f9aff',
              background: isOcrProcessing ? '#ccc' : 'white',
              color: isOcrProcessing ? '#666' : '#0f9aff',
              fontSize: '24px',
              cursor: isOcrProcessing ? 'wait' : 'pointer'
            }}
          >
            {isOcrProcessing ? '⏳' : '📷'}
          </button>

          <input
            ref={fileInputRef}
            type="file"
            accept="image/*"
            onChange={handleImageUpload}
            style={{ display: 'none' }}
          />
        </div>

        {isOcrProcessing && (
          <div style={{
            marginBottom: '20px',
            padding: '20px',
            background: '#e3f2fd',
            borderRadius: '12px',
            textAlign: 'center'
          }}>
            <p style={{ margin: 0, color: '#0f9aff', fontWeight: '600' }}>
              🔍 이미지를 분석하는 중...
            </p>
            <small style={{ color: '#666' }}>잠시만 기다려주세요</small>
          </div>
        )}

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
                        const placeholder = e.target.parentElement.querySelector('.placeholder-icon');
                        if (placeholder) placeholder.style.display = 'flex';
                      }}
                    />
                  ) : null}
                  <div 
                    className="placeholder-icon"
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