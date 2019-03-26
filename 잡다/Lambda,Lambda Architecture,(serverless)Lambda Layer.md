# Lambda,Lambda Architecture,(serverless)Lambda Layer  

## Lambda  

## Lambda Architecture  
> Nathan Marz with James Warren :   
> Big Data Principles and Best Practices of Scalable Realtime Data Systems :  
> 1.7 Lambda Architecture  

* 큰 데이터를 레이어를 쪼개서 다루기  
  > 각 레이어가 꼭 단일 계층/컴포넌트 로 구성될 필요는 없음  
  > 필요에 따라 추상화시켜서 구성하면 됨  

* Big Data systems as a series of layers  
  * **query = function(all data)**  
    > running the functions on the fly to get the results  
    > **it would take a huge amount of resources to do** : expensive  
  * **Batch layer**  
    * **batch view = function(all data)**  
      > alternative approach - precompute the query function  
    * **query = function(batch view)**  
      > reading the results from the precomputed view  
      > reducing random access cost   
    * ex]  
      > storing events viewing page  
      > computing pageviews per day via batch  
      > analyzing something to use pageviews precomputed  
    * 전체 데이터에 대한 엄격한 불변식은 느림 - 느리니까 batch 로 precomputing  
      > 해당 서비스 목적에 맞게 모델링된 마스터 스토리지를 구축한다고 보면 됨  
    * concurrency challenges of scheduling work and merging results  
      * MapReduce cluster (arbitrarily distributed)  
        > Scaling to however many nodes you have available  

  * Serving layer  
    * Random access to batch views  
    * Updated by batch layers  
    * random write를 지원할 필요가 없음 -> designing can be simple  
    
  * Speed layer  
    * 최근 이슈에 대한 것만 처리  
      > 탐색 범위가 좁아짐  
    * 속도 측면이 주 이슈이므로, 데이터에 대한 불변식을 엄격하게 지킬 필요 없음  
      > 나중에 배치 layer에서 다시 업데이트  
    * **batch view = function(all data)**  
    * **realtime view = function(realtime view, new data)**  
    * **query = function(batch view, realtime view)**  
    * database that support random read and random writes  
      > 어차피 새로운 추가되는 데이터를 임시적으로 다루므로, 공간의 효율성 보다는 속도의 효율성  
  * ![](https://www.irisidea.com/wp-content/uploads/2017/06/Lambda.jpg)  
    > [Why Lambda Architecture in Big Data Processing](https://www.irisidea.com/lambda-architecture-big-data-processing/  )  
    > 각각의 레이어 별로 뷰는 여러 개가 될 수 있음  

## Lambda Layer (serverless)  
