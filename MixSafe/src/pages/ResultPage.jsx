import { useNavigate } from "react-router-dom";
import "../styles/global.css";

export default function ResultPage() {
  const navigate = useNavigate();

  return (
    <div className="result">
      <h1>혼합 결과</h1>
      <p>⚠️ 락스 + 뜨거운 물 → 염소가스가 발생할 수 있어요!</p>

      <button onClick={() => navigate("/")}>돌아가기</button>
    </div>
  );
}
