# 3회차: 기능 개발과 GitHub 협업

> Claude Code 개발자 교육과정 · 3회차 (2026-11-05)
> 과정 조회·관리 API(FR-01~03)를 **탐색 → 계획 → 구현 → 검증**으로 만들고 PR로 병합한다.
> 120분 · 강의 40% + 따라하기 60% · 모듈 6, 7, 8

## 진행 순서

| 시간 | 내용 | 형식 |
|---|---|---|
| 0:00 – 0:08 | 지난 회차 복습 & 과제 리뷰 | 강의 + 공유 |
| 0:08 – 0:23 | 1. 기능 개발 워크플로우, 버그는 재현 테스트부터 | 강의 + 시연 |
| 0:23 – 0:35 | 2. GitHub 협업: main 보호, PR 템플릿, 리뷰, 충돌 | 강의 |
| 0:35 – 0:47 | 3. 세션과 CLAUDE.md 관리 | 강의 + 시연 |
| 0:47 – 0:55 | 휴식 | — |
| 0:55 – 1:37 | 4. 따라하기 ①: 저장소 보호 → FR-01~03 백엔드 → 목록 화면 → PR | 실습 |
| 1:37 – 1:52 | 5. 따라하기 ②: 버그 수정(재현 테스트부터)과 충돌 해결 | 실습 |
| 1:52 – 2:00 | 정리, 퀴즈, 과제 안내 | 강의 |

## 오늘의 목표

1. 기능 하나를 탐색 → 계획 → 구현 → 검증 순서로 Claude와 끝까지 개발한다
2. 버그는 재현 테스트부터 만들고 고치는 습관을 들인다
3. main 보호, PR 템플릿, 동료 리뷰, 충돌 해결로 팀 단위 GitHub 협업을 시작한다
4. 세션은 작업 단위로, CLAUDE.md는 루트·백엔드·프론트로 나눠 짧게
5. EduHub 과정 조회·관리 API(FR-01~03)와 과정 목록 화면을 PR로 병합한다

## 2회차 복습

- 한 세션 = 한 작업, 위치를 알려 주고, 큰 일은 Plan 모드로 계획부터
- AI는 How, 개발자는 What·Why·Where: 지식이 있어야 문제가 보인다
- Git은 2단계 커밋 후 push, 작업은 브랜치 → 개발 → PR → 병합

과제 점검: 2회차 PR이 main에 병합되었나 / `.env`가 저장소에 올라가지 않았나 / 요구사항의 애매한 점 3가지 결정 / 회고(맡긴 것·직접 한 것·아낀 방법)

---

## 1. 기능 개발 워크플로우

### 탐색 → 계획 → 구현 → 검증

① 탐색(수정 금지) → ② 계획(Plan 모드) → **사람이 계획 검토** → ③ 구현(작은 단위로) → ④ 검증(테스트·diff)

| 단계 | Claude에게 시키는 것 | 개발자가 확인 | 토큰 습관 |
|---|---|---|---|
| ① 탐색 | 관련 파일·기존 패턴 설명만, 수정 금지 | 내가 아는 구조와 같나 | `@`로 시작 위치 지정 |
| ② 계획 | 만들 파일, 순서, 테스트 목록 | 패키지, 계층, 범위 | 여기서 잡는 게 가장 싸다 |
| ③ 구현 | 계획의 한 덩어리씩, 테스트와 함께 | 방향이 이상하면 Esc | 이상하면 바로 멈추기 |
| ④ 검증 | 테스트·빌드 명령 실행 | 내 터미널에서 직접 | 완료 기준을 명령어로 |

### 테스트로 완료 기준을 준다 (TDD 맛보기)

- 🔴 **Red**: 사람이 실패하는 테스트 작성 (요구사항을 코드로)
- 🟢 **Green**: Claude가 통과하도록 구현 (테스트 수정 금지)
- 🔵 **Refactor**: 구조 정리, 테스트는 계속 통과

```text
CourseServiceTest에 "종료된 과정은 수정할 수 없다" 테스트를 추가해 두었어.
이 테스트가 통과하도록 CourseService를 구현해줘.
- 테스트 코드는 수정하지 마
- 완료 기준: ./gradlew test --tests "*CourseServiceTest" 통과
```

