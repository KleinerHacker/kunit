# 거리

패키지: `org.pcsoft.framework.kunit.distance`
기본 단위: **미터** (`KDistanceUnit.BASE == KDistanceUnit.METER`)

거리 그룹은 열린 기반 래퍼 `KDistanceUnitInstance`(`KDistanceUnit.BASE`의 단일 항, **임의의** 지수)
아래에서 각 지수를 컴파일 타임에 안전한 고유 타입으로 모델링합니다:

* **`KLengthUnitInstance`** — 지수 1 (길이)
* **`KAreaUnitInstance`** — 지수 2 (면적)
* **`KVolumeUnitInstance`** — 지수 3 (부피)

값은 생성에 사용된 단위와 관계없이 항상 미터(또는 제곱/세제곱미터)로 정규화되어 저장됩니다. 길이, 면적,
부피는 서로 다른 타입이므로 `+`/`-`/비교에서 이들을 섞으면 **컴파일 오류**가 됩니다(그런 연산자가 존재하지
않음). 반면 `*`/`/`는 가능한 한 같은 타입 패밀리 안에 머물며(`length * length = area`,
`area / length = length`), `{1,2,3}` 밖의 지수(또는 지수 0의 무차원 결과)에 대해서는
`KDistanceUnitInstance`/`KMixedUnitInstance`로 폴백합니다.

## 지수 1 - 길이

| 단위 | Enum 값 | 기호 | 생성자 | 1 단위 (미터) |
|---|---|---|---:|---:|
| 미터 | `KDistanceUnit.METER` | `m` | `Number.meters` | 1.0 |
| 마일 | `KDistanceUnit.MILE` | `mi` | `Number.miles` | 1609.344 |
| 해리 | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `Number.nauticalMiles` | 1852.0 |
| 야드 | `KDistanceUnit.YARD` | `yd` | `Number.yards` | 0.9144 |
| 피트 | `KDistanceUnit.FOOT` | `ft` | `Number.feet` | 0.3048 |
| 인치 | `KDistanceUnit.INCH` | `in` | `Number.inches` | 0.0254 |
| 패덤 | `KDistanceUnit.FATHOM` | `ftm` | `Number.fathoms` | 1.8288 |
| 체인 | `KDistanceUnit.CHAIN` | `ch` | `Number.chains` | 20.1168 |
| 펄롱 | `KDistanceUnit.FURLONG` | `fur` | `Number.furlongs` | 201.168 |
| 천문단위 | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `Number.astronomicalUnits` | 1.495978707e11 |
| 광초 | `KDistanceUnit.LIGHT_SECOND` | `ls` | `Number.lightSeconds` | 299792458.0 |
| 광분 | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `Number.lightMinutes` | 1.798754748e10 |
| 광시 | `KDistanceUnit.LIGHT_HOUR` | `lh` | `Number.lightHours` | 1.0792528488e12 |
| 광일 | `KDistanceUnit.LIGHT_DAY` | `ld` | `Number.lightDays` | 2.59020683712e13 |
| 광주 | `KDistanceUnit.LIGHT_WEEK` | `lw` | `Number.lightWeeks` | 1.813144785984e14 |
| 광년 | `KDistanceUnit.LIGHT_YEAR` | `ly` | `Number.lightYears` | 9.4607304725808e15 |
| 파섹 | `KDistanceUnit.PARSEC` | `pc` | `Number.parsecs` | 3.0856775814913673e16 |

위의 모든 단위는 `valueAs`/`toString` 대상이나 접두어 infix 함수의 `unit` 인자로 사용할 수 있는 bare
`val` 별칭을 가지고 있습니다: `meters`, `miles`, `nauticalMiles`, `yards`, `feet`, `inches`, `fathoms`,
`chains`, `furlongs`, `astronomicalUnits`, `lightSeconds`, `lightMinutes`, `lightHours`,
`lightDays`, `lightWeeks`, `lightYears`, `parsecs`.

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5.miles
d.value                        // 8046.72 (미터로 정규화됨)
d.valueAs(KDistanceUnit.MILE)    // 5.0 (마일로 다시 읽기)
d.valueAs(feet)                 // 26400.0
d.valueAs(nauticalMiles)        // ≈ 4.3452 (해리로 다시 읽기)
```

### 연산자

```kotlin
import org.pcsoft.framework.kunit.distance.*

