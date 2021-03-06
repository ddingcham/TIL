## 배우고, 느낀 점

### [1. "서비스 개발" 시 고려해야할 것]

#### 신규 서비스는 최대한 유저가 이해하기 쉽게, 심플하게 만들어야 한다.

 새로운 서비스는 사용자에게 낯설게 느껴진다. 기능이 복잡하고, 다양해질 수록 낯설음은 더 커진다.  
 낯설음으로 인해서 서비스에 대한 부정적인 인식이 생긴다면, 플랫폼 내의 기존 사용자까지 떠나는 상황이 발생한다.  
 따라서 초기에 오픈 시킬 대상의 범위도 중요해진다.  

 예를 들어, 화장품 추천 서비스를 새롭게 런칭하다면 "화장품 추천"에 대해 특화된,  
 우리만의 가장 중요한 가치(기능)만을 먼저 개발해서,  
 화장품에 대해 전문성 있는 사용자들에게 먼저 공개해서 가치를 증명하는 과정이 필요하다.  
 ```
 예를 들면..
 간편송금 많이 하잖아요..
 간편 송금은 말그대로 간편하게..빠르게
 A->B 돈을 보내는거에요..

 본질은 돈을 빠르게 전달하는것이라고 했을때..
 그것에 촛점이 맞는 기획..

 예를 들면 계좌번호를 카피해서 쉽게 입력한다거나..
 돈을 받을사람을 빨리 찾게 해준다거나
 이런건 송금을 쉽고 빠르게 할수 있는 촛점에 맞는 기획이에요
 ```

* 가치 증명 후 확장
기능과 제공 대상을 한정 시켜 서비스의 가치를 증명하고 나면, 서비스를 확장시켜야 한다. 확장의 대상은 기능, 사용자 두 관점이 있다.  

  * 기능 관점  
  가장 중요한 정보, 기능을 제공한 뒤 그 가치가 증명되고, 낯설음이 익숙해질 즈음
  피드백을 통해 수정되고, 추가된 기능들을 제공한다.  

  * 사용자 관점  
  해당 도메인에 대해 어느 정도 익숙한 사람들에게 새로운 서비스의 가치를 증명 받고나면,
  좀 더 넓은 범위의 사용자들에게도 서비스를 제공한다.

* 필요한 것 - 유연성  

  * **특정 기능 시스템** 뿐만 아니라 **전체적인 시스템 관점**에서도 기능을 추가/수정하는 것에 유연성을 필요로 한다.  

  * **전체 시스템과 특정 시스템 관점**에서 감당할 수 있는 요청이 너무 많거나 적지 않도록 조절하는 것에 유연성을 필요로 한다.  
  
  * **유연성을 위한 원칙**들에게도 유연성이 필요함  
    * 비용 관리 - 비용(시간, 인력)을 효율적으로 사용하자  
      * 중요하면서 확정된 것은 비용을 많이 투자해서 견고하게 개발  
      * 변할 가능성이 높거나(검증 부족), 중요하지 않다면? 상황을 고려해서 적절한 수준을 찾기  
        > 적절함의 기준은 아직 내 수준으로는 판단하기가 어렵다 ㅠ  
      
    * 때로는 견고함이 일반적으로 필요한 부분도 있음  
      > 이런 부분들은 이전 경험, 사례들을 반영하여 미리 견고하게 개발  
      
    * 때때로 견고함을 위해서는 ....
      * 사용하는 분야에 대한 깊은 이해가 필요하기도 함  
        > 깊은 이해는 결국 기본기에서 나온다.  


### [2. 분산 시스템에서 고려해야 할 것]
* 서비스 개발자(백엔드)로써 필요한 경험, 과제들 // 멘토님 조언  

  * 분산된 시스템에서의 세션 정보 관리  
    
  * 특정 기간에만 영속화가 필요한 데이터 관리  
    ex) 일정 기간에만 필요한 사용자 로그 기록

  * 정적인 자원 외에 캐싱에 적합한 대상들  
    > 잘못된 캐싱 대상은 오히려 성능에 부정적인 영향을 끼친다.  
  
  * I/O 이슈  
    ex) OAuth 적용 시 토큰 정보와 유저 정보를 꼭 같은 테이블에 관리해야 하나?  

  * Connection pool 이슈  
    ex) 실제 요청을 처리하기 위해 RestTemplate 사용할 때 발생할 수 있는 이슈  
    ex) DB Connection Pool 효율적으로 사용하려면 ?  
        > Spring Batch의 chunk, writer 부분을 가지고 경험해보기.  

  * 상황 별로 비동기 / 동기 어떤 것이 더 적합한 처리 방식일지  

  * 만약 시스템에 장애가 일어났을 때, 서비스가 중단되지 않으려면?  
    * 서버(어플리케이션, DB 등) 다중화  
    * 장애 내성, 지연 내성을 위해 Circuit Breaker 패턴 (Hystrix)  

### [3. 도구(기술)에 대한 생각]
* 같은 도구여도 어떻게 사용하느냐에 따라 다른 효과를 볼 수 있다.
  상황에 따라 적절한 플러그인, 기능을 적용해야 한다. 
  이 때, 적절함의 기준은 "비용 관리"와 유사하다.
  아무리 좋은 기능도 필요함 대비 도입하는 데 비용이 너무 크다면, 
  기능이 좀 부족하더라도 상황에 맞는 차선책을 찾는 게 우선

* 다양한 도구들이 현업에서 어떤 식으로 활용되는 지 생각해 볼 수 있었음
