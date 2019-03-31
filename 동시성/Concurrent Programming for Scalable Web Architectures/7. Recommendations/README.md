# Concurrent Programming for Scalable Web Architectures  
# 7. Recommendations

## Intro  

#### Understanding the implications of different server architectures helps to provide insights for expected performance 

* core of a web application - application logic  
  * specific code implemented by own developers  
  * web frameworks or application containers  
  
* Selecting a Web Server Architecture  
* Picking the Right Concurrency Concepts for Application Logic  
* Choosing a Storage Backend  
  
## 7.1 Selecting a Web Server Architecture  
> see also : chapter 4  
> I/O handling and connection concurrency for web servers  

* sufficient performance result can be achieved with various models  
* web servers are challenged with  
  * highly concurrency connections  
  * extream I/O parallelism, virtually not shared state    
  
* event-driven architectures using async I/O  
  * better scalability under load  
  * requiring less memory  
  * gaining better CPU utilization  
  
* thread-based servers  
  * web servers are used in general-purpose setups  
  * executing the application logic in the same server instance  
  * still a valid contender  
  
#### Implementing a custom webserver - choice of the right concepts if often constrainted by the platform and language  
> 가능하면 추천하는 정책    
* I/O multiplexing strategies (async/non-blocking)  
* cooperative scheduling  
* low-level frameworks  
  * often implementing the basic building blocks of a network server  
  * allowing the developer to focus on protocol-dependant and application-spcific features  

## 7.2 Picking the Right Concurrency Concepts for Application Logic  

#### Different concurrency requirements that a web application might imply  
* **reducing the latency of request handling**  
  > via parallelizing independent operations of a request  
* providing notification mechanisms and supporting server-initiated events  
  > via **coordinating different pending requests**  
* some applications require to **share highly fluctuating and variable state** inside the application servers  
  > interactive and collaborative applications cannot rely on state  
  > state : solely isolated in the storage backend  
  
### Reduction of latency of a request can mainly be achieved by parallelizing independent operations  
* ex] decreasing the overall latency   
  * **parallel database queries**  
  * **subdivided computations**  
* ratio of CPU-bound and I/O-bound operations  
  > important property for accelerating requests  
* CPU-bound operations can only be decreased by **using more threads on more cores**  
* **I/O bound operation**  
  > When additional threads are used for heavy I/O parallelism  
  * Using too many threads for I/O-bound operations results in decreasing performance and scalability issues  
    * **context switching overhead**  
    * **memory consumption**  
    
### Concurrency Programming models  
* Thread-based models  
  * futures / promises : notion that helps  
    * dispatching independent tasks  
    * **eventually collecting their results**  
    * **without the need for complex synchronization**  
* Actors models  
  * Used for I/O-bound and CPU-bound operations  
  * **efficiency** depends on the **underlying implementation**  
* Event-driven Architectures  
  * primarily I/O-bound tasks  
  * but **unusable for computationally heavy operations**  
    > these operations can be outsourced to external components  
    
### Coordinating requests and synchronizing shared application state are related  
#### Allowing to partially isolate some application state and groups of users for interactivity  
* ex1] browser multiplayer game session with dozens of players  
  > **conceptual instance with a single shared application state**  
* ex2] real-time word processor (web-based collaborative software application)  
  running editing sessions with several users  
  
#### session affinity  
* running application instance can be transparently mapped to a designated application server  
  > no need to share states between application servers  
  > each session is bound to a single server  
  > server can host multiple application sessions  
* server can entirely isolate application state for this session  
* server can easily coordinate pending requests for notifications  

* appropriate concepts  
  * event-driven architectures  
  * actor-based systems  
  * STM  
  * Avoiding a usage of locks - risk of deadlocks or race conditions  
  
#### binding specific state to a certain server -> contrary to our shared-nothing design of application servers  

### Application state is global and cannot be divided into disjoint partitions  

#### ex] the instant messaging capabilities of a large social web application    
* **state must be either outsourced to a distributed backend component**  
  > requiring that any user might contact any other user  
  * working with all concurrency concepts  
  * redis  
    * distributed key/value store  
    * supporting pub/sub  

