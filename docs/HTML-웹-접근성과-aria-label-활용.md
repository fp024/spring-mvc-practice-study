# HTML 웹 접근성과 `aria-label` 활용 가이드

SonarQube(SonarLint)의 웹 접근성 경고 원인과 Bootstrap 입력 그룹(`input-group`) 등에서 `<label>` 태그 대신 `aria-label`을 활용해 접근성을 준수하는 방법을 정리합니다.

---

## 1. 문제 현상: SonarQube 경고 발생

HTML 파일에서 다음과 같은 `<input>` 요소를 작성했을 때 SonarQube / SonarLint에서 경고가 발생할 수 있습니다.

```html
<div class="input-group">
  <span class="input-group-text">Bno</span>
  <input type="text" class="form-control" th:value="${board.bno}" readonly />
</div>
```

* **경고 내용**: `All form controls should have associated labels` (규칙: `Web:S5254`)
* **의미**: 모든 입력 폼 컨트롤(`input`, `select`, `textarea`)에는 연결된 레이블(이름)이 있어야 함.

---

## 2. 왜 경고가 발생할까요? (웹 접근성과 스크린 리더)

1. **사람의 눈 vs 스크린 리더**
   - **일반 사용자**: 바로 옆의 `<span class="input-group-text">Bno</span>`를 시각적으로 보고 "아, 글 번호 칸이구나" 하고 쉽게 인식합니다.
   - **스크린 리더(화면 낭독기)**: 시각장애인용 스크린 리더는 코드 구조상 `<span>`과 `<input>`이 서로 연관되어 있는지 알 수 없습니다. 따라서 사용자가 이 입력창에 포커스를 맞추었을 때 단순 **"편집창(이름 없음)"**으로만 읽어주게 됩니다.
2. **WCAG(웹 콘텐츠 접근성 가이드라인) 요구사항**
   - 웹 표준에서는 시각적 디자인과 상관없이 모든 컨트롤이 스크린 리더가 읽을 수 있는 **접근 가능한 이름(Accessible Name)**을 필수로 갖도록 요구합니다.

---

## 3. 해결책: `aria-label` 속성 활용

Bootstrap의 `input-group`처럼 디자인 구조상 `<label>` 태그를 배치하기 어색한 경우, **`aria-label` 속성을 사용하는 것이 표준적인 해결책**입니다.

```html
<div class="input-group">
  <span class="input-group-text">Bno</span>
  <!-- aria-label 속성 추가 -->
  <input
    type="text"
    class="form-control"
    th:value="${board.bno}"
    aria-label="Bno"
    readonly
  />
</div>
```

### 왜 `aria-label`을 넣으면 경고가 꺼질까요?
- `aria-label`은 W3C의 WAI-ARIA(Web Accessibility Initiative) 표준 속성입니다.
- 화면 디자인(시각적 UI)에는 전혀 영향을 주지 않으면서, 스크린 리더에게 **"이 컨트롤의 이름은 'Bno'입니다"**라고 직접 알려줍니다.
- SonarQube는 스크린 리더가 읽을 수 있는 이름(Accessible Name)이 확보되었음을 감지하고 경고를 해제합니다.

---

## 4. 접근 가능한 이름을 부여하는 3가지 방법 비교

| 방법 | 코드 예시 | 적합한 상황 |
| :--- | :--- | :--- |
| **1. `<label>` + `id` 연결** | `<label for="title">Title</label>`<br>`<input id="title" ... />` | 일반적인 등록/수정 입력 폼 (가장 기본 권장) |
| **2. `aria-label` 직접 명시** | `<input aria-label="Bno" ... />` | `input-group-text` 사용 시, 검색창, 아이콘만 있는 버튼 등 |
| **3. `aria-labelledby` 참조** | `<span id="bno-lbl">Bno</span>`<br>`<input aria-labelledby="bno-lbl" ... />` | 이미 존재하는 다른 요소의 텍스트를 그대로 라벨로 참조할 때 |

---

## 5. 요약

* 읽기 전용(`readonly`) 필드라 하더라도 입력 폼 요소(`<input>`)라면 스크린 리더를 위한 이름(Accessible Name)이 필요합니다.
* 디자인상 별도의 `<label>` 태그를 두기 어려울 때는 **`aria-label="항목명"`**을 추가하는 것이 가장 간결하고 웹 표준에 부합하는 해결책입니다.
