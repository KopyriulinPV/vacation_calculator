package com.example.vacation_calculator.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "work_calendar")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkCalendarDate {

    @Id
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private DayType status;

}
