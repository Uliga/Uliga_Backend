package com.uliga.uliga_backend.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;

/**
 * KST(Asia/Seoul) 기준으로 날짜/시간을 처리하는 유틸리티 클래스
 */
public class KstDateUtils {
  private KstDateUtils() {
  }

  /** KST(Asia/Seoul) 타임존 상수 */
  private static final ZoneId KST = ZoneId.of("Asia/Seoul");

  /**
   * 하루(day)를 밀리초 단위로 변환하여 반환합니다. (1일 = 24*60*60*1000 ms)
   *
   * @param day 일 수
   * @return 밀리초 (long)
   */
  public static long dayToMilliSeconds(int day) {
    // 1일 = 24시간 * 60분 * 60초 * 1000밀리초
    return day * 24L * 60 * 60 * 1000;
  }

  /**
   * 임의의 Date(UTC 기준 Date)를 받아 해당 순간의 KST ZonedDateTime을 반환합니다.
   *
   * @param date UTC 기준 Date (또는 타임스탬프)
   * @return KST ZonedDateTime
   */
  private static ZonedDateTime toSeoulZoned(Date date) {
    Instant inst = date.toInstant();
    return inst.atZone(KST);
  }

  /**
   * 주어진 Date(또는 타임스탬프)로부터 KST 기준 연도-주차(ISO 주년 기준)를 "YYYY-WW" 형식 문자열로 반환합니다.
   *
   * @param date UTC 기준 Date (또는 타임스탬프)
   * @return "연도-주차" 예: "2025-23"
   */
  public static String getWeekString(Date date) {
    ZonedDateTime kst = toSeoulZoned(date);
    WeekFields wf = WeekFields.ISO; // ISO 규칙: 한 해의 첫 번째 주를 포함.
    int weekYear = kst.get(wf.weekBasedYear());
    int weekNum = kst.get(wf.weekOfWeekBasedYear());
    return String.format("%d-%02d", weekYear, weekNum);
  }

  /**
   * 주어진 Date로부터 KST 기준 연도를 반환합니다.
   *
   * @param date UTC 기준 Date
   * @return 연도 (int)
   */
  public static String getYearString(Date date) {
    ZonedDateTime kst = toSeoulZoned(date);
    return Integer.toString(kst.getYear());
  }

  /**
   * 주어진 Date로부터 KST 기준 연도-월("YYYY-MM") 형식 문자열을 반환합니다. 월은 1~12,
   * 두 자리로 맞추기 위해 padStart(2, '0')와 동일하게 포맷팅합니다.
   *
   * @param date UTC 기준 Date
   * @return "연도-월" 예: "2025-06"
   */
  public static String getMonthString(Date date) {
    ZonedDateTime kst = toSeoulZoned(date);
    int year = kst.getYear();
    int month = kst.getMonthValue(); // 1 ~ 12, pad 0처리 필요
    return String.format("%d-%02d", year, month);
  }

  /**
   * UTC 기준 Date를 받아, 해당 일(KST 기준)의 시작 시각(00:00:00.000 KST)을 UTC로 변환하여 Date로
   * 반환합니다.
   *
   * @param date UTC 기준 Date
   * @return KST 기준 해당 일자 시작 시간의 UTC Date
   */
  public static Date getUtcStartOfKstDay(Date date) {
    ZonedDateTime kst = toSeoulZoned(date);
    LocalDate localDate = kst.toLocalDate();
    ZonedDateTime startOfDayKst = localDate.atStartOfDay(KST); // KST 자정 →

    Instant utcInstant = startOfDayKst.toInstant(); // UTC로 변환
    return Date.from(utcInstant);
  }

  /**
   * UTC 기준 Date를 받아, 해당 일(KST 기준)의 끝 시각(23:59:59.999999999 KST)을 UTC로 변환하여 Date로
   * 반환합니다.
   *
   * @param date UTC 기준 Date
   * @return KST 기준 해당 일자 종료 시간의 UTC Date
   */
  public static Date getUtcEndOfKstDay(Date date) {
    ZonedDateTime kst = toSeoulZoned(date);
    LocalDate localDate = kst.toLocalDate();
    // KST 자정 + 하루 - 1 나노초 = 23:59:59.999999999
    ZonedDateTime endOfDayKst = localDate.plusDays(1)
        .atStartOfDay(KST)
        .minusNanos(1);
    Instant utcInstant = endOfDayKst.toInstant();
    return Date.from(utcInstant);
  }

