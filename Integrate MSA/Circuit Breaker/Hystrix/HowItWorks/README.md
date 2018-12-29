# 들어가기 전에 ...
```
spring-hystrix 예제
http://bcho.tistory.com/1250?category=431297  

Circuit breaker 패턴은 개인적인 생각에서는 MSA에서는 거의 필수적으로 적용해야 하는 패턴이라고 생각을 하지만
Hystrix를 이용하면 Command를 일일이 작성해야 하고, 이로 인해서 코드 복잡도가 올라갈 수 있다. 
이를 간소화 하기 위해서 Spring 오픈소스에 이 Hystrix를 잘 추상화 해놓은 기능이 있는데, 
그 부분 구현에 대해서는 다음글을 통해서 살펴보도록 한다.

출처: http://bcho.tistory.com/1250?category=431297 [조대협의 블로그]  
```
```
https://www.baeldung.com/rx-java
// Introduction to RxJava
http://reactivex.io/documentation/ko/observable.html
// ReactiveX - Observable
https://medium.com/@benlesh/hot-vs-cold-observables-f8094ed53339
// Hot vs Cold Observables
```


# [Hystrix wiki - How It Works](https://github.com/Netflix/Hystrix/wiki/How-it-Works#CircuitBreaker)  

```
How Does Hystrix Accomplish Its Goals?
Wrapping all calls to external systems // dependencies
HystrixCommand / HystrixObservableCommand
```
#### 우리 코드에 침투시켜 감싸는 거 가능 // 감싸고 싶은 대상은 다 감쌀 수 있는듯.

## Flow Chart  
![](https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/hystrix-command-flow-chart.png)  

### 1. Construct a **HystrixCommand** or **HystrixObservableCommand** Object  
  > to represent the request you are making to the dependency.  
  
* **HystrixCommand**  
  > if the dependency is expected to return a single reponse.  
    >> 단일 응답 반환에 대한 의존  
  
* **HystrixObservableCommand**  
  > if the dependency is expected to return an Observable that emits reponses.  
    >> 응답을 반환하는 Observable에 대한 의존  
    
### 2. Execute the Command  
> ultimately every HystrixCommand is **backed by an Observable implementation**  
> ,even those commands that are intended to return single, simple values.  

* **HystrixCommand** // implements HystrixExecutable<R>  
  * execute() // **Synchronous**  
    > **blocks**, then returns the single response received from the dependency.  
    > throws an exception -> case of an error  
    
    > K value = command.execute();  
      >> invoke -> queue().get()
    
  * queue()  
    > returns a **Future** with which you can obtain the single response from the dependency.  
    
    > Future\<K\> futureValue = command.queue();  
      >> invoke -> toObservable().toBlocking().toFuture()
  
* **HystrixCommand** & **HystrixObservableCommand**  
  * observe() // Hot Observable  
    > subscribes to the **Observable** that represents the response(s) from the dependency.  
    > returns an Observable that replicates that source Observable.  
    
    > Observable\<K\> hotObservableValue = command.observe();  
  
  * toObservable() // Cold Observable  
    > returns an Observable that, when you subscribe to it,  
    > will execute the Hystrix command and emit its reponses.  
    
    > Observable\<K\> coldObservable = command.toObservable();  
    
### 3. Is the Response Cached?  
If request caching is enabled for this command  
&& if the reponse to the request is available in the cache  
#### this **cached reponse** will be immediately returned in the form of an **Observable**.  

### 4. Is the Circuit Open?  
* If the circuit is open (or "tripped")  
  * then Hystrix will not execute the command  
  * but will route the flow to "(8)Get the Fallback".  

* If the circuit is closed  
  * then the flow proceeds to (5) to check  
  * if there is capacity available to run the command.  
  
### 5. Is the Thread-Pool/Queue/Semaphore Full?  
> Thread-Pool/Queue/Semaphore is associated with the command.  

* If the thread-pool and queue (or semaphore) are full  
  > then Hystrix will not execute the command  
  > but will immediately route the flow to "(8)Get the Fallback".
  
