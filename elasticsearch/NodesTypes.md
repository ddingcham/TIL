# Nodes Types

#### Node Configuration은 elasticsearch.yml에 정의 -> static 하게만 적용 가능  

## Node Roles  
* **Master eligible(선출)**  
  * configuration    
    > node.master : true(default)  
* **Data**  
  * configuration    
    > node.data : true(default)  
* Ingest  
  * configuration    
    > node.ingest : true(default)  
* **Coordinating only**  
* Machine Learning  

#### 각 노드들은 복수의 역할군 수행 가능  

## Cluster State and Master Nodes

### Cluster State
> maintaining details of a cluster  
  >> nodes in cluster, indices, mappings, settings, shard allocation, ....  
  >> with Get _cluster/state : _cluster endpoint  
  
* Cluster State on **each node**  
  > 마스터 노드에서만 상태 변경(update) 가능  

### Master Node
#### 모든 클러스터는 "단 하나"의 마스터 노드를 갖음  

* charge of cluster-wide settings and changes  
  * creating or deleting indices  
  * adding or removing nodes  
  * allocating shards to nodes  
  
* Master-eligible Nodes  
  > node.master = true 인 노드가 복수 개 존재하면? // 복수인 이유 : 마스터가 죽으면 대체할 노드가 필요하니까  
  * Master-eligible Node는 Master Node 투표권을 갖는다.  
    > Master-eligible 권한은 Master 역할과 똑같이 node.master = true 값으로 결정
  * Master-eligible Nodes Election  
    * **discovery.zen.minimum_master_nodes** setting  
      > ex] set N/2+1   // N = number of master-eligible nodes  
    #### discovery.zen.minimum_master_nodes 값은 Split Brain 이슈 때문에 매우 중요  
    #### 참고 : [Split Brain](https://github.com/byrage/TIL/blob/master/elasticsearch/concepts.md)  
    
### High Availability of the Master Node  
* Configuring minimum_master_nodes  
  * recommend 3 master-eligible nodes  
    > **with minimum_master_nodes set to 2**  
  * config elasticsearch.yml  
    * cluster stability : **extremely important setting**  
      > to avoid a **split brain**  
      
* 마스터 노드 고가용성 보장 컨셉 (ex] 3개 시나리오)  
  * 복수(3개) master-eligible node들은 master-node가 장애 시 2개의 백업 보장을 의미  
  * 남은 master-eligible node끼리 투표 // 득표수가 minimum_master_nodes를 넘을 때까지  
  * 장애 상태였던 이전 마스터 노드가 복귀해도, 마스터 롤은 현재 수행중인 노드가 그대로 유지  


## Data Nodes  

#### All nodes are data nodes by default  

### Features  
* **hold the shards that contain the documents indexed**  
* **execute data related operations**  

#### 일반적으로 클러스터 내의 I/O, CPU, Memory 관련 이슈와 직접적인 연관  
#### 모니터링을 통해 적절한 숫자의 data node 관리 필요  


#### provides a nice separation of the master and data roles  
> 각 노드 간의 책임 분리가 지원되는 구조여서 각 시스템에 적합한 노드 구조 설계 가능  

## Coordinating Node
#### Client request를 receives & handles  
#### forwards the request and combines results  
> 프론트 서버같은 느낌   

## Sample Architectures  
### 1-Node Cluster  
> development and testing  
> no-high-availability  

### 2-Node Clusters  
> **potential for split brain**  
#### important : one master-eligible (no-high-availability) OR minimum_master_nodes = 2  

### 3-5 Node Clister  
#### 3 Master-eligible Nodes & 5 Data Nodes  
> should set **minimum_master_nodes=2**  

### Larger Case  
> 일반적으로 master-eligible = 3 그대로 유지  
> data nodes 만 필요에 따라 추가 구성  

## See also : [Hot/Warm Architecture](https://github.com/byrage/TIL/blob/master/elasticsearch/concepts.md)  
