# 혼합 단위

**혼합 단위**(독일어: *Mischeinheit*)는 여러 개의 `KUnit`이 각각 자신의 지수와 함께 결합된 값입니다. 예:
속도의 `m^1 * s^-1`, 힘의 `m^1 * kg^1 * s^-2`. kunit에서는 범용 `KMixedUnitInstance` 클래스가 이를 표현합니다.

그룹별 래퍼 클래스(`KLengthUnitInstance` 등, [미리 정의된 단위](units/length.md) 참고)는 단일 물리 차원을
다룰 때 편리하지만, **서로 다른** 그룹의 단위를 결합해야 하거나 래퍼 클래스가 제공하는 동일 그룹 내 자동 변환을
원하지 않을 때는 `KMixedUnitInstance`를 직접 사용해야 합니다.

## 구조

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KMixedUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value`는 정규화된 `Double` 크기이며, 항상 `units`에 나열된 단위와 지수에 상대적입니다 - 그룹 래퍼와 달리
  `KMixedUnitInstance`는 그룹의 기본 단위로의 정규화를 **수행하지 않습니다**.
- `units`는 물리 차원을 설명하는 `(KUnit, 지수)` 쌍의 목록입니다.

모든 "순수" 단위는 이 범용 표현으로 변환하기 위한 `toKMixedUnitInstance()` 확장 함수를 제공합니다.

```kotlin
import org.pcsoft.framework.kunit.length.*

val d = 5.meters()
val mixed = d.toKMixedUnitInstance() // KMixedUnitInstance: value=5.0, units=[METER^1]
```

!!! note
    아래 예시 중 `seconds()`/`TimeUnit`을 참조하는 부분은 "시간" 단위 그룹이 길이와 결합될 때의 모습을
    보여주기 위한 것입니다 - kunit은 현재 `length` 그룹만 제공합니다([미리 정의된 단위](units/length.md)
    참고). 직접 추가하려면 [사용자 정의 단위 추가](custom-units.md)를 따르세요.

## 곱셈과 나눗셈

두 `KMixedUnitInstance` 사이의 `*`와 `/`는 **항상** 허용됩니다 - 단위의 곱셈/나눗셈은 항상 물리적으로 의미가
있으므로 차원 제약이 없습니다.

- `*`는 일치하는 단위의 지수를 더하고, 한쪽에만 존재하는 단위는 그대로 결과에 포함시킵니다.
- `/`는 일치하는 단위에 대해 오른쪽 피연산자의 지수를 뺍니다(오른쪽에만 존재하는 단위는 지수의 부호가
  반전됩니다).
- 결과 지수가 `0`이 되면 해당 단위는 결과에서 완전히 제거됩니다.

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 10.meters().toKMixedUnitInstance()   // units=[METER^1]
val width = 4.meters().toKMixedUnitInstance()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

서로 다른 두 단위 그룹(예: 길이와, 추가된다면 시간)을 혼합하는 것도 동일하게 동작하며, 진정한 혼합 단위를
생성합니다.

```kotlin
// "시간" 단위 그룹이 존재한다고 가정 (사용자 정의 단위 추가 문서의 패턴을 따름):
val distance = 100.meters().toKMixedUnitInstance()
val time = 10.seconds().toKMixedUnitInstance()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## 덧셈과 뺄셈

`*`/`/`와 달리, `+`와 `-`는 두 `KMixedUnitInstance`가 **동일한 물리적 차원**을 나타낼 때만 허용됩니다 - 한쪽의
각 항에 대해, 다른 쪽에 동일한 단위 그룹(예: 모든 `KLengthUnit` 값)이면서 동일한 지수를 가진 항이 정확히
하나 존재해야 합니다(순서는 무관). `KUnit` 자체가 완전히 동일할 필요는 **없습니다** - 일치하는 항은
자동으로 정규화되어 변환됩니다. 이는 그룹별 래퍼 클래스(`KLengthUnitInstance` 등)가 "pure" 단위에 대해
수행하는 것과 동일한 방식입니다. 결과는 왼쪽 피연산자의 `units`로 표현됩니다.

```kotlin
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
(a + b).value // 8.0

val c = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))
(a + c).value // 4832.032 (3마일을 미터로 변환한 뒤 더함), units=[METER^1]
```

단위 그룹이나 지수가 일치하지 않으면 여전히 실패합니다.

```kotlin
val time = KMixedUnitInstance(3.0, listOf(KUnitTerm(TimeUnit.SECOND, 1)))
a + time // IllegalStateException 발생: TimeUnit.SECOND에 대응하는 단위 그룹이 없음

val area = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))
a + area // IllegalStateException 발생: 지수 불일치 (1 vs 2)
```

`hasSameUnits`를 사용하면 (그룹이 아닌) **완전히 동일한지** 사전에 확인할 수 있습니다.

```kotlin
val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KLengthUnit.METER, 0)))
a.hasSameUnits(b) // (단위 -> 지수) 시그니처를 순서와 무관하게 비교
```

## 값 읽기와 포맷팅

`valueAs`는 값을 임의의 목표 단위 집합으로 변환합니다 - 각 목표는 단위 그룹별로(파생 단위의 경우 지수까지)
정확히 하나의 항과 일치해야 합니다. `toString` 오버로드는 동일하게 동작하지만 기호까지 함께 렌더링합니다.

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters().toKMixedUnitInstance() / 1.seconds().toKMixedUnitInstance()

speed.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // 36.0 (km/h)
speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // "36.0 km*h^-1"

val area = 200.meters().toKMixedUnitInstance() * 50.meters().toKMixedUnitInstance()
area.valueAs(KLengthDerivedUnit.HECTARE) // 1.0
```

인자가 없는 기본 `toString()`은 항상 각 항의 고유 `KUnit.symbol`을 사용하며 `*`로 연결합니다. 예:
`"5.0 m*s^-1"`.

## 순수 단위와 혼합 단위의 혼용

모든 순수 단위 래퍼 클래스는 `KMixedUnitInstance`에 대해 `*`/`/`를 직접 지원하므로, 이 연산자들을 위해
`toKMixedUnitInstance()`를 명시적으로 호출할 필요는 거의 없습니다.

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 100.meters()                 // KLengthUnitInstance
val mixed = distance.toKMixedUnitInstance()       // KMixedUnitInstance

val combined = distance * mixed              // KMixedUnitInstance: METER^2
```

## 다시 순수 단위로 변환

`KMixedUnitInstance`가 다시 단일 단위 그룹의 항 하나만으로 구성되면, 그룹별 `toXxxUnit()` 확장 함수(예:
`toKLengthUnit()`)를 통해 해당 그룹의 래퍼 클래스로 다시 변환할 수 있습니다.

```kotlin
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters() / 2.seconds()          // KMixedUnitInstance (시간 그룹이 존재한다고 가정)
val distanceAgain = speed.toKMixedUnitInstance() * 2.seconds() // units=[METER^1]
distanceAgain.toKLengthUnit().value             // 10.0

val area = 200.meters() * 50.meters()           // units=[METER^2]
area.toKLengthUnit().value                        // 10000.0 (면적, 지수 2)
```

`KMixedUnitInstance`가 해당 그룹의 항 하나로만 구성되어 있지 **않은** 경우(예: 여전히 혼합된 길이/시간 값인 경우)
변환은 `IllegalStateException`을 던집니다.
