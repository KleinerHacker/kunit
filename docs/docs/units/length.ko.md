# 길이

패키지: `org.pcsoft.framework.kunit.length`
기본 단위: **미터** (`KLengthUnit.BASE == KLengthUnit.METER`)

`KLengthUnitInstance`는 `KLengthUnit.BASE`의 단일 항으로 제한된 `KUnitInstance`를 임의의 지수로
래핑합니다 - 순수한 길이는 지수 1, 면적은 2, 부피는 3입니다. 값은 생성에 사용된 단위와 관계없이 항상
미터(또는 제곱/세제곱미터)로 정규화되어 저장됩니다.

## 지수 1 - 길이

| 단위 | Enum 값 | 기호 | 생성자 | 1 단위 (미터) |
|---|---|---|---:|---:|
| 미터 | `KLengthUnit.METER` | `m` | `Number.meters()` | 1.0 |
| 마일 | `KLengthUnit.MILE` | `mi` | `Number.miles()` | 1609.344 |
| 해리 | `KLengthUnit.NAUTICAL_MILE` | `nmi` | `Number.nauticalMiles()` | 1852.0 |
| 야드 | `KLengthUnit.YARD` | `yd` | `Number.yards()` | 0.9144 |
| 피트 | `KLengthUnit.FOOT` | `ft` | `Number.feet()` | 0.3048 |
| 인치 | `KLengthUnit.INCH` | `in` | `Number.inches()` | 0.0254 |
| 패덤 | `KLengthUnit.FATHOM` | `ftm` | `Number.fathoms()` | 1.8288 |
| 체인 | `KLengthUnit.CHAIN` | `ch` | `Number.chains()` | 20.1168 |
| 펄롱 | `KLengthUnit.FURLONG` | `fur` | `Number.furlongs()` | 201.168 |
| 천문단위 | `KLengthUnit.ASTRONOMICAL_UNIT` | `AU` | `Number.astronomicalUnits()` | 1.495978707e11 |
| 광년 | `KLengthUnit.LIGHT_YEAR` | `ly` | `Number.lightYears()` | 9.4607304725808e15 |
| 파섹 | `KLengthUnit.PARSEC` | `pc` | `Number.parsecs()` | 3.0856775814913673e16 |

위의 모든 단위는 `valueIn`/`toString` 대상이나 접두어 infix 함수의 `unit` 인자로 사용할 수 있는 bare
`val` 별칭을 가지고 있습니다: `meters`, `miles`, `nauticalMiles`, `yards`, `feet`, `inches`, `fathoms`,
`chains`, `furlongs`, `astronomicalUnits`, `lightYears`, `parsecs`.

```kotlin
import org.pcsoft.framework.kunit.length.*

val d = 5.miles()
d.value                        // 8046.72 (미터로 정규화됨)
d.valueIn(KLengthUnit.MILE)    // 5.0 (마일로 다시 읽기)
d.valueIn(feet)                 // 26400.0
d.valueIn(nauticalMiles)        // ≈ 4.3452 (해리로 다시 읽기)
```

### 연산자

```kotlin
import org.pcsoft.framework.kunit.length.*

// + / - : 같은 그룹, 서로 다른 길이 단위 간 자동 변환
val a = 1.miles() + 500.meters()   // KLengthUnitInstance, 미터로 정규화됨
val b = 2.miles() - 800.meters()

// 비교
2.miles() > 1.miles()               // true
1.miles() == 1609.344.meters()      // true (정규화된 값이 같음)
5.hectares() > 5.meters()           // IllegalStateException 발생 (면적 vs 길이, 지수가 다름)

// * / / : 항상 허용되며, 새로운 지수를 가진 KUnitInstance를 생성
val area = 200.meters() * 50.meters()   // KUnitInstance: value=10000.0, units=[METER^2]
val lengthAgain = area / 50.meters().toKUnitInstance() // KUnitInstance: value=200.0, units=[METER^1]
```

### 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=`는 **동일한 지수**를 가진 두 `KLengthUnitInstance`의 정규화된 `value`를
비교합니다. 서로 다른 지수 간(예: 길이와 면적)의 비교는 `+`/`-` 규칙과 마찬가지로
`IllegalStateException`을 던집니다.

## 지수 2 - 면적

지수가 2인 `KLengthUnitInstance`는 면적을 나타냅니다(예: 두 길이를 곱한 결과). 원시 `KLengthUnit.BASE^2`
(제곱미터) 표현 외에도, 다음과 같은 명명된 특수 단위(`KLengthDerivedUnit`)를 변환/포맷 대상으로 사용할
수 있습니다.

