<h1 align="center">🍀행운복권🍀</h1>

"행운복권v2"는 개발자의 꿈을 갖고 처음으로 개발부터 배포까지 진행한 프로젝트인 ["행운복권"](https://github.com/junjange/lucky-lottery-android)을 Jetpack Compose로 마이그레이션하고 클린 아키텍처를 적용하여 리팩토링한 결과물입니다.

## 주요 기능
- 로또 6/45 및 연금복권 720+의 회차별 당첨번호 조회
- QR코드 스캔을 통한 당첨 여부 즉시 확인
- OCR을 활용한 복권 번호 자동 인식
- 내 번호 저장 및 자동 당첨 확인
- 랜덤 번호 생성 기능
- 푸시 알림을 통한 추첨일 알림

## 기술적 개선사항
- Jetpack Compose를 사용하여 선언형 UI 구현
- Clean Architecture 적용으로 계층 분리 및 의존성 역전
- 멀티 모듈 아키텍처로 관심사 분리 및 빌드 최적화
- Repository 패턴과 UseCase를 통한 비즈니스 로직 캡슐화

<br>

# Architecture
## MVI + Clean Architecture
```
presentation (feature) → domain → data
         ↓                          ↓
    ViewModel              Repository Implementation
         ↓                          ↓
      UseCase              Local/Remote DataSource
```

## 단방향 데이터 흐름 (Unidirectional Data Flow)
프로젝트는 MVI 패턴을 참고한 단방향 데이터 흐름을 구현하여 상태 관리의 예측 가능성과 디버깅 용이성을 향상시켰습니다.

### Contract 패턴
각 화면은 `Contract`를 통해 State, Event, Effect를 명확하게 정의합니다.

```kotlin
interface Contract {
    data class State(...)      // UI 상태
    sealed interface Event     // 사용자 액션
    sealed interface Effect    // 일회성 이벤트 (토스트, 네비게이션 등)
}
```

### 데이터 플로우
```
User Action (Event)
       ↓
   ViewModel
       ↓              ↘
   UseCase          Effect (일회성 이벤트)
       ↓                    ↓
State 업데이트       UI Side Effect
       ↓            (네비게이션, 스낵바 등)
   UI 재구성
(Recomposition)
```

## Multi-Module Structure
### Core Modules
- `core:designsystem` - 공통 디자인 시스템 및 테마
- `core:domain` - 비즈니스 로직 및 UseCase
- `core:data` - Repository 구현체
- `core:ui` - 공통 UI 컴포넌트
- `core:local` - Room Database 및 DataStore
- `core:remote` - Retrofit API 클라이언트
- `core:navigation` - 네비게이션 관련
- `core:notification` - 알림 관리
- `core:kakao` - 카카오 로그인
- `core:google` - 구글 로그인 및 AdMob
- `core:firebase` - Firebase 서비스
- `core:ocr` - OCR (Tesseract) 기능

### Feature Modules
- `feature:main` - 메인 화면
- `feature:home` - 홈 화면 (당첨번호 조회)
- `feature:mynumber` - 내 번호 관리
- `feature:randomnumber` - 랜덤 번호 생성
- `feature:notification` - 알림 설정
- `feature:my` - 마이페이지
- `feature:login` - 로그인
- `feature:register` - 회원가입
- `feature:editprofile` - 프로필 수정
- `feature:withdrawal` - 회원 탈퇴
- `feature:setting` - 설정
- `feature:splash` - 스플래시

### Build Modules
- `app` - 앱 모듈
- `build-logic` - Gradle Convention Plugins
  - Convention Plugins를 통한 빌드 로직 재사용
  - 모듈별 공통 설정 중앙화
- `buildSrc` - 빌드 설정 및 의존성 관리
  - 모듈 경로 정의 (Modules)
  - 버전 정보 (Versions)

<br>

# Tech Stacks

## Language & Build
- **Kotlin** - 주 개발 언어
- **Gradle Kotlin DSL** - 빌드 스크립트
- **Version Catalog** - 의존성 중앙 관리

## Asynchronous
- **Coroutines** - 비동기 처리
- **Flow** - 반응형 데이터 스트림

## UI
- **Jetpack Compose** - 선언형 UI 프레임워크
- **Material Design 3** - 디자인 시스템
- **Navigation Compose** - 화면 전환
- **Coil** - 이미지 로딩

## Dependency Injection
- **Dagger Hilt** - 의존성 주입
- **Hilt Navigation Compose** - Compose 통합
- **Hilt WorkManager** - WorkManager 통합

## Local Storage
- **Room** - 로컬 데이터베이스
- **DataStore** - Key-Value 저장소
- **Paging 3** - 페이징 처리

## Network
- **Retrofit2** - REST API 클라이언트
- **OkHttp** - HTTP 클라이언트
- **Gson** - JSON 직렬화/역직렬화
- **Kotlinx Serialization** - Kotlin 직렬화

## Background Tasks
- **WorkManager** - 백그라운드 작업 스케줄링

## Third Party Libraries
### Authentication
- **Kakao SDK** - 카카오 로그인
- **Google Play Services Auth** - 구글 로그인

### Firebase
- **Firebase Auth** - 인증
- **Firebase Messaging** - 푸시 알림

### Image & QR
- **Android Image Cropper** - 이미지 크롭
- **Zxing** - QR 코드 스캔
- **Tesseract4Android** - OCR (광학 문자 인식)

### Ads
- **Google AdMob** - 광고

### Utilities
- **JSoup** - HTML 파싱

<br>
