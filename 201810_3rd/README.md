# 201810_3rd
## [사다리 게임 TDD 연습](https://github.com/ddingcham/ladder)
* 기본 TDD cycle 연습 **Red-Green-Refactor**
* 객체지향 생활체조
* [알리바바 코딩 가이드](https://github.com/ddingcham/Alibaba-Java-Coding-Guidelines)

## 20181023
### 계획
* 입력 / 출력 제외한 기능 테스트 코드 작성
* 테스트 통과
* 리팩토링 **프로그래밍 요구사항을 모두 지킬 필요는 없음**

### 기록
* [직전 commit을 지우려고 했을 때 이슈](https://code.i-harness.com/ko-kr/q/e267e)

> `git reset --hard HEAD^1`

> 워킹 디렉토리의 작업 내용도 사라짐 -> 다른 방법 찾기

* [Stream API를 사용할 때 예외 처리](https://kihoonkim.github.io/2017/09/09/java/noexception-in-stream-operations/)

* [TDD 학습 시 커밋 단위 참고할 링크](https://github.com/javajigi/minesweeper-ruby/issues/5)

* 기능에 대한 정의를 **명확하게** 내린 후 테스트 케이스를 작성하자...

> 쓸 데 없는 테스트 케이스와 프로덕션 코드를 작성하고 있었음 

> ex: LEFT, STAY, RIGHT => 있거나, 없거나

> 이론으로는 알지만, 실제로는 적용이 어려운 이유를 느꼈음

> 1) 어플리케이션 전체적인 맥락에 대한 요구사항 정의하기

> 2) 구현에 필요한 요구사항을 정의하기

> 3) 구현에 필요한 요구사항을 기반으로 테스트 케이스 작성하기

* 때로는 인스턴스 변수를 2개 갖는 클래스도 존재할 수 있음
> 출처 : thoughtworks anthology

```
class Name {
Surname family;
GivenNames given;
}
class Surname {
String family;
}
class GivenNames {
List<String> names;
}
```

* 컨벤션 관련 코딩 가이드, 해당 가이드에 대한 정적 분석기를 활용하면서 느낀 점

## 20181024
### 계획
* 입력 / 출력 제외한 기능 테스트 코드 작성

> 1) 어플리케이션 전체적인 맥락에 대한 요구사항 정의하기

> 2) 구현에 필요한 요구사항을 정의하기

> 3) 구현에 필요한 요구사항을 기반으로 테스트 케이스 작성하기

* 테스트 통과
* 리팩토링 **프로그래밍 요구사항을 모두 지킬 필요는 없음**

### 기록
* Test 한 Production 코드들 임시로 테스트 하기 위한 공간 활용 ....temp package에 작성

> 요구사항에 대한 것만 작성한다는 강박관념에 빠질 필요는 없을 것 같음