// + / - : 같은 그룹, 서로 다른 길이 단위 간 자동 변환
val a = 1.miles + 500.meters   // KLengthUnitInstance, 미터로 정규화됨
val b = 2.miles - 800.meters

// 비교
2.miles > 1.miles               // true
1.miles == 1609.344.meters      // true (정규화된 값이 같음)
// 5.hectares > 5.meters        // 컴파일되지 않음: 면적과 길이는 서로 다른 타입

// * / / : 두 피연산자가 모두 정적으로 차원이 정해지면 길이 패밀리 안에 머무름
val area = 200.meters * 50.meters   // KAreaUnitInstance: value=10000.0 (m²)
val lengthAgain = area / 50.meters  // KLengthUnitInstance: value=200.0 (m)
val ratio = 10.meters / 2.meters    // KMixedUnitInstance (무차원), value=5.0
```

### 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=`는 **동일한 타입**(동일한 차원)을 가진 두 값의 정규화된 `value`를
비교합니다. 서로 다른 차원(예: 길이와 면적)을 섞는 것은 `+`/`-` 규칙과 마찬가지로 컴파일러가 거부합니다 —
그런 연산자가 존재하지 않습니다. 차원이 다른 `equals`는 단순히 `false`를 반환합니다.

## 지수 2 - 면적

`KAreaUnitInstance`는 면적을 나타냅니다(예: `length * length`의 결과, 또는 길이를 infix `pow` 연산자로
제곱한 결과: `2.meters pow 2` == `(2 m)²` == 4 m², `2 kilo meters pow 2` == 4 000 000 m²). `squareXxx`
생성자는 없습니다 — `pow`가 유일한 거듭제곱 문법입니다(아래 "`pow`로 거듭제곱" 절 참고). 다음과 같은 명명된
특수 단위(`KDistanceDerivedUnit`)를 변환/포맷 대상으로 사용할 수 있습니다.

| 특수 단위 | Enum 값 | 기호 | 생성자 | 1 단위 (m²) |
|---|---:|---:|---:|---:|
| 아르 | `KDistanceDerivedUnit.ARE` | `a` | `Number.ares` | 100.0 |
| 헥타르 | `KDistanceDerivedUnit.HECTARE` | `ha` | `Number.hectares` | 10 000.0 |
| 에이커 | `KDistanceDerivedUnit.ACRE` | `ac` | `Number.acres` | 4046.8564224 |

```kotlin
import org.pcsoft.framework.kunit.distance.*

val plot = 3.hectares
plot.value                                   // 30000.0 (m²)
plot.valueAs(KDistanceDerivedUnit.ARE)          // 300.0
plot.valueAs(KDistanceDerivedUnit.ACRE)         // ≈ 7.4132

val computed = 200.meters * 50.meters     // KAreaUnitInstance (10 000 m²)
computed.valueAs(KDistanceDerivedUnit.HECTARE) // 1.0

plot + computed                              // 허용됨: 둘 다 면적 -> KAreaUnitInstance
// plot + 5.meters                           // 컴파일되지 않음: 면적과 길이
```

## 지수 3 - 부피

`KVolumeUnitInstance`는 부피를 나타냅니다(예: `length * length * length`, `area * length`, 또는 길이를
세제곱한 결과: `2.meters pow 3` == 8 m³, `2 kilo meters pow 3`). 면적과 마찬가지로 `cubicXxx` 생성자는
없으며 `pow`를 사용합니다(아래 "`pow`로 거듭제곱" 절 참고). 다음과 같은 명명된 특수 단위를 사용할 수 있습니다.

| 특수 단위 | Enum 값 | 기호 | 생성자 | 1 단위 (m³) |
|---|---:|---:|---:|---:|
| 리터 | `KDistanceDerivedUnit.LITER` | `L` | `Number.liters` | 0.001 |
| 미국 액량 갤런 | `KDistanceDerivedUnit.US_GALLON` | `gal (US)` | `Number.usGallons` | 0.003785411784 |
| 영국 갤런 | `KDistanceDerivedUnit.IMPERIAL_GALLON` | `gal (UK)` | `Number.imperialGallons` | 0.00454609 |
| 미국 액량 온스 | `KDistanceDerivedUnit.US_FLUID_OUNCE` | `fl oz` | `Number.usFluidOunces` | 2.95735295625e-5 |
| 오일 배럴 | `KDistanceDerivedUnit.OIL_BARREL` | `bbl` | `Number.oilBarrels` | 0.158987294928 |

