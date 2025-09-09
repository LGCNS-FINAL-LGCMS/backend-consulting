# Backend-Consulting

---

# 🖥️ 서버 설명

- 플랫폼에 제공되는 콘텐츠의 품질을 높이기 위해 강사에게 강의에 대한 피드백을 제공하는 서버입니다.
- 강사 대시보드 : 강사는 강의에 대한 지표(수익 현황, 수강생들의 수강 현황)들을 대시보드로 확인 해 볼 수 있습니다.
- 강사 레포트 : 강의의 Q&A와 수강생들의 리뷰를 바탕으로 강사의 장단점을 분석하고 개선점을 제공합니다.
- 패키지 구조
    
    ```
        ├─main
        │  ├─java
        │  │  └─com
        │  │      └─lgcms
        │  │          └─consulting
        │  │              ├─advice
        │  │              ├─api
        │  │              ├─common
        │  │              │  ├─annotation
        │  │              │  ├─aspect
        │  │              │  └─dto
        │  │              │      └─exception
        │  │              ├─config
        │  │              │  ├─ai
        │  │              │  ├─batch
        │  │              │  │  └─utils
        │  │              │  └─redis
        │  │              ├─domain
        │  │              ├─dto
        │  │              │  ├─projection
        │  │              │  ├─request
        │  │              │  │  ├─dashboard
        │  │              │  │  └─lecture
        │  │              │  └─response
        │  │              │      ├─dashboard
        │  │              │      ├─lecture
        │  │              │      └─report
        │  │              ├─repository
        │  │              └─service
        │  │                  ├─ai
        │  │                  │  └─tools
        │  │                  ├─dashboard
        │  │                  └─internal
        │  │                      └─lecture
        │  └─resources
        └─test
            └─java
                └─com
                    └─lgcms
                        └─consulting
    ```
    

---

# **👨🏻‍💻** 담당자

| 이름 | 역할 |
| --- | --- |
| 남윤호 | 백엔드 개발 |
| 이재원 | CI/CD, 모니터링 |

---

# 🛠️ 기술 스택

### Languages

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)

