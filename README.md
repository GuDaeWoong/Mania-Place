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
    
```
```

```
```

```
```

```
```
</details>

<details>
<summary>첫번째토글</summary>

    
```
```

```
```

```
```

```
```
</details>

<details>
<summary>첫번째토글</summary>

    
```
```

```
```

```
```

```
```
</details>


- [⚡️ 인기 검색어 랭킹](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2532dc3ef5148060a259f2725a3702fc)

- [⚡️ 캐싱을 이용하여 새소식 조회를 더욱 빠르게!](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2532dc3ef5148095965ac33c8bc36536)

- [⚡️ 재고 관리 동시성 이슈 해결](https://www.notion.so/teamsparta/5-Mania-Place-2462dc3ef51480ac98a0d38eace19c50?source=copy_link#2532dc3ef5148097af2ff6da3e4576a8)

---

## 🚨 트러블 슈팅

<details>
<summary>첫번째토글</summary>

    
```
```

```
```

```
```

```
```
</details>

<details>
<summary>첫번째토글</summary>

    
```
```

```
```

```
```

```
```
</details>

<details>
<summary>첫번째토글</summary>

    
```
```

```
```

```
```

```
```
</details>

<details>
<summary>첫번째토글</summary>

    
```
```

```
```

```
```

```
```
</details>
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