* [Junit 으로 테스트 코드 작성시 (@Rule public TestName, @Before)](https://stackoverflow.com/questions/1548462/junit-before-only-for-some-test-methods)

> 내 상황에 알맞은 방법은 아닐수도 있지만, Junit의 위대함을 다시 느꼈음

* 확실히 완벽하진 않더라도, 요구사항을 명확하게 정리해 가면서 진행하니까 고민하는 시간이 줄어드는 것을 느낌

> 당장 정리할 수 있는 것과 TDD의 효과를 통해 정의할 수 있는 것에 대한 감각이 필요할 것 같음

* Test Doubles 관련 [출처](https://adamcod.es/2014/05/15/test-doubles-mock-vs-stub.html)

```
When To Use Test Doubles

There are two schools of thought on Test Doubles: Classical, and Mockist. Put simply, people who use the Classical style only use Test Doubles when there is a compelling reason to do so, such as external dependencies, whilst a Mockist would mock everything, all the time.

The advantage of using the Classical style is that you don't have to worry about internal behaviour of your objects, which is what OOP is all about. When you change an object and it's collaborators your tests will still pass, but this comes at the sacrifice of speed and isolation.

The advantage of using the Mockist style is that your tests are isolated and can run much faster, but at the sacrifice of coupling your tests to the internal behaviour of your objects and their collaborators.

At this point, no one way is the right way. Like all decisions in software development, it's up to you to use your judgement and select whatever method feels most appropriate for the given situation. Hopefully this post will have given you a solid foundation in the tools available to you, allowing you to make an informed decision next time you find yourself needing a Collaborator in a test.
```

> 테스트 코드 리팩토링 시 적용

## 20181025
### 계획
* 입력 / 출력 을 포함한 전체 프로그램 완성
* 완성 과정에서 필요한 테스트 코드 작성 및 통과
* 시간이 가능하면 리팩토링

### 기록
* 첫 번째 연습을 마무리 하고 느낀 점

> 생각보다 어렵다....

> Out -> In 방식으로 접근했는지 안했는지가 티가 났다.

> 전체적인 맥락의 요구사항 정리를 대충 했더니, 아무 맥락 없이 테스트 순서가 이루어 졌다.

> 쉽고, 당연할 것만 같은 In 들 부터 하다보니, 기계적으로 객체를 정의했고, 관계가 복잡해 지는 결과를 낳았다.

> 그나마 객체지향 생활체조랑 코드 가이드라인이 없었다면, 더 복잡해졌을 것이다. 

## 20181026
### 계획
* 요구사항 분석 다시해보기
* 입력 / 출력 프로그램 리팩토링
* 도메인 모델 다시 생각해보기
* 각각의 리팩토링 과정에서의 테스트 코드 재작성

### 기록
* 다시 고민해봐야 할 주제 _ 내가 TDD 연습을 하는 이유 (중요하지만, 관건은 아님)

> [테스트 상태인 Private 메소드를 Public메소드로 변환시 Unit Testing은 어떻게 해야하나?](https://www.slipp.net/questions/253)

> 리팩토링 시의 고려사항 또는 후의 코드 상태 평가 기준으로 생각해볼 필요가 있을 듯

> 특히 [dongkuk 님의 답변](https://www.slipp.net/questions/253#answer-980)
이 인상 깊게 느껴진다.

> [관련 예시-자바지기님의 시도 기록](https://www.slipp.net/wiki/pages/viewpage.action?pageId=6160426)

> 어떤 상황에서 이런 판단을 내리신 건지 생각해볼 것 // 재사용 관점에서 분리, 가독성 관점에서 분리

> 각각의 분리 케이스에 따라 각각 다른 목적을 갖고 각각 다른 결과가 나올 것 같다.

> 목적에 대한 것은 지금 내가 어떤 요구사항에 얽힌 작업을 하고 있는 지 // 미래에 요구사항이 어떻게 변할지

> 내가 작업한 것이 미래에 어떤 사이드 이팩트를 발생 시킬지

> 덧붙여서 [양완수님의 okkycon 2018 repository](https://github.com/yangwansu/okkycon2018)
 가 주는 메시지를 파악하는 것도 다시 시도하게 되었다.
 
## 20181027
### 계획
* 현재 코드 상황에서 개선해야 될 사항들 찾기

### 기록
* 개선 사항 찾으면서 기준이 필요하다고 생각 되어서 리팩토링 관련 책을 다시 읽기로 하고, 정리중 .....

* 점검

> 명확하게 세분화한 요구사항들을 모두 테스트하고 있는가? -> No

>> 테스트 코드를 기반으로 프로덕션 작성 하지 않았음

> 테스트 없이 작성한 프로덕션이 테스트 하기가 쉬운가?

>> 모든 코드가 테스트하기 쉬워 보이지 않았음  
>> 몇 몇 코드들이 테스트 코드를 다시 작성하려고 하거나,  
>> 파라미터에 대해서 어디까지 알게 하여야 할 지 결정하기가 어려웠음  
>> 느낌 가는 대로 한 부분들이 그런 것 같음

## 20181028
### 계획
* 개선 사항에 대한 기준을 위해 리팩토링 책의 Bad Smell 부분 정리
	[정리 내용 링크](https://github.com/ddingcham/TIL/blob/master/201810_3rd/Bad_Smell.md)

### 기록

* 개선 사항 1 LadderConnection

```
/*
 * boolean connected => 가로 선의 각 칸의 연결 여부를 래핑
 * 굳이 할 필요 없을 수도..
 * boolean primitive 자체가 두 가지 케이스만을 보장하니까 ...
 * 외부에서 호출하는 generate() 와 isConnected(), connected(), unConnected()
 * ,next() 4가지 모두 래핑에 대한 목적과 역할이 희미해진 상태라고 보임 
 * 굳이 LadderConnection으로 역할을 나누는 것 보다
 * LadderRow가 그 역할을 수행 하는 것이 더 알기 쉽다고 판단
 */
 
 ```
