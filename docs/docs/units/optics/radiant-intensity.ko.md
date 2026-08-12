# 복사도

패키지: `org.pcsoft.framework.kunit.optic.radiantintensity`
기본 단위: **와트 매 스테라디안**(`KRadiantIntensityUnit.BASE == KRadiantIntensityUnit.WATT_PER_STERADIAN`)

종류: **구성 단위**

복사도 `Iₑ`는 광원이 **입체각당** 방출하는 복사속(전력)입니다: `Iₑ = P / Ω`. 이는
[광도](luminous-intensity.ko.md)의 **방사량** 체계 대응 물리량입니다 — 기하학적 구조는 같지만
루멘이 아니라 와트로 측정되므로, 눈으로 볼 수 없는 적외선과 자외선을 포함한 모든 방사를 셈합니다.

이 물리량의 정준 기본 차원 표준형은 `mass¹ · distance² · time⁻³ · solidAngle⁻¹`입니다.

## 단위

| 단위               | 열거값                                   | 기호 |               토큰 | W/sr 1단위당 |
|--------------------|------------------------------------------------|--------|--------------------:|---------------:|
| 와트 매 스테라디안 | `KRadiantIntensityUnit.WATT_PER_STERADIAN`   | `W/sr` | `wattsPerSteradian` |            1.0 |

이 토큰은 모든 SI 접두사를 사용할 수 있습니다(`milli.wattsPerSteradian`, `kilo.wattsPerSteradian` 등).

## 분해

이 그룹에는 하나의 분해가 있으며, 두 형식 모두 동일한 타입이 지정된 값이 같은 인스턴스를 만들어
냅니다. 이 그룹은 질량 항을 가지므로 네이티브 형식은 **단위 템플릿**으로부터 조립됩니다
(같은 설명은 [발광 효율](luminous-efficacy.ko.md)도 참조하십시오).

| 형식             | 표현식                                                        |
|------------------|---------------------------------------------------------------------|
| 타입이 지정된 연산자   | `power / solidAngle`                                              |
| 네이티브 형식(`toX()`) | `(5 of kilo.grams · m² / s³ / sr).toRadiantIntensity()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val typed = (20 of watts) / (4 of steradians)
val native = (
    5 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / steradians.toUnit()
).toRadiantIntensity()

typed == native                 // true
typed into wattsPerSteradian    // 5.0
```

## 그룹으로 계산하기

| 표현식                        | 결과 타입                       | 의미                    |
|-----------------------------------|------------------------------------|-----------------------------|
| `power / solidAngle`              | `KRadiantIntensityUnitInstance`   | `Iₑ = P / Ω`               |
| `radiantIntensity * solidAngle`   | `KPowerUnitInstance`              | `P = Iₑ · Ω`               |
| `power / radiantIntensity`        | `KSolidAngleUnitInstance`         | 퍼지는 원뿔각 |
| `radiantIntensity / area`         | `KRadianceUnitInstance`           | `Lₑ = Iₑ / A`              |

## 실전 예제 — 적외선 LED

적외선 발광체가 0.2 sr 원뿔각으로 **20 mW**를 방사합니다. 그 복사도, 그리고 0.05 sr 검출기 조리개가
받는 전력은 다음과 같습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val i = (20 of milli.watts) / (0.2 of steradians)
i into milli.wattsPerSteradian       // 100.0

val caught = i * (0.05 of steradians)  // KPowerUnitInstance
caught into milli.watts                // 5.0 mW가 검출기에 도달
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 W/sr 값**을 비교하므로
`(1 of wattsPerSteradian) == (1000 of milli.wattsPerSteradian)`입니다. `toString()`은 값을
기본 단위로 표시합니다: `"5.0 W/sr"`.

## 관련 항목

* [광도](luminous-intensity.ko.md) — 측광량 체계의 대응 물리량.
* [복사휘도](radiance.ko.md) — 발광 면적당 복사도.
* [발광 효율](luminous-efficacy.ko.md) — 와트와 루멘 사이의 다리.
* [광학 개요](overview.ko.md)
</content>
