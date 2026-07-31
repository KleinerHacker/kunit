# 선밀도

패키지: `org.pcsoft.framework.kunit.mechanic.lineardensity`
기본 단위: **킬로그램 매 미터**
(`KLinearDensityUnit.BASE == KLinearDensityUnit.KILOGRAMS_PER_METER`)

유형: **구성된 단위**

선밀도는 단위 길이당 질량입니다 — [면밀도](areadensity.md)(`kg/m²`)와
[밀도](density.md)(`kg/m³`)의 1차원 형제입니다. 이는 **구성된** 단위입니다 —
`mass · length⁻¹`(`kg/m`)의 합성입니다.

`KLinearDensityUnitInstance` 는 정확히 두 항으로 된 표준 정규화 형태의 `KMixedUnitInstance` 를 감쌉니다: `KMassUnit.BASE`(그램)가 `+1`,
`KDistanceUnit.BASE`(미터)가 `-1` 입니다. 이 라이브러리의 질량 성분은 그램으로 정규화되어 있으므로, 저장된 값은 원시 그램 기반 성분 값이며 kg/m 읽기는 고정 계수로 나눕니다.

## 이름이 붙은 단위

| 단위             | 기호    |                 토큰 | kg/m 로 1 단위 |
|------------------|---------|---------------------:|---------------:|
| 킬로그램 매 미터 | `kg/m`  |  `kilogramsPerMeter` |            1.0 |
| 그램 매 미터     | `g/m`   |      `gramsPerMeter` |           1e-3 |
| 그램 매 센티미터 | `g/cm`  | `gramsPerCentimeter` |            0.1 |
| 텍스(섬유)       | `tex`   |                `tex` |           1e-6 |
| 데니어(섬유)     | `den`   |             `denier` |    ≈ 1.1111e-7 |
| 파운드 매 피트   | `lb/ft` |      `poundsPerFoot` |      ≈ 1.48816 |

모든 단위는 전체 SI 접두사 범위를 지원합니다; 섬유용 데시텍스는 `deci.tex` 입니다.

## 핵심 단위로 계산하기

| 식                                                 | 결과 타입                    | 의미          |
|----------------------------------------------------|------------------------------|---------------|
| `mass / length`                                    | `KLinearDensityUnitInstance` | `ρ_l = m / l` |
| `lineardensity * length`, `length * lineardensity` | `KMassUnitInstance`          | `m = ρ_l · l` |
| `mass / lineardensity`                             | `KLengthUnitInstance`        | `l = m / ρ_l` |

네이티브 형식도 사용할 수 있습니다: 제네릭 엔진으로 만든 그램 매 미터 식은 모두
`toLinearDensity()` 로 변환됩니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) / (4 of meters)
val native = ((2000 of grams).toUnit() / (4 of meters).toUnit()).toLinearDensity()

typed == native                 // true - 둘 다 0.5 kg/m
typed into gramsPerMeter        // 500.0
```

## 실전 예제: 드럼 위의 강철 케이블

강철 케이블의 무게는 2.6 kg/m 입니다. 길이 45 m 짜리 케이블의 질량은 얼마이며, 500 kg 적재 한계는 케이블을 얼마나 허용합니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val cable = 2.6 of kilogramsPerMeter
val mass = cable * (45 of meters)     // KMassUnitInstance
mass into kilo.grams                  // 117.0

val maxLength = (500 of kilo.grams) / cable // KLengthUnitInstance
maxLength into meters                        // ≈ 192.31
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val sum = (10 of kilogramsPerMeter) + (4 of kilogramsPerMeter) // 14 kg/m
(1 of kilogramsPerMeter) > (1 of gramsPerMeter)                // true
(1 of kilogramsPerMeter) == (1000 of gramsPerMeter)            // true
(1 of tex) == (9 of denier)                                     // true (섬유 관계)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

(0.5 of kilogramsPerMeter).toString()                 // "0.5 kg/m"(기본 단위)
"${(0.5 of kilogramsPerMeter) into gramsPerMeter} g/m" // "500.0 g/m"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학          | Kotlin                         | 의미                              |
|---------------|--------------------------------|-----------------------------------|
| `kg/m`        | `kilogramsPerMeter`            | 선밀도, 기본 단위(이름 붙은 토큰) |
| `kg·m⁻¹`      | `kilo.grams * (meters pow -1)` | 순수한 곱으로 표현한 같은 양      |
| `tex`         | `tex`                          | 섬유 선밀도(1 g/km)               |
| `ρ_l = m / l` | `mass / length`                | 타입 분해                         |
| `m = ρ_l · l` | `lineardensity * length`       | 질량에 대해 정리                  |
| `dtex`        | `deci.tex`                     | 접두사가 붙은 섬유 읽기           |
