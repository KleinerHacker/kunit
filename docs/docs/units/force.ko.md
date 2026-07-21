# 힘

패키지: `org.pcsoft.framework.kunit.force`
기본 단위: **뉴턴**(`KForceUnit.BASE == KForceUnit.NEWTON`)

힘은 **구성된** 단위로, 조합 `mass · length · time⁻²`(`kg·m/s²`)입니다. `KForceUnitInstance` 는 세 항 —
지수 `+1` 의 `KMassUnit.BASE`(그램), 지수 `+1` 의 `KDistanceUnit.BASE`(미터), 지수 `-2` 의
`KTimeUnit.BASE`(초) — 을 감쌉니다. 라이브러리의 질량 성분은 **그램**(킬로그램이 아님)으로 정규화되므로
뉴턴은 원시 성분 기준의 1000배입니다. 저장 값은 원시 성분 값이며 뉴턴으로 읽을 때 이 고정 인자로 나눕니다.

## 힘 만들기

힘은 `mass * acceleration` 으로, 또는 이름 있는 토큰으로 만듭니다. 이름 있는 단위는 값 1 토큰으로 남습니다
(`of`/`into` 와 함께 사용):

| 힘 | 기호 | 토큰 | 1 단위의 N 환산 |
|---|---|---:|---:|
| 뉴턴 | `N` | `newtons` | 1.0 |
| 다인 | `dyn` | `dynes` | 1.0e-5 |
| 파운드힘 | `lbf` | `poundsForce` | 4.4482216152605 |
| 폰드(그램힘) | `p` | `ponds` | 9.80665e-3 |

**킬로폰드 / 킬로그램힘(kgf)은 전용 토큰이 아닙니다** — 킬로뉴턴이 `kilo.newtons` 인 것처럼 `kilo.ponds`
입니다. 이름 있는 단위는 `KPrefixBuilder` 를 통해 SI 접두사를 지원합니다(`kilo.newtons`, `mega.newtons`,
`kilo.ponds` 등).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f into newtons               // 10.0
f into poundsForce           // ≈ 2.248
(1 of kilo.ponds) into newtons // 9.80665(1 kp = 1 kgf)
```

## 핵심 단위(질량과 가속도)로 계산

| 식 | 결과 타입 | 의미 |
|---|---|---|
| `mass * acceleration` | `KForceUnitInstance` | 힘 = m · a(뉴턴 제2법칙) |
| `acceleration * mass` | `KForceUnitInstance` | 힘(교환 가능) |
| `force / mass` | `KAccelerationUnitInstance` | 가속도 = F / m |
| `force / acceleration` | `KMassUnitInstance` | 질량 = F / a |
| `force / area` | `KPressureUnitInstance` | 압력(압력 참조) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*

val f = (2 of kilo.grams) * (3 of standardGravities) // KForceUnitInstance
f into newtons               // ≈ 58.84
val a = (10 of newtons) / (2 of kilo.grams)          // KAccelerationUnitInstance, 5 m/s²
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

val s = (10 of newtons) + (4 of newtons)  // 14 N
(10 of newtons) > (4 of newtons)          // true
(10 of newtons) * (2 of newtons)          // KMixedUnitInstance(그룹에서 탈출)
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

(10 of newtons).toString()   // "10.0 N"(기본 단위)
"${(1 of kilo.ponds) into newtons} N" // "9.80665 N"
```
