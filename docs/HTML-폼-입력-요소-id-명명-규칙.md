# HTML 폼 입력 요소의 `id` / `name` 명명 규칙 가이드

Spring MVC와 Thymeleaf 환경에서 `<form>` 내부 입력 요소(`<input>`, `<textarea>` 등)와 `<label>`을 작성할 때의 `id`, `name` 명명 규칙 및 권장사항을 정리합니다.

---

## 1. `id`와 `name`의 역할 차이

| 속성 | 주 사용처 | 설명 |
| :--- | :--- | :--- |
| **`name`** | **서버 전송** (백엔드) | HTTP 요청 시 폼 데이터의 파라미터 키가 되며, Spring MVC의 DTO(예: `BoardDTO.title`)에 자동 바인딩되는 기준입니다. |
| **`id`** | **브라우저 문서** (프론트엔드) | HTML 문서 내에서 유일해야 하는 식별자입니다. `<label for="...">` 연결, JavaScript DOM 탐색, CSS 선택자 등에서 사용됩니다. |

---

## 2. 권장 방식: `name` 및 DTO 필드명과 일치시키기 (`id="title"`)

별도의 ID 충돌 위험이 없는 일반적인 단일 폼 화면에서는 **`id`를 DTO 필드명(`name`)과 동일하게 맞추는 것**을 가장 권장합니다.

```html
<div class="mb-3">
  <label class="form-label" for="title">Title</label>
  <input type="text" id="title" name="title" class="form-control">
</div>
```

### 권장 이유

1. **Thymeleaf 폼 바인딩 표준과의 일치**:
   - Thymeleaf의 폼 바인딩 문법인 `th:field="*{title}"`를 사용하면, 렌더링 시 자동으로 `id="title" name="title"`이 생성됩니다.
   - 따라서 처음부터 `id="title"` 형태로 맞춰두면 향후 `th:field`로 리팩터링하거나 혼용할 때 일관성이 유지됩니다.
2. **간결성과 직관성**:
   - 불필요한 접두사(`form_`, `input_` 등) 없이 필드의 목적을 바로 파악할 수 있습니다.
3. **웹 접근성(Accessibility) 보장**:
   - `<label for="title">`과 정확히 매핑되어 라벨 클릭 시 해당 input으로 포커스가 정상 이동하며, 스크린 리더도 올바르게 인식합니다.

---

## 3. 접두사(Prefix)가 필요한 경우와 케이싱 규칙

한 페이지 내에 여러 폼(예: 본문 등록 폼, 검색 폼, 모달 팝업 폼 등)이 공존하여 `id` 중복 위험이 있을 때는 접두사를 사용해 구분할 수 있습니다.

### 케밥 케이스(`kebab-case`) 사용 권장
HTML/CSS 표준 관례에서는 언더스코어(`_`, 스네이크 케이스)보다 **하이픈(`-`, 케밥 케이스)**을 주로 사용합니다.

```html
<!-- 권장: 케밥 케이스 (어떤 폼인지 명확히 표현) -->
<label class="form-label" for="board-title">Title</label>
<input type="text" id="board-title" name="title" class="form-control">

<!-- 비권장: 스네이크 케이스 (DB 컬럼 스타일) -->
<label class="form-label" for="form_title">Title</label>
<input type="text" id="form_title" name="title" class="form-control">
```

> [!NOTE]
> `id`에는 접두사가 붙더라도 서버로 전송되는 파라미터 키는 `name="title"`이므로 Spring DTO 바인딩에는 아무런 영향을 주지 않습니다.

---

## 4. 요약

| 상황 | `id` 작성 권장 | 예시 |
| :--- | :--- | :--- |
| **일반적인 단일 폼 (기본)** | DTO 필드명/`name`과 일치 | `id="title"` |
| **Thymeleaf `th:field` 사용 시** | Thymeleaf 자동 생성 (`name`과 일치) | `th:field="*{title}"` |
| **다중 폼 / ID 충돌 방지 필요 시** | 명확한 컨텍스트 접두사 + 케밥 케이스 | `id="board-title"`, `id="search-keyword"` |
