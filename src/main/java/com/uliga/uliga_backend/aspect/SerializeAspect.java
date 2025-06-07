package com.uliga.uliga_backend.aspect;

import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.common.annotation.Serialize;
import com.uliga.uliga_backend.common.annotation.SerializePaginated;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Serialize(dto = X.class) 어노테이션이 붙은 컨트롤러(또는 메서드)를 가로채서,
 *                Mono<Entity> → Mono<Dto>, Flux<Entity> → Flux<Dto> 로 자동 변환하는
 *                Aspect
 */
@Aspect
@Component
@RequiredArgsConstructor
public class SerializeAspect implements Ordered {

  private final ObjectMapper objectMapper;

  /**
   * @Serialize(dto = SomeDto.class) 어노테이션이 붙은 메서드 실행 전/후 AOP 포인트컷
   */
  @Around("@annotation(serialize)")
  public Object serializeReturn(ProceedingJoinPoint pjp, Serialize serialize) throws Throwable {
    // 1) 먼저 컨트롤러 메서드를 실행해서 반환값(예: Mono<Entity> 또는 Flux<Entity>)을 얻는다
    Object returnValue = pjp.proceed();

    // 2) 어노테이션에서 지정한 DTO 클래스
    Class<?> dtoClass = serialize.dto();

    // 3) 반환값이 Mono일 때
    if (returnValue instanceof Mono<?>) {
      @SuppressWarnings("unchecked")
      Mono<Object> mono = (Mono<Object>) returnValue;
      // Mono<Entity> → Mono<Dto>
      return mono.map(entity -> objectMapper.convertValue(entity, dtoClass));
    }

    // 4) 반환값이 Flux일 때
    if (returnValue instanceof Flux<?>) {
      @SuppressWarnings("unchecked")
      Flux<Object> flux = (Flux<Object>) returnValue;
      // Flux<Entity> → Flux<Dto>
      return flux.map(entity -> objectMapper.convertValue(entity, dtoClass));
    }

    // 5) 그 외(동기 반환 등)일 경우 직접 변환
    return objectMapper.convertValue(returnValue, dtoClass);
  }

  /**
   * @SerializePaginated(dto = SomeDto.class) 애노테이션이 붙은 메서드를 가로채서,
   *                         PaginatedDto<Entity> → PaginatedDto<Dto> 로 변환합니다.
   */
  @Around("@annotation(serializePaginated)")
  public Object serializePaginatedReturn(ProceedingJoinPoint pjp, SerializePaginated serializePaginated)
      throws Throwable {
    Object returnValue = pjp.proceed();
    Class<?> dtoClass = serializePaginated.dto();

    if (returnValue instanceof PaginatedDto<?>) {
      @SuppressWarnings("unchecked")
      PaginatedDto<Object> original = (PaginatedDto<Object>) returnValue;

      // 1) 원본 엔티티 리스트 (nodes) 가져오기
      List<Object> originalNodes = original.getNodes();
      // 2) 각 엔티티를 DTO로 변환
      List<Object> convertedNodes = originalNodes.stream()
          .map(entity -> objectMapper.convertValue(entity, dtoClass))
          .collect(Collectors.toList());

      // 3) 새로운 PaginatedDto<Dto> 객체 생성하여 반환
      return new PaginatedDto<>(convertedNodes, original.getTotalCount());
    }

    // PaginatedDto가 아닌 경우 그대로 반환
    return returnValue;
  }

  /**
   * Order를 낮게 지정해서, @ResponseBody 직렬화보다 먼저 AOP가 적용되도록 합니다.
   */
  @Override
  public int getOrder() {
    // 기본 HandlerAdapter/ResponseBodyAdvice보다 먼저 실행되게 낮은 숫자를 줍니다.
    return Ordered.LOWEST_PRECEDENCE - 100;
  }
}