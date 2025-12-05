const API_BASE_URL = "http://13.209.163.252";

// 혼합 결과 API
export async function fetchMixResult(product1, product2) {
  console.log("📥 입력된 제품:", { product1, product2 });

  // ✅ 서버가 기대하는 형식: product1Id, product2Id (통일), source1, source2로 구분
  const payload = {
    product1Id: product1.id,
    product2Id: product2.id,
    source1: product1.source,
    source2: product2.source
  };

  console.log("📤 Mix API 요청 payload:", payload);

  try {
    const response = await fetch(`${API_BASE_URL}/api/mix`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      const errorText = await response.text();
      console.error("❌ API 에러 응답:", errorText);
      throw new Error(`API 요청 실패: ${response.status} - ${errorText}`);
    }

    const data = await response.json();
    console.log("✅ 서버 응답:", data);
    return data;
  } catch (error) {
    console.error("fetchMixResult 오류:", error);
    throw error;
  }
}


// 제품 검색 (초록누리) - productName으로 검색
export async function searchProduct(productName) {
  if (!productName.trim()) return null;

  try {
    const url = `${API_BASE_URL}/api/product?productName=${encodeURIComponent(productName)}`;
    console.log("🔍 제품 검색 URL:", url);
    
    const response = await fetch(url);

    if (!response.ok) {
      console.warn("제품 검색 실패:", response.status);
      return null;
    }
    
    const data = await response.json();
    console.log("✅ 제품 검색 결과:", data);
    
    // ✅ 응답 데이터가 배열인 경우 첫 번째 항목 반환
    if (Array.isArray(data) && data.length > 0) {
      return data[0];
    }
    
    return data;
  } catch (error) {
    console.error("searchProduct 오류:", error);
    return null;
  }
}


// 물질 검색 (기본 물질) - substanceName으로 검색
export async function searchSubstance(substanceName) {
  if (!substanceName.trim()) return null;

  try {
    const url = `${API_BASE_URL}/api/substance?substanceName=${encodeURIComponent(substanceName)}`;
    console.log("🔍 물질 검색 URL:", url);
    
    const response = await fetch(url);

    if (!response.ok) {
      console.warn("물질 검색 실패:", response.status);
      return null;
    }
    
    const data = await response.json();
    console.log("✅ 물질 검색 결과:", data);
    
    // ✅ 응답 데이터가 배열인 경우 첫 번째 항목 반환
    if (Array.isArray(data) && data.length > 0) {
      return data[0];
    }
    
    return data;
  } catch (error) {
    console.error("searchSubstance 오류:", error);
    return null;
  }
}


// OCR 이미지 검색 API - Base64 인코딩 방식
export async function searchProductByOcr(imageFile) {
  try {
    // 파일 크기 체크 (5MB 제한)
    const maxSize = 5 * 1024 * 1024;
    if (imageFile.size > maxSize) {
      throw new Error('이미지 크기가 너무 큽니다. 5MB 이하의 이미지를 사용해주세요.');
    }

    // 이미지를 Base64로 변환
    const base64Image = await new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsDataURL(imageFile);
    });

    const response = await fetch(`${API_BASE_URL}/api/search/ocr-image`, {
      method: "POST",
      headers: { 
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        base64Image: base64Image
      })
    });

    if (!response.ok) {
      throw new Error(`OCR 검색 실패: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("OCR 오류:", error);
    throw error;
  }
}