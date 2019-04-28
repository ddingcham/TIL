# Functional Programming  

## Overview  

### function => first class citizen + pure (no side effect)   

* first-class    

  * first-class-citizen  
    * 변수/데이터 할당 가능  
    * 행위에 대한 인자화 가능  
    * 행위에 대한 리턴화 가능  

  * first-class-function   
    * Higher-order functions: passing functions as arguments  
      > 행위에 대한 인자로써의 함수  
    * Anonymous and nested functions  
    * Non-local variables and closures  
    * Higher-order functions: returning functions as results  
      > 행위에 대한 리턴으로써의 함수  
    * Assigning functions to variables  
      > 함수를 변수/데이터에 할당  
    * Equality of functions  

  * 언어적 관점에서의 지원 (ex] passing functions as arguments)  
    * C (언어적 관점에서 first-class-citizen 으로 취급을 지원하지 않음)  
      > function pointer 나 delegates 기반으로 작성  
      ~~~C
      void map(int (*f)(int), int x[], size_t n) {
        for (int i = 0; i < n; i++)
          x[i] = f(x[i]);
      }
      ~~~  
    * Haskell  
      > 함수가 일급 시민으로 취급되면, 함수를 일반적인 언어에서 값을 취급하듯이 사용 가능  
      ~~~Haskell
      map :: (a -> b) -> [a] -> [b]
      map f []     = []
      map f (x:xs) = f x : map f xs
      ~~~
      
* pure function  
  * see : [The Duality of Pure Functions](https://nofluffjuststuff.com/magazine/2016/11/the_duality_of_pure_functions)  
    ```
    A pure function does not have side-effects and its result does not depend on anything other than its inputs. 
    A pure function guarantees that for a given input it will produce the same output no matter how many times it is called.
    ```

* 사이드 이펙트가 없다면 -> 비결정론적인 흐름에도 문제가 발생하지 않음  

#### no mutable state : functional programming is so compelling when it comes to concurrency and parallelism  
> ex] **Immutable data 는 잠금 장치가 없어도, Thread-Safe 를 만족함**  
> 2장에서 다룬 내용은 가변+공유 상태에 대한 이슈를 관리하기 위한 것  
> 가변 상태를 다루지 않는다면? -> 해당 이슈들의 발생 원인이 없음  

## Day1  

### JAVA - 가변 상태의 위험성 // 캡슐화와 사이드 이펙트 제어  

* 진짜 immutable 한 지 알기가 어려움  
  * ex]  
    ~~~java  
    class DateParser {
      private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      
      public Date parse(String s) throws ParseException {
        return format.parse(s);
      }  
    }
    ~~~  
    * "final" 키워드로 선언한 DateFormat은 얼핏보기에는 **immutable하므로, thread-safe 하다.**    
      > 하지만 DateFormat 구현체 SimpleDateFormat 내부적으로 가변 상태를 갖음  
    * 복수의 스레드가 DateParser를 사용한다면 ???  
      > SimpleDateFormat 구현체 내부의 가변 상태로 인한 동시성 이슈가 발생  
  * OOP : 약한 결합과 강한 응집을 갖도록 디자인 할 지라도 ???  
    > 동시성 관점에서 바라볼 때는, 사용자가 **세부 구현**에 대한 신경을 써야만 함    
    > **캡슐화의 이유가 없어짐**  
    * 동시성 관점에서 **세부 구현**에 대한 신경을 쓴다는 건  
      > 실제 관심을 쏟을 부분(어플리케이션 로직)보다 환경(사용하는 플랫폼, API)에 대한 부분에 **더 많은 관심**이 필요  
      > **경우에 따라 불가능한 일**    
      * **가변 상태**에 대한 부분을 해당 구현체의 API에서 바로 만날 수 없을 수도 있음  
      * 바로 만날 수도 없고, **가변 상태**를 확인하기 위해 봐야 할 API의 뎁스가 어디까지 이어질 지도 알 수 없음  
  
* Thread-safe를 위해 캡슐화한 대상이 외부에 노출되는 경우  
  * ex]  
    ~~~java  
    public class Lotto {
      private List<LottoNumber> lottoNumbers = new LinkedList<LottoNumber>(6);
      
      public synchronized void addLottoNumber(LottoNumber number) {
        lottoNumbers.add(number);
      }
      
      public synchronized Iterator<LottoNumber> getLottoNumberIterator() {
        return lottoNumbers.iterator();
      }
    }
    ~~~  
    * 동시성 이슈 시나리오  
      * lottoNumbers 는 외부 접근이 제한 (private)  
      * 상태에 대한 변경은 오직 **synchronized**를 통해 동기화된 addLottoNumber()만 가능  
      * 하지만, **외부에서 Iterator 를 가지고, 내부 가변 상태(LottoNumber)를 참조 한다면?**  
        **Iterator가 사용되는 동안 다른 스레드가 addLottoNumber()를 호출하면 동시성 이슈 발생 가능성 존재**  
        
  * see also   
    * LottoNumber를 Immutable 한 Value-Object 로 다루기  
      > [조영호님 : Domain-Driven Design의 적용-1.VALUE OBJECT와 REFERENCE OBJECT 2부](http://aeternum.egloos.com/1111257)  
      > [위 링크 요약](https://github.com/ddingcham/ORMWithDDD/blob/master/demo/doc/1.VALUE%20OBJECT%20%26%20REFERENCE%20OBJECT/2.md)  
    * [Java SE java.util.Collections](https://docs.oracle.com/javase/8/docs/api/?java/util/Collections.html)  
      > **Collections.unmodifiable~~~()**  
    * [ThoughtWorks Anthology - 객체지향 생활체조 - 일급 콜렉션](https://developerfarm.wordpress.com/2012/02/01/object_calisthenics_/)  


#### Clojure 베이스로 함수형 프로그래밍을 지원하는 언어가 언어 차원에서 가변 상태를 원천적으로 피하게 해주는 걸 경험  
#### 이 특징이 병렬화된 실행에서 얼마나 강력하게 작용하는 지 (강력 - **사용자에게 단순한 모델**)  

## Day1  
> Clojure 1 - LISP 기본 문법  

