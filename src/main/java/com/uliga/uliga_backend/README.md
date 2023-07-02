# 코드구조

## domain
* controller, service, repository 같은 도메인과 연관 있는 코드들을 담고 있는 패키지입니다.

## global
* 애플리케이션 전역에서 사용되는 상수, util 클래스들을 포함하고 있습니다.
* 다양한 configuration 클래스들, jwt, oauth2 관련 로직들을 확인하실 수 있습니다.

## infra
* 백엔드 서버 안내 페이지를 리턴하는 MainController, 서버 프로필을 확인하는 WebRestController 코드를 확인하실 수 있습니다.