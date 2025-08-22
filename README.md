<img width="512" height="381" alt="image" src="https://github.com/user-attachments/assets/2b21e1b5-9618-42d5-9a4c-f356cf7b09b5" />

### ·•· 마니아 층을 위한 중고거래 플랫폼 ·•·

## 🗂️ 목차
| [👀 서비스 개요](#-서비스-개요) | [🗝️ 핵심 기능](#핵심-기능) | [🖇️ 시스템 아키텍처](#시스템-아키텍처) |

| [📐 설계 문서](#-설계-문서) | [🛠️ 기술 스택](#기술-스택) | [🌊 서비스 플로우](#-서비스-플로우) |

| [💡 의사결정 및 기능 구현](#-의사결정-및-기능-구현) | [⚡ 성능 개선](#-성능-개선) | [🚨 트러블 슈팅](#-트러블-슈팅) |

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

---


## <h2 id="기술-스택">🛠️ 기술 스택</h2>



## 🌊 서비스 플로우
## 💡 의사결정 및 기능 구현
## ⚡ 성능 개선
## 🚨 트러블 슈팅
## 📅 일정
## ☕ KPT회고
## 🧑‍💻 팀원 소개
