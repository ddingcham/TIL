# [ELK 스택 데이터 분석](https://www.inflearn.com/course/elk-스택-데이터-분석)  

## ELK 스택 전체적인 기본 그림  
(WildFly, MySQL, ActiveMQ, mongoDB) -> logstash -> elasticsearch -> kibana  

#### 어떤 데이터든지
* 수집하고  
  > 어떤 형태(ex] csv)의 데이터든 logstash가 수집해서 elasticsearch에 저장  
* 분석하고, 검색하고  
  > elasticsearch를 통해 분석, 수집  
* 시각화하고  
  > 프론트엔드 코드 하나도 없이 kibana를 통해 시각화  


## elasticsearch 기본 개념  
특정 text에 대해서 HashTable 기반 저장  
인덱스(Document에 보관)를 통해서 조회 가능 // O(1)  
#### 서치 관점에서 매우 빠름  

Index : Database  
Type : Table  
Document : Row  
Field  : Column  
Mapping : Schema  

거의 실시간 개념  

Http Verb (REST_JSON)  
/(Index)/(Type)/(id)  

## Bulk POST  

벌크를 위해서 크게 두 개의 제이슨 오브젝트를 다룸  
* meta 정보가 담격있는 "index" 오브젝트  
* 실제 벌크하려는 도큐먼트 관련 오브젝트  

벌크 옵션을 주게 되면 (meta와 도큐먼트) 쌍 데이터를 여러 쌍 삽입 가능  
=> HTTP 요청을 위한 network i/o는 1회  
=> 리퀘스트 바디에 삽입할 쌍들을 넣어서 요청  
  => 이것도 한계가 있을 듯  
  
#### 사실 JSON 으로 된 데이터만 있으면 엄청나게 쉬움  
#### elasticsearch에 벌크하려는 meta와 도큐먼트 구조에 맞게 JSON화 시키는 게 문제일 뿐  
#### Spring Batch 의 page, chunk 개념이 Spring Data elasticsearch와 어떻게 연동되는지가 중요할 듯  
> 응 아니야 LogStash도 좋은 솔루션이야  

## Search  

## Metric Aggregation  

## Bucket Aggregation  
