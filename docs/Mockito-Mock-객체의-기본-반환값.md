# Mockito Mock 객체의 기본 반환값 (Default Return Values)

Mockito에서 별도의 스터빙(`when(...).thenReturn(...)`) 없이 `@Mock` 객체의 메서드를 호출했을 때 반환되는 기본값과 동작 원리를 정리합니다.

---

## 1. 개요
Mockito는 테스트 도중 발생할 수 있는 불필요한 `NullPointerException(NPE)`을 방지하고 테스트 코드를 간결하게 유지하기 위해, 기본 Answer 전략(`Answers.RETURNS_DEFAULTS`)에서 컬렉션이나 컨테이너 타입에 대해 **비어 있는 객체(Empty)**를 반환합니다.

따라서 `List<BoardDTO>`를 반환하는 메서드는 스터빙하지 않아도 `null`이 아닌 **빈 리스트(`[]`)**를 반환합니다.

---

## 2. 반환 타입별 Mockito 기본값

| 타입 분류 | 반환 타입 | 기본 반환값 (Stubbing 없을 때) |
| :--- | :--- | :--- |
| **컬렉션** | `List<T>` | `Collections.emptyList()` (빈 불변 리스트) |
| | `Set<T>` | `Collections.emptySet()` |
| | `Map<K, V>` | `Collections.emptyMap()` |
| | `Collection<T>` | `Collections.emptyList()` |
| **배열** | `T[]`, 기본형 배열 | 길이가 0인 빈 배열 (`new T[0]`) |
| **컨테이너 / 스트림** | `Optional<T>` | `Optional.empty()` |
| | `Stream<T>` | `Stream.empty()` |
| **기본형 (Primitive)** | `boolean` | `false` |
| | `int`, `long`, `short`, `byte` | `0` |
| | `float`, `double` | `0.0` |
| | `char` | `'\u0000'` |
| **기타 일반 객체** | 커스텀 DTO, 엔티티, `String` 등 | `null` |

---

## 3. 실전 예시

### 3.1. Controller 코드
```java
@GetMapping("/list")
public void list(Model model) {
  // boardService.getList()가 빈 리스트를 반환하므로 model에는 빈 리스트가 담김
  model.addAttribute("list", boardService.getList());
}
```

### 3.2. Mockito 단위 테스트
```java
@ExtendWith(MockitoExtension.class)
class BoardControllerTests {

  @Mock
  private BoardService boardService;

  @InjectMocks
  private BoardController boardController;

  @Test
  void testList() throws Exception {
    // when(boardService.getList()).thenReturn(...) 스터빙을 생략해도
    // boardService.getList()는 null이 아닌 빈 리스트([])를 반환하여 NPE 없이 통과
    mockMvc.perform(get("/board/list"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("list"));
  }
}
```

---

## 4. 주의사항

- **`null` 상황을 검증해야 하는 경우**:
  - 기본값이 `emptyList`이므로, 서비스가 `null`을 반환했을 때의 예외나 분기 처리를 테스트하려면 반드시 명시적으로 `null`을 스터빙해야 합니다.
  ```java
  when(boardService.getList()).thenReturn(null);
  ```
- **불변 리스트 반환**:
  - 기본 반환되는 빈 컬렉션은 불변 객체(`Collections.emptyList()`)이므로, 컨트롤러나 서비스에서 반환된 컬렉션에 `.add()` 등을 수행하면 `UnsupportedOperationException`이 발생합니다.
