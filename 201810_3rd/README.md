# 201810_3rd
## [��ٸ� ���� TDD ����](https://github.com/ddingcham/ladder)
* �⺻ TDD cycle ���� **Red-Green-Refactor**
* ��ü���� ��Ȱü��
* [�˸��ٹ� �ڵ� ���̵�](https://github.com/ddingcham/Alibaba-Java-Coding-Guidelines)

## 20181023
### ��ȹ
* �Է� / ��� ������ ��� �׽�Ʈ �ڵ� �ۼ�
* �׽�Ʈ ���
* �����丵 **���α׷��� �䱸������ ��� ��ų �ʿ�� ����**

### ���
* [���� commit�� ������� ���� �� �̽�](https://code.i-harness.com/ko-kr/q/e267e)

> `git reset --hard HEAD^1`

> ��ŷ ���丮�� �۾� ���뵵 ����� -> �ٸ� ��� ã��

* [Stream API�� ����� �� ���� ó��](https://kihoonkim.github.io/2017/09/09/java/noexception-in-stream-operations/)

* [TDD �н� �� Ŀ�� ���� ������ ��ũ](https://github.com/javajigi/minesweeper-ruby/issues/5)

* ��ɿ� ���� ���Ǹ� **��Ȯ�ϰ�** ���� �� �׽�Ʈ ���̽��� �ۼ�����...

> �� �� ���� �׽�Ʈ ���̽��� ���δ��� �ڵ带 �ۼ��ϰ� �־��� 

> ex: LEFT, STAY, RIGHT => �ְų�, ���ų�

> �̷����δ� ������, �����δ� ������ ����� ������ ������

> 1) ���ø����̼� ��ü���� �ƶ��� ���� �䱸���� �����ϱ�

> 2) ������ �ʿ��� �䱸������ �����ϱ�

> 3) ������ �ʿ��� �䱸������ ������� �׽�Ʈ ���̽� �ۼ��ϱ�

* ���δ� �ν��Ͻ� ������ 2�� ���� Ŭ������ ������ �� ����
> ��ó : thoughtworks anthology

```
class Name {
Surname family;
GivenNames given;
}
class Surname {
String family;
}
class GivenNames {
List<String> names;
}
```

* ������ ���� �ڵ� ���̵�, �ش� ���̵忡 ���� ���� �м��⸦ Ȱ���ϸ鼭 ���� ��

## 20181024
### ��ȹ
* �Է� / ��� ������ ��� �׽�Ʈ �ڵ� �ۼ�

> 1) ���ø����̼� ��ü���� �ƶ��� ���� �䱸���� �����ϱ�

> 2) ������ �ʿ��� �䱸������ �����ϱ�

> 3) ������ �ʿ��� �䱸������ ������� �׽�Ʈ ���̽� �ۼ��ϱ�

* �׽�Ʈ ���
* �����丵 **���α׷��� �䱸������ ��� ��ų �ʿ�� ����**

### ���
* Test �� Production �ڵ�� �ӽ÷� �׽�Ʈ �ϱ� ���� ���� Ȱ�� ....temp package�� �ۼ�

> �䱸���׿� ���� �͸� �ۼ��Ѵٴ� ���ڰ��信 ���� �ʿ�� ���� �� ����

