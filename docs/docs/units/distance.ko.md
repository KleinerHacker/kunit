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

모든 값은 `number of <토큰>`으로 만들고 `value into <토큰>`으로 다시 읽습니다.

## 지수 1 - 길이

| 단위 | Enum 값 | 기호 | 토큰 | 1 단위 (미터) |
|---|---|---|---:|---:|
| 미터 | `KDistanceUnit.METER` | `m` | `meters` | 1.0 |
| 마일 | `KDistanceUnit.MILE` | `mi` | `miles` | 1609.344 |
| 해리 | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `nauticalMiles` | 1852.0 |
| 야드 | `KDistanceUnit.YARD` | `yd` | `yards` | 0.9144 |
| 피트 | `KDistanceUnit.FOOT` | `ft` | `feet` | 0.3048 |
| 인치 | `KDistanceUnit.INCH` | `in` | `inches` | 0.0254 |
| 패덤 | `KDistanceUnit.FATHOM` | `ftm` | `fathoms` | 1.8288 |
| 체인 | `KDistanceUnit.CHAIN` | `ch` | `chains` | 20.1168 |
| 펄롱 | `KDistanceUnit.FURLONG` | `fur` | `furlongs` | 201.168 |
| 천문단위 | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `astronomicalUnits` | 1.495978707e11 |
| 파섹 | `KDistanceUnit.PARSEC` | `pc` | `parsecs` | 3.0856775814913673e16 |
| 큐빗 | `KDistanceUnit.CUBIT` | `cubit` | `cubits` | 0.4572 |
| 로마 피트 (pes) | `KDistanceUnit.ROMAN_FOOT` | `pes` | `romanFeet` | 0.2957 |
| 로마 걸음 (passus) | `KDistanceUnit.ROMAN_PACE` | `passus` | `romanPaces` | 1.4787 |
| 스타디온 | `KDistanceUnit.STADIUM` | `stadium` | `stadia` | 185.0 |
| 로마 마일 (mille passus) | `KDistanceUnit.ROMAN_MILE` | `mp` | `romanMiles` | 1481.5 |
| 로드 (perch) | `KDistanceUnit.ROD` | `rod` | `rods` | 5.0292 |
| 리그 | `KDistanceUnit.LEAGUE` | `lea` | `leagues` | 4828.032 |
| 케이블 길이 | `KDistanceUnit.CABLE_LENGTH` | `cable` | `cableLengths` | 185.2 |
| 베르스타 | `KDistanceUnit.VERST` | `verst` | `versts` | 1066.8 |
| 프로이센 마일 | `KDistanceUnit.PRUSSIAN_MILE` | `prussian mi` | `prussianMiles` | 7532.5 |

### 빛 이동 거리 (접두사 없는 `light` 그룹)

빛 이동 거리는 접두사가 없는 `light` 빌더로 묶여 있어 `5 of light.seconds`, `3 of light.years`
처럼 거의 자연어처럼 작성할 수 있습니다. 이들은 어떤 SI 접두사도 받지 않습니다
(`kilo.lightYears`는 물리적으로 의미가 없기 때문입니다).

| 단위 | 열거값 | 기호 | 토큰 | 1 단위의 미터 값 |
|---|---|---|---:|---:|
| 광초 | `KDistanceUnit.LIGHT_SECOND` | `ls` | `light.seconds` | 299792458.0 |
| 광분 | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `light.minutes` | 1.798754748e10 |
| 광시 | `KDistanceUnit.LIGHT_HOUR` | `lh` | `light.hours` | 1.0792528488e12 |
| 광일 | `KDistanceUnit.LIGHT_DAY` | `ld` | `light.days` | 2.59020683712e13 |
| 광주 | `KDistanceUnit.LIGHT_WEEK` | `lw` | `light.weeks` | 1.813144785984e14 |
| 광년 | `KDistanceUnit.LIGHT_YEAR` | `ly` | `light.years` | 9.4607304725808e15 |

