## global 패키지 구조

* common
  * 사용하는 어노테이션, 상수들을 포함하고 있는 패키지 입니다.
* component
  * 사용하는 컴포넌트를 위한 패키지입니다. 
* config
  * 설정 클래스들을 위한 패키지입니다.
* error
  * exception, handler, response 패키지를 포함하고 있습니다.
    * exception 은 공통적으로 발생할 수 있는 예외들을 포함하고 있습니다.
    * handler은 공통적으로 발생할 수 잇는 예외들을 handling하는 handler를 포함하고 있습니다.
    * response는 예외 발생시 리턴하는 응답 클래스를포함하고 있습니다.
* filter
  * spring security에서 사용되는 필터를 포함하고 있습니다.
* jwt
  * jwt 관련 클래스들을 포함하고 있습니다.
* oauth2
  * oauth2 관련 클래스들을 포함하고 있습니다.
* util
  * util 클래스들을 포함하고 있습니다.