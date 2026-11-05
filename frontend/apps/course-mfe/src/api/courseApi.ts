import { Configuration, CourseApi } from '@eduhub/api-client';

// 생성된 클라이언트의 인스턴스는 여기 한 곳에서만 만든다 (Axios를 직접 생성하지 않는다)
// basePath '': 개발 서버 프록시(webpack.config.js의 devServer.proxy)를 거쳐 백엔드로 간다
export const courseApi = new CourseApi(new Configuration({ basePath: '' }));
