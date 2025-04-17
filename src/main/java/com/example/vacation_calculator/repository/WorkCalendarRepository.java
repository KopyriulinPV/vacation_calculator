package com.example.vacation_calculator.repository;

import com.example.vacation_calculator.model.WorkCalendarDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface WorkCalendarRepository extends JpaRepository<WorkCalendarDate, LocalDate> {

    @Query("SELECT w FROM WorkCalendarDate w WHERE w.status = 'WORKDAY' AND w.date BETWEEN :start AND :end")
    List<WorkCalendarDate> findWorkdaysBetween(LocalDate start, LocalDate end);

    List<WorkCalendarDate> findByDateIn(List<LocalDate> dates);

}
