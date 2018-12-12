# 201812_2nd
### DDD에 대한 내용을 바탕으로 Mayak 리팩토링  
[repo](https://github.com/ddingcham/MayakAgain)  

## 20181210
### 계획
1. 공부했던 거 몇 가지 복습, 다시 보기
2. AGGREGATE 정리 및 테스트 / ENTITIY 리팩토링

### 기록
1. 알고리즘 공부한 게 기억이 가물가물한 이유  
[새기기 _ 김창준님의 공부하는 방법](http://cafe.daum.net/_c21_/bbs_search_read?grpid=LtXl&fldid=AoDe&datanum=6)
```
Study/acmicpc 폴더에 보면 작년 봄/여름 동안 풀었던 알고리즘 문제들이 70여개 정도 있다.
(일회성 공부가 되기 싫어서 아마 노트에 풀이과정도 정리하고, 다시풀기도 했던거 같다.)

이번에 기술면접을 준비하느라 자료구조/알고리즘 지식을 되돌아 보는데,
한 방에 기억 나는 거 없이, 자료를 보면 그제서야 머리속에서 줄줄이 기억이 난다.

나름 열심히 했는데, 왜 그런지 고민하다가 공감되는 글이 있었다.
(아마도 체계적으로 한다고 자료구조/알고리즘 단원별로 나눠서 훈련한게 오히려 독이 된거 같다.)
(이게 망치의 오류인가 보다 -> 차라리 랜덤으로 풀 걸)

그렇다고 근래에 알고리즘/자료구조를 따로 공부할 계획은 없다.
대신 redis는 방학동안 무조건 적용해볼 거고 그 때 고민해야 겠다.
리팩토링하면서 일급콜렉션에 대한 고민도 많이 해야겠다.
```

2. 루즈해지니까 제약사항을 주자  
  // 금요일까지 ENTITIY 관련된 작업 끝내기  
## 20181211
### 계획
1. AGGREGATE 정리 및 테스트 / ENTITIY 리팩토링

### 기록
1. 이상 발견, 해결
* [이상](https://github.com/ddingcham/MayakAgain/commit/6ac59eda02e8a5786d8580e700bf3d1c8dd3f220)  
* [해결위한참고](https://www.slideshare.net/javajigi/orm-27141159)  
  * 37, 38, 39 슬라이드  
  > 기존 잘못된 Entity 매핑을 기반으로 (도메인 주도에 대한 이해 부족)  
  > 개별 AGGREGATE 로직 구현을 진행하다 보니, 다시 꼬이려는 상황이 발생함  
  ```
  <도메인 주도 설계 개발 과정>
  1. 객체(도메인) 설계
  2. 비즈니스 로직 구현
  3. 객체와 테이블 매핑
  
  특히 1-2 과정은 1을 완성시키고 2를 진행하는 과정으로 보기 보다는
  Aggregate 분석에 기반해서 1에 대한 청사진만 그린 후
  테스트를 토대로 2를 진행하면서 점차 1을 완성(리팩)시켜야 될 것 같다.
  ```
  
  * **추가적인 고려사항**  
    ENTITIY 간 관계 매핑 시 주도권에 대한 기준은 요구사항(도메인 용어 적용)을  
    바탕으로 하지만, 어느 AGGREGATE냐에 따라 계층의 높낮이 구조는 변경될 수 있을 것 같다.
    (높은 애가 주도권을 갖는다고 가정할 때)
    높낮이 구조 변경에 따라, 양방향 매핑이 고려될 수도 있다고 판단된다.  
    (지양해야되는 건 맞지만 괜히 지원하는 게 아니다.)

2. 테스트 코드 작성하면서 리팩토링 시 팁  
> 테스트 실행 시 외부에서 의존하는 애들 때문에 일이 커질 수 있다.  
> 이럴 때 걍 컴파일 대상에서 제외시키면 집중할 일에만 집중할 수 있다.  
> 이 정도는 센스로 찾기  

3. [MartinFowler 블로그 Microservice](https://martinfowler.com/articles/microservices.html) 읽고 내맘대로 정리한거  
화면 반 잘라서 보면 딱 맞음  
```
Microservices

<definition>
MicroService Architectrue
: has sprung(도약v) up over the last few years to describe a particular way of designing software applications as suites of independently deployable services.
While there is no precise definition of this architectural style, there are certain common charateristics around organization around business capability, automated deployment, intelligence in the endpoints, and decentralized control of languages and data

<abstracts>
: microservice architectural style is an approach to developing a single application as a suite of small services, each running in its own process and communicating with lightweight mechanisms, often an HTTP resource API. These services are built around business capabilities and independently deployable by fully automated deployment machinery.
There is a bare minimum of centralized management of these services, which may be written in different programming languages and use different data storage technologies.

<traditional architecture>
microservices style : it's useful to compare it to the monolithic style
=> a monolithic application built as a single unit.
Enterprise Applications are often built in three main parts
1) a client-side user interface
: consisting of HTML pages nad javascript running in a browser on the user's machine
2)a database
: consisting of many tables inserted into a common, and usually relational, database management system
3)a server-side application
: will handle HTTP requests, execute domain logic, retrieve and update data from the database, and select and populate HTML views to be sent to the browser.
     !!! This server-side application is a monolith
     - a single logical executable. Any changes to the system
     involve building and deploying a new version of the      server-side application

<monolithic server>
Such a monolithic server is a natural way to approach building such a system. All your logic for handling a request runs in a single process, allowing you to use the basic features of your language to divide up the application into classes, functions, and namespaces.
WIth some care, you can run and test the application on a developer's laptop, and use a deployment pipeline to ensure that changes are properly tested and deployed into production. You can horizontally scale the monolith by running many instances behind a load-balancer.


Monolithic applications can be successful, but increasingly peaople are feeling frustrations with them
- especially as more applications are being deployed to the cloud.
Change cycles are tied together -
a change made to a small part of the pplication, requires the entire monolith to be rebuilt and deplyed. Over time it's often hard to keep a good modular structrue, making it harder to keep changes that ought to only affect one module within that module. Scling requires scaling of the entire application rather than parts of it that require greater resource.


These frustrations have led to the microservice architectural style
: building applications as suites of services. As well as the fact that services are independently deployable and scalable, each service also provides a firm module boundary, even allowing for different services to be written in different programming languages. They can also be managed by different teams


We do not claim that the microservice style is novel or innovative, its roots go back at least to the design principles of Unix. But we do think that not enough people consider a microservice architectrue and that many software developments would be better off if they used it.


<Characteristics of a Microservice Architectrue>

-Componentization via Services
For as long as we've been involved in the software industry, there's been a desire to build systems by pluggin together components, much in the way we see things are made in the physical world. During the last couple of decades we've seen considerable progress with large compendiums of common libraries that are part of most language plaforms.

*Definition of Component
When talking about components we run into the difficult definition of what makes a component. Our definition is that a component is a unit of sftware that is independently replaceable and upgradeable.

*Different concept about service object in many OO programs
Microservice architectures will use libraries, but their primary way of componentizing their own software is by breaking down into services. We define libraries as components that are linked into a program and called using in-memory function calls, while services are out-of-process components who communicate with a mechanism such as a web service request, or remote procedure call.

*reason for using services as components
1) Services are independently deployable.
: If you have an application that consists of a multiple libraries in a single process, a change to any single component results in having to redeploy the entire application. But if that application is decomposed into multiple services, you can expect many single service changes to only require that service to be redeployed. That's not an absolute, some changes will change service interfaces resulting in some coordination, but the aim of a good microservice architecture is to minimize these through cohesive service boundaries and evolution mechanisms in the service contracts

2) More explicit component interface.

*Published Interface is a term I used (first in Refactoring) to refer to a class interface that's used outside the code base that it's defined in. As such it means more than public in Java and indeed even more than a non-internal public in C#. In my column for IEEE Software I argued that the distinction between published and public is actually more important than that between public and private.

*The reason is that with a non-published interface you can change it and update the calling code since it is all within a single code base. Such things as renames can be done, and done easily with modern refactoring tools. But anything published so you can't reach the calling code needs more complicated treatment.



: Most languages do not have a good mechanism for defining an explicit Published Interface. Often it's only documentation and discipline that prevents clients breaking a component's encapsulation, leading to overly-tight coupling between components. Services make it easier to avoid this by using explicit remote call mechanisms.


*downsides of remote calls
Using services like this does have downsides. Remote calls are more expensive than in process calls, and thus remote APIs need to be coarser-grained, which is often more awkward to use. If you need to change the allocation of responsibilities between components, such movements of behavior are harder to do when you're crossing process boundaries.

At first approximation, we can observe that services map to runtime processes, but that is only a first approximation. A service may consist of multiple processes that will always be developed and deployed together, such as an application process and a database that's only used by that service.

-Organized around Business Capabilities

*existing spliting a application
When looking to split a large application into parts, often management focuses on the technology layer, leading to UI teams, server-side logic teams, and database teams. When teams are separated along these lines, even simple changes can lead to a cross-team project taking time and budgetary approval. A smart team will changes can lead to a cross-team project taking time and budgetary approval. A smart team will optimise around this and plump for the lesser of two evils - just force the logic into whichever application they have acess to. Logic everywhere in other words. This is an example of Conway's Law in action.
// Conway's Law : Any organization that designs a system will produce a design whose structure is a copy of the organization's communication structure.
(ex) Siloed functional teams
UI specialists - middleware specialists - DBAs
-> lead to silod application arhitectrues

*microservice approach
The microservice approach to division is different, splitting up into services organized around business apability. Such services take a broad-stack implementation of software for that business area, including user-interface, persistant storage, and any external collaborations. Consequently the teams are cross-functional, including the full range of skills required for the development: user-experience, database, and project management.
(ex) Cross-functional teams -> organised around capabilities

*example microservice approach
One company organised in this way is "www.comparethemarket.com".
Cross functional teams are responi=sible for building and operating
each product and each product is split out into a number of individual
services communicating via a message bus.

Large monolithic applications can always be modularized around
business capabilities too, although that's not the common case. Certainly we would urge a large team building a monolithic
application to divide itself along bussiness lines. The main issue we
have seen here, is that they tend to be organised around too many
contexts If the monolith spans many of these modular boundaries it
can be difficult for individual members of a team to fit them into their
short-term memory. Additionally we see that the modular lines require
a great deal of discipline to enforce. The necessarily more explicit
separation required by service components makes it easier to keep the
team boundaries clear.

-Products not Projects
Most application development efforts that we see use a project model: where the aim is to deliver some piece of software which is then considered to be completed. On completion the software is handed over to a maintenance organization and the project team that built it is disbanded.

Microservice proponents tend to avoid this model preferring instead the notion that a team should own a product over its full lifetime.
A common inspiration for this is Amazon's notion of "you build, you run it" where a development team takes full responsibility for the software in production. this brings developers into day-to-day contact with how their software behaves in production and increases contact with their users, as they have to take on at least some of the support burden.

The product mentality, ties in with the linkage to business capabilities. Rather than looking at the software as a set of functionality to be completed, there is an on-going relationship where the question is how can software assist its users to enhance the business capability.

There's no reason why this same approach can't be taken with monolithic applications, but the smaller granularity of services can make it easier to create the personal relationships between service developers and their users.

-Smart endpoints and dumb pipes

*communication between different processes
When building communication structures between different processes, we've seen many products and approaches that stress putting significant smarts into communication mechanism itself.
A good example of this is the Enterprise Service Bus(ESB), where ESB products often include sophisticated facilities for message routing, choreography, transformation, and applying business rules.

*microservice communication approach
The microservice community favours an alternative approach: smart endpoints and dumb pipes. Applications built from microservices aim to be as decoupled and as cohesive as possible - they own their own domain logic and act more as filters in the classical Unix sense-
receiving a request, applying logic as appropriate and producing a response. These are choreographed using simple RESTish protocols rather than complex protocols such as WS-Choreography or BPEL or orchestration by a central tool.

*The two protocols used most comonly are HTTP request-response with resource API's and lightweight messaging.
1) The best expression of the first is Microservice teams use the principles and protocols that the world wide web (and to a large extent, Unix) is built on. Often used resources can be cached with very little effort on the part of developers or operations folk.
2) The second approach in common use is messaging over a light weight message bus. The infrastructure chosen is typically dumb (dumb as in acts as a message router only) - simple implementations such as RabbitMQ or ZeroMQ don't do much more than provide a reliable asynchronous fabirc - the smarts still live in the end points that are producing and consuming messages; in the services.

*monolith case
In a monolith, the components are executing in-process and communication between them is via either method invocation or function call. The biggest issue in changing a monolith into microservices lies in changing the communication pattern. A naive conversion from in-memory method calls to RPC leads to chatty
communications which don't perform well. Instaead you need to replace the fine-grained communication with a coarser-grained approach.

-Decentralized Governance

*Standard : Governance centralised vs decentralized
One of the consequences of centralised governance is the tendency to standardise on single technology platforms. Experience shows that this approach is constricting not every probelm is a nail and not every solution a hammer. We prefer using the right tool for the job and while monolithic applications can take advantage of different languages to a certain extent, it isn't that common.

Splitting the monolith's components out into services we have a choice when building each of them. You want to use Node.js to standup a simple reports page? Go for it. C++ for a particularly gnarly near-real-time component? Fine. You want to swap in a different flavour of database that better suits the read behaviour of one component? We have the technology to rebuild him.

Of course, just because you can do something, doesn;t mean you should - but partitioning your system in this way means you have the option.

Teams building microservices prefer a different approach to standards too. Rather than use a set of defined standards written down somewhere on paper they prefer the idea of producing useful tools that other developers can use to solve similar problems to the ones they are facing. These tools are usually harvested from implementations and shared with a wider group, sometimes, but not exclusively using an internal open source model. Now that git and github have become the de facto version control system of choice, open source practices are becoming more and more common in-house.

*Organisation Case : Netflix
Netflix is a good example of an organisation that follows this philosophy. Sharing useful and, above all, battle-tested code as libraries encourages other developers to solve similar problems in similar ways yet leaves the door open to picking a different approach if required.
Shared libraries tend to be focused on common problems of data storage, inter-process communication and as we discuss further below, infrastructure automation.

*Some Opinion : opposite about overhead
For the microservice community, overheads are particularly unattractive. That isn't to say that the community doesn't value service contracts. Quite the opposite, since there tend to be many more of them. It's just that they are looking at different ways of managing those contracts. Patterns like Tolerant Reader and Consumer-Driven Contracts are often applied to microservices. These aid service contracts in evolving independently. Executing consumer driven contracts as part of your build increases confidence and provides fast feedback on whether your services are functioning. Indeed we know of a team in Australia who drive the build of new services with consumer driven contracts. They use simple tools that allow them to define the contract for a service. This becomes part of the automated build before code for the new service is even written. The service is then built out only to the point where it satisfies the contract - an elegant approach to avoid the "YAGNI" dilemma when building new software. These techniques and the tooling growing up around them, limit the need for central contract management by decreasing the temporal coupling between services.

Perhaps the apogee of decentralised governance is the build it / run it ethos popularised by Amazon. Teams are responsible for all aspects aof the software they build including operating the software 24/7. Devolution of this level of responsibility is definitely not the norm but we do see more and more companies  pushing responsibility to the development teams. Netflix is another organisation that has adopted this ethos. Being woken up at 3am every night by your pager is certainly a powerful incentive to focus on quality when writing your code. These ideas are about as far away from the traditional centralized governance model as it is possible to be.

-Decentralized Data Management

*Overview of data management
Decentralization of data management presents in a number of different ways. At the most abstract level, it means that the conceptual model of the world will differ between systems. This is a common issue when integrating across a large enterprise, the sales view of a customer will differ from the support view. Somethings that are called customers in the sales view may not appear at all in the support view. Those that do may have different attributes and (worse) common attributes with subtly different semantics.

This issue is common between applications, but can also occur within applications, particular when that application is divided into separate components. A useful way of thinking about this is the Domain-Driven Design notion of Bounded Context. DDD divides a complex domain up into multiple bounded contexts and maps out the relationships between them. This process is usefull for both monolithic and microservice architectrues, but there is a natural correlation between service and context boundaries that helps clarify, and as we describe in the section on business capabilities, reinforce the separations.

*important term - polyglot
As well as decentralizing decisions about conceptual models, microservices also decentralize data storage decisions. While monolithic applications prefer a single logical database for persistant data, enterprises often prefer a single database across a range of applications - many of these decisions driven through vendor's commercial models around licensing. Microservices prefer letting each service manage its own database, either dirfferent instances of the same database technology its own database, either different instances of the same database technology, or entirely different database systems - an approach called Polyglot Persistence. You can use polyglot persistence in a monolith, but it appears more frequently with microservices.

Decentralizing responsibility for data across microservices has implications for managing updates. The common approach to dealing with updates has been to use transactions to guarantee consistency when updating multiple resources. This approach is often used within monoliths.

* "emphasize transactionless coordination between services"
Using transactions like this helps with consistency, but imposes significant temporal coupling, which is problematic across multiple services. Distributed transactions are notoriously difficult to implement and as a consequence microservice architectrues "emphasize transactionless coordination between services", with explicit recognition that consistency may only be eventual consistency and problems are dealt with by compensating operations.

Choosing to manage inconsistencies in this way is a new challenge for many development teams, but it is one that often matches business practice. Often businesses handle a degree of inconsistency in order to respond quickly to demand, while having some kind of reversal process to deal with mistakes. The trade-off is worth it as long as the cost of fixing mistakes is less than the cost of lost business under greater consistency.

-Infrastructure Automation
Infrastructure automation techniques have evolved enormously over the last few years - the evolution of the cloud and AWS in particular has reduced the operational comlexity of building, deploying and operating microservices.

Many of the products or systems being build with microservices are being built by teams with extensive experience of Continuous Delivery and it's precursor, Continuous Integration. Teams building software this way make extensive use of infrastructure automation techniques. This is illustrated in the build pipeline shown below.

SInce this isn't an article on Continuous Delivery we will call attention to just a couple of key features here. We want as much confidence as possible that our software is working, so we run lots of automated tests. Promotion of working software 'up' the pipeline means we automate deployment to each new environment.

A monolithic application will be built, tested and pushed through these environments quite happlily. It turns out that once you have invested in automating the path to production for a monolith, then deploying more applications doesn't seem so scary any more. Remember, one of the aims of CD is to make deployment boring, so whether its one or three applications, as long as its still boring it doesn't matter.

Another area where we see teams using extensive infrastructure automation is when managing microservices in production. In contrast to our assertion above that as long as deployment is boring there isn't that much difference between monoliths and microservices, the operational landscape for each can be strikingly different.

-Design for failure

A consequence of using services as components, is that applications need to be designed so that they can tolerate the failure of services. Any service call could fail due to unavailability of the supplier, the client has to respond to this as gracefully as possible. This is a disadvantage compared to a monolithic design as it introduces additional complexity to handle it. The consequence is that microservice teams constantly reflect on how service failures affect the user experience. Netflix's Simian Army induces failures of services and even datacenters during the working day to test both the application's resilience and monitoring.

This kind of automated testing in production would be enough to give most operation groups the kind of shivers usually preceding a week off work. This isn't to say that monolithic architectural styles aren;t capable of sophisticated monitoring setups - it's just less common in our experience.

Since services can fail at any time, it's important to be able to detect the failures quickly and, if possible, automatically restore service. Microservice applications put a lot of emphasis on real-time monitoring of the application, checking both architectural elements (how many requests per second is the database getting) and business relevant metrics (such as how many orders per minute are received). Semantic monitoring can provide an early warning system of something going wrong that triggers development teams to follow up and investigate.

This is particularly important to a microservices architecture because the miroservice preference towards choreopraphy and event collaboration leads emergent behavior. While many pundits praise the value of serendipitous emergence, the truth is that emergent behavior can sometimes be a bad thing. Monitoring is vital spot bad emergent behavior quickly so it can be fixed.

Monoliths can be built to be as transparent as a microservice - in fact, they should be. The difference is that you absolutely need to know when services running in different processes are disconnected. With libraries the same process this kind of transparency is less likely to be useful.

Microservice teams would expect to see sophisticated monitoring and logging setups for each individual service such as dashboards showing up/down status and a variety of operational and business relevant metrics. Details on circuit breaker status, current throughput and latency are other examples we often encounter in the wild.

-Evolutionary Design
Microservice practitioners, usually have come from an evolutionary design background and see service decomposition as a further tool to enable application developers to control changes in their application without slowing down change. Change control doesn't necessarily mean change reduction - with the right attitudes and tools you can make frequent, fast, and well-controlled changes to software.

Whenever you try to break a software system into components, you're faced with the decision how to divide up the pieces - what are the principles on which we decide to slice up our application? The key property of a component is the notion of independent replacement and upgradeability - which implies we look for points where we can imagine rewriting a component without affecting its collaborators. Indeed many microservice groups take this further by explicitly expecting many services to be scrapped rather than evolved in the longer term.

*monolith case : Guardian website
The Guardian website is a good example of an application that was designed and built as a monolith, but has been evolving in a miroservice direction. The monolith still is the core of the website, but they prefer to add new features by building microservices that use the monolith's API. This approach is particularly handy for features that are inherently temporary, such as specialized pages to handle a sporting event. Such a part of the website can quickly be put together using rapid development languages, and removed once the event is over. We've seen similar approaches at a financial institution where new services are added for a market opportunity and discarded after a few months or even weeks.

This emphasis on replaceability is a special case of a more general principle of modular design, which is to drive modularity through the pattern of change. You want to keep things that change at the same time in the same module. Parts of a system that change rarely should be in different services to those that are currently undergoing lots of churn. If you find yourself repeatedly changing two services together, that's a sign that they should be merged.

Putting components into services adds an opportunity for more granular release planning. With a monolith any changes require a full build and deployment of the entire application. With micoservices,mto redeploy the service(s) you modified. This can simplify and speed up the release process. The downside is that you have to worry about changes to one service breaking its consumers. The traditional integration approach is to try to deal with this problem using versioning, but the preference in the microservice world is to only use versioning as a last resort. We can avoid a lot of versioning by designing services to be as tolerant as possible to changes in their suppliers.

-Are Microservices the Future
Our main aim in writing this article is to explain the major ideas and principles of microservices. By taking the time to do this we clearly think that the microservicecs architectural style is a an important idea
=> one worth serious consideration for enterprise applications. We have recently built several systems using the style and know of others who have used and favor this approach.

*enterprise related with Microservices
Those we know about who are in some way pionering the architectural style include Amazon, Netflix, The Guardian, the UK Government Digital Service, realestate.com.au, For ward and comparethemarket.com. The conference circuit in 2013 was full of examples of companies that are moving to something that would class as microservices - including Travis CI. In addition there are plenty of organizations that have long been doing what we would class as microservices, but without ever using the name. (Often this is labelled as SOA - although, as we've said, SOA comes in many contradictory forms.)

*Oposite VIew
Despite these positive experiences, however, we aren't arguing that we are certain that microservices are the future direction for software architectures. While our experiences so far are positive compared to monolithic applications, we're conscious of the fact that not enough time has passed for us to make full judgement.

Often the true consequences of your architectural decisions are only evident several years after you made them. We have seen projects where a good team, with a strong desire for modularity, has built a monolithic architecture that has decayed over the years. Many people believe that such decay is less likely with microservices, since the service boundaries are explicit and hard to patch around. Yet until we se enough systems with enough age, we can't truly assess how microservice architectures mature.

*Certain reasons about microservices
There are certainly reasons why one might expect microservices to mature poorly. In any effort at componentization, success depends on how well the software fits into components. It's hard to figure out exactly where the component boundaries should lie. Evolutionary design recognizes the difficulties of getting boundaries right and thus the importance of it being easy to refactory them. But when your components are services with remote communications, then refactoring is much harder than with in-process libraries. Moving code is difficult across service boundaries, any interface changes need to be coordinated between participants, layer of backwards compatibility need to be added, and testing is made more complicated.

Another issue is If the components do not compose cleanly, then all you are doing is shifting complexity from inside a component to the connections between components. Not just does this just move complexity around, it moves it to a place that's less explicit and harder to control. It's easy to think things are better when you are looking at the inside of a small, simple component, while missing messy connections between services.

*Result
Finally, there is the factor of team skill. New techniques tend to be adopted by more skillful teams. But a technique that is more effective for a more skillful team isn't necessarily going to work for less skillful teams. We've seen plenty of cases of less skillful teams building messy monolithic architectures, but it takes time to see what happens when this kind of mess occurs with microservices. A poor team will always create a poor system - it's very hard to tell if microservices reduce the mess in this case or make it worse.

One reasonable argument we've heard is that you shouldn't start with a microservices architecture. Instead begin with a monolith, keep it modular, and spllit it into microservices once the monolith becomes a probelm.
(Although this advice isn't ideal, since a good in-process interface is usually not a good service interface.)

So we write this with cautious optimism. So far, we've seen enough about the microservice style to feel that it can be a worthwhile road to tread. We can't say for sure where we'll end up, but one of the challenges of software devleopment is that you can only make decisions based on the imperfect information that you currently have to hand.
```

## 20181212
### 계획

### 기록
1. [생각해 볼 만한 글 (동의, 반대 둘 다 들었음)](https://okky.kr/article/398140)  
2. [ORM 성능 개선 정리](https://github.com/ddingcham/TIL/blob/master/201812_2nd/ORM_%EC%84%B1%EB%8A%A5%EA%B0%9C%EC%84%A0.md)  
3. JPQL 기반 fetch join 적용 시 QueryDSL을 활용하기  
   * QueryDSL 미적용 시 JPQL이 틀릴 수 있다.  
   ```
   @Query("select DISTINCT store from Store store join fetch store.reservations reservation where reservation.activated = 'true'")
   Store findByIdWithActiveReservation(long id);
   ```
   * QueryDSL을 적용하면 Entity Type 기반으로 fetch join이 적용된 JPQL을 생성할 수 있다.  
   ```
   query.from(order)
   .innerJoin(order.member, member).fetch()
   .leftJoin(order.orderItem, orderItem).fetch()
   .list(order)
   
   위의 코드를 갖고 있는 method가 Repository에 선언한 메소드에 의해 호출된다.  
   ```
   
   * 자세한 사항은 스프링 공식 레퍼런스랑 아래 자료를 토대로 확인  
     * [그 외 여러가지 활용할 만한 케이스가 많다.](https://ultrakain.gitbooks.io/jpa/chapter10/chapter10.4.html)  
     * [예제가 많아 실제로 적용하게 될 때 학습하기 좋을 자료](http://adrenal.tistory.com/23?category=301635)  
       > 안개가 겉힌다는 표현이 내가 했던 생각이랑 똑같다.  
       
   * MyBatis랑 비교  
     ```
     JPA 기반에서는 도메인을 단순하게 관리할 수 있는 대신  
     MyBatis보다 다양한 것들을 학습해야 한다.  
     하지만 도메인(Entity)를 격리시킨 상태에서, 지속적으로 개선해 나갈 수 있기에  
     어려워도 학습할 만한 가치가 있는 것 같다.  
     (또 자료를 보니깐 생각보다 모든 부분을 다 알아야 하는 상황이 많이 나오지도 않은 것 같다.)  
     
     그리고 어차피 Entity가 엄청나게 커질 일도 없을 것 같다.  
     해당 Entitiy가 사용되는 서비스 자체가 분할되고 있기 때문이다.  
     
     요즘 많이 쓰는 프레임워크나 기술, 사상들은
     작은 단위로 나눌 수 있고, 필요한 부분만 빠르게 적용하고
     작아진 만큼 빠르게 만들고, 빠르게 개선할 수 있는
     그런 게 트렌드인 것 같다.
     ```