> 모든 테스트를 사람이 쓸 필요는 없다. **핵심 업무 규칙의 테스트만큼은 사람이** 쓴다.

### 버그는 재현 테스트부터

① 증상(재현 절차, 기대·실제, 에러 원문) → ② 원인("수정하지 말고 원인만") → ③ 재현 테스트(지금은 실패 🔴) → ④ 수정(재현 테스트 통과 🟢) → ⑤ 전체 테스트(다른 곳은?) → ⑥ 커밋·PR(`fix(범위): …`)

| 단계 | 지시 예 | 왜 |
|---|---|---|
| ② 원인 | "첫 페이지에 11번부터 나와. 원인과 위치만 찾아줘. 수정하지 마" | 원인을 사람이 이해해야 방향을 판단 |
| ③ 재현 | "이 버그를 재현하는 테스트를 먼저. 지금은 실패해야 해" | 실패를 봐야 버그를 잡았는지 안다 |
| ④ 수정 | "재현 테스트가 통과하도록. 원인이 프론트면 프론트만" | 엉뚱한 곳을 고치지 않게 |

> 원인이 한 줄이면 직접 고친다: 재현 테스트는 Claude가, 한 줄 수정은 사람이.

### 오늘 만들 기능: FR-01~03 과정 조회·관리

| ID | 요구사항 | API·화면 |
|---|---|---|
| FR-01 | 과정 목록, 제목·분류 검색 (페이지) | `GET /api/v1/courses?keyword=&category=&page=0` + 목록 화면 |
| FR-02 | 과정 상세와 회차 | `GET /api/v1/courses/{id}` |
| FR-03 | 과정·회차 등록·수정·종료 | `POST /courses`, `PUT /{id}`, `POST /{id}/sessions`, `POST /{id}/close` |

계층: `course-mfe`(목록 화면, MUI Table) → `CourseController`(`@Tag` `@Operation`) → `CourseService`(`@Transactional`) → `CourseRepository`(Spring Data JPA) → PostgreSQL(Flyway V1)

화면은 생성된 Axios 클라이언트(`courseApi.getCourses()`)로 호출하고, 응답은 DTO(record)로 받는다.

---

## 2. GitHub 협업

**AI가 만든 코드도 사람이 확인하고 병합한다.**
main 보호(직접 푸시 금지) · PR 템플릿(빠뜨리지 않게) · 사람 리뷰(Claude는 보조) · 충돌 해결(의도를 아는 사람이)

### 팀 협업 흐름

| 팀원 A (작성자) | 팀원 B (리뷰어) |
|---|---|
| 1. Claude와 계획 → 구현 → 테스트 | |
| 2. diff 검토, 직접 테스트 실행 | |
| 3. push + PR (템플릿 작성) | |
| | 4. 리뷰 요청 알림 → 브랜치 받아 직접 실행 |
| | 5. 리뷰 코멘트 (Claude 리뷰는 참고용) |
| 6. 리뷰 반영 → push (PR 갱신) | |
| | 7. Approve |
| 8. Squash and merge | |

### main 보호: AI 시대의 안전장치

| 설정 | 효과 | 왜 필요한가 |
|---|---|---|
| PR 없이 main 직접 푸시 금지 | 모든 변경이 PR을 거친다 | Claude가 실수로 푸시해도 막힌다 |
| 승인 1명 이상 | 사람 리뷰 없이 병합 불가 | AI 코드도 사람이 확인 |
| 강제 푸시·삭제 금지 | 이력을 다시 쓸 수 없다 | 공유 이력 보호 |
| 상태 검사 통과 (CI) | 빌드·테스트 실패 시 병합 불가 | 6주차 CI 이후 켠다 |
| Squash merge만 허용 | PR 하나 = 커밋 하나 | 읽고 되돌리기 쉽다 |

- 설정 위치: **Settings → Branches** 또는 **Rules → Rulesets**. 병합 방식은 **Settings → General → Pull Requests**
- 주의: 개인 무료 계정의 Private 저장소에서는 보호 기능이 제한될 수 있다 (공식 문서 확인 필요)

### PR 템플릿: `.github/pull_request_template.md`

