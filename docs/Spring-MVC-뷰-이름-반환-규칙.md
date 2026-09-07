# Spring MVC 컨트롤러의 뷰 이름(View Name) 반환 규칙

Spring MVC 컨트롤러에서 뷰(View) 이름을 반환할 때 맨 앞 슬래시(`/`)의 유무에 따른 동작 방식과 권장 규칙을 정리합니다.

---

## 1. 문제 현상
MockMvc 단위 테스트 작성 시, 컨트롤러 메서드에 따라 뷰 이름의 맨 앞 슬래시 유무가 달라 테스트가 실패하는 경우가 발생합니다.

```java
// void 반환 메서드: 슬래시가 없어야 통과
.andExpect(view().name("board/list"));

// String 반환 메서드: 슬래시가 있어야 통과 (?)
.andExpect(view().name("/board/modify"));
```

---

## 2. 원인 분석

### 2.1. `void` 반환 타입의 동작 (`RequestToViewNameTranslator`)
컨트롤러 메서드의 반환 타입이 `void`이면 스프링의 기본 `DefaultRequestToViewNameTranslator`가 요청 URL 경로를 바탕으로 뷰 이름을 자동 생성합니다.

- **요청 URL**: `/board/list`
- **자동 생성된 뷰 이름**: 맨 앞의 슬래시를 제거한 **`board/list`**

```java
@GetMapping("/list")
public void list() {
  // 스프링이 자동으로 "board/list"를 뷰 이름으로 지정
}
```

### 2.2. `String` 반환 타입의 동작
컨트롤러 메서드가 `String`을 반환하면 개발자가 명시한 문자열이 **그대로** 뷰 이름이 됩니다.

```java
@GetMapping("/modify/{bno}")
public String modifyGET(@PathVariable("bno") Long bno) {
  return "/board/modify"; // 뷰 이름이 그대로 "/board/modify"가 됨
}
```

### 2.3. MockMvc `view().name()` 검증 방식
MockMvc의 `andExpect(view().name(...))` 검증은 뷰 리졸버(ViewResolver)가 해석하기 전의 **원본 뷰 이름 문자열과 완전 일치(Exact Match)**하는지를 비교합니다. 따라서 코드에 작성된 슬래시 유무가 그대로 반영되어 불일치가 발생합니다.

---

## 3. 슬래시(`/`)로 시작할 때 발생할 수 있는 잠재적 문제

뷰 리졸버(`InternalResourceViewResolver`, `ThymeleafViewResolver` 등)는 보통 `prefix` 끝에 슬래시를 포함하도록 설정합니다.

- **prefix 설정 예시**: `/WEB-INF/views/` 또는 `classpath:/templates/`
- **뷰 이름이 `"/board/modify"`인 경우**:
  `prefix + viewName` 결합 시 `/WEB-INF/views//board/modify.html` 처럼 **이중 슬래시(`//`)**가 발생할 수 있습니다.
- 대부분의 웹 컨테이너나 리졸버가 이중 슬래시를 내부적으로 정규화해주지만, 환경에 따라 파일 리소스 검색 실패나 예기치 않은 오류의 원인이 될 수 있습니다.

---

## 4. 권장 컨벤션

스프링 MVC에서는 뷰 이름을 반환할 때 **맨 앞 슬래시를 붙이지 않는 상대 경로 형태**로 통일하는 것이 표준 관례입니다.

### 컨트롤러 코드
```java
// 권장: 맨 앞 슬래시 제거
@GetMapping("/modify/{bno}")
public String modifyGET(@PathVariable("bno") Long bno) {
  return "board/modify";
}

@GetMapping("/read/{bno}")
public String read(@PathVariable("bno") Long bno) {
  return "board/read";
}
```

### 테스트 코드
```java
// 일관되게 슬래시 없는 형태로 검증 가능
@Test
void testList() throws Exception {
  mockMvc.perform(get("/board/list"))
      .andExpect(status().isOk())
      .andExpect(view().name("board/list"));
}

@Test
void testModifyGET() throws Exception {
  mockMvc.perform(get("/board/modify/123"))
      .andExpect(status().isOk())
      .andExpect(view().name("board/modify"));
}
```
