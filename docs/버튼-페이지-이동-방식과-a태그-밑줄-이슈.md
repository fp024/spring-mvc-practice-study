# 버튼 페이지 이동 구현 방식 비교: `<a>` 링크 방식 vs JavaScript Form 전송 방식

게시판 화면(`read.html`, `modify.html`)에서 '목록', '수정', '삭제' 등의 버튼을 구현할 때 사용하는 두 가지 대표적인 패턴과, `<a>` 태그 중첩 시 발생하는 밑줄(`_`) 버그 원인을 정리합니다.

---

## 1. 두 가지 이동 방식 비교

### 방식 1: HTML `<a>` 링크 태그 기반 (`read.html` 권장 방식)
단순한 페이지 이동(GET 요청)에 적합한 방식입니다.

```html
<!-- ⭕ 올바른 작성법: <a> 태그 자체를 버튼으로 디자인 -->
<a th:href="@{/board/list}" class="btn btn-info">LIST</a>
<a th:href="@{|/board/modify/${board.bno}|}" class="btn btn-warning">MODIFY</a>
```

* **장점**:
  - 별도의 자바스크립트 코드 없이 순수 HTML로 깔끔하게 동작합니다.
  - 마우스 우클릭 "새 탭에서 열기"나 `Ctrl + 클릭`이 정상 지원됩니다.
* **적합한 상황**:
  - 단순 화면 이동 (상세보기, 수정 페이지 진입, 목록으로 돌아가기)

---

### 방식 2: JavaScript + Form 전송 기반 (`modify.html` 방식)
화면에 하나의 폼(`<form id="actionForm">`)을 두고, 버튼 클릭 시 JS 이벤트로 `action`과 `method`를 동적으로 변경하여 제출하는 방식입니다.

```html
<!-- HTML -->
<form id="actionForm" action="/board/modify" method="post"></form>

<div class="float-end">
  <button type="button" class="btn btn-info btnList">LIST</button>
  <button type="button" class="btn btn-warning btnModify">MODIFY</button>
  <button type="button" class="btn btn-danger btnRemove">REMOVE</button>
</div>

<!-- JavaScript -->
<script type="text/javascript">
  const formObj = document.querySelector("#actionForm");

  // MODIFY: POST 전송
  document.querySelector(".btnModify").addEventListener("click", () => {
    formObj.action = "/board/modify";
    formObj.method = "post";
    formObj.submit();
  });

  // LIST: GET 전송
  document.querySelector(".btnList").addEventListener("click", () => {
    formObj.action = "/board/list";
    formObj.method = "get";
    formObj.submit();
  });

  // REMOVE: POST 전송
  document.querySelector(".btnRemove").addEventListener("click", () => {
    formObj.action = "/board/remove";
    formObj.method = "post";
    formObj.submit();
  });
</script>
```

* **장점**:
  - **POST 요청 처리 가능**: 수정(`modify`), 삭제(`remove`)처럼 서버 데이터를 변경하는 작업은 보안상 반드시 POST 방식으로 전송해야 하므로 이 방식이 필수적입니다.
  - **파라미터 보존 용이**: 목록으로 돌아갈 때 현재 페이지 번호, 검색 키워드 등의 `<input type="hidden">` 파라미터를 유지한 채 함께 전송하기 좋습니다.
  - `<a>` 태그를 쓰지 않으므로 링크 관련 스타일 문제(밑줄 등)가 발생하지 않습니다.
* **적합한 상황**:
  - 수정/삭제 폼 전송 처리, 페이징/검색 조건을 유지해야 하는 복합 화면

---

## 2. `read.html`에서 겪었던 밑줄(`_`) 버그의 원인

```html
<!-- ❌ 문제가 되었던 코드 -->
<a th:href="@{/board/list}">
  <button type="button" class="btn btn-info">LIST</button>
</a>
```

### 왜 버튼 옆에 `_` 밑줄이 보였을까요?
1. **인라인 요소의 공백 렌더링**: `<a>` 태그와 내부 `<button>` 태그 사이의 **줄바꿈(엔터) 및 들여쓰기 공백**이 브라우저에서 텍스트 공백 1칸으로 변환되었습니다.
2. **`<a>` 태그의 기본 밑줄 적용**: `<a>` 태그의 기본 브라우저 스타일인 `text-decoration: underline`이 그 **공백 텍스트에 적용**되면서 버튼 옆/아래로 삐져나온 밑줄(`_`)처럼 표시된 것입니다.
3. **HTML5 표준 위반**: HTML5 명세에서는 대화형(Interactive) 요소인 `<a>` 안에 또 다른 대화형 요소인 `<button>`을 중첩하는 것을 허용하지 않습니다.

### 해결책
`<a>` 태그 안에 `<button>`을 넣지 않고, **`<a>` 태그 자체에 Bootstrap 버튼 클래스(`.btn`)를 부여**하면 밑줄도 사라지고 HTML5 표준도 완벽히 충족됩니다.

---

## 3. 상황별 권장 가이드

| 상황 | 추천 방식 | 이유 |
| :--- | :--- | :--- |
| **단순 조회 및 이동** (예: `read.html`의 목록, 수정 버튼) | `<a class="btn ...">` | 가볍고 직관적이며 JS 불필요 |
| **데이터 변경(수정/삭제)** (예: `modify.html`의 수정완료, 삭제) | `<button>` + JS Form Submit | POST 요청 전송 필수 |
| **검색 조건/페이징 유지 이동** | `<button>` + JS Form Submit | hidden 파라미터를 폼과 함께 일괄 전송 가능 |
