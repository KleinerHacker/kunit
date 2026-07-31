# 입체각

패키지: `org.pcsoft.framework.kunit.mechanic.solidangle`
기본 단위: **스테라디안**(`KSolidAngleUnit.BASE == KSolidAngleUnit.STERADIAN`)

유형: **구성된 단위**

입체각은 2차원 각도입니다: 원뿔이 잘라내는 구 표면의 비율입니다. 이는 **구성된** 단위입니다 —
`1 sr = 1 rad²` — 하지만 스테라디안은 독립적으로 이름 붙은 SI 단위이며 자체 어휘 (제곱도, 스팟)를 가지고 있으므로, 단일 항 래퍼를 가진 별도의 그룹으로 모델링됩니다.

`KSolidAngleUnitInstance` 는 지수 1의 단일 `KSolidAngleUnit.BASE` 항을 감싼 `KMixedUnitInstance` 를 감싸며, 항상 스테라디안으로
정규화됩니다. [각도](angle.md) 그룹과의 다리는 타입이 지정된 연산자
`angle * angle` 와 네이티브 `rad²` 형식도 받아들이는 형식 인식 훅 `toSolidAngle()` 입니다.

## 이름이 붙은 단위

| 단위          | 기호   |            토큰 |                sr로 1 |
|---------------|--------|----------------:|----------------------:|
| 스테라디안    | `sr`   |    `steradians` |                   1.0 |
| 제곱도        | `deg²` | `squareDegrees` | (π/180)² ≈ 3.04617e-4 |
| 스팟(완전 구) | `sp`   |         `spats` |          4π ≈ 12.5664 |

모든 단위는 전체 SI 접두사 범위를 지원합니다 (`milli.steradians`, `micro.steradians`).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val full = 1 of spats
full into steradians    // ≈ 12.566
full into squareDegrees // ≈ 41252.96(전체 하늘)
```

## 분해

입체각은 두 가지 동등한 방법으로 도달할 수 있습니다. 둘 다 동일한 정규 값으로 축소됩니다.

| 형식                 | Kotlin                                  | 결과 타입                 |
|----------------------|-----------------------------------------|---------------------------|
| 타입이 지정된 연산자 | `angle * angle`                         | `KSolidAngleUnitInstance` |
| 네이티브 표현식      | `(angle.toUnit() pow 2).toSolidAngle()` | `KSolidAngleUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val typed = (90 of degrees) * (90 of degrees)
val native = ((90 of degrees).toUnit() pow 2).toSolidAngle()

typed == native            // true - 둘 다 (π/2)² sr ≈ 2.4674 sr
typed into steradians      // ≈ 2.4674
```

## 평면각으로 계산하기

| 식                        | 결과 타입                 | 의미            |
|---------------------------|---------------------------|-----------------|
| `angle * angle`           | `KSolidAngleUnitInstance` | 입체각 `Ω = φ²` |
| `solidangle / angle`      | `KAngleUnitInstance`      | 나머지 평면각   |
| `solidangle + solidangle` | `KSolidAngleUnitInstance` | 동일 타입 연산  |

## 실전 예제: LED 빔 각도

LED가 30° × 30°의 정사각형 빔으로 발광합니다. 이 LED는 어떤 입체각을 비추며, 전체 구에 대한 비율은 얼마입니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val beam = (30 of degrees) * (30 of degrees)
beam into steradians    // ≈ 0.2742
beam into squareDegrees // 900.0
beam into spats         // ≈ 0.0218(구 표면적의 약 2.2 %)
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val sum = (3 of steradians) + (1 of steradians) // 4 sr
(1 of spats) > (10 of steradians)               // true
(3 of steradians) * (2 of steradians)           // KMixedUnitInstance(그룹에서 탈출)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

(2 of steradians).toString()               // "2.0 sr"(기본 단위)
"${(1 of spats) into squareDegrees} deg²"  // "41252.96... deg²"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학          | Kotlin                                    | 의미                                        |
|---------------|-------------------------------------------|---------------------------------------------|
| `sr`          | `steradians`                              | 입체각, 기본 단위                           |
| `deg²`        | `squareDegrees`                           | 제곱도                                      |
| `rad²`        | `(radians.toUnit() pow 2).toSolidAngle()` | 평면각의 제곱으로서의 입체각(네이티브 형식) |
| `Ω = φ₁ · φ₂` | `angle * angle`                           | 타입이 지정된 분해                          |
| `φ = Ω / φ₁`  | `solidangle / angle`                      | 평면각에 대해 푼 형식                       |
| `msr`         | `milli.steradians`                        | 접두사가 붙은 입체각                        |
