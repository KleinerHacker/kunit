# 노출량 (광노출량)

패키지: `org.pcsoft.framework.kunit.optic.luminousexposure`
기본 단위: **럭스 초**(`KLuminousExposureUnit.BASE == KLuminousExposureUnit.LUX_SECOND`)

종류: **구성 단위**

노출량 `H`는 **시간에 걸쳐 누적된** 조도입니다: `H = E · t`. 이는 표면이 받은 *광량(빛의 총량)*으로 —
박물관 보존 담당자가 안료의 퇴색을 제한하기 위해 연간 럭스시간 단위로 예산을 잡는 물리량이며, 카메라
노출값의 배후에 있는 물리량입니다.

이 물리량의 정준 기본 차원 표준형은 `luminousIntensity¹ · solidAngle¹ · distance⁻² · time¹`입니다.

## 단위

| 단위       | 열거값                          | 기호 |        토큰 | lx·s 1단위당 |
|------------|-------------------------------------|--------|-------------:|---------------:|
| 럭스 초 | `KLuminousExposureUnit.LUX_SECOND`  | `lx*s` | `luxSeconds` |            1.0 |
| 럭스 시   | `KLuminousExposureUnit.LUX_HOUR`    | `lx*h` |   `luxHours` |           3600 |

모든 토큰은 모든 SI 접두사를 사용할 수 있습니다(`kilo.luxHours`는 연간 광량 예산의 일반적인 단위입니다).

## 분해

이 그룹에는 하나의 분해가 있으며, 두 형식 모두 동일한 타입이 지정된 값이 같은 인스턴스를 만들어
냅니다.

| 형식             | 표현식                                                                   |
|------------------|--------------------------------------------------------------------------|
| 타입이 지정된 연산자   | `illuminance * time`                                                         |
| 네이티브 형식(`toX()`) | `((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val typed = (50 of lux) * (10 of seconds)
val native = ((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()

typed == native          // true
typed into luxSeconds    // 500.0
```

## 그룹으로 계산하기

| 표현식                        | 결과 타입                     | 의미                    |
|-----------------------------------|---------------------------------|-----------------------------|
| `illuminance * time`              | `KLuminousExposureUnitInstance` | `H = E · t`                |
| `luminousExposure / time`         | `KIlluminanceUnitInstance`      | 평균 조도        |
| `luminousExposure / illuminance`  | `KTimeUnitInstance`             | 노출 시간          |

## 실전 예제 — 박물관의 광량 예산

민감한 수채화는 연간 약 **50,000 lx·h**로 제한됩니다. 전시 조도 50 lx, 하루 개관 시간 8시간일 때,
며칠 동안 전시할 수 있을까요?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val perDay = (50 of lux) * (8 of hours)     // KLuminousExposureUnitInstance
perDay into luxHours                         // 400.0

val budget = 50_000 of luxHours
(budget into luxHours) / (perDay into luxHours)   // 연간 125 개관일

// 반대로: 200 lx에서는 얼마나 오래 전시할 수 있는가?
val t = budget / (200 of lux)                // KTimeUnitInstance
t into hours                                  // 250.0 시간
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 lx·s 값**을 비교하므로 `(1 of luxHours) == (3600 of luxSeconds)`입니다.
`toString()`은 값을 기본 단위로 표시합니다: `"3600.0 lx*s"`.

## 관련 항목

* [조도](illuminance.ko.md) — 이 물리량이 누적되는 속도.
* [광량](luminous-energy.ko.md) — 조도 대신 광속에 대한 동일한 개념.
* [광학 개요](overview.ko.md)
</content>