각 `토큰`은 값-1 `KLengthUnitInstance`로, `of`(생성)와 `into`(읽기)에 모두 사용됩니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val d = 5 of miles
d.value               // 8046.72 (미터로 정규화됨)
d into miles          // 5.0 (마일로 다시 읽기)
d into feet           // 26400.0
d into nauticalMiles  // ≈ 4.3452
```

### 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// + / - : 같은 그룹, 서로 다른 길이 단위 간 자동 변환
val a = (1 of miles) + (500 of meters)   // KLengthUnitInstance, 미터로 정규화됨
val b = (2 of miles) - (800 of meters)

// 비교
(2 of miles) > (1 of miles)              // true
(1 of miles) == (1609.344 of meters)     // true (정규화된 값이 같음)
// (5 of hectares) > (5 of meters)       // 컴파일되지 않음: 면적과 길이는 서로 다른 타입

// * / / : 두 피연산자가 모두 정적으로 차원이 정해지면 길이 패밀리 안에 머무름
val area = (200 of meters) * (50 of meters)   // KAreaUnitInstance: value=10000.0 (m²)
val lengthAgain = area / (50 of meters)       // KLengthUnitInstance: value=200.0 (m)
val ratio = (10 of meters) / (2 of meters)    // KMixedUnitInstance (무차원), value=5.0
```

### 숫자로 스케일링

거리 값은 타입을 유지한 채 순수한 `Number`로 스케일링할 수 있습니다(길이는 길이로, 면적은 면적으로 유지). 덕분에 공식 형태의 계산을 자연스럽게 쓸 수 있습니다. 예를 들어 원의 넓이 `A = π · r²`를 전적으로 단위 시스템으로 계산합니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.distance.*

val r = 12 of centi.meters       // KLengthUnitInstance, 0.12 m
val area = Math.PI * (r * r)     // KAreaUnitInstance: π·r² ≈ 0.04524 m²

val tripled = (12 of meters) * 3 // KLengthUnitInstance, 36 m
val half = area / 2              // KAreaUnitInstance, 원 넓이의 절반
```

### 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=`는 **동일한 타입**(동일한 차원)을 가진 두 값의 정규화된 `value`를
비교합니다. 서로 다른 차원(예: 길이와 면적)을 섞는 것은 `+`/`-` 규칙과 마찬가지로 컴파일러가 거부합니다 —
그런 연산자가 존재하지 않습니다. 차원이 다른 `equals`는 단순히 `false`를 반환합니다.

## 지수 2 - 면적

`KAreaUnitInstance`는 면적을 나타냅니다(예: `length * length`의 결과, 또는 길이를 infix `pow` 연산자로
제곱한 결과: `(2 of meters) pow 2` == `(2 m)²` == 4 m², `(2 of kilo.meters) pow 2` == 4 000 000 m²).
`squareXxx` 토큰은 없습니다 — `pow`가 유일한 거듭제곱 문법입니다(아래 "`pow`로 거듭제곱" 절 참고). 다음과
같은 명명된 특수 단위 토큰을 사용할 수 있습니다.

| 특수 단위 | 기호 | 토큰 | 1 단위 (m²) |
|---|---:|---:|---:|
| 아르 | `a` | `ares` | 100.0 |
| 헥타르 | `ha` | `hectares` | 10 000.0 |
| 에이커 | `ac` | `acres` | 4046.8564224 |
| 루드 | `ro` | `roods` | 1011.7141056 |
| 제곱퍼치(제곱로드) | `perch²` | `squarePerches` | 25.29285264 |
| 모르겐(프로이센) | `Mg` | `morgens` | 2553.22 |
| 요흐(오스트리아) | `Joch` | `jochs` | 5754.642 |
| 타크베르크(바이에른) | `Tw` | `tagwerks` | 3407.27 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val plot = 3 of hectares
plot.value        // 30000.0 (m²)
plot into ares    // 300.0
plot into acres   // ≈ 7.4132

val computed = (200 of meters) * (50 of meters)  // KAreaUnitInstance (10 000 m²)
computed into hectares                           // 1.0

plot + computed   // 허용됨: 둘 다 면적 -> KAreaUnitInstance
// plot + (5 of meters)  // 컴파일되지 않음: 면적과 길이
```

## 지수 3 - 부피

`KVolumeUnitInstance`는 부피를 나타냅니다(예: `length * length * length`, `area * length`, 또는 길이를
세제곱한 결과: `(2 of meters) pow 3` == 8 m³). 면적과 마찬가지로 `cubicXxx` 토큰은 없으며 `pow`를
사용합니다(아래 "`pow`로 거듭제곱" 절 참고). 다음과 같은 명명된 특수 단위 토큰을 사용할 수 있습니다.

| 특수 단위 | 기호 | 토큰 | 1 단위 (m³) |
|---|---:|---:|---:|
| 리터 | `L` | `liters` | 0.001 |
| 미국 액량 갤런 | `gal (US)` | `usGallons` | 0.003785411784 |
| 영국 갤런 | `gal (UK)` | `imperialGallons` | 0.00454609 |
| 미국 액량 온스 | `fl oz` | `usFluidOunces` | 2.95735295625e-5 |
| 오일 배럴 | `bbl` | `oilBarrels` | 0.158987294928 |
| 임페리얼 부셸 | `bu (UK)` | `imperialBushels` | 0.03636872 |
| 임페리얼 호그스헤드 | `hhd` | `hogsheads` | 0.32731785 |
| 임페리얼 파인트 | `pt (UK)` | `imperialPints` | 0.00056826125 |
| 임페리얼 쿼트 | `qt (UK)` | `imperialQuarts` | 0.0011365225 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val tank = 200 of liters
tank.value          // 0.2 (m³)
tank into usGallons // ≈ 52.834

val cube = (2 of meters) * (2 of meters) * (2 of meters)  // KVolumeUnitInstance (8 m³)
cube into liters                                          // 8000.0

tank + cube         // 허용됨: 둘 다 부피 -> KVolumeUnitInstance
```

