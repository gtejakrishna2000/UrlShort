package com.example.UrlShort.Dtos;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class DashboardDto {


    private Map<String, Integer> UrlClickMap = new HashMap<String, Integer>();

    @Override
    public String toString() {
        return "User Dashboard Information{" +
                ",ShortLinks='"+
                UrlClickMap + '\'' +

                '}';
    }

}
