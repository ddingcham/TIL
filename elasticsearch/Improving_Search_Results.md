# Improving Search Results

#### overview : expect in any good search applications  
* the ability to **page** through a large result set    
* changing the **sort order** of the results  
* having the search terms **highlighted** in the response  
* recognizing search terms that are **spelled incorrectly**  
* searching for the title or author of a blog  

## Summary 

## Multi-field Searches  
#### 유저는 우리가 어떻게 필드를 모델링 했는지 모르고,
#### 우리는 유저의 검색 요청이 어떤 필드에 대한 검색을 원한 건지 항상 알 수 없음.  
> **multi_match** 쿼리는 복수의 필드에 대해서 쿼리를 적용하도록 지원  

### Query example  
~~~JSON
GET index/_search
{
  "query" : {
    "multi_match": {
      "query" : "query_value",
      "fields" : [ "title", "content", "writer" ],
      "type" : "best_fields"
    }
  }
}
~~~  
제목 필드에 대한 우선 순위를 부여해야하는 요구사항 스펙이 주어 진다면???  

#### title, content, writer 필드들 간에 가중치를 부여해야 한다면 ???

### Per_field Boosting  
> You can boost the **score of a field** using the caret (**^**) symbol  

~~~JSON
GET index/_search
{
  "query" : {
    "multi_match": {
      "query" : "query_value",
      "fields" : [ "title^2", "content", "writer" ],
      "type" : "best_fields"
    }
  }
}
~~~  
> "title^2"  

#### 히팅 목록의 elem들은 적용 안했을 때와 같지만, 점수의 분포에 따른 소팅 결과가 달라짐  

#### best practice ??
> **Experiment**, **analyze**에 의존 됨 -> **결국 사용자에게 가치를 주는 문제**  

### match_phrase  
"query" : "everything something"  

#### issue : everything 만 매칭되도 조회되는 검색 결과
> 너무 많음  

using match_phrase -> **"구"** 전체가 에 대한 매칭만 조회  

~~~JSON
GET index/_search
{
  "query" : {
    "multi_match": {
      "query" : "query_value",
      "fields" : [ "title^2", "content", "writer" ],
      "type" : "phrase"
    }
  }
}
~~~  

## Mispelt Werds  
#### deal with "misspelled words"  

### using fuzziness
* Fuzzy matching : fuzzily similar  
* fuzziness = 1 : "shark" can be assigned "shard".  
  > 'k' -> 'd' (convert 1 character)  
* fuzziness = 2 : "alocasion" can be assigned "allocation".  
  > '' -> 'l', 's' -> 't' (convert 2 characters)  
* can be set to 0, 1, or 2;  // can be set to **"auto"**  

#### Query example
~~~JSON
GET index/_search
{
  "query": {
    "match": {
      "content": {
        "query": "shark",
        "fuzziness": 1
      }
    }
  }
}
~~~

#### if Multiple terms (phrase) in the query
* "some query"
> fuzziness = 1 : "same qury" -> 'a' -> 'o', '' -> 'e'  
// **applied to each term**  

### Choosing the Fuzziness (0,1,2 ... auto)  

#### be aware of the side effect  
> fuzziness and short terms  
* fuzziness = 2, "hi" can be assigned  
  * "jim"  
  * "tim"  
  * "uri"  
  * "phil"  
  * "cj"  
  
"hi"라는 쿼리가 아무런 의미도 없어짐  

#### 확신이 없으면 auto를 활용 // fuzziness를 직접 정의하기 보다는  
> generates an edit distance  
**based on the lenghth of the query terms**  
> actually the preferred way **to use fuzziness**

## Searching for Exact Terms  
#### exact matches = index the string as keyword + search on the keyword field  


## Sorting  
> **_score** descending is the default sorting  
### The sort Clause  
> query can contain a **sort** clause that specifies one or more fields to sort on  
> "sort": [{"sort_field_A": {"order": "desc"}}, {"sort_field_B": {...}}, ...]  
  >> sort array  
#### Java High Level Client의 SortBuilder API 구조를 보면 쉽게 이해되는 부분  

### If _score 가 정렬 기준이 아니면?  
#### scores are not calculated for hits  
> 스코어 계산 역시 리소스를 잡아먹는 작업 // 필요없으면 없애자  
> ex] "hits": [{"_score": null}]  
  >> Each hit contains a "sort" // if used for sorting that hit  

## Pagination
### Be Careful with Deep Pagination  
#### if you want hits from 990 to 1,000  
#### 1,000 documents from each shard (ex] 5) -> coordinating node need to merge 5,000 hits  
#### using index.max_result_window  (default 10,000)  
> **from+size** can't be more than index.max_result_window  
> To avoid overwhelming a cluster  

## Highlighting  
> highlighting은 highlighting 이엇음 별 내용 없었음...  

#### 역시 실 사용에 대한 시나리오를 매칭하면서 공부해야 이해가 잘됨.
