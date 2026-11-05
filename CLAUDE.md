# EduHub

사내 교육과정 조회·수강 신청 시스템. 백엔드(Spring Boot)와 프론트엔드(React)로 구성된 모노레포.
요구사항과 결정 사항(D-xx)은 `docs/requirements.md`를 기준으로 한다.

## 구조
- `backend/`: Spring Boot 3, Java 21, JPA, Flyway, springdoc. 규칙은 `backend/CLAUDE.md`
- `frontend/`: npm workspaces. `packages/api-client`(OpenAPI 생성 클라이언트), `apps/course-mfe`(과정 화면). 규칙은 `frontend/CLAUDE.md`
- `docs/`: 요구사항, 회차별 교육 정리(`docs/lectures/`)
- API 계약: 백엔드 OpenAPI 어노테이션 → `/v3/api-docs` → `frontend/packages/api-client`에 클라이언트 생성

## 로컬 실행
- 백엔드: `cd backend && ./gradlew bootRun` (dev 프로필, H2 인메모리) → http://localhost:8080/swagger-ui.html
- 프론트: `cd frontend && npm install && npm run dev -w apps/course-mfe` → http://localhost:3001

## 공통 규칙
- 커밋 메시지는 Conventional Commits: `feat(course): 과정 목록 조회 API`. 백엔드와 프론트 변경은 커밋을 나눈다
- 브랜치는 `feature/…`, `fix/…`, `chore/…`. main은 보호되어 있어 PR(Squash merge)로만 병합한다
- API를 추가·변경하면 프론트 클라이언트를 다시 생성한다: `cd frontend && npm run generate:api`
- 작업을 마치기 전에 변경한 영역의 테스트를 실행해 통과를 확인한다

## 하지 말 것
- 비밀 정보는 코드·설정 파일에 쓰지 않는다. 환경 변수와 `.env`(커밋 금지)만 쓴다
- `frontend/packages/api-client/src/generated/`는 생성 코드이므로 직접 고치지 않는다
- `backend/src/main/resources/db/migration/`의 기존 Flyway 스크립트는 고치지 않는다. 새 버전 파일로 추가한다
