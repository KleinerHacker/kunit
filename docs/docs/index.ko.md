# KUnit

**KUnit**은 맨 숫자 대신 물리 단위로 계산하기 위한 Kotlin 프레임워크(Java에서도 사용 가능)입니다. 미터, 마일,
제곱미터를 순수한 `Double` 값으로 추적하며 모든 호출 지점이 단위에 대해 일치하기를 바라는 대신, `kunit`은 값과
함께 단위를 지니고 다니며 변환, 곱셈, 차원 장부 정리를 대신 처리합니다.

## 왜 KUnit인가?

물리량을 원시 숫자로 다루는 것은 오류가 발생하기 쉽습니다: 변환 없이 실수로 미터와 마일을 더하거나 면적과
길이를 더하기 쉽습니다. kunit은 단위를 타입의 일부로 만들어 이를 해결합니다:

- **두 개의 동사, `of`와 `into`.** `number of <단위>`(`5 of meters`)로 만들고, `value into <단위>`
  (`v into kilo.meters`)로 읽습니다. 숫자와 단위는 엄격하게 분리됩니다.
- **타입 안전한 산술.** 호환되지 않는 단위 그룹이나 지수 간의 `+`와 `-`는 조용히 잘못된 숫자를 만들어내는 대신
  `IllegalStateException`을 던집니다.
- **자동 변환.** `(5 of meters) + (3 of miles)`는 그냥 동작합니다 — 두 피연산자가 내부적으로 정규화되므로,
  결합하기 전에 단위를 수동으로 변환할 필요가 전혀 없습니다.
- **자유로운 곱셈과 나눗셈.** 단위의 곱셈이나 나눗셈은 *항상* 허용되며 결과 물리 차원(지수)을 자동으로
  추적합니다. 예: `length * length`는 면적이 됩니다.
- **완전한 `Number` 지원.** `Int`, `Long`, `Float`, `Double` 및 기타 모든 `Number` 타입에서 값을 만들 수
  있습니다. 모든 것은 내부적으로 `Double`로 정규화됩니다.
- **완전한 SI 접두사 표**, Quetta(Q)부터 Quecto(q)까지, 단위별 접두사 정책이 컴파일 타임에 강제되는 접두사
  빌더(`kilo.meters`, `milli.seconds`)로 제공됩니다.
- **명명된 특수 단위**(헥타르, 리터, 에이커 등)를 `of`/`into`와 함께 사용하는 일반적인 값 1 토큰으로 제공.

## 핵심 개념

kunit은 두 개의 중심 타입을 중심으로 구축됩니다:

- **`KMixedUnitInstance`** — *혼합 단위*("Mischeinheit"): `Double` 기본 값과 하나 이상의 `KUnit`(각각 정수
  지수와 쌍을 이룸, 예: 속도의 경우 `m^1 * s^-1`). 이것은 나머지 모든 것을 구동하는 범용 엔진입니다.
- **`KUnit`** — 단위 그룹에 속하는 단일 "순수" 단위(예: 미터는 길이 그룹에 속함). 구체적인 단위 그룹은
  `enum class ... : KUnit`(예: `KDistanceUnit`)로 모델링됩니다.

각 단위 그룹은 추가로 단일 단위 그룹으로 제한된 `KMixedUnitInstance`를 캡슐화하고 항상 그 그룹의 기본 단위로
정규화된 **래퍼 클래스**(예: `KLengthUnitInstance`)를 제공합니다. 이것이 대부분의 경우 사용하게 될 타입입니다 —
현재 제공되는 단위는 [사전 정의된 단위](units/kinematics/distance.md)를, 언제 어떻게 범용 `KMixedUnitInstance` 엔진으로
직접 내려갈지는 [혼합 단위](mixed-units.md)를 참조하세요.

새로운 물리량(질량이나 시간 등)에 대한 지원을 추가하고 싶다면, 완전한 단계별 안내는
[사용자 정의 단위 추가](custom-units.md)를 참조하세요.

!!! note "단위 객체는 불변입니다"
    모든 단위 값 — `KMixedUnitInstance` 엔진 및 `KLengthUnitInstance`나 `KTimeUnitInstance` 같은 모든 "순수"
    래퍼 — 은 **불변**입니다. 어떤 연산도 기존 인스턴스를 변경하지 않습니다. 연산자(`+`, `-`, `*`, `/`)와 변환은
    항상 **새로운** 객체를 반환하며 피연산자는 그대로 둡니다. 이로써 단위 값을 자유롭게 공유하거나 키나 상수로
    사용해도 안전합니다.

## 빠른 시작

모듈을 의존성으로 추가하고(또는 프로젝트/소스 세트로 포함하고) 필요한 단위 그룹의 어휘를 가져옵니다.

### 길이

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// 임의의 Number 타입에서 `of`로 순수한 길이 값을 만들기
val distance = 5 of meters
val trip = 10 of miles

// 연산자: 같은 그룹과 지수 내에서의 자동 변환
val total = distance + trip          // KLengthUnitInstance, 미터로 정규화
val diff = trip - distance

// 비교
val isFarther = trip > distance      // true

// `into`로 특정 단위의 값을 읽기
println(total into kilo.meters)      // 예: 21.0467...
println(total into yards)            // 예: 23018.4...

// 순수한 길이를 곱하면 강하게 타입이 지정된 면적이 됨
val area = distance * trip           // KAreaUnitInstance

// 면적(지수 2)과 부피(지수 3)를 위한 명명된 특수 단위
val plot = 3 of hectares
println(plot into ares)              // 300.0

val tank = 200 of liters
println(tank into usGallons)
```

### SI 접두사

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters

// `5 of kilo.meters` -> KLengthUnitInstance(== 5000 m)
val fiveKm = 5 of kilo.meters
println(fiveKm.value) // 5000.0(미터로 정규화)
```

### 혼합 / 복합 단위

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds

// 값 1 템플릿에서 단위 표현식을 조합하고 `of`로 스케일링
val accel = 10 of meters / (seconds pow 2)   // KMixedUnitInstance, m·s⁻²
```

## 체크아웃 및 빌드

```bash
git clone <repository-url>
cd kunit
```

kunit은 Gradle을 사용합니다(래퍼가 저장소에 포함되어 있어 로컬 Gradle 설치가 필요 없습니다):

```bash
# 빌드
./gradlew build          # Windows: gradlew.bat build

# 테스트만 실행
./gradlew test            # Windows: gradlew.bat test
```

툴체인 25를 해결할 수 있는 JDK가 필요합니다(필요한 경우 `foojay-resolver` 플러그인이 자동으로 다운로드합니다).
