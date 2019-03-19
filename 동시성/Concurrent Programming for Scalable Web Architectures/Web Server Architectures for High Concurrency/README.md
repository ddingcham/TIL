# Concurrent Programming for Scalable Web Architectures  
## 4. Web Server Architectures for High Concurrency  

### Introduction

#### closer look at concurrency when handling multiple connections inside a web server.  
> request handling is decoupled from application logic  

#### creation of dynamic content is not scoped here  
> 문제의 단순화를 위해 분할 시켰으니, 분할된 부분만 고려  

there is also no coordination required between two distinct requests  

#### 1) the challenges of concurrency for high-performance web servers 
> with **huge numbers of simultaneous connections**  

#### 2) how different server architectures handle parallelism  
* different programming models for concurrency  
* different programming models for I / O operations  

#### 3) compare the general concurrency models  
* thread-based-model  
* event-driven-model  

### Summary  

#### The challenge of scalability for web servers is characterized by intense concurreny of HTTP connections  

#### The massive parallelism of I/O-bound operations is thus the primary issue.  

#### various server architectures that provide different combinations  
* multi-process servers  
* multi-threaded servers  
* event-driven servers  
* combined approaches - such as SEDA  

#### synchronous, blocking I/O model suffers a performance setback when it is used as part of massive I/O parallelism  

#### large numbers of threads is limited by increasing performance penalties  
> due to thread stack sizes  
* **permanent context switching**  
* **memory consumption**  

#### event-driven server architectures   
* suffer from less comprehensible and understandable programming style  
* can often not take direct advantage of true CPU parallelism  

#### Combined approaches suggest concepts that incorporate both models  

#### Gaining the benefits of cooperative scheduling and asynchronous/non-blocking I/O operations  
> 무슨 아키텍쳐를 선택하느냐가 중요한 게 아니라 지금 문제에서 필요한 게 뭔지가 더 중요함  

