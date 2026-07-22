# 면밀도

패키지: `org.pcsoft.framework.kunit.areadensity`
기본 단위: **킬로그램 매 제곱미터**(`KAreaDensityUnit.BASE == KAreaDensityUnit.KILOGRAM_PER_SQUARE_METER`)

유형: **구성된 단위**

면밀도(면질량 / 면하중, 구조역학에서 일반적)는 **구성된** 단위로, 조합 `mass · length⁻²`(`kg/m²`)입니다.
`KAreaDensityUnitInstance` 는 두 항 — 지수 `+1` 의 `KMassUnit.BASE`(그램)와 지수 `-2` 의
`KDistanceUnit.BASE`(미터) — 을 감쌉니다. 저장 값은 원시 그램 기준 성분 값이며 kg/m² 로 읽을 때 고정 인자로
나눕니다.

## 면밀도 만들기

밀도와 마찬가지로 면밀도에도 **맨 토큰이 없습니다** — 모든 표기(kg/m², g/mm² 등)는 비율입니다. 식으로, 또는
타입이 지정된 `mass / area` 연산자로 만들고, 그런 식에 대해 `into` 로 다시 읽습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val q = (25 of kilo.grams) / ((5 of meters) * (1 of meters)) // KAreaDensityUnitInstance, 5 kg/m²
q into (kilo.grams / (meters pow 2))       // 5.0
q into (grams / (milli.meters pow 2))      // 0.005(mm² 당)
```

## 핵심 단위(질량, 면적, 밀도)로 계산

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `mass / area` | `KAreaDensityUnitInstance` | 면밀도 = m / A |
| `area density * area` | `KMassUnitInstance` | 질량 = q · A |
| `area * area density` | `KMassUnitInstance` | 질량(교환 가능) |
| `mass / area density` | `KAreaUnitInstance` | 면적 = m / q |
| `density * length` | `KAreaDensityUnitInstance` | 주어진 재료·두께의 판 |
| `area density / length` | `KDensityUnitInstance` | 체적 밀도로 되돌림 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*
import org.pcsoft.framework.kunit.areadensity.*

// 두께 3 m 판: 밀도 × 두께 = 면질량
val density = (2 of kilo.grams) / (1 of liters)      // 2000 kg/m³
val q = density * (3 of meters)                      // KAreaDensityUnitInstance
q into (kilo.grams / (meters pow 2))                 // 6000.0
val back = q / (3 of meters)                         // KDensityUnitInstance, 2000 kg/m³
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val area = (5 of meters) * (1 of meters)
val a = (15 of kilo.grams) / area   // 3 kg/m²
val b = (5 of kilo.grams) / area    // 1 kg/m²
(a - b) into (kilo.grams / (meters pow 2)) // 2.0
a > b                                       // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.areadensity.*

((5 of kilo.grams) / ((5 of meters) * (1 of meters))).toString() // "1.0 kg/m²"(기본 단위)
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·`는 곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `kg/m²` | `kilo.grams / (meters pow 2)` | 면밀도, 기본 단위(제곱미터당 킬로그램) — 분수 형식 |
| `kg·m⁻²` | `kilo.grams * (meters pow -2)` | 같은 면밀도를 음의 지수 곱으로 표현 |
| `g/mm²` | `grams / (milli.meters pow 2)` | 제곱밀리미터당 그램 |
| `25 kg / (5 m · 1 m)` | `(25 of kilo.grams) / ((5 of meters) * (1 of meters))` | 질량 ÷ 넓이로 생성 |
