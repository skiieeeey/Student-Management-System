package com.sky.studentmanagement.controller;

import com.sky.studentmanagement.dto.DashboardListDto;
import com.sky.studentmanagement.dto.StatsDto;
import com.sky.studentmanagement.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final DashboardService dashboardService;

    DashboardController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String dashboard(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "2") int size,
                                Model model){
        logger.info("Get /dashboard - get request for dashboard stats.");
        StatsDto stats = dashboardService.getStats();
        Page<DashboardListDto> dashboardList = dashboardService.getRecentRegistrations(page, size);

        model.addAttribute("stats", stats);
        model.addAttribute("dashboardList", dashboardList);

        return "dashboard";
    }
}
