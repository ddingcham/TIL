# Concurrent Programming for Scalable Web Architectures  
# 5. Concurrency Concepts for Applications and Business Logic  

## 5.1 Overview  

#### Decoupling from low-level connection handling or request prasing duties  
* **An application server provides a reactive behavior that generates responses as a result to incoming request**  
* **An application server recives invocations from upstream web servers in form of high-level messages**  
* **An application server executes business logic mapped to request URIs**  

#### the flow of execution for a request includes interactions with different components of the architectures  
> also computational operation as part of the application logic  
* different components  
  * databases  
  * backend services  

### Activities  
#### CPU-bound activities  
* primarily consume CPU time during execution  
  > generally, computationally heavy algorithms operating on in-memory data  
* input validation  
* template rendering  
* on-the-fly encoding/decoding of content  

#### I/O-bound activities  
* mainly limited by I/O resources  
  * network I/O  
  * file I/O  
* often take place when tasks operates on external data that is not part of its own memory  

#### A flow of execution for request handling  
  ![](http://berb.github.io/diploma-thesis/community/resources/req_para.svg)  
  
* first queries a cache  
* then **dispatched two independent database queries**  
  * left side  
    > execution is strictly sequential  
  * right side  
    > the independent operations are parallelized in order to improve latency results  
* and finally accesses the cache again  

#### Web application typically triggers operations of both types  
* content-rich web applications (ex] blogs, wikis)  
  * primarily rely on database operations  
    > and to some extend on template rendering  
* other applications are mainly CPU-bound  
  * provide computational services or web services for media file conversions  
  
* **Low latencies of responses are favored in both cases**  
  > for good UX  
  
#### In order to minimize latencies, the parallelization of independent operations should be considered  
* ![](http://berb.github.io/diploma-thesis/community/resources/req_coord.svg)  
* first node represents the arrival of the request  
* last node the compiled response  
* nodes in between represent operations such as database operations or computation functions  
* **scatter and gather**  
  > Splitting up the flow of control results in parallel operations, that must be synchronized at a later time.  
  * The browser of user A continuously sends ** requests for notifications by using long polling**  
  * Once user B posts a new message, the web application coordinates the notification and responds  
    > **pending requests, effectively notifying user A**  
    
#### necessary for collaborative, interactive and web real-time applications  


## 5.7 Summary  

#### The application logic of modern, large-scale web applications is inherently concurrent  
> requests are entirely isolated and easy to execute independently  

#### The concept of threads and locks is based on mutable state shared by multiple flows of execution  
* **choosing the right locking granularity** : too difficult/error-prone    
  * danger of race conditions : danger of deadlocks  
  * degenerated sequential execution / unmanageable nondeterminism  
  
#### STM : transactional operations for concurrent code  
* transactions isolate operations on mutable shared state and are lock-free and composable  
  > must not contain any side effects (no I/O operations)   
  > 중단-롤백/재실행 을 위해  
* hiding complexity of transaction handling in a software layer  

#### Actors are separate, single-threaded entities that communicate via immutable,async,guaranteed messaging  
* isolating mutability of state  
* message-based, distributed computing  

#### Event-driven architectures evict the danger of deadlocks via single-threaded event loop  
* sequentially executing each event with its associated event handler(callback)  
* IoC  
* **utilizing a single CPU core very well**  
  > 어플리케이션 요구사항들이 대부분 I/O bound 에 속하고, 복잡한 연산이 필요없는 경우  
  
