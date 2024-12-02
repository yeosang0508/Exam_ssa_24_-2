package com.example.exam_ssa_24_12.controller;

import com.example.exam_ssa_24_12.service.TourismService;
import com.example.exam_ssa_24_12.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TourismController {

    private final TourismService tourismService;
    private final WeatherService weatherService;

    // 메인 페이지 표시
    @GetMapping("/")
    public String showMainPage() {
        System.out.println("Main page accessed");
        // templates/tourism/main.html 반환
        return "tourism/main";
    }

    // 관광지 검색 API
    @GetMapping("/tourism/search")
    @ResponseBody
    public Map<String, Object> searchTouristSpots(@RequestParam String cityName) {
        String response = tourismService.searchTouristSpots(cityName);

        // 텍스트 데이터를 수동으로 파싱
        Map<String, Object> parsedData = new HashMap<>();
        String[] spots = response.split("\n");
        List<Map<String, String>> spotDetails = new ArrayList<>();

        for (String spot : spots) {
            Map<String, String> spotInfo = new HashMap<>();
            String[] details = spot.split(", ");
            for (String detail : details) {
                if (detail.startsWith("관광지: ")) {
                    spotInfo.put("title", detail.replace("관광지: ", "").trim());
                } else if (detail.startsWith("주소: ")) {
                    spotInfo.put("address", detail.replace("주소: ", "").trim());
                } else if (detail.startsWith("전화번호: ")) {
                    spotInfo.put("phone", detail.replace("전화번호: ", "").trim());
                }

            }
            if (!spotInfo.isEmpty()) {
                spotDetails.add(spotInfo);
            }
        }
        parsedData.put("items", spotDetails);
        return parsedData;
    }


    // 날씨 정보 API
    @GetMapping("/weather")
    @ResponseBody
    public Map<String, Object> getWeatherInfo(@RequestParam String cityName) {
        System.out.println("Weather search called for city: " + cityName);

        Map<String, Object> response = new HashMap<>();
        try {
            // 날씨 서비스 호출
            String weatherInfo = weatherService.getWeather(cityName);
            response.put("status", "success");
            response.put("data", weatherInfo);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "날씨 정보를 불러오는 중 오류가 발생했습니다.");
            System.err.println("Weather search error: " + e.getMessage());
        }

        return response;
    }
}
