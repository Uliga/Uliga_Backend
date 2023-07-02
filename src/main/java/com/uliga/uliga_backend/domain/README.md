# 2023.07 리팩터링 시작

## 리팩터링을 하는 이유

급하게 개발을 하다보니 SOLID 원칙을 거의 지키지못하고 개발하게되었습니다.
많은 도메인이 있지만, 대부분 로직이 accountBookController, accountBookService에 종속적으로 쓰여져있습니다.

가계부에 수입 추가, 조회 같은 로직이 수행될때, 모두 accountBookService 내부에서 해당 로직을 수행하는 구조인데, 이러면 accountBookService가 너무 많은 책임을 가지게 됩니다.

SOLID 원칙을 지키도록 코드 리팩터링 진행중입니다.