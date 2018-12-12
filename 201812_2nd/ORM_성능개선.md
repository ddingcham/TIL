# ORM 성능 개선  
요구사항에서 기술 구현 관점 차례로 접근하도록 정리한 자료  

* 5 tips to write efficient queries with JPA and Hibernate  
  > 요구사항 관점  

* 7 Tips to boost your Hibernate performance  
  > Persistence 기술 관점  

* JPA N+1 문제 및 해결방안 _ jojoldu  
  > 개선 방법을 적용 위치와 객체의 책임  
  > 책임의 관점에 대해서도 고려가 필요  

## [5 tips to write efficient queries with JPA and Hibernate](https://thoughts-on-java.org/5-tips-write-efficient-queries-jpa-hibernate/)
### 1. Choose a projection that fits your use case  
#### This tip is as obvious as it is important.

* You have to decide for each use case  
  * which information it needs.  
  * which operations it has to perform and choose accordingly.  
  
* Entities are a good fit if you have to update or remove (or insert) a record. // AGGREGATE  
  * persistence context has to manage the entities  
    > creates an overhead compared to a DTO proejction.  
    > need to read (almost) all entity attributes (**entity associations**).
    
* DTO's is are a good fit for use cases that only need to read a record.  
  > if they provide all required and **no additional properties**.  
  * Requires you to create a new DTO when you implement a new use case.
    > You can't reuse the same DTO and data access services for all use cases.  
    > if you want to optimize for efficiency.  
    

* **Conclusion _ this doesn't have to be a black and white decision.**  
  약간의 비효율성이 발생할 수 있지만 당장 성능에 큰 고려 대상이 아니라면 ...  
  각각의 경우의 수(유즈케이스)에서 최적화된 각각의 DTO보다는  
  #### 다수의 유즈케이스에서 재사용하기 적합한 DTO를 사용한다.  
  #### 만약 성능 상에 이슈가 있다면 그 이슈에 대해서만 변경된 설정을 적용할 수 있다.

### 2. Avoid eager fetching in your mapping definition  
성능 개선의 가장 중요한 요소 중 하나는 알맞은 FetchType 설정    
* FetchType  
  > defines when Hibernate performs additional queries to initialize an association.  
  * EAGER : when it loads the entity.  
  * **LAZY : when you use the association.**  
  #### It doesn't make any sense to perform additional queries to load data  
  #### before you know that you need it. // for use cases

### 3. Initialize all required associations in your query  
#### What do you do if your use case needs one of these associations?  
은총알은 없듯이 FetchType.LAZY가 능사는 아님.  
* This(LAZY LOADING) is the easiest but also the most inefficient approach  
  to initialize a lazy entity association.

* **n+1 select issue**  
  n : queries for entities associated  
  1 : query for entitiy(Entry Point)  
  
* Solution  
  * FetchType.EAGER  
  * query-independent EntitiyGraph  
  * simple JOIN FETCH clause  
  * JPQL  
  * Criteria Query  
  
  
### 4. Use pagination when you select a list of entities.  
#### When you fetch huge lists of entities or DTOs  
#### Do You really need all of them?  

* Pagination  
  > Humans can't handle lists with hundreds of elements.  
  * UI doesn't need them.  
  * They just slow down your application.

* Solution  
  > Split them into multiple chunks and present each of them on a separate page.  
  * setting appropriate values  
    **firstResult and maxResult on the Query interface.**
  
### 5. Log SQL statements
#### You will still create inefficient queries without recognizing it.  
#### You should always check the executed SQL statements when you apply any changes to your code.
#### Check the generated SQL statements when you make any changes to your code.

## [7 Tips to boost your Hibernate performance](https://thoughts-on-java.org/tips-to-boost-your-hibernate-performance/)  
#### The most common reasons for performance issues are not that difficult to fix.

### 1. Find performance issues with Hibernate Statistics  
* Finding the performance issues as early as possible is always the most important part.  
  ```
  - Most of the performance issues are hardly visible on a small test system.  
  - They are caused by some small ineffiencies which are not visible,
    if you test with a small database and only one parallel user on your local test system.
  ```
* To activate Hibernate Statistics  
  > hibernate.generate_statistics  
  > log level for org.hibernate.stat to DEBUG  
  * Collect some internal statistics about each session like  
    * the number of performed queries  
    * the time spent for them  
    * number of cache hits and misses  
  
