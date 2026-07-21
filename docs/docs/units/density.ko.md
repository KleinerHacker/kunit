# 밀도

패키지: `org.pcsoft.framework.kunit.density`
기본 단위: **킬로그램 매 세제곱미터**(`KDensityUnit.BASE == KDensityUnit.KILOGRAM_PER_CUBIC_METER`)

밀도(질량 밀도)는 **구성된** 단위로, 조합 `mass · length⁻³`(`kg/m³`)입니다. `KDensityUnitInstance` 는 두
항 — 지수 `+1` 의 `KMassUnit.BASE`(그램)와 지수 `-3` 의 `KDistanceUnit.BASE`(미터) — 을 감쌉니다. 저장
값은 원시 그램 기준 성분 값이며 kg/m³ 로 읽을 때 고정 인자로 나눕니다.

## 밀도 만들기

밀도에는 **맨 토큰이 없습니다** — 모든 표기(kg/m³, g/cm³ 등)는 비율입니다. 식으로, 또는 타입이 지정된
`mass / volume` 연산자로 만들고, 그런 식에 대해 `into` 로 다시 읽습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
steel into (kilo.grams / (meters pow 3))   // 7850.0
steel into (kilo.grams / (centi.meters pow 3)) // 0.00785(= 7.85 g/cm³)

val d = (6 of kilo.grams) / (2 of liters)  // 3 kg/L = 3000 kg/m³
```

## 핵심 단위(질량과 부피)로 계산

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `mass / volume` | `KDensityUnitInstance` | 밀도 = m / V |
| `density * volume` | `KMassUnitInstance` | 질량 = ρ · V |
| `volume * density` | `KMassUnitInstance` | 질량(교환 가능) |
| `mass / density` | `KVolumeUnitInstance` | 부피 = m / ρ |
| `density * length` | `KAreaDensityUnitInstance` | 면밀도(면밀도 참조) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

val d = (2 of kilo.grams) / (1 of liters)  // 2 kg/L
val m = d * (3 of liters)                  // KMassUnitInstance
m into kilo.grams                          // 6.0
val v = (6 of kilo.grams) / d              // KVolumeUnitInstance
v into liters                              // 3.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val a = (3 of kilo.grams) / (1 of liters)
val b = (1 of kilo.grams) / (1 of liters)
(a - b) into (kilo.grams / (meters pow 3)) // 2000.0
a > b                                       // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

((1 of kilo.grams) / (1 of liters)).toString() // "1000.0 kg/m³"(기본 단위)
```
