# Thymeleaf의 Java 8 날짜/시간(JSR-310) 지원

Thymeleaf에서 Java 8의 날짜 및 시간 API(`java.time.*`: `LocalDateTime`, `LocalDate`, `Instant` 등)를 처리하는 방식과 `#temporals` 유틸리티 객체 사용법을 정리합니다.

---

## 1. 버전별 지원 방식

| 구분 | Thymeleaf 3.0 | Thymeleaf 3.1+ (현재 프로젝트: 3.1.5) |
| :--- | :--- | :--- |
| **지원 방식** | `thymeleaf-extras-java8time` 별도 의존성 필요 | **Thymeleaf Core에 기본 내장** |
| **추가 설정** | `pom.xml`에 모듈 추가 및 Dialect 등록 필요 | **추가 의존성 없이 즉시 사용 가능** |

> **참고**: Thymeleaf 3.1부터는 Java 8 이상의 날짜/시간 API 지원이 코어 모듈로 완전히 통합되어 별도의 extras 라이브러리를 추가할 필요가 없습니다.

---

## 2. 유틸리티 객체 구분

전달받은 날짜 객체의 타입에 따라 사용하는 Thymeleaf 내장 표현식 유틸리티 객체가 다릅니다.

- **`#temporals`**: Java 8 날짜/시간 API (`java.time.LocalDate`, `LocalDateTime`, `ZonedDateTime`, `Instant` 등)
- **`#dates`**: 레거시 날짜 객체 (`java.util.Date`)
- **`#calendars`**: 레거시 캘린더 객체 (`java.util.Calendar`)

---

## 3. 실전 사용 예시

### 3.1. Controller
복잡하게 `SimpleDateFormat`이나 `DateFormat`으로 컨트롤러에서 문자열로 변환할 필요 없이, `LocalDateTime` 객체를 그대로 모델에 담아 넘깁니다.

```java
@Controller
public class HomeController {

  @GetMapping("/home")
  public String home(Model model) {
    // Java 8 LocalDateTime 객체 전달
    model.addAttribute("serverTime", LocalDateTime.now());
    return "home_example/home";
  }
}
```

### 3.2. Thymeleaf 템플릿
템플릿에서 `#temporals.format(...)`을 사용하여 원하는 패턴으로 포맷팅합니다.

```html
<!-- 기본 포맷팅 -->
<p th:text="${#temporals.format(serverTime, 'yyyy-MM-dd HH:mm:ss')}">2026-09-07 22:10:00</p>

<!-- 날짜만 포맷팅 -->
<p th:text="${#temporals.format(serverTime, 'yyyy-MM-dd')}">2026-09-07</p>

<!-- 특정 로케일 기준 포맷팅 -->
<p th:text="${#temporals.format(serverTime, 'yyyy년 M월 d일 (E)', #locale)}">2026년 9월 7일 (월)</p>
```

---

## 4. `#temporals` 주요 메서드

- **`#temporals.format(target, pattern)`**: 지정한 패턴 문자열(`yyyy-MM-dd` 등)로 포맷팅
- **`#temporals.format(target, pattern, locale)`**: 지정한 로케일을 적용하여 포맷팅
- **`#temporals.day(target)`**, **`#temporals.month(target)`**, **`#temporals.year(target)`**: 특정 날짜 요소(일, 월, 연) 추출
- **`#temporals.hour(target)`**, **`#temporals.minute(target)`**, **`#temporals.second(target)`**: 시간 요소 추출
