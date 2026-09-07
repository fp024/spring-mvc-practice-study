# Prettier 설정 안내

프로젝트 루트의 [`.prettierrc`](../.prettierrc)에 정의된 코드 포맷터(Prettier) 설정에 대한 설명입니다.

---

## 1. 설정 옵션 상세

```json
{
  "useTabs": false,
  "printWidth": 100,
  "tabWidth": 2,
  "trailingComma": "es5",
  "semi": true,
  "singleQuote": false,
  "arrowParens": "always",
  "endOfLine": "lf"
}
```

| 옵션명 | 설정값 | 설명 |
| :--- | :--- | :--- |
| **`useTabs`** | `false` | 들여쓰기 시 탭(Tab) 대신 공백(Space)을 사용합니다. |
| **`printWidth`** | `100` | 한 줄의 최대 길이를 100자로 제한하며, 초과 시 자동으로 줄바꿈합니다. |
| **`tabWidth`** | `2` | 들여쓰기 깊이를 공백 2칸으로 지정합니다. |
| **`trailingComma`** | `"es5"` | ES5에서 문법적으로 유효한 곳(객체 리터럴, 배열 리터럴의 마지막 프로퍼티 등)에 후행 쉼표(trailing comma)를 붙입니다. (함수 파라미터 제외) |
| **`semi`** | `true` | 모든 문장의 끝에 세미콜론(`;`)을 항상 추가합니다. |
| **`singleQuote`** | `false` | 문자열 리터럴에 작은따옴표(`'`) 대신 큰따옴표(`"`)를 기본으로 사용합니다. |
| **`arrowParens`** | `"always"` | 화살표 함수에서 매개변수가 1개일 때도 항상 괄호로 감쌉니다. (예: `(x) => x`) |
| **`endOfLine`** | `"lf"` | 개행 문자로 `LF`(`\n`)를 강제합니다. (Windows 환경의 `CRLF` 혼용으로 인한 Git 충돌 방지) |

---

## 2. 포맷팅 실행 스크립트

[`package.json`](../package.json#L9)에 정의된 스크립트를 통해 대상 파일(`.html`, `.css`, `.js`, `.json`)에 일괄 포맷팅을 적용할 수 있습니다.

```bash
pnpm run format
```

> **참고**: 무시 대상 경로는 [`.prettierignore`](../.prettierignore)에 정의되어 있습니다 (`node_modules`, `.git`, `.vscode` 등).
