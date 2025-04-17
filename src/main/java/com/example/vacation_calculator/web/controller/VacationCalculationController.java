package com.example.vacation_calculator.web.controller;

import com.example.vacation_calculator.dto.VacationCalculationRequest;
import com.example.vacation_calculator.dto.VacationCalculationResponse;
import com.example.vacation_calculator.service.VacationCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculate")
@RequiredArgsConstructor
public class VacationCalculationController {

    private final VacationCalculationService vacationService;

    @GetMapping
    public VacationCalculationResponse calculate(@RequestBody VacationCalculationRequest request) {
        return vacationService.calculate(request);
    }

}