```markdown
## 관련 요구사항
## 변경 내용
## API 변경 (Swagger 경로)
## 확인 방법
## Claude에게 맡긴 것 / 직접 한 것
## 체크리스트
- [ ] 내 터미널에서 ./gradlew test 통과
- [ ] 요청하지 않은 파일·기능 없음
- [ ] .env 등 비밀 정보 없음
- [ ] 구조를 AI 없이 설명할 수 있다
```

- PR을 만들 때 본문에 자동으로 채워지고, Claude도 이 형식에 맞춰 PR 본문을 쓴다
- "맡긴 것 / 직접 한 것"과 "AI 없이 설명" 체크는 **직접** 쓴다

### 리뷰: 사람이 기본, Claude는 보조

| 누가 | 무엇을 본다 | 방법 |
|---|---|---|
| 작성자 | 요청 안 한 변경, 비밀 정보, 테스트 실행 | diff 직접 검토, 내 터미널에서 테스트 |
| Claude (보조) | 규칙 위반, 빠진 예외 처리, 테스트 누락 | 새 세션에서 "수정 말고 문제만 목록으로" |
| 동료 (필수) | 요구사항·구조에 맞나, 이해할 수 있나 | 브랜치를 받아 직접 실행, 코멘트 |

- ❌ "이상해요", "테스트 더 필요"
- ✅ **위치 + 규칙/요구사항 + 제안**: "CourseController가 CourseRepository를 직접 호출해요. CLAUDE.md 계층 규칙에 따라 Service를 거치도록 바꿔 주세요"

### 충돌 해결: 의도를 아는 사람이 확인한다

두 팀원이 같은 파일(CourseController, CourseService)을 동시에 고치면 나중에 병합하는 쪽에서 충돌이 난다.

1. `git switch 내-브랜치`
2. `git merge main`
3. 충돌 파일 확인
4. 두 의도 모두 살려 정리
5. 테스트
6. add → commit → push

```text
main을 병합하면서 생긴 충돌을 해결해줘. 양쪽 변경의 의도를 설명하고,
둘 다 살리는 방향으로. 끝나면 테스트를 돌려줘
```

> 반드시 확인: 한쪽 변경이 조용히 사라지지 않았나? 상대 팀원에게 결과를 보여 주고 확인받는다.

---

## 3. 세션과 CLAUDE.md 관리

### 세션은 작업 단위로

| 상황 | 터미널(CLI) | 데스크톱 앱 |
|---|---|---|
| 작업 하나가 끝남 | 커밋 후 `/clear` | `/clear` 또는 새 세션 |
| 나중에 돌아올 작업 | `/rename fr-03-course-admin` | 사이드바 세션 목록 |
| 어제 작업 이어가기 | `claude --continue`, `--resume`, `/resume` | 사이드바에서 선택 |
| 같은 작업, 컨텍스트가 큼 | `/compact 남길 내용` | 같음 |
| 두 작업을 동시에 | `claude --worktree fr-03` | 폴더 선택에서 Worktree |

**세션 사이에 맥락 넘기기**: 큰 기능은 `docs/progress/fr-03.md`에 결정 사항·완료·남은 일을 남기게 하고, 다음 세션은 "`@docs/progress/fr-03.md` 를 읽고 남은 일부터" → 대화를 통째로 잇는 것보다 싸고 정확하다.

### 병렬 작업은 git worktree로

같은 폴더에서 세션 두 개가 동시에 고치면 서로 덮어쓴다 → 같은 저장소(`.git` 하나)를 브랜치별로 다른 폴더에 꺼내 놓는다.

```bash
claude --worktree fr-01
claude --worktree fr-03

# 직접 만들기
git worktree add ../eduhub-fr-03 -b feature/fr-03-course-admin
git worktree list
git worktree remove ../eduhub-fr-03
```

> 병렬 수는 내가 리뷰할 수 있는 만큼만 (오늘은 두 개까지). 보관·삭제 전에 커밋.

### CLAUDE.md는 나눠서, 짧게

| 파일 | 언제 읽히나 | 무엇을 쓰나 |
|---|---|---|
| 루트 `CLAUDE.md` | 세션 시작 시 항상 | 지도, 실행 명령, 공통 규칙, 금지 |
| `backend/`, `frontend/` `CLAUDE.md` | 그 폴더 파일을 다룰 때 | 영역별 규칙 |
| `@경로` import | 함께 읽힘 | 정리용. **토큰은 줄지 않는다** |
| `.claude/rules/*.md` (paths) | 지정 경로를 다룰 때 | 특정 파일 종류 규칙 |
| `CLAUDE.local.md` | 루트와 함께 | 개인 메모, 커밋 안 함 |

