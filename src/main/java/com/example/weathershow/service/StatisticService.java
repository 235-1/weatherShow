package com.example.weathershow.service;

import com.example.weathershow.pojo.TrendVo;
import com.example.weathershow.pojo.WeatherKpiMetrics;
import org.springframework.stereotype.Service;

/**
 * @author X
 * @date 2026/6/9 19:00
 */


public interface StatisticService {

    // 获取主页四个kpi数据
    WeatherKpiMetrics getKpiData();

    // 获取气温趋势年变化图数据
    TrendVo getTrendYearData();

    // 获取气温趋势月变化图数据
    TrendVo getTrendMonthData();
}
