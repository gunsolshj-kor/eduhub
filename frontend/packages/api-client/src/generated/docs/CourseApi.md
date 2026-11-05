# CourseApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addSession**](#addsession) | **POST** /api/v1/courses/{id}/sessions | 회차 추가|
|[**closeCourse**](#closecourse) | **POST** /api/v1/courses/{id}/close | 과정 종료|
|[**createCourse**](#createcourse) | **POST** /api/v1/courses | 과정 등록|
|[**getCourse**](#getcourse) | **GET** /api/v1/courses/{id} | 과정 상세 조회|
|[**getCourses**](#getcourses) | **GET** /api/v1/courses | 과정 목록 조회|
|[**updateCourse**](#updatecourse) | **PUT** /api/v1/courses/{id} | 과정 수정|

# **addSession**
> IdResponse addSession(sessionCreateRequest)

과정에 회차를 추가한다. 정원은 1명 이상, 종료된 과정에는 추가할 수 없다

### Example

```typescript
import {
    CourseApi,
    Configuration,
    SessionCreateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CourseApi(configuration);

let id: number; // (default to undefined)
let sessionCreateRequest: SessionCreateRequest; //

const { status, data } = await apiInstance.addSession(
    id,
    sessionCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **sessionCreateRequest** | **SessionCreateRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**IdResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 추가 성공 |  -  |
|**400** | 입력값 오류 (정원 1명 미만 등) |  -  |
|**404** | 과정이 없음 |  -  |
|**409** | 종료된 과정 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **closeCourse**
> closeCourse()

과정을 CLOSED로 바꾼다. 이미 종료된 과정이면 그대로 둔다

### Example

```typescript
import {
    CourseApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CourseApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.closeCourse(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | 종료 성공 |  -  |
|**404** | 과정이 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **createCourse**
> IdResponse createCourse(courseCreateRequest)

OPEN 상태의 과정을 등록하고 id를 돌려준다

### Example

```typescript
import {
    CourseApi,
    Configuration,
    CourseCreateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CourseApi(configuration);

let courseCreateRequest: CourseCreateRequest; //

const { status, data } = await apiInstance.createCourse(
    courseCreateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **courseCreateRequest** | **CourseCreateRequest**|  | |


### Return type

**IdResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | 등록 성공 |  -  |
|**400** | 입력값 오류 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getCourse**
> CourseDetailResponse getCourse()

과정 정보와 회차 목록(일정, 장소, 정원, 남은 자리)을 조회한다

### Example

```typescript
import {
    CourseApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CourseApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getCourse(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**CourseDetailResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | 조회 성공 |  -  |
|**404** | 과정이 없음 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getCourses**
> PageResponseCourseSummaryResponse getCourses()

제목·분류로 검색하고 페이지 단위로 조회한다 (page는 0부터)

### Example

```typescript
import {
    CourseApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CourseApi(configuration);

let keyword: string; //제목 검색어 (부분 일치) (optional) (default to undefined)
let category: string; //분류 코드 (예: BACKEND) (optional) (default to undefined)
let page: number; //Zero-based page index (0..N) (optional) (default to 0)
let size: number; //The size of the page to be returned (optional) (default to 10)
let sort: Array<string>; //Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional) (default to undefined)

const { status, data } = await apiInstance.getCourses(
    keyword,
    category,
    page,
    size,
    sort
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **keyword** | [**string**] | 제목 검색어 (부분 일치) | (optional) defaults to undefined|
| **category** | [**string**] | 분류 코드 (예: BACKEND) | (optional) defaults to undefined|
| **page** | [**number**] | Zero-based page index (0..N) | (optional) defaults to 0|
| **size** | [**number**] | The size of the page to be returned | (optional) defaults to 10|
| **sort** | **Array&lt;string&gt;** | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | (optional) defaults to undefined|


### Return type

**PageResponseCourseSummaryResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateCourse**
> updateCourse(courseUpdateRequest)

제목·분류·설명을 수정한다. 종료된 과정은 수정할 수 없다

### Example

```typescript
import {
    CourseApi,
    Configuration,
    CourseUpdateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CourseApi(configuration);

let id: number; // (default to undefined)
let courseUpdateRequest: CourseUpdateRequest; //

const { status, data } = await apiInstance.updateCourse(
    id,
    courseUpdateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **courseUpdateRequest** | **CourseUpdateRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | 수정 성공 |  -  |
|**400** | 입력값 오류 |  -  |
|**404** | 과정이 없음 |  -  |
|**409** | 종료된 과정 |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

