# 201810_4th
## [로또 게임 TDD 연습](https://github.com/ddingcham/lotto)
* 기본 TDD cycle 연습 **Red-Green-Refactor**
* 객체지향 생활체조
* [알리바바 코딩 가이드](https://github.com/ddingcham/Alibaba-Java-Coding-Guidelines)

## 20181029
### 계획
* step 1 요구사항 정리
* step 1 진행 

### 기록
* 브랜치 관리하면서 진행하기로 함

> [참고](https://www.slipp.net/questions/9)

* 이전에 [사다리게임]() 구현 하면서 느낀 점을 토대로 작업 제약사항을 추가해서 진행 하기로 함

>
Out - In 방식으로 접근하려 했더니 생각보다 전체 OutLine 잡는 게 어려워서  
꼭 필요하지만 작은 몇 개의 요구사항 구현을 먼저 진행하기로 함  
구현한 작은 요구사항들(In)을 토대로 Out을 수정 하고, 
전체 OutLine이 수정될 때마다 지속적으로 리팩토링 작업을 하기로 함  

* TDD 작업 시 작업 순서에 대해서 많은 경험을 하고 싶다. -> 나만의 노하우

## 20181030
### 계획
* step 1 에 대한 클라이언트를 TDD로 구현
* 클라이언트 TDD 진행과 리팩토링 진행

### 기록
* 클라이언트 코드 작성을 병행하면서 느낀 점

> "테스트 코드는 프로덕션의 최초 고객"  
> // OkkyCon Real TDD : 양완수님  
> 테스트 대상 파악이 힘들어서 도메인을 사용하게 될 클라이언트를  
> TDD 도중 작성하는 방식으로 진행했는데,  
> 미처 파악하지 못했던 요구사항과 리팩토링 대상이 보이기 시작함.....  
> 프로덕션을 호출하는 최초 고객(테스트 대상)이 생각이 안나면  
> 예상 고객(클라이언트) 작업도 병행하면 좋을 듯함.