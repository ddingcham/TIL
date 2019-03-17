# Concurrent Programming for Scalable Web Architectures  
## 3. The Quest for Scalable Web Architectures  

### Introduction  
* behind the architectures that provide web applications  
* outline several technologies that have emerged to serve dynamic web content  
* more recent cloud-based architectrues  
* how these technologies have been integrated into common architectures.  
  * load-balancing  
  * established and important mechanism for scalability  
* infrastructure and services provided by popular cloud vendors  

#### generic architectural model for scalable web applications  
> focusing on concurrency.  

#### separation into the different architectural components  
* provide a clearer scalability path for each component  
* reason about particular concurrency challenges inherent to the distinct components.

### Summary  

#### delivering dynamic content represents a different challenge than serving static files  
> request per process -> thread / container model  

#### Containers model / A tiered view on web architectures  
> which is the preferred way for Java-based applications (servlet specification)  
* split up deployment-specific components  
* **Logical Separation of Concerns** by layering    
  * presentation  
  * application logic  
  * persistence  

#### load balancing / enabling the ditribution of load to multiple servers  

#### Cloud Computing platforms and infrastructures relate well to scalable web architectures  

#### Instead of a single, monolithic architecture / a set of dedicated service components  
**with loose coupling**  

* allows us to scale different service components / 수평 확장  
  * appropriate scaling strategies  
  * makes overall architecture **more robust, reliable and agile**  
  
#### isolation of web servers, applications servers, backend storage sustems  
> more precise view on concurrency challenges inside each component type  
> 동시성에 관한 복잡한 문제를 고립된 분할을 통해 단순화  

#### content delivery and faster web sites side  
> 주요 주제는 아니지만, 고려가 필요하긴 함  
