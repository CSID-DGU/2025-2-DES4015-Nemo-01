import "./styles/global.css";

function App() {
  return (
    <div className="home">

      {/* 🎬 Frame 18 — 상단 파란 헤더 전체 */}
      <header className="header">
        <h1 className="header-title">MIX SAFE</h1>
        <p className="header-desc">혼합 되었을 시 염려되는 상품 두 개를 선택해주세요!</p>

        {/* 🎬 Frame 18 내부 — 섞는 박스 영역 */}
        <div className="mix-area">
          <div className="mix-box">
            <div className="plus-icon"></div>
        </div>
          <div style={{ color: "white", fontSize: "35px" }}>+</div>
          <div className="mix-box">
            <div className="plus-icon"></div>
          </div>
        </div>

        <button className="search-btn">검색하기</button>
      </header>


      {/* 🎬 Frame 20 — 전체 추천 영역 배경 (파란→흰 그라데이션 박스) */}
      <section className="recommend-wrapper">

        <div className="recommend-content">

          {/* 🎬 Frame 27 — 추천 제목 + 전체보기 */}
          <div className="recommend-header">
            <div className="recommend-title">검색하고 싶은 물질이 여기 있다면?</div>
            <div className="recommend-header-right">전체보기</div>
          </div>

          {/* 🎬 Frame 26 — 물질 추천 카드 리스트 */}
          <div className="product-list">
            
            {/* 카드 1 */}
            <div className="product">
              <div className="product-image-wrapper">
                <img src="/src/assets/hotwater.png" alt="뜨거운 물" />
                <div className="gradient-overlay"></div>
                <div className="product-label">뜨거운 물</div>
              </div>
            </div>

            {/* 카드 2 */}
            <div className="product">
              <div className="product-image-wrapper">
                <img src="/src/assets/oil.png" alt="기름" />
                <div className="gradient-overlay"></div>
                <div className="product-label">기름</div>
              </div>
            </div>

            {/* 카드 3 */}
            <div className="product">
              <div className="product-image-wrapper">
                <img src="/src/assets/coldwater.png" alt="차가운 물" />
                <div className="gradient-overlay"></div>
                <div className="product-label">차가운 물</div>
              </div>
            </div>

          </div>


          <div className="dots">
            <span className="dot active"></span>
            <span className="dot"></span>
            <span className="dot"></span>
          </div>

        </div>
      </section>


      {/* 🎬 Frame 20 하단 구성물 — 네비게이션 바 */}
      <footer className="navbar">
        <div className="nav-item">
          <img src="/src/assets/Shopping bag.svg" />
          알림
        </div>
        <div className="nav-item">
          <img src="/src/assets/home1.svg" />
          홈
        </div>
        <div className="nav-item">
          <img src="/src/assets/Group.svg" />
          설정
        </div>
      </footer>

    </div>
  );
}

export default App;