# Mockito 테스트에 `-Xshare:off` 를 붙이는 이유

이 프로젝트는 `pom.xml` 의 surefire 설정에서 테스트 JVM 옵션을 다음과 같이 주고 있다.

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-surefire-plugin</artifactId>
  <configuration>
    <argLine>-javaagent:${org.mockito:mockito-core:jar} -Xshare:off</argLine>
  </configuration>
</plugin>
```

(`pom.xml:289`)

여기서 `-Xshare:off` 가 무슨 역할을 하고, 왜 **테스트 환경에 한해** 붙이는 편이 나은지 정리한다.
아래 실측값은 모두 이 저장소에서 직접 측정한 것이며, 측정 환경은 다음과 같다.

- OS: Windows 11 Pro (26200)
- 테스트 실행 JDK: Temurin **21.0.12+8** (`toolchains.xml` 의 JDK 21)
- Mockito **5.23.0**, JUnit 6.1.3, maven-surefire-plugin 3.5.6

---

## 1. 배경: Mockito 5 의 inline mock maker 와 Java agent

- Mockito 5 부터는 기본 mock maker 가 `mock-maker-inline` 이다.
  서브클래스를 만들어 mock 을 대신 끼우는 방식이 아니라, **이미 로딩된 클래스의 바이트코드를
  런타임에 다시 변환(retransform)** 해서 mock 을 만든다.
  (mockito-core 의 MANIFEST 에도 `Can-Retransform-Classes: true` 가 들어 있다.)
- 이 retransform 을 하려면 `java.lang.instrument.Instrumentation` 이 필요하고,
  그래서 Mockito(정확히는 Byte Buddy)는 Java agent 로 붙는다.
  - 예전 방식: 테스트 실행 중에 self-attach (동적 agent 로딩)
  - 권장 방식: 위 설정처럼 `-javaagent:` 로 **JVM 시작 시점에** 붙이기
    (JDK 21 의 [JEP 451] 이후 동적 agent 로딩은 경고를 띄우고, 향후 릴리스에서
    기본 차단 예정이라 `-javaagent` 방식이 안전하다.)
- 이때 Byte Buddy 는 자기 helper 클래스를 부트스트랩 클래스로더가 볼 수 있게
  `Instrumentation.appendToBootstrapClassLoaderSearch(...)` 로 **부트스트랩 클래스패스에
  jar 를 덧붙인다.** 이 동작이 뒤에서 설명할 CDS 와 정면으로 충돌한다.

[JEP 451]: https://openjdk.org/jeps/451

## 2. `-Xshare` 는 CDS(Class Data Sharing) 스위치

CDS 는 JDK 클래스들의 파싱/검증 결과를 미리 아카이브(`classes.jsa`)로 만들어 두고,
JVM 기동 시 그 파일을 **읽기 전용으로 메모리 매핑** 해서 재사용하는 기능이다.
JVM 시작 시간과 메모리 사용량을 줄여준다.

| 값             | 의미                                                         |
| -------------- | ------------------------------------------------------------ |
| `-Xshare:auto` | 가능하면 CDS 사용, 실패하면 조용히 무시 (**JDK 12+ 기본값**) |
| `-Xshare:on`   | CDS 를 반드시 사용, 아카이브 매핑 실패 시 JVM 기동 실패      |
| `-Xshare:off`  | CDS 를 아예 사용하지 않음                                    |

아무 설정도 안 하면 최신 JDK 는 기본 CDS 아카이브를 켠 상태로 테스트를 돌린다.
`java -version` 출력 끝에 `mixed mode, sharing` 이 붙어 있으면 CDS 가 켜진 상태다.

```console
$ java -version
OpenJDK 64-Bit Server VM Temurin-21.0.12+8 (build 21.0.12+8-LTS, mixed mode, sharing)
                                                                             ^^^^^^^
