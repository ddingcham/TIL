# Understanding Configuration Settings  

* configuration levels  
  * index  
    > typically configured using **REST APIs**  
  * node  
    > typically configured in **elasticsearch.yml or command line**  
  * cluster  
    > typically configured using **REST APIs**
  
* Index Level include  
  * number of shards  
  * number of replicas  
  * refresh rates  
  * read-only  
    #### 참고자료 (각각의 항목들 그래프용) : [Tune for indexing speed](https://www.elastic.co/guide/en/elasticsearch/reference/current/tune-for-indexing-speed.html)

* Node Level include  
  * file paths  
  * labels  
  * network interfaces  

* Cluster Level include  
  * logging levels  
  * index templates  
  * scripts  
  * shard allocation  
  
#### 설정 레벨에 대한 선/후행 조건  
> 위에서 부터 아래로 커버  
* **Transient settings**  
* **Persistent settings**  
* **Command-line settings**  
* **Config file settings**  

#### not all settings can be updated via the API  
* **static settings** : elasticsearch.yml ...  
* **dynamic settings** : live cluster에게 Cluster Update API를 활용해서 적용 가능  
