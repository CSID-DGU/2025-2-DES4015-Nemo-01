const API_BASE_URL = "";

// 혼합 결과 API
export async function fetchMixResult(product1, product2) {
  const payload = {
    product1Id: product1.id,
    product2Id: product2.id,
    source1: product1.source,
    source2: product2.source,
  };

  try {
    const response = await fetch(`${API_BASE_URL}/api/mix`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      throw new Error(`API 요청 실패: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("fetchMixResult 오류:", error);
    throw error;
  }
  const data = await response.json();
console.log("🔥 서버 응답:", data);
return data;
}


// 제품 검색 (초록누리)
export async function searchProduct(productName) {
  try {
    const response = await fetch(
      `${API_BASE_URL}/api/product?productName=${encodeURIComponent(productName)}`
    );

    if (!response.ok) throw new Error("제품 검색 실패");
    return await response.json();
  } catch (error) {
    console.error("searchProduct 오류:", error);
    return null;
  }
}


// 물질 검색
export async function searchSubstance(substanceName) {
  try {
    const response = await fetch(
      `${API_BASE_URL}/api/substance?substanceName=${encodeURIComponent(substanceName)}`
    );

    if (!response.ok) throw new Error("물질 검색 실패");
    return await response.json();
  } catch (error) {
    console.error("searchSubstance 오류:", error);
    return null;
  }
}
