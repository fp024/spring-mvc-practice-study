# Thymeleaf URL 링크 표현식(`@{...}`) 사용 가이드

Thymeleaf에서 동적 경로 및 쿼리 파라미터를 포함한 URL 링크(`th:href`)를 작성하는 방법과 권장 패턴을 정리합니다.

---

## 1. 왜 일반 `href` 대신 `th:href="@{...}"`를 써야 할까요?

Thymeleaf의 URL 링크 표현식인 `@{...}`는 단순한 문자열 조합이 아니라 다음과 같은 강력한 기능들을 자동으로 처리해 줍니다.

1. **컨텍스트 패스(Context Path) 자동 포함**:
   - 로컬에서는 `http://localhost:8080/board/list`이지만 배포 환경에서 컨텍스트 패스가 `/myapp`인 경우, `th:href="@{/board/list}"`는 자동으로 `/myapp/board/list`로 변환해 줍니다.
2. **자동 URL 인코딩**:
   - 한글이나 특수문자가 파라미터에 포함될 때 브라우저에 안전하도록 자동으로 UTF-8 인코딩을 수행합니다.
3. **세션 ID(jsessionid) URL 재작성 지원**:
   - 쿠키를 지원하지 않는 브라우저 환경에서 필요한 경우 URL 뒤에 자동으로 세션 정보를 유지합니다.

---

## 2. 동적 경로를 작성하는 3가지 방법

### 방법 1: 리터럴 대체 문법 `|...|` (가장 직관적이고 추천)
자바스크립트의 백틱 템플릿 리터럴(`` `${val}` ``)과 유사하게, 파이프 기호(`|`)로 URL 전체를 감싸서 `${...}` 변수를 바로 삽입하는 방식입니다.

```html
<!-- /board/modify/10 형태로 렌더링 -->
<a th:href="@{|/board/modify/${board.bno}|}">MODIFY</a>
```

* **장점**: 눈으로 읽기 가장 쉽고 직관적입니다.
* **주의**: 반드시 `@{` 뒤와 닫는 `}` 앞에 파이프(`|`)가 들어가야 합니다.  
  (❌ `@{/board/modify/${board.bno}}` 처럼 파이프가 없으면 파싱 에러 발생)

---

### 방법 2: 표준 경로 변수(Path Variable) 문법
Thymeleaf 표준 방식으로 URL 경로 내에 `{플레이스홀더}`를 두고, 뒤에 소괄호 `(키=값)` 형태로 값을 바인딩합니다.

```html
<!-- /board/read/10 형태로 렌더링 -->
<a th:href="@{/board/read/{bno}(bno=${board.bno})}">READ</a>
```

* **장점**: 경로 템플릿과 실제 데이터 주입이 명확히 분리되어 안전합니다.

---

### 방법 3: 쿼리 파라미터(Query Parameter) 문법
소괄호 `(키=값)` 안에 경로에 없는 파라미터 이름을 지정하면, Thymeleaf가 자동으로 쿼리스트링(`?key=value&...`)을 생성해 줍니다.

```html
<!-- /board/list?page=1&size=10 형태로 렌더링 -->
<a th:href="@{/board/list(page=${page}, size=10)}">LIST</a>
```

* `?`나 `&` 기호를 직접 쓰지 않아도 Thymeleaf가 파라미터 개수에 맞춰 자동으로 붙여줍니다.

---

### 응용: 경로 변수 + 쿼리 파라미터 혼합 사용
소괄호 안에 경로에 지정된 변수와 그렇지 않은 변수를 함께 넘기면, 자동으로 경로 치환과 쿼리스트링 생성이 동시에 이루어집니다.

```html
<!-- /board/read/10?page=2&type=T 형태로 렌더링 -->
<a th:href="@{/board/read/{bno}(bno=${board.bno}, page=2, type='T')}">READ</a>
```

---

## 3. 흔한 실수와 주의사항

### 1. 리터럴 대체 기호(`|`) 누락
```html
<!-- ❌ 문법 오류: Thymeleaf가 경로를 해석하지 못함 -->
<a th:href="@{/board/modify/${board.bno}}">MODIFY</a>

<!-- ⭕ 정상: 파이프(|)로 감싸야 함 -->
<a th:href="@{|/board/modify/${board.bno}|}">MODIFY</a>
```

### 2. `@{}` 없이 문자열 더하기 사용
```html
<!-- ❌ 비권장: 컨텍스트 패스 누락 및 인코딩 미지원 -->
<a th:href="'/board/modify/' + ${board.bno}">MODIFY</a>

<!-- ⭕ 정상: @{...} 사용 -->
<a th:href="@{|/board/modify/${board.bno}|}">MODIFY</a>
```

### 3. `<a>` 태그 안에 `<button>` 중첩 (HTML5 표준 위반)
```html
<!-- ⚠️ 비권장: 대화형(Interactive) 요소 간 중첩으로 HTML5 표준 위반 및 브라우저 호환성 문제 -->
<a th:href="@{/board/list}">
  <button type="button" class="btn btn-info">LIST</button>
</a>

<!-- ⭕ 권장: <a> 태그 자체에 Bootstrap 버튼 클래스 적용 -->
<a th:href="@{/board/list}" class="btn btn-info btnList">LIST</a>
```

---

## 4. 요약 표

| 용도 | 작성 문법 | 생성 결과 예시 |
| :--- | :--- | :--- |
| **단순 링크** | `@{/board/list}` | `/board/list` |
| **경로 변수 (리터럴 방식)** | `@{|/board/modify/${board.bno}|}` | `/board/modify/10` |
| **경로 변수 (표준 방식)** | `@{/board/read/{bno}(bno=${board.bno})}` | `/board/read/10` |
| **쿼리 파라미터** | `@{/board/list(page=1, size=10)}` | `/board/list?page=1&size=10` |
| **경로 + 쿼리 혼합** | `@{/board/read/{bno}(bno=${board.bno}, page=1)}` | `/board/read/10?page=1` |
