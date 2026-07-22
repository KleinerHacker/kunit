# 주파수

패키지: `org.pcsoft.framework.kunit.frequency`
기준 단위: **헤르츠** (`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

주파수 그룹은 단위 시간당 어떤 일이 몇 번 일어나는지를 모델링합니다. 이는 **네이티브한 일차원** 그룹이며
**시간의 역수** (`1 Hz = 1/s`) 입니다. `KFrequencyUnitInstance` 는 단일 `KFrequencyUnit.HERTZ` 항을 감싸며
항상 헤르츠로 정규화하여 저장합니다.

주파수는 시간의 역수이므로 그룹 간 동작이 **시간과 정확히 반대** 로 정의됩니다. 주파수를 곱하는 것은 시간으로 나누는 것과 같고,
주파수로 나누는 것은 시간을 곱하는 것과 같습니다.

## 단위

| 단위 | 열거값 | 기호 | 토큰 | 1 단위의 헤르츠 값 |
|---|---|---|---:|---:|
| 헤르츠 | `KFrequencyUnit.HERTZ` | `Hz` | `hertz` | 1.0 |
| 초당 회전수 | `KFrequencyUnit.RPS` | `rps` | `rps` | 1.0 |
| 초당 프레임수 | `KFrequencyUnit.FPS` | `fps` | `fps` | 1.0 |
| 분당 회전수 | `KFrequencyUnit.RPM` | `rpm` | `rpm` | 1/60 |
| 분당 박자수 | `KFrequencyUnit.BPM` | `bpm` | `bpm` | 1/60 |

각 `토큰` 은 `of`(구축)와 `into`(읽기)에 사용하는 값 1 의 `KFrequencyUnitInstance` 입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

val f = 2 of kilo.hertz      // 2000 Hz (SI 접두사로 kHz)
f.value                      // 2000.0 (헤르츠로 정규화)
(3000 of rpm) into hertz     // 50.0 (3000 rpm = 50 Hz)
(50 of hertz) into rpm       // 3000.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

// + / - : 같은 그룹, 단위 간 자동 변환
val a = (1 of kilo.hertz) + (500 of hertz)   // KFrequencyUnitInstance: 1500.0 Hz
val b = (1 of kilo.hertz) - (500 of hertz)   // KFrequencyUnitInstance: 500.0 Hz

// 비교와 동등성 (정규화된 헤르츠 값 기준)
(1 of kilo.hertz) == (1000 of hertz)         // true
(1 of kilo.hertz) > (500 of hertz)           // true
```

### 시간의 역수인 그룹 간 연산자

주파수와 시간은 서로 역수이므로 강타입 결과로 결합됩니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.frequency.*

val f = 60 / (1 of seconds)          // KFrequencyUnitInstance, 60 Hz (횟수 / 시간 = 주파수)
val period = 1 / (2 of hertz)        // KTimeUnitInstance, 0.5 s (횟수 / 주파수 = 시간)
val count = (50 of hertz) * (2 of seconds)   // 100.0 (주파수 * 시간 = 무차원 횟수)

val v = (2 of meters) * (5 of hertz) // KSpeedUnitInstance, 10 m/s (길이 * 주파수 = 속도)
(v / (5 of hertz)) into meters       // 2.0 (속도 / 주파수 = 거리)
```

## 실제 예제: 회전하는 바퀴의 표면 속도

둘레가 **2 m** 인 바퀴가 **초당 5 회전** 합니다. 둘레에 회전 주파수를 곱하면 표면(접지) 속도가 됩니다.
이는 `length * frequency = speed` 이며, 익숙한 `length / time = speed` 의 역연산입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.frequency.*

val circumference = 2 of meters
val revolutions = 5 of rps               // 5 Hz
val surfaceSpeed = circumference * revolutions // KSpeedUnitInstance
surfaceSpeed.value                       // 10.0 m/s
```

## `pow` 를 사용한 거듭제곱

중위 연산자 `pow` 로 정수 거듭제곱을 계산합니다(Kotlin 에는 오버로드 가능한 `^` 가 없습니다). 주파수 그룹에서
`pow` 는 범용 `KMixedUnitInstance` 를 반환합니다(주파수에는 차원이 있는 거듭제곱 타입이 없습니다).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.frequency.*

val squared = (2 of hertz) pow 2     // KMixedUnitInstance: 4.0 Hz²
```

## SI 접두사

주파수는 **모든** 크기를 허용하므로 모든 SI 접두사 빌더(`quetta` … `quecto`)를 속성 접근으로 모든 주파수 단위와
결합할 수 있습니다. `kilo.hertz` 는 kHz, `mega.hertz` 는 MHz, `giga.hertz` 는 GHz 입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.frequency.*

(1 of mega.hertz).value          // 1000000.0 (MHz)
(2_400_000_000 of hertz) into giga.hertz // 2.4 (GHz)
```

## toString 형식

기준 단위 `toString()` 만 존재합니다. 특정 단위로 형식화하려면 `into` 를 사용하세요.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

(1 of kilo.hertz).toString()             // "1000.0 Hz" (기준 단위 표현)
"${(50 of hertz) into rpm} rpm"          // "3000.0 rpm"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적 표기와 KUnit 의 Kotlin 표기로 보여줍니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)를 사용하고, `·` 는 곱셈, `/` 는 분수를 나타냅니다. 한 양을 분수와 음의 지수를 가진 곱 두 가지로 쓸 수 있는 경우 두 등가 Kotlin 형식을 모두 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `Hz` | `hertz` | 주파수, 기준 단위(헤르츠) |
| `kHz` | `kilo.hertz` | 킬로헤르츠(헤르츠에 접두사 적용) |
| `1/s` = `s⁻¹` | `1 / (1 of seconds)` | 주기로부터의 주파수(타입화된 헤르츠) |
| `Hz²` | `hertz pow 2` | 헤르츠 제곱(범용 혼합 단위) |
