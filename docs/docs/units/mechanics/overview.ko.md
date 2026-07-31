# 역학 — 개요

패키지: `org.pcsoft.framework.kunit.mechanic.mass`, `…force`, `…pressure`, `…density`, `…areadensity`, `…power`,
`…energy`

역학 (동역학)은 물체가 **왜** 움직이는지, 그리고 물질이 어떻게 분포하는지를 묻습니다 — 질량, 그에 작용하는 힘, 힘이 면적에 가하는 압력, 그리고 부피나 표면에 얼마나 많은 질량이 채워져 있는지의
상호작용입니다. [운동학](../kinematics/overview.md)의 비율 위에, 이 주제는 1개의 **네이티브** 기본량 (질량)과 질량·길이·시간에서 **구성된** 4개의 양을 더합니다.

## 이 주제의 단위

| 단위   | 유형     | 기준 단위                       | 페이지                    |
|--------|----------|---------------------------------|---------------------------|
| 질량   | 네이티브 | 그램(`g`)                       | [질량](mass.md)           |
| 힘     | 구성     | 뉴턴(`N`)                       | [힘](force.md)            |
| 압력   | 구성     | 파스칼(`Pa`)                    | [압력](pressure.md)       |
| 밀도   | 구성     | 킬로그램 매 세제곱미터(`kg/m³`) | [밀도](density.md)        |
| 면밀도 | 구성     | 킬로그램 매 제곱미터(`kg/m²`)   | [면밀도](areadensity.md)  |
| 전력   | 구성     | 와트(`W`)                       | [전력(역학)](power.md)    |
| 에너지 | 구성     | 줄(`J`)                         | [에너지(역학)](energy.md) |

전력과 에너지는 각각 기술적으로 **하나**의 양이며, 다른 주제 분야와 공유됩니다. 이들은 분야별로 문서화되며 서로 참조합니다 ([전력 (전기)](../electrical/power.md),
[전력 (열역학)](../thermodynamics/power.md), [에너지 (전기)](../electrical/energy.md),
[에너지 (열역학)](../thermodynamics/energy.md)).

## 양들의 관계

| 식                    | 결과       | 공식          |
|-----------------------|------------|---------------|
| `mass * acceleration` | 힘         | `F = m · a`   |
| `force / area`        | 압력       | `p = F / A`   |
| `pressure * area`     | 힘         | `F = p · A`   |
| `mass / volume`       | 밀도       | `ρ = m / V`   |
| `density * length`    | 면밀도     | `ρ_A = ρ · d` |
| `force * speed`       | 전력       | `P = F · v`   |
| `power / speed`       | 힘         | `F = P / v`   |
| `power / force`       | 속도       | `v = P / F`   |
| `force * length`      | 에너지(일) | `W = F · s`   |
| `power * time`        | 에너지     | `W = P · t`   |
| `energy / time`       | 전력       | `P = W / t`   |
| `energy / power`      | 시간       | `t = W / P`   |

## 실전 예제 — 뉴턴의 제2법칙과 접지 압력

**2 kg** 블록을 표준 중력으로 가속하고, 그 결과 생기는 무게 힘을 **0.5 m²** 접지면에 분산시킵니다. 힘은 `F = m · a`, 압력은 `p = F / A`입니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.acceleration.*
import org.pcsoft.framework.kunit.mechanic.force.*
import org.pcsoft.framework.kunit.mechanic.pressure.*

val f = (2 of kilo.grams) * (1 of standardGravities)  // KForceUnitInstance
f into newtons                                         // ≈ 19.61 (N)

val area = (1 of meters) * (0.5 of meters)             // KAreaUnitInstance, 0.5 m²
val p = f / area                                       // KPressureUnitInstance
p into pascals                                         // ≈ 39.23 (Pa)
```

## 실전 예제 — 밀도로부터 강철 부품의 질량

강철의 밀도는 **7850 kg/m³**입니다. **2 L** 부품의 질량은 `m = ρ · V`입니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
val mass = steel * (2 of liters)                          // KMassUnitInstance
mass into kilo.grams                                      // 15.7 (2 L당 kg)
```

## 실전 예제 — 윈치의 일과 전력

윈치가 **100 N**의 힘으로 **5 m**를 **5 s** 동안 끌어당깁니다. 일은 `W = F · s`, 전력은 `P = W / t`이며 — 이는 직접적인 역학적 형태 `P = F · v`와 같습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.common.energy.*

val w = (100 of newtons) * (5 of meters)                    // KEnergyUnitInstance
w into joules                                                // 500.0

val p = w / (5 of seconds)                                   // KPowerUnitInstance
p into watts                                                 // 100.0

val direct = (100 of newtons) * ((1 of meters) / (1 of seconds)) // P = F · v, 100 W
p == direct                                                  // true
```

## 값 출력 (`toString`)

`toString()`은 값을 해당 그룹의 **기준 단위**(값 + 기호)로 출력합니다. 다른 단위는 문자열 템플릿 안에서
`into`로 읽고 기호를 직접 붙이세요:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.force.*

val f = 10 of newtons
f.toString()                 // "10.0 N" (기준 단위)
"${f into kilo.newtons} kN"  // "0.01 kN"
```

## 표기법

아래 표는 이 분야의 핵심 관계를 수학 표기와 KUnit의 Kotlin 표기로 대비합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`), `·`는 곱셈, `/`는 분수를 나타냅니다.

| 수학        | Kotlin                                                | 의미               |
|-------------|-------------------------------------------------------|--------------------|
| `F = m · a` | `(2 of kilo.grams) * (1 of standardGravities)`        | 질량×가속도에서 힘 |
| `p = F / A` | `f / area`                                            | 힘÷면적에서 압력   |
| `F = p · A` | `p * area`                                            | 압력×면적에서 힘   |
| `ρ = m / V` | `(6 of kilo.grams) / (2 of liters)`                   | 질량÷부피에서 밀도 |
| `m = ρ · V` | `steel * (2 of liters)`                               | 밀도×부피에서 질량 |
| `W = F · s` | `(100 of newtons) * (5 of meters)`                    | 힘×길이에서 일     |
| `P = F · v` | `(100 of newtons) * ((1 of meters) / (1 of seconds))` | 힘×속도에서 전력   |
| `P = W / t` | `w / (5 of seconds)`                                  | 일÷시간에서 전력   |

## 다음에 볼 것

* [질량](mass.md) — 네이티브 기본량 (그램 정규화).
* [힘](force.md) 및 [압력](pressure.md) — 뉴턴의 법칙과 면적당 힘.
* [밀도](density.md) 및 [면밀도](areadensity.md) — 부피당·표면당 질량.
* [전력 (역학)](power.md) — 와트, `F · v`, 그리고 마력 단위.
* [에너지 (역학)](energy.md) — 역학적 일 `F · s`로서의 줄.
