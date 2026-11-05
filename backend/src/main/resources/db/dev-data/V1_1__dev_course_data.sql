-- 개발용 데이터 (dev 프로필에서만 실행. application-dev.yml의 flyway.locations 참고)
INSERT INTO course (title, category, description) VALUES
    ('Spring Boot 입문', 'BACKEND', '스프링 부트로 REST API를 만든다'),
    ('JPA 실전', 'BACKEND', '연관관계, 지연 로딩, N+1 문제'),
    ('Java 21 새 기능', 'BACKEND', 'record, 패턴 매칭, 가상 스레드'),
    ('React 기초', 'FRONTEND', '컴포넌트, 상태, 훅'),
    ('TypeScript 핵심', 'FRONTEND', '타입 시스템과 제네릭'),
    ('MUI로 화면 만들기', 'FRONTEND', 'Material UI 컴포넌트와 테마'),
    ('Git과 GitHub 협업', 'DEVOPS', '브랜치, PR, 리뷰, 충돌 해결'),
    ('Docker 입문', 'DEVOPS', '이미지와 컨테이너'),
    ('GitHub Actions CI', 'DEVOPS', '빌드·테스트 자동화'),
    ('Claude Code 활용', 'AI', '탐색 → 계획 → 구현 → 검증'),
    ('프롬프트 작성법', 'AI', '목표·범위·완료 기준을 주는 지시'),
    ('SQL 튜닝 기초', 'DATA', '실행 계획과 인덱스');

INSERT INTO course_session (course_id, start_date, place, capacity)
SELECT id, DATE '2026-11-16', '본사 3층 교육장', 20 FROM course WHERE title = 'Spring Boot 입문';
INSERT INTO course_session (course_id, start_date, place, capacity)
SELECT id, DATE '2026-12-07', '본사 3층 교육장', 20 FROM course WHERE title = 'Spring Boot 입문';
INSERT INTO course_session (course_id, start_date, place, capacity)
SELECT id, DATE '2026-11-23', '온라인', 50 FROM course WHERE title = 'JPA 실전';
INSERT INTO course_session (course_id, start_date, place, capacity)
SELECT id, DATE '2026-11-30', '본사 5층 세미나실', 15 FROM course WHERE title = 'React 기초';
INSERT INTO course_session (course_id, start_date, place, capacity)
SELECT id, DATE '2026-12-14', '온라인', 100 FROM course WHERE title = 'Claude Code 활용';
