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
```
GET index/_search
{
}
```

## Sorting  


## Pagination


## Highlighting