- **남길 것**: 코드로 알 수 없는 것, 실제로 틀렸던 것, 구체적인 명령·경로
- **뺄 것**: 코드로 알 수 있는 것, 포매터가 강제하는 것, "잘" 같은 말, 긴 절차(→ 스킬)

---

## 4. 따라하기 ①: FR-01~03을 PR로 (8단계)

시작 전: Docker Desktop이 켜져 있는지, 2회차 PR이 병합된 main을 받았는지 확인 (`git switch main && git pull`)

| Step | 내용 |
|---|---|
| 1 | 저장소 보호, PR 템플릿 |
| 2 | CLAUDE.md 분리 |
| 3 | DB·엔티티·Flyway |
| 4 | 탐색·계획 |
| 5 | API 구현·테스트 |
| 6 | OpenAPI 확인 |
| 7 | 과정 목록 화면 |
| 8 | PR·리뷰·병합 |

역할 분담: **팀원 A** FR-01·02 조회 API + 과정 목록 화면 / **팀원 B** FR-03 등록·수정·종료 API / 혼자라면 순서대로 모두

### Step 1. 저장소 보호와 PR 템플릿 (팀장 1명)

GitHub 설정
- Settings → Branches(또는 Rulesets): main에 PR 필수, 승인 1명, 강제 푸시·삭제 금지
- Settings → General → Pull Requests: Squash만 허용, "Automatically delete head branches" 켜기

PR 템플릿은 브랜치에서 추가하고 PR로 병합

```bash
git switch main && git pull
git switch -c chore/pr-template
mkdir -p .github
# 템플릿 직접 붙여넣기
git add .github/pull_request_template.md
git commit -m "chore: PR 템플릿 추가"
git push -u origin chore/pr-template
```

✅ main에서 커밋 후 push → 거부되면 성공. 확인 후 `git reset --hard origin/main`으로 되돌린다.

### Step 2. CLAUDE.md를 루트·백엔드·프론트로 (`chore/claude-md-split` 브랜치)

```text
[목표] CLAUDE.md를 루트, backend/, frontend/로
[범위]
- 루트: 소개 2줄, 지도, 실행 명령, 공통 규칙
- backend: 테스트 명령, 계층, JPA, OpenAPI
- frontend: MUI·Axios 클라이언트 5줄 이내
[하지 않는 것] 코드 수정, 새 규칙 발명
[완료 기준] 각 파일 60줄 이내, 명령은 백틱
[진행 방식] 세 파일의 diff를 보여줘
```

- 예제와 비교해 필요 없는 규칙은 빼고, 지난주 실제로 틀린 것은 넣는다
- `/memory`로 열어 한두 줄은 직접 고친다
- 커밋·푸시 → PR → 승인 → Squash

✅ 새 세션에서 `@backend/build.gradle.kts` 설명 요청 → `/context`에 `backend/CLAUDE.md`가 함께 보인다.

### Step 3. 개발 DB, 엔티티, Flyway (공통 기반, 한 명이 먼저)

지시의 범위
- docker-compose: `postgres:16`, 접속 정보는 `.env`
- `application-dev.yml`: `ddl-auto=validate`, `open-in-view=false`
- `V1__create_course.sql`: `course`, `course_session`
- `Course`·`CourseSession`: LAZY, `@Setter` 금지, `close()`
- Repository + `@DataJpaTest` 1개

```bash
cp .env.example .env
docker compose up -d postgres
cd backend && ./gradlew test
```

diff 검토 체크리스트
- 마이그레이션 파일 이름이 `V1__설명.sql` 형식? 기존 마이그레이션을 고치지 않았나?
- `@ManyToOne(fetch = LAZY)`? `application-dev.yml`·docker-compose에 비밀번호가 직접 없나?

### Step 4. 탐색하고 계획 받기 (각자 브랜치, 새 세션)

① 탐색 (수정 금지)

```text
@docs/requirements.md 의 FR-01, FR-02를 구현하려고 해.
course 패키지 구조와 기존 패턴을 설명해줘.
코드는 수정하지 마.
```

