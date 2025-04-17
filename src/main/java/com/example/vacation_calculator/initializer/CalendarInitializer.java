package com.example.vacation_calculator.initializer;

import com.example.vacation_calculator.model.DayType;
import com.example.vacation_calculator.model.WorkCalendarDate;
import com.example.vacation_calculator.repository.WorkCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CalendarInitializer implements CommandLineRunner {

    private final WorkCalendarRepository calendarRepository;

    @Override
    public void run(String... args) {

        Set<LocalDate> holidays = new HashSet<>(Set.of(
                // 2024
                LocalDate.of(2024, 1, 1),  LocalDate.of(2024, 1, 2),  LocalDate.of(2024, 1, 3),
                LocalDate.of(2024, 1, 4),  LocalDate.of(2024, 1, 5),  LocalDate.of(2024, 1, 6),
                LocalDate.of(2024, 1, 7),  LocalDate.of(2024, 1, 8),
                LocalDate.of(2024, 2, 23),
                LocalDate.of(2024, 3, 8),
                LocalDate.of(2024, 4, 29),
                LocalDate.of(2024, 4, 30),
                LocalDate.of(2024, 5, 1),  LocalDate.of(2024, 5, 9),
                LocalDate.of(2024, 5, 10),
                LocalDate.of(2024, 6, 12),
                LocalDate.of(2024, 11, 4),
                LocalDate.of(2024, 12, 30), LocalDate.of(2024, 12, 31),
                // 2025
                LocalDate.of(2025, 1, 1),  LocalDate.of(2025, 1, 2),  LocalDate.of(2025, 1, 3),
                LocalDate.of(2025, 1, 4),  LocalDate.of(2025, 1, 5),  LocalDate.of(2025, 1, 6),
                LocalDate.of(2025, 1, 7), LocalDate.of(2025, 1, 8),
                LocalDate.of(2025, 2, 23),
                LocalDate.of(2025, 3, 8),
                LocalDate.of(2025, 5, 1),  LocalDate.of(2025, 5, 2),
                LocalDate.of(2025, 5, 8),
                LocalDate.of(2025, 5, 9),
                LocalDate.of(2025, 6, 12), LocalDate.of(2025, 6, 13),
                LocalDate.of(2025, 11, 3), LocalDate.of(2025, 11, 4),
                LocalDate.of(2025, 12, 31)
        ));


        Set<LocalDate> workingSaturdays = new HashSet<>(Set.of(
                // 2024
                LocalDate.of(2024, 4, 27),
                LocalDate.of(2024, 11, 2),
                LocalDate.of(2024, 12, 28),
                // 2025
                LocalDate.of(2025, 11, 1)
        ));


        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);

        List<WorkCalendarDate> days = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {

            if (holidays.contains(d)) {
                days.add(new WorkCalendarDate(d, DayType.HOLIDAY));
            } else if (workingSaturdays.contains(d)) {
                days.add(new WorkCalendarDate(d, DayType.WORKDAY));
            } else if (d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY) {
                days.add(new WorkCalendarDate(d, DayType.WEEKEND));
            } else {
                days.add(new WorkCalendarDate(d, DayType.WORKDAY));
            }
        }
        calendarRepository.saveAll(days);

    }
}