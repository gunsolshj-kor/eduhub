import { useState } from 'react';
import {
  Alert,
  CircularProgress,
  MenuItem,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
} from '@mui/material';
import { useCourses } from '../hooks/useCourses';

const PAGE_SIZE = 10;

// 분류 코드. 백엔드에 분류 목록 API가 생기면 그 응답으로 바꾼다
const CATEGORIES = ['BACKEND', 'FRONTEND', 'DEVOPS', 'AI', 'DATA'];

/** 과정 목록 화면 (FR-01): 제목 검색, 분류 필터, 페이지 이동. 데이터는 useCourses만 쓴다. */
export function CourseListPage() {
  const [keyword, setKeyword] = useState('');
  const [category, setCategory] = useState('');
  const [page, setPage] = useState(0); // MUI TablePagination도 0부터 시작
  const { data, loading, error } = useCourses({ keyword, category, page, size: PAGE_SIZE });

  if (error) return <Alert severity="error">과정 목록을 불러오지 못했습니다.</Alert>;

  return (
    <Stack spacing={2}>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
        <TextField
          label="과정명 검색"
          value={keyword}
          onChange={(e) => {
            setKeyword(e.target.value);
            setPage(0); // 검색어가 바뀌면 첫 페이지부터
          }}
          sx={{ flex: 1 }}
        />
        <TextField
          select
          label="분류"
          value={category}
          onChange={(e) => {
            setCategory(e.target.value);
            setPage(0);
          }}
          sx={{ minWidth: 160 }}
        >
          <MenuItem value="">전체</MenuItem>
          {CATEGORIES.map((c) => (
            <MenuItem key={c} value={c}>
              {c}
            </MenuItem>
          ))}
        </TextField>
      </Stack>
      {loading ? (
        <CircularProgress />
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>과정명</TableCell>
              <TableCell>분류</TableCell>
              <TableCell>상태</TableCell>
              <TableCell align="right">회차 수</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data?.content.map((c) => (
              <TableRow key={c.id}>
                <TableCell>{c.title}</TableCell>
                <TableCell>{c.category}</TableCell>
                <TableCell>{c.status === 'CLOSED' ? '종료' : '모집 중'}</TableCell>
                <TableCell align="right">{c.sessionCount}</TableCell>
              </TableRow>
            ))}
            {data?.content.length === 0 && (
              <TableRow>
                <TableCell colSpan={4}>조건에 맞는 과정이 없습니다.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      )}
      <TablePagination
        component="div"
        count={data?.totalElements ?? 0}
        page={page}
        rowsPerPage={PAGE_SIZE}
        rowsPerPageOptions={[PAGE_SIZE]}
        onPageChange={(_, p) => setPage(p)}
      />
    </Stack>
  );
}
