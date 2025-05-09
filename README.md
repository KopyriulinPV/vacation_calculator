Vacation Calculator (Калькулятор отпускных)

1. Быстрый старт и запуск приложения.

Для локального запуска потребуется установленный Docker.

cd docker

docker-compose up -d

Это поднимет сервис с базой данных PostgreSQL, который будет доступен на порту 5432.

Сервис автоматически инициализирует базу с нужной структурой и данными календаря.

2. Запустите Spring Boot приложение.

Выполните VacationCalculatorApplication.java.

Приложение будет запущено на localhost:8080 (по умолчанию).

3. К приложению написаны unit тесты vacation_calculator\src\test\java\com\example\vacation_calculator
\VacationCalculatorApplicationTests.java

4. Стек используемых технологий: Java 11, Spring Boot 2.7+ (Web, Data JPA), PostgreSQL 12.3, Lombok,

Maven, Docker Compose, Mockito.

5. Описание логики работы приложения.

Приложение предоставляет API для рассчёта суммы отпускных по средней зарплате за последние 12 месяцев. 

Для корректного рассчёта в базе инициализируется производственный календарь РФ на 2024-2025 годы с 

отмеченными рабочими, выходными и праздничными днями.

API

GET /calculacte

Параметры запроса:

avgSalary – средняя зарплата за последние 12 месяцев (BigDecimal, обязательна)

vacationDays – количество дней отпуска (Integer, по умолчанию если не указаны даты)

vacationDates – список дат предполагаемого отпуска (необязательно; если есть, расчет идет по этим дням)

Примеры запросов:

http://localhost:8080/calculate

   {

   "avgSalary": 100000,

   "vacationDays": 14

   }

   {

   "avgSalary": 100000,

   "vacationDates": ["2025-05-01","2025-04-02","2025-07-03"]

   }

Принципы расчёта.

Базовый вариант.

Если передано только количество отпускных дней (vacationDays), приложение вычисляет отпускные на основе средней 
зарплаты за последние 12 месяцев и реального количества рабочих дней в этом периоде (по календарю из базы).

Продвинутый вариант.

Если указан список дат (vacationDates), приложение анализирует эти даты по производственному календарю. 
Отпускные считаются только за те дни, которые попадают на рабочие дни. Праздничные и выходные в расчет не включаются.


