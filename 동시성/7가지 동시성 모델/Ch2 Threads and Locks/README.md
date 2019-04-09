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