② 계획 (Plan 모드)

```text
CourseQueryService, Controller
DTO는 record, Pageable(0부터)
없는 id는 404, OpenAPI 주석
@WebMvcTest, @DataJpaTest
[하지 않는 것] 등록·수정, 화면
```

계획 검토 포인트: 패키지 위치, Controller → Service → Repository, DTO 사용, 테스트가 검색·페이지·404를 덮는지. 고칠 점은 바로: "검색은 Specification 말고 `@Query` 하나로"

### Step 5. 구현과 테스트 (Accept edits)

```java
@Tag(name = "Course")
@RestController
@RequestMapping("/api/v1/courses")
class CourseController {
    @Operation(summary = "과정 목록 조회")
    @GetMapping getCourses(...)

    @ApiResponse(responseCode = "404")
    @GetMapping("/{id}") getCourse(...)
}
```

B (FR-03) 핵심
- 등록 `@Valid`, 수정(종료 과정은 409), 회차 추가(정원 1 이상), 종료
- "종료된 과정 수정 시 예외" 테스트는 B가 먼저 **직접** 작성

diff 검토 체크리스트
- Controller가 Repository를 직접 부르지 않나? 엔티티가 응답으로 나가지 않나? (DTO record)
- 목록 조회에서 회차를 지연 로딩하며 쿼리가 여러 번 나가지 않나? (N+1) 404·409가 `ErrorResponse` 형식인가?

### Step 6. OpenAPI 확인 (직접)

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

- `/swagger-ui.html`에서 Try it out
- `/v3/api-docs` = 프론트 클라이언트 생성 원본
- 읽기 좋은 문서인지는 사람만 판단

### Step 7. 과정 목록 화면 (A)

- api-client: openapi-generator로 생성 (`generate:api`)
- course-mfe: React + MUI, 단독 실행 `:3001`
- 생성된 `courseApi`만 사용
- RTL 테스트 1개, MFE 연결은 6주차
- CORS 오류가 나면 해결 방법(개발 서버 프록시 vs 백엔드 CORS)을 Claude에게 장단점과 함께 묻고, 선택은 직접

### Step 8. PR, 리뷰, 병합

1. **커밋 나누기**: 백엔드 `feat(course)` / 프론트 `feat(course-mfe)`
2. **PR**: 본문 초안은 Claude, "맡긴 것/직접 한 것"은 직접
3. **Claude 리뷰**: `/clear` 후 새 세션, 심각도 표시
4. **동료 리뷰**: 상대 브랜치 받아 직접 테스트, 코멘트 1개+
5. **병합**: 반영 → 승인 → Squash → 정리

```text
이 브랜치의 main 대비 변경을 리뷰해줘. 수정하지 말고 문제만 목록으로.
기준: @CLAUDE.md, @backend/CLAUDE.md, docs/requirements.md 의 FR-01~03
심각도(높음/중간/낮음)를 붙여줘.
```

✅ main에 PR 3개 이상(엔티티 기반, FR-01·02, FR-03) 병합 + main에서 `./gradlew test` 통과

### 자주 겪는 문제

| 증상 | 원인과 해결 |
|---|---|
| main 푸시가 거부됨 | 정상 (보호 규칙). 브랜치를 만들어 PR로 |
| Flyway checksum mismatch | 적용된 마이그레이션을 고쳤다 → 되돌리고 `V2__`로 추가. 로컬은 `down -v` (개발 DB만) |
| Schema-validation: missing column | 엔티티와 마이그레이션이 다르다 → 어느 쪽을 고칠지 직접 판단 |
| Testcontainers가 Docker를 못 찾음 | Docker Desktop 실행 확인 |
| 생성된 api-client가 옛날 API | 백엔드를 띄우고 `npm run generate:api` 다시 |
| 두 사람이 같은 파일을 고쳐 충돌 | 따라하기 ② Step 3 |
| Claude가 범위 밖 파일까지 고침 | Esc → "이번 범위는 X까지만" → 진행됐으면 `/rewind` |

---

## 5. 따라하기 ②: 버그 수정과 충돌 해결

### Step 1~2. "첫 페이지가 안 보여요" (`fix/course-list-first-page`)

