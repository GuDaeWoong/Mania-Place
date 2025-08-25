<img width="512" height="381" alt="image" src="https://github.com/user-attachments/assets/2b21e1b5-9618-42d5-9a4c-f356cf7b09b5" />

### ·•· 마니아 층을 위한 중고거래 플랫폼 ·•·

## 🗂️ 목차
| [👀 서비스 개요](#-서비스-개요) | [🗝️ 핵심 기능](#핵심-기능) | [🖇️ 시스템 아키텍처](#시스템-아키텍처) |

| [📐 설계 문서](#-설계-문서) | [🛠️ 기술 스택](#기술-스택) | [🌊 서비스 플로우](#-서비스-플로우) |

| [💡 의사결정 및 기능 구현](#-의사결정-및-기능-구현) | [⚡️ 성능 개선](#성능-개선) | [🚨 트러블 슈팅](#-트러블-슈팅) |

| [📅 일정](#-일정) | [☕ KPT회고](#-kpt회고) | [🧑‍💻 팀원 소개](#-팀원-소개) |


## 👀 서비스 개요
### Mania Place
**요즘 애니메이션은 '문화'입니다.** 

하지만 애니메이션을 사랑하는 덕후들은 여전히 **정보 수집, 소통, 굿즈 거래, 커뮤니티**

등 일상 속 불편함을 겪고 있습니다.


<br><br>
🎬 매일매일 "새로 나온 재밌는 애니 굿즈 상품은 없을까?" 궁금하지만,

**여러 사이트를 돌아다니며 찾기엔 너무 번거롭고...**


<br><br>
♻️ 한 번 보고 마는 아크릴 스탠드, 안 맞는 피규어, 중복 굿즈들...

**중고 거래는 사기 걱정도 되고 너무 귀찮고...**


<br><br>
🛍️ 한정판 굿즈 놓치고 싶지 않은데

**어디서 언제 나오는지 정보가 다 흩어져 있고...**


<br><br>
🔥 지금 사람들이 가장 많이 찾는 건 뭘까?

**실시간으로 확인할 방법이 없으니, 트렌드를 놓치기 일쑤...**


<br><br>
🤔 "이 굿즈 살까? 말까?" 혼자 고민만 하다가

**결국 충동구매로 후회하거나, 망설이다 놓치거나...**


<br><br>
> 이런 불편함을 해결하기 위해,  
> **하루 한 번, 애니메이션과 더 가까워지는 플랫폼**을 만들었습니다.


---

## <h2 id="핵심-기능">🗝️ 핵심 기능</h2>

**거래자 간 채팅 시스템**

### 실시간 소통 기능

- 굿즈 거래 전 상품 상태 및 가격 협상
- 거래 조건 및 배송 방법 논의

### 안전한 거래 환경

- 검증된 websocket과 stomp통신을 통해 안전한 거래 환경 제공
- 수평 확장을 고려한 메세지 브로커 설계로 안정성 제공

---

**분산락을 통한 중복 거래 방지**

### 동시 거래 제어

- 같은 상품에 대한 동시 구매 요청 방지
- 선착순 거래 시스템으로 공정한 거래 보장
- 결제 진행 중 다른 사용자의 접근 차단
- 거래 취소 시 즉시 다른 사용자에게 기회 제공

### 시스템 안정성

- 서버 과부하 상황에서도 안정적인 거래 처리
- 데이터 정합성 보장으로 거래 오류 최소화
- 한정판 굿즈 판매 시 시스템 안정성 확보

---

**관심사 태그 시스템**

### 맞춤형 콘텐츠 큐레이션

- 개인 관심사 태그 설정 및 관리
- 태그 기반 맞춤 상품 추천

### 태그 기반 필터링

- 굿즈 검색 시 관심 태그 우선 표시

---

**구매 고민 상담 페이지**

### 커뮤니티 기반 상담

- "살까말까" 고민 게시판 운영
- 다른 덕후들의 솔직한 구매 조언
- 상품별 장단점 및 후기 공유

### 투표 시스템 세부 기능

- **좋아요 / 싫어요** : “구매 추천 여부” 실시간 집계
- **투표 이유**: 재미 또는 구매를 갈등하는 유저를 위한 좋아요/싫어요

### 댓글 시스템

- 좋아요 싫어요 이외에도 상품 구매하는 것에 대한 진심 어린 조언

---

**최신 소식 공유 페이지**

### 최신 정보 공유

- "새소식" 공지 게시판 운영
- 최신 서브컬처 컨텐츠, 굿즈, 이벤트 소식을 제공
- 광고 게시를 통해 수익 창출 가능

### 캐시 기반 성능 최적화

- Redis 캐시를 활용해 빠른 응답 속도와 높은 처리량 확보
- TTL과 무효화 로직을 함께 적용해 데이터 최신성 확보

---

**인기 검색어 랭킹**

### 인기 검색어 랭킹

- 지난 24시간 동안 가장 많이 검색된 키워드를 실시간으로 집계
- 최신 트렌드를 한눈에 파악할 수 있는 탐색 경험 제공
- 관심사 기반으로 새로운 상품을 발견하고 구매로 연결
- 검색량 데이터를 기반으로 수요 높은 상품을 예측
---

**운영 및 모니터링**

### AOP 기반 로깅

- 주요 비즈니스 로직의 실행 흐름과 예외를 자동으로 로깅
- 에러 추적 및 디버깅 효율성 향상

### Pinpoint 모니터링 환경 구축

- 애플리케이션 성능 모니터링(APM)을 통한 트랜잭션 추적
- 장애 발생 시 원인 분석 및 성능 병목 지점 파악 가능

---

## <h2 id="시스템-아키텍처">🖇️ 시스템 아키텍처</h2>

### Cloud Architecture
<img width="1592" height="942" alt="image" src="https://github.com/user-attachments/assets/c3c7f9bb-7431-43a2-bb53-fbde8983b690" />

### CI/CD Pipeline
<img width="1696" height="718" alt="image" src="https://github.com/user-attachments/assets/8d366f45-59c0-42f2-ad2c-8bd94642ba38" />

---

## 📐 설계 문서

### ERD
<img width="2019" height="1071" alt="image" src="https://github.com/user-attachments/assets/acbe8ab1-de3f-47e3-b125-176b4ac542ef" />

### 와이어프레임
<img width="2048" height="1041" alt="image" src="https://github.com/user-attachments/assets/a377ae7f-d3aa-4bfd-9909-1942c743d9d7" />

### API 명세서

#### 유저
| Method | 기능 | URL | 설명 |
|--------|------|----------|------|
| POST   | 회원가입 | `/api/accounts` | 새로운 사용자 계정을 생성합니다. |
| POST   | 어드민 회원가입 | `/api/admin/accounts` | 새로운 어드민 계정을 생성합니다. |
| GET    | 회원조회 | `/api/accounts/{id}` | 회원 정보를 조회합니다. |
| GET    | 마이페이지 조회 | `/api/accounts/me` | 마이페이지를 조회합니다. |
| PATCH  | 회원 비밀번호 변경 | `/api/accounts/me/password` | 회원 비밀번호를 변경합니다. |
| PATCH  | 회원정보 수정 | `/api/accounts/me` | 회원 정보를 수정합니다. |
| DELETE | 회원탈퇴 | `/api/accounts/me` | 사용자 계정을 삭제합니다. |
| POST   | 로그인 | `/api/login` | 이메일과 비밀번호로 로그인, 성공 시 AccessToken 반환 |
| POST   | 로그아웃 | `/api/logout` | AccessToken을 무효화하여 세션 종료 |
| POST   | AccessToken 재발급 | `/api/refresh` | RefreshToken으로 AccessToken 재발급 |

---

#### 상품
| Method | 기능 | URL | 설명 |
|--------|------|----------|------|
| POST   | 상품 등록 | `/api/items` | 상품을 생성합니다. |
| GET    | 상품 단건 조회 | `/api/items/{itemId}` | 상품을 단건 조회합니다. |
| GET    | 상품 필터 조회 | `/api/items/serarch` | 상품을 필터링하여 조회합니다. |
| GET    | 유저별 상품 조회 | `/api/items/serarch/interest` | 관심 태그 기반 상품 조회 |
| PATCH  | 상품 수정 | `/api/items/{itemId}` | 상품을 수정합니다. |
| DELETE | 상품 삭제 | `/api/items/{itemId}` | 상품을 삭제합니다. |

---

#### 태그
| Method | 기능 | URL | 설명 |
|--------|------|----------|------|
| POST   | 태그 등록 | `/api/admin/tags` | 태그를 등록합니다. |
| GET    | 태그 조회 | `/api/admin/tags/{tagId}` | 태그를 조회합니다. |
| PATCH  | 태그 이름 수정 | `/api/admin/tags/{tagId}` | 태그를 수정합니다. |
| DELETE | 태그 삭제 | `/api/admin/tags/{tagId}` | 태그를 삭제합니다. |

---

#### 상품 댓글
| Method | 기능 | URL | 설명 |
|--------|------|----------|------|
| POST   | 댓글 생성 | `/api/item/{itemId}/comments` | 특정 상품글의 댓글 생성 |
| GET    | 댓글 조회 | `/api/item/{itemId}/comments` | 특정 상품글의 댓글 조회 |
| PATCH  | 댓글 수정 | `/api/item/{itemId}/comments/{commentId}` | 댓글을 수정합니다. |
| DELETE | 댓글 삭제 | `/api/item/{itemId}/comments/{commentId}` | 댓글을 삭제합니다. |

---

#### 살까말까
| Method | 기능 | URL | 설명 |
|--------|------|----------|------|
| POST   | 살까말까 생성 | `/api/posts` | 살까말까 게시물 생성 |
| GET    | 살까말까 단건 조회 | `/api/posts/{postId}` | 살까말까 단건 조회 |
| GET    | 살까말까 전체 조회 | `/api/posts` | 살까말까 전체 조회 |
| GET    | 내가 쓴 살까말까 조회 | `/api/posts/me` | 마이페이지 내 살까말까 조회 |
| PATCH  | 살까말까 수정 | `/api/posts/{postId}` | 살까말까 게시물 수정 |
| DELETE | 살까말까 삭제 | `/api/posts/{postId}` | 살까말까 게시물 삭제 |

---

#### 살까말까 댓글
| Method | 기능 | URL | 설명 |
|--------|------|----------|------|
| POST   | 댓글 생성 | `/api/post/{postId}/comments` | 살까말까 댓글 생성 |
| GET    | 댓글 조회 | `/api/post/{postId}/comments` | 살까말까 댓글 조회 |
| PATCH  | 댓글 수정 | `/api/post/{postId}/comments/{commentId}` | 살까말까 댓글 수정 |
| DELETE | 댓글 삭제 | `/api/post/{postId}/comments/{commentId}` | 살까말까 댓글 삭제 |

---

#### 살까말까 좋아요 / 싫어요
| Method | 기능 | URL | 설명 |
|--------|------|----------|------|
| POST   | 좋아요/싫어요 등록 | `/api/posts/{postId}/vote` | 살까말까 좋아요/싫어요 등록 |
| DELETE | 좋아요/싫어요 취소 | `/api/posts/{postId}/vote` | 살까말까 좋아요/싫어요 취소 |

---

#### 오더
| Method | 기능 | URL | 설명 |
|--------|------|-----|------|
| POST   | 오더 등록 | `/api/orders` | 결제를 신청합니다. |
| GET    | 오더 단건 조회(나의 것만) | `/api/orders/my/{orderId}` | 나의 결제 단건을 조회합니다. |
| GET    | 내가 구매한 오더 전체 조회 | `/api/orders/me` | 나의 결제 내역 전체를 조회합니다. |
| POST   | 오더 수정 (주문 완료 → 배송중) | `/api/user/orders/{orderId}/status/ready` | 주문 완료에서 배송중으로 상태 변경 |
| POST   | 오더 수정 (배송중 → 거래 완료) | `/api/user/orders/{orderId}/status/completed` | 배송중에서 거래 완료로 상태 변경 |
| POST   | 주문 취소 (주문 완료 → 주문 취소) | `/api/user/orders/{orderId}/status/canceled` | 주문 완료에서 주문 취소로 상태 변경 |

---

#### 새소식
| Method | 기능 | URL | 설명 |
|--------|------|-----|------|
| POST   | 새소식 생성 | `/api/admin/newsfeeds` | 새소식 게시글 생성 |
| GET    | 새소식 단건 조회 | `/api/newsfeeds/{newsfeedId}` | 새소식 단건 조회 |
| GET    | 새소식 전체 조회 | `/api/newsfeeds` | 새소식 전체 조회 |
| PATCH  | 새소식 수정 | `/api/admin/newsfeeds/{newsfeedId}` | 새소식 게시글 수정 |
| DELETE | 새소식 삭제 | `/api/admin/newsfeeds/{newsfeedId}` | 새소식 게시글 삭제 |

---

#### 새소식 댓글
| Method | 기능 | URL | 설명 |
|--------|------|-----|------|
| POST   | 댓글 생성 | `/api/newsfeeds/{newsfeedId}/comments` | 새소식 댓글 작성 |
| GET    | 댓글 조회 | `/api/newsfeeds/{newsfeedId}/comments` | 새소식 댓글 조회 |
| PATCH  | 댓글 수정 | `/api/newsfeeds/{newsfeedId}/comments/{commentId}` | 새소식 댓글 수정 |
| DELETE | 댓글 삭제 | `/api/newsfeeds/{newsfeedsId}/comments/{commentId}` | 새소식 댓글 삭제 |

---

#### 채팅방
| Method | 기능 | URL | 설명 |
|--------|------|-----|------|
| POST   | 채팅방 생성 | `/api/chatroom` | 대화를 위한 채팅방 생성 |

---

#### 키워드 랭킹
| Method | 기능 | URL | 설명 |
|--------|------|-----|------|
| GET    | 키워드 랭킹 조회 | `/api/keywords/ranking` | 키워드 랭킹 조회 |

---

#### 채팅
| Method | 기능 | URL | 설명 |
|--------|------|-----|------|
| CONNECT   | 웹소켓 연결 | `ws://localhost:8080/ws/chat` | 웹소켓에 연결합니다. |
| SUBSCRIBE | 채팅방 구독 | `/sub/chatroom/2` | 채팅방을 구독합니다. |
| SEND      | 채팅 전송 | `/pub/chatroom/2` | 채팅 메시지를 발행합니다. |


## 🌊 서비스 플로우
<img width="1995" height="1002" alt="image" src="https://github.com/user-attachments/assets/ee7ffc90-208b-4f39-87c2-2f1b896031a1" />


## <h2 id="기술-스택">🛠️ 기술 스택</h2>

| 구분 | 사용 기술 |
|------|------------|
| **Back-end** | Java 17, Spring Framework, Spring Boot, Spring Data JPA, Spring Security, JWT, Query DSL, Websocket, STOMP, Jackson |
| **Productivity Tools** | Lombok, Gradle |
| **Database** | MySQL, Redis |
| **Infra & CI/CD** | Docker, RabbitMQ, Amazon EC2, Amazon SES, GitHub Actions |
| **Test** | Postman, JMeter |
| **Monitoring** | PinPoint |
| **Tools** | IntelliJ IDEA |
| **Collaboration** | GitHub, Notion, Slack, ERD cloud, draw.io |

### 디렉토리

📦 Mania-Place
├── 📂 common                  # 공통 모듈, 전체 프로젝트에서 재사용되는 코드
│   ├── 📂 annotation          # 커스텀 어노테이션
│   ├── 📂 aop                 # AOP 관련 로직
│   ├── 📂 config              # 공통 설정
│   ├── 📂 dto                 # 공통 DTO
│   ├── 📂 entity              # 공통 엔티티
│   ├── 📂 exception           # 예외 처리
│   │   ├── 📂 enums           # 예외 관련 열거형
│   │   └── 📂 exceptionclass  # 커스텀 예외 클래스
│   ├── 📂 filter              # 필터 처리
│   ├── 📂 health              # 상태 체크
│   └── 📂 security            # 보안 관련
│       └── 📂 jwt             # JWT 인증 처리
└── 📂 domain                  # 도메인별 모듈
    ├── 📂 auth                # 인증/인가
    │   ├── 📂 controller      # API 컨트롤러
    │   │   └── 📂 dto         # 요청/응답 DTO
    │   ├── 📂 domain          # 도메인 레이어
    │   │   ├── 📂 model       # 도메인 모델
    │   │   └── 📂 repository  # DB 접근
    │   └── 📂 service         # 서비스 로직
    ├── 📂 chatmessage         # 채팅 메시지
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   └── 📂 request
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 chatroom            # 채팅방
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   ├── 📂 request
    │   │   └── 📂 response
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 Image               # 이미지
    │   ├── 📂 dto
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 item                # 상품
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   ├── 📂 request
    │   │   └── 📂 response
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 itemcomment         # 상품 댓글
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   ├── 📂 request
    │   │   └── 📂 response
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 itemtag             # 상품 태그
    │   ├── 📂 entity
    │   └── 📂 repository
    ├── 📂 keyword             # 키워드
    │   ├── 📂 controller
    │   ├── 📂 domain
    │   │   ├── 📂 model
    │   │   └── 📂 repository
    │   └── 📂 service
    │       └── 📂 dto
    ├── 📂 newsfeed            # 뉴스피드
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   ├── 📂 request
    │   │   └── 📂 response
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 newsfeedcomment     # 뉴스피드 댓글
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   ├── 📂 request
    │   │   └── 📂 response
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 order               # 주문
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   ├── 📂 request
    │   │   └── 📂 response
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 post                # 게시글
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   ├── 📂 request
    │   │   └── 📂 response
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 postcomment         # 게시글 댓글
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   ├── 📂 request
    │   │   └── 📂 response
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 tag                 # 태그
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   │   ├── 📂 request
    │   │   └── 📂 response
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   ├── 📂 service
    │   └── 📂 util
    ├── 📂 user                # 사용자
    │   ├── 📂 controller
    │   ├── 📂 dto
    │   ├── 📂 entity
    │   ├── 📂 repository
    │   └── 📂 service
    ├── 📂 usertag             # 사용자 태그
    │   ├── 📂 entity
    │   └── 📂 repository
    └── 📂 vote                # 투표
        ├── 📂 controller
        ├── 📂 dto
        │   ├── 📂 request
        │   └── 📂 response
        ├── 📂 entity
        ├── 📂 repository
        └── 📂 service

---

## 💡 의사결정 및 기능 구현

- [💡 JWT&Spring Security](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef5148048b568f3ed64348ccd)

- [💡 Query DSL을 통한 상품 검색](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef514804ab31cea9466834883)

- [💡 상품 검색과 이미지 조회 로직 분리](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef514804ab31cea9466834883)

- [💡 검색어 랭킹 기능 Redis (Zset) 도입](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef51480de882dfd02e62aeffc)

- [💡 상품 주문 동시성 제어](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2532dc3ef5148082880bf5aa36bbd576) 

- [💡 아마존 SES를 이용한 메일 알람 기능 구현](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef514800b920dd4f9f43d1a25) 

- [💡 실시간채팅 도입](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef514805bbb29f2c32546a6fc)

- [💡 rabbitMQ 도입](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2532dc3ef5148039ac32f46fe7191708)

- [💡 Pinpoint APM 도입](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef51480b3ba41c63e1b0f4941)

---

## <h2 id="성능-개선">⚡️ 성능 개선</h2>

- [⚡️ 인기 검색어 랭킹](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2532dc3ef5148060a259f2725a3702fc)

- [⚡️ 캐싱을 이용하여 새소식 조회를 더욱 빠르게!](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2532dc3ef5148095965ac33c8bc36536)

- [⚡️ 재고 관리 동시성 이슈 해결](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2532dc3ef5148097af2ff6da3e4576a8)

---

## 🚨 트러블 슈팅

- [🚨 커넥션풀 고갈 현상](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2532dc3ef51480ff88cdfd92f9610d90)

- [🚨 태그 저장 동시성 문제](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef5148025bebbf303f9137a64)

- [🚨 Redis 직렬화 문제](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef514803caf10f01060a81727)

- [🚨 분산 락 구현 시 트랜잭션 전파 이슈](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2542dc3ef51480238d45fbcb0c6e1033)

---

## 📅 일정

| 구분         | 기간                  | 활동                     | 비고                          |
|--------------|-----------------------|--------------------------|-------------------------------|
| 기획         | 2025.07.16 ~ 07.20    | 아이디어 회의 및 S.A 작성 | S.A 피드백                   |
| MVP          | 2025.07.21 ~ 07.28    | 최소 기능 개발            | MVP 시연                      |
| 스프린트 1차 | 2025.07.29 ~ 08.05    | 추가 기능 개발            | 스프린트 회고 후 배포         |
| 스프린트 2차 | 2025.08.06 ~ 08.14    | 고도화                   | 스프린트 회고 후 배포 & 5분 브리핑 |
| 발표 준비    | 2025.08.16 ~ 08.24    | 브로셔 및 발표자료 제작    | 최종 프로젝트 제출             |


---

## ☕ KPT회고

- **Keep**
    - 작업 진행 상황을 꼼꼼히 공유하여 팀 전체의 이해를 높임
    - 모든 팀원이 성실하게 코드 리뷰에 참여함
    - 회의 시 모두가 마이크를 켜고 참여했으며,
    불필요한 시간을 줄이기 위해 회의 시간을 20분으로 제한함
    - 신규 기능 도입 시 가이드라인을 문서화하여 공유함
    - 항상 서로를 존중하며 트러블 없이 프로젝트를 성공적으로 완수함
    - 새로운 기술이나 새로운 기능 구현에 있어 사용자 관점에서  깊이 있는 고민을 하고 
    문제 해결 방안을 찾아 적용한 것

- **Problem**
    - 초기 깃 컨벤션 설정이 다소 미흡하여 일관성 유지에 어려움이 있었음
    - 프로젝트 릴리즈 버전 관리에 대한 이해도 부족
    - 주기적 서류 업데이트 체계 미흡으로, 후에 문서 버전 관리 강화의 필요성을 느낌
    - 테스트 코드 보강 필요
 
- **Try**
    - 단순 캘린더 형태에 그치지 않고, 업무별 세부 일정을 조직적으로 관리
    - 릴리즈 버전 규칙(SemVer 등)을 학습하고 실제 배포 과정에 적용하여 일관된 버전 관리 정착
    - 문서 버전 관리를 코드와 동일하게 Git/협업 툴 기반으로 운영하여 최신화 체계 강화
    - 적극적인 신규 기능 도입 확대

---

## 🧑‍💻 팀원 소개

- [구대웅(리더)](https://teamsparta.notion.site/2532dc3ef51480118514d61161f883e5?source=copy_link)
- [최경진(부리더)](https://teamsparta.notion.site/2532dc3ef51480a38d82ce8342289c52?source=copy_link)
- [우새빛](https://teamsparta.notion.site/2532dc3ef5148051b4ded565a65ac9ef?source=copy_link)
- [윤희준](https://teamsparta.notion.site/2532dc3ef5148032bfdff594362df57f?source=copy_link)
- [이호준](https://teamsparta.notion.site/2532dc3ef51480e59f57c872122e2523?source=copy_link)
- [이효선](https://teamsparta.notion.site/2532dc3ef51480a4b3a4d9d49687ebbf?source=copy_link)
