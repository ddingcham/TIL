# Threads and Locks  

## Day1  

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
      