* **application servers mutually sharing global state**  
  * only applicable with using a distributed STM  
  * only applicable with using distributed actor system  
      
#### However, Dependencies betwwen application servers : these are contrary to our preferred shared-nothing style      

### Choosing strategies    

#### Thread-based : valid approach when none of the aforementioned concurrency requirements are actually needed  
* simple squence of operations  
  > providing a very intuitive model for developers  
* if] need shared state inside the application logic  
  > **usage of STM should be favored in order to prevent locking issues**    
   
#### Dedicated backend storages : If there is no imperative need to share state inside the application logic     

#### Actor model  
* requiring the developer to embrace the notions of asynchrony and distribution  
* via akka : complement the actor model    
  * STM  
  * additional fault tolerance  
* strong foundation for distributed application architectures  
* **should be considered in very biginning of large scale**  

#### Single-Threaded Event-Driven Architectures  
* 1. Application logic of a web application primarily integrating the services provided by other components  
* 2. Not Requiring computationally expensive operations  
* **With a shared-nothing style, a sustaining scale-out can be accomplished by constantly adding new instances**  

#### Actor model and Single-threaded event-driven architecture share several principles  
* asynchrony  
* queuing based on messages(events, isolated state)  

#### SEDA architecture  
* combining with either one of these concepts for application logic resembles the original SEDA  
* SEDA는 단일 서버에 대한 관점으로 보여주지만, 우리는 주로 분산 시스템에 SEDA 개념을 적용  

## 7.3 Choosing a Storage Backend  
* Assuming that a single database instance does not fit our requirements  
  * to scale out  
  * to provide high availability  

* trade-off between strong consistency and eventual consistency  
* (non)relational db  

### actual data model : essential to focus on the intrinsic data model of the application first    
* identifying certain groups of data items  
  > **representing independent domains**  
* keeping in mind future requirements  
  > **changing the data model**  
#### agile development style with frequent changes to the data model should be taken into account  
 
### scalability requirements : Second Consideration  
* different pre-environments - **dimensions of scale**    
  * Scaling out a blog application using additional database instances  
  * Growing a large e-commerce site that already starts with multiple data centers  

* considerations   
  * Will the application be challenged by vastly parallel access to the persistence layer  
  * Is it the fast-growing amount of data to store  
  * **ratio of read and write operations**  
    > as well as the impact of search operations  

### consistency requirements : Third Consideration    
* **strong consistency** is generally preferred by all parties involved  
* **relaxed(eventual) consistency** via impact of stale data in different menifestations  
  > helping to define parts of the data model    
* in practice, strong consistency is rarely possible 

### Choosing [database systems](https://github.com/ddingcham/TIL/blob/master/%EB%8F%99%EC%8B%9C%EC%84%B1/Concurrent%20Programming%20for%20Scalable%20Web%20Architectures/6.%20Concurrent%20and%20Scalable%20Storage%20Backends/6.3%20Types%20of%20Distributed%20Database%20Systems.md)
* RDBMS  
* Document stores  
* key/value stores  
* Graph database  
* wide column stores  

### polyglot persistence  
> see also : [Martin Fowler : PolyglotPersistence](https://martinfowler.com/bliki/PolyglotPersistence.html)    
**If no database type fits all needs, different domains of the data models may be separated .via different database**  

#### ex] e-commerce site via polyglot persistence  
* RDBMS  
  * **Stock of Products**  
    * indispensable asset  
    * customers expect consistent information on product availabilities  
* Wide Column store  
  > data warehouse storage - mainly for analytics  
  * **Customer data**  
    * rarely change  
  * **Previous Orders**  
    * do not change  
  * **Tracked user actions** : in order to calculate recommendations    
    * can be stored asynchronously - decoupled analytical processing at a later time    
  * **content of a shopping cart** : **eventual consistency** in order to accept every single write operation (addToCart)    
    * very different : customers expect every action to succeed,  
      **no matter if a node in the data center fails**  
* Document store  
  > **relaxed consistency**,  
  > **dealing with the consequences of conflicting operations via. merging different versions of a chart**  
  
  * **product ratings**  
  * **comments**  
  
