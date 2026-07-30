# 비에너지 (Specific Energy)

패키지: `org.pcsoft.framework.kunit.thermo.specificenergy`
기본 단위: **킬로그램당 줄** (`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

유형: **구성된 단위**

비에너지는 단위 질량당 에너지입니다: `energy / mass` (`J/kg`). 맥락에 따라 동일한 양이 *비엔탈피*,
*비잠열* 또는 *발열량*이라고 불립니다 — 이들은 모두 이 단위 그룹을 공유합니다.

`KSpecificEnergyUnitInstance`는 정규 형식 `distance² · time⁻²` (`m²·s⁻²`)의 정확히 두 항으로 이루어진
`KMixedUnitInstance`를 감싸며, 항상 J/kg로 정규화됩니다.

!!! note "질량 차원이 상쇄됨"
    `J/kg = kg·m²·s⁻²/kg = m²·s⁻²`. 따라서 정규 형식에는 질량 항이 전혀 없습니다. `KMassUnitInstance`에
    대한 연산자만이 질량 그룹의 그램 기준과 이 그룹의 킬로그램당 정의를 연결합니다.

단위 온도당으로는 [비열](specific-heat-capacity.md)이 되고, 킬로그램 대신 몰당으로는
[몰 에너지](molar-energy.md)가 됩니다.

## 이름이 붙은 단위

| 단위 | 기호 | 토큰 | J/kg로 1 |
|---|---|---:|---:|
| 킬로그램당 줄 | `J/kg` | `joulesPerKilogram` | 1.0 |
| 그램당 칼로리 | `cal/g` | `caloriesPerGram` | 4184.0 |
| 킬로그램당 와트시 | `Wh/kg` | `wattHoursPerKilogram` | 3600.0 |
| 파운드당 Btu | `Btu/lb` | `btusPerPound` | 2326.0 |

모두 전체 SI 접두사 범위를 지원합니다(`kilo.joulesPerKilogram`, `mega.joulesPerKilogram`,
`kilo.wattHoursPerKilogram` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val h = 334 of kilo.joulesPerKilogram
h into joulesPerKilogram      // 334_000.0
h into caloriesPerGram        // ≈ 79.83
h into wattHoursPerKilogram   // ≈ 92.78
```

## 실전 예제: 얼음 녹이기

물의 융해 잠열은 334 kJ/kg입니다. 2.5 kg짜리 얼음 덩어리를 녹이는 데 얼마의 에너지가 필요할까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val latentHeat = 334 of kilo.joulesPerKilogram
val block = 2.5 of kilo.grams

val energy = latentHeat * block     // KEnergyUnitInstance
energy into kilo.joules             // 835.0 kJ
energy into joules                  // 835_000.0 J

// 역방향: 1 MJ로 얼마의 얼음을 녹일 수 있는가?
val melted = (1000 of kilo.joules) / latentHeat  // KMassUnitInstance
melted into kilo.grams              // ≈ 2.994 kg
```

## 핵심 단위(에너지 & 질량)로 계산하기

| 표현식 | 결과 타입 | 의미 |
|---|---|---|
| `energy / mass` | `KSpecificEnergyUnitInstance` | 비에너지 |
| `specificEnergy * mass` | `KEnergyUnitInstance` | 총 에너지 |
| `mass * specificEnergy` | `KEnergyUnitInstance` | 총 에너지(교환 법칙) |
| `energy / specificEnergy` | `KMassUnitInstance` | 관련된 질량 |

## 분해

두 분해 모두 동일한 타입이 지정된 값-동등 인스턴스를 생성합니다.

| 분해 | 형식 | 결과 |
|---|---|---|
| `energy / mass` | 타입이 지정된 연산자 | `KSpecificEnergyUnitInstance` 직접 |
| `distance² · time⁻²` | 네이티브 표현식 + `toSpecificEnergy()` | `KSpecificEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

// 타입이 지정된 연산자 형식
val typed = (1 of joules) / (1 of kilo.grams)

// 네이티브 기저 차원 형식 (m²·s⁻²), toSpecificEnergy()가 인식
val native = (((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2)).toSpecificEnergy()

typed == native // true - 둘 다 1.0 J/kg
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val total = (1 of kilo.joulesPerKilogram) + (500 of joulesPerKilogram)  // 1500 J/kg
val rest  = (1 of kilo.joulesPerKilogram) - (250 of joulesPerKilogram)  // 750 J/kg

(1 of kilo.joulesPerKilogram) > (500 of joulesPerKilogram)   // true
(1 of kilo.joulesPerKilogram) == (1000 of joulesPerKilogram) // true
```

## toString 형식화

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

(334 of kilo.joulesPerKilogram).toString()                        // "334000.0 J/kg"
"${(334 of kilo.joulesPerKilogram) into caloriesPerGram} cal/g"   // "79.83..."
```

## 표기법

아래 표는 이 단위와 그 구성 요소가 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를
보여줍니다. 지수는 유니코드 위첨자(`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다.
어떤 양이 분수와 음의 지수를 갖는 곱 둘 다로 표기될 수 있는 경우, 두 가지 동등한 Kotlin 형식이 모두
나열됩니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `J/kg` | `joulesPerKilogram` | 비에너지, 기본 단위 — 이름이 붙은 토큰 |
| `m²·s⁻²` | `(meters pow 2) / (seconds pow 2)` | 기저 차원으로의 동일한 양 |
| `kJ/kg` | `kilo.joulesPerKilogram` | 킬로그램당 킬로줄 |
| `Wh/kg` | `wattHoursPerKilogram` | 킬로그램당 와트시(배터리 에너지 밀도) |
| `q = Q / m` | `(334 of kilo.joules) / (1 of kilo.grams)` | 에너지 ÷ 질량에서 비에너지 |
| `Q = q · m` | `latentHeat * block` | 비에너지 × 질량에서 에너지 |
| `m = Q / q` | `(1000 of kilo.joules) / latentHeat` | 에너지 ÷ 비에너지에서 질량 |
