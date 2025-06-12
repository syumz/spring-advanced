# SPRING ADVANCED

## 👨‍🏫 프로젝트 소개
- 코드 개선 및 **테스트 코드** 작성
    - JPA 심화 학습과 기본 Spring 구조에 대해 다시 한 번 더 복습
    - **테스트 코드** 작성을 통해 기존 코드의 안정성 확보, 유지보수성 향상
    - 코드를 효율적으로 검증, 핵심 비즈니스 로직을 깔끔하게 관리

---

## ⏲️ 개발 기간
- 2025.06.05(목) ~ 2023.06.12(수)

---

## 💻 개발환경
- **Language** : Java 17
- **Framework** : Spring Boot 3.3.3
- **IDE** : IntelliJ
- **Build Tool** : Gradle

---

## ⚙️ 프로젝트 설명
### Lv1.
**1. 코드 개선 퀴즈 - Early Return**
- 조건에 맞지 않는 경우 즉시 리턴하여 불필요한 로직의 실행을 방지하고 성능을 향상시킵니다.

- 패키지 `package org.example.expert.domain.auth.service;` 의 `AuthService` 클래스에 있는 `signup()` 중 아래의 코드 부분의 위치를 리팩토링해서 해당 에러가 발생하는 상황일 때, passwordEncoder의 encode() 동작이 불필요하게 일어나지 않게 코드 개선
```java
if (userRepository.existsByEmail(signupRequest.getEmail())) {
    throw new InvalidRequestException("이미 존재하는 이메일입니다.");
}
```

**2. 리팩토링 퀴즈 - 불필요한 if-else 피하기**
- 복잡한 if-else 구조는 코드의 가독성을 떨어뜨리고 유지보수를 어렵게 만듭니다. 불필요한 else 블록을 없애 코드를 간결하게 합니다.

- 패키지 package org.example.expert.client; 의 WeatherClient 클래스에 있는 getTodayWeather() 중 아래의 코드 부분 리팩토링
```java
WeatherDto[] weatherArray = responseEntity.getBody();
if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
        throw new ServerException("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: " + responseEntity.getStatusCode());
        } else {
        if (weatherArray == null || weatherArray.length == 0) {
        throw new ServerException("날씨 데이터가 없습니다.");
    }
}
```

**3. 코드 개선 퀴즈 - Validation**
- 패키지 package org.example.expert.domain.user.service; 의 UserService 클래스에 있는 changePassword() 중 아래 코드 부분을 해당 API의 요청 DTO에서 처리할 수 있게 개선
```java
if (userChangePasswordRequest.getNewPassword().length() < 8 ||
        !userChangePasswordRequest.getNewPassword().matches("ㅅ") ||
        !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
        throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
}
```

### Lv2.
**N+1 문제**
- `TodoController`와 `TodoService`를 통해 `Todo` 관련 데이터를 처리합니다.
- 여기서 N+1 문제가 발생할 수 있는 시나리오는 `getTodos` 메서드에서 모든 Todo를 조회할 때, 각 Todo와 연관된 데이터를 개별적으로 가져오는 경우입니다.
- 요구사항:
    - JPQL `fetch join`을 사용하여 N+1 문제를 해결하고 있는 `TodoRepository`가 있습니다. 이를 동일한 동작을 하는 `@EntityGraph` 기반의 구현으로 수정


### Lv3.
**1. 테스트 코드 연습 - 1 (예상대로 성공하는지에 대한 케이스입니다.)**
- 테스트 패키지 package org.example.expert.config; 의 PassEncoderTest 클래스에 있는 matches_메서드가_정상적으로_동작한다() 테스트가 의도대로 성공할 수 있게 수정

**2. 테스트 코드 연습 - 2 (예상대로 예외처리 하는지에 대한 케이스입니다.)**
- 테스트 패키지 package org.example.expert.domain.manager.service; 의 ManagerServiceTest 의 클래스에 있는 manager_목록_조회_시_Todo가_없다면_NPE_에러를_던진다() 테스트가 성공하고 컨텍스트와 일치하도록 테스트 코드와 테스트 코드 메서드 명을 수정
- 테스트 패키지 org.example.expert.domain.comment.service; 의 CommentServiceTest 의 클래스에 있는 comment_등록_중_할일을_찾지_못해_에러가_발생한다() 테스트가 성공할 수 있도록 테스트 코드를 수정
- 테스트 패키지 org.example.expert.domain.manager.service의 ManagerServiceTest 클래스에 있는 todo의_user가_null인_경우_예외가_발생한다() 테스트가 성공할 수 있도록 서비스 로직을 수정

### Lv4.
**AOP를 활용한 API 로깅**
- 어드민 사용자만 접근할 수 있는 특정 API에는 접근할 때마다 접근 로그를 기록해야 합니다.


**로깅 구현 방법**

**AOP**를 사용하여 구현하기
- 어드민 API 메서드 실행 전후에 요청/응답 데이터를 로깅
- 로깅 내용에는 다음이 포함되어야 합니다:
  - 요청한 사용자의 ID
  - API 요청 시각
  - API 요청 URL
  - 요청 본문(`RequestBody`)
  - 응답 본문(`ResponseBody`)

**세부 구현 가이드**
  - `@Around` 어노테이션을 사용하여 어드민 API 메서드 실행 전후에 요청/응답 데이터를 로깅
  - 요청 본문과 응답 본문은 JSON 형식으로 기록
  - 로깅은 `Logger` 클래스를 활용하여 기록

