<img width="512" height="381" alt="image" src="https://github.com/user-attachments/assets/2b21e1b5-9618-42d5-9a4c-f356cf7b09b5" />

### ·•· 마니아 층을 위한 중고거래 플랫폼 ·•·

## 팀원 소개
| 이름 |	역할	| GitHub 주소 |
|-----|------|------------|
| 구대웅 | 리더 | https://github.com/GuDaeWoong |
| 최경진 | 부리더 | https://github.com/Che0807 |
| 우새빛 | 멤버 | https://github.com/saevit |
| 윤희준 | 멤버 | https://github.com/planbsoho |
| 이호준 | 멤버 | https://github.com/Gaeso |
| 이효선 | 멤버 | https://github.com/hohohosn |

## 🗂️ 목차
| [👀 서비스 개요](#-서비스-개요) | [🗝️ 핵심 기능](#핵심-기능) | [🖇️ 시스템 아키텍처](#시스템-아키텍처) |

| [📐 설계 문서](#-설계-문서) | [🛠️ 기술 스택](#기술-스택) | [🌊 서비스 플로우](#-서비스-플로우) |

| [💡 의사결정 및 기능 구현](#-의사결정-및-기능-구현) | [⚡️ 성능 개선](#성능-개선) | [🚨 트러블 슈팅](#-트러블-슈팅) |

| [📅 일정](#-일정) | [☕ KPT회고](#-kpt회고) |


## 👀 서비스 개요
### Mania Place
**요즘 애니메이션은 '문화'입니다.** 

하지만 애니메이션을 사랑하는 덕후들은 여전히 **정보 수집, 소통, 굿즈 거래, 커뮤니티**

등 일상 속 불편함을 겪고 있습니다.

<br>
🎬 매일매일 "새로 나온 재밌는 애니 굿즈 상품은 없을까?" 궁금하지만,

**여러 사이트를 돌아다니며 찾기엔 너무 번거롭고...**

<br>
♻️ 한 번 보고 마는 아크릴 스탠드, 안 맞는 피규어, 중복 굿즈들...

**중고 거래는 사기 걱정도 되고 너무 귀찮고...**

<br>
🛍️ 한정판 굿즈 놓치고 싶지 않은데

**어디서 언제 나오는지 정보가 다 흩어져 있고...**

<br>
🔥 지금 사람들이 가장 많이 찾는 건 뭘까?

**실시간으로 확인할 방법이 없으니, 트렌드를 놓치기 일쑤...**

<br>
🤔 "이 굿즈 살까? 말까?" 혼자 고민만 하다가

**결국 충동구매로 후회하거나, 망설이다 놓치거나...**

<br>

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
<img width="1530" height="1182" alt="image" src="https://github.com/user-attachments/assets/bca68c3c-515a-4bad-a557-8f7cd7840bf2" />

### CI/CD Pipeline
<img width="1696" height="718" alt="image" src="https://github.com/user-attachments/assets/5a268abe-d5e4-4830-8e00-00ad6b14a9b9" />


---

## 📐 설계 문서

### ERD
<img width="2019" height="1071" alt="image" src="https://github.com/user-attachments/assets/acbe8ab1-de3f-47e3-b125-176b4ac542ef" />

### 와이어프레임
<img width="2048" height="1041" alt="image" src="https://github.com/user-attachments/assets/a377ae7f-d3aa-4bfd-9909-1942c743d9d7" />

### API 명세서


### 디렉토리

## 📂 프로젝트 구조

```plaintext
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
```

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

---

## 💡 의사결정 및 기능 구현

<details>
  <summary>💡 JWT&Spring Security</summary>

  ### 1. 배경
  
  Mania Place 서비스에서는 로그인과 권한 관리가 필요했습니다.
  
  세션 기반 인증을 고려했으나, 트래픽이 몰릴 때 서버 부담이 크고 여러 서버를 확장할 때 세션 공유 문제가 발생할 수 있었습니다.
  
  이 문제 때문에 **JWT와 Spring Security**를 도입하게 되었습니다.
  
  ---
  
  ### 2. 요구사항
  
  - 사용자가 로그인 후 안전하게 인증 상태를 유지할 것
  - 일반 사용자 / 관리자 권한을 구분해 접근을 제어할 것
  - 서버가 상태를 직접 들고 있지 않아도 확장이 가능할 것
  - 보안성을 해치지 않으면서도 사용자가 불편하지 않게 구현할 것
  
  ---
  
  ### 3. 의사결정
  
  - **세션 기반 인증**은 서버 부하와 확장성 문제로 제외
  - **OAuth2 / OIDC**는 기능은 풍부하지만 현재 프로젝트 범위에는 오버스펙이라 판단
  - **JWT**는 무상태(stateless)로 서버 부담을 줄이고, 권한 정보도 함께 담을 수 있어 적합하다고 판단
  - **JWT + Spring Security** 조합으로 결정
  
  ---
  
  ### 4. 구현 상세
  
  - 로그인 성공 시 **액세스 토큰과 리프레시 토큰**을 발급
    - 액세스 토큰: 유효기간 짧게 설정 (보안 강화)
    - 리프레시 토큰: 새 토큰을 발급받을 때 사용
  - Spring Security의 **필터 체인**을 설정해
  - JWT 토큰 검증 과정에서 사용자 권한(`ROLE_USER`, `ROLE_ADMIN`)을 확인해 기능별 접근 제어
  - **로그아웃 시 토큰 블랙리스트 처리**를 통해 토큰 재 사용을 방지
  
  ---
  
  ### 5. 향후 고려 사항
  
  - 토큰 갱신 과정에서 **리프레시 토큰 재사용 방지** 로직을 강화할 필요 있음
  - 추후 외부 서비스 연동 시 **OAuth2 / OIDC**로 확장 가능성 고려
  
</details>

<details>
  <summary>💡 Query DSL을 통한 상품 검색</summary>

  ### 1. 배경

  상품 전체 조회 시 연관된 이미지와 태그를 **N+1 문제 없이 fetch join**으로 가져오면서, 동시에 **Pageable**로 페이징 처리했습니다.

  그러나 상품과 이미지가 `@OneToMany` 관계로:

  - **조인 결과가 곱해져 중복 row 발생** → JSON 응답에 불필요한 중복 값 포함  
  - **DB 레벨에서 LIMIT, OFFSET 적용 불가** → Hibernate가 Java 메모리에서 중복 제거  
  - 대용량 데이터 시 **메모리 사용량 증가, 성능 저하 우려**  

  등의 문제가 발생했습니다.

  ---

  ### 2. 요구사항

  - 연관 엔티티 중복 없이 조회  
  - DB 레벨에서 페이징 적용  
  - N+1 문제 방지  
  - 대용량 데이터에서도 성능 문제 방지  

  ---

  ### 3. 의사결정

  **단계별로 분리하여 조회**

  1. 조건에 맞는 Item의 ID만 먼저 페이징 처리하여 조회  
  2. 해당 ID 리스트 기준으로 연관된 Tag, Image를 `fetch join`하여 조회  
  3. 별도 count 쿼리를 작성하여 전체 페이지 수 계산  
  4. 최종적으로 데이터를 조립하여 `Page<ItemDto>` 형태로 반환  

  **단계별로 분리하여 조회하는 이유**

  - **중복 row 방지**: fetch join으로 여러 컬렉션을 한 번에 조회하면 (Item × Tag × Image)처럼 곱해져 중복 발생
  - **DB 레벨 페이징 적용**: ID만 먼저 조회하면 LIMIT, OFFSET이 정확히 동작
  - **성능 최적화**: 페이지에 필요한 Item에 대해서만 조회하므로, 대용량 데이터에서도 메모리 사용량과 처리 속도가 초기 버전 대비 효율적
  - **ID 기반 조회 장점**: IN절 조회는 기본 엔티티 중심 쿼리라 JPA가 같은 Item ID를 기준으로 하나의 객체로 묶어 연관 컬렉션을 추가 → row 수 증가 없이 안전하게 조회 가능

  **QueryDSL을 도입한 이유**

  - 페이징, 동적 where절, 정렬 등 복잡한 여러 쿼리들을 하나의 메서드에 응집시킬 수 있음
  - 리포지토리 계층에서 **조회 책임을 명확하게 관리**할 수 있어, 유지보수성과 확장성 측면에서도 유리하다고 판단

  ---

  ### 4. 구현 상세

  ItemRepositoryCustomImpl

```
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Item> search(String keyword, List<String> tags, Long userId, Pageable pageable) {

		// 상품 조회를 위한 where절
		BooleanExpression whereCondition = null;

		// 키워드를 전달 받았다면 해당 키워드가 제목or설명에 포함된 상품을 조회
		if (keyword != null) {
			BooleanExpression keywordCondition = item.itemName.likeIgnoreCase("%" + keyword + "%")
				.or(item.itemDescription.likeIgnoreCase("%" + keyword + "%"));
			whereCondition = keywordCondition;
		}

		// 태그를 전달 받았다면 해당 태그가 존재하는 상품을 조회
		if (tags != null && !tags.isEmpty()) {
			BooleanExpression tagCondition = tag.tagName.in(tags);
			whereCondition = (whereCondition == null) ? tagCondition : whereCondition.or(tagCondition);
		}

		// 유저 id를 전달 받았다면 해당 판매자가 해당 id와 일치하는 상품을 조회
		if (userId != null) {
			BooleanExpression userCondition = item.user.id.eq(userId);
			whereCondition = (whereCondition == null) ? userCondition : whereCondition.and(userCondition);
		}

		return buildPagedItem(whereCondition, pageable);
	}

	private Page<Item> buildPagedItem(BooleanExpression whereCondition, Pageable pageable) {

		// 정렬 조건
		OrderSpecifier<?> orderSpecifier = getOrderSpecifiers(pageable);

		// 페이지의 id 조회
		List<Long> itemIds = queryFactory
			.select(item.id)
			.from(item)
			.join(item.itemTags, itemTag)
			.join(itemTag.tag, tag)
			.where(
				item.isDeleted.isFalse(),
				whereCondition)
			.orderBy(orderSpecifier)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		if (itemIds.isEmpty()) {
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		}

		// 페이지의 상품들 정보 조회
		List<Item> items = queryFactory
			.selectFrom(item)
			.distinct()
			.join(item.user).fetchJoin()
			.join(item.itemTags, itemTag).fetchJoin()
			.join(itemTag.tag, tag).fetchJoin()
			.join(item.images).fetchJoin()
			.where(item.id.in(itemIds))
			.orderBy(orderSpecifier)
			.fetch();

		// 페이징 처리를 위한 전체 열 카운트
		long total = Optional.ofNullable(
			queryFactory
				.select(item.countDistinct()) // fetchJoin 없이 count 하기위해 distinct
				.from(item)
				.join(item.itemTags, itemTag)
				.join(itemTag.tag, tag)
				.where(
					item.isDeleted.isFalse(),
					whereCondition)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(items, pageable, total);
	}

	private OrderSpecifier<?> getOrderSpecifiers(Pageable pageable) {
		// Query DSL 필드 접근 경로(path) 지정 도구
		PathBuilder<Item> path = new PathBuilder<>(Item.class, "item");

		// 필터링해서 createdAt 정렬만 받고, 없으면 기본값(최신순) 세팅
		Sort.Order firstOrder = pageable.getSort().stream()
			.filter(order -> "createdAt".equals(order.getProperty()))
			.findFirst()
			.orElse(new Sort.Order(Sort.Direction.DESC, "createdAt"));

		// pageable의 정렬 값을 Query DSL에 적용가능한 형탤 변환 ("createdAt,desc" -> item.createdAt.desc())
		return new OrderSpecifier<>(
			firstOrder.isAscending() ? Order.ASC : Order.DESC,               // .asc()/.desc() 판별
			path.getComparable(firstOrder.getProperty(), Comparable.class)); // "createdAt" -> item.createdAt
	}
}
```

ItemService
```
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
  private final ItemRepository itemRepository;

	@Loggable
	@Transactional
	public PageResponseDto<ItemSummaryResponse> searchItems(String keyword, List<String> tags, Long userId,
		Pageable pageable) {

		Page<Item> pagedItems = itemRepository.search(keyword, tags, userId, pageable);

		Page<ItemSummaryResponse> response = pagedItems.map(ItemSummaryResponse::from);

		return new PageResponseDto<>(response);
	}
}
```

  ---

  ### 5. 향후 고려 사항

  **현재 강점**  
  - 정확한 페이징 + 연관 정보 한번에 조회  
  - fetch join으로 N+1 문제 해결  
  - 동적 조건 및 정렬 등 쿼리 최적화 유리

  **제약 사항**  
  - 쿼리 3번 필요  
  - 정렬 유지나 중복 제거 등 쿼리 작성 시 주의 필요
  - 책임 분리 설계가 어려움

</details>

<details>
<summary>💡 상품 검색과 이미지 조회 로직 분리</summary>

### 1. 배경

QueryDSL을 사용하여 상품 조회 시 연관 정보를 한 번에 조회하면서 페이징이 의도대로 작동하도록 구현했습니다.

그러나, 상품에 종속된 태그와 달리 이미지는 상품뿐 아니라 여러 도메인에서 재사용될 수 있어, **이미지를 상품과 한 객체로 볼 수 있을지 고민**이 있었습니다.

---

### 2. 요구사항

- 여러 객체 간의 로직 **분리하여 책임 명확화**
- 연관 엔티티 중복 없이 조회
- DB 레벨에서 페이징 적용
- N+1 문제 방지
- 대용량 데이터에서도 성능 문제 방지

---

### 3. 의사결정

**이미지 조회 로직 분리**

1. 상품 엔티티 페이징으로 조회
2. 해당 페이지의 상품 ID 리스트 추출
3. 연관 정보(이미지)를 ID 리스트 기반으로 별도 조회하여 Map으로 그룹핑
4. 반환된 연관 정보와 함께 응답값 생성

**이미지 로직을 분리한 이유**

- 한 번에 이미지를 조회하면 쿼리 수가 적고 조회 속도가 빠르다는 장점은 인지
- 그러나 이미지는 여러 도메인에서 재사용 가능 → 상품과 함께 조회하면 책임 분리 어려움
- 이미지의 저장·수정·삭제 로직은 **이미지 서비스**에서 관리 → 관리 일관성 확보 필요
- 구조 복잡성을 방지하고, 여러 도메인에서 요청 시 **동일한 구조**를 유지하면 코드 이해도 향상될 것이라 판단

---

### 4. 구현 상세

ItemRepositryCustomImpl

```
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
	. . . (기본적인 사항들은 위의 구현 내용과 동일)
	
	private Page<Item> buildPagedItem(BooleanExpression whereCondition, Pageable pageable) {
		// 정렬 조건
		. . .

		// 페이지의 id 조회
		. . .

		// 페이지의 상품들 정보 조회 <- '.join(item.images).fetchJoin()' 제거
		List<Item> items = queryFactory
			.selectFrom(item)
			.distinct()
			.join(item.user).fetchJoin()
			.join(item.itemTags, itemTag).fetchJoin()
			.join(itemTag.tag, tag).fetchJoin()
			.join(item.images).fetchJoin()
			.where(item.id.in(itemIds))
			.orderBy(orderSpecifier)
			.fetch();

		// 페이징 처리를 위한 전체 열 카운트
		. . .

		return new PageImpl<>(items, pageable, total);
	}
}
```

ItemService

```
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
  private final ItemRepository itemRepository;
  private final ImageService imageService;
	
	@Loggable
	@Transactional(readOnly = true)
	public PageResponseDto<ItemGetAllResponse> searchItems(String keyword, List<String> tags, Long userId,
		Pageable pageable) {

		searchKeywordService.addKeyword(keyword);

		Page<Item> pagedItems = itemRepository.search(keyword, tags, userId, pageable);

		return buildGetAllItems(pagedItems);
	}

	// 해당 페이지 상품의 이미지를 맵핑하여 반환
	@Transactional(readOnly = true)
	protected PageResponseDto<ItemGetAllResponse> buildGetAllItems(Page<Item> pagedItems) {
		// 해당 게시글 ID 목록에 대한 이미지 정보를 반환
		Map<Long, Image> mainImagesMap = imageService.getMainImagesForItems(pagedItems);

		// 조합
		Page<ItemGetAllResponse> dtoPage = pagedItems.map(item -> {
			// --메인이미지 조합
			Image mainImage = mainImagesMap.getOrDefault(item.getId(), null);

			return ItemGetAllResponse.from(item, mainImage.getImageUrl());
		});

		return new PageResponseDto<>(dtoPage);
	}
}
```

ImageService
```
@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	// 현재 상품 페이지에 있는 상품의 이미지들을 맵으로 묶어 반환
	@Transactional(readOnly = true)
	public Map<Long, Image> getMainImagesForItems(Page<Item> pagedItems) {
		// 현재 페이지에 존재하는 itemId 리스트
		List<Long> itemIds = pagedItems.getContent().stream()
			.map(Item::getId)
			.distinct()
			.collect(Collectors.toList());

		if (itemIds.isEmpty()) {
			return Collections.emptyMap();
		}

		// 해당 상품들의 대표 이미지 조회
		List<Image> mainImages = imageRepository.findMainImagesByItemIds(itemIds);

		// 결과 리스트를 itemId를 키로 하는 Map으로 변환
		return mainImages.stream()
			.collect(Collectors.toMap(
				img -> img.getItem().getId(),
				img -> img
			));
	}
}
```

---

### 5. 향후 고려 사항

**현재 강점**

- 이미지 관련 로직을 독립적으로 관리할 수 있어 유지보수성이 높음
- 여러 도메인에서 재사용 가능
- 상품 로직과 혼합되지 않아 **도메인 구조 자체는 명확**

**제약 사항**

- 개선한 구조는 연관 객체가 많아질수록 **추가 조회 쿼리 수 증가**
- 결과를 **직접 조립해야 해서 코드 복잡도가 상승**
- 조회 쿼리와 데이터 매핑이 분산되어 **실제 조회의 데이터 흐름 파악이 어려울 수 있음**

</details>

<details>
<summary>💡 검색어 랭킹 기능 Redis (Zset) 도입</summary>

## 1. 배경

기존 인기 검색어 랭킹 기능은 RDB 기반으로 구현되어 있었으나,

대량의 실시간 쓰기와 정렬/집계 연산이 동시에 발생하면서 성능 저하와 DB 커넥션 풀 고갈 문제가 발생하였습니다.

이에 따라 실시간 집계와 빠른 조회를 동시에 만족시킬 수 있는 대안이 필요했습니다.

---

## 2. 요구사항

- 대량의 **실시간 쓰기** 처리를 안정적으로 지원해야 함
- 상위 **N개 인기 검색어를 초고속 조회** 가능해야 함
- 운영 중인 서비스와의 **호환성 및 운영 효율성**을 고려해야 함
- 새로운 인프라 도입 시 **운영 부담 최소화** 필요

---

## 3. 의사결정

해결 방안으로는 RDB 튜닝, 인메모리 캐시, NoSQL 계열 DB 도입이 검토되었습니다.

| 대안 | 장점 | 한계 |
| --- | --- | --- |
| **기존 RDB 튜닝** (쿼리 최적화, 인덱스, 커넥션 풀 확대, 배치 처리) | 추가 인프라 없이 개선 가능, 안정적인 운영 경험 | 대량 실시간 쓰기 시 물리적 한계, 정렬/집계 연산 부하 지속 |
| **인메모리 캐시 (Caffeine 등)** | 단일 서버 환경에서 매우 빠른 조회 속도 | 다중 서버 환경 동기화 어려움, 서버 재시작 시 데이터 유실, 랭킹 정렬 로직 직접 구현 필요 |
| **NoSQL 계열 (MongoDB, Cassandra, Redis 등)** | 고성능 쓰기 처리, 일부 제품은 TTL·정렬 지원 | 대부분 랭킹 로직 별도 구현 필요, 새로운 DB 운영 부담 |

그중 Redis를 선택한 이유:

- 이미 서비스 내 다른 기능에서 Redis를 운영 중 → 추가 학습·운영 부담 최소화
- Redis의 **ZSet** 자료구조를 통해 랭킹 정렬을 별도 구현 없이 지원
- 인메모리 캐시의 장점을 포함하면서도 NoSQL DB의 성능적 이점을 활용 가능

---

## 4. 구현 상세

1. **ZSet(정렬 집합) 활용**
    - 점수 기반 자동 정렬 및 상위 N개 검색어를 빠르게 조회 가능
    - 별도 랭킹 구현 필요 없음
2. **쓰기·읽기 성능 최적화**
    - 초당 수십만 건 수준의 쓰기 처리 가능
    - DB 커넥션 풀 고갈 문제 근본적 해결
3. **운영 효율성**
    - 기존 Redis 운영 경험 재활용
    - 새로운 학습 및 운영 부담 최소화

---

## 5. 향후 고려 사항

- **Redis 장애 대응**: 단일 인스턴스 장애 시 데이터 유실 가능 → 클러스터링 및 영속화 옵션(RDB, AOF) 검토 필요
- **메모리 관리**: 인메모리 특성상 데이터 크기 관리 필수 → TTL 및 데이터 정리 정책 적용 필요
- **확장성 고려**: 향후 트래픽 급증 시 Redis Cluster로 수평 확장 가능성 확보

</details>

<details>
<summary>💡 상품 주문 동시성 제어</summary>

### 1. 배경

문제 상황

- 다중 사용자 환경에서 동일한 리소스에 대한 동시 접근으로 인한 데이터 일관성 문제 발생
- 트래픽 증가에 따른 동시성 이슈 빈발로 비즈니스 로직의 정확성 보장 필요

비즈니스 임팩트

- 데이터 불일치로 인한 고객 불만 및 신뢰도 하락
- 재고 오버셀링, 중복 예약 등의 운영상 문제
- 시스템 안정성 및 데이터 무결성 확보 필요

---

### 2. 요구사항

기능적 요구사항

- 동일 리소스에 대한 동시 접근 시 데이터 일관성 보장
- 트랜잭션 격리 수준에 따른 적절한 락 메커니즘 구현
- 데드락 방지 및 처리 메커니즘 구축
- 높은 동시성을 지원하면서도 성능 최적화

---

### 3. 의사결정

동시성 제어 방식 선택

**비관락, 낙관락 장점**

| **특성**       | **비관락**                                   | **낙관락**                          |
|----------------|---------------------------------------------|-------------------------------------|
| **데이터 일관성** | 완벽한 일관성 보장 (데이터 충돌 발생 불가)       | 버전 관리를 통해 충돌 제어                |
| **성능**       | - 순차적으로 처리하여 안정적 <br> - 재시도 로직 불필요 <br> - 일정한 응답 시간 | - 높은 동시성 처리 <br> - 락 대기시간 없음 <br> - 병렬 처리 최적화 |
| **구현 복잡도** | - DB 락 메커니즘 활용 <br> - 직관적인 코드        | - 비즈니스 로직과 분리 <br> - 유연한 충돌 처리 전략 <br> - 세밀한 제어 가능 |
| **사용자 경험** | - 확실한 처리 보장 <br> - 실패 시 즉시 알림 반환 <br> - 예측 가능한 결과 | - 빠른 초기 응답 <br> - 대기 시간 없음 <br> - 동시 작업 가능 |
| **확장성**     | - 트랜잭션 경계 명확 <br> - 데이터 무결성 보장      | - 수평 확장 용이 <br> - 클라우드 환경 최적화 |

**비관락, 낙관락 단점**

| **특성**       | **비관락**                                               | **낙관락**                                   |
|----------------|----------------------------------------------------------|----------------------------------------------|
| **성능**       | - 낮은 동시성 <br> - 순차 처리로 인한 대기                | - 높은 충돌률에서 성능저하 <br> - 지속적인 재시도 오버 헤드 <br> - CPU 사용량 증가 <br> - 불안정한 응답시간 |
| **리소스 점유** | - DB 커넥션 장시간 점유 <br> - 커넥션 풀 고갈 위험 <br> - 메모리 사용량 증가 | - 재시도로 인한 리소스 낭비 <br> - 버전 관리 오버헤드 <br> - 복잡한 상태관리 |
| **시스템 위험** | - 데드락 위험 <br> - 락 타임아웃 처리 복잡 <br> - 장애 전파 위험 | - 복잡한 에러 처리 <br> - 부분 실패 상황 관리 <br> - 데이터 정합성 검증 복잡 |
| **구현 복잡도** | - 트랜잭션 경계관리 복잡 <br> - 락 범위 설정 어려움 <br> - 중첩 트랜잭션 처리 복잡 | - 복잡한 재시도 로직 <br> - 버전 충돌 해결 전략 필요 <br> - 디버깅 복잡성 |

**비관락을 선택한 이유**

1. 데이터 일관성 보장
- 중고거래에서 가장 중요한 것은 “정확한 1개 판매” 재고 불일치로 인한 고객불만 차단
2. 중고상품의 특성 : 재고 희소성
- 중고상품은 대부분 1개 한정판매 이기때문에 낙관락의 장점인 높은 동시성이 무의미함
- 낙관락의 단점인 높은 충돌률에 의하여 성능 저하가 발생
3. 구현 단순성
- 비관락 : 복잡한 재시도 로직 없이 간단하게 구현가능, 유지보수성 향상
- 낙관락 : 성능은 좋지만 복잡한 재시도 로직
4. 비즈니스 로직 통합
- 재고확인, 사용자 검증, 결제 처리를 하나의 트랜잭션으로 안전하게 처리
5. 사용자 경험 개선
- 구매 가능 여부를 미리 확실하게 확인가능

**DB 비관락 vs Redis 비관락(분산락)**

| **구분**       | **DB 비관락**                                      | **Redis 비관락 (분산락)**                   |
|----------------|---------------------------------------------------|---------------------------------------------|
| **장점**       | - ACID 보장 <br> - 데이터 일관성 완벽 <br> - 트랜잭션 롤백 지원 | - DB 부하 분산 <br> - 매우 빠른 락 처리 속도 <br> - 확장성 우수 <br> - 타임아웃 제어 가능 |
| **단점**       | - DB 커넥션 점유 <br> - 커넥션 풀 고갈 위험 <br> - 높은 DB 부하 <br> - 확장성 제한 | - 추가 인프라 필요 (Redis 서버) <br> - 네트워크 지연/장애 영향 <br> - Redis 장애 시 위험 |
| **확장성**     | 낮음 (DB 스케일업 필요)                            | 높음 (수평 확장 가능)                       |
| **적합한 경우** | 단일 DB 환경, 데이터 일관성이 최우선일 때           | 다중 서버 환경, 대규모 트래픽, 고속 락이 필요한 경우 |

**Redis 분산락을 선택한 이유**

1. 확장성 고려
- 서비스 성장에 따른 수평 확장을 대비해 분산환경에서도 동작하는 락 필요
2. DB 부하 분산
- 인기 상품의 경우 동시 접속자가 급증하는데 DB 커넥션을 오래 점유하면 전체 시스템 성능 저하 유발
- Redis로 락 처리를 분리해 DB는 실제 데이터 처리에만 집중
3. 유연한 타임 아웃 제어
- Redis에서 락 타임아웃을 유연하게 조절해 데드락 방지 가능
4. 성능 최적화
- 메모리 기반 Redis는 락 획득/해제가 매우 빠름

---

### 4. 구현 상세

재고관리 서비스

```
@Service
@RequiredArgsConstructor
public class StockService {
    @Lazy
    private final RedissonClient redissonClient;
    private final ItemRepository itemRepository;

    // 분산락을 통한 재고감소
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStock(Long itemId, Long quantity) {
        String lockKey = "Lock:" + itemId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 획득 대기시간 3초, 자동 해제시간 10초
            boolean acquired = lock.tryLock(3000, 10000, TimeUnit.MILLISECONDS);
            if (!acquired) {
                throw new CustomException(ExceptionCode.STOCK_LOCK_FAILED);
            }

            try {
                // DB 레벨 비관적 락과 조합
                Item item = itemRepository.findByIdWithLock(itemId)
                    .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));

                item.decreaseStock(quantity);

            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException(ExceptionCode.OPERATION_INTERRUPTED);
        }
    }
}
```

Repository 레벨 비관적 락
```
@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT i FROM Item i WHERE i.id = :itemId")
	Optional<Item> findByIdWithLock(@Param("itemId") Long itemId);
}
```

주문 서비스

```
@Service
@RequiredArgsConstructor
public class OrderService {
    private final StockService stockService;
    
    @Transactional
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto, Long userId) {
        // 비즈니스 로직 검증...
        
        // 분산 락을 통한 안전한 재고 차감
        stockService.decreaseStock(item.getId(), requestDto.getQuantity());
        
        // 주문 생성 로직...
    }
}
```

분산락 순서도

<img width="294" height="461" alt="image" src="https://github.com/user-attachments/assets/f2d221f6-9884-4cb7-b819-7a18d6cd7163" />

**데드락 방지 전략**

- 락 순서 정렬: 여러 리소스 락 시 ID 순으로 정렬하여 획득
- 타임아웃 설정: 모든 락에 적절한 타임아웃 설정
- 락 범위 최소화: 트랜잭션 범위를 최소한으로 유지

---

### 5. 향후 고려 사항

**모니터링**

- **락 대기시간 모니터링**: 평균 대기시간 및 최대 대기시간 추적
- **데드락 발생률 추적**: 데드락 발생 빈도 및 패턴 분석
- **성능 메트릭 수집**: 처리량, 응답시간, 에러율 지속적 모니터링

**확장성 고려사항**

- **샤딩 전략**: 데이터 증가 시 수평적 확장을 위한 샤딩 계획

</details>

<details>
<summary>💡 아마존 SES를 이용한 메일 알람 기능 구현</summary>
    
### 1. 배경

새소식은 발매 예정인 굿즈나 이벤트와 같은 공식적인 정보를 사용자에게 전달하는 공간입니다.

회원들이 새소식에 더 많은 관심을 가지고 자주 이용하도록 하기 위해, 새소식이 등록되면 이메일로 알림을 보내는 방안을 검토하였습니다.

검토 과정에서 고려한 사항은 다음과 같습니다.

- 프론트엔드나 앱이 구동되어 있지 않음
- 이메일, 문자, 카카오톡 중에서 문자와 카카오톡은 현재 시점에서 비즈니스 계정 인증이 어려움
- 이메일은 이미 기업 광고 매체로 널리 사용됨
- 우리 서비스에서는 회원 가입 시 이메일 입력이 필수임

→ 따라서, 알림 매체로 이메일 전송 기능을 우선적으로 도입하기로 결정하였습니다.

---

### 2. 요구사항

- 새소식 등록 시 회원에게 이메일 전송
- 메일 발송 중 API 응답 지연 최소화
- 메일 발송 실패 시 로그 기록 및 재시도 가능
- 확장성 고려 (나중에 외부 MQ 적용 가능)

---

### 3. 의사결정

**SMTP 선택 이유**

- 이메일 알림 기능을 구현하기 위해 **SMTP(Simple Mail Transfer Protocol)**를 사용하기로 결정
- SMTP는 인터넷에서 이메일을 전송할 때 사용하는 **표준 통신 규약**으로, 
발신 메일을 받아 수신 서버로 전달하는 역할
- SMTP를 사용함으로써:
    - **범용성**: 구글, 아마존 SES, 사내 메일 서버 등 거의 모든 메일 서버에서 지원
    - **안정성**: 표준화된 프로토콜이므로 호환성 문제 최소화
    - **직접 제어 가능**: 전송 과정을 코드에서 바로 제어할 수 있음

**SMTP 테스트 환경과 실제 서비스**

- 초기 개발 단계에서는 **구글 SMTP**로 테스트하였으나 전송 횟수 제한이 존재
- 따라서, 실제 서비스에서는 전문 메일 송신 서비스인 **아마존 SES**를 사용하여 구현

**큐 활용 및 설계 이유**

- 메일 발송 실패 시 안정적인 처리를 위해 **Redis 큐**를 활용
- 새소식 등록 시 모든 회원에게 메일을 보내도록 **비동기 처리**하도록 설계
- 큐를 사용함으로써:
    - 대량 발송 시 API 응답 지연 최소화
    - 실패한 메일에 대한 재시도 및 로깅 용이
    - 나중에 RabbitMQ, Kafka, SQS 등 다른 메시지 큐로 전환 시 구조적 유연성 확보

---

### 4. 구현 상세

**과정 1. 기본 메일 전송 구현**

build.gardle 의존성 추가
    
```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-mail'
}
```

application.yml 메일 설정 추가
```
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${SES_USERNAME}
    password: ${SES_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
email: ${EMAIL}
```

MailSendService
```
@RequiredArgsConstructor
public class MailSendService {
	private final JavaMailSender mailSender;
	
	// 송신자 메일 환경변수 받기
	@Value("${email}")
	private String from;

	public void sendNewsfeedNotification(String toEmail, String newsfeedTitle) {
		// 메일 메시지 객체 생성
		SimpleMailMessage message = new SimpleMailMessage();
		
		// 송신자 메일
		message.setFrom(from)
		
		// 수신자 메일
		message.setTo(toEmail);
		
		// 제목
		message.setSubject("[새소식] '" + newsfeedTitle + "'가 새로 올라왔어요!");

		// 본문
		StringBuilder body = new StringBuilder();
		body.append("안녕하세요, Mania Place입니다.\n\n");
		body.append("제목: ").append(newsfeedTitle).append("\n");
		body.append("지금 바로 Mania Place에 접속하고 새소식을 확인해보세요!");
		message.setText(body.toString());
		
		// 전송
		mailSender.send(message);
	}
}
```

- `JavaMailSender`로 메일 발송 서비스 구현
- 동기(Synchronous) 방식으로 처리되어 **각 메일 발송 완료까지 응답 대기 → API 지연 및 서버 리소스 점유 발생**
- 실제 측정 결과: 메일 1건 발송 시 **4.34초 소요**, 수신자 수 증가에 비례하여 응답 시간이 **선형적으로 증가**함 확인

**과정 2. 비동기 처리 시도**

- MailService에 `@Async`를 적용해 메일 발송을 **비동기 처리 시도**
- API 응답은 즉시 반환되며, 메일 전송은 **백그라운드에서 진행**
- 비동기 적용 후 응답 시간: **평균 200ms 수준**
- 비동기로 응답 속도는 빨라졌지만, 메일 전송 실패 시 바로 알기 어렵고 재처리도 쉽지 않다는 단점 발생

**과정 3. 안정적 발송을 위한 큐 적용**

MailRequestDto

```
@Data
public class MailRequest implements Serializable {
	private String toEmail;
	private String title;
	private int retryCount = 0;  // 재시도 횟수 기본 0

	private MailRequest(String toEmail, String newsfeedTitle) {
		this.toEmail = toEmail;
		this.title = newsfeedTitle;
	}

	public static MailRequest of(String toEmail, String newsfeedTitle) {
		return new MailRequest(toEmail, newsfeedTitle);
	}
}
```
​
RedisMailQueueService
```
@Service
@RequiredArgsConstructor
public class RedisMailQueueService {
	private final RedissonClient redissonClient;

	private static final String MAIL_QUEUE_KEY = "newsfeed:mail:queue";

	public void enqueueMail(MailRequest mailRequest) {
		// 메일 요청을 Redis 큐에 넣는 저장·전달 로직
		RBlockingQueue<MailRequest> queue = redissonClient.getBlockingQueue(MAIL_QUEUE_KEY);
		queue.add(mailRequest);
	}
}
```

RedisMailWorker
```
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMailWorker implements InitializingBean, DisposableBean {
	private final RedissonClient redissonClient;
	private final MailSendService mailService;

	private static final String MAIL_QUEUE_KEY = "newsfeed:mail:queue";
	private volatile boolean running = true;
	private Thread workerThread;

	@Override
	public void afterPropertiesSet() {
		workerThread = new Thread(() -> {
			RBlockingQueue<MailRequest> queue = redissonClient.getBlockingQueue(MAIL_QUEUE_KEY);

			while (running) {
				MailRequest mailRequest = null;
				try {
					// 큐에서 MailRequest 객체가 들어올 때까지 대기
					mailRequest = queue.take();

					// 받은 요청으로 메일 전송
					mailService.sendNewsfeedNotification(
						mailRequest.getToEmail(),
						mailRequest.getTitle()
					);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt(); // 인터럽트 처리
				} catch (Exception e) {
					// 메일 전송 에러 발생 시 로그 기록
					log.error("[{}] {} - {} - (USER ID: {}) ({} ms) - ERROR: {} | Params: {}",
						"MAIL_SEND", MAIL_QUEUE_KEY, "sendNewsfeedNotification",
						"system", 0L, e.getMessage(),
						mailRequest != null ? mailRequest.toString() : "null"
					);

					if (mailRequest != null && mailRequest.getRetryCount() < 3) {
						mailRequest.setRetryCount(mailRequest.getRetryCount() + 1);
						try {
							// 실패한 요청 다시 큐에 넣기
							Thread.sleep(1000); // 1초 대기
							queue.offer(mailRequest);

						} catch (Exception ex) {
							// 큐에 다시 넣는 중 에러 발생 시 로그 기록
							log.error("[{}] {} - {} - (USER ID: {}) ({} ms) - ERROR: {} | Params: {}",
								"MAIL_SEND_REQUEUE", MAIL_QUEUE_KEY, "queue.offer",
								"system", 0L, ex.getMessage(),
								mailRequest != null ? mailRequest.toString() : "null"
							);
						}
					}
				}
			}
		});

		workerThread.setDaemon(true);
		workerThread.start();
	}

	@Override
	public void destroy() {
		running = false;
		workerThread.interrupt();
	}
}
```

- 앞선 단계에서 적용한 `@Async` 제거
- Redis 큐를 사용하여 메일 발송 요청을 **비동기적으로 처리하도록 변경**
- 워커가 큐를 모니터링하며 메일 발송 수행
- 메일 전송에 실패하면 다시 큐에 넣어 전송 재시도 수행
이때, 무한 재시도로 인한 큐 적체 및 리소스 낭비를 방지하기 위해 최대 3회로 제한

---

### 5. 향후 고려 사항

- **재시도 정책 개선**
    - *지수 백오프*: 재시도 간격을 점차 늘려 과부하 방지
    - *배치 재전송*: 실패 메일을 모아 일정 시점에 일괄 재발송
- **메시지 큐 확장성**
    - *RabbitMQ*: 안정적인 메시지 전송, ack/queue 관리 용이
    - *Kafka*: 높은 처리량(TPS), 이벤트 스트리밍 적합
    - *SQS (AWS)*: 완전 관리형 서비스로 서버 운영 부담 없음
    
</details>

<details>
<summary>💡 실시간채팅 도입</summary>
    
### 1. 배경

http 프로토콜은 단방향통신으로 설계되었는데 실시간 채팅을 구현한다고 가정했할때, 채팅을보내고 응답을 받는요청을 또 보내야 하기에 구조적으로 의아함을 느껴 좀 더 나은 방식들이 있는지 찾아보았습니다.

---

### 2. 요구사항

실시간 양방향 통신으로 사용자가 메세지를 보냈을 때 즉시 상대방에게 메세지가 도착해야합니다.

---

### 3. 의사결정

1. 폴링 : 스핀락처럼 계속해서 새로운 메세지를 확인하는 방식

단점 :  메세지가 왔는지 계속 확인을 하는과정에서 계속해서 리소스를 사용하는데, 이런 문제를 해결하기 위해 1초마다 확인하는것을 5초마다 확인하는식으로 수정한다면 실시간채팅을 확인하기 위한 목적이 부정되는 모순이 발생해 적합하지 않은 방식이라고 판단했습니다.

2. 롱폴링 : 요청을 보내고 새로운 메세지가 생기면 응답하는방식

단점 : 폴링의 단점인 무의미한 요청을 계속 보내는것은 해결했지만 메세지가 생길때까지 연결을 유지하기때문에 쓰레드를 점유하는 문제가 있어 대규모 트래픽상황에서 부적합한 방식이라고 판단했습니다.

3. SSE : 클라이언트가 서버에 연결을 요청하면 서버는 이 연결을 유지하며 새로운 데이터가 생길때마다 클라이언트에게 응답을주는 실시간 단방향 통신방식 

단점 : 요청은 http, 응답은 sse로 구현은 가능하지만 기본적으로 단방향 통신을 위해 나온방식이기고  채팅과 같은 양방향 구조엔 적합하지 않다고 판단했습니다.

4. websocket + stomp 

websockete : 한 번 연결되면 양방향으로 계속 통신하는 양방향 통신지원 프로토콜 stateful한 프로토콜이기에 서로의 상태를 알수있어 채팅읽음, 채팅방 나감등의 정보도 실시간으로 알수가 있음. 

stomp프로토콜 : 웹소캣 프로토콜의 위에서 동작하는 프로토콜로 send, subscribe, publish 등의 규정된 틀을 제공하는 서브 프로토콜
단점 : 단일 서버에서는 수많은 websocket연결을 유지하고 stomp 통신처리하는것이 부담될 수 있습니다.

5. 결정 

위의 방안들중 서비스의 안정성, 자원 소모를 고려했을때 아래와같은 이유로 4번을 선택했습니다. 

웹소켓 연결 자체는 스레드를 계속 점유하지 않고 대기하다가 stomp통신이 오갈때만 스레드가 할당되어 유지하는것에는 문제가없다고 판단했습니다.

또한 대용량 트래픽 상황에서는 TaskExecutor 설정을 통해 메시지 처리를 위한 스레드 풀의 크기를 유연하게 조절할 수 있어 유연한 대처에 적합하다고 판단했습니다. 

단일서버로 인해 생기는 문제점은 메세지 큐등을 사용하여 여러 서버로 상태를 공유하는 방식으로 구현해 해결해 볼 수 있을 것 같아 websocket + stomp 방식으로 구현하기로 결정했습니다.

---

### 4. 구현 상세

```
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat") //stomp 연결을 위한 엔드포인트 설정
                .setAllowedOriginPatterns("*")// 개발 단계에서 모든 origin 허용
                .withSockJS();// socket+javascript
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        // 메세지브로커에서 클라이언트가 구독할 prefix
        registry.setApplicationDestinationPrefixes("/pub");
        // 클라이언트가 서버로 메세지 보낼 때 prefix
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(interceptors);
    }
```
---

### 5. 향후 고려 사항

메세지의 영속성, 메세지의 유실, 대규모 트래픽상황에서의 안정적인 쓰레드 할당이 필요합니다.

</details>

<details>
<summary>💡 rabbitMQ 도입</summary>
    
### 1. 배경

기존 채팅 시스템은 메세지가 생성될 때마다 매번 데이터베이스에 직접 INSERT 쿼리를 보내는 동기적 방식으로 구현되어 있습니다. 

이 방식은 트레픽이 몰리는 상황이라면 DB에 부하가 집중되어 성능이 떨어질 수 있고, DB 접근 과정에서 문제가 발생한다면 서비스 응답 자체가 지연되는 문제를 발생시키기도 합니다.

```
   //controller
   @MessageMapping("/chatroom/{roomId}")
    public void sendMessageV1(
            @DestinationVariable Long roomId,
            @Payload ChatMessageRequest request,
            CustomPrincipal principal
    ) {
        chatMessageService.saveMessage(roomId, request, principal);
        messagingTemplate.convertAndSend("/sub/chatroom/" + roomId, request);
    }
```

```
    //service
    @Transactional
    public ChatMessage saveMessage(Long roomId,ChatMessageRequest request, CustomPrincipal principal) {
        User sender = userService.findByIdOrElseThrow(principal.getId());
        ChatRoom room = chatRoomService.findByIdOrElseThrow(roomId);
        ChatMessage message = ChatMessage.of(room, sender, request.getContent(), null);
        return chatMessageRepository.save(message);
    }
```

---

### 2. 요구사항

비동기 처리 : 메세지 저장 로직을 분리하여 사용자가 메세지를 보냈을 때 즉각적으로 응답을 받을 수 있어야함.

성능 및 안정성 : DB에 직접적인 부하를 줄이고, 트래픽이 몰려도 안정적으로 처리할 수 있어야함.

유연한 시스템 확장 : 향후 트래픽 증가에 대비하여 시스템을 확장할 수 있어야함.

---

### 3. 의사결정

기존 동기적 방식은 채팅 저장 로직과 사용자 응답 로직이 강하게 결합되어 있어 서비스의 안정성을 보장하기 어렵다고 판단해 메세지 저장 로직을 비동기적으로 처리할 수 있는 메세지 브로커가 필요하다고 판단했습니다.

기술 스택 고민

1. redis pub/sub : 인메모리를 사용해 빠른 서비스를 제공하지만 영속성을 지원하지 않아 메세지 유실 가능성이있습니다.
2. 카프카 높은 처리량과 확장성에 강한 대표적인 메세지 기술스택이지만 이번 채팅기능의 경우 서브비즈니스로직이기에 오버스팩이라고 판단했습니다.
3. rabbitMQ : 메세지 전송보장과 유실방지를 지원하지만 카프카대비 낮은 처리량, 메세지가 큐에 쌓여 병목이 발생하지 않도록 관리 필요합니다
4. acitveMQ : 오래된 자바기반의 jms 브로커로 자바생태계에 특화되어있고 연동이 쉽습니다.
5.  결론 
네개의 메세징 기술중 rabbitMQ와 activeMQ를 고민했습니다. 
자바기반의 언어인 activeMQ가 호환성 문제 등 적합하다고도 생각했지만, 오히려 자바에 대한 높은 종속성이 추후 확장을 고려했을 때 결합도를 높일 수 있다고 생각했습니다. 

이번 고민의 가장 근본적인 부분인 유연성과 확장성, 느슨한 결합이라는 관점에서 보았을때  java message service 기반인 activeMQ보다는 AMPQ기반의 RabbitMQ를 선택하는게 고민했던 부분을 해소할 수 있다고 판단했습니다.

---

### 4. 구현 상세

컨트롤러 레이어에서 서비스레이어의 비즈니로직을 호출하는것이 아닌 RabbitTemplate과 MessageTemplate으로 발행하기만 하고 , 서비스레이어에서는 @RabbitListener 어노테이션으로 비동기적으로 소비 후 일괄적으로 저장합니다.

이러한 메세지 브로커 기반의 구조는 스케일 아웃에 유리하여 메세지 처리량을 늘려야 할경우 단순히 @RabbitListener를 사용하는 서비스 서버를 추가하여 부하를 분산함으로써 여러 서버가 메세지를 분산환경에서 처리 할 수 있도록 해줍니다.

```
    //controller
    @MessageMapping("/chatroom/{roomId}")
    public void sendMessageV2(
            @DestinationVariable Long roomId,
            @Valid @Payload ChatMessageRequest request,
            CustomPrincipal principal
    ) {
        if (principal == null) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
        MessageQueue messageQueue = MessageQueue.of(request.getContent(), principal.getId(), roomId, LocalDateTime.now());
        rabbitTemplate.convertAndSend(CHAT_QUEUE_NAME, messageQueue);
        messagingTemplate.convertAndSend("/sub/chatroom/" + roomId, messageQueue);
    }
```

```
    //service
    @RabbitListener(queues = "chat.queue")
    public void consumeAndBufferMessage(MessageQueue request) {
        messageBuffer.add(request);
        log.info("버퍼에 메세지 추가: {}, 담긴메세지 개수: {}", request.getContent(), messageBuffer.size());
    }

    @Scheduled(fixedDelay = 20000)
    public void saveMessageFromBuffer() {
        if(messageBuffer.isEmpty()) {
            return;
        }
        List<MessageQueue> messageSave = new ArrayList<>(messageBuffer);
        messageBuffer.clear();

        List<ChatMessage> chatMessages = messageSave.stream()
                .map(this::createChatMessageFromQueue)
                .toList();
        chatMessageRepository.saveAll(chatMessages);
        log.info("버퍼에 있는 메세지 {} 개 20초마다 일괄저장", chatMessages.size());
    }
```

---

### 5. 향후 고려 사항

트래픽 증가 시 RabbitMQ 큐에 메시지가 쌓여 병목 현상이 발생하지 않도록 큐의 상태 모니터링시스템 구축이 필요할 수 있습니다.

메시지 전송 실패 시 재시도 로직이나 데드 레터 큐(Dead Letter Queue)를 활용하는 방안을 고민해야 합니다.

</details>

<details>
<summary>💡 Pinpoint APM 도입</summary>

### 1. 배경

프로젝트가 기능 구현 단계를 완료하고 성능 최적화 단계에 진입하면서, 기존의 성능 테스트 방식의 한계가 드러났습니다. JMeter를 활용한 부하 테스트를 통해 성능 병목 현상은 확인할 수 있지만, 실제 어떤 구간에서 지연이 발생하는지 구체적인 원인을 파악하기 어려운 상황이었습니다.

로깅 시스템을 통해 일부 추적이 가능하나, 다음과 같은 문제점들로 인해 새로 도입하기로 했습니다.

- 로그 분석에 많은 시간이 소요됨
- 전체 요청 플로우에서 병목 구간을 특정하기 어려움
- 리소스 사용률과 성능 지표를 실시간으로 확인할 수 없음
- 성능 개선 전후 비교 분석을 위한 정량적 지표 부족

중고거래 플랫폼은 상품 검색, 상세 조회, 채팅, 결제 등 여러 기능이 함께 작동하기 때문에, 성능 병목을 정확히 파악할 수 있는 도구가 필요하다고 느꼈습니다.

---

### 2. 요구사항

**기능적 요구사항**

- **요청 추적**: 사용자 요청부터 응답까지의 전체 플로우 추적
- **병목 지점 식별**: 각 메서드와 서비스 레이어별 응답 시간 측정
- **실시간 모니터링**: CPU, 메모리 등 시스템 리소스 사용률 실시간 확인
- **오류 추적**: 예외 발생 시점과 스택 트레이스 상세 정보 제공
- **성능 지표 시각화**: 응답 시간, TPS, 처리량 등 주요 지표의 그래프 표시

**비기능적 요구사항**

- **낮은 오버헤드**: 운영 환경에서도 최소한의 성능 영향
- **간편한 설치 및 설정**: 복잡한 설정 없이 빠른 도입 가능
- **오픈소스**: 라이선스 비용 부담 없이 활용 가능
- **한국어 지원**: 국내 개발 환경에 최적화된 문서와 커뮤니티

---

### 3. 의사결정

여러 APM 도구를 비교 검토한 결과 Pinpoint를 선택했습니다.

**선택 이유 :**

- **네이버에서 개발한 오픈소스**로 한국어 문서와 커뮤니티 지원이 풍부
- **Java 애플리케이션에 특화**되어 Spring Boot 프로젝트와의 호환성이 우수
- **에이전트 방식**으로 코드 수정 없이 도입 가능
- **상세한 트레이싱**: 메서드 레벨까지 세밀한 성능 추적 제공
- **직관적인 UI**: 서비스 맵과 성능 차트를 통한 시각적 분석 가능

**다른 도구 대비 장점:**

- Zipkin: 더 상세한 메트릭 제공
- New Relic: 무료로 사용 가능, 상세한 한국어 문서

---

### 4. 구현 상세

**단계적 도입**

1. **1단계**: 로컬 환경에서 Docker로 Pinpoint를 구성하여 성능 분석 용도로만 활용
2. **2단계**: 로컬에서의 안정성 확인 후 EC2 서버의 기존 컨테이너에 통합 배포
3. **3단계**: 아래 고려사항으로 인해 Pinpoint 전용 컨테이너로 분리하여 독립 운영

**고려사항**

처음에는 애플리케이션과 Pinpoint를 동일 컨테이너에 배치하려 했으나, 메모리 부족 현상이 발생하는 것을 확인했습니다. 또한, 모니터링 시스템과 서버가 한 컨테이너에 함께 있을 경우, 한 컨테이너가 문제가 생기면 모니터링까지 영향을 받을 수 있다는 점을 고려하여, 컨테이너를 분리하여 구성하기로 결정했습니다.

**아키텍처**

<img width="1063" height="559" alt="image" src="https://github.com/user-attachments/assets/36219fa1-7d70-4e06-9f6b-1369667eb6cb" />
Pinpoint 아키텍처

인스턴스 1에서 구동되는 Spring 앱에 `Pinpoint Agent`를 부착하여 트랜잭션과 성능 데이터를 수집하고, 이를 인스턴스 2의 `Pinpoint Collector`로 전송합니다. Collector는 수집된 데이터를 `HBase`에 저장하며, `HBase`에 저장된 데이터는 `Pinpoint Web`을 통해 시각화되어 웹 대시보드로 제공됩니다.

**Pinpoint 구축 및 연결 순서**

1. **스프링 앱 컨테이너(컨테이너 1)**
    - Pinpoint Agent를 부착 (VM 환경변수 설정)

```
// 예시
java -javaagent:/home/ubuntu/pinpoint-agent-2.5.3/pinpoint-bootstrap.jar \
-Dpinpoint.agentId=mania001 \
-Dpinpoint.applicationName=mania_place.dev \
-Dpinpoint.config=/home/ubuntu/pinpoint-agent-2.5.3/profiles/release/pinpoint.config \
-jar Mania-Place-0.0.1-SNAPSHOT.jar
```

2. **Pinpoint 서버 컨테이너(컨테이너 2)**
    - Docker로 Pinpoint를 구동하고 Collector, HBase, Web 준비
3. **컨테이너 간 네트워크 설정**
    - 프라이빗 IP 기반 통신 구성
    - 포트 8081로 인바운드/아웃바운드 트래픽 허용

---

### 5. 향후 고려 사항

**개선 및 확장 계획**

- **알림 시스템 구축**: 성능 임계치 초과 시 자동 알림 설정
- **모니터링 도구 확장**: Prometheus, Grafana와의 통합 모니터링 구축

<br>

**위험 요소 및 대응 방안**

**주요 위험 요소:**

- Agent 오버헤드로 인한 성능 영향
- HBase 저장소 용량 관리 이슈

**대응 방안:**

- 개발 환경에서 충분한 검증 후 단계적 확대
- 데이터 보존 기간 정책 수립으로 용량 관리

</details>

---

## <h2 id="성능-개선">⚡️ 성능 개선</h2>

<details>
<summary>⚡️ 인기 검색어 랭킹</summary>

### 1. 기능 소개

Mania Place의 **인기 검색어 랭킹 기능**은 

지난 24시간 동안 사용자들이 가장 많이 검색한 키워드를 실시간으로 집계하여 보여주는 기능입니다.

사용자는 최신 트렌드를 빠르게 파악할 수 있고, 판매자는 수요가 높은 상품을 예측하여 판매 전략을 세울 수 있습니다.

---

### 2. 문제 정의

기존 시스템에서는 인기 검색어 집계가 느리고 정확도가 떨어졌습니다.

- 검색어 데이터가 DB에만 저장되어, 실시간 집계가 어렵습니다.
- 24시간 기준 집계 로직이 복잡하고, 계산 비용이 높습니다.
- 트래픽이 급증하면 DB 커넥션이 고갈되어 시스템 응답 속도가 저하됩니다.

---

### 3. 해결 방안

- **Redis ZSet**을 활용하여 실시간 검색어 점수 집계
- 1시간 단위 스냅샷을 통해 과거 24시간 데이터를 효율적으로 계산
- 키별 점수 합산 및 정렬로 상위 N개 검색어를 빠르게 조회

---

### 4. 성능 테스트

<img width="1111" height="544" alt="image" src="https://github.com/user-attachments/assets/34fd2f79-f480-4f06-9655-8a9f058eeca1" />
<img width="1135" height="69" alt="image" src="https://github.com/user-attachments/assets/830a4eb1-6360-44c1-ab14-e5fb14ce4f80" />

#### **[인기 검색어 조회/개선 전]**

- **TPS (초당 처리량):** 113.5 / sec
    - **Latency (평균 응답 시간):** 4,030 ms
    - **Error Rate (에러율):** 0 %

#### **[분석 및 주요 문제점]**

- **높은 응답 지연 시간 (High Response Latency):** 평균 응답 시간이 4초(4,030ms)를 초과합니다. 이는 실시간성이 중요한 랭킹 서비스에서 사용자가 심한 지연을 체감할 수 있는 수준입니다.
- **확장성 한계 (Scalability Limitation):** 초당 약 113건의 요청만 처리 가능해, 향후 사용자 증가나 이벤트 발생 시 급증하는 트래픽을 감당하지 못하고 서비스 장애로 이어질 위험이 높습니다.

---

<img width="1112" height="531" alt="image" src="https://github.com/user-attachments/assets/46d187e5-5c94-41f7-b1c6-2fb8af0b2829" />
<img width="1134" height="69" alt="image" src="https://github.com/user-attachments/assets/7780c8c4-b945-45cf-a3cc-282f599e887d" />

#### **[인기 검색어 조회/개선 후]**

- **TPS (초당 처리량):** 132.6 / sec
- **Latency (평균 응답 시간):** 2,393 ms
- **Error Rate (에러율):** 0 %

#### **[분석 및 주요 개선점]**

DB 부하를 Redis로 이전하여 1차적인 시스템 안정성 확보에는 성공했지만, 응답 속도 측면에서는 추가 개선이 필요한 상태입니다.

- **1차 목표 달성 (확장성 확보):** 처리량(TPS)은 **17% 증가**하고 응답 시간은 **41% 단축**되어, 대규모 트래픽을 에러 없이 안정적으로 처리할 수 있는 **확장성**을 확보했습니다.
- **향후 과제 (응답 속도 최적화):** 1차 목표는 달성했으나, **2.4초 수준의 응답 시간은 실시간 서비스에서 더 개선될 필요가 있습니다.** 다음 단계로, Redis의 실시간 데이터 집계 방식을 추가 최적화하여 응답 시간을 **1초 미만으로 단축하는 것을 목표**로 합니다.

---

### 5. 해결 완료

- **Redis ZSet 활용 실시간 집계**
    - 시간별 Redis 키(`keyword_rankings:yyyy-MM-dd-HH`)로 검색어 점수 누적
    - 각 키는 25시간 TTL 적용 → 자동 만료로 오래된 데이터 제거
- **검색어 추가**
    - 입력 키워드는 실시간으로 해당 시간대 Redis ZSet에 **점수 1씩 증가**
- **24시간 랭킹 조회**
    - 현재 시간부터 23시간 전까지 총 24개의 시간별 Redis 키 조회
    - **ZUNIONSTORE**를 활용하여 24시간 키의 점수를 **Redis에서 바로 합산**
    - `reverseRangeWithScores`로 점수 내림차순 조회 → 상위 N개 반환
    - 0 이하 점수는 랭킹에서 제외
- **스케줄러 기반 DB 스냅샷 저장 (안정성 확보)**
    - 매 정시(매시간 0분 0초) Redis에서 키워드 점수 조회 → DB 스냅샷 테이블 저장
    - **Redis 서버가 다운되더라도 스냅샷 데이터를 기준으로 인기 검색어 조회 기능이 유지**
    - Redis 서버 복구 후에도 스냅샷 데이터를 이용해 정상 서비스 복구 가능
- **데이터 정리**
    - 30일 이상 된 DB 스냅샷 데이터 주기적 삭제로 용량 관리

---

### 6. 향후 개선 사항

**1. DB 스냅샷을 활용한 장애 대응 능력 강화**

현재 매시간 생성하고 있는 DB 스냅샷을 재해 복구 및 서비스 연속성 보장에 적극적으로 활용하여 시스템의 안정성을 한 단계 끌어올립니다.

- **Fallback 로직 도입:** Redis 장애가 발생하더라도 서비스가 중단되지 않도록, **가장 최신 DB 스냅샷을 조회하여 인기 검색어 기능을 제공**하는 Fallback(대체 작동) 로직을 구현합니다. 이를 통해 데이터 유실을 최대 1시간 이내로 제한하고 서비스 다운타임을 최소화합니다.
- **신속한 복구 지원:** 장애로부터 Redis 서버가 복구되었을 때, **DB의 최신 스냅샷 데이터를 Redis에 자동으로 다시 적재(Cache Warming)**하는 프로세스를 구축하여 빠르고 안정적으로 서비스를 정상화합니다.

**2. 랭킹 조회 API 성능 최적화**

현재 2.4초(2,393ms) 수준인 인기 검색어 조회 API의 응답 시간을 **1초 미만으로 단축**하여, 사용자에게 실시간에 가까운 경험을 제공하는 것을 목표로 합니다.

- **실시간 집계 로직 개선:** 모든 사용자 요청 시마다 24개의 키를 `ZUNIONSTORE`로 집계하는 현재 방식 대신, **별도의 스케줄러가 랭킹 결과를 주기적으로 미리 계산하여 캐시**해두는 방식으로 변경하여 조회 속도를 획기적으로 개선합니다.

**3. 검색 관련 기능 확장**

안정화된 랭킹 시스템을 기반으로 사용자에게 더 풍부한 가치를 제공하기 위한 신규 기능 확장을 검토합니다.

- **연관 검색어 추천:** 사용자가 특정 키워드를 검색했을 때, 연관성이 높은 다른 키워드를 함께 추천하여 탐색의 편의성을 높입니다.
- **시간대별 트렌드 분석:** 시간의 흐름에 따른 특정 키워드의 인기도 변화를 시각적으로 보여주어, 사용자와 판매자 모두에게 유용한 인사이트를 제공합니다.
    
</details>

<details>
<summary>⚡️ 캐싱을 이용하여 새소식 조회를 더욱 빠르게!</summary>
	
### 1. 기능 소개

새소식은 발매 예정인 굿즈나 이벤트 같은 공식적인 소식을 사용자에게 알리는 공간으로, 

운영자가 게시글을 올리면 사용자가 조회합니다. 새 글 업데이트 시 사용자에게 이메일이 발송됩니다.

---

### 2. 문제 정의

공식 소식이 올라오는 창구이므로 사용자는 신규 새소식에 매우 민감하게 반응합니다.

따라서 메일 알림을 받은 다수의 사용자가 즉시 접속할 가능성이 높아, 이에 따른 시스템 성능 저하가 발생합니다.

---

### 3. 해결 방안 : 캐싱을 통한 성능 개선

- 관리자만 새소식 등록, 수정, 삭제의 권한이 있으며, 새소식 등록은 빈번하지 않고 수정 및 삭제는 매우 드뭅니다.
- 공지 성격상 많은 사용자가 같은 데이터를 짧은 시간에 반복적으로 조회합니다.
- 새소식은 전체 조회만 가능하며, 사용자들이 다양한 소식을 골고루 접할 수 있도록 개인화나 검색 기능은 제한하고 있습니다.
- 변경 주기는 낮지만 조회 빈도가 높은 데이터이므로, 매번 DB에서 조회하는 대신 캐시에 저장하면 응답 속도를 높이고 서버 부하를 크게 줄일 수 있다고 판단했습니다.

---

### 4. 성능 테스트

**✔️ 테스트 조건**
    
**[환경]**

- 로컬 환경에서 테스트: 칩 Apple M1 | 메모리 16GB
- JMeter 사용
- 게시글 5000건 존재

**[스레드 속성]**

- **사용자 수**: 500명
- **Ramp-up 시간**: 1초
- **루프 카운트**: 10회

**[대상]**

- 캐싱 전 새소식 전체 조회
- 인메모리 캐싱 적용 후 새소식 전체조회
- Caffeine 캐싱 적용 후 새소식 전체조회
- Redis 캐싱 적용 후 새소식 전체조회

**✔️ 테스트 목적**

1. 로컬 환경에서 500명 동시 사용자 부하를 시뮬레이션해 **다양한 캐싱 기법 적용 전후의 새소식 조회 성능과 안정성을 비교**
2. 서비스 환경에서 적용 가능성이 높은 **캐시 솔루션을 도출**


**✔️ 테스트 결과**

**[Response Times Over Time(시간 경과별 응답 시간)]**

| <img width="2048" height="1651" alt="image" src="https://github.com/user-attachments/assets/4940f656-9076-4a05-9dd5-202f66e2f98e" /> | <img width="2048" height="1643" alt="image" src="https://github.com/user-attachments/assets/a9aac996-3ad4-41ec-b47c-e9be70979c93" /> |
| [개선 전] : 3,000ms 이상 구간에서 요동 <br> (y축: 0~4000ms/ x축: 0~33s) | [인메모리 캐싱 적용] : 500~660ms 범위에서 안정 <br> (y축: 0~660ms/ x축: 0~7s) |
| <img width="2048" height="1624" alt="image" src="https://github.com/user-attachments/assets/5a54eb91-1f4a-4791-9415-f725fd9fc099" /> | <img width="2048" height="1627" alt="image" src="https://github.com/user-attachments/assets/ead609d7-d6a0-454a-93b4-2b8f2360db99" /> |
| [카페인 캐싱 적용] : 500~600ms 범위 <br> (y축: 0~600ms/ x축: 0~6s) | [레디스 캐싱 적용] : 550~700ms 범위 <br> (y축: 0~700ms/ x축: 0~6s) |

- 캐싱 전: 평균 응답 시간이 3,000ms 이상이며, 모든 요청을 처리하는 데 약 33초가 소요된다.
- 캐싱 후: 응답 시간이 대체로 400~700ms 사이로 개선되었고, 모든 요청 처리 시간은 약 10초 내외이다.

**[Transactions per Second(초당 처리 건수)]**

<img width="2048" height="1650" alt="image" src="https://github.com/user-attachments/assets/8e12efa6-a829-477b-9fd2-b805afb13b3d" />
[개선 전] : 100~190건 사이의 값에서 요동
(y축: 0~200/ x축:0~33s)

<img width="2048" height="1645" alt="image" src="https://github.com/user-attachments/assets/d9f48bfa-d1c8-4d31-8b46-b953971396a5" />
[인메모리 캐싱 적용] : 950건까지 상승하다 하강
(y축: 0~950/ x축:0~7s)

<img width="2048" height="1629" alt="image" src="https://github.com/user-attachments/assets/5e6e3806-0534-453e-9e9b-5aa8a1baf711" />
[카페인 캐싱 적용] : 1000건까지 상승하다 하강
(y축: 0~1000/ x축:0~7s)

<img width="2048" height="1624" alt="image" src="https://github.com/user-attachments/assets/792cfd99-c934-4fa3-a1dc-cfba06b06f5a" />
[레디스 캐싱 적용] : 850건까지 상승하다 하강
(y축: 0~900/ x축:0~6s)

- 캐싱 전: 그래프가 흔들리는 모양을 띄며, 대체적으로 100~190건의 요청을 초마다 응답하고 있다.
- 캐싱 후: 그래프가 위로 볼록한 곡선(3차 함수 형태)을 보이며, 초당 최대 약 1,000건의 요청을 처리한다.
<br>
**[총합 보고서]**

| 캐시 종류 | 평균 | 99% | 최소 | 최대 | 오류률 | 처리량 |
| --- | --- | --- | --- | --- | --- | --- |
| 캐시 없음 | 3010 | 4765 | 75 | 6009 | 0.00% | 152.3/sec |
| 인메모리 캐시 | 539 | 1092 | 12 | 2086 | 0.00% | 765.3/sec |
| Caffeine 캐시 | 506 | 1031 | 11 | 1405 | 0.00% | 806.6/sec |
| Redis 캐시 | 551 | 1097 | 13 | 1500 | 0.00% | 733.9/sec |
1. **평균 응답 시간**
    - Caffeine(506ms) < 인메모리(539ms) < Redis(551ms) ≪ 캐시 없음(3010ms)
2. **99퍼센타일 응답 시간** **(최악 성능)**
    - Caffeine(1031ms) < 인메모리(1092ms) ≈ Redis(1097ms)  ≪ 캐시 없음(4765ms)
3. **응답 시간 일관성**
    - Caffeine ≈ Redis ≈ 인메모리 > 캐시 없음
4. **처리량 (TPS)**
    - Caffeine(806.6) > 인메모리(765.3) > Redis(733.9)  ≫ 캐시 없음(152.3)

**✔️   최종 선택**

| 방식 | 장점 | 단점 | 적합한 환경 |
| --- | --- | --- | --- |
| **캐시 없음** | 구현 단순 | 성능·안정성 모두 열세 | 권장하지 않음 |
| **인메모리 캐싱** | 구현 간단, 단일 인스턴스에 적합 | Redis·Caffeine 대비 성능 낮음 | 단일 서버, 간단한 구조 |
| **Caffeine 캐싱** | 속도·처리량 우수, 변동 폭 최소 | 단일 인스턴스 한정 | 최고 성능이 필요한 단일 서버 |
| **Redis 캐싱** | TPS 유지력 우수, 멀티 서버 환경 지원 | 네트워크 오버헤드 발생 | 대규모 분산 환경 |
1. 테스트 결과 **Caffeine**이 응답 시간과 처리량 면에서 가장 우수했으나(평균·p99·TPS 모두 선두), Caffeine과 다른 캐시들 간 성능 격차는 크지 않았습니다. 
2. 인메모리 캐시는 JVM 내부 한정으로 멀티 서버 환경에서 확장성과 운영에 한계가 있습니다.
3. 본 프로젝트는 다른 영역에서 분산 환경 확장성을 고려해 적합한 기술을 도입했으며, 캐싱도 같은 관점에서 결정했습니다. 기업 환경에서는 이와 같은 이유로 **Redis 등 분산 캐시**를 널리 사용합니다.

따라서 캐시 미사용 대비 현저한 성능 향상이 확인되었고, 전체적인 성능 격차가 크지 않다는 점과 분산 구조에서의 적합성을 종합적으로 고려하여 **최종적으로 Redis를 선택**했습니다.

#### ✔️ 최종 구현

```
// build.gradle
dependencies {
	// redis 캐시
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'

	//json 직렬화를 위한 jackson
	implementation 'com.fasterxml.jackson.core:jackson-databind'
	implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'
}
```

```
@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		// 자바 타임 모듈 등록하여 LocalDateTime -> ISO-8601 형태로 직렬화
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// 타입 정보 포함 (직렬화/역직렬화 시 클래스 정보 유지)
		mapper.activateDefaultTyping(
			LaissezFaireSubTypeValidator.instance,
			ObjectMapper.DefaultTyping.NON_FINAL,
			JsonTypeInfo.As.PROPERTY
		);

		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

		// 캐시 설정
    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // 키는 문자열 직렬화
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer)) // 값은 JSON 직렬화
        .disableCachingNullValues() // null은 캐시에 저장하지 않음
        .entryTtl(Duration.ofMinutes(10)); // TTL 10분

    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(cacheConfig) // 캐시 설정
        .build();
	}
}
```

```
@Service
@RequiredArgsConstructor
public class NewsfeedService {

	private final NewsfeedRepository newsfeedRepository;
	private final UserService userService;
	private final ImageService imageService;
	
	@Loggable
	@Transactional(readOnly = true)
	@Cacheable(
		value = "listCache",
		key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()"
	)
	public PageResponseDto<NewsfeedListResponse> getAllNewsfeeds(Pageable pageable) {

		// 새소식 전체 조회(소프트딜리트 빼고)
		Page<Newsfeed> pagedNewsfeeds = newsfeedRepository.findByIsDeletedFalseWithFetchJoin(pageable);

		// 페이지에 들어갈 대표 이미지 일괄 조회
		Map<Long, Image> mainImageMap = imageService.getMainImagesForNewsfeeds(pagedNewsfeeds);

		List<NewsfeedListResponse> contentList = pagedNewsfeeds.stream().map(newsfeed -> {
        Image mainImage = mainImageMap.getOrDefault(newsfeed.getId(), null);
        return NewsfeedListResponse.of(newsfeed, mainImage != null ? mainImage.getImageUrl() : null);
    })
    .collect(Collectors.toList()); // 가변 리스트로 변환

    return new PageResponseDto<>(
        new PageImpl<>(contentList, pageable, pagedNewsfeeds.getTotalElements())
    );
	}
}
```

- JPA에서 반환하는 `Page.getContent()`는 보통 불변 리스트로 감싸져 있어서, 직접 캐시에 넣으면 직렬화 문제가 발생
- `PageResponseDto` 내부 `content` 필드는 `Page.getContent()`를 받아서 초기화하므로, 전달하는 `Page`의 리스트로 가변 리스트 필요
- 따라서, 서비스 레이어에서 `Page<T>`의 내용을 가변 리스트로 새로 수집 후, 그 리스트를 사용해 `PageImpl`을 새로 생성하여 전달
- `PageImpl`은 리스트를 복사하거나 불변으로 감싸지 않기 때문에, 개발자가 넘긴 가변 리스트를 그대로 유지

---

### 5. 해결 완료

**결과와 효과**

- **분산 환경과 확장성**: 서버 간 캐시 데이터 공유가 가능하여 분산 환경 구축과 확장이 용이합니다.
- **높은 처리량과 안정성**: TPS 유지력이 뛰어나 높은 부하도 원활히 처리하며, 가용성·복제·영속성 옵션으로 안정적인 운영이 가능합니다.
- **유연한 관리**: 캐시 매니저를 통한 설정 관리와 다양한 모니터링 도구를 지원합니다.
- **다양한 데이터 구조 및 호환성**: Hash, List, Set, Sorted Set 등 다양한 데이터 구조를 저장할 수 있으며, 여러 언어와 플랫폼과 호환됩니다.

**주의 사항 / 한계**

- **외부 서버 의존**: 캐시가 외부 서버에 의존합니다.
- **운영 및 유지보수 부담**: 관리 비용과 설정, 직렬화/역직렬화 구현이 필요하며, 튜닝 학습이 요구됩니다.
- **네트워크 위험**: 네트워크 오버헤드 발생 가능성과 장애 시 캐시 접근 지연 또는 실패 위험이 있습니다.
- **초기 지연 가능성**: 캐시 미스 발생 시 초기 지연(콜드 스타트)이 발생할 수 있습니다.

---

### 6. 향후 개선 사항

- **콜드 스타트 문제 해결**
    - 새 게시글 작성 시 캐시에 미리 적재하는 프리히팅 전략 도입합니다.
    - TTL을 동적으로 조절하여 신규 새소식 등록 시 캐싱을 더 오랜 시간(예: 1시간) 동안 유지합니다.
- **장애 대응 강화**
    - 네트워크 장애나 서버 오류 시 예외 처리를 추가합니다.
    - 폴백(fallback) 메커니즘과 타임아웃 설정 강화, 서비스 연속성을 확보합니다.
- **가용성·복제·영속성 옵션 적용**
    - 제공되는 옵션을 적극 도입하여 시스템 안정성과 데이터 무결성 강화합니다.
  
</details>

<details>
<summary>⚡️ 재고 관리 동시성 이슈 해결</summary>
	
### 1. 기능 소개

상품 주문 및 재고 관리 시스템

중거거래 플랫폼에서 용자가 상품을 주문할 때 실시간으로 재고를 차감하고 관리하는 핵심 기능입니다. 다수의 사용자가 동시에 같은 상품을 주문할 때 정확한 재고 계산과 오버셀링 방지가 중요한 비즈니스 요구사항입니다.

---

### 2. 문제 정의

기존 시스템에서는 동시성 제어 메커니즘이 없어 여러 사용자가 동시에 주문할 때 심각한 데이터 불일치 문제가 발생했습니다.

```
상품: 5개
실제 판매: 12개
재고 오차: 7개 
```

주요 문제점

- 동시 접근 시 재고 계산 오류
- 재고보다 많은 수량 판매 (오버셀링)
- 데이터 일관성 깨짐으로 인한 비즈니스 로직 오류

---

### 3. 해결 방안

Redisson 분산 락 + 비관적 락 조합

다중 인스턴스 환경에서 안전한 동시성 제어를 위해 이중 락 메커니즘을 사용

해결 방법

- Redisson 분산 락으로 인스턴스 간 동기화
- DB 레벨 비관적 락으로 데이터 무결성 보장
- REQUIRES_NEW 전파 옵션으로 독립적 트랜잭션 관리

적용 코드
    
```
// 수정 전: 동시성 이슈 발생 코드
public void decreaseStock(Long itemId, Long quantity) {
    Item item = itemRepository.findById(itemId).orElseThrow();
    item.decreaseStock(quantity); // Race Condition 발생 지점
}

// 수정 후: 분산 락 적용
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void decreaseStock(Long itemId, Long quantity) {
    String lockKey = "Lock:" + itemId;
    RLock lock = redissonClient.getLock(lockKey);
    // 분산 락 + 비관적 락으로 안전한 재고 차감
}
```

---

### **4. 성능 테스트**

**Redis 분산락 사용전 후 비교**

동시성 테스트

초기 재고 : 5개

동시 요청 : 100명 각 1번씩 시행

19번 아이템 100명이 1번씩 구매 시도
<img width="269" height="153" alt="image" src="https://github.com/user-attachments/assets/d628be4c-44fa-416a-9f3e-759b52d14ad2" />

시행 결과

분산락 적용 : 5개 성공, 95개 실패, 재고 0개

분산락 미적용 : 12개 성공, 88개 실패, 재고 0개

---

### 5. 해결 완료

동시성 이슈 해결

- **데이터 정합성 100% 보장**: 재고 오차 완전 제거
- **오버셀링 방지**: 재고 한도 내에서만 주문 처리
- **다중 인스턴스 안정성**: MSA 환경에서도 안전한 동시성 제어
- **예외 처리 강화**: 락 획득 실패 시 명확한 에러 메시지 제공

동시성 이슈 해결함으로서 얻어지는 비즈니스 임팩트

- 고객 신뢰도 향상 (재고 오류로 인한 주문 취소 0건)
- 운영팀 업무 효율성 증대 (수동 재고 정정 작업 불필요)
- 매출 손실 방지 (정확한 재고 관리로 판매 기회 극대화)

</details>

---

## 🚨 트러블 슈팅

<details>
<summary>🚨 커넥션풀 고갈 현상</summary>
	
### **1. 문제 상황**

- Mania Place의 **상품 검색 기능** 부하 테스트 중, 동시 사용자 수 증가 시 검색 실패율이 급증하고 응답 시간이 현저히 지연되었습니다.
- 검색 요청 처리 마비의 주된 원인은 **인기 검색어 랭킹 집계 기능이 검색 트랜잭션에 포함되어 있었기 때문**입니다.

---

### **2. 원인 분석**

**[환경]**

**Docker 컨테이너 환경**

(애플리케이션에 CPU 1코어, 메모리 1.5GB 자원 할당)

**[사용 도구]**
Docker Compose, Apache JMeter

**[데이터 조건]**

- **초기 데이터:**  아이템 = 1600개 생성 후 실험

---

**[스레드 속성]**

- **사용자 수**: 30명
- **Ramp-up 시간**: 1초
- **루프 카운트**: 10회

---

**[대상]**

- 아이템 검색

---

**[목적]**

- 오류 발생 확인

<img width="1132" height="70" alt="image" src="https://github.com/user-attachments/assets/ab781d7f-d562-41ab-9103-89afe96481ae" />

jp@gc - Transactions per Second

<img width="1105" height="537" alt="image" src="https://github.com/user-attachments/assets/132330c2-77f5-4c9d-873f-cc132c57a741" />

### **[검색기능]**

- **TPS (초당 처리량):** 1.4 / sec
- **Latency (평균 응답 시간):** 20,468 ms
- **Error Rate (에러율):** 31.33 %

### **[분석 및 주요 문제점]**

- **시스템 마비 수준의 성능:** 31.33%의 높은 에러율과 20초가 넘는 응답 시간은 사실상 시스템이 부하를 전혀 감당하지 못하고 마비 상태에 이르렀음을 의미합니다.
- **DB 커넥션 풀 고갈:** 부하를 견디지 못한 데이터베이스의 응답 지연이 원인이 되어 커넥션 풀이 완전히 고갈되었습니다. 이로 인해 새로운 요청은 트랜잭션조차 시작하지 못하고 실패했습니다.

### **[결론]**

**시스템 마비 발생.** 1초 만에 30명의 동시 사용자가 유입되는 스파이크 트래픽 상황에서, 시스템은 부하를 전혀 감당하지 못하고 사실상 마비 상태에 이르렀습니다.

**[상세 분석]**

- **원인:** 발생한 오류의 100%가 **HikariCP 커넥션 풀 타임아웃**이었습니다.
    
    이는 데이터베이스의 응답이 너무 느려 커넥션을 제때 반납하지 못했기 때문입니다.
  
<img width="1548" height="546" alt="image" src="https://github.com/user-attachments/assets/e87b3c57-c270-4b1d-9ff0-accc4417b4bd" />

- **결과:** 커넥션 부족으로 새로운 요청은 트랜잭션조차 시작하지 못하고 실패했으며, 이로 인해 **31.33%의 요청이 유실**되고, 응답 가능한 요청마저 **평균 20초 이상이 소요**되어 정상적인 서비스 제공이 불가능함을 확인했습니다.
- **애플리케이션 스레드와 DB 커넥션 풀의 자원 불균형:** 60명의 동시 사용자 요청이 유입되자 100개 이상의 애플리케이션 스레드가 생성되었습니다. 하지만 데이터베이스 커넥션 풀(HikariCP)의 최대치는 **10개**로 설정되어 있었습니다.
- **커넥션 병목 현상:** 검색 요청이 발생할 때마다 실행되는 '인기 검색어 카운트 업데이트' 로직으로 인해 DB 트랜잭션이 길어졌습니다. 먼저 커넥션을 차지한 10개의 스레드가 작업을 마칠 때까지 다른 모든 스레드(100개 이상)는 커넥션을 무한정 대기했습니다.
- **타임아웃 및 예외 발생:** 대기하던 스레드들은 결국 커넥션 타임아웃(30초)을 초과하게 되었고, 이로 인해 `SQLTransientConnectionException`이 발생했습니다. Spring/JPA는 이 예외를 `CannotCreateTransactionException`으로 감싸 애플리케이션에 전달, 결과적으로 대량의 검색 실패(에러율 31.33%)로 이어졌습니다.
- **근본 원인:** 검색 기능(읽기)과 랭킹 집계(쓰기)가 **동일한 DB 커넥션 풀을 공유**하며 경합하는 구조가 문제의 핵심이었습니다.

---

### **3. 문제 해결**

1. **커넥션 풀 증설 시도 (10 - > 20 증설)**

<img width="425" height="253" alt="image" src="https://github.com/user-attachments/assets/cdfe2716-d294-44fe-a542-fdd8e7903b7b" />

<img width="1108" height="537" alt="image" src="https://github.com/user-attachments/assets/79fd0da8-353e-4673-b03e-ec648ca8d4a8" />

<img width="1134" height="71" alt="image" src="https://github.com/user-attachments/assets/866255bf-42ca-496c-afdc-72bd32e585b0" />

위 테스트와 동일하게 했을때 정상 작동을 확인했지만

2배 증가한 커넥션 풀만큼 2배의 사용자를 추가 요청 보냈을때(사용자 30 - > 사용자 60)

<img width="1117" height="544" alt="image" src="https://github.com/user-attachments/assets/dc128101-09fe-4cef-b01c-264d24fcb0a4" />

<img width="1135" height="72" alt="image" src="https://github.com/user-attachments/assets/4ce7652e-d497-4376-b301-ba436a67b38c" />

<img width="1546" height="216" alt="image" src="https://github.com/user-attachments/assets/771b6ea8-c00e-47fe-a4ad-19116b601a30" />

똑같은 오류를 확인할 수 있었습니다.

- DB 커넥션 풀(HikariCP) 최대치를 늘려 동시 트랜잭션 처리 능력을 확장
- **결과:** 동시 요청 수가 증가하면 여전히 커넥션이 고갈되고 검색 실패가 발생 → 근본적인 해결책 아님
1. **근본적 해결: Redis 도입**
    - 인기 검색어 집계 기능을 **RDB → Redis ZSet**으로 이전
    - **효과:**
        - Redis의 인메모리 구조로 **빠른 읽기/쓰기 처리** 가능
        - 검색 기능과 랭킹 집계 기능의 **자원 경합 해소**
        - DB는 핵심 검색 트랜잭션 처리에만 집중 가능 → 안정성 확보

###[검색 기능 개선 결과]

<img width="1133" height="70" alt="image" src="https://github.com/user-attachments/assets/08b072b1-efd4-4110-9c30-3a8637bac0b0" />

**jp@gc - Transactions per Second**

<img width="1109" height="541" alt="image" src="https://github.com/user-attachments/assets/98961d8b-ffe7-4e09-a069-f46e8e19918a" />

### **[검색기능/개선 후]**

- **TPS (초당 처리량):** 59.9 / sec
- **Latency (평균 응답 시간):** 380 ms
- **Error Rate (에러율):** 0 %

### **[분석 및 주요 개선점]**

- **획기적인 성능 향상:** 평균 응답 시간이 **380ms**로 크게 단축되었고, 초당 처리량(TPS)은 **약 42배 증가**하여 사용자에게 쾌적한 검색 경험을 제공할 수 있게 되었습니다.
- **시스템 안정성 완벽 확보:** 에러율이 **0%**로 해소되었고, DB 커넥션 병목 현상이 사라져 대규모 트래픽에도 안정적으로 서비스를 운영할 수 있는 기반을 마련했습니다.

---

### **4. 회고**

- **아키텍처 설계의 중요성:** 잦은 쓰기와 실시간 집계 기능을 메인 DB에 통합한 것이 성능 저하의 직접적인 원인이었습니다.
- **자원 이해의 필요성:** 애플리케이션 스레드와 DB 커넥션 풀 간의 상호작용을 이해하는 것이 병목 현상을 해결하는 핵심이었습니다.
- **향후 대응 방안:** 실시간 집계 기능을 개발할 때는 검색 기능과의 의존성을 분리하는 것을 우선적으로 고려해야 합니다.

</details>

<details>
<summary>🚨 태그 저장 동시성 문제</summary>
[https://www.notion.so/teamsparta/24e2dc3ef514805eac6cce2bafe76037](https://www.notion.so/24e2dc3ef514805eac6cce2bafe76037?pvs=21)

본 글은 해당 게시글을 요약한 내용이며, 자세한 내용은 위 링크에서 확인하실 수 있습니다.

### 1. 문제 상황

저희 팀에서 개발 중인 서브컬처 중고거래 사이트에는 개인화 서비스를 위한 '태그' 기능이 있습니다. 

사용자는 관심사에 맞는 태그를 최대 10개까지 설정할 수 있으며, 이 태그는 회원 프로필과 중고 물품에 모두 적용되어 개인 맞춤 상품 추천에 활용됩니다.

**그런데 여러 사용자가 동시에 태그를 저장하는 과정에서 동시성 문제가 발생했습니다.** 특히 회원가입 시점이나 사용자가 태그를 일괄 수정할 때, 물품 등록시 태그를 저장할때 데이터 일관성 문제와 중복 저장 오류가 반복적으로 나타났습니다.

**[ 테스트 시나리오 ]** 
인기 애니메이션 굿즈의 한정판매로 인해 판매 시작 전 대량의 사용자가 동시에 회원가입을 시도하는 상황을 시뮬레이션 했습니다. 약 **100명**의 사용자가 동시에 가입을 진행하며, 이 과정에서 **중복되는 태그를 입력**하는 경우가 발생할 수 있다고 가정했습니다.

![image.png](attachment:878329f4-fd68-44c7-a52b-cbe6acafe5e1:image.png)

- 전체 요청 중 약 16%의 에러율 발생
- 주요 에러 :
1. `Duplicate entry ... for key`
    - 같은 태그명이 이미 존재하는데 INSERT 시도
    - MySQL의 `UNIQUE` 제약조건에 걸려 `SQLIntegrityConstraintViolationException` 발생

![image.png](attachment:3acdc9d9-76a8-431f-9a59-a4df4e649669:image.png)

1. `Deadlock found when trying to get lock`
    - 동시에 여러 트랜잭션이 같은 테이블/인덱스를 갱신하려다가 MySQL의 잠금 경합으로 데드락 발생
    - MySQL이 교착 상태를 감지하고 한 트랜잭션을 강제 롤백

---

### 2. 원인 분석

동시성 발생 주요 원인인 **`findOrCreateTag`** 메서드 코드와 작동 방식 :
    
```
public Tag findOrCreateTag(String tagName) {
		return tagRepository.findByTagName(tagName) // SELECT
			.orElseGet(() -> tagRepository.save(Tag.of(tagName))); //INSERT
	}
```

1. 사용자가 입력한 태그 이름을 DB에서 먼저 조회를 합니다.
2. 존재하지 않으면 해당 태그를 저장합니다.

초기 개발 당시에는 `findOrCreateTag()` 메서드에서 태그의 중복 여부를 확인한 후 저장하는 로직으로 구현했기 때문에, 중복된 태그가 생성될 가능성을 고려하지 못했습니다.

**`SELECT`** 쿼리로 태그를 조회한 후, 태그가 존재하지 않으면 **`INSERT`**쿼리로 태그를 저장하는 방식인데,

이러한 로직을 여러 요청이 동시에 실행할 경우 아래와 같은 문제가 발생한것이었습니다.

```
1번 오류 -> UNIQUE 제약조건 위반 
Thread A : SELECT tag WHERE name='자바' → 없음
Thread B : SELECT tag WHERE name='자바' → 없음

Thread A : INSERT '자바'
Thread B : INSERT '자바'  ← UNIQUE 제약조건 오류 발생 , 롤백

2번 오류 -> 교착상태 발생
Thread A : SELECT tag WHERE name='자바' → 없음
Thread B : SELECT tag WHERE name='스프링' → 없음

Thread A : INSERT '자바'
Thread B : INSERT '스프링'

Thread A : SELECT tag WHERE name='스프링' → 없음
Thread B : SELECT tag WHERE name='자바' → 없음

Thread A : INSERT '스프링' 
Thread B : INSERT '자바' -> 교착상태 발생 , 롤백
```

---

### 3. 문제 해결

### 1. S-lock, X-lock 락 적용

- S Lock과 X Lock 이란?
    
    ## **MySQL의 비관적 락 : S-lock과 X-lock**
    
     → MySQL의 S-lock과 X-lock은 비관적 락을 구현한 것입니다.
    
    ### 기본 개념
    
    | 구분 | 이름 | 설명 | 동시에 접근 가능한 트랜잭션 |
    | --- | --- | --- | --- |
    | **S-lock** | Shared Lock (공유 락) | **읽기 전용 락**. 다른 트랜잭션도 읽기는 가능하지만, 쓰기는 불가능 | **다른 S-lock**은 허용 / X-lock은 불가 |
    | **X-lock** | Exclusive Lock (배타 락) | **쓰기 전용 락**. 읽기·쓰기 모두 다른 트랜잭션이 접근 불가 | 아무도 접근 불가 |
    
    ### 동작 방식
    
    ### S-lock (공유 락)
    
    - 어떤 트랜잭션이 데이터에 S-lock을 걸면, 다른 트랜잭션은 해당 데이터를 **읽을 수는 있지만 수정은 못함**.
    - 예:
        
        ```sql
        SELECT * FROM tags WHERE tag_name = 'choco' LOCK IN SHARE MODE;
        ```
        
        → 다른 트랜잭션은 `UPDATE`나 `DELETE`, `INSERT`(해당 레코드 키 값) 불가.
        
    
    ### X-lock (배타 락)
    
    - 어떤 트랜잭션이 데이터에 X-lock을 걸면, 다른 트랜잭션은 **읽기·쓰기 모두 불가능**.
    - 예:
        
        ```sql
        SELECT * FROM tags WHERE tag_name = 'choco' FOR UPDATE;
        ```
        
        → 해당 행을 다른 트랜잭션이 읽으려고 해도 대기 상태.
        
    
    ### INSERT, UPDATE, DELETE 시 기본 락
    
    - **INSERT**: 새로운 행에 X-lock (배타 락)
    - **UPDATE**: 해당 행에 X-lock
    - **DELETE**: 해당 행에 X-lock
    - **SELECT**: 기본적으로 락 없음 (단, `LOCK IN SHARE MODE` 또는 `FOR UPDATE` 옵션 사용 시 S/X-lock 걸림)
    

가장 먼저 생각한 해결책은 태그 조회 시점에 X-lock을 걸어서 다른 트랜잭션이 동일한 태그에 대해 조회조차 못하게 하는 것이었습니다.

S-lock(공유락)의 경우 여러 트랜잭션이 동시에 같은 데이터를 조회할 수 있기 때문에, 조회 후 각자 INSERT를 시도하면 UNIQUE 제약조건 위반은 막을 수 있겠지만 여전히 데드락이 발생할 것으로 예상했습니다.

예상대로 될지 확인하기 위해 S-lock과 X-lock을 번갈아 적용하며 테스트를 진행했습니다.

- S-lock, X-lock 테스트 과정과 Gap lock
    
    **S-lock 적용 테스트:**
    
    ```java
    @Repository
    public interface TagRepository extends JpaRepository<Tag, Long> {
        @Lock(LockModeType.PESSIMISTIC_READ) // S Lock
        Optional<Tag> findByTagName(String tagName);
        boolean existsByTagName(String tagName);
    }
    ```
    
    `findByTagName` 메서드에 S-lock을 적용하고 동시성 테스트를 수행했습니다.
    
    **테스트 결과 :**
    
    ![image.png](attachment:ebb51dc3-2d94-4920-afaf-4ae4f61c9353:image.png)
    
    ![image.png](attachment:7e08c120-2c06-4328-b789-3a75e9d58d0b:image.png)
    
    - 예상대로 **데드락이 발생**했습니다.
    
    **콘솔 시뮬레이션 :**
    
    ![트랜잭션 A](attachment:513e0ccc-c237-4da3-a558-a02c57fecc5f:image.png)
    
    트랜잭션 A
    
    ![트랜잭션 B](attachment:c1900238-0656-4874-90d3-c432fb4f24bf:image.png)
    
    트랜잭션 B
    
    콘솔에서 직접 시뮬레이션한 결과, 두 트랜잭션이 동시에 'hojun'이라는 동일한 태그명에 대해 S-lock을 획득한 후 각자 INSERT를 시도하면서 데드락에 빠지는 것을 확인했습니다. S-lock으로는 이 문제를 방지할 수 없다는것을 확인했습니다.
    
    **X-lock 적용 테스트:**
    
    ```java
    @Repository
    public interface TagRepository extends JpaRepository<Tag, Long> {
        @Lock(LockModeType.PESSIMISTIC_WRITE) // X Lock
        Optional<Tag> findByTagName(String tagName);
        boolean existsByTagName(String tagName);
    }
    ```
    
    S-lock과 동일하게 `findByTagName` 메서드에 **X-lock**을 적용하고 동시성 테스트를 수행했습니다.
    
    **테스트 결과 :**
    
    ![image.png](attachment:2f7a89f7-3f00-4c10-a382-3300fb3aacfe:image.png)
    
    ![image.png](attachment:e656c188-2809-401f-9507-d92da4438ccc:image.png)
    
    - 예상과 다르게 이전과 똑같이 **데드락이 발생**했습니다.
    
    **콘솔 시뮬레이션 :**
    
    ![트랜잭션 A](attachment:f48df497-208a-4df6-81e3-bcbaaabb32f9:image.png)
    
    트랜잭션 A
    
    ![트랜잭션 B](attachment:13476fbe-b405-4a29-8436-e1d21e091aa9:image.png)
    
    트랜잭션 B
    
    처음 시뮬레이션했을 때 의아했던 점은, 트랜잭션 A에서 **X-lock**을 걸었음에도 불구하고 트랜잭션 B에서 같은 태그를 조회하는 것이 가능했다는 것입니다. 그리고 INSERT 단계에서는 S-lock과 동일한 메커니즘으로 데드락이 발생했습니다.
    
    '쿼리문을 잘못 작성했나?' 하는 의문이 들어 다시 검토해보니, **계속 SELECT로 조회하던 태그는 데이터베이스에 존재하지 않는 값**이었습니다.
    
    **검증 테스트:**
    이미 존재하는 태그에 X-lock을 걸고 조회해보니, 다른 트랜잭션에서는 조회가 블록되는 것을 확인할 수 있었습니다. 그렇다면 존재하지 않는 태그를 INSERT할 때는 어떻게 락이 작동했던 걸까요?
    
    현재 걸려있는 락의 종류를 확인하기 위해 다음 쿼리를 실행했습니다
    
    ```sql
    SELECT object_schema, object_name, lock_type, lock_mode FROM performance_schema.data_locks;
    ```
    
    - S-lock 적용시
    
    ![image.png](attachment:23677733-81b6-46da-a201-cf4bb8d353b4:image.png)
    
    - X-lock 적용시
    
    ![image.png](attachment:22b87f85-c9ee-4279-8708-d8761090b487:image.png)
    
    S락과 X락만 걸린게 아닌 **GAP** 이라는 Lock이 걸린것을 확인했습니다.
    
    **Gap Lock의 작동 원리 :**
    
    ![인덱스 레코드 사이에 존재한 Gap들](attachment:44bff36e-26db-4071-b026-bd9bf16a65d9:image.png)
    
    인덱스 레코드 사이에 존재한 Gap들
    
    - Gap Lock은 인덱스 레코드 사이의 "빈 공간"에 걸리는 락입니다. 위 사진에서는 레코드 사이에 1,2,3,4번에 해당하는 공간을 지칭합니다. 존재하지 않는 태그명을 조회할 때 해당 인덱스 갭에 락이 걸려서, 그 범위에 새로운 레코드가 삽입되는 것을 방지합니다.
    - 이로 인해 두 트랜잭션이 동일한 갭에 서로 다른 락을 요청하면서 데드락이 발생하게 됩니다.
    

**결론:**
S-lock과 X-lock 모두 Gap Lock 메커니즘으로 인해, 처음 생성되는 태그들에 대한 동시 INSERT 시 데드락 문제를 근본적으로 해결할 수 없음을 확인했습니다.

### 2. INSERT IGNORE

### 3. @Retryable을 활용한 재시도 메커니즘

**REQUIRES_NEW를 통한 새로운 트랜잭션 처리 시도:**

락 기반 해결책의 한계를 확인한 후, 태그 저장 부분만 새로운 트랜잭션으로 분리하여 

: `@Transactional(propagation = Propagation.REQUIRES_NEW)` 

오류 발생 시 재시도하는 방법을 시도했습니다.
하지만 이 방법은 예상과 달리 제대로 작동하지 않았습니다. 원인을 분석해보니, 새로운 트랜잭션 생성으로 인해 

**커넥션 풀 부족 현상**이 발생했습니다.

- 기존 트랜잭션(회원가입/물품등록)이 커넥션을 점유한 상태
- 태그 저장용 새로운 트랜잭션이 추가 커넥션을 요구
- 동시 접속자가 많을 때 사용 가능한 커넥션 부족으로 아예 처리가 불가능한 상황 발생

**@Retryable 어노테이션 적용:**

그래서 트랜잭션을 분리하지 않고, 기존의 단일 트랜잭션 내에서 동시성 오류 발생 시 **재시도**하는 방향으로 접근했습니다. Spring에서 제공하는 `@Retryable` 어노테이션을 활용했습니다.

```
	@Retryable(
		retryFor = {CannotAcquireLockException.class, SQLTransientException.class,
		 IllegalStateException.class},
		maxAttempts = 3, // 기본값
		backoff = @Backoff(delay = 200) // ms 단위
	)
	@Transactional
	public UserRegisterResponse register(UserRegisterRequest userRegisterRequest,
	 UserRole userRole) {
				... // 회원가입 로직
		}
```

**@Retryable의 동작 방식:**

- `retryFor`: 지정된 예외(락,  발생 시에만 재시도 수행
- `maxAttempts`: 최대 재시도 횟수 설정 (기본값 3회)
- `backoff`: 재시도 간격 설정 (200ms 지연으로 순간적인 동시성 충돌 회피)

![image.png](attachment:e8f1d55a-ea9c-4e52-8ec5-f059d9ac0256:image.png)

![image.png](attachment:027df347-a872-4e5f-9b28-f344a07d3b55:image.png)

**테스트 결과:**
동시성 오류가 발생했을 때 200ms 딜레이를 두고 자동으로 재시도하게 함으로써, JMeter 테스트에서 모든 회원가입과 물품등록이 성공적으로 완료되는 것을 확인했습니다.

**@Retryable 의 장점 :**

- 복잡한 메커니즘 없이 간단한 설정으로 동시성 문제 해결가능
- 기존 트랜잭션 구조 유지로 커넥션 풀 부족 문제 방지

**@Retryable 의 단점 :**

**근본적 해결이 아닌 우회 방식**

- 동시성 문제의 원인을 제거한 것이 아니라 오류 발생 시 재시도로 회피하는 방식
- 동시 접속자가 매우 많아질 경우 재시도 횟수가 증가하여 성능 저하 가능성

**예측 불가능한 응답 시간**

- 재시도로 인해 사용자 요청 처리 시간이 불규칙해짐 (200ms ~ 600ms 추가 지연 가능)
- 현재는 3번 반복으로 문제가 안생겼지만 최악의 경우 3번 모두 실패하면 여전히 에러 발생 가능

---

### 4. 회고

**개선방향 :**

현재의 `@Retryable` 방식은 임시방편이라고 판단되어, 향후에는 MySQL의 구문을 활용하여 데이터베이스 레벨에서 **동시성 문제를 근본적으로 해결**하거나, Redis를 활용한 태그 캐싱 및 분산 락 구현을 통해 더 정교한 동시성 제어를 도입할 예정입니다. 또한 인기 태그 사전에 생성(현실적으로 가장 쉬운 방법)하여 중복을 없애는 방법과 재시도 빈도 테스트하여 동시성 충돌 자체를 예방하고 시스템 성능을 지속적으로 관찰할 계획입니다. 

**느낀점 :** 

앞으로 새로운 비즈니스 로직을 설계할 때는 동시성과 트랜잭션을 한 번 더 꼼꼼히 고려한 뒤에 구현해야겠다고 느꼈습니다.

</details>

<details>
<summary>🚨 Redis 직렬화 문제</summary>
	
### 1. 문제 상황

처음에는 Redis 캐시 매니저에 **null 값 캐싱 방지**와 **TTL 만료 시간**만 설정한 뒤,
새소식 전체 조회 메서드에 `@Cacheable`을 적용해 캐싱을 구현했습니다.

하지만 새소식 전체 조회를 호출하자 다음과 같은 오류가 발생했습니다.

- 최초 코드
    
    ```java
    // build.gradle
    dependencies {
    	// redis 캐시
    	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    
    	//json 직렬화를 위한 jackson
    	implementation 'com.fasterxml.jackson.core:jackson-databind'
    	implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'
    }
    ```
    
    ```java
    @Configuration
    @EnableCaching
    public class CacheConfig {
    
    	@Bean
    	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    		RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
    			.disableCachingNullValues() // null은 캐시에 저장하지 않음
          .entryTtl(Duration.ofMinutes(10)); // TTL 10분
    
    		return RedisCacheManager.builder(redisConnectionFactory)
    			.cacheDefaults(cacheConfig)
    			.build();
    	}
    }
    ```
    
    ```java
    @Service
    @RequiredArgsConstructor
    public class NewsfeedService {
    
    	private final NewsfeedRepository newsfeedRepository;
    	private final UserService userService;
    	private final ImageService imageService;
    	
    	@Loggable
    	@Transactional(readOnly = true)
    	@Cacheable(
    		value = "listCache",
    		key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()"
    	)
    	public PageResponseDto<NewsfeedListResponse> getAllNewsfeeds(Pageable pageable) {
    
    		// 새소식 전체 조회(소프트딜리트 빼고)
    		Page<Newsfeed> pagedNewsfeeds = newsfeedRepository.findByIsDeletedFalseWithFetchJoin(pageable);
    
    		// 페이지에 들어갈 대표 이미지 일괄 조회
    		Map<Long, Image> mainImageMap = imageService.getMainImagesForNewsfeeds(pagedNewsfeeds);
    
    		// NewsfeedListResponse에 적용 (+ 이미지 맵핑)
    		Page<NewsfeedListResponse> dtoPage = pagedNewsfeeds.map(newsfeed -> {
    			Image mainImage = mainImageMap.getOrDefault(newsfeed.getId(), null);
    			return NewsfeedListResponse.of(newsfeed, mainImage.getImageUrl());
    		});
    
    		return new PageResponseDto<>(dtoPage);
    	}
    }
    ```
    

```java
2025-08-12 14:52:35.328 15137 ERROR [http-nio-8080-exec-3] o.a.c.c.C.[.[.[.[dispatcherServlet] - Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.springframework.data.redis.serializer.SerializationException: Cannot serialize] with root cause
java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.example.place.common.dto.PageResponseDto]
```

---

### 2. 원인 분석

Redis 캐싱 과정에서 객체 직렬화 설정이 없어서 기본 `JdkSerializationRedisSerializer` 방식이 사용되었고, 이로 인해 DTO가 직렬화/역직렬화되지 못해 예외가 발생하였습니다.

---

### 3. 문제 해결

1. **Spring CacheManager 직렬화 방식을 JSON으로 변경**
    
    ```java
    // CacheConfig.cacheManager()
    
    // 캐시 설정
    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
      .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // 키는 문자열 직렬화
      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) // 값은 JSON 직렬화
      .disableCachingNullValues() // null은 캐시에 저장하지 않음
      .entryTtl(Duration.ofMinutes(10)); // TTL 10분
    ```
    
    - 직렬화 방식을 지정:
        - 키 직렬화는 StringRedisSerializer 사용해 문자열로 지정
        - 값 직렬화 GenericJackson2JsonRedisSerializer 사용해 Json형태로 지정
2. **가변 리스트 변환**
    
    ```java
    // newsfeedService.getAllNewsfeeds()
    
    List<NewsfeedListResponse> contentList = pagedNewsfeeds.stream().map(newsfeed -> {
        Image mainImage = mainImageMap.getOrDefault(newsfeed.getId(), null);
        return NewsfeedListResponse.of(newsfeed, mainImage != null ? mainImage.getImageUrl() : null);
    })
    .collect(Collectors.toList()); // 가변 리스트로 변환
    
    return new PageResponseDto<>(
        new PageImpl<>(contentList, pageable, pagedNewsfeeds.getTotalElements())
    );
    ```
    
    - `Page`의 `content`가 불변 리스트일 경우 직렬화 과정에서 문제가 발생할 수 있다고 판단하여, `Collectors.toList()`를 사용해 가변 리스트로 변환 후 응답값으로 전달
3. **LocalDateTime 직렬화 처리**
    
    ```java
    // CacheConfig.cacheManager()
    
    // 자바 타임 모듈 등록하여 LocalDateTime -> ISO-8601 형태로 직렬화
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);
    
    // 캐시 설정
    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
    	.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // 키는 문자열 직렬화
    	.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer)) // 값은 JSON 직렬화
    	.disableCachingNullValues() // null은 캐시에 저장하지 않음
    	.entryTtl(Duration.ofMinutes(10)); // TTL 10분
    ```
    
    - `java.time.LocalDateTime`은 기본 설정에서 직렬화가 지원되지 않아 여전히 예외가 발생
    - 이를 해결하기 위해 `JavaTimeModule`을 등록하고, `WRITE_DATES_AS_TIMESTAMPS` 옵션을 비활성화해 ISO-8601 문자열로 직렬화/역직렬화되도록 설정
4. **타입 정보 유지**
    
    ```java
    // CacheConfig.cacheManager()
    
    // 타입 정보 포함 (직렬화/역직렬화 시 클래스 정보 유지)
    mapper.activateDefaultTyping(
    	LaissezFaireSubTypeValidator.instance,
    	ObjectMapper.DefaultTyping.NON_FINAL,
    	JsonTypeInfo.As.PROPERTY
    );
    ```
    
    - 직렬화 가능. 
    그러나 여전히 역직렬화 시 **런타임에 제네릭 타입 정보**를 제대로 알 수 없어,
    Jackson이 `LinkedHashMap` 같은 기본 구조로 변환해버리는 문제 발생
    - 이를 방지하기 위해 `ObjectMapper.activateDefaultTyping`을 사용해 JSON에 클래스 타입 정보를 함께 저장하도록 설정
    1. **DTO 생성자에 @JsonCreator/@JsonProperty 적용**
    
    ```java
    // PageResponseDto
    
    @JsonCreator
    private PageResponseDto(
    	@JsonProperty("content") List<T> content,
    	@JsonProperty("page") int page,
    	@JsonProperty("totalPages") int totalPages) {
    	this.content = content;
    	this.page = page;
    	this.totalPages = totalPages;
    }
    ```
    
    - 불변 객체나 `final` 필드만 있는 DTO는 Jackson이 기본 생성자 없이 역직렬화할 수 없는 문제 발생
    - `@JsonCreator`와 `@JsonProperty`를 사용해 역직렬화 시 필드 매핑이 가능하도록 명시

- 최종 코드
    
    ```java
    // build.gradle
    dependencies {
    	// redis 캐시
    	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    
    	//json 직렬화를 위한 jackson
    	implementation 'com.fasterxml.jackson.core:jackson-databind'
    	implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'
    }
    ```
    
    ```java
    @Configuration
    @EnableCaching
    public class CacheConfig {
    
    	@Bean
    	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    		// 자바 타임 모듈 등록하여 LocalDateTime -> ISO-8601 형태로 직렬화
    		ObjectMapper mapper = new ObjectMapper();
    		mapper.registerModule(new JavaTimeModule());
    		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
    		// 타입 정보 포함 (직렬화/역직렬화 시 클래스 정보 유지)
    		mapper.activateDefaultTyping(
    			LaissezFaireSubTypeValidator.instance,
    			ObjectMapper.DefaultTyping.NON_FINAL,
    			JsonTypeInfo.As.PROPERTY
    		);
    
    		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);
    
    		// 캐시 설정
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // 키는 문자열 직렬화
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer)) // 값은 JSON 직렬화
            .disableCachingNullValues() // null은 캐시에 저장하지 않음
            .entryTtl(Duration.ofMinutes(10)); // TTL 10분
    
        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(cacheConfig) // 캐시 설정
            .build();
    	}
    }
    ```
    
    ```java
    @Service
    @RequiredArgsConstructor
    public class NewsfeedService {
    
    	private final NewsfeedRepository newsfeedRepository;
    	private final UserService userService;
    	private final ImageService imageService;
    	
    	@Loggable
    	@Transactional(readOnly = true)
    	@Cacheable(
    		value = "listCache",
    		key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()"
    	)
    	public PageResponseDto<NewsfeedListResponse> getAllNewsfeeds(Pageable pageable) {
    
    		// 새소식 전체 조회(소프트딜리트 빼고)
    		Page<Newsfeed> pagedNewsfeeds = newsfeedRepository.findByIsDeletedFalseWithFetchJoin(pageable);
    
    		// 페이지에 들어갈 대표 이미지 일괄 조회
    		Map<Long, Image> mainImageMap = imageService.getMainImagesForNewsfeeds(pagedNewsfeeds);
    
    		List<NewsfeedListResponse> contentList = pagedNewsfeeds.stream().map(newsfeed -> {
            Image mainImage = mainImageMap.getOrDefault(newsfeed.getId(), null);
            return NewsfeedListResponse.of(newsfeed, mainImage != null ? mainImage.getImageUrl() : null);
        })
        .collect(Collectors.toList()); // 가변 리스트로 변환
    
        return new PageResponseDto<>(
            new PageImpl<>(contentList, pageable, pagedNewsfeeds.getTotalElements())
        );
    	}
    }
    ```
    
    - JPA에서 반환하는 `Page.getContent()`는 보통 불변 리스트로 감싸져 있어서, 직접 캐시에 넣으면 직렬화 문제가 발생할 수 있음.
    - `PageResponseDto` 내부 `content` 필드는 `Page.getContent()`를 받아서 초기화하므로, 전달하는 `Page`의 리스트가 가변 리스트여야 함.
    - 서비스 레이어에서 `Page<T>`의 내용을 가변 리스트로 새로 수집 후, 그 리스트를 사용해 `PageImpl`을 새로 만들면, 그 내부 리스트는 가변 리스트가 됨.
    - `PageImpl`은 리스트를 복사하거나 불변으로 감싸지 않기 때문에, 개발자가 넘긴 가변 리스트를 그대로 유지함.

---

### 4. 회고

이 과정을 통해 Redis 캐시에서 DTO를 안전하게 저장·조회하려면,

값 직렬화 시 JSON 변환 + 타입 정보 포함 + 역직렬화를 위한 생성자 설정이 함께 고려되어야 한다는 것을 알 수 있었습니다.
    
</details>

<details>
<summary>🚨 분산 락 구현 시 트랜잭션 전파 이슈</summary>

### 1. 문제 상황

분산 락과 트랜잭션 경계 충돌 문제
분산 락을 도입하면서 기존 트랜잭션 관리 방식과 충돌이 발생했습니다. 특히 `OrderService`에서 `StockService`를 호출할 때 트랜잭션 경계와 락의 생명주기가 맞지 않아 예상과 다른 동작이 발생했습니다.

발생 문제

- 락이 해제된 후 트랜잭션이 커밋되어 동시성 제어가 무의미해짐
- 트랜잭션 롤백 시 락은 이미 해제되어 다른 스레드가 잘못된 데이터에 접근
- 외부 트랜잭션과 내부 트랜잭션의 경계가 모호하여 데이터 일관성 문제 발생

문제 발생 구간

```jsx
// 문제 발생 구간
@Transactional  // 외부 트랜잭션 시작
public CreateOrderResponseDto createOrder(...) {
    
    stockService.decreaseStock(itemId, quantity); // 내부에서 락 획득/해제
    
    // 주문 생성 및 저장
    orderRepository.save(order);
    
    // 외부 트랜잭션 커밋 (락은 이미 해제된 상황)
}
```

문제의 시퀀스

1. 사용자A: 외부 트랜잭션 시작 → 락 획득 → 재고 차감 → 락 해제
2. 사용자B: 락 획득 가능 → 재고 차감 (사용자A 트랜잭션 미커밋 상태)
3. 사용자A: 트랜잭션 커밋
4. 사용자B: 트랜잭션 커밋
5. **결과**: 동시성 제어 실패, 재고 오차 발생

---

### 2. 원인 분석

트랜잭션 전파 방식의 문제

- 기존에는 기본 전파 방식인 `REQUIRED`를 사용했는데, 이는 외부 트랜잭션에 참여하는 방식이었습니다.

### 트랜잭션과 락의 생명주기 불일치

- **락의 생명주기**: 메서드 실행 시간 동안만 유지
- **트랜잭션 생명주기**: 외부 메서드 완료까지 유지
- **결과**: 락이 해제된 후에도 트랜잭션이 살아있어 데이터 일관성 보장 불가

---

### 3. 문제 해결

### REQUIRES_NEW 전파 옵션 적용

독립적인 트랜잭션을 생성하여 락과 트랜잭션의 생명주기를 일치시켰습니다.

```jsx
// 해결된 코드
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void decreaseStock(Long itemId, Long quantity) {
    String lockKey = "Lock:" + itemId;
    RLock lock = redissonClient.getLock(lockKey);
    
    try {
        boolean acquired = lock.tryLock(3000, 10000, TimeUnit.MILLISECONDS);
        if (!acquired) {
            throw new CustomException(ExceptionCode.STOCK_LOCK_FAILED);
        }
        
        try {
            // 독립적인 트랜잭션에서 DB 작업 수행
            Item item = itemRepository.findByIdWithLock(itemId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ITEM));
            
            item.decreaseStock(quantity);
            // 메서드 종료 시 트랜잭션 즉시 커밋
            
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 트랜잭션 커밋 후 락 해제
            }
        }
        
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new CustomException(ExceptionCode.OPERATION_INTERRUPTED);
    }
}
```

개선사항

**1. 트랜잭션 격리**

- 재고 관리 로직을 독립적인 트랜잭션으로 분리
- 락 해제 전에 데이터베이스 변경사항 확실히 커밋
- 외부 트랜잭션과 무관하게 재고 처리 완료

**2. 원자성 보장**

- 락 획득 → DB 작업 → 트랜잭션 커밋 → 락 해제 순서 보장
- 중간에 실패 시 트랜잭션 롤백 후 락 해제
- 다른 스레드는 완전히 처리된 데이터만 접근 가능

---

### 4. 회고

잘했던 점

1. 문제 분석
- 단순히 "동시성 문제"로 끝내지 않고 트랜잭션 전파 방식까지 깊이 있게 분석
- 락과 트랜잭션의 생명주기 차이를 정확히 파악하고 해결방안 도출
1. 안정성 고려
- REQUIRES_NEW로 인한 성능 오버헤드를 인지하면서도 데이터 일관성을 우선시함

아쉬운점

1. 초기 설계 단계에서 지식부족

- 분산 락 도입 시 트랜잭션 전파 방식에 대한 사전 검토 부족
- 동시성 테스트 시나리오를 충분히 구성하지 못해 늦은 발견  

</details>

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

## 🧭 향후 개선 계획

중고거래의 완성도를 높이기 위해 실제 택배사 api 연동을 고려했으나,
api인증 절차와 테스트의 어려움이 있어 이번 프로젝트에서는 구현하지 못하였습니다.

검색어 랭킹 기능 최적화

테스트 코드를 더욱 보강하고, 실제 운영 환경을 가정한 다양한 테스트를 통해 시스템의 안정성을 높이고 싶습니다. 

회원가입 API의 성능이 다른 기능들에 비해 현저히 느린 문제가 있었습니다. 또한 프로젝트의 핵심 기능인 결제 시스템에서 실제 결제 API 연동을 완전히 구현하지 못해 완성도가 아쉬웠습니다.

캐싱 fallback 메커니즘 구축 **pre heating 시스템 구축**

**모니터링 시스템 알림 체계를 구축하는 방안이 필요해 보입니다.**

**채팅 기능 예외 처리 보강**  
메시지 전송 실패 시 재시도 로직이나 데드 레터 큐(Dead Letter Queue)로 개선 계획이 있습니다.

---
