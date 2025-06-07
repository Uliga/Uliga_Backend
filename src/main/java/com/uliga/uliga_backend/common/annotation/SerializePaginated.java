package com.uliga.uliga_backend.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PaginatedDto<엔티티>를 PaginatedDto<DTO>로 변환할 때 사용합니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SerializePaginated {
  /**
   * 내부 리스트(List<T>)의 엔티티를 변환할 DTO 클래스 타입을 지정합니다.
   */
  Class<?> dto();
}