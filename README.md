# kafka-project
kafka를 공부하는 리포지토리입니다. 

## kafka producer 
- kafka 로 메시지를 전달하는 역할 

## kafka consumer 
- kafka 에서 메시지를 읽어들이는 역할 
- Topic: 메시지 데이터의 구분을 할 수 있는 논리적 개념 
- Offset: Kafka Message 의 고유 번호. consumer 에서 메시지를 어디까지 읽었는지 확인하는 용도로 쓰임. 
- <log 예시>
```dockerfile

  * ### record: ConsumerRecord(topic = dev-topic, partition = 0, leaderEpoch = 0, offset = 1, CreateTime = 1649170434791, serialized key size = -1, serialized value size = 13, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = kafka message)
  * ### topic: dev-topic, value: kafka message, offset: 1
  
```
- `acknowledgment.acknowledge();` 
  - kafka 메시지 읽어온 곳까지 commit.
    이 부분을 하지 않으면 메시지를 소비했다고 commit 된 것이 아니므로 계속 메시지를 읽어온다
### 그룹 코디네이터 
- 컨슈머들은 하나의 컨슈머 그룹의 구성원으로 속하며, 컨슈머 그룹 내의 각 컨슈머들은 서로 자신의 정보를 공유하며 하나의 공동체로 동작함. 
- 컨슈머 그룹은 컨슈머 합류 등 변화를 인지하고 **각 컨슈머들에게 작업을 균등하게 분배** 해야 하는데, 이러한 동작을 컨슈머 **리밸런싱** 이라고 한다. 
- 카프카의 그룹 코디네이터가 이를 관리한다. 
  - 컨슈머 그룹이 구독한 토픽의 파티션들과 그룹의 멤버들을 트래킹하는 것 
  - 파티션 또는 그룹의 멤버에 변화가 생기면, 작업을 균등하게 재분배하기 위해 컨슈머 리밸런싱 동작이 발생 
  - 그룹 코디네이터는 각 컨슈머 그룹 별로 존재하며, 카프카 클러스터 내의 브로커 중 하나에 위치. 


### 애플리케이션 로그 분석 
```dockerfile
Cluster ID: Ao2ADftjS9io11lbT5GZpw
```
- 현재 Kafka 클러스터의 고유 ID 표시 

#### Kafka 컨슈머 그룹 조인 과정 
```dockerfile
Discovered group coordinator localhost:9092 (id: 2147483647 rack: null)
```
- `localhost:9092 `가 컨슈머 그룹의 코디네이터 역할을 함.
  - 그룹 코디네이터는 쉽게 말해 리밸런싱(작업 분배)을 관리한다. 
- 컨슈머가 그룹에 참여하기 위해 코디네이터를 찾음. 
```dockerfile
Successfully joined group with generation Generation{generationId=3, memberId='consumer-dev-group-1-3e4b359f-1409-4747-9d46-d1b96354f064', protocol='range'}
```
- 현재 generationId=3 → 컨슈머 그룹의 세 번째 세션.

#### 토픽 파티션 할당 
```dockerfile
Finished assignment for group at generation 3: {consumer-dev-group-1-3e4b359f-1409-4747-9d46-d1b96354f064=Assignment(partitions=[test-topic-0])}
```
- 컨슈머 consumer-dev-group-1이 test-topic-0의 데이터를 가져오는 역할

#### 오프셋 설정 
```dockerfile
Setting offset for partition test-topic-0 to the committed offset FetchPosition{offset=0, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 0 rack: null)], epoch=0}}
```
- 컨슈머가 어디까지 읽었는지 기억함 
- test-topic-0의 오프셋을 0부터 시작하도록 설정
- consumer 는 test-topic-0 할당 받았고 읽을 준비 완료 

## 시리얼라이저 
`key.serializer`
- 카프카에 쓸 레코드의 키와 밸류값을 직렬화하기 위해 시리얼라이저 클래스를 사용한다.

`value.serializer`
- 카프카 브로커는 메시지의 키값, 밸류값으로 바이트 배열을 받지만, 프로듀서 인터페이스는 임의의 자바 객체를 키 혹은 밸류로 전송할 수 있도록 매개변수화된 타입을 사용할 수 있게 한다.
- 프로듀서 입장에서는 이 객체를 어떻게 바이트로 바꿔야 하는지 알아야 한다.

```dockerfile
// 메시지의 키값과 밸류값으로 문자열 타입을 사용하므로, StringSerializer 사용했다.
		kafkaProps.put("key.serializer",
				StringSerializer.class.getName());
		kafkaProps.put("value.serializer",
				StringSerializer.class.getName());

```
### 콜백
- 메시지는 비동기적으로 전송되지만, 메시지 전송에 완전히 실패했을 때를 위해 에러 처리가 필요하다.
- 에러 처리를 위해 프로듀서는 레코드를 전송할 때 콜백을 지정할 수 있도록 한다.

## 에이브로 시리얼라이저

## 카프카 구조 이해하기 
- 카프카는 클러스터 -> 브로커 -> 토픽 -> 파티션 -> 세그먼트로 구성

<img width="761" alt="Image" src="https://github.com/user-attachments/assets/507dcae2-1e80-411e-b7c3-b94f16321981" />
### 리더 선출 


### Raft 알고리즘 

### 트러블 슈팅
- avro 전송 오류 
- avro 란
- 장점
  - 스키마를 통해 데이터 구조 및 타입을 알 수 있다.
    데이터 압축
    스키마 변경에 유연하게 대응 가능

## 이슈 
### 질문1
electLeader() 메서드는 두 가지 서로 다른 형태의 리더 선출을 할 수 있게 해줌.
선호 리더 선출과 언클린 리더 선출 중 언클린 리더 선출은 무엇일까?

### 답변1 
우선 리더 선출과 관련해서 N개의 파티션 중에 리더 파티션은 단 하나만 존재하며 나머지 파티션은 팔로워(Follower) 파티션이 되어 사용자가 replication-factor로 지정한 수 만큼의 Replica를 구성합니다. 이러한 Replica를 구성하는 여러 방식 중에 카프카는 효율적이고 간단한 시스템 구축을 위해 '클라이언트와 리더 파티션 간의 1:1 커뮤니케이션' 을 합니다.

Partition Leader Election은 '리더 파티션을 담당했던 브로커에 장애가 생겼을 때 해당 리더를 어떻게 대체할 것인가?'에 대한 내용입니다.
unclean.leader.election.enable 옵션은 ISR(in-sync replica)가 아닌 OSR(out-of sync replica)를 가지고 있는 broker를 leader로 선출 할 수 있도록 설정합니다. (리더 파티션은 팔로워 파티션의 pull 요청의 마지막 Offset 값을 활용하여 각 팔로워 파티션의 LAG을 체크합니다.)

최초에 Preferred Reader(선호 리더)라 Leader 가 되고, 나머지 파티션은 Follwer 가 됩니다. 여기서 Preferred Leader란 토픽이 처음 생성될 때 리더였던 Replica를 말합니다.

그런데, 만약 리더 브로커가 죽고 다른 모든 Replica도 ISR 상태가 아니라면 어떻게 될까요?

해당 파티션의 리더가 없게 되고 시간이 지날수록 데이터의 유실이 발생합니다. 따라서, Offset 차이만큼의 데이터 유실을 감수하고서라도 Out of Sync 상태의 Replica를 강제로 리더로 선출하게 만들어 버릴 수 있는데, 이를 Unclean Leader Election이라고 합니다.