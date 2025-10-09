package com.example.demo.home;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

@Service
public class HomeService {
    public Map<String, Object> homeData() {
        Map<String,Object> m = new HashMap<>();
        m.put("hero", Arrays.asList(
                Map.of("title","최고의 캠핑 의자","summary","가벼우면서 편안한 캠핑 의자 추천"),
                Map.of("title","방수 텐트 추천","summary","3계절용 가성비 텐트")
        ));
        m.put("categories", Arrays.asList(
                Map.of("name","캠핑","description","텐트, 의자, 취침도구"),
                Map.of("name","등산","description","백팩, 신발, 스틱")
        ));
        return m;
    }
}
