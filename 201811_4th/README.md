# 네이버 핵데이 준비


## 20181124

### 추천

* [추천시스템과 협업필터링](https://www.slideshare.net/bage79/ss-45783615)
  * 추천 시스템  
    user에게 item3과 item4 중 하나를 추천할 경우  
    두 가지 시나리오  
    
    * user와 user1이 유사하므로 user1이 선호하는 item3을 추천  
      > item1, item2 에 대한 user와 user1의 선호 성향(유사도) 비슷  
      >> 비슷한 성향의 user들이 필요
      
    * user가 item1을 선호하고, item1이 item3과 유사하므로 item3을 추천  
      > item1을 선호하는 user1은 item3도 선호함  
      >> 특정 item을 선호하게 된 이유 -> 같은 이유로 다른 item도 선호할 가능성이있음  
      
  * 협업필터링  
  > 큰 집합의 사람들을 비교하여 유사한 취향의 작은 집합을 도출  
  
    * 협업필터링 사용 예 -> 제품 추천시스템 (아마존)  
      > 다양한 데이터 기반의 휴게소 추천 시스템 
      
    * 협업필터링의 종류  
      * 사용자 기반 필터링  
        * 데이터 양이 작고, 데이터 변경이 자주 일어나는 경우  
          > 한 번에 모든 유저에 대한 설문조사  
        * 실시간으로 유사도 측정  
        
      * 항목(제품) 기반 필터링  
        * 데이터양이 크고, 데이터 변경이 자주 일어나지 않는 경우  
          > ex] 책, 영화 등에 대한 추천  
        * 항목간 유사도를 저장하여 사용  
        
   * 느낌  
     협업 필터링 중 항목 기반 필터링 추천이  
     우리가 필요한 기능에 대한 인사이트를 제공해 줄 것 같음  
     
