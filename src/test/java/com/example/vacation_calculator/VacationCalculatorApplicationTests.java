package com.example.vacation_calculator;
import com.example.vacation_calculator.dto.VacationCalculationRequest;
import com.example.vacation_calculator.dto.VacationCalculationResponse;
import com.example.vacation_calculator.model.DayType;
import com.example.vacation_calculator.model.WorkCalendarDate;
import com.example.vacation_calculator.repository.WorkCalendarRepository;
import com.example.vacation_calculator.service.VacationCalculationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class VacationCalculatorApplicationTests {

	@Test
	@DisplayName("Тестирование расчета суммы отпускных при предоставлении в запросе средней зарплаты" +
			" и количества отпускных дней.")
	void testCalculateByVacationDaysOnly() {

		WorkCalendarRepository calendarRepository = Mockito.mock(WorkCalendarRepository.class);
		VacationCalculationService service = new VacationCalculationService(calendarRepository);

		List<WorkCalendarDate> workdays = IntStream.rangeClosed(1, 247)
				.mapToObj(i -> new WorkCalendarDate(LocalDate.of(2024, 1, 1).plusDays(i),
						DayType.WORKDAY))
				.collect(Collectors.toList());
		when(calendarRepository.findWorkdaysBetween(any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(workdays);

		BigDecimal avgSalary = new BigDecimal("60000");
		int vacationDays = 14;

		VacationCalculationRequest request = new VacationCalculationRequest(avgSalary, vacationDays, null);

		VacationCalculationResponse response = service.calculate(request);

		BigDecimal expectedOneDayCost = avgSalary.multiply(BigDecimal.valueOf(12))
				.divide(BigDecimal.valueOf(247), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal expectedVacationPay = expectedOneDayCost.multiply(BigDecimal.valueOf(vacationDays))
				.setScale(2, BigDecimal.ROUND_HALF_UP);

		assertThat(response.getVacationPay()).isEqualByComparingTo(expectedVacationPay);
	}

	@DisplayName("Тестирование расчета суммы отпускных дней при предоставлении в запросе средней зарплаты" +
			" и списка дат отпуска.")
	@Test
	void testCalculateByVacationDatesList() {

		WorkCalendarRepository calendarRepository = Mockito.mock(WorkCalendarRepository.class);
		VacationCalculationService service = new VacationCalculationService(calendarRepository);

		BigDecimal avgSalary = new BigDecimal("60000");
		List<LocalDate> vacationDates = List.of(
				LocalDate.of(2024, 6, 10),  // WORKDAY
				LocalDate.of(2024, 6, 11),  // HOLIDAY
				LocalDate.of(2024, 6, 12),  // WORKDAY
				LocalDate.of(2024, 6, 13),  // WEEKEND
				LocalDate.of(2024, 6, 14)   // WORKDAY
		);

		List<WorkCalendarDate> yearWorkdays = IntStream.rangeClosed(1, 247)
				.mapToObj(i -> new WorkCalendarDate(LocalDate.of(2024, 1, 1).plusDays(i), DayType.WORKDAY))
				.collect(Collectors.toList());
		when(calendarRepository.findWorkdaysBetween(any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(yearWorkdays);

		List<WorkCalendarDate> statusList = List.of(
				new WorkCalendarDate(vacationDates.get(0), DayType.WORKDAY),
				new WorkCalendarDate(vacationDates.get(1), DayType.HOLIDAY),
				new WorkCalendarDate(vacationDates.get(2), DayType.WORKDAY),
				new WorkCalendarDate(vacationDates.get(3), DayType.WEEKEND),
				new WorkCalendarDate(vacationDates.get(4), DayType.WORKDAY)
		);
		when(calendarRepository.findByDateIn(vacationDates)).thenReturn(statusList);

		VacationCalculationRequest request = new VacationCalculationRequest(avgSalary, null, vacationDates);

		VacationCalculationResponse response = service.calculate(request);

		BigDecimal expectedOneDayCost = avgSalary.multiply(BigDecimal.valueOf(12))
				.divide(BigDecimal.valueOf(247), 2, BigDecimal.ROUND_HALF_UP);
		int expectedPayableDays = 3;
		BigDecimal expectedVacationPay = expectedOneDayCost.multiply(BigDecimal.valueOf(expectedPayableDays))
				.setScale(2, BigDecimal.ROUND_HALF_UP);

		assertThat(response.getPayableDays()).isEqualTo(expectedPayableDays);
		assertThat(response.getVacationPay()).isEqualByComparingTo(expectedVacationPay);
	}
}