* [Semaphore](https://wikidocs.net/22304)  
  // rock 같은 느낌이네..  
  ```
  여러 개의 프로그램 엄밀히 말하면 쓰레드에서 접근을 하여 임의로 바꿀 수 있는데, 
  이것을 잠깐 또는 어떤 특정 조건에 의해서 막아주는 역할을 한다. 
  이것이 세마포어이다.
  ```
  
### 6. HystrixObservableCommand.construct() or HystrixCommand.run()  
* Purpose  
  > Hystrix invokes the request to the dependency by means of the method  
  
  * HystrixCommand.run()  
    > returns a single response or throws an exception.  
  * HystrixObservableCommand.construct()  
    > returns an **Observable** that emits the response(s)  
    > or sends an **onError** notification.  
    
* The thread will throw a **TimeoutException**  
  * **run()** or construct() method exceeds the **command's timeout value**  
  * Hystrix routes the response through "8. Get the Fallback"  
    > if that method(run(), construct()) does not cancel/interrupt.  
    
  #### There is no way to force the latent thread to stop work  
    > The best Hystrix can do on the JVM is to throw it an InterruptedException.  
  * If the work wrapped by Hystrix does not respect InterruptedExceptions.  
    > the thread in the Hystrix thread pool will contiue its work,  
    #### though the client already received a TimeoutException.  
    
#### Most Java HTTP client libraries do not interpret InterruptedExceptions.  
#### Make sure to correctly configure connection and read/write timeouts on the HTTP clients.  

* If the command -> throw any exceptions (X) && returned a response (O)  
  * Hystrix returned response after Hystrix performs some  
    * logging  
    * metrics reporting  
    
  * In the case of run()  
    > Hystrix  
      >> returns an **Observable** that emits the single response.  
      >> makes an **onCompleted** notification.  
      
  * In the case of construct()  
    > Hystrix returns the same Observable returned by construct\(\)  
      >> // construct\(\) -> or sends an **onError** notification.  
      
### 7. Calculate Circuit Health  
* Hystrix reports  
  * successes  
  * failures  
  * rejections  
  * timeouts  
  
#### Hystrix uses these stats to determine when the circuit should **"trip"**
* when the circuit should "trip"  
  * at which point it short-circuits  
    > any subsequent requests  
      >> until a recovery period elapses  
  * upon which it closes the circuit again  
    > after first checking certain health checks.

### 8. Get the Fallback  
#### Hystrix tried to revert to your fallback whenever a command execution fails  
* when an exception is thrown by **construct()**, **run()**  
* when the command is short-circuited  
  > because the circuit is open.  
* when the command's thread pool and queue or semaphore are capacity.  
* when the command has exceeded its timeout length.  

#### Provide a generic response without any network dependency.  
* from an in-memory cache  
* by means of other static logic  

#### You should do so by means of **another Command** // extends AbstractCommand<R>  
  > If you must use a network call in the **fallback**  

#### Fallback Implementation 
* In the case of a HystrixCommand
  [HystrixCommand.getFallback()](http://netflix.github.io/Hystrix/javadoc/com/netflix/hystrix/HystrixCommand.html#getFallback--)  
  > DEFAULT BEHAVIOR: It throws UnsupportedOperationException.

* In the case of a HystrixObservableCommand  
  [HystrixObservableCommand.resumeWithFallback()](http://netflix.github.io/Hystrix/javadoc/com/netflix/hystrix/HystrixObservableCommand.html#resumeWithFallback--)  
  > DEFAULT BEHAVIOR: It throws UnsupportedOperationException.

#### It is a poor practice to implement a fallback implementation that can fail.  
#### 마지막 Command의 Fallback은 항상 정상 동작할 수 있게 구현

#### 실패하거나 FallBack이 존재하지 않는 경우에 대한 커맨드 실행 방식별 케이스  
* execute()  
  > throws an exception.  
    >> // 내부적으로 invoke queue().get()  
    
* queue()  
  > successfully returns a **Future**  
    >> will throw Exception when invoke Future.get() // Future is 실패한 Future
    
* observe(), toObservable()    
  > returns an Observable that, when you subscribe to it,  
  
  * observe()  
    > will immediately terminate by calling the subscriber's onError method.  
    
  * toObservable()  
    > same observe() but not immediately
    
### Return the Successful Response  
* Flow  
  ![](https://github.com/Netflix/Hystrix/wiki/images/hystrix-return-flow-640.png)  
  
* Cases - Executes Command  
  * execute()
    > obtains a Future in the same manner as does .queue()  
    > and then calls get() on this Future to obtain the single value emitted by the Observable  
    
  * queue()
    > converts the Observable into a BlockingObservable  
    > so that it can be converted into a Future, then returns this Future  
    
  * observe()
    > subscribes to the Observable immediately and begins the flow that executes the command;  
    > returns an Observable that, when you subscribe to it, replays the emissions and notifications  
    
  * toObservable()
    > returns the Observable unchanged;  
    > you must subscribe to it in order to actually begin the flow that leads to the execution of the command  

    
    
    
    
