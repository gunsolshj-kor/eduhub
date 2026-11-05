# Frontend 규칙

## 명령어 (frontend 폴더에서)
- 설치: `npm install`
- 개발 서버: `npm run dev -w apps/course-mfe` (http://localhost:3001, `/api`는 8080으로 프록시)
- 테스트: `npm test -w apps/course-mfe`, 타입 검사: `npm run typecheck -w apps/course-mfe`
- API 클라이언트 생성: `npm run generate:api` (백엔드 실행 중, `JAVA_HOME`이 JDK 11 이상이어야 함)

## API 호출
- Axios를 직접 생성하지 않는다. 생성된 `CourseApi` 인스턴스는 `src/api/courseApi.ts` 한 곳에서만 만든다
- 서버 데이터 조회는 커스텀 훅(`src/hooks/useXxx.ts`)으로 감싸고, 화면 컴포넌트는 API를 직접 부르지 않는다
- 페이지 번호는 API와 화면 모두 0부터. 화면에 보여 줄 때만 +1

## UI
- MUI 컴포넌트를 쓴다. 로딩은 `CircularProgress`, 에러는 `Alert`
- Module Federation(shell 연결)은 6주차에 추가한다. 지금 course-mfe는 단독 실행 앱이다
