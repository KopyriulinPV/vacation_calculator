package com.example.vacation_calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationCalculationRequest {

    private BigDecimal avgSalary;

    private Integer vacationDays;

    private List<LocalDate> vacationDates;

}
