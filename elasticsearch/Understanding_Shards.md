# Understanding Shards

## Shards Overview  
#### an index is "split" into shards before any documents are indexed.  
// 인덱스는 가상의 네임스페이스 개념 (샤드들을 포인팅하기 위한)  

* 샤드의 종류  
  * primary  
    > 원본  
  * replicas  
    > primary 복사본  
    
  #### 각각의 노드들이 모든 샤드들을 갖고 있는 모델이 아님 !!!!!!!!!!!

  #### a primary and all replicas are guaranteed to be on different node  
  > primary와 레플리카는 각각 다른 노드에 존재하는 게 보장됨  
  
  #### node가 하나인 cluster 내에서는 replica에 대한 보장을 받을 수 없음  
  > 당연히. 레플리카랑 프라이머리는 같은 노드에 존재할 수 없음  
  
### Scaling Elasticsearch  

#### cluster에 새로운 node가 추가되면  
* 샤드들 재분산(redistribution, 복구일 수도 잇음)  
* **현재의 노드들의 상황에 맞춰서**  
  > 레플리카 생성  
* **master node**가 샤드들의 분산 상태를 정의  

### configuring the Number of Shards 
#### 인덱스 생성 시에만 !!!!!! 프라이머리 샤드의 갯수를 지정할 수 있음  
#### 레플리카의 숫자는 언제나 변경 가능  
-> It is costly change the number of shards, so choose wisely!  

### Why Create Replicas?  
#### Read throughput  
* A query can be performed **on a primary or replica shard**  
* 데이터 스케일링과 클러스터 자원의 사용을 용이하게 하는 데 도와줌  
  > 아직 와닿진 않음..  
  
#### High availability  
* 노드는 손실될 수 있기 때문에, **데이터 가용성**을 위해서 레플리카는 당연히 필요  
* **프라이머리 샤드**를 갖는 노드가 손실(상)되면, **레플리카 샤드들** 중 **새로운 프라이머리 샤드** 선택  

## Anatomy of a Write Operation

### How Data Gets In -> 클러스터에 "어떻게" document가 색인되는 지    
> 앞에서 정의된 세계관 : a primary and all replicas are guaranteed to be on different node  
#### 각각의 프라이머리 역할의 샤드들은 여러 개의 노드들에 분산 저장  

#### Document Routing  
> 도큐먼트 색인 시 **샤드 선정 방식**  
* simple formula  
> shard = hash(_routing) % number_of_primary_shards  
> 이런 식이라는 것만 알아둘 것  

**결론 : Put index/_doc/doc_id -> hash(doc_id) % number_of_primary_shards**  

#### 프라이머리 샤드에서의 쓰기 작업 (ex] index, delete, update document)  

* basic workflow  
  * **primary shard**에 **최우선으로 작업**  
  * 해당 primary shard에 대한 replicas 와의 Sync 방식    
    #### 문서 색인 요청을 모든 레플리카 샤드에 포워딩 (parallel 하게)  
    > "I will wait until you are done replicating." -> give me acknowledge !  
  * **레플리카 쓰기 작업에 대한 응답 수신**(acknowledge)  
  * **coordinating 역할을하는 node**에게 쓰기 작업 결과 전달  
    > 쓰기 요청을 한 **client application**에게 작업 결과에 대한 세부 내역 전송  
    
* **Updates and Deletes**  
  > 기본적으로 쓰기 작업(indexing)과 비슷  
  #### current version of the document is deleted  
  #### (작업을 한)분산된 전체 문서들에 대한 새로운 버젼 색인  
  
## Anatomy of a Search  

### 분산 검색 작업의 어려움  
* 검색하려는 인덱스 내에 모든 샤드들을 검색해야 한다.  
* 소팅/페이징 작업 시 각각의 샤드의 검색 결과가 분산된 모델  
  #### 각각의 샤드에서의 소팅/페이징 작업 결과는 전체에 대한 결과가 아님  
  
#### The Query Phase  
> // 검색 요청에 대한 **전처리**  

* basic workflow  
  * 쿼리를 모든 shard에 대한 replica 들에게 브로드 캐스팅  
  * **excutes locally** : 각각의 레플리카 샤드들은 쿼리를 실행  
  * 각각의 샤드들은 doc_id 와 "정렬 기준값"(sort values)들을 coordinating node에게 리턴  
  * coordinating node 는 sort value들의 merge 를 통해 **globally sorted list of results** 생성  
  * **globally sorted list of results** 내에 있는 doc_id를 갖고, 실제 document들을 **fetch**  
  
* _score 계산 관련 이슈  
  > locally ? globally ?  

  #### "_score"는 "term frequency"(TF)와 "inverse document frequency"(IDF) 기반으로 생성  
  #### 필독 : [TF, IDF 배경 지식](https://ko.wikipedia.org/wiki/Tf-idf)  

  * TF 는 도큐먼트 레벨에서 도출되므로 shard-locally 여도 이슈가 없음  
  * IDF는 전체 문서에 상대적인 결과이므로 shard-locally 하면 이슈 발생  
    ```
    역문서 빈도는 한 단어가 문서 집합 전체에서 얼마나 공통적으로 나타나는지를 나타내는 값이다.
    전체 문서의 수를 해당 단어를 포함한 문서의 수로 나눈 뒤 로그를 취하여 얻을 수 있다.
    ```
  
  #### 걍 결론 부터 말하면 : Why no compute IDF globally for every search -> Because that would be expensive !  
    > IDF는 모든 샤드를 대상으로(shard-globally) 각각의 히트들에 **의존하는** 계산 결과
  
  #### 비용에 대한 변명 : Using local IDF is rarely a problem, especially with large datasets  
    > 매우 소수의 케이스를 제외하면 local IDF 도 거의 비슷한 결과를 나타낸다는 듯  
    > 할 수는 있음 : GET index/_search?search_type=dfs_query_then_fetch  
      >> 주로 **작은 데이터셋**에 대한 **테스팅**이나 **디버깅**에 유용  
      >> 프로덕션에도 적용이 필요할 경우는 진짜 거의 없다고 함  
            
## How many shards do I(my index) need?  

#### 이슈별 고려(점검) 대상들  
* Heavy indexing ?  
  > **more nodes**와 **primary shards 갯수**를 고려  

* Heavy search traffic ?  
  > Increase the number of replicas  
  > 필요하다면, 노드들의 숫자도  

* Large dataset ?  
  > enough primary shards 점검  
  > **10-40GB** 각각의 샤드가 감당할 데이터 셋 규모 권장스펙  

* Small dataset ?  
  > 하나의 샤드로도 감당 가능한가?  

#### 엘라스틱 서치의 장점 중 하나가 변경에 용이하고, 모니터링 기능이 좋은 것 이므로, // elastic stack echosystem 이 잘 되어있음
#### 그에 대비를 한 인덱스 모델(논리적 모델)을 구축 한다면  
#### 샤드의 숫자는 초기엔 여유 있게 설계 후 모니터링을 통해 조정해 나가면 될 듯  
    
