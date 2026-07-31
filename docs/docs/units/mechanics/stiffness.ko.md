# 강성 (스프링 상수)

패키지: `org.pcsoft.framework.kunit.mechanic.lineforce`
기본 단위: **뉴턴 매 미터**(`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

유형: **구성된 단위**

강성 (스프링 상수) `k = F / s` 는 단위 변위당 필요한 힘입니다. 차원은
`mass · time⁻²`(`N/m`)이며 — [표면장력](surface-tension.md)의 차원과 정확히 같습니다. KUnit 은 두 읽기 모두를 위해 하나의 중립적인 그룹 `lineforce` 를
모델링하며, 강성은 그 중 하나입니다. 이 페이지는 그 읽기를 문서화합니다.

!!! note "하나의 그룹, 두 개의 읽기"
`KLineForceUnitInstance` 는 공유 타입이므로, KUnit 관점에서 강성과 표면장력은 같은 단위입니다. 이 그룹은 어느 쪽 읽기도 다른 쪽의 이름을 차지하지 않도록 중립적인 이름
`lineforce` 를 가집니다. 값에 이름을 붙여서 서로 구분하세요.

## 이름이 붙은 단위

| 단위             | 기호     |                   토큰 | N/m 로 1 단위 |
|------------------|----------|-----------------------:|--------------:|
| 뉴턴 매 미터     | `N/m`    |      `newtonsPerMeter` |           1.0 |
| 뉴턴 매 밀리미터 | `N/mm`   | `newtonsPerMillimeter` |        1000.0 |
| 킬로폰드 매 미터 | `kp/m`   |    `kilopondsPerMeter` |       9.80665 |
| 파운드힘 매 인치 | `lbf/in` |   `poundsForcePerInch` |     ≈ 175.127 |
| 다인 매 센티미터 | `dyn/cm` |   `dynesPerCentimeter` |          1e-3 |

스프링 데이터 시트는 N/mm 으로 표기합니다; 킬로뉴턴 매 미터는 접두사 형식
`kilo.newtonsPerMeter` 이며 수치적으로 N/mm 과 같습니다.

## 핵심 단위로 계산하기

| 식                                         | 결과 타입                | 의미                                |
|--------------------------------------------|--------------------------|-------------------------------------|
| `force / length`                           | `KLineForceUnitInstance` | `k = F / s`                         |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | 스프링 힘 `F = k · s`               |
| `force / lineforce`                        | `KLengthUnitInstance`    | 변위 `s = F / k`                    |
| `energy / area`                            | `KLineForceUnitInstance` | [표면장력](surface-tension.md) 읽기 |

네이티브 형식은 `toLineForce()` 로 변환됩니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (1 of newtons) / (1 of meters)
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 2)).toLineForce()

typed == native            // true - 둘 다 1 N/m
typed into newtonsPerMeter // 1.0
```

## 실전 예제: 서스펜션의 코일 스프링

코일 스프링의 정격은 40 N/mm 입니다. 2000 N 의 바퀴 하중에서 얼마나 압축되며, 15 mm 변위는 어떤 힘을 발생시킵니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val k = 40 of newtonsPerMillimeter
k into newtonsPerMeter                 // 40000.0

val travel = (2000 of newtons) / k     // KLengthUnitInstance
travel into milli.meters               // 50.0

val force = k * (15 of milli.meters)   // KForceUnitInstance
force into newtons                     // 600.0
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.lineforce.*

// 병렬 스프링은 단순히 더해집니다
val parallel = (40 of newtonsPerMillimeter) + (20 of newtonsPerMillimeter) // 60 N/mm
(40 of newtonsPerMillimeter) > (30 of kilo.newtonsPerMeter)                // true
(1 of newtonsPerMillimeter) == (1 of kilo.newtonsPerMeter)                 // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(40 of newtonsPerMillimeter).toString()                          // "40000.0 N/m"(기본 단위)
"${(40 of newtonsPerMillimeter) into newtonsPerMillimeter} N/mm" // "40.0 N/mm"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학        | Kotlin                          | 의미                    |
|-------------|---------------------------------|-------------------------|
| `N/m`       | `newtonsPerMeter`               | 강성, 기본 단위         |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | 기저 차원의 같은 양     |
| `N/mm`      | `newtonsPerMillimeter`          | 스프링 데이터 시트 읽기 |
| `k = F / s` | `force / length`                | 타입 분해               |
| `F = k · s` | `lineforce * length`            | 스프링 힘               |
| `s = F / k` | `force / lineforce`             | 변위                    |