```

---

## 3. 테스트 환경에서 off 가 이득인 이유

### 3-1. 테스트를 돌릴 때마다 VM 경고가 stderr 에 찍힌다

`-javaagent` 로 Mockito 를 붙인 JVM 에서 Mockito 를 사용하면 아래 경고가 stderr 로 나온다.

```
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes
because bootstrap classpath has been appended
```

부트스트랩 클래스패스가 변경되면 아카이브에 저장된 전제(클래스패스 구성)가 깨지므로,
JVM 이 "이제 부트 로더 클래스만 공유하겠다"고 알려주는 것이다.

#### 경고가 나는 시점 (실측)

`-javaagent` 를 붙이기만 하고 JVM 이 그냥 뜨는 시점에는 **경고가 나오지 않는다.**
mockito-core 의 `Premain-Class`(`org.mockito.internal.PremainAttach`)는 `Instrumentation`
인스턴스를 받아 보관만 하고, 부트스트랩 append 는 Byte Buddy 가 실제로 필요할 때 일어난다.

```console
$ java -javaagent:mockito-core-5.23.0.jar -version      # 경고 없음
OpenJDK 64-Bit Server VM Temurin-21.0.12+8 (..., mixed mode, sharing)
```

그런데 그 "필요한 시점" 은 생각보다 훨씬 빠르다. 확인해 보면
**`Mockito.framework()` 를 한 번 호출한 것만으로도** — 즉 mock 을 하나도 만들지 않았는데도 —
이미 append 가 일어나 경고가 나온다.

| 프로그램이 한 일                | `-javaagent` + CDS 기본 |
| ------------------------------- | ----------------------- |
| `Mockito.framework()` 만 호출   | 경고 발생               |
| 일반 클래스 mock 1개 생성       | 경고 발생               |
| JDK 인터페이스(`List`) mock 생성 | 경고 발생               |

즉 **`MockitoExtension` 이 붙은 테스트가 하나라도 있으면 그 테스트 JVM 에서는 경고가 난다.**
`@Mock` 없이 `@InjectMocks` 만 쓰는 현재 저장소의 `HomeControllerStandaloneTests`
같은 테스트도 예외가 아니다.

또한 이 경고는 CDS 를 "반드시 쓰라"고 강제해도 사라지지 않는다.

```console
$ java -Xshare:on -javaagent:mockito-core-5.23.0.jar ...
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes ...
   (기동 실패는 아니고, 경고만 그대로)