```kotlin
import org.pcsoft.framework.kunit.distance.*

val tank = 200.liters
tank.value                                        // 0.2 (m³)
tank.valueAs(KDistanceDerivedUnit.US_GALLON)        // ≈ 52.834

val cube = 2.meters * 2.meters * 2.meters   // KVolumeUnitInstance (8 m³)
cube.valueAs(KDistanceDerivedUnit.LITER)    // 8000.0

tank + cube                                  // 허용됨: 둘 다 부피 -> KVolumeUnitInstance
```

## `pow`로 거듭제곱

infix `pow` 연산자로 값을 정수 거듭제곱합니다. Kotlin에는 오버로드 가능한 `^` 연산자(그리고 `^=`)가
없으므로, `pow`가 모든 그룹에서 유일한 거듭제곱 문법입니다 — `squareXxx`/`cubicXxx` 생성자는 없습니다.

`pow`는 값을 거듭제곱하고 **모든** 지수에 `n`을 곱합니다. 따라서 `2.meters pow 2`는 `(2 m)² = 4 m²`
입니다(지수만이 아니라 값도 거듭제곱됨). 거리 그룹에서는 결과가 차원을 가집니다: `pow 2`는
`KAreaUnitInstance`, `pow 3`은 `KVolumeUnitInstance`, 그 외 지수는 일반 `KDistanceUnitInstance`입니다.

```kotlin
import org.pcsoft.framework.kunit.distance.*

val area = 2.meters pow 2         // KAreaUnitInstance: 4.0 m²
val big = 2 kilo meters pow 2     // KAreaUnitInstance: 4 000 000 m²  ((2000 m)²)
val volume = 2.meters pow 3       // KVolumeUnitInstance: 8.0 m³
val m4 = 2.meters pow 2 pow 2     // KDistanceUnitInstance: 16.0 m⁴  ((4 m²)²)
val inverse = 2.meters pow -1     // KDistanceUnitInstance: 0.5 m⁻¹
```

`pow`는 명명된 infix 함수이므로 `* / + -`보다 **더 약하게** 결합합니다. 혼합 식에서는 괄호를 사용하세요
(`(a * b) pow 2`). 모든 단위 그룹에서 사용할 수 있습니다 — 예: `2.hours pow 2`(시간에는 차원 거듭제곱
타입이 없으므로 일반 `KMixedUnitInstance`).

## SI 접두어

모든 `KDistanceUnit`은 24개의 SI 접두어(`KUnitPrefix`, 루트 패키지, Quetta/Q부터 Quecto/q까지) 중 어떤
것과도 그룹별 infix 생성 함수(구체 단위를 바로 반환)와 `with`(valueAs/toString 대상용)를 사용해 결합할 수 있습니다.

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.distance.*

// 생성: "5 kilo meters" -> KLengthUnitInstance (direct, == 5000.meters)
val fiveKm = 5 kilo meters
fiveKm.value // 5000.0

// 접두어가 붙은 대상을 사용해 값을 다시 읽기
val d = 5.miles
d.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER)  // 8.04672 (km)
d.toString(KUnitPrefix.KILO with KDistanceUnit.METER) // "8.04672 km"

// 접두어는 파생 단위(면적/부피)와도 결합됨
val tank = 200.liters
tank.valueAs(KUnitPrefix.MILLI with KDistanceDerivedUnit.LITER) // 200000.0 (mL)
```

## toString 포맷팅

```kotlin
import org.pcsoft.framework.kunit.distance.*

5.meters.toString()                        // "5.0 m" (기본 단위 표현)
5.miles.toString(KDistanceUnit.MILE)          // "5.0 mi"
(200.meters * 50.meters).toString(KDistanceDerivedUnit.HECTARE) // "1.0 ha"
```