* [Collaborative Filtering - 추천시스템의 핵심기술](http://www.oss.kr/info_techtip/show/5419f4f9-12a1-4866-a713-6c07fd36e647)

  * Memory-based Methods  
    // 사용자/항목(제품) 기반 협업 필터링
    * 장점  
      > 구현이 간단하고 이해하기 쉽다.  
      > 유사도를 구하거나, 예상평점을 구하는 것이 머신러닝에 비해 쉬움  
        >> 대량 데이터 기반으로 모델을 만들고, 해당 모델의 평가를 하고, 최적화 하는 과정 X  
      > 단순한 몇 가지의 수식으로 쉽게 계산이 가능  
        >> 단순한 몇 가지의 수식을 뽑아내기 위한 규칙이 어렵긴 마찬가지일 듯  
        
    * 단점  
      * 평점에 대한 정보가 많지 않은 경우 예측의 정확도가 높지 않다.  
        > ex] 평점이 없는 책에 대한 적절한 평점은 ?  
      * 범위성 이슈  
        > 사용자나 아이템이 매우 많다면 실시간으로 사용하는데 계산시간이 오래걸린다.  
          >> 우리에게 해당되는 부분은?  

   * 사용자/항목(제품) 기반 협업 필터링 다시 살펴보기  
   
     * 사용자기반 협업 필터링(User-based Collaborative Filtering)  
       * 예제를 통한 고민 (user * 5, item * 5)  
         * 주어진 데이터  
           ![주어진 데이터](https://lh3.googleusercontent.com/h0-wTLZROUmvenG73IViFsC8YtvClh8TMraNIEk0RjABL3bn12-sGr2ubVS1H4N--QRXrZcFg-HRftxGKGul=w673-h683)  
         * 예시 시나리오  
           <User1 에게 Item5 추천 여부>  
           > User1과 비슷한 성향을 가진 사용자를 찾는다.  
           > 해당 사용자가 Item5에 준 평점이 높다면???  
           > User1 역시 Item5를 좋아할 것이다. **추천 적합**  
           
         * 유사도  
           **시나리오 속 이슈**
           User1과 유사한 성향을 가진 사람은 누구인가?  
           * 유사한 성향 -> 유사도(Similarity)  
             유사도가 높다는 것은 그만큼 서로 유사한 성향을 갖는다.  
             > 유사도가 높은 User 간 비슷한 Item 취향을 갖는다.  
             
           * 계산된 유사도를 이용해 가장 유사하다고 생각되는 User / Item 선정  
             > **선정된 것**은 사용자에게 추천 대상 결정 시 중요한 **척도**
             
           * 사용자/아이템 간의 유사도 계산  
             > ex] 피어슨 상관계수  
             
       * 피어슨 상관계수를 이용한 유사도 계산  
           ![피어슨 상관계수](http://oss.kr/oss/images/news/20160927_Recommendation_02.jpg)  
           
         * 방식  
           비교 대상이 되는 2개의 변수에 대해 선형적인 관계를 추정하는 방법  
           * 변수들의 값이 동일한 방향으로 움직이는 가 / 아닌 가  
             * 비례 관계  
             * 반비례 관계  
             * 관계 없음  
         
         * 피어슨 상관 계수 기반 유사도 측정 (User1, 비교 User)  
           * Sim(User1, User2) = 0.85  
           * Sim(User1, User3) = 0.70  
           * Sim(User1, User5) = -0.79  
           > User1과 가장 유사한 사람은 User2 이다.  
             
           * 이슈  
             User2의 Item5에 대한 평점은 3점이다.  
             > 하지만 user2의 Item5에 대한 평점을 User1에게도 적용할 수 있다고 생각하긴 어렵다.  
               >> User2의 평점 성향과 3점의 의미  
               >> User2가 평점을 낮게 주거나, 높게 주는 성향을 준다면 같은 3의 가치를 의미하진 않는다.  
               
             * Prediction Function  
             ![Prediction Function](http://oss.kr/oss/images/news/20160927_Recommendation_03.jpg)  
             
               * 방식  
                 * "(r_a) ̅  "의 값을 **기준값**으로 설정  
                 * 유사도에 따른 다른 사용자의 평점을 이용해 가중치를 조절  
                 * 최종적으로 User가 특정 Item을 좋아할지 말지 계산  
               > 뭔가 우리가 브레인 스토밍했던 내용인 듯 ????  
               > 하지만, 아직 구체적인 규칙화는 못하는 중  
                     
     
     * 아이템기반 협업 필터링(Item-based Collaborative Filtering)  
       User1에게 Item을 추천하기 위해, User1이 과거에 좋아했던 Item과 유사한 Item 추천  
       
       * Cosine Similarity  
       ![Cosine Similarity](http://oss.kr/oss/images/news/20160927_Recommendation_04.jpg)  
       
         * 방식  
           * 평점을 **벡터**로 생각  
           * **2개 벡터**사이의 각도를 계산  
           * **그 각도가 작을수록 가까이 있기 때문에 유사하다고 판단**
     
  * Collaborative Filtering 기반 추천 시스템 평가  
  
     * 평가 방법  
       * 사용자 평가 // 실 사용자 대상  
       * 온라인 평가 // 실 사용자 대상  
       * 오프라인 평가  
         > 과거의 데이터를 이용해 평가  
         > 데이터 기반의 평가이므로 따로 사용자가 필요하지 않음  
         
     * 오프라인 평가  
       > 평가방법과 평가항목을 표준화시킬 수 있기 때문에 자주 활용된다.  
       * 평가항목 예  
         Accuracy, Coverage, Confidence, Novelty  
         > 유의미한 테스트 셋이 중요해 질듯...  
         
  * 결론  
    ```
    Collaborative Filtering은 제안된지 20년이 지난 기술이지만 
    그 간결함과 성능으로 인해 아직도 많은 추천시스템에서 활용하고 있다.
    
    많은 변형 알고리즘과 방법들이 소개되었으며 
    별도의 메타데이터가 없어도 잘 동작한다는 점은 Collaborative Filtering의 장점이라고 할 수 있다.
    
    하지만 데이터가 적은 경우(Cold Start & Data Sparsity)에는 효과를 보여주기 어렵고, 
    Scalability 문제가 있다는 점 
    그리고 모든 도메인에 적용하기는 어렵다는 것은 Collaborative Filtering의 대표적인 단점으로 꼽힌다.

    많은 연구자들과 기업에서 Collaborative Filtering을 연구하고 있기 때문에 앞으로 더욱 발전된 방법들이 우리 앞에 나타날 것
    ```
    // Java 플랫폼에서의 추천 솔루션 생각해보기  


* 추가적인 자료들  
  * [추천 시스템 분석 - 어떻게 아마존과 넷플릭스 ...](http://blog.kthdaisy.com:8080/recommendation_system_kthdaisy/)  
    > 예시와 용어/개념  , 특히 **비즈니스 룰**    
    
  * [연관 규칙과 가중 선호도...](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.880.308&rep=rep1&type=pdf)  
    > 논문이라서 작업의 과정을 볼 수 있어서 도움됨  
  
  * [최범균님 자료](http://javacan.tistory.com/entry/Programming-CI-Study-ch2-RecommendationSystem)  
    > 생각할 거리들과 예시, 그리고 코드 도 볼 수 있음  
    
  * [메타데이터의 현재와 미래](https://blog.lgcns.com/700)  
    > 메타데이터라는 걸 정확히 집고 넘어갈 필요가 있음 // 추가적인 조사 필요  
    
  * [추천시스템 시리즈](https://bahnsville.tistory.com/894)  
    > 추천 시스템 구축 시 필요한 것들에 대해 다양하게 배경지식을 쌓을 수 있음  
    
  * [연관 규칙 관련 포스트](http://yamalab.tistory.com/86?category=747907)  
    > 연관 규칙이라는 