  /**
   * UTC 기준 Date를 받아, 해당 주(KST 기준) 시작 시각(Sunday 00:00:00.000 KST)을 UTC로 변환하여 Date로
   * 반환합니다.
   * date-fns의 startOfWeek 기본 동작(주 시작을 Sunday로 간주)을 따라 구현
   *
   * @param date UTC 기준 Date
   * @return KST 기준 해당 주 시작 시간의 UTC Date
   */
  public static Date getUtcStartOfKstWeek(Date date) {
    ZonedDateTime kst = toSeoulZoned(date);
    // KST 기준, 주의 첫 날은 Sunday로 가정 (date-fns 기본값)
    ZonedDateTime sunday = kst.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY))
        .toLocalDate()
        .atStartOfDay(KST);
    Instant utcInstant = sunday.toInstant();
    return Date.from(utcInstant);
  }

  /**
   * UTC 기준 Date를 받아, 해당 주(KST 기준) 종료 시각(Saturday 23:59:59.999999999 KST)을 UTC로
   * 변환하여 Date로 반환합니다.
   *
   * @param date UTC 기준 Date
   * @return KST 기준 해당 주 종료 시간의 UTC Date
   */
  public static Date getUtcEndOfKstWeek(Date date) {
    ZonedDateTime kst = toSeoulZoned(date);
    // Saturday = Sunday 기준 + 6일
    ZonedDateTime saturday = kst.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SATURDAY))
        .toLocalDate()
        .atTime(LocalTime.MAX) // 23:59:59.999999999
        .atZone(KST);
    Instant utcInstant = saturday.toInstant();
    return Date.from(utcInstant);
  }

  /**
   * UTC 기준 Date를 받아, 해당 달(KST 기준) 시작 시각(1일 00:00:00.000 KST)을 UTC로 변환하여 Date로
   * 반환합니다.
   *
   * @param date UTC 기준 Date
   * @return KST 기준 해당 달 시작 시간의 UTC Date
   */
  public static Date getUtcStartOfKstMonth(Date date) {
    ZonedDateTime kst = toSeoulZoned(date);
    LocalDate firstDayOfMonth = kst.toLocalDate().withDayOfMonth(1);
    ZonedDateTime startOfMonthKst = firstDayOfMonth.atStartOfDay(KST);
    Instant utcInstant = startOfMonthKst.toInstant();
    return Date.from(utcInstant);
  }

  /**
   * UTC 기준 Date를 받아, 해당 달(KST 기준) 종료 시각(마지막 날 23:59:59.999999999 KST)을 UTC로 변환하여
   * Date로 반환합니다.
   *
   * @param date UTC 기준 Date
   * @return KST 기준 해당 달 종료 시간의 UTC Date
   */
  public static Date getUtcEndOfKstMonth(Date date) {
    ZonedDateTime kst = toSeoulZoned(date);
    LocalDate lastDayOfMonth = kst.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());
    ZonedDateTime endOfMonthKst = lastDayOfMonth.atTime(LocalTime.MAX).atZone(KST);
    Instant utcInstant = endOfMonthKst.toInstant();
    return Date.from(utcInstant);
  }

  /**
   * UTC 기준 Date를 받아, 지정된 포맷(formatStr)과 KST 타임존 옵션으로 문자열로 포맷팅합니다.
   *
   * @param date      UTC 기준 Date
   * @param formatStr Java DateTimeFormatter 패턴 (예: "yyyy-MM-dd HH:mm:ss")
   * @return KST 기준으로 포맷팅된 문자열
   */
  public static String formatUtcDateToKst(Date date, String formatStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
    ZonedDateTime kst = toSeoulZoned(date);
    return kst.format(formatter);
  }

  /**
   * 주어진 Date에서 KST 기준으로 하루 전 날짜(Date) 객체를 반환합니다.
   * (KST 기준으로 날짜 부분에서 -1일)
   *
   * @param date UTC 기준 Date; null인 경우 현재 시점을 사용
   * @return KST 기준 하루 전 순간을 UTC Date로 변환한 값
   */
  public static Date getYesterday(Date date) {
    ZonedDateTime kst;
    if (date == null) {
      kst = Instant.now().atZone(ZoneOffset.UTC).withZoneSameInstant(KST); // 현재 UTC→KST
    } else {
      kst = toSeoulZoned(date);
    }
    ZonedDateTime yesterdayKst = kst.toLocalDate().minusDays(1).atStartOfDay(KST) // KST 기준 날짜를 -1일 후 자정
        .withHour(kst.getHour()) // JS date-fns.addDays와 유사하게 시간은 그대로 유지
        .withMinute(kst.getMinute())
        .withSecond(kst.getSecond())
        .withNano(kst.getNano());
    Instant resultInst = yesterdayKst.toInstant(); // 다시 UTC Instant로 변환
    return Date.from(resultInst);
  }
}