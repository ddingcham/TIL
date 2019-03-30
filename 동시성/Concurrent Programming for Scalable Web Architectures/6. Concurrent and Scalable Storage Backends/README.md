# Concurrent Programming for Scalable Web Architectures  
# 6. Concurrent and Scalable Storage Backends  

## Intro  
#### Some type of persistent state represents an essential part of the overall application  
> edged away state handling to dedicated backend services  

#### impact of concurrency and scalability in storage backends  
* challenge of guaranteeing consistency in distributed database systems  
* point to different consistency models  
* internal concept of distributed database systems  
  > how they handle replication and partitioning  
* **different types of ditributed database systems and asses their features**  

## Summary  

#### Essential Requirements for persistent storage (large-scale web applications)  
> Distributing Database System  
* availability  
* high performance  
* scalability (operations/volumes)  

#### CAP - failures cannot be avoided, but must rather be anticipated    
> Consistency, Availability, Partition-Tolerance  
* Finding a trade-off between strict consistency and high availability  
  * ACID paradigm - Strong Consistency  
  * BASE paradigm - Basic Availability / Eventual Consistency  

* trade-off based on **Actual Application Scenario**  
  * 그치만 일관성은 중요함  
    > 일관성 포기가 아님  
  * 일관성을 즉시 보장할 지, 여유를 두고 보장할 지에 대해서 고려해야 함  

#### challenging distribution   
* consensus  
* distributed transactions  
* revisioning based on vector clocks  
* **Replication**  
  > availability and fault-tolerance  
* **Partitioning**  
  > handling large amounts of data  
  > allocating them to diffrent physical nodes  
  
**Although RDBMS (relational data - transactional operations) fits many business applications and can be used in various scenarios,**    
**there are other database concepts that provide different characteristics and their own benefits**  

