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
