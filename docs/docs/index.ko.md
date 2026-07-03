# kunit

**kunit**은 순수한 숫자 대신 물리 단위로 계산할 수 있게 해주는 Kotlin 프레임워크입니다(자바에서도 사용 가능). 미터, 마일,
평방미터를 단순한 `Double` 값으로 다루면서 모든 호출부가 동일한 단위를 사용한다고 기대하는 대신, kunit은 값과 함께 단위 정보를
함께 다루며 변환, 곱셈, 차원 관리를 대신 처리해줍니다.

## 왜 kunit인가?

물리량을 순수한 숫자로 다루는 것은 오류가 발생하기 쉽습니다. 미터와 마일을 변환 없이 실수로 더하거나, 면적과 길이를 더하는 실수를
저지르기 쉽습니다. kunit은 단위를 타입의 일부로 만들어 이 문제를 해결합니다.

- **타입 안전한 연산.** 서로 다른 단위 그룹이나 지수 간의 `+`, `-` 연산은 잘못된 숫자를 조용히 반환하는 대신
  `IllegalStateException`을 던집니다.
- **자동 변환.** `5.meters() + 3.miles()`는 그대로 동작합니다 - 두 피연산자는 내부적으로 정규화되므로 결합하기 전에
  수동으로 단위를 변환할 필요가 없습니다.
- **자유로운 곱셈과 나눗셈.** 단위의 곱셈/나눗셈은 *항상* 허용되며 결과적인 물리 차원(지수)을 자동으로 추적합니다.
  예: `길이 * 길이`는 면적이 됩니다.
- **모든 `Number` 타입 지원.** `Int`, `Long`, `Float`, `Double` 및 그 밖의 모든 `Number` 타입으로부터 값을
  생성할 수 있습니다. 내부적으로는 항상 `Double`로 정규화됩니다.
- **완전한 SI 접두어 표**, Quetta(Q)부터 Quecto(q)까지, 어떤 단위와도 범용적으로 조합 가능합니다.
- **명명된 특수 단위**(헥타르, 리터, 에이커 등)를 편리한 그룹·지수 종속 입출력 대상으로 제공하며, 기존의 원시 지수
  표현을 대체하지 않습니다.

## 핵심 개념

kunit은 두 가지 핵심 타입을 중심으로 구성됩니다.

- **`KMixedUnitInstance`** - *혼합 단위*(Mischeinheit): `Double` 기본값과 하나 이상의 `KUnit` 조합, 각각 정수
  지수와 짝을 이룹니다 (예: 속도의 `m^1 * s^-1`). 이는 다른 모든 것을 구동하는 범용 엔진입니다.
- **`KUnit`** - 단위 그룹에 속하는 단일 "순수" 단위입니다(예: 미터는 길이 그룹에 속함). 구체적인 단위 그룹은
  `enum class ... : KUnit` (예: `KLengthUnit`)로 모델링됩니다.

모든 단위 그룹은 추가로 **래퍼 클래스**(예: `KLengthUnitInstance`)를 제공하며, 이는 단일 단위 그룹으로 제한된
`KMixedUnitInstance`를 캡슐화하고 항상 해당 그룹의 기본 단위로 정규화됩니다. 대부분의 경우 이 타입을 사용하게 됩니다 - 현재
제공되는 단위는 [미리 정의된 단위](units/length.md)를, 범용 `KMixedUnitInstance` 엔진을 직접 사용해야 하는 경우는
[혼합 단위](mixed-units.md)를 참고하세요.

새로운 물리량(예: 질량이나 시간)에 대한 지원을 추가하고 싶다면, 단계별 안내인
[사용자 정의 단위 추가](custom-units.md)를 참고하세요.

!!! note "단위 객체는 불변입니다"
    모든 단위 값 - `KMixedUnitInstance` 엔진은 물론 `KLengthUnitInstance`나 `KTimeUnitInstance`
    같은 모든 "순수" 래퍼 - 은 **불변(immutable)** 입니다. 어떤 연산도 기존 인스턴스를 변경하지
    않으며, 연산자(`+`, `-`, `*`, `/`)와 변환은 항상 **새로운** 객체를 반환하고 피연산자는 그대로
    둡니다. 따라서 단위 값은 자유롭게 공유하거나 키 또는 상수로 사용해도 안전합니다.

## 빠른 시작

모듈을 의존성으로 추가하거나(또는 프로젝트/소스 세트로 포함) 필요한 단위 그룹의 어휘를 임포트하세요.

### 길이

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

// 모든 Number 타입으로부터 순수 길이 값을 생성
val distance = 5.meters()
val trip = 10.miles()

// 연산자: 동일 그룹·동일 지수 내에서 자동 변환
val total = distance + trip          // KLengthUnitInstance, 미터로 정규화됨
val diff = trip - distance

// 비교
val isFarther = trip > distance      // true

// 특정 단위로 값을 읽기
println(total.valueAs(KUnitPrefix.KILO with meters)) // 예: 21.0467...
println(total.valueAs(yards))                         // 예: 23018.4...

// 순수 단위의 곱셈/나눗셈은 혼합 단위(KMixedUnitInstance)를 생성
val area = distance.toKMixedUnitInstance() * trip.toKMixedUnitInstance()

// 면적(지수 2)과 부피(지수 3)를 위한 특수 단위
val plot = 3.hectares()
println(plot.valueAs(KLengthDerivedUnit.ARE))   // 300.0

val tank = 200.liters()
println(tank.valueAs(KLengthDerivedUnit.US_GALLON))
```

### SI 접두어

```kotlin
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.length.meters
import org.pcsoft.framework.kunit.length.toKLengthUnit

// "5 kilo meters" -> KPrefixBuilder -> KMixedUnitInstance -> KLengthUnitInstance
val fiveKm = (5 kilo meters).toKMixedUnitInstance().toKLengthUnit()
println(fiveKm.value) // 5000.0 (미터로 정규화됨)
```

### 혼합 단위

```kotlin
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

// 혼합 단위를 수동으로 구성, 예: 제곱미터 (길이^1 * 길이^1)
val speed = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val doubled = speed * speed // 지수가 더해짐 -> 길이^2
```

## 체크아웃 & 빌드

```bash
git clone <repository-url>
cd kunit
```

kunit은 Gradle을 사용합니다(래퍼가 저장소에 포함되어 있어 별도의 Gradle 설치가 필요 없습니다):

```bash
# 빌드
./gradlew build          # Windows: gradlew.bat build

# 테스트만 실행
./gradlew test            # Windows: gradlew.bat test
```

Toolchain 25를 해석할 수 있는 JDK가 필요합니다(`foojay-resolver` 플러그인이 필요 시 자동으로 다운로드합니다).
