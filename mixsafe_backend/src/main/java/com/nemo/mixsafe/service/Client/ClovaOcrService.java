package com.nemo.mixsafe.service.Client;

import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClovaOcrService {

    @Value("${naver.ocr.invoke-url}")
    private String invokeUrl;

    @Value("${naver.ocr.secret-key}")
    private String secretKey;

    public String extractTextFromImage(String base64Image) throws IOException {

        URL url = new URL(invokeUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("X-OCR-SECRET", secretKey);

        // JSON Body 생성
        JsonObject requestJson = new JsonObject();
        JsonArray images = new JsonArray();
        JsonObject image = new JsonObject();

        image.addProperty("format", "jpg");
        image.addProperty("name", "sample_image");
        image.addProperty("data", base64Image);

        images.add(image);

        requestJson.add("images", images);
        requestJson.addProperty("version", "V2");
        requestJson.addProperty("requestId", "mixsafe_ocr");
        requestJson.addProperty("timestamp", System.currentTimeMillis());

        // 요청 보내기
        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestJson.toString().getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        InputStream responseStream = (code == 200)
                ? conn.getInputStream()
                : conn.getErrorStream();

        String rawJson = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println("============== OCR RAW JSON ==============");
        System.out.println(rawJson);
        System.out.println("==========================================");

        return parseInferText(rawJson);
    }

    private String parseInferText(String json) {

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        if (!root.has("images")) return "";

        JsonArray images = root.getAsJsonArray("images");
        StringBuilder sb = new StringBuilder();

        for (JsonElement imgElem : images) {
            JsonObject imgObj = imgElem.getAsJsonObject();
            JsonArray fields = imgObj.getAsJsonArray("fields");

            if (fields == null) continue;

            for (JsonElement fElem : fields) {
                String text = fElem.getAsJsonObject().get("inferText").getAsString();
                sb.append(text).append(" ");
            }
        }

        return sb.toString().trim();
    }
}