심어 둔 버그 (강사 제공)

```ts
courseApi.getCourses(keyword, category, page + 1, size)
```

증상: 11번부터 나오고, 10개 이하면 목록이 비어 보인다.

순서
1. 원인 찾기: 원인과 위치만, 수정 금지
2. 재현 테스트: "페이지 0이면 API에 page=0" — 지금은 실패
3. 수정은 직접: `page + 1` → `page` 한 줄
4. Claude에게 알리고 전체 테스트
5. 재발 방지: `frontend/CLAUDE.md`에 "페이지 번호는 API와 화면 모두 0부터. 표시만 +1" — 실제로 틀린 것만 CLAUDE.md에

### Step 3. 충돌 해결 연습 (2인 1조)

| 순서 | 팀원 A | 팀원 B |
|---|---|---|
| 1 | `chore/error-code-a` 브랜치 | `chore/error-code-b` 브랜치 |
| 2 | ErrorCode에 `COURSE_ALREADY_CLOSED` 추가 | 같은 위치에 `SESSION_CAPACITY_INVALID` 추가, `COURSE_NOT_FOUND` 메시지 수정 |
| 3 | PR → 먼저 병합 | PR (아직 병합 안 함) |
| 4~5 | — | "This branch has conflicts" → `git merge main` → 충돌 |
| 6 | B의 결과를 보고 내 변경이 살아 있나 확인 | Claude에게 의도 설명 + 둘 다 살리기, 테스트 |
| 7 | — | `git add` → `git commit` → push → 승인 → 병합 |

✅ 병합된 main의 ErrorCode에 A와 B의 코드가 모두 있고 테스트 통과

---

## 정리

### 오늘의 핵심 5가지

1. 기능은 탐색 → 계획 → 구현 → 검증. 각 단계 끝에서 사람이 방향을 확인한다
2. 핵심 업무 규칙의 테스트는 사람이 쓰고, Claude가 통과시킨다
3. 버그는 원인 파악 → 재현 테스트 → 수정. 한 줄 수정은 직접
4. main 보호, PR 템플릿, 사람 리뷰가 기본. 충돌 해결 결과는 상대에게 확인받는다
5. 세션은 작업 단위로, CLAUDE.md는 나눠서 짧게, 실제로 틀렸던 것만 적는다

### 확인 퀴즈

1. 기능 개발을 네 단계로 나누면 좋은 점은? 어느 단계에서 틀린 것을 잡는 게 가장 쌀까?
2. 구현을 맡길 때 "테스트 코드는 수정하지 마"라고 하는 이유는?
3. 버그를 고칠 때 재현 테스트를 수정보다 먼저 만드는 이유는?
4. main 보호가 AI 에이전트와 일할 때 특히 중요한 이유는?
5. PR 리뷰를 구현한 세션이 아니라 새 세션에서 시키는 이유는?
6. `backend/CLAUDE.md`는 언제 읽히나? `@import`하면 토큰이 줄어드나?
7. 같은 저장소에서 두 작업을 동시에 맡기려면? 주의할 점은?
8. 충돌을 Claude가 해결했을 때 반드시 확인할 것은?

### 과제 (4주차까지)

- [ ] FR-01~03 PR 모두 병합 (못 한 것은 `docs/progress/`에)
- [ ] 팀원 PR 리뷰 2개+, "위치 + 규칙 + 제안" 형식
- [ ] CLAUDE.md: 실제로 틀린 것 1~2줄 추가, 1줄 이상 삭제
- [ ] 더 해 보기 중 하나 (worktree, 작업 기록, 경로별 규칙)
- [ ] AI 없이 설명하기: 목록 요청이 화면에서 DB까지
- [ ] 회고: 맡긴 것 / 직접 한 것 / `/usage`와 아낀 방법

### 다음 회차 (4주차): 권한·스킬·서브에이전트

- 수강 신청·대기자·취소 (FR-04~06)
- 동시성 (NFR-01)
- 권한 설정 `.claude/settings.json`
- 반복 작업을 스킬로, 구현 → 리뷰 서브에이전트
- 미리 읽기: 권한, 설정·스킬·에이전트·훅 예제, 서브에이전트

> 작게 나누고, 테스트로 확인하고, 사람이 리뷰해서 병합한다.