* You can avoid the most common issues caused by slow queries  
  * too many queries  
  * missing cache usage  
  #### 5 or 10 additional queries during your test might be several hundreds or thousands
  
### 2. Improve slow queries  
#### Slow queries are not a real JPA or Hibernate issue  
  This kind of performance problems occurs with every framework,  
  **even with plain SQL over JDBC and needs to be analyzed and fixed on the SQL**  
  
* common issue  
  * JPQL  
  * Criteria API  
  
* more complex, optimized SQL  
  * use native query to perform a native SQL statement  
  * 단점 : 매핑작업을 할 수 밖에 없다.  
    * Object[]로 받아오거나  
    * @SqlReultSetMapping  

### 3. Choose the right FetchType  
#### FetchType in most of the cases is FetchType.LAZY

### 4. Use query specific fetching  
You can define eager fetching for the specific queries for **which you really need it**.  
* Solutions  
  * JPQL statement by using FETCH JOIN instead of JOIN  
    > Hibernate don't only join the two entities within the query  
    > but also fetch the related entities from the database.  
    ```
    1 SELECT DISTINCT a FROM Author a JOIN FETCH a.books b
    ```
    #### [중요] REPOSITORY에서 꺼낼 때 Association 이 ~~~~하게 패칭된 ENTITIY를 꺼내면 될 듯
    
  * @NamedEntityGraph, @NamedEntitiyGraphs  
    allows you to define a graph of entities that shall be fetched from the database.  
    ```
    If you combine above’s entity graph with a query that selects an Author entity,
    the entity manager will also fetch the books relationship from the database.
    ```
    
  * @NamedEntityGraph -> Java API  
    If you need a more dynamic way to define your entitiy graph  
    
### 5. Let the database handle data heavy operations  
#### You have to consider that a database is very efficient in handling huge datasets.  
#### 너무 복잡한 연산이나 너무 무거운 데이터를 다루는 작업은 database로 옮기는 게 나음  
* call stored procedures  
  * @NamedStoredProcedureQuery  
  * corresponding Java API  
#### 최후의 수단으로 사용  
  
### 6. Use caches to avoid reading the same data multiple times  
Modular application design and parallel user sessions  
often result in reading the same multiple times.  

#### cache the data that is often read and not changed to often  

* Hibernate offers 3 different caches  
  * 1st Level Cache in Hibernate Session  
    * activated by default  
    * caches all entities that were used within the current session.  
    
  * 2nd Level Cache  
    * ex  
      * javax.persistence.Cacheable  
      * org.hibernate.annotations.Cache  
      * with EhCache  
    
  * query cache  
    > is the only one which **does not store entities.**  
    * caches query results  
      * only entity references  
      * scalar values  
      
### 7. Perform updates and deletes in bulks  
```
삭제여부나 활성화 여부에 대한 테이블을 따로 만들고  
해당 Entitiy의 PK를 가지고 index 먹여서 관리하다가  
index 대로 batch 처리 후 테이블 비워버리면 될 듯?

특정 Entity에 대한 요청이 들어오면  
불변식 검사를 Entity를 꺼내와서 하지말고  
따로 만든 테이블로 검사를 하면 굳이 덩어리 큰 Entity Table 탐색할 필요가 없자나  

만약 저런 식으로 처리를 하면서  
Transaction Script 패턴이 아닌
Domain 안에서 처리를 하려면 매핑을 어떻게 해야되지?  

패칭을 세부적으로 설정해야되나?
```

* **Natural but very Inefficient** in Java  
  Updating or deleting one entity after the other feels quite natural in Java  
  but it is also very inefficient.  
  
* Better Approach  
  would be to perform these operations(Updating/Deleting) in bulks  
  by creating update or delete statements that affect multiple records at once.  

* **CriteriaUpdate and CriteriaDelete operations** _ JPA 2.1  
  
### Conclusion  
#### the most important 
#### Hibernate statistics which allow you to find these inefficiencies  
#### definition of the right FetchType in the **entity mapping and the query**

## [JPA N+1 문제 및 해결방안 _ jojoldu](https://jojoldu.tistory.com/165)  

