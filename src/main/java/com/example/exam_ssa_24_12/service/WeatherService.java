package com.example.exam_ssa_24_12.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherService {

    @Value("${weather.apiKey}")
    private String apiKey;

    public String getWeather(String cityName) {
        try {
            // 지역명 매핑
            cityName = mapCityToEnglish(cityName);

            // API URL 구성
            String url = UriComponentsBuilder.fromHttpUrl("http://api.openweathermap.org/data/2.5/weather")
                    .queryParam("q", cityName)
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .toUriString();

            System.out.println("Weather API Request URL: " + url);

            // HTTP 연결
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Weather API 호출 실패: " + responseCode + " - " + conn.getResponseMessage());
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

            String temp = rootNode.path("main").path("temp").asText("정보 없음");
            String description = rootNode.path("weather").get(0).path("description").asText("정보 없음");
            String windSpeed = rootNode.path("wind").path("speed").asText("정보 없음");

            return String.format("현재 온도: %s°C, 날씨 상태: %s, 풍속: %s m/s", temp, description, windSpeed);

        } catch (Exception e) {
            System.err.println("Weather API Error: " + e.getMessage());
            throw new RuntimeException("날씨 정보를 가져오는 중 문제가 발생했습니다.", e);
        }
    }

// 지역 매핑 메서드
    private String mapCityToEnglish(String city) {
        switch (city) {
            case "대전":
                return "daejeon";
            case "서울":
                return "seoul";
            case "부산":
                return "busan";
            case "광주":
                return "gwangju";
            case "인천":
                return "incheon";
            case "대구":
                return "daegu";
            case "울산":
                return "ulsan";
            case "수원":
                return "suwon";
            case "고양":
                return "goyang";
            case "용인":
                return "yongin";
            case "창원":
                return "changwon";
            case "성남":
                return "seongnam";
            case "청주":
                return "cheongju";
            case "천안":
                return "cheonan";
            case "전주":
                return "jeonju";
            case "안산":
                return "ansan";
            case "남양주":
                return "namyangju";
            case "안양":
                return "anyang";
            case "김해":
                return "gimhae";
            case "평택":
                return "pyeongtaek";
            case "포항":
                return "pohang";
            case "제주":
                return "jeju";
            case "김포":
                return "gimpo";
            case "의정부":
                return "uiwangbu";
            case "구미":
                return "gumi";
            case "광명":
                return "gwangmyeong";
            case "양산":
                return "yangsan";
            case "원주":
                return "wonju";
            case "울진":
                return "uljin";
            case "춘천":
                return "chuncheon";
            default:
                return city; // 매핑되지 않은 경우 입력값 그대로 반환
        }
    }

}
