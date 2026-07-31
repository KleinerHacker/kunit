# 기계적 응력 & 탄성계수

패키지: `org.pcsoft.framework.kunit.mechanic.pressure`
기본 단위: **파스칼**(`KPressureUnit.BASE == KPressureUnit.PASCAL`)

유형: **구성된 단위**

기계적 응력 `σ = F / A` 와 탄성 (영률) 계수 `E = σ / ε` 는 정확히
[압력](pressure.md)의 차원을 가집니다: `mass · length⁻¹ · time⁻²`. 따라서 KUnit 은 이를 위한 단위 그룹을 도입하지 **않습니다** — 둘 다 압력 그룹의 **읽기**이며, 그
접두사 별칭을 통해 표현됩니다. 이 페이지는 그 읽기를 문서화하며, 그룹 자체는 [압력](pressure.md) 페이지에 설명되어 있습니다.

!!! note "MPa, N/mm² 그리고 GPa 는 접두사 별칭"
정역학 단위는 전용 토큰이 **아닙니다**, 왜냐하면 정확히 도달할 수 있기 때문입니다:
**MPa = N/mm² = `mega.pascals`** 이고 **GPa = `giga.pascals`** 입니다.
`(1 of newtons) / ((1 of milli.meters) * (1 of milli.meters))` 는 `1 of mega.pascals` 와 정확히 같은 값을 산출합니다.

## 읽기 표

| 읽기               | 기호   | Kotlin         | Pa 로 1 단위 |
|--------------------|--------|----------------|-------------:|
| 파스칼             | `Pa`   | `pascals`      |          1.0 |
| 킬로파스칼         | `kPa`  | `kilo.pascals` |          1e3 |
| 메가파스칼 = N/mm² | `MPa`  | `mega.pascals` |          1e6 |
| 기가파스칼(계수)   | `GPa`  | `giga.pascals` |          1e9 |
| 면적당 힘          | `N/m²` | `force / area` |          1.0 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*

val fromExpression = (1 of newtons) / ((1 of milli.meters) * (1 of milli.meters))
fromExpression into mega.pascals // 1.0 (N/mm² 는 메가파스칼입니다)
```

## 훅의 법칙

[변형률](strain.md) 그룹과 함께, 압력 그룹은 훅의 법칙 양변을 모두 지원합니다:

| 식                                       | 결과 타입               | 의미                 |
|------------------------------------------|-------------------------|----------------------|
| `force / area`                           | `KPressureUnitInstance` | 응력 `σ = F / A`     |
| `stress / strain`                        | `KPressureUnitInstance` | 탄성계수 `E = σ / ε` |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | 응력 `σ = E · ε`     |
| `pressure * area`                        | `KForceUnitInstance`    | 작용력 `F = σ · A`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.perMille
import org.pcsoft.framework.kunit.mechanic.strain.div
import org.pcsoft.framework.kunit.mechanic.strain.times

val modulus = (210 of mega.pascals) / (1 of perMille) // E = σ / ε
modulus into giga.pascals                              // 210.0 (강철)

val stress = (210 of giga.pascals) * (2 of perMille)   // σ = E · ε
stress into mega.pascals                                // 420.0
```

## 실전 예제: 하중을 받는 인장 로드

직경 20 mm 강철 인장 로드 (A ≈ 314 mm²)가 60 kN 을 받습니다. 응력은 얼마이며, S235 강철의 항복강도 235 MPa 미만입니까, 그리고 3 m 로드는 얼마나 늘어납니까 (E = 210
GPa)?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.ratio
import org.pcsoft.framework.kunit.times

val area = (10 of milli.meters) * (10 of milli.meters) * Math.PI // ≈ 314 mm²
val stress = (60 of kilo.newtons) / area
stress into mega.pascals                     // ≈ 191.0
stress < (235 of mega.pascals)                // true - 항복강도 이내

val strainRatio = (stress into giga.pascals) / 210.0 // ε = σ / E, 순수 비율로
val elongation = (3 of meters) * strainRatio
elongation into milli.meters                          // ≈ 2.73
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

val sum = (100 of mega.pascals) + (50 of mega.pascals) // 150 MPa
(1 of giga.pascals) > (999 of mega.pascals)            // true
(1000 of mega.pascals) == (1 of giga.pascals)          // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

(210 of mega.pascals).toString()                    // "2.1E8 Pa"(그룹 기본 단위)
"${(210 of mega.pascals) into mega.pascals} MPa"    // "210.0 MPa"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학         | Kotlin                                            | 의미                           |
|--------------|---------------------------------------------------|--------------------------------|
| `MPa`        | `mega.pascals`                                    | 응력 읽기(= N/mm²)             |
| `N/mm²`      | `newtons / (milli.meters pow 2)`                  | 면적당 힘으로 표현한 같은 읽기 |
| `GPa`        | `giga.pascals`                                    | 탄성계수 읽기                  |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | 기저 차원의 같은 양            |
| `σ = F / A`  | `force / area`                                    | 힘과 면적으로부터의 응력       |
| `E = σ / ε`  | `stress / strain`                                 | 훅의 법칙, 계수에 대해 정리    |
| `σ = E · ε`  | `pressure * strain`                               | 훅의 법칙, 응력에 대해 정리    |
