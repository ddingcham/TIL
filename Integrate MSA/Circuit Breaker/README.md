# Circuit Breaker

## Martin Fowler  

### [Microservices](https://martinfowler.com/articles/microservices.html)
* Design for failure  
  #### The circuit breaker and production ready code
```
[Circuit Breaker] appears in [Release It!] alongside other patterns such as Bulkhead and Timeout.

Implemented together
: building communicating applications.
```  

### [Circuit Breaker](https://martinfowler.com/bliki/CircuitBreaker.html)  
* cascading failures across multiple systems  
```
One of the big differences between in-memory calls and remote calls is
that remote calls can fail, or hang without a response until some timeout limit is reached.

What's worse if you have many callers on a unresponsive supplier, 
then you can run out of critical resources leading to cascading failures across multiple systems.
```  

* basic idea
```
The basic idea behind the circuit breaker is very simple.  
You wrap a protected function call in a circuit breaker object, which monitors for failures.
```  

### [Release It](https://www.amazon.com/gp/product/B00A32NXZO?ie=UTF8&tag=martinfowlerc-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=B00A32NXZO)  
#### Design and Deploy Production-Ready Software  
Review  
  ```
  Agile development emphasizes delivering production-ready code every iteration. 
  This book finally lays out exactly what this really means for critical systems today. 
  You have a winner here. 
  --Tom Poppendieck, Poppendieck LLC.
  ```
  
## 조대협의 블로그  
[Circuit breaker 패턴을 이용한 장애에 강한 MSA 서비스 구현하기 #1 - Circuit breaker와 넷플릭스 Hystrix](http://bcho.tistory.com/1247?category=431297)  

### MSA에서 서비스간 장애 전파  
// cascading failures across multiple systems
```
하나의 컴포넌트가 느려지거나 장애가 나면 
그 장애가난 컴포넌트를 호출하는 종속된 컴포넌트까지 장애가 전파되는 특성
```

![출처 - 조대협의 블로그](https://t1.daumcdn.net/cfile/tistory/99E6754C5AC39FAA08)  
[출처 - 조대협의 블로그](http://bcho.tistory.com/1247?category=431297)  

### Circuit breaker 패턴  

* Service A와 Service B 사이에 Circuit breaker  
  > Circuit breaker를 통해서 Service B 호출  
  * 정상 케이스  
    ![출처 - 조대협의 블로그](https://t1.daumcdn.net/cfile/tistory/99427C475AC39FAA08)  
    [출처 - 조대협의 블로그](http://bcho.tistory.com/1247?category=431297)  
    
  * Service B 장애 케이스
    ![출처 - 조대협의 블로그](https://t1.daumcdn.net/cfile/tistory/993FD73B5AC39FAA17)  
    [출처 - 조대협의 블로그](http://bcho.tistory.com/1247?category=431297)  
    
    
## 생태계  
* Java  
  * [Netflix 오픈소스 Hystrix - Circuit breaker 구현체](https://github.com/Netflix/Hystrix)  
    > wiki 설명이 잘 되어있음  
  * [Spring Circuit breaker - Hystrix Clients](https://cloud.spring.io/spring-cloud-netflix/multi/multi__circuit_breaker_hystrix_clients.html)

* [Microservices architecture, implementation and monitoring with Spring Cloud, Netflix OSS and Dockers](https://medium.com/@madhupathy/simplified-microservices-building-with-spring-cloud-netflix-oss-eureka-zuul-hystrix-ribbon-2faa9046d054)  
  #### Introduction  
  ```
  Lets get a simple introduction to technologies involved:
  
  * Spring Config Server → babysitter for configurations of all APIs
  * Spring Cloud Netflix Eureka → directory for all APIs
  * Spring Cloud Netflix Ribbon → network cops for all communications
  * Spring Cloud Netflix Hystrix → re-router/circuit breaker
  * Spring Cloud Netflix Hystrix Dashboard → cctv cameras for all rest calls
  * Spring Cloud Netflix Zuul → gatekeeper for APIs
  * Dockers → big smiling fish thingy, for building, shipping & running apps
  ```
