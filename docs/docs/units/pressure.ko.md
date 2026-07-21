# 압력

패키지: `org.pcsoft.framework.kunit.pressure`
기본 단위: **파스칼**(`KPressureUnit.BASE == KPressureUnit.PASCAL`)

압력은 **구성된** 단위로, 조합 `mass · length⁻¹ · time⁻²`(`kg/(m·s²)` = `N/m²`)입니다.
`KPressureUnitInstance` 는 세 항 — 지수 `+1` 의 `KMassUnit.BASE`(그램), 지수 `-1` 의 `KDistanceUnit.BASE`
(미터), 지수 `-2` 의 `KTimeUnit.BASE`(초) — 을 감쌉니다. 힘과 마찬가지로 저장 값은 원시 그램 기준 성분
값이며 파스칼로 읽을 때 고정 인자로 나눕니다.

## 압력 만들기

압력은 `force / area` 로, 또는 이름 있는 토큰으로 만듭니다. 이름 있는 단위는 값 1 토큰으로 남습니다
(`of`/`into` 와 함께 사용):

| 압력 | 기호 | 토큰 | 1 단위의 Pa 환산 |
|---|---|---:|---:|
| 파스칼 | `Pa` | `pascals` | 1.0 |
| 바 | `bar` | `bars` | 100000.0 |
| 기압 | `atm` | `atmospheres` | 101325.0 |
| 제곱인치당 파운드힘 | `psi` | `psis` | 6894.757 |
| 토르(mmHg) | `Torr` | `torrs` | 133.322 |

접두사로 유도 가능한 표기는 전용 토큰이 아닙니다: **hPa** = `hecto.pascals`, **kPa** = `kilo.pascals`,
구조역학 단위 **N/mm² = MPa** = `mega.pascals`(또는 식 `newtons / (milli.meters pow 2)`).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.pressure.*

val p = 2 of bars
p into pascals               // 200000.0
p into atmospheres           // ≈ 1.974
(1 of mega.pascals) into pascals // 1000000.0(= 1 N/mm²)
```

## 핵심 단위(힘과 면적)로 계산

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `force / area` | `KPressureUnitInstance` | 압력 = F / A |
| `pressure * area` | `KForceUnitInstance` | 힘 = p · A |
| `area * pressure` | `KForceUnitInstance` | 힘(교환 가능) |
| `force / pressure` | `KAreaUnitInstance` | 면적 = F / p |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.pressure.*

val area = (2 of meters) * (1 of meters)   // KAreaUnitInstance, 2 m²
val p = (100 of newtons) / area            // KPressureUnitInstance, 50 Pa
val f = p * area                           // KForceUnitInstance, 100 N
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

val s = (10 of pascals) + (4 of pascals)  // 14 Pa
(2 of bars) > (1 of atmospheres)          // true
(10 of pascals) * (2 of pascals)          // KMixedUnitInstance(그룹에서 탈출)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

(50 of pascals).toString()   // "50.0 Pa"(기본 단위)
"${(1 of bars) into pascals} Pa" // "100000.0 Pa"
```
