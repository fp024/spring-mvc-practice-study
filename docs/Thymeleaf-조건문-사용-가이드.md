# Thymeleaf 조건문(`th:if`, `th:unless`) 사용 가이드

JSP(JSTL)에서 Thymeleaf로 전환할 때의 흔한 실수와 올바른 조건문 사용 패턴(`th:if`, `th:unless`, `th:switch`, `th:block`)을 정리합니다.

---

## 1. 가장 흔한 실수: `<th:if>`는 태그가 아닌 "속성(Attribute)"

JSP/JSTL의 `<c:if>` 태그에 익숙한 경우 Thymeleaf에서도 태그 형태로 작성하는 실수를 하기 쉽습니다.

```html
<!-- ❌ 잘못된 문법: <th:if>라는 HTML 태그는 존재하지 않음 -->
<th:if="${!board.delFlag}">
  <button type="button" class="btn btn-warning btnModify">MODIFY</button>
</th:if>
```

Thymeleaf는 HTML 표준 구조를 그대로 유지하는 **네추럴 템플릿(Natural Template)**을 지향하므로, 조건문은 기존 HTML 태그의 **속성(`th:if="..."`)**으로 부여해야 합니다.

---

## 2. 올바른 조건문 작성 패턴

### 패턴 1: 요소 자체에 직접 속성으로 부여 (권장)
별도의 감싸는 태그 없이 조건이 필요한 HTML 태그에 바로 `th:if`를 붙입니다. 조건이 참(`true`)일 때만 해당 태그 전체가 렌더링됩니다.

```html
<button type="button" class="btn btn-warning btnModify" th:if="${!board.delFlag}">MODIFY</button>
```

### 패턴 2: 감싸는 블록이 필요할 때 (`<th:block>` 활용)
JSTL의 `<c:if>`처럼 **화면에 아무런 부모 HTML 태그를 남기지 않고** 여러 요소를 한 번에 조건부로 묶고 싶을 때 `<th:block>`을 사용합니다.

```html
<!-- JSP의 <c:if test="${...}">와 1:1 매핑되는 방식 -->
<th:block th:if="${!board.delFlag}">
  <button type="button" class="btn btn-warning btnModify">MODIFY</button>
  <button type="button" class="btn btn-danger btnRemove">REMOVE</button>
</th:block>
```

---

## 3. `th:if` vs `th:unless` (부정 조건 처리)

Thymeleaf는 조건이 거짓(`false`)일 때 렌더링하는 **`th:unless`** 속성을 제공합니다.

| 표현 방식 | 코드 예시 | 설명 |
| :--- | :--- | :--- |
| **`th:if` + `!`** | `th:if="${!board.delFlag}"` | 자바 스타일의 논리 부정 연산자 |
| **`th:if` + `not`** | `th:if="${not board.delFlag}"` | Thymeleaf의 텍스트 부정 연산자 |
| **`th:unless`** | `th:unless="${board.delFlag}"` | 조건이 `false`일 때 렌더링 (if-not 역할) |

> [!TIP]
> `th:unless`는 조건식 자체가 참이 아닐 때 실행되므로, `th:unless="${board.delFlag}"`처럼 표현식 안에 부정(`!`)을 또 넣지 않도록 주의합니다.

---

## 4. 다중 분기 처리: `th:switch` & `th:case`

JSP의 `<c:choose>`, `<c:when>`, `<c:otherwise>`에 대응되는 다중 분기 문법입니다.

```html
<!-- JSP의 <c:choose>와 대응 -->
<div th:switch="${board.writer}">
  <span th:case="'admin'">관리자 작성글</span>
  <span th:case="'manager'">운영자 작성글</span>
  <!-- default 케이스 (c:otherwise 대응) -->
  <span th:case="*">일반 사용자 작성글</span>
</div>
```

---

## 5. 인라인 삼항 연산자 (Ternary Operator) & Elvis 연산자

태그를 없애거나 표시하는 대신, **속성값이나 텍스트 내용만 조건부로 바꿀 때** 유용합니다.

### 삼항 연산자
```html
<!-- 조건 ? 참일때 : 거짓일때 -->
<span th:text="${board.delFlag ? '삭제됨' : '게시중'}">상태</span>
```

### Elvis 연산자 (`?:`)
값이 `null`이 아니면 그 값을 쓰고, `null`이면 기본값을 출력합니다.
```html
<!-- board.title이 null이면 '제목 없음' 출력 -->
<span th:text="${board.title} ?: '제목 없음'">제목</span>
```

---

## 6. JSP(JSTL) ➡️ Thymeleaf 매핑 요약표

| 기능 | JSP (JSTL) | Thymeleaf |
| :--- | :--- | :--- |
| **단일 조건 (태그 인라인)** | `<c:if test="...">` 태그로 감쌈 | `<tag th:if="...">` (속성으로 직접 부여) |
| **단일 조건 (블록 감싸기)** | `<c:if test="..."> ... </c:if>` | `<th:block th:if="..."> ... </th:block>` |
| **부정 조건** | `<c:if test="${!flag}">` | `th:if="${!flag}"` 또는 `th:unless="${flag}"` |
| **다중 분기** | `<c:choose>` / `<c:when>` | `<div th:switch="...">` / `th:case="..."` |
| **기본값 (Otherwise)** | `<c:otherwise>` | `th:case="*"` |
