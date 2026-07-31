# 표면장력

패키지: `org.pcsoft.framework.kunit.mechanic.lineforce`
기본 단위: **뉴턴 매 미터**(`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

유형: **구성된 단위**

표면장력 `σ` 는 단위 새 표면을 만드는 데 필요한 에너지이며, 이는 접촉선을 따라 단위 길이당 작용하는 힘과 동등합니다: `1 J/m² = 1 N/m`. 차원은 `mass · time⁻²` 입니다.

이는 [강성](stiffness.md)이 공유하는 **단위 길이당 힘**의 차원과 정확히 같습니다. 따라서 KUnit 은 두 읽기 모두를 위해 하나의 중립적인 그룹 `lineforce` 를 모델링하며, 표면장력은 그
중 하나입니다. 이 페이지는 그 읽기를 문서화합니다.

!!! note "하나의 그룹, 두 개의 읽기"
`KLineForceUnitInstance` 는 공유 타입입니다. 표면장력과 스프링 상수를 구분하는 것은 이름을 붙이는 방법뿐입니다 — 이 그룹은 어느 쪽 읽기도 다른 쪽의 이름을 차지하지 않도록 중립적으로 이름
붙여져 있습니다.

## 이름이 붙은 단위

| 단위             | 기호     |                   토큰 | N/m 로 1 단위 |
|------------------|----------|-----------------------:|--------------:|
| 뉴턴 매 미터     | `N/m`    |      `newtonsPerMeter` |           1.0 |
| 다인 매 센티미터 | `dyn/cm` |   `dynesPerCentimeter` |          1e-3 |
| 뉴턴 매 밀리미터 | `N/mm`   | `newtonsPerMillimeter` |        1000.0 |
| 파운드힘 매 인치 | `lbf/in` |   `poundsForcePerInch` |     ≈ 175.127 |
| 킬로폰드 매 미터 | `kp/m`   |    `kilopondsPerMeter` |       9.80665 |

표면장력은 보통 mN/m 또는 수치적으로 동일한 dyn/cm 으로 표기됩니다: 25 °C 의 물은 ≈ 72 mN/m = 72 dyn/cm 입니다. 밀리뉴턴 매 미터는 접두사 형식
`milli.newtonsPerMeter` 입니다.

## 분해

| 형식          | Kotlin                                                  | 결과 타입                |
|---------------|---------------------------------------------------------|--------------------------|
| 에너지 / 면적 | `energy / area`                                         | `KLineForceUnitInstance` |
| 힘 / 길이     | `force / length`                                        | `KLineForceUnitInstance` |
| 네이티브 식   | `(mass.toUnit() / (time.toUnit() pow 2)).toLineForce()` | `KLineForceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val viaEnergy = (2 of joules) / ((1 of meters) * (1 of meters))
val viaForce = (2 of newtons) / (1 of meters)

viaEnergy == viaForce                  // true - 둘 다 2 N/m
(72 of milli.joules) / ((1 of meters) * (1 of meters)) into dynesPerCentimeter // 72.0
```

## 핵심 단위로 계산하기

| 식                                         | 결과 타입                | 의미                   |
|--------------------------------------------|--------------------------|------------------------|
| `energy / area`                            | `KLineForceUnitInstance` | `σ = W / A`            |
| `lineforce * area`, `area * lineforce`     | `KEnergyUnitInstance`    | 표면에너지 `W = σ · A` |
| `energy / lineforce`                       | `KAreaUnitInstance`      | `A = W / σ`            |
| `force / length`                           | `KLineForceUnitInstance` | `σ = F / l`            |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | `F = σ · l`            |

## 실전 예제: 비눗방울 막을 만드는 에너지

0.05 m² 의 비눗방울 막을 붑니다 (두 표면, 각 표면 σ ≈ 25 mN/m). 이는 얼마의 에너지가 필요하며, 그 막은 10 cm 철사에 어떤 힘을 가합니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sigma = 25 of milli.newtonsPerMeter
val area = (0.5 of meters) * (0.1 of meters)   // 0.05 m²

val energy = sigma * area                       // KEnergyUnitInstance
energy into milli.joules                        // 1.25

val force = sigma * (10 of centi.meters)        // KForceUnitInstance
force into milli.newtons                        // 2.5
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sum = (72 of dynesPerCentimeter) + (8 of dynesPerCentimeter) // 80 dyn/cm
(72 of dynesPerCentimeter) > (50 of milli.newtonsPerMeter)       // true
(1 of dynesPerCentimeter) == (1 of milli.newtonsPerMeter)        // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(72 of dynesPerCentimeter).toString()                     // "0.072 N/m"(기본 단위)
"${(72 of dynesPerCentimeter) into dynesPerCentimeter} dyn/cm" // "72.0 dyn/cm"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                          | 의미                   |
|-------------|---------------------------------|------------------------|
| `N/m`       | `newtonsPerMeter`               | 표면장력, 기본 단위    |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | 기저 차원의 같은 양    |
| `mN/m`      | `milli.newtonsPerMeter`         | 일상적인 표면장력 읽기 |
| `dyn/cm`    | `dynesPerCentimeter`            | CGS 읽기(= 1 mN/m)     |
| `σ = W / A` | `energy / area`                 | 분해 A                 |
| `σ = F / l` | `force / length`                | 분해 B                 |
| `W = σ · A` | `lineforce * area`              | 표면에너지             |