## `pow`로 거듭제곱

infix `pow` 연산자로 값을 정수 거듭제곱합니다. Kotlin에는 오버로드 가능한 `^` 연산자(그리고 `^=`)가
없으므로, `pow`가 모든 그룹에서 유일한 거듭제곱 문법입니다 — `squareXxx`/`cubicXxx` 토큰은 없습니다.

`pow`는 값을 거듭제곱하고 **모든** 지수에 `n`을 곱합니다. 따라서 `(2 of meters) pow 2`는 `(2 m)² = 4 m²`
입니다(지수만이 아니라 값도 거듭제곱됨). 거리 그룹에서는 결과가 차원을 가집니다: `pow 2`는
`KAreaUnitInstance`, `pow 3`은 `KVolumeUnitInstance`, 그 외 지수는 일반 `KDistanceUnitInstance`입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

val area = (2 of meters) pow 2         // KAreaUnitInstance: 4.0 m²
val big = (2 of kilo.meters) pow 2     // KAreaUnitInstance: 4 000 000 m²  ((2000 m)²)
val volume = (2 of meters) pow 3       // KVolumeUnitInstance: 8.0 m³
val m4 = (2 of meters) pow 2 pow 2     // KDistanceUnitInstance: 16.0 m⁴  ((4 m²)²)
val inverse = (2 of meters) pow -1     // KDistanceUnitInstance: 0.5 m⁻¹
```

`pow`는 `* / + -`보다 **더 약하게** 결합합니다. 혼합 식에서는 괄호를 사용하세요(`(a * b) pow 2`). 모든
단위 그룹에서 사용할 수 있습니다 — 예: `(2 of hours) pow 2`(시간에는 차원 거듭제곱 타입이 없으므로 일반
`KMixedUnitInstance`).

## SI 접두어

모든 길이 단위는 24개의 SI 접두어 **빌더**(`kilo`, `milli`, …; 루트 패키지)와 프로퍼티 접근으로 결합하여
`of`/`into`용 값-1 템플릿을 만들 수 있습니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.distance.*

// 생성: "5 of kilo.meters" -> KLengthUnitInstance (== 5000 m)
val fiveKm = 5 of kilo.meters
fiveKm.value // 5000.0

// 접두어가 붙은 단위로 값을 다시 읽기
val d = 5 of miles
d into kilo.meters  // 8.04672 (km)

// 접두어는 명명된 면적/부피 토큰과도 결합됨
val tank = 200 of liters
tank into milli.liters  // 200000.0 (mL)
```

## toString 포맷팅

기본 단위 `toString()`만 존재하며, 특정 단위는 `into`로 포맷합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

(5 of meters).toString()               // "5.0 m" (기본 단위 표현)
"${(5 of miles) into miles} mi"        // "5.0 mi"
"${((200 of meters) * (50 of meters)) into hectares} ha" // "1.0 ha"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·`는 곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `m` | `meters` | 길이, 기본 단위(미터) |
| `km` | `kilo.meters` | 접두사가 붙은 길이(킬로미터) |
| `m²` | `meters pow 2` | 넓이(미터 제곱) |
| `m³` | `meters pow 3` | 부피(미터 세제곱) |
| `m⁻¹` | `meters pow -1` | 길이의 역수 |
| `2 m · 2 m` | `(2 of meters) * (2 of meters)` | 길이×길이로 만든 넓이 |
| `π · A` | `Math.PI * area` | 스칼라×넓이(크기 스케일링, 넓이 유지) |
| `A / 2` | `area / 2` | 넓이를 순수한 숫자로 나눔(넓이 유지) |
