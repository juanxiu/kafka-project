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