# Threads and Locks  

## Overview
* 실전 코드에서는 Thread **직접 다룰 일** 없음  

* [first lock](https://github.com/ddingcham/simple-concurrency/commit/d6ad42e31b102f63f47cd8ecaf9c755d4f09bbd0)  
  * 읽기-수정하기-쓰기 패턴  
    ~~~
    getfield #2
    iconst_1
    iadd
    putfield #2
    ~~~  
  * intrinsic lock in every java object  
    > mutex / monitor / critical section  
    > synchronized syntax  
  * 단순한 상황에서는 lock 대신 atomic  
    > java.util.concurrent.atomic  

* [race condition](https://github.com/ddingcham/simple-concurrency/commit/2376628f1f792da6f30cd1eb132d1bd4cbb92a37)  
  > answerReady == true && answer == 0 인 상황이 나올 수 있음  
  * 결정론적인 실행 같지만 내부적으로 **코드 실행 순서 변경** 가능성 있음  
    * 컴파일러의 정적 최적화 수행  
    * JVM의 동적 최적화 수행  
    * 실제 처리하는 하드웨어에서의 순서 변경  
  * 컴파일러, JVM, 하드웨어 가 실행 순서를 변경하는 걸 막기 보다는 ...   
    > 내부적인 최적화 방식을 건들기 보다는  
    * 자바 메모리 모델 이해를 토대로 정책을 찾아야 됨  
  * **memory visibility**  
    > The Java memory model defines when changes to memory made by one thread become visible to another thread.  
    > 읽기 작업 스레드와 쓰기 작업 쓰레드가 동기화되는 경우에만 가시성을 보장  
    * synchronization  
      * instrinsic lock  
        * Thread : start() -> join()  
        * java.util.concurrent  
      * 변경뿐만 아니라 읽기에 대한 동기화도 고려해야하는 대상  
    
* [multiple locks](https://github.com/ddingcham/simple-concurrency/commit/c0ae09a42371c0f6114eb2ca668b656055408cdc)   
  * 엄격한 동기화의 문제점  
    * 병렬적 실행을 위한 Thread 활용이 의미 없어짐  
    * **데드락 발생 가능성**  
      > complexity  
      > left/right 에 대한 순환 락  
      
* [락과 외부 의존성 via Cloning](https://github.com/ddingcham/simple-concurrency/commit/d83973e9f2f7734df344599d10ca5a98d29c6774)  
  * 락을 보유한 스레드의 외부 호출  
    > @Transactional 내부에서의 외부 호출이 트랜잭션 범위에 포함되지 않는 케이스랑 비슷  
  * 락을 건 대상을 쥐고 있는 게 아닌 클로닝 시점까지만 잠그고, 그 이후엔 복제본 활용  
    > **락 보유 시간은 짧을수록 좋음**  
      
## Day1
* 요약  
  * 공유 상태에 대한 접근 동기화  
  * 쓰기 스레드 / 읽기 스레드 동기화  
  * 복수의 락에 대한 순서 공통 룰  
  * 락 보유 중에 외부 호출 X  
  * 락은 최대한 짧게 보유  
  
* [java-memory-model : William Pugh](http://www.cs.umd.edu/~pugh/java/memoryModel/)  
* [JSR - 133](http://qwefgh90.github.io/java/JSR-133-(Java-Memory-Model)-FAQ(%EB%B2%88%EC%97%AD)/)  
  * goal  
    * Preserving existing safety guarantees  
      > variable values may not be created "out of thin air"  
      > : each value for a variable observed by some thread  
      > must be a value that can reasonably be placed there by some thread  
    * Semantics of correctly synchronized programs should be  
      > as **simple and intuitive** as possible  
    * Semantics of incompletely or incorrectly synchronized programs should be defined  
      > so that **potential security hazards are minimized**  
    * Programmers should be able to reason confidently  
      > how multithreaded programs interact with memory  
    * Desigining high performance JVM implementations across a wide range of popular hardware architectures  
    * New **Guarantee of initialization safety**  
      초기화 안정성 보장 관점에서의 자바 메모리 모델  
      > via final without the need for synchronization  
      > all threads (see a reference to that object) will also see the values for its final fields  
      > **that were set in the constructor**  
    * Minimal impact on existing code  
* 스레드 간 공유 객체 관리를 위해서는 락만 가능 ?  
  > Immutable Object / Cloning / final(JMM : check JVM version)  
  
* [안티패턴]double-checked locking  
  > **early JVMs issue** : synchronization was slow    
  * bad case : double-checked-locking  
    ~~~java
    private static Something doubleCheckedInstance = null;

    public Something getInstance() {
      if (doubleCheckedInstance == null) {
        synchronized (this) {
          if (doubleCheckedInstance == null) doubleCheckedInstance = new Something();
        }
      }
      return doubleCheckedInstance;
    }
    ~~~  
    * instructions can be reordered by the compiler or the cache  
      * instruction_1 : initialize doubleCheckedInstance  
      * instruction_2 : write to the doubleCheckedInstance  
    * returning a **partially constructed Something**(uninitialized object)  
    * see also : ["Double Checked Locking is broken"](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)
    
  * solutions  
    * using "volatile" syntax  
      > not supported in JVMs prior oto 1.5  
    * **Initialization On Demand Holder idiom**  
      > thread-safe & a lot easier  
      ~~~java
      private static class LazySomethingHolder {
        public static Something something = new Something();
      }
      
      public static Something getInstance() {
        return LazySomethingHolder.something;
      }
      ~~~  

* Philosopher -> 데드락 케이스 상황 구현  
  * **Coffman conditions** : Necessary conditions for deadlock  
    > [DEADLOCK IN JAVA WITH EXAMPLE](http://www.idc-online.com/technical_references/pdfs/information_technology/Deadlock_in_Java_with_Example.pdf)  
    * **Mutual Exclusion**  
      > At least one resource must be held in a non-shareable mode.  
      > Only one process can use the resource at any given instant of time  
    * **Hold and Wait** or Resource Holding  
      > A process is currently holding at least one resource  
      > and requesting additional resources which are being held by other processes  
    * **No Preemption**  
      > The operating system must not de-allocate resources once they have been allocated;  
      > they must be released by the holding process voluntarily  
    * **Circular Wait**  
      > A process must be waiting for a resource which is being held by another process,  
      > another process is waiting for the first process to release the resource  
      
* **동기화 정책 부재 + 내부 최적화 (실행 순서) 로 인한 사이드 이펙트 구현**  

## Day2 : 내재된 잠금장치(intrinsic lock) 대체  
#### java.util.concurrent  

#### instrinsic lock의 한계  
* instrinsic lock를 얻다가 블로킹 상태에 빠지는 경우, 해당 스레드를 원상복귀시킬 방법이 없음  
* instrinsic lock에 대한 대기 시간 도중에 강제로 중단시킬 방법이 없음  
* instrinsic lock을 얻는 방식이 synchoronized 뿐  
  * 공유 자원 잠금  
    ~~~java
    void synchronizedMethod() {
      synchronized(object) {
        <<공유 자원 사용>>
      }
    }
    ~~~  
  * 메서드 전체 잠금  
    * 시그니쳐  
      ~~~java
      synchronized void synchronizedMethod() {
          <<메서드 본문>>      
      }
      ~~~  
    * 메서드 내부 정의  
      ~~~java
      void synchronizedMethod() {
        synchronized(this) {
          <<메서드 본문>>
        }
      }
      ~~~  

#### ex] [interruptible](https://github.com/ddingcham/simple-concurrency/commit/9535674a123472099928b7c8f5316a164a163978)  
* 스레드가 instrinsic lock으로 인해 데드락이 되면, 방법이 없음  
  > **JVM 전체를 죽이는 것 밖에**  
  * (몇몇 시도)[https://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html]  
    > 더 이상 사용되지 않음 : 시도를 했었다는 것만 알아두자    
    
* lockInterruptibly  
  ```
  Lock(interface) -> lockInterruptibly()
  Implementation Considerations

  The ability to interrupt a lock acquisition in some implementations may not be possible, 
  and if possible may be an expensive operation. 
  The programmer should be aware that this may be the case. 
  An implementation should document when this is the case.

  An implementation can favor responding to an interrupt over normal method return.

  A Lock implementation may be able to detect erroneous use of the lock, 
  such as an invocation that would cause deadlock, and may throw an (unchecked) exception in such circumstances. 
  The circumstances and the exception type must be documented by that Lock implementation.
  ```  

#### [Timeouts](https://github.com/ddingcham/simple-concurrency/blob/master/threadsAndLocks/src/main/java/philosopher/TimeOutablePhilosopher.java)  

* lock을 얻기위해 대기하는 시간에 제한을 둠  
  > 영원한 대기 방지  
* 한계점   
  * 데드락을 완전히 피하는 게 아닌, 데드락 상태로 빠지는 건 인정하되, 빠져나올 방법을 제공할 뿐임  
  * **라이브락**  
    * 모든 스레드가 동시에 타임 아웃을 발생시키면 ?  
    * 스레드들이 곧 바로, 데드락 상태에 빠지게 됨  
    * 탈출을 할 수 있긴 하지만, **이 상태가 무한히 반복되지 않는다는 보장이 없음**  
* 스레드들이 서로 다른 타임아웃 값을 갖도록 해서 완화할 수는 있지만, 궁극적인 해결방법은 아님   

#### [Hand-over-Hand Locking](https://github.com/ddingcham/simple-concurrency/blob/master/threadsAndLocks/src/main/java/linkedlist/ConcurrentSortedList.java)  
* 복수의 노드로 구성된 리스트에 삽입  
  > DB 트랜잭션 처리와 비슷함  
  > 리스트 : 테이블 / 부분 노드 : 부분 레코드  
  * 리스트 전체에 대한 잠금  
  * 삽입에 필요한 리스트의 일정 부분만에 대한 잠금  
    * 원하는 노드의 양쪽 노드를 잠금  
    
* ReentrantLock lock()/unlock() 를 활용해서 잠금 여부 동기화  
  > 내재된 잠금장치(intrinsic lock)만으로는 한계가 있음  
  > 구현체 제공  
  
* ConcurrentSortedList - size() : 단 하나의 노드에 대한 lock만 갖는 걸 보장하도록 구현  
  > 스레드 당 복수의 lock을 가질 일이 없으므로 **Global Ordering Rule** 필요 없음  
  
#### [Condition Variables](https://github.com/ddingcham/simple-concurrency/blob/master/threadsAndLocks/src/main/java/philosopher/SignalingPhilosopher.java)  
* waiting via Condition Variables     
  * 큐에서 값을 꺼낼 경우 - **큐 안에 적어도 하나 이상의 값이 있을 때까지 대기**  
  * 버퍼에 값을 넣을 경우 - **버퍼에 필요한 공간이 생길때까지 대기**  

* **Simple Pattern : using Condition Variables effectively**  
  ~~~java  
  ReentrantLock lock = new ReentrantLock();
  Condition condition = lock.newCondition();
  
  lock.lock();
  try {
    while (  <<waiting-case-condition>>  ) {
      condition.await();
      <<using-shared-resource>>  
    } finally {  lock.unlock();  }
  }
  ~~~  
  * **잠금 장치 획득**  
    > waiting-case-condition이 깨지길 기다리려면 잠금을 얻는 게 필수  
    > **특정 조건이 성립하더라도 잠금 장치를 얻지 못하면 의미 없음**  
    > **잠금 장치에 대한 대기시간 동안 특정 조건이 풀려버림**  
    
  * **waiting-case-condition** : **blocking/aSynchronous**   
    * 대기 조건이 참이면 await()를 호출해 대기  
      * 기다릴 필요가 없을 때까지 **블로킹되는 작업을 원자적으로 수행**  
      * 원자적 수행 : 다른 스레드 관점에서 해당 동작이 "완전 수행(1)"/"전혀 수행(0)" 처럼 이분법적으로만 가시되게   
    * 대기 조건이 거짓이면 : **상태를 공유하는 다른 스레드가 signal()/signalAll() 호출**  
      * await()는 블로킹을 멈추고, **자동으로 잠금장치를 재 획득**  
      * await()의 루프 호출 : **리턴 시점에서 대기 조건이 거짓이 될 수 있으므로**  
      
* 이전의 식사하는 철학자 예제들과 비교  
  * 이전 예제 : [GlobalOrderingPhilosopher](https://github.com/ddingcham/simple-concurrency/blob/master/threadsAndLocks/src/main/java/philosopher/GlobalOrderingPhilosopher.java), [TimeOutablePhilosopher](https://github.com/ddingcham/simple-concurrency/blob/master/threadsAndLocks/src/main/java/philosopher/TimeOutablePhilosopher.java)  
    > **모든 철학자가 공유하는 변수(Chopstick)에 대한 잠금**  
    > 한 번에 한 철학자만 식사 가능  
  * **동시성 측면에서 병렬로 실행(식사 가능한)될 수 있는 스레드(철학자) 숫자가 늘어남**  
  
#### [Atomic Variables](https://github.com/ddingcham/simple-concurrency/blob/10cce22cf9ae876d7ce562a4902704f940502ec1/threadsAndLocks/src/main/java/counting/CountingThread.java)  
* **Atomic Variables**의 lock 대비 장점 : via java.util.concurrent.atomic  
  > **non-blocking / lock-free**  
  * getCount()의 동기화  
    > 동기화되지 않아 발생한 메모리 가시성 문제를 배제할 수 있게됨     
  * 데드락 방지  
    > lock 메커니즘이 개입하지 않으므로 **원자 변수에 대한 동작**이 데드락에 걸리는 경우가 없음  

#### Lock Fairness - Provided ReentrantLock  
```
public ReentrantLock()
Creates an instance of ReentrantLock. This is equivalent to using ReentrantLock(false).

public ReentrantLock(boolean fair)
Creates an instance of ReentrantLock with the given fairness policy.
Parameters:
fair - true if this lock should use a fair ordering policy
```  
* lock 소유권을 대기 시간이 가장 긴 스레드에게 **우선 제공**  
  * lock 우선권에 대한 공정성 : **스레드 스케쥴링에 대한 공정성이 아님**     
    > lock-fairness에 의해서 lock 우선권을 갖는 스레드가 우선 실행된다는 보장이 없음  
    > 외부 스케쥴링에 종속된 **비결정론적인 부분**임  
* trade-offs  
  * Programs using fair locks accessed by many threads may display lower overall throughput (i.e., are slower; often much slower)  
  * 느리지만, starvation 이슈의 발생을 줄일 수 있음     
  
* 정책 선택 : **starvation 이슈로 인한 부작용 비용**과 **lock 우선권을 갖는 스레드로 스케쥴링될 때까지의 비용**을 비교  

#### [ReentrantReadWriteLock](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/ReentrantReadWriteLock.html#readLock--)  
> "This class has the following properties" 숙지  

* **동시 읽기는 허용**되지만, 읽기 vs 쓰기 / 쓰기 vs 쓰기에 대한 잠금  
  > 허용/차단 여부를 확인하게 되므로 부하가 추가됨  
* ReentrantLock 대비, **다수의 읽기 스레드** / **상대적으로 소수의 쓰기 스레드** 케이스 제어 시 효과적임    
  * 세부제어가 가능하지만 복잡함  
* 용도에 따라 transaction isolation level 설정 하듯이 다른 구현체들과 비교해서 선택하면 될 듯  

* sample usages 1 : how to perform **lock downgrading** after updating a cache   
  > 쓰기 작업 시 잠금 처리  
  * **쓰기 작업 완료 후 writeLock을 반납하기 전에 readLock으로 Lock Downgrading**  
    * 여기서 부터는 쓰기 작업에 대한 재진입 허용  
    * **하지만, 만약 쓰기 작업이 재진입 될 경우, 읽기에 대한 재진입은 허용하지 않도록 구현**  
  * readLock -> writeLock 으로 Lock Upgrading 은 안됨  
  
~~~java  
class CachedData {
  Object data;
  volatile boolean cacheValid;
  final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
   
  void processCachedData() {
    rwl.readLock().lock();
    if (!cacheValid) {
      // Must release read lock before acquiring write lock  
      rwl.readLock().unlock();
      rwl.writeLock().lock();
      try {
        // <Recheck state>  
        // : another thread might have acquired write lock and changed state before we did  
        if (!cacheValid) {
          data = ...
          cacheValid = true;
        }
        // Downgrade by acquiring read lock before releasing write lock
        rwl.readLock().lock();
      } finally {
        rwl.writeLock().unlock(); // Unlock write, still hold read
      }
    }
    
    try {
      use(data);
    } finally {
      rwl.readLock().unlock();
    }
  }
}
~~~  

* sample usages 2 : TreeMap that is expected to be large and concurrently accessed(much more read)     
  * ReentrantReadWriteLocks can be used to improve concurrency in some uses of kinds of Collections   
  * 강한 일관성   
~~~java
class RWDictionary {
  private final Map<String, Data> m = new TreeMap<String, Data>();
  private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
  private final Lock r = rwl.readLock();
  private final Lock w = rwl.writeLock();
  
  public Data get(String key) {
    r.lock();
    try { return m.get(key); }
    finally { r.unlock(); }
  }
  public String[] allKeys() {
    r.lock();
    try { return m.keySet().toArray(); }
    finally { r.unlock(); }
  }
  public Data put(String key, Data value) {
    w.lock();
    try { return m.put(key, value); }
    finally { w.unlock(); }
  }
  public void clear() {
    w.lock();
    try { m.clear(); }
    finally { w.unlock(); }
  }
}
~~~  

#### Spurious Wakeup  
* When waiting upon a [Condition](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Condition.html), a "spurious wakeup" is permitted to occur    
  > Java SE java.util.concurrent.locks.Condition -> Implementation Considerations  
  
* 스레드가 wait() / await() 상태에서 시그널을 받고, 작업을 수행하기 직전에  
  * **다른 동시 작업 흐름에 의해 대기 조건이 변경될 가능성이 있음** -> wakeup 되었지만 가짜나 마찮가지임  
  * 따라서 위의 Condition Variables - Simple Pattern 을 따라야 함    
    > 대기 조건 검사를 하는 loop 내에서 wait() 호출하는 패턴을 활용함으로써 동시 실행으로 인한 부작용을 예방할 수 있음   
  
#### AtomicIntegerFieldUpdater - AtomicInteger  
* [java.util.concurrent.atomic.AtomicIntegerFieldUpdater\<T\>](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicIntegerFieldUpdater.html)  
  > **리플렉션 기반으로 객체를 원자적으로 취급할 수 있도록 래핑해주는 유틸**  
  > **비원자적인 기존 정의를 유지하면서, 원자적 작업이 필요한 특정 상황에서만 래핑해서 활용**  
* [Why and when to use AtomicIntegerFieldUpdater](https://stackoverflow.com/questions/17153572/why-and-when-to-use-atomicintegerfieldupdater)  
  > if you are creating a lot of objects and you don't want to create a lot of AtomicXXX objects, you can create a static AtomicIntegerFieldUpdater object and let it be shared between all the objects  
* [javamex - Atomic field updaters](https://www.javamex.com/tutorials/synchronization_concurrency_7_atomic_updaters.shtml)  
  > In truth, these wrappers are used inside the Java class libraries, but probably aren't used much in user code.  

#### [IntrinsicSignalingPhilosopher]()  

#### [Hand-over-Hand Locking과 SingleLock 비교](https://github.com/ddingcham/simple-concurrency/commit/ecf02d3fc0fd2bf5e86742bcd19c6278d8c7de20)  
