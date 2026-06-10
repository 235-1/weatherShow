package com.example.weathershow.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author X
 * @date 2026/6/9 18:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherExtreme {
    private Integer year;
    private Integer month;
    private String extremeType; // 对应 extreme_type
    private Integer occurrenceCount; // 对应 occurrence_count
    private Double thresholdValue;   // 对应 threshold_value
}
