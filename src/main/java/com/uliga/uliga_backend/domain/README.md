## domain 패키지 구조

각 도메인들은 아래와 같은 패키지들을 포함하고 있습니다.

> Common, JoinTable, Token은 다른 구조를 가지고 있습니다.
> <br/>
> * Common 패키지는 엔티티들에서 공통적으로 사용되는 BaseTimeEntity, Date 클래스를 담고 있습니다.
> * JoinTable 패키지는 다대다 관계 해소를 위한 엔티티를 담고 있습니다.
> * Token 패키지에는 토큰 관련 dto, 예외 클래스가 포함되어있습니다.

* api
  * 컨트롤러 클래스를 포함하고 있는 패키지입니다.
* application
  * 서비스 클래스르를 포함하고 있는 클래스있습니다.
* dto
  * dto 클래스와 NativeQ라는 패키지를 포함하고 있습니다.
  * NativeQ에는 @Query 어노테이션을 사용한 조회와 MyBatis 조회 쿼리시 사용되는 dto 클래스들을 포함하고 있습니다.
* exception
  * 각 도메인 관련 예외와 그에 따른 핸들러 패키지를 포함하고 있습니다.
* mapper
  * mybatis mapper를 위한 패키지입니다.
* model
  * 각 도메인 엔티티 클래스를 포함하고 있는 패키지입니다.
* repository
  * jpa repository를 위한 패키지입니다.