import { useState } from 'react';
import SearchModal from '../components/SearchModal';
import { PRODUCTS } from '../data/products';
import homeIcon from "../assets/home1.svg";
import alertIcon from "../assets/Group.svg";
import settingsIcon from "../assets/Shopping bag.svg";
import mixsafeLogo from "../assets/MIXSAFE.svg";


// ========================================
// 🏠 메인 홈페이지
// ========================================
export default function HomePage({ onNavigate, selectedProducts, setSelectedProducts }) {
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState(null);

  const handleBoxClick = (slotIndex) => {
    setSelectedSlot(slotIndex);
    setModalOpen(true);
  };

  const handleProductSelect = (product, slotIndex) => {
    const newSelected = [...selectedProducts];
    newSelected[slotIndex] = product;
    setSelectedProducts(newSelected);
  };

  const handleSearch = () => {
    if (selectedProducts[0] && selectedProducts[1]) {
      onNavigate('loading');
    } else {
      alert('두 개의 제품을 모두 선택해주세요!');
    }
  };

  return (
    <div style={{ width: '403px', margin: '0 auto', background: '#ffffff', minHeight: '100vh' }}>
      {/* (전체 코드 그대로) */}
      {/* 🔵 상단 파란 헤더 */}
      <header style={{
        background: '#0f9aff',
        padding: '90px 20px 80px',
        borderRadius: '0 0 30px 30px',
        textAlign: 'center'
      }}>
        <img 
          src={mixsafeLogo} 
          alt="MixSafe Logo"
          style={{
            width: "250px",
            height: "auto",
            display: "block",
            margin: "0 auto",
          }}
        />

        <p style={{
          color: '#FFFFFF',
          fontSize: '14px',
          marginTop: '10px',
          marginBottom: '15px'
        }}>
          혼합 되었을 시 염려되는 상품 두 개를 선택해주세요!
        </p>

        <div style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          gap: '25px'
        }}>
          <div
            onClick={() => handleBoxClick(0)}
            style={{
              width: '125px',
              height: '125px',
              background: '#ffffff',
              borderRadius: '10px',
              boxShadow: 'inset 0 1px 4px 0 rgba(0,0,0,0.25)',
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              cursor: 'pointer',
              position: 'relative',
              overflow: 'hidden'
            }}
          >
            {selectedProducts[0] ? (
              <div style={{ textAlign: 'center', padding: '10px' }}>
                <img
                  src={selectedProducts[0].image}
                  alt={selectedProducts[0].name}
                  style={{
                    width: '70px',
                    height: '70px',
                    objectFit: 'cover',
                    borderRadius: '8px'
                  }}
                />
                <div style={{
                  fontSize: '11px',
                  marginTop: '5px',
                  color: '#0f9aff',
                  fontWeight: '600'
                }}>
                  {selectedProducts[0].name}
                </div>
              </div>
            ) : (
              <div style={{
                fontSize: '40px',
                color: '#9CD5FF'
              }}>+</div>
            )}
          </div>

          <div style={{ fontSize: '35px', color: 'white' }}>+</div>

          <div
            onClick={() => handleBoxClick(1)}
            style={{
              width: '125px',
              height: '125px',
              background: '#ffffff',
              borderRadius: '10px',
              boxShadow: 'inset 0 1px 4px 0 rgba(0,0,0,0.25)',
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              cursor: 'pointer',
              position: 'relative',
              overflow: 'hidden'
            }}
          >
            {selectedProducts[1] ? (
              <div style={{ textAlign: 'center', padding: '10px' }}>
                <img
                  src={selectedProducts[1].image}
                  alt={selectedProducts[1].name}
                  style={{
                    width: '70px',
                    height: '70px',
                    objectFit: 'cover',
                    borderRadius: '8px'
                  }}
                />
                <div style={{
                  fontSize: '11px',
                  marginTop: '5px',
                  color: '#0f9aff',
                  fontWeight: '600'
                }}>
                  {selectedProducts[1].name}
                </div>
              </div>
            ) : (
              <div style={{
                fontSize: '40px',
                color: '#9CD5FF'
              }}>+</div>
            )}
          </div>
        </div>

        <button
          onClick={handleSearch}
          style={{
            width: '120px',
            height: '34px',
            background: '#FFFFFF',
            borderRadius: '12px',
            border: 'none',
            marginTop: '20px',
            fontSize: '14px',
            fontWeight: '700',
            color: '#0F98FD',
            cursor: 'pointer'
          }}
        >
          검색하기
        </button>
      </header>

      <section style={{ marginTop: '-20px' }}>
        <div style={{
          width: '100%',
          padding: '25px 15px 100px',
          borderRadius: '12px 12px 0 0',
          background: 'linear-gradient(180deg, #F0F9FF 0%, #D5E6F1 100%)',
          boxSizing: 'border-box'
        }}>
          <div style={{
            padding: '0 5px',
            marginBottom: '20px'
          }}>
            <div style={{
              fontSize: '15px',
              fontWeight: '700',
              color: '#414141'
            }}>
              검색하고 싶은 물질이 여기 있다면?
            </div>
          </div>

          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(3, 1fr)',
            gap: '10px',
            marginBottom: '20px'
          }}>
            {PRODUCTS.map((product) => (
              <div
                key={product.id}
                onClick={() => {
                  if (!selectedProducts[0]) {
                    handleProductSelect(product, 0);
                  } else if (!selectedProducts[1]) {
                    handleProductSelect(product, 1);
                  } else {
                    alert('이미 두 개의 제품이 선택되었습니다.');
                  }
                }}
                style={{
                  borderRadius: '12px',
                  overflow: 'hidden',
                  cursor: 'pointer',
                  position: 'relative',
                  height: '115px'
                }}
              >
                <img
                  src={product.image}
                  alt={product.name}
                  style={{
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover',
                    display: 'block'
                  }}
                />

                <div style={{
                  position: 'absolute',
                  bottom: 0,
                  left: 0,
                  width: '100%',
                  height: '60px',
                  background: 'linear-gradient(180deg, rgba(15, 154, 255, 0) 0%, rgba(15, 154, 255, 0.95) 70%)',
                  pointerEvents: 'none'
                }}></div>

                <div style={{
                  position: 'absolute',
                  bottom: '8px',
                  left: 0,
                  right: 0,
                  fontWeight: '700',
                  fontSize: '12px',
                  color: 'white',
                  textAlign: 'center',
                  textShadow: '0 2px 8px rgba(0,0,0,0.5)',
                  zIndex: 10,
                  padding: '0 5px'
                }}>
                  {product.name}
                </div>
              </div>
            ))}
          </div>

        </div>
      </section>

      <footer style={{
        position: 'fixed',
        bottom: 0,
        left: '50%',
        transform: 'translateX(-50%)',
        width: '403px',
        background: '#F0F9FF',
        display: 'flex',
        justifyContent: 'space-between',
        padding: '25px 45px 15px',
        borderRadius: '12px 12px 0 0',
        boxShadow: '0 -2px 10px rgba(0,0,0,0.05)'
      }}>
        {/* 알림 */}
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px' }}>
          <img src={alertIcon} alt="알림" style={{ width: '26px', height: '26px' }} />
          <span style={{ fontSize: '13px', color: '#0f9aff' }}>알림</span>
        </div>

        {/* 홈 */}
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px' }}>
          <img src={homeIcon} alt="홈" style={{ width: '28px', height: '28px' }} />
          <span style={{ fontSize: '13px', color: '#0f9aff' }}>홈</span>
        </div>

        {/* 설정 */}
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px' }}>
          <img src={settingsIcon} alt="설정" style={{ width: '26px', height: '26px' }} />
          <span style={{ fontSize: '13px', color: '#0f9aff' }}>설정</span>
        </div>
      </footer>

      <SearchModal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        onSelect={handleProductSelect}
        selectedSlot={selectedSlot}
      />
    </div>
  );
}