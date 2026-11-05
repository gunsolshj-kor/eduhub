import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { Container, CssBaseline, Typography } from '@mui/material';
import { CourseListPage } from './pages/CourseListPage';

// 단독 실행용 진입점. 6주차에 shell(host)에 붙일 때는 CourseApp을 expose한다
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <CssBaseline />
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h5" component="h1" gutterBottom>
        교육과정
      </Typography>
      <CourseListPage />
    </Container>
  </StrictMode>,
);