```

#### 다른 방법으로는 끌 수 없다

- 이 메시지는 유니파이드 로깅(`-Xlog:...`)이 아니라 VM 의 `warning` 출력이라
  `-Xlog:cds=off` 같은 옵션으로는 억제되지 않는다.
- Maven 의 `-q` 는 Maven 로그만 줄일 뿐, 포크된 테스트 JVM 이 stderr 로 직접 쓰는 이 줄에는
  영향이 없다.
- `-Xshare:off` 로 CDS 자체를 끄면, 애초에 "공유가 제한된다"고 알릴 일이 없어져 경고가 사라진다.

```console
$ java -javaagent:mockito-core-5.23.0.jar -Xshare:off ...
mock ok, size=42        # 경고 없이 깨끗
```

#### 경고 한 줄이 실제로 만드는 비용

"경고 한 줄쯤" 으로 넘기기 쉽지만, 테스트 환경에서는 아래처럼 번진다.

- **실패 분석 비용**: 테스트가 깨졌을 때 봐야 할 stderr 맨 위에 무관한 VM 경고가 있으면,
  원인 후보를 하나 더 배제하는 데 시간이 든다. 특히 Mockito 관련 실패일 때
  "이 경고 때문인가?" 라는 오해를 유발한다.
- **CI 오탐**: stderr 에 출력이 있으면 실패로 처리하거나 경고 배지를 붙이는 파이프라인,
  또는 로그 스캐너에서 매 빌드마다 걸린다.
- **surefire 리포트 오염**: 포크된 JVM 의 stderr 는 surefire 리포트/콘솔 출력에 함께 실려서,
  테스트 개수만큼 반복 노출된다.
- **경고 무시 습관**: 항상 떠 있는 경고는 곧 아무도 안 읽게 되고, 나중에 진짜 의미 있는
  VM 경고가 같은 자리에 나와도 묻힌다. 테스트 출력은 신호 대 잡음 비를 지키는 게 이득이다.

### 3-2. "아카이브된 클래스를 재변환" 하는 경로 자체를 안 타게 된다

inline mock maker 는 `retransformClasses` 로 **이미 로딩된 클래스**를 다시 변환한다.
그런데 CDS 로 매핑된 클래스는 공유 아카이브의 읽기 전용 영역에서 온 것이어서,
retransform 대상이 되면 JVM 이 공유 상태를 되돌리고 별도로 다시 만들어야 한다.

- 정상 시나리오에서는 잘 동작하지만, 특정 JDK 버전/플랫폼 조합에서 `hs_err_pid*.log` 를
  남기며 크래시하거나 클래스 변환이 이상하게 동작한다는 보고가 꾸준히 올라오는 영역이다.
  (`java.base` 처럼 아카이브에 들어 있을 가능성이 높은 JDK 내부 클래스를 mock 할 때
  더 잘 드러난다. 이 프로젝트도 `mock(List.class)` 같은 코드가 언제든 들어올 수 있다.)
- CDS 를 끄면 모든 클래스가 평범하게 클래스패스에서 로딩·검증되므로, 변환 경로가 하나로
  단순해지고 JDK 를 올릴 때마다 이 조합을 다시 의심할 필요가 없다.
- **진단 비용도 줄어든다.** 테스트 JVM 이 크래시했을 때 CDS 가 꺼져 있으면
  "일단 CDS 끄고 재현해 보라" 는 첫 단계를 건너뛸 수 있고, 원인 후보에서 아카이브 매핑을
  바로 제외할 수 있다.

테스트 JVM 은 짧게 떴다 사라지는 일회성 프로세스이므로, **여기서 CDS 를 포기하는 대가가
가장 작다** 는 점이 이 선택의 핵심이다. 얻는 것은 안정성과 단순함, 잃는 것은 곧 종료될
프로세스의 기동 시간 수십 ms 다.

### 3-3. 실행 환경 차이에 따른 재현성이 좋아진다

CDS 아카이브의 유효성과 존재 여부는 환경에 따라 다르다.

- JDK 배포판/이미지에 따라 `classes.jsa` 가 아예 없을 수 있다(그 경우 `auto` 는 조용히 무시).
- JDK 버전, 클래스패스 구성, agent 유무에 따라 공유 범위가 달라진다.
- 컨테이너에서는 아카이브 파일 접근/메모리 매핑 조건이 로컬과 다를 수 있다.

그래서 `auto` 로 두면 **개발자 로컬 · CI 러너 · Docker 이미지에서 경고가 나는 곳과
안 나는 곳이 갈린다.** "내 로컬에선 안 나는데 CI 에서만 나온다" 는 종류의 질문이 생기고,
이를 추적하는 시간이 실제 비용이다. `-Xshare:off` 는 이 변수를 제거해서 어디서 돌려도
같은 조건을 만든다. 테스트 환경에서 재현성은 속도보다 우선한다.

### 3-4. 테스트 JVM 은 애초에 CDS 이득을 덜 받는다

CDS 기본 아카이브에 담기는 것은 대부분 **JDK 부트 클래스**다.
반면 테스트 실행 시간의 대부분은 Spring · JUnit · Mockito · 애플리케이션 클래스처럼
**앱 클래스로더가 로딩하는 클래스**가 차지한다. 이들은 기본 아카이브의 대상이 아니다.

- 앱 클래스까지 아카이브하려면 AppCDS(`-XX:SharedArchiveFile`)를 써야 하는데,
  **agent 가 부트스트랩 클래스패스를 건드리면 공유가 부트 로더 클래스로 제한**되므로
  "테스트를 AppCDS 로 더 빠르게" 라는 대안 자체가 이 조합에서는 성립하지 않는다.
- 게다가 mock 대상 클래스는 어차피 retransform 되어 아카이브된 형태를 그대로 쓰지 못한다.

즉 이 조합에서 CDS 가 남겨주는 이득은 **부트 클래스 로딩 단축분** 정도로 한정되고,
그것이 아래 실측한 수십 ms 다.

---

## 4. 비용 (실측 트레이드오프)

같은 프로그램을 12회씩 반복 실행한 결과(JDK 21.0.12, Windows 11):

| 실행 내용                              | CDS 기본(auto) | `-Xshare:off` |     차이 |
| -------------------------------------- | -------------: | ------------: | -------: |
| 빈 JVM (`java -version`)               |  156 ms (평균) |        182 ms | **+26 ms** |
| Mockito agent + mock 1개 생성 프로그램 |        1207 ms |       1286 ms | **+79 ms** |

정리하면 **JVM 하나당 대략 30~80 ms** 다.

- 이 프로젝트는 surefire 기본값(`forkCount=1`, `reuseForks=true`)으로 모든 테스트를
  JVM 하나에서 돌린다. 실제로 `mvn test` 전체가 7.7초에 끝나므로, 추가 비용은 그 중 1% 안쪽이다.
- 반대로 `forkCount` 를 올리거나 `reuseForks=false` 로 바꾸면 이 비용이 **포크 수만큼 곱해진다.**
  예를 들어 테스트 클래스마다 JVM 을 새로 띄우는 구성에서 클래스가 200개면
  30~80 ms × 200 ≈ 6~16초가 된다. 그런 구성으로 바꿀 때는 아래 §5 방법으로 실측하고
  판단하는 것이 좋다.
- **운영/애플리케이션 실행 옵션에는 넣지 말 것.** 위 근거는 전부 "Mockito agent 가 붙는
  테스트 JVM" 에 한정된 이야기다. 실제 서버(Tomcat) 기동에는 CDS 를 켜두는 편이 유리하고,
  거기서는 agent 가 부트스트랩을 건드리지도 않으므로 경고도 나지 않는다.

---

## 5. 확인 · 재현 방법

### CDS 가 켜져 있는지

```bash
java -version                 # 끝에 "sharing" 이 있으면 CDS 사용 중
java -Xlog:cds=info -version  # classes.jsa 매핑 과정을 자세히 출력
```

### 경고 재현

```bash
# 테스트 클래스패스 추출
./mvnw -o -q dependency:build-classpath -Dmdep.outputFile=target/test-cp.txt -Dmdep.includeScope=test

