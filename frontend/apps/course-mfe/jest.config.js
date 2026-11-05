/** @type {import('jest').Config} */
module.exports = {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.ts'],
  transform: {
    '^.+\.(ts|tsx)$': ['ts-jest', { tsconfig: { jsx: 'react-jsx', esModuleInterop: true, module: 'commonjs', moduleResolution: 'node', isolatedModules: true } }],
  },
  // 생성 클라이언트 패키지도 TypeScript 소스이므로 변환 대상에 포함한다
  transformIgnorePatterns: ['/node_modules/(?!@eduhub/)'],
};
