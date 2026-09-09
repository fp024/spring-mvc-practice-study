# Thymeleaf HTML 작성 중 Prettier `Unexpected closing tag ":th:div"` 오류 원인 및 해결

Thymeleaf 템플릿(`HTML`) 편집 중 Prettier 포매터에서 다음과 같은 파싱 에러가 발생할 때의 원인과 해결 방법을 정리합니다.

```text
SyntaxError: Unexpected closing tag ":th:div". It may happen when the tag has already been closed by another tag.
For more info see https://www.w3.org/TR/html5/syntax.html#closing-elements-that-have-implied-end-tags (25:17)
  23 |                   <label class="form-label" for="title">Title</label>
  24 |                   <input type="text" id="title" name="title" class="form-control">
> 25 |                 </div>
```

---

## 1. `:th:div`는 무슨 태그인가요?

코드에 직접 작성한 적이 없는 `:th:div`라는 이름이 에러에 등장하는 이유는 **부모 태그인 `<th:block>`** 때문입니다.

- Prettier는 HTML 파서(내부적으로 `angular-html-parser` 기반)를 사용하여 문서를 구문 분석(AST 생성)합니다.
- 네임스페이스가 있는 커스텀 태그(`<th:block>`) 내부에 위치한 자식 요소들을 처리할 때, 파서 내부적으로 부모의 네임스페이스 접두사를 부여하여 `:th:div` 형태로 식별합니다.
- 즉, 문법상의 외계 태그가 아니라 **"th:block 안에 위치한 일반 `<div>` 태그"**를 파서가 지칭한 것입니다.

---

## 2. 왜 태그 짝이 맞는데도 에러가 발생할까요?

실제 HTML 태그의 열림/닫힘 쌍이 정상임에도 위 에러가 지속되는 주된 원인은 다음과 같습니다.

### 2.1. 타이핑 중 일시적인 구문 오류와 자동 포맷팅
코드 작성 중 속성값의 따옴표(`"`)나 태그 괄호(`>`), 슬래시(`/`)가 잠시 열려 있거나 불완전한 상태에서 VS Code의 Auto Save나 자동 포맷팅이 실행되면, 파서가 윗부분의 태그를 정상 인식하지 못하고 지나칩니다.  
그 결과 정상적으로 닫힌 `</div>`를 만났을 때 *"열리지 않은 태그가 닫혔다"*고 판단합니다.

### 2.2. 에디터 / 파서의 메모리 캐시 꼬임 (Stale AST State)
VS Code의 Prettier 확장은 성능을 위해 파일 변경 시 구문 트리(AST)를 메모리에서 증분(incremental) 처리하거나 캐싱합니다.  
편집 과정에서 문법 오류 상태의 캐시가 에디터 메모리에 남아 실제 코드를 올바르게 수정한 이후에도 이전 에러를 계속 띄우는 현상이 발생할 수 있습니다.

---

## 3. 해결 방법

### 방법 1: 부모 블록 주석 처리 후 재저장 (가장 빠르고 확실한 해결책)
파서가 문서 전체를 처음부터 완전히 새로 파싱(Clean Re-parse)하도록 구조적인 큰 변화를 주는 방법입니다.

1. 최상위 `<th:block>` 태그를 잠시 HTML 주석(`<!-- ... -->`) 처리합니다.
2. 파일 저장(`Ctrl + S`).
3. 주석을 다시 해제하고 파일 저장(`Ctrl + S`).

> [!TIP]
> 문서의 큰 블록이 주석 처리되었다가 복원되면 파서가 기존의 꼬여 있던 AST 캐시를 완전히 폐기하고 새로 파싱하므로 오류가 즉시 해결됩니다.

### 방법 2: VS Code Prettier 확장 재시작
1. `F1` 또는 `Ctrl + Shift + P`를 눌러 명령 팔레트 실행
2. `Format Document (Forced)` 또는 `Developer: Reload Window` 실행

### 방법 3: 태그 셀프 클로징 명시
HTML5 void 태그인 `<input>`, `<img>`, `<br>` 등 뒤에 명시적으로 셀프 클로징 슬래시(`/ >`)를 붙여주면 파서가 태그의 끝을 더 엄격하고 명확하게 인식하는 데 도움이 됩니다.

```html
<!-- 셀프 클로징 명시 -->
<input type="text" id="title" name="title" class="form-control" />
```

---

## 4. 요약

| 항목 | 내용 |
| :--- | :--- |
| **현상** | 태그 짝이 맞는데도 `Unexpected closing tag ":th:div"` 발생 |
| **원인** | `<th:block>` 내부 요소를 파싱할 때의 내부 식별명 + 타이핑 중 파서 캐시 꼬임 |
| **조치** | `<th:block>` 주석 처리 후 저장 → 주석 해제 후 재저장하여 파서 리셋 |
