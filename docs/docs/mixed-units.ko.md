# 혼합 단위

**혼합 단위**는 각각 고유한 지수로 거듭제곱된 여러 `KUnit`로 구성된 값입니다. 예:
속도의 경우 `m^1 * s^-1`, 힘의 경우 `m^1 * kg^1 * s^-2`. kunit에서 이것은 범용 `KMixedUnitInstance` 클래스로
표현됩니다.

그룹별 래퍼 클래스(`KLengthUnitInstance` 등, [사전 정의된 단위](units/kinematics/distance.md) 참조)는 단일 물리 차원을
다루는 데 편리하지만, **서로 다른** 그룹의 단위를 결합해야 하거나 래퍼 클래스가 제공하는 같은 그룹 자동 변환을
원하지 않을 때는 `KMixedUnitInstance`를 사용합니다.

## 구조

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KMixedUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value`는 정규화된 `Double` 크기이며, 항상 `units`에 나열된 바로 그 단위와 지수에 대한 상대값입니다 — 그룹
  래퍼와 달리 `KMixedUnitInstance`는 그룹의 기본 단위로의 정규화를 **하지 않습니다**.
- `units`는 물리 차원을 기술하는 `(KUnit, exponent)` 쌍의 리스트입니다.

모든 "순수" 단위는 이 범용 표현으로 변환하는 `toUnit()` 확장을 노출합니다:

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5 of meters
val mixed = d.toUnit() // KMixedUnitInstance: value=5.0, units=[METER^1]
```

## 곱셈과 나눗셈

`*`와 `/`는 두 `KMixedUnitInstance` 사이에서 **항상** 허용됩니다 — 단위의 곱셈/나눗셈은 항상 물리적으로 의미가
있으므로 차원 제한이 없습니다.

- `*`는 일치하는 단위의 지수를 더하고, 한쪽에만 존재하는 단위는 그대로 이어받습니다.
- `/`는 일치하는 단위에서 우변의 지수를 뺍니다(우변에만 존재하는 단위는 지수를 부호 반전합니다).
- 결과 지수가 `0`이 되면 그 단위는 결과에서 완전히 제거됩니다.

```kotlin
import org.pcsoft.framework.kunit.distance.*

val distance = (10 of meters).toUnit()   // units=[METER^1]
val width = (4 of meters).toUnit()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

두 개의 서로 다른 단위 그룹(예: 길이와 시간)을 섞어도 정확히 같은 방식으로 동작하며 진정한 혼합 단위를
생성합니다:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

val distance = (100 of meters).toUnit()
val time = (10 of seconds).toUnit()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## 순수한 숫자로 스케일링

모든 단위 값은 순수한 `Number`로 스케일링할 수 있습니다. 이는 **크기만** 바꾸는 연산입니다. 값은 바뀌지만 단위 항과 지수는 그대로 유지되므로 결과는 타입과 차원을 유지합니다.

- `unit * n`, `n * unit`, `unit / n`은 모두 **같은 타입의 단위**를 반환합니다(길이는 길이로, 면적은 면적으로 유지).
- `n / unit`은 차원을 **반전**하여(모든 지수의 부호를 반전) 일반 `KMixedUnitInstance`를 반환합니다. 주기로부터 주파수 같은 역수 값을 만드는 관용적인 방법입니다.
- 스칼라 `+`/`-`는 의도적으로 **제공하지 않습니다**. 무차원 숫자를 차원이 있는 값에 더하는 것은 무의미하기 때문입니다.

실제 예시 — 원의 넓이 `A = π · r²`를 전적으로 단위 시스템으로 계산합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.distance.*

val r = 12 of centi.meters       // KLengthUnitInstance, 0.12 m
val area = Math.PI * (r * r)     // KAreaUnitInstance: π·r² ≈ 0.04524 m²
area into (meters * meters)      // ≈ 0.04524 (제곱미터)
```

길이를 스케일링하거나 경로를 균등하게 나누는 것도 같은 방식입니다.

```kotlin
val tripled = (12 of meters) * 3 // KLengthUnitInstance, 36 m
val leg = (10 of kilo.meters) / 4 // KLengthUnitInstance, 2.5 km (경로의 4분의 1)
```

숫자를 단위로 **나누면** 차원이 반전됩니다(예: 주기로부터 주파수).

```kotlin
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.time.seconds

val frequency = 1 / (2 of seconds) // KMixedUnitInstance: value=0.5, units=[SECOND^-1] (0.5 Hz)
```

아핀(affine) **절대 온도** 그룹만은 예외입니다. 절대 온도를 숫자로 스케일링하는 것은 물리적으로 무의미하므로(켈빈 값이 −273.15 오프셋을 포함), `(20 of celsius) * 2`는 **컴파일 오류**입니다. 대신 선형 **온도 차이**를 스케일링하세요([온도 차이](units/thermodynamics/temperature-difference.md) 참조).

## 덧셈과 뺄셈

`*`/`/`와 달리 `+`와 `-`는 **같은 물리 차원**을 기술하는 두 `KMixedUnitInstance` 사이에서만 허용됩니다: 한쪽의
모든 항에 대해, 같은 단위 그룹에 속하고(예: 모두 `KDistanceUnit` 값) 같은 지수를 가진 항이 다른 쪽에 정확히
하나 있어야 합니다(순서 무관). `KUnit` 자체가 동일할 필요는 **없습니다** — 일치하는 항은 그룹별 래퍼 클래스
(`KLengthUnitInstance` 등)가 "순수" 단위에 대해 하는 것과 같은 방식으로 정규화를 통해 자동 변환됩니다. 결과는
좌변 피연산자의 `units`로 표현됩니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.miles

val a = (5 of meters).toUnit()
val b = (3 of meters).toUnit()
(a + b).value // 8.0

val c = (3 of miles).toUnit()
(a + c).value // 4832.032(3마일을 미터로 변환한 후 더함), units=[METER^1]
```

일치하지 않는 단위 그룹이나 일치하지 않는 지수는 여전히 실패합니다:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

a + (3 of seconds).toUnit()       // IllegalStateException 던짐: 시간 항에 일치하는 단위 그룹 없음
a + ((2 of meters) pow 2).toUnit() // IllegalStateException 던짐: 지수 불일치(1 대 2)
```

미리 **정확한** 일치(같은 그룹뿐 아니라 같은 `KUnit`)를 확인하려면 `hasSameUnits`를 사용합니다:

```kotlin
val x = (5 of meters).toUnit()
val y = (3 of meters).toUnit()
x.hasSameUnits(y) // (unit -> exponent) 시그니처를 순서 무관하게 비교
```

## 값 읽기

`into`는 대상 단위 템플릿(맨 토큰, 접두사가 붙은 빌더 템플릿, 또는 특수 값 1 인스턴스)에서 값을 읽어 순수한
`Double`을 반환합니다. 양쪽 모두 같은 물리 차원을 기술해야 합니다. `valueAs`도 사용자 정의 단위 `toString`도
없습니다. 특정 단위는 `"${v into kilo.meters} km"`처럼 형식화합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (1 of seconds)

speed into (kilo.meters / hours)   // 36.0(km/h)

val area = (200 of meters) * (50 of meters)
area into hectares                 // 1.0
```

기본(인자 없는) `toString()`은 항상 각 항 고유의 `KUnit.symbol`을 `*`로 이어붙여 사용합니다. 예:
`"5.0 m*s^-1"`.

## 순수 단위와 혼합 단위 섞기

모든 순수 단위 래퍼 클래스는 `KMixedUnitInstance`에 대해 직접 `*`/`/`를 지원하므로, 이 연산자들을 위해
`toUnit()`을 명시적으로 호출할 필요가 거의 없습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*

val distance = 100 of meters        // KLengthUnitInstance
val mixed = distance.toUnit()       // KMixedUnitInstance

val combined = distance * mixed              // KMixedUnitInstance: METER^2
```

## 순수 단위로 다시 변환하기

`KMixedUnitInstance`가 다시 단일 단위 그룹의 정확히 하나의 항을 나타내게 되면, 그룹별 `toXxxUnit()` 확장(예:
`toDistance()`)을 통해 그 그룹의 래퍼 클래스로 다시 변환할 수 있습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (2 of seconds)    // KSpeedUnitInstance
val distanceAgain = speed.toUnit() * (2 of seconds).toUnit() // units=[METER^1]
distanceAgain.toDistance().value               // 10.0

val area = (200 of meters) * (50 of meters)    // KAreaUnitInstance
area.toUnit().toDistance().value               // 10000.0(면적, 지수 2)
```

`KMixedUnitInstance`가 그 그룹의 정확히 하나의 항으로 구성되어 있지 **않으면**(예: 여전히 혼합된 길이/시간
값이면) 변환은 `IllegalStateException`을 던집니다.

같은 좁히기는 **거리 값에 직접**(`KMixedUnitInstance`뿐 아니라) 사용할 수 있습니다: 일반 `KDistanceUnitInstance`
— 또는 임의의 리프 — 는 `toLength()`, `toArea()`, `toVolume()`으로 특정 차원으로 좁힐 수 있으며, 이들은 지수가
검사되고 불일치 시 `IllegalStateException`을 던집니다:

```kotlin
val area = (200 of meters) * (50 of meters)  // KAreaUnitInstance(지수 2)
area.toArea().value                          // 10000.0
area.toDistance().toArea().value             // 10000.0(넓혔다가 다시 좁힘)
area.toLength()                              // IllegalStateException(지수 2, 1 아님)
```
