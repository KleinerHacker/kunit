# 혼합 단위

**혼합 단위**(독일어: *Mischeinheit*)는 여러 개의 `KUnit`이 각각 자신의 지수와 함께 결합된 값입니다. 예:
속도의 `m^1 * s^-1`, 힘의 `m^1 * kg^1 * s^-2`. kunit에서는 범용 `KUnitInstance` 클래스가 이를 표현합니다.

그룹별 래퍼 클래스(`KLengthUnitInstance` 등, [미리 정의된 단위](units/length.md) 참고)는 단일 물리 차원을
다룰 때 편리하지만, **서로 다른** 그룹의 단위를 결합해야 하거나 래퍼 클래스가 제공하는 동일 그룹 내 자동 변환을
원하지 않을 때는 `KUnitInstance`를 직접 사용해야 합니다.

## 구조

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value`는 정규화된 `Double` 크기이며, 항상 `units`에 나열된 단위와 지수에 상대적입니다 - 그룹 래퍼와 달리
  `KUnitInstance`는 그룹의 기본 단위로의 정규화를 **수행하지 않습니다**.
- `units`는 물리 차원을 설명하는 `(KUnit, 지수)` 쌍의 목록입니다.

모든 "순수" 단위는 이 범용 표현으로 변환하기 위한 `toKUnitInstance()` 확장 함수를 제공합니다.

```kotlin
import org.pcsoft.framework.kunit.length.*

val d = 5.meters()
val mixed = d.toKUnitInstance() // KUnitInstance: value=5.0, units=[METER^1]
```

!!! note
    아래 예시 중 `seconds()`/`TimeUnit`을 참조하는 부분은 "시간" 단위 그룹이 길이와 결합될 때의 모습을
    보여주기 위한 것입니다 - kunit은 현재 `length` 그룹만 제공합니다([미리 정의된 단위](units/length.md)
    참고). 직접 추가하려면 [사용자 정의 단위 추가](custom-units.md)를 따르세요.

## 곱셈과 나눗셈

두 `KUnitInstance` 사이의 `*`와 `/`는 **항상** 허용됩니다 - 단위의 곱셈/나눗셈은 항상 물리적으로 의미가
있으므로 차원 제약이 없습니다.

- `*`는 일치하는 단위의 지수를 더하고, 한쪽에만 존재하는 단위는 그대로 결과에 포함시킵니다.
- `/`는 일치하는 단위에 대해 오른쪽 피연산자의 지수를 뺍니다(오른쪽에만 존재하는 단위는 지수의 부호가
  반전됩니다).
- 결과 지수가 `0`이 되면 해당 단위는 결과에서 완전히 제거됩니다.

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 10.meters().toKUnitInstance()   // units=[METER^1]
val width = 4.meters().toKUnitInstance()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

서로 다른 두 단위 그룹(예: 길이와, 추가된다면 시간)을 혼합하는 것도 동일하게 동작하며, 진정한 혼합 단위를
생성합니다.

```kotlin
// "시간" 단위 그룹이 존재한다고 가정 (사용자 정의 단위 추가 문서의 패턴을 따름):
val distance = 100.meters().toKUnitInstance()
val time = 10.seconds().toKUnitInstance()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## 덧셈과 뺄셈

`*`/`/`와 달리, `+`와 `-`는 두 `KUnitInstance`가 **정확히** 동일한 `units`를 가질 때만 허용됩니다 - 동일한
`KUnit`이 동일한 지수를 가지고 있어야 합니다(순서는 무관). `KUnitInstance`는 어떤 단위가 같은 그룹에
속하는지 알지 못하기 때문에 여기서는 서로 다른 단위 간 자동 변환이 **없습니다** - 이것이 바로 그룹별 래퍼
클래스(`KLengthUnitInstance` 등)가 추가로 제공하는 기능입니다.

```kotlin
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
(a + b).value // 8.0

val c = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))
a + c // IllegalStateException 발생: METER != MILE, 둘 다 "길이"임에도 불구하고
```

`hasSameUnits`를 사용해 사전에 호환성을 확인할 수 있습니다.

```kotlin
val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KLengthUnit.METER, 0)))
a.hasSameUnits(b) // (단위 -> 지수) 시그니처를 순서와 무관하게 비교
```

## 값 읽기와 포맷팅

`valueAs`는 값을 임의의 목표 단위 집합으로 변환합니다 - 각 목표는 단위 그룹별로(파생 단위의 경우 지수까지)
정확히 하나의 항과 일치해야 합니다. `toString` 오버로드는 동일하게 동작하지만 기호까지 함께 렌더링합니다.

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters().toKUnitInstance() / 1.seconds().toKUnitInstance()

speed.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // 36.0 (km/h)
speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // "36.0 km*h^-1"

val area = 200.meters().toKUnitInstance() * 50.meters().toKUnitInstance()
area.valueAs(KLengthDerivedUnit.HECTARE) // 1.0
```

인자가 없는 기본 `toString()`은 항상 각 항의 고유 `KUnit.symbol`을 사용하며 `*`로 연결합니다. 예:
`"5.0 m*s^-1"`.

## 순수 단위와 혼합 단위의 혼용

모든 순수 단위 래퍼 클래스는 `KUnitInstance`에 대해 `*`/`/`를 직접 지원하므로, 이 연산자들을 위해
`toKUnitInstance()`를 명시적으로 호출할 필요는 거의 없습니다.

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 100.meters()                 // KLengthUnitInstance
val mixed = distance.toKUnitInstance()       // KUnitInstance

val combined = distance * mixed              // KUnitInstance: METER^2
```

## 다시 순수 단위로 변환

`KUnitInstance`가 다시 단일 단위 그룹의 항 하나만으로 구성되면, 그룹별 `toXxxUnit()` 확장 함수(예:
`toKLengthUnit()`)를 통해 해당 그룹의 래퍼 클래스로 다시 변환할 수 있습니다.

```kotlin
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters() / 2.seconds()          // KUnitInstance (시간 그룹이 존재한다고 가정)
val distanceAgain = speed.toKUnitInstance() * 2.seconds() // units=[METER^1]
distanceAgain.toKLengthUnit().value             // 10.0

val area = 200.meters() * 50.meters()           // units=[METER^2]
area.toKLengthUnit().value                        // 10000.0 (면적, 지수 2)
```

`KUnitInstance`가 해당 그룹의 항 하나로만 구성되어 있지 **않은** 경우(예: 여전히 혼합된 길이/시간 값인 경우)
변환은 `IllegalStateException`을 던집니다.
