# 201812_1st
### DDD에 대한 내용을 바탕으로 Mayak 리팩토링  
[repo](https://github.com/ddingcham/MayakAgain)  

## 20181204
1. ENTITY 리팩토링  

### 기록
1. AGGREGATE 정리  
https://github.com/ddingcham/MayakAgain/wiki/ENTITY-%EB%B0%8F-AGGREGATE-%EC%A0%95%EB%A6%AC  

2. 정리된 내용을 바탕으로 테스트 코드 작성  
  * 처음 시도 실패함  
    > 일관성이 우선적으로 수반되지 않으면 테스트 코드 작성하는 데 애먹게 됨  
    > 고려해야 될 대상이 넓어짐  
    > 일관성부터 확보한 후에 진행  
  
    [ENTITY에 대한 Convention](https://github.com/ddingcham/MayakAgain/wiki/Convention---ENTITY)
    
  * 무슨 테스트부터 통과하게 해야할 지  
    * Entity 에 대한 Convention 적용 후  
      > 당연히 테스트들이 부러짐  
      
    * 가장자리 테스트 코드부터 통과시켜보기  
      > 테스트하기 쉬운 애들부터 하라했음  
      
  * Entity 클래스들의 equals()  
    equals() 에서는 유일성과 추적성을 위해서 id 값만 비교  
    대신 같은 속성을 지닌 Entity 인지 여부 비교를 위한 method 추가  
    > 왜냐면 id는 Repository를 통해서 우리가 신경쓰지 않도록 해야함  
    > 복잡도를 감소시키고, 비즈니스 로직에만 집중해야 하니까  
    ```
    Menu some_menu;
    store.addMenu(some_menu);
    // repository에 영속성 전이
    // 나중에 언젠가 .....
    store.hasMenu(some_menu);
    // store.hasMenu() 내부에서는 id가 아닌 특정 속성 같은지 여부를 비교하도록 함
    // 이를 위해 menu라는 Entitiy가 해당 인터페이스를 지원해줘야 함
    ```

  * AGGREGATE 정리한 거에 맞춰서 ENTITIY에 대한 테스트 코드 추가 재시도  
    * 일단 ENTITIY의 역할에 맞지 않은 테스트부터 격리시키자  
      (ex) DTO -> ENTITY 이런 애들  
      
#### 알아야 할 것만 알면 되도록 분류하는 과정이 어렵지
#### 분류하고 나니까 너무 쉽다.  
#### ORM의 위엄 : 알아야 할 것, 의식해야 할 것을 줄여준다.  
#### Spring 공부 하다가 느꼇던 비슷한 감정을 느낌

3. 외부 의존 없이 ENTITY에 대해서만 테스트 가능하게 리팩토링  

4. 오늘까지 한 내용
https://github.com/ddingcham/MayakAgain/pull/2

## 20181205
1. ENTITY 리팩토링  

### 기록 
* 소프트웨어 개발의 복잡성은 본질적인 특성에 기인한 것이지 비본질적인 특성에 기인한 것이 아니다. _ Fredy Brooks  
#### 조영호님 = GOD  

  * ENTITY의 관점으로 요구사항을 분석해보려고 하니깐, 내가 봐야될 것들이 분명해지는 것을 느낌  
    > ENTITY 및 AGGREGATE 정리 - 새로운 Reservation 생성 정리하다가  
    
  * 처음에 ENTITY라는 개념 없이 요구사항을 분석하려고 할 때는 고려해야될 것들이 마구잡이로 나왔었는데  
    ```
    뭐라고 딱 말로 표현할 수 는 없지만, 
    AGGREGATE (root, boundary, invariant) 개념을 통해 분석해야 될 범위가 지정되니까  
    기능(새로운 Reservation 생성)에 필요한 작업들을 각 ENTITY들의 역할별로 나누는 게 수월해지는 느낌을 받았음
    ```
    > 근데 아닐 수도 있음, 두번 째로 하는 거라서 그런 거 일 수도 있음  
    > 그치만 주어진 문제를 어떻게 추상화 하느냐에 따라 느껴지는 복잡도가 줄어드는 건 맞을 거임 // 왜냐면 밑에  
    
  * 새로 분석을 하니까 기존의 코드에서 고칠 점들이 많이 보임  
    > DDD 맛을 보고, 추상화를 위한 도구들이 업데이트되서 그런 거 같음  
    > 왜 테스트가 어려웠는 지, 왜 테스트하기 쉬운 부분만 분리하기 어려웠는지  
    > 느꼈으면 된거 아닌가?
