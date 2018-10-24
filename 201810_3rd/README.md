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