# Hystrix Overview - [Netflix/Hystrix wiki home](https://github.com/Netflix/Hystrix/wiki) 정리  

## What Is Hystrix?  
**By isolating points of access**, Hystrix does ...  
* stopping cascading failures across them  
* providing fallback options  
#### all of which improve your system's overall resiliency.
> 시스템의 장애 내성 강화  

## What Is Hystrix For?  
* 네트워크 기반 액세스 종속성으로부터 오는 [대기 시간/장애]에 대한 **보호 및 제어**  
* Stop cascading failures in a complex distributed system  
* 실패 시점을 앞당겨서 복구 시점 또한 빠르게  
  > 이게 운영 이슈만은 아닐 듯  
* Fallback and gracefully degrade when possible  
* Enable near real-time monitoring, alerting, and operational control  
  ```
  ...
  Fall-back 메시징인데, 
  Circuit breaker에서 Service B가 정상적인 응답을 할 수 없을 때, 
  Circuit breaker가 룰에 따라서 다른 메세지를 리턴하게 하는 방법이다. 
  
  예를 들어 Service A가 상품 목록을 화면에 뿌려주는 서비스이고, 
  Service B가 사용자에 대해서 머신러닝을 이용하여 상품을 추천해주는 서비스라고 했을때, 
  
  Service B가 장애가 나면 상품 추천을 해줄 수 없다. 
  이때 상품 진열자 (MD)등이 미리 추천 상품 목록을 설정해놓고, 
  Service B가 장애가 난 경우 Circuit breaker에서 이 목록을 리턴해주게 하면 
  머신러닝 알고리즘 기반의 상품 추천보다는 정확도는 낮아지지만 최소한 시스템이 장애가 나는 것을 방지 할 수 있고 
  다소 낮은 확률로라도 상품을 추천하여 꾸준하게 구매를 유도할 수 있다.
  출처: http://bcho.tistory.com/1247?category=431297 [조대협의 블로그]
  ```

## What Problem Does Hystrix Solve?  
```
If the host application is not isolated from these external failures, 
it risks being taken down with them.

99.99^30 = 99.7% uptime
0.3% of 1 billion requests = 3,000,000 failures
2+ hours downtime/month even if all dependencies have excellent uptime.
```
#### if you do not engineer the whole system for resilience.  

### cascading failures  
#### 분리된 각각의 서비스(?),셀(?)들이 SPOF(Single Point Of Failure)처럼 될 가능성이 있음  
```
Every point in an application that reaches out over the network 
or into a client library that might result in network requests is a source of potential failure.
```

#### Ex] all request threads block in second  
* When one of many backend systems becomes latent  
  > it can block the entire user request  
  
  ![](https://github.com/Netflix/Hystrix/wiki/images/soa-2-640.png)  
  
* With high volume traffic a single backend dependency  
  > becoming latent can cause all resources  
  > to become saturated in seconds on **all servers**  
  
  ![](https://github.com/Netflix/Hystrix/wiki/images/soa-3-640.png)  


### Black Box // network access is performed through a third-party client
```
where implementation details are hidden and can change at any time, 
and network or resource configurations are different for each client library 
and often difficult to monitor and change.
```

### Release It! -> Release It????  
#### Network connections fail or degrade
* Service and servers fail or become slow  
* New libraries or service deployments change behavior or performance characteristics.

#### Needs to be isolated and managed
  > single failing dependency can't take down an entire application or system
  
## What Design Principles Underlie Hystrix?  
* Preventing any single dependency from using up all container (such as Tomcat) user threads.  
  > 이유나 자료  
  
* Shedding load and failing fast instead of queueing.  
  > 이유나 자료  
  
* Providing fallbacks wherever feasible to protect users from failure.  
  > 이유나 자료  
  
* Using isolation techniques to limit the impact of any one dependency.  
  such as bulkhead, swimlane, circuit breaker patterns  
  > 이유나 자료  
  
* Optimizing for time-to-discovery through near real-time metrics, monitoring, and alerting.  
  > 이유나 자료  
  
* Optimizing for time-to-recoveryn by means of low latency propagation of  
  ```
  configuration changes and support for dynamic property changes in most aspects of Hystrix,
  which allows you to make real-time operational modifications with low latency feedback loops.
  ```  
  
* Protecting against failures in the entire dependency client execution,  
  not just in the traffic.  
  
## How Does Hystrix Accomplish Its Goals?  

### Wrapping all calls to external systems // dependencies  
* **HystrixCommand** [Test](https://github.com/Netflix/Hystrix/blob/master/hystrix-core/src/test/java/com/netflix/hystrix/HystrixCommandTest.java)  
* **HystrixObservableCommand** [Test](https://github.com/Netflix/Hystrix/blob/master/hystrix-core/src/test/java/com/netflix/hystrix/HystrixObservableCommandTest.java)  
  #### TestCode에 사용법이 잘 나와있음 : typically executes within a separate thread  
* [command pattern](http://en.wikipedia.org/wiki/Command_pattern)  
    
### Timing-out calls that take longer than thresholds you define.  
There is a default, but for most dependencies you custom-set these timeouts by means of  
**Properties** [Test](https://github.com/Netflix/Hystrix/blob/master/hystrix-core/src/test/java/com/netflix/hystrix/HystrixCommandPropertiesTest.java)  
so that they are slightly higher than the measured 99.5th percentile performance for each dependency.  
  ```
  99.99^30 = 99.7% uptime
  0.3% of 1 billion requests = 3,000,000 failures
  ```  

### Maintaining a small thread-pool (or semaphore) for each dependency;  
if it becomes full,  
requests destined for that dependency will be immediately rejected instead of queued up.  
#### 감당 할 수 있는 만큼만 정상 처리.  

### 처리 케이스 측정 // Spring에선 기본 DashBoard도 지원  
* successes  
* failures (exceptions thrown by client)  
* timeouts  
* thread rejections  
  > Maintaining a small thread-pool (or semaphore) for each dependency 관련?  
  
### Tripping a circuit-breaker 
* to stop all requests  
  * to a particular service for a period of time  
  * either manually or automatically  
  * if the error percentatge for the service passes a threshold.  
  
### Performing fallback logic // 처리 케이스별(+short-circuits) 다른 fallback   
  ```
  ...
  Fall-back 메시징인데, 
  Circuit breaker에서 Service B가 정상적인 응답을 할 수 없을 때, 
  Circuit breaker가 룰에 따라서 다른 메세지를 리턴하게 하는 방법이다. 
  
  출처: http://bcho.tistory.com/1247?category=431297 [조대협의 블로그]
  ```

### Monitoring metrics and configuration changes in near real-time.
[**HystrixDynamicProperties**](https://github.com/Netflix/Hystrix/blob/master/hystrix-core/src/main/java/com/netflix/hystrix/strategy/properties/HystrixDynamicProperties.java)  
```
 * A hystrix plugin (SPI) for resolving dynamic configuration properties. This
 * SPI allows for varying configuration sources.
 * 
 * The HystrixPlugin singleton will load only one implementation of this SPI
 * throught the {@link ServiceLoader} mechanism.
 *
 * @author agentgt
```

## Scenario  
![](https://github.com/Netflix/Hystrix/wiki/images/soa-4-isolation-640.png)  
#### [Dependency별로 큰 네모를 통해 서킷브레이커 적용] // HystrixCommand, HystrixObservableCommand  