* [Junit ���� �׽�Ʈ �ڵ� �ۼ��� (@Rule public TestName, @Before)](https://stackoverflow.com/questions/1548462/junit-before-only-for-some-test-methods)

> �� ��Ȳ�� �˸��� ����� �ƴҼ��� ������, Junit�� �������� �ٽ� ������

* Ȯ���� �Ϻ����� �ʴ���, �䱸������ ��Ȯ�ϰ� ������ ���鼭 �����ϴϱ� ����ϴ� �ð��� �پ��� ���� ����

> ���� ������ �� �ִ� �Ͱ� TDD�� ȿ���� ���� ������ �� �ִ� �Ϳ� ���� ������ �ʿ��� �� ����

* Test Doubles ���� [��ó](https://adamcod.es/2014/05/15/test-doubles-mock-vs-stub.html)

```
When To Use Test Doubles

There are two schools of thought on Test Doubles: Classical, and Mockist. Put simply, people who use the Classical style only use Test Doubles when there is a compelling reason to do so, such as external dependencies, whilst a Mockist would mock everything, all the time.

The advantage of using the Classical style is that you don't have to worry about internal behaviour of your objects, which is what OOP is all about. When you change an object and it's collaborators your tests will still pass, but this comes at the sacrifice of speed and isolation.

The advantage of using the Mockist style is that your tests are isolated and can run much faster, but at the sacrifice of coupling your tests to the internal behaviour of your objects and their collaborators.

At this point, no one way is the right way. Like all decisions in software development, it's up to you to use your judgement and select whatever method feels most appropriate for the given situation. Hopefully this post will have given you a solid foundation in the tools available to you, allowing you to make an informed decision next time you find yourself needing a Collaborator in a test.
```

> �׽�Ʈ �ڵ� �����丵 �� ����

## 20181025
### ��ȹ
* �Է� / ��� �� ������ ��ü ���α׷� �ϼ�
* �ϼ� �������� �ʿ��� �׽�Ʈ �ڵ� �ۼ� �� ���
* �ð��� �����ϸ� �����丵

### ���
* ù ��° ������ ������ �ϰ� ���� ��

> �������� ��ƴ�....

> Out -> In ������� �����ߴ��� ���ߴ����� Ƽ�� ����.

> ��ü���� �ƶ��� �䱸���� ������ ���� �ߴ���, �ƹ� �ƶ� ���� �׽�Ʈ ������ �̷�� ����.

> ����, �翬�� �͸� ���� In �� ���� �ϴٺ���, ��������� ��ü�� �����߰�, ���谡 ������ ���� ����� ���Ҵ�.

> �׳��� ��ü���� ��Ȱü���� �ڵ� ���̵������ �����ٸ�, �� ���������� ���̴�. 

## 20181026
### ��ȹ
* �䱸���� �м� �ٽ��غ���
* �Է� / ��� ���α׷� �����丵
* ������ �� �ٽ� �����غ���
* ������ �����丵 ���������� �׽�Ʈ �ڵ� ���ۼ�

### ���
* �ٽ� ����غ��� �� ���� _ ���� TDD ������ �ϴ� ���� (�߿�������, ������ �ƴ�)

> [�׽�Ʈ ������ Private �޼ҵ带 Public�޼ҵ�� ��ȯ�� Unit Testing�� ��� �ؾ��ϳ�?](https://www.slipp.net/questions/253)

> �����丵 ���� ������� �Ǵ� ���� �ڵ� ���� �� �������� �����غ� �ʿ䰡 ���� ��

> Ư�� [dongkuk ���� �亯](https://www.slipp.net/questions/253#answer-980)
�� �λ� ��� ��������.

> [���� ����-�ڹ�������� �õ� ���](https://www.slipp.net/wiki/pages/viewpage.action?pageId=6160426)

> � ��Ȳ���� �̷� �Ǵ��� ������ ���� �����غ� �� // ���� �������� �и�, ������ �������� �и�

> ������ �и� ���̽��� ���� ���� �ٸ� ������ ���� ���� �ٸ� ����� ���� �� ����.

> ������ ���� ���� ���� ���� � �䱸���׿� ���� �۾��� �ϰ� �ִ� �� // �̷��� �䱸������ ��� ������

> ���� �۾��� ���� �̷��� � ���̵� ����Ʈ�� �߻� ��ų��

> ���ٿ��� [��ϼ����� okkycon 2018 repository](https://github.com/yangwansu/okkycon2018)
 �� �ִ� �޽����� �ľ��ϴ� �͵� �ٽ� �õ��ϰ� �Ǿ���.
 
## 20181027
### ��ȹ
* ���� �ڵ� ��Ȳ���� �����ؾ� �� ���׵� ã��

### ���
* ���� ���� ã���鼭 ������ �ʿ��ϴٰ� ���� �Ǿ �����丵 ���� å�� �ٽ� �б�� �ϰ�, ������ .....

* ����

> ��Ȯ�ϰ� ����ȭ�� �䱸���׵��� ��� �׽�Ʈ�ϰ� �ִ°�? -> No

>> �׽�Ʈ �ڵ带 ������� ���δ��� �ۼ� ���� �ʾ���

> �׽�Ʈ ���� �ۼ��� ���δ����� �׽�Ʈ �ϱⰡ ���?

>> ��� �ڵ尡 �׽�Ʈ�ϱ� ���� ������ �ʾ���  
>> �� �� �ڵ���� �׽�Ʈ �ڵ带 �ٽ� �ۼ��Ϸ��� �ϰų�,  
>> �Ķ���Ϳ� ���ؼ� ������ �˰� �Ͽ��� �� �� �����ϱⰡ �������  
>> ���� ���� ��� �� �κе��� �׷� �� ����

## 20181028
### ��ȹ
* ���� ���׿� ���� ������ ���� �����丵 å�� Bad Smell �κ� ����
	[���� ���� ��ũ](https://github.com/ddingcham/TIL/blob/master/201810_3rd/Bad_Smell.md)

### ���

* ���� ���� 1 LadderConnection

```
/*
 * boolean connected => ���� ���� �� ĭ�� ���� ���θ� ����
 * ���� �� �ʿ� ���� ����..
 * boolean primitive ��ü�� �� ���� ���̽����� �����ϴϱ� ...
 * �ܺο��� ȣ���ϴ� generate() �� isConnected(), connected(), unConnected()
 * ,next() 4���� ��� ���ο� ���� ������ ������ ������� ���¶�� ���� 
 * ���� LadderConnection���� ������ ������ �� ����
 * LadderRow�� �� ������ ���� �ϴ� ���� �� �˱� ���ٰ� �Ǵ�
 */
 
 ```