# mock 을 하나 만드는 작은 프로그램을 두 조건으로 실행해 비교
java -javaagent:javaagent-libs/mockito-core-5.23.0.jar            -cp "target/cds-probe;$(cat target/test-cp.txt)" Repro  # 경고 발생
java -javaagent:javaagent-libs/mockito-core-5.23.0.jar -Xshare:off -cp "target/cds-probe;$(cat target/test-cp.txt)" Repro  # 경고 없음
```

### 기동 비용 측정

동일 명령을 10회 이상 반복해 평균/최솟값을 비교한다.
JVM 기동 시간은 편차가 크므로 1~2회 실행만으로 판단하지 않는 것이 좋다.

---

## 6. IDE(IntelliJ IDEA)에서 테스트를 실행할 때

surefire 의 `argLine` 은 **Maven 이 테스트를 포크할 때만** 적용된다.
IDE 에서 테스트 클래스를 직접 실행하면 IntelliJ 가 자체 JVM 을 띄우므로 이 옵션이 안 붙는다.
결과적으로 IDE 에서만 경고가 보이고 "Maven 에서는 안 나는데?" 하는 상황이 생긴다.

- Run/Debug Configurations → 해당 JUnit 설정 → **VM options** 에 다음을 넣는다.

  ```text
  -javaagent:<로컬 저장소 경로>/org/mockito/mockito-core/5.23.0/mockito-core-5.23.0.jar -Xshare:off
  ```

  (이 저장소에는 `javaagent-libs/mockito-core-5.23.0.jar` 도 있어 상대 경로로 지정해도 된다.)

- 매 설정마다 반복하지 않으려면 **Edit Configurations → Templates → JUnit** 의
  VM options 에 넣어 두면 이후 생성되는 설정에 자동 적용된다.

---

## 7. 정리 및 대안 비교

| 선택                                                        | 결과                                                                  |
| ----------------------------------------------------------- | --------------------------------------------------------------------- |
| 아무것도 안 함 (`-Xshare:auto` 기본)                        | 동작은 하지만 Mockito 를 쓰는 테스트 JVM 마다 CDS 경고가 계속 출력됨  |
| `-Xshare:on`                                                | 경고는 그대로 나옴. 아카이브 매핑 실패 시 기동만 더 까다로워짐         |
| **`-Xshare:off` (현재 선택)**                               | 경고 없음, CDS ↔ retransform 충돌 회피, 환경 간 동일 조건, +30~80 ms   |
| `-XX:-UseSharedSpaces`                                      | `-Xshare:off` 와 사실상 동일. 옛 이름이므로 `-Xshare:off` 를 쓰면 됨   |
| `-XX:+EnableDynamicAgentLoading` (pom 에 주석으로 남아 있음) | **다른 문제의 해법.** 동적 agent 로딩 경고를 끄는 옵션이며 CDS 와 무관 |

`-XX:+EnableDynamicAgentLoading` 은 Mockito 를 self-attach 로 붙일 때 나오는 아래 경고를
억제하는 옵션이다. (실제로 `-javaagent` 없이 실행하면 이렇게 나온다.)

```
Mockito is currently self-attaching to enable the inline-mock-maker. This will no longer
work in future releases of the JDK. ...
WARNING: A Java agent has been loaded dynamically (byte-buddy-agent-1.17.7.jar)
WARNING: Dynamic loading of agents will be disallowed by default in a future release
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes ...
```

이 프로젝트는 이미 `-javaagent:${org.mockito:mockito-core:jar}` 로 시작 시점에 붙이고 있어
필요하지 않다. 참고로 위 출력에서 보듯 **self-attach 로 붙여도 CDS 경고는 똑같이 나오므로**,
두 옵션은 목적이 다르고 서로의 대체재가 아니다.

### 결론

Mockito 5 의 inline mock maker + `-javaagent` 조합을 쓰는 **테스트 JVM 에 한해**
`-Xshare:off` 를 붙이는 것이 이득이다.

- 얻는 것: 깨끗한 테스트 출력, 환경 간 동일한 재현 조건, JDK 업그레이드 시 클래스 변환
  관련 사고 가능성과 진단 비용 감소
- 잃는 것: 곧 종료될 테스트 JVM 하나당 기동 시간 30~80 ms (현재 구성에서 포크 1개)

---

## 부록: 앞으로 커버리지 도구를 추가할 때

지금 `argLine` 은 값을 직접 지정하고 있다.

```xml
<argLine>-javaagent:${org.mockito:mockito-core:jar} -Xshare:off</argLine>
```

JaCoCo 처럼 **`argLine` 프로퍼티에 자기 agent 옵션을 채워 넣는 플러그인**을 나중에 추가하면,
위 설정이 그 값을 덮어써서 커버리지가 수집되지 않는다. 그때는 지연 치환 문법으로 바꿔야 한다.

```xml
<argLine>@{argLine} -javaagent:${org.mockito:mockito-core:jar} -Xshare:off</argLine>
```

단, `@{argLine}` 은 해당 프로퍼티가 정의돼 있지 않으면 문자열이 그대로 전달되어 기동이 실패하므로,
`<properties>` 에 기본값(빈 값)을 함께 정의해 두어야 한다. 커버리지 도구를 넣기 전까지는
지금처럼 직접 지정하는 편이 단순하고 안전하다.

## 참고

- JEP 451: Prepare to Disallow the Dynamic Loading of Agents — <https://openjdk.org/jeps/451>
- Mockito `mock-maker-inline` / agent 설정 문서 —
  <https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.2>
- `java` 커맨드 `-Xshare` 옵션 — <https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html>
- Class Data Sharing 개요 — <https://docs.oracle.com/en/java/javase/21/vm/class-data-sharing.html>