### Framework
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring_AI-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Batch](https://img.shields.io/badge/Spring%20Batch-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

### Middleware

![OpenTelemetry](https://img.shields.io/badge/OpenTelemetry-FFB01F?style=flat-square&logo=opentelemetry&logoColor=black)

### Database

![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)

---

# 📌 기능

### ✔️ 데이터 가공 배치

- Airflow → API 서버: 스케쥴링 된 시간에 REST API로 서버에 Batch Job 실행을 요청합니다.
- API 서버 → 강의 서버: Fegin으로 필요한 데이터를 요청합니다.
- 강의 서버 → API 서버: 데이터에서 필요한 column만 추출해서 전송합니다.
- API 서버 → Airflow: 데이터를 저장하는 작업이 끝나면 Job이 완료되었다고 전송합니다.
- Airflow → API 서버: 차트에 맞게 데이터를 가공하는 Job 실행을 요청합니다.
- API 서버: 데이터를 가공하는 Job을 실행합니다.

### ✔️ 강사 대시보드

- 강사 → API 서버: 각 차트에서 조회할 조건(특정 강의명, 기간)들과 함께 서버에 대시보드 차트에 들어갈 데이터를 요청합니다.
- API 서버: 조건에 맞는 미리 가공해둔 차트용 통계 자료를 조회하고 반환합니다.

### ✔️ 강사 레포트

- 강사 → API 서버: 서버에 피드백 레포트를 요청합니다.
- DB를 조회해서 이미 작성된 레포트가 있다면:
    - API 서버: 작성된 레포트를 반환합니다.
- 없다면:
    - API 서버 → LLM API: 레포트에 분석하고 싶은 데이터들과 함께 레포트 작성을 요청합니다.
        - 강의 질문 목록
        - 수강생 수강평
    - API 서버: 작성된 레포트를 DB에 저장하고 반환합니다.

---

# **📜** 주요 기능

### 대용량 데이터 배치 처리

- 매일 데이터 배치 처리를 통해 대시보드에 필요한 통계 데이터를 미리 가공
- JVM 21과 Spring Batch 5에서 공식 지원하는 virtual thread를 적용해서 청크 병렬 처리로 성능 최적화

### Spring AI를 통한 Amazon Bedrock 호출

- Spring AI로 따로 Python환경을 구축할 필요 없이 간편하게 기존 Spring 환경에서 LLM API 호출 가능

### Redis 기반 분산락

- LLM 호출시 분산 락을 통해서 불필요한 토큰 낭비 방지

### Custom Metric을 통한 토큰 사용량

- AOP기반 Custom Metric을 도입해서 LLM을 호출할때 마다 토큰 사용량을 자동으로 메트릭으로 기록

---

# ⚡ 트러블슈팅

### JPA Item Writer 성능 이슈

- 문제 :
    - Batch 성능이 100만건 밖에 안되는데도 3~5분 걸려서 생각보다 성능이 안나오는 이슈가 있었습니다.
- 원인 :
    - JPA 기반 ItemWriter는 기본적으로 영속성을 위해 아이템을 전부 조회하고 insert하는 과정을 가집니다.
    - 그리고 Bulk Insert를 설정했음에도 적용되지 않는 이슈가 있었습니다.
- 해결 과정:
    - 처음에는 JPAItemWriter에서 설정을 merge 대신 persist로 바꾸니 필요없는 조회과정이 사라져서 정확히 실행시간이 절반으로 줄었습니다.
    - 그럼에도 불구하고 성능이 안좋다는 생각이 있었고 Jdbc기반의 JdbcBatchItemWriter와 비교해 보았더니 Bulk Insert가 제대로 동작하지 않는다는 사실을 발견했습니다.
    - Jdbc기반으로 변경하니 동일 조건에서 6분 → 15초까지 시간을 줄일 수 있었습니다.
    - 거기에 virtual thread도 한스푼 추가하니 거의 1초까지 줄어듦을 확인할 수 있었습니다..

### LLM 호출시 따닥 이슈

- 문제 :
    - 레포트 요청을 계속 보내면
- 원인 :
    - 지금은 단순히 비즈니스 로직에서 DB에 생성된 레포트가 있나 체크하고, 있다면 LLM을 호출하지 않고 있는 데이터를 반환하게 짜놨습니다.
    - 하지만 LLM을 호출해서 레포트를 작성하는 동안에 동일한 강사의 요청이 온다면 아직 DB에 생성된 레포트가 없기 때문에 생성되기 전까지 계속 요청을 중복해서 처리하고 있었습니다.
- 해결 과정:
    - Redisson 기반의 분산 락을 구현해서 적용하니까 중복 호출 문제가 해결됐습니다!

### 토큰 사용량 메트릭 기록

- 문제 :
    - 처음에 커스텀 메트릭을 기록할 때 다른 서비스들의 비즈니스 로직이 구현이 끝난 상태여서 제가 만든 커스텀 어노테이션을 적용하기가 까다로운 상태였습니다.
- 원인 :
    - Spring AI 에서는 다양한 .chatresponse, .content, .entity와 같이 다양한 형태로 호출한 결과 값을 받을 수 있습니다.
    - 하지만 토큰 사용량 계산을 위해서는 반드시 ChatResponse 에서 토큰 사용량을 받아와야 했고, 처음에 컨벤션을 따로 정하지 않았기 때문에 전부 다양하게 비즈니스 로직을 구현해둔 상태였습니다.
- 해결 과정:
    - AOP pointcut 중 execution을 통해 직접적으로 LLM 호출이 일어나는 call 메소드에서 ChatResponse를 인터셉트해서 계산하는 방법으로 해결했습니다.
    - 다른 팀원들은 비즈니스 로직 코드 수정없이 토큰 사용량을 추적할 수 있게 되었습니다.

---

# 💡 느낀점

- Spring AI 를 처음 써봤는데 다른 AI 프레임워크 들을 써보지 않아서 정확한 비교는 불가능 하지만 장단점이 명확했습니다. 장점은 기존 Spring 환경에서 간단하게 써볼 수 있다는 것이였으나 복잡한 Agent구성이나 아직 지원되지 않는 AI 모델이 많다는 점, 그리고 이슈들이 꽤나 많아서 본격적으로 Agent 개발을 공부한다면 차라리 Python에서 도전할 것 같습니다.
- 배치 성능 테스트를 제대로 못해 본 점이 아쉽습니다. Redis 기반으로 조회성능을 올려보는 것도 비교해 보고 싶었지만 그러지 못한 점도 걸리네요. 이번에 처음 접해봤지만 대규모 데이터를 다루는 것이 어렵기도 하고 재밌었습니다. 더 공부해보고 싶은 것 중 하나입니다.
- 배치 최적화를 하는 과정에서 전부 native query 작성이 많았는데 작성하는 것도 힘들고 나중에 관리하는 것도 힘들었습니다. QueryDSL도 공부하고 싶습니다.
- Agentic AI 로 유의미한 피드백을 제공하는 레포트를 꿈꿨지만 지금 나온 결과물은 사실 Agent의 탈을 쓴 단순한 데이터 파이프 라인이라는 생각이 듭니다. 추후 고도화를 하게된다면 꼭 이 부분부터 손대고 싶어요.
- virtual thread는 정말이지.. 너무 좋네요
- Kafka를 이용한 부분이 없다는 게 정말 너무 아쉽습니다. 대시보드에 마이크로 배치를 적용할 곳을 찾아봤어야 했는데..
