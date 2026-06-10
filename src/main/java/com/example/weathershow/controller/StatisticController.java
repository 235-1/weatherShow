package com.example.weathershow.controller;


import com.example.weathershow.pojo.Result;
import com.example.weathershow.pojo.TrendVo;
import com.example.weathershow.pojo.WeatherKpiMetrics;
import com.example.weathershow.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author X
 * @date 2026/6/9 18:06
 */

@RestController
public class StatisticController {

    @Autowired
    private StatisticService statisticService;
    /*
     * 获取主页四个kpi数据
     * 年均气温（20年均值）
     * 极端高温阈值
     * 极端低温阈值
     * 气温日较差均值
     * */
    @GetMapping("/kpi")
    public Result getKpiData() {
        WeatherKpiMetrics weatherKpiMetrics = statisticService.getKpiData();
        return Result.success(weatherKpiMetrics);
    }

    /*
    * 气温趋势图数据
    * */
    @GetMapping("/trendYear")
    public Result getTrendData() {
        TrendVo trendVo = statisticService.getTrendYearData();
        return Result.success(trendVo);
    }

    @GetMapping("/trendMonth")
    public Result getTrendMonthData() {
        TrendVo trendVo = statisticService.getTrendMonthData();
        return Result.success(trendVo);
    }
}
