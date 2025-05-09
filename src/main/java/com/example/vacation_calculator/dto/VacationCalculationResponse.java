package com.example.vacation_calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationCalculationResponse {

    private BigDecimal vacationPay;

    private Integer payableDays;

}
