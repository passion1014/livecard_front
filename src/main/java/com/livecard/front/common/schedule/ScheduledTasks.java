package com.livecard.front.common.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

//    private final StatisticService statisticService;
//    private final WeatherService weatherService;
//    private final MenuService menuService;
//    private final PigBreathAlarmService pigBreathAlarmService;

    // 공통 작업을 실행하는 메서드
    private void executeCommonTasks(String type) {
        /* STEP 01 산차 통계 생성 */
//        statisticService.setLitterStatistic(StatisticLitterDto.builder().date(LocalDate.now()).build());

        /* STEP 02 산자수 증가 통계 생성 */
//        statisticService.setPigletStatistic(StatisticPigletDto.builder().year(Year.now()).build());

        /* STEP 03 양돈 배치 현황 통계 생성 */
//        statisticService.setCageStatistic();

        /* STEP 04 신규 양돈 현황 통계 생성 */
//        statisticService.setPigRegistCnt();

        /* STEP 05 통계조회 > 양돈통계 */
//        statisticService.setPigSpecies(Year.now());

        /* STEP 06 양돈 평균 호흡수 집계 */
//        statisticService.setAvgPigBreath();

        /* STEP 07 분만알림 및 응급알림 실행 */
//        pigBreathAlarmService.setPigAlarmHistory();

        // 3시가 아닐때
//        if(!"3".equals(type)){
//            /* STEP 08 분만예정일 집계 */
//            statisticService.setFarrowing();
//        }else{
//            /* STEP 08 분만예정일 당일알림 */
//            pigBreathAlarmService.setFarrowingDaysAlarm();
//
//            /* STEP 09 분만예정일 지연알림 */
//            pigBreathAlarmService.setFarrowingDelayAlarm();
//
//            /* STEP 10 분만예정일 집계 */
//            statisticService.setFarrowing();
//
//        }

    }


    @Scheduled(cron = "0 0 0 * * *")
    public void executeTaskAt0AM() {
        executeCommonTasks("0");
        System.out.println("배치 작업 0시 실행: " + LocalDateTime.now());
    }

    // 매일 3시에 실행
    @Scheduled(cron = "0 0 3 * * *")
    public void executeTaskAt3AM() {
        executeCommonTasks("3");
        System.out.println("배치 작업 3시 실행: " + LocalDateTime.now());
    }

    // 매일 6시에 실행
    @Scheduled(cron = "0 0 6 * * *")
    public void executeTaskAt6AM() {
        executeCommonTasks("6");
        System.out.println("배치 작업 6시 실행: " + LocalDateTime.now());
    }

    // 매일 9시에 실행
    @Scheduled(cron = "0 0 9 * * *")
    public void executeTaskAt9AM() {
        executeCommonTasks("9");
        System.out.println("배치 작업 9시 실행: " + LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void executeTaskAt12PM() {
        executeCommonTasks("12");
        System.out.println("배치 작업 12시 실행: " + LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 15 * * *")
    public void executeTaskAt15PM() {
        executeCommonTasks("15");
        System.out.println("배치 작업 15시 실행: " + LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 18 * * *")
    public void executeTaskAt18PM() {
        executeCommonTasks("18");
        System.out.println("배치 작업 18시 실행: " + LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 21 * * *")
    public void executeTaskAt21PM() {
        executeCommonTasks("21");
        System.out.println("배치 작업 21시 실행: " + LocalDateTime.now());
    }

    /**
     * 정각 1초마다
     * 1. 날씨 갱신
     * 2. 메뉴 뱃지 갱신
     **/
    // 정각 1초마다
    @Scheduled(cron = "1 0 * * * *")
    public void executeTaskEveryDay() {
//        weatherService.setWeatherData();
//        menuService.setMenuBadge();
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void executeTaskEvery10Minutes() {

    }
}