| 특수 단위 | Enum 값 | 기호 | 생성자 | 1 단위 (m²) |
|---|---:|---:|---:|---:|
| 아르 | `KLengthDerivedUnit.ARE` | `a` | `Number.ares()` | 100.0 |
| 헥타르 | `KLengthDerivedUnit.HECTARE` | `ha` | `Number.hectares()` | 10 000.0 |
| 에이커 | `KLengthDerivedUnit.ACRE` | `ac` | `Number.acres()` | 4046.8564224 |

```kotlin
import org.pcsoft.framework.kunit.length.*

val plot = 3.hectares()
plot.value                                   // 30000.0 (m²)
plot.valueIn(KLengthDerivedUnit.ARE)          // 300.0
plot.valueIn(KLengthDerivedUnit.ACRE)         // ≈ 7.4132

val computed = 200.meters() * 50.meters()     // KUnitInstance, units=[METER^2]
computed.toKLengthUnit().valueIn(KLengthDerivedUnit.HECTARE) // 1.0

plot + computed.toKLengthUnit()                // 허용됨: 둘 다 지수 2 (면적)
plot + 5.meters()                              // IllegalStateException 발생 (면적 vs 길이)
```

## 지수 3 - 부피

지수가 3인 `KLengthUnitInstance`는 부피를 나타냅니다. 원시 `KLengthUnit.BASE^3`(세제곱미터) 표현 외에도
다음과 같은 명명된 특수 단위를 사용할 수 있습니다.

| 특수 단위 | Enum 값 | 기호 | 생성자 | 1 단위 (m³) |
|---|---:|---:|---:|---:|
| 리터 | `KLengthDerivedUnit.LITER` | `L` | `Number.liters()` | 0.001 |
| 미국 액량 갤런 | `KLengthDerivedUnit.US_GALLON` | `gal (US)` | `Number.usGallons()` | 0.003785411784 |
| 영국 갤런 | `KLengthDerivedUnit.IMPERIAL_GALLON` | `gal (UK)` | `Number.imperialGallons()` | 0.00454609 |
| 미국 액량 온스 | `KLengthDerivedUnit.US_FLUID_OUNCE` | `fl oz` | `Number.usFluidOunces()` | 2.95735295625e-5 |
| 오일 배럴 | `KLengthDerivedUnit.OIL_BARREL` | `bbl` | `Number.oilBarrels()` | 0.158987294928 |

```kotlin
import org.pcsoft.framework.kunit.length.*

val tank = 200.liters()
tank.value                                        // 0.2 (m³)
tank.valueIn(KLengthDerivedUnit.US_GALLON)        // ≈ 52.834

val cube = 2.meters() * 2.meters() * 2.meters()   // KUnitInstance, units=[METER^3]
cube.toKLengthUnit().valueIn(KLengthDerivedUnit.LITER) // 8000.0

tank + cube.toKLengthUnit()                        // 허용됨: 둘 다 지수 3 (부피)
```

## SI 접두어

모든 `KLengthUnit`은 24개의 SI 접두어(`KUnitPrefix`, 루트 패키지, Quetta/Q부터 Quecto/q까지) 중 어떤
것과도 범용 infix 생성 함수와 `with`(valueIn/toString 대상용)를 사용해 결합할 수 있습니다.

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

// 생성: "5 kilo meters" -> KPrefixBuilder -> KUnitInstance -> KLengthUnitInstance
val fiveKm = (5 kilo meters).toKUnitInstance().toKLengthUnit()
fiveKm.value // 5000.0

// 접두어가 붙은 대상을 사용해 값을 다시 읽기
val d = 5.miles()
d.valueIn(KUnitPrefix.KILO with KLengthUnit.METER)  // 8.04672 (km)
d.toString(KUnitPrefix.KILO with KLengthUnit.METER) // "8.04672 km"

// 접두어는 파생 단위(면적/부피)와도 결합됨
val tank = 200.liters()
tank.valueIn(KUnitPrefix.MILLI with KLengthDerivedUnit.LITER) // 200000.0 (mL)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.length.*

5.meters().toString()                        // "5.0 m" (기본 단위 표현)
5.miles().toString(KLengthUnit.MILE)          // "5.0 mi"
(200.meters() * 50.meters()).toKLengthUnit().toString(KLengthDerivedUnit.HECTARE) // "1.0 ha"
```
