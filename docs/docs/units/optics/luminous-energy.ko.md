# 광량 (발광 에너지)

패키지: `org.pcsoft.framework.kunit.optic.luminousenergy`
기본 단위: **루멘 초**(`KLuminousEnergyUnit.BASE == KLuminousEnergyUnit.LUMEN_SECOND`)

종류: **구성 단위**

발광 에너지 `Q`는 **시간에 걸쳐 누적된** 광속입니다: `Q = Φ · t`. 광속이 램프가 *지금 이 순간* 얼마나
밝은지를 나타낸다면, 발광 에너지는 램프가 총 얼마나 많은 빛을 전달했는지를 나타냅니다 — 램프 수명 등급과
사진 플래시 에너지의 배후에 있는 물리량입니다. 루멘 초는 **탈봇**이라고도 불립니다.

이 물리량의 정준 기본 차원 표준형은 `luminousIntensity¹ · solidAngle¹ · time¹`입니다.

## 단위

| 단위         | 열거값                          | 기호 |          토큰 | lm·s 1단위당 |
|--------------|-------------------------------------|--------|---------------:|---------------:|
| 루멘 초 | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` | `lumenSeconds` |            1.0 |
| 탈봇       | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` |      `talbots` |            1.0 |
| 루멘 시   | `KLuminousEnergyUnit.LUMEN_HOUR`    | `lm*h` |    `lumenHours` |           3600 |

`talbots`는 기본 단위의 또 다른 표기이며 독자적인 단위가 아닙니다. 모든 토큰은 모든 SI 접두사를
사용할 수 있습니다(`kilo.lumenHours`, `milli.lumenSeconds` 등).

## 분해

이 그룹에는 하나의 분해가 있으며, 두 형식 모두 동일한 타입이 지정된 값이 같은 인스턴스를 만들어
냅니다.

| 형식             | 표현식                                                                  |
|------------------|-------------------------------------------------------------------------|
| 타입이 지정된 연산자   | `luminousFlux * time`                                                       |
| 네이티브 형식(`toX()`) | `((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val typed = (800 of lumens) * (5 of seconds)
val native = ((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()

typed == native            // true
typed into lumenSeconds    // 4000.0
```

## 그룹으로 계산하기

| 표현식                       | 결과 타입                   | 의미                       |
|-----------------------------------|--------------------------------|--------------------------------|
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance` | `Q = Φ · t`                   |
| `luminousEnergy / time`          | `KLuminousFluxUnitInstance`   | 평균 광속              |
| `luminousEnergy / luminousFlux`  | `KTimeUnitInstance`           | 광속이 방출된 시간 |

## 실전 예제 — 램프 수명 동안의 총 광량

800 lm LED 전구의 정격 수명은 **25,000시간**입니다. 이 전구가 일생 동안 전달할 총 광량은 다음과
같습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val q = (800 of lumens) * (25_000 of hours)
q into lumenHours          // 20_000_000.0
q into mega.lumenHours     // 20.0

// 하루 3시간씩 사용한다면, 며칠을 커버할 수 있는가?
val perDay = (800 of lumens) * (3 of hours)
q into lumenHours / (perDay into lumenHours)   // ≈ 8333일
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 lm·s 값**을 비교하므로 `(1 of lumenHours) == (3600 of lumenSeconds)`입니다.
`toString()`은 값을 기본 단위로 표시합니다: `"3600.0 lm*s"`.

## 관련 항목

* [광속](luminous-flux.ko.md) — 이 물리량이 누적되는 속도.
* [노출량](luminous-exposure.ko.md) — 광속 대신 조도에 대한 동일한 개념.
* [광학 개요](overview.ko.md)
</content>
