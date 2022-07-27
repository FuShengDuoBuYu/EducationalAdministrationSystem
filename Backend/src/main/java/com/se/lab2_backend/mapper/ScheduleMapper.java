package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleMapper extends JpaRepository<Schedule, Long> {
}
