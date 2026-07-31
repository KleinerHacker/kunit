# 관성 모멘트

패키지: `org.pcsoft.framework.kunit.mechanic.inertia`
기본 단위: **킬로그램 제곱미터**(`KInertiaUnit.BASE == KInertiaUnit.KILOGRAM_METERS_SQUARED`)

유형: **구성된 단위**

관성 모멘트 `J` 는 [질량](mass.md)의 회전 대응물입니다: 물체가 회전 변화에 얼마나 강하게 저항하는지를 나타냅니다. 이는 **구성된** 단위로, 조합 `mass · length²`(`kg·m²`)입니다.

`KInertiaUnitInstance` 는 정규 형식으로 정확히 두 항 — 지수 `+1` 의 `KMassUnit.BASE`(그램)와 지수 `+2`
의 `KDistanceUnit.BASE`(미터) — 을 감싼 `KMixedUnitInstance` 를 감쌉니다. 이 라이브러리의 질량 성분은 그램으로 정규화되므로, 저장 값은 원시 그램 기준 성분 값이며 kg·m²
로 읽을 때 고정 인자로 나눕니다.

## 이름이 붙은 단위

| 단위              | 기호      |                     토큰 |   kg·m²로 1 |
|-------------------|-----------|-------------------------:|------------:|
| 킬로그램 제곱미터 | `kg*m^2`  |  `kilogramMetersSquared` |         1.0 |
| 그램 제곱센티미터 | `g*cm^2`  | `gramCentimetersSquared` |        1e-7 |
| 파운드-피트 제곱  | `lb*ft^2` |       `poundFeetSquared` | ≈ 0.0421401 |

모든 단위는 전체 SI 접두사 범위를 지원합니다 (소형 서보 로터용 `milli.kilogramMetersSquared`).

## 분해

| 형식            | Kotlin                                                  | 결과 타입              |
|-----------------|---------------------------------------------------------|------------------------|
| 질량 × 면적     | `mass * area`                                           | `KInertiaUnitInstance` |
| 네이티브 표현식 | `(mass.toUnit() * (length.toUnit() pow 2)).toInertia()` | `KInertiaUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.inertia.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) * ((3 of meters) * (3 of meters))
val native = ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2)).toInertia()

typed == native                     // true
typed into kilogramMetersSquared    // 18.0
```

## 핵심 단위로 계산

| 식                              | 결과 타입                      | 의미                                        |
|---------------------------------|--------------------------------|---------------------------------------------|
| `mass * area`, `area * mass`    | `KInertiaUnitInstance`         | `J = m · r²`                                |
| `inertia / mass`                | `KAreaUnitInstance`            | 회전 반경의 제곱 `r² = J / m`               |
| `inertia / area`                | `KMassUnitInstance`            | `m = J / r²`                                |
| `inertia * angularvelocity`     | `KAngularMomentumUnitInstance` | [각운동량](angular-momentum.md) `L = J · ω` |
| `inertia * angularacceleration` | `KEnergyUnitInstance`          | [토크](torque.md) `M = J · α`               |

## 실전 예제: 프레스의 플라이휠

단단한 플라이휠 원판 (`J = ½ · m · r²`)의 질량은 40 kg, 반지름은 0.3 m 입니다. 관성 모멘트는 얼마이며, 1500 rpm 에서 어떤 각운동량을 가집니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute
import org.pcsoft.framework.kunit.mechanic.inertia.*

val r = 0.3 of meters
val j = ((40 of kilo.grams) * (r * r)) / 2  // ½ · m · r²
j into kilogramMetersSquared                // 1.8

val l = j * (1500 of revolutionsPerMinute)  // KAngularMomentumUnitInstance
l into kilogramMetersSquaredPerSecond       // ≈ 282.74
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

val total = (10 of kilogramMetersSquared) + (4 of kilogramMetersSquared) // 14 kg·m²
(10 of kilogramMetersSquared) > (4 of kilogramMetersSquared)            // true
(10 of kilogramMetersSquared) * (2 of kilogramMetersSquared)            // KMixedUnitInstance
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

(18 of kilogramMetersSquared).toString()                       // "18.0 kg*m^2"(기본 단위)
"${(18 of kilogramMetersSquared) into poundFeetSquared} lb*ft^2" // "427.1... lb*ft^2"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학         | Kotlin                          | 의미                              |
|--------------|---------------------------------|-----------------------------------|
| `kg·m²`      | `kilogramMetersSquared`         | 관성 모멘트, 기본 단위(명명 토큰) |
| `kg·m^2`     | `kilo.grams * (meters pow 2)`   | 같은 양을 순수 곱으로 표현        |
| `J = m · r²` | `mass * area`                   | 타입이 지정된 분해                |
| `r² = J / m` | `inertia / mass`                | 회전 반경의 제곱                  |
| `L = J · ω`  | `inertia * angularvelocity`     | 각운동량                          |
| `M = J · α`  | `inertia * angularacceleration` | 토크                              |
