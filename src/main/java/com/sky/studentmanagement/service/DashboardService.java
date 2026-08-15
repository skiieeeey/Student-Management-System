package com.sky.studentmanagement.service;

import com.sky.studentmanagement.dto.DashboardListDto;
import com.sky.studentmanagement.dto.StatsDto;
import org.springframework.data.domain.Page;

public interface DashboardService {
    StatsDto getStats();

    Page<DashboardListDto> getRecentRegistrations(int page, int size);
}
