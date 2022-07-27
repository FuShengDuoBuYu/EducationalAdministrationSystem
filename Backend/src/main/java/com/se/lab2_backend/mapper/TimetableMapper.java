package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Classroom;
import com.se.lab2_backend.entity.Course;
import com.se.lab2_backend.entity.Timetable;
import com.se.lab2_backend.entity.TimetableKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TimetableMapper extends JpaRepository<Timetable, Long> {
    Timetable findByTimetableId(TimetableKey timetableKey);

    @Transactional
    Integer deleteAllByCourse(Course course);
}
