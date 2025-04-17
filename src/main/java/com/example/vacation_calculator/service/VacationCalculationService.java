package com.example.vacation_calculator.service;

import com.example.vacation_calculator.dto.VacationCalculationRequest;
import com.example.vacation_calculator.dto.VacationCalculationResponse;
import com.example.vacation_calculator.exception.NotEnoughDataException;
import com.example.vacation_calculator.model.DayType;
import com.example.vacation_calculator.model.WorkCalendarDate;
import com.example.vacation_calculator.repository.WorkCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationCalculationService {

    private final WorkCalendarRepository calendarRepository;

    public VacationCalculationResponse calculate(VacationCalculationRequest request) {

        LocalDate now = LocalDate.now();
        LocalDate yearAgo = now.minusMonths(12).plusDays(1);

        List<WorkCalendarDate> workdays =
                calendarRepository.findWorkdaysBetween(yearAgo, now);
        int totalWorkDays = workdays.size();
        if (totalWorkDays == 0) throw new NotEnoughDataException("Не было рабочих дней к этому моменту");

        BigDecimal oneDayCost = request.getAvgSalary()
                .multiply(BigDecimal.valueOf(12))
                .divide(BigDecimal.valueOf(totalWorkDays), 2, RoundingMode.HALF_UP);

        int payableDays = 0;

        if (request.getVacationDates() == null || request.getVacationDates().isEmpty()) {
            if (request.getVacationDays() == null || request.getVacationDays() < 1)
                throw new NotEnoughDataException("Не указаны дни отпуска");
            payableDays = request.getVacationDays();
        } else {
            List<WorkCalendarDate> datesStatuses =
                    calendarRepository.findByDateIn(request.getVacationDates());

            payableDays = (int) datesStatuses.stream()
                    .filter(wcd -> wcd.getStatus() == DayType.WORKDAY)
                    .count();
        }
        BigDecimal vacationPay = oneDayCost.multiply(BigDecimal.valueOf(payableDays)).setScale(2, RoundingMode.HALF_UP);
        return new VacationCalculationResponse(vacationPay, payableDays);
    }

}
