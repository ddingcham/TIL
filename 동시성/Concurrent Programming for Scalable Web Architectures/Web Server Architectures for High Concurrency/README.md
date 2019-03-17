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
