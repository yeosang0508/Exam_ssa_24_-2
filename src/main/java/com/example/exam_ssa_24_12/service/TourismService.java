package com.example.exam_ssa_24_12.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class TourismService {

    @Value("${tourism.apiKey}")
    private String serviceKey;

    public String searchTouristSpots(String keyword) {
        try {
            // API URL 구성
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService1/searchKeyword1");
            urlBuilder.append("?serviceKey=").append(serviceKey);
            urlBuilder.append("&MobileOS=ETC");
            urlBuilder.append("&MobileApp=TourismApp");
            urlBuilder.append("&_type=json");
            urlBuilder.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));

            System.out.println("Tourism API Request URL: " + urlBuilder.toString());
            // HTTP 연결
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Tourism API 호출 실패: " + responseCode + " - " + conn.getResponseMessage());
            }

            // 응답 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // JSON 응답 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            if (itemsNode.isArray()) {
                StringBuilder result = new StringBuilder();
                for (JsonNode item : itemsNode) {
                    String title = item.path("title").asText("정보 없음");
                    String address = item.path("addr1").asText("정보 없음");
                    String tel = item.path("tel").asText("정보 없음");

                    result.append("관광지: ").append(title).append(", 주소: ").append(address).append(", 전화번호: ").append(tel).append("\n");
                }
                return result.toString();
            } else {
                return "관광지 정보를 찾을 수 없습니다.";
            }

        } catch (Exception e) {
            System.err.println("Tourism API Error: " + e.getMessage());
            throw new RuntimeException("관광지 정보를 가져오는 중 문제가 발생했습니다.", e);
        }
    }
}
