const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

// 이번 주는 단독 실행되는 React 앱. Module Federation(remote) 설정은 6주차에 추가한다
module.exports = {
  entry: './src/index.tsx',
  output: { path: path.resolve(__dirname, 'dist'), filename: '[name].[contenthash].js', clean: true },
  resolve: { extensions: ['.tsx', '.ts', '.js'] },
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        loader: 'ts-loader',
        exclude: /node_modules\/(?!@eduhub\/)/,
        options: { transpileOnly: true, compilerOptions: { noEmit: false } },
      },
    ],
  },
  plugins: [new HtmlWebpackPlugin({ template: './public/index.html' })],
  devServer: {
    port: 3001,
    historyApiFallback: true,
    // CORS 대신 개발 서버 프록시를 쓴다: 브라우저는 3001로만 요청하고, /api는 백엔드(8080)로 넘긴다
    proxy: [{ context: ['/api'], target: 'http://localhost:8080' }],
  },
};
