package com.example.weathershow.service.impl;

import com.example.weathershow.mapper.WeatherKpiMetricsMapper;
import com.example.weathershow.mapper.WeatherTrendMonthMapper;
import com.example.weathershow.mapper.WeatherTrendYearMapper;
import com.example.weathershow.pojo.TrendVo;
import com.example.weathershow.pojo.WeatherKpiMetrics;
import com.example.weathershow.pojo.WeatherTrendMonth;
import com.example.weathershow.pojo.WeatherTrendYear;
import com.example.weathershow.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author X
 * @date 2026/6/9 19:00
 */
@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private WeatherKpiMetricsMapper weatherKpiMetricsMapper;

    @Autowired
    private WeatherTrendYearMapper weatherTrendYearMapper;

    @Autowired
    private WeatherTrendMonthMapper weatherTrendMonthMapper;

    @Override
    public WeatherKpiMetrics getKpiData() {
        WeatherKpiMetrics weatherKpiMetrics = weatherKpiMetricsMapper.queryAll();
        return weatherKpiMetrics;
    }

    @Override
    public TrendVo getTrendYearData() {

        List<WeatherTrendYear> list = weatherTrendYearMapper.queryAll();

        return TrendVo.builder()
                .xAxis(list.stream().map(item -> (Object) item.getYear()).collect(Collectors.toList()))
                .avgSeries(list.stream().map(WeatherTrendYear::getAvgTemp).collect(Collectors.toList()))
                .maxSeries(list.stream().map(WeatherTrendYear::getMaxTemp).collect(Collectors.toList()))
                .minSeries(list.stream().map(WeatherTrendYear::getMinTemp).collect(Collectors.toList()))
                .rangeSeries(list.stream().map(WeatherTrendYear::getAvgDailyRange).collect(Collectors.toList()))
                .build();
    }

    @Override
    public TrendVo getTrendMonthData() {
        List<WeatherTrendMonth> list = weatherTrendMonthMapper.queryAll();

        return TrendVo.builder()
                .xAxis(list.stream().map(item -> (Object) item.getMonthDimension()).collect(Collectors.toList()))
                .avgSeries(list.stream().map(WeatherTrendMonth::getAvgTemp).collect(Collectors.toList()))
                .maxSeries(list.stream().map(WeatherTrendMonth::getMaxTemp).collect(Collectors.toList()))
                .minSeries(list.stream().map(WeatherTrendMonth::getMinTemp).collect(Collectors.toList()))
                .rangeSeries(list.stream().map(WeatherTrendMonth::getAvgDailyRange).collect(Collectors.toList()))
                .build();
    }
}
