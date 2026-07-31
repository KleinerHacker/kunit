# 변형률

패키지: `org.pcsoft.framework.kunit.mechanic.strain`
기본 단위: **순수 비율**(`KStrainUnit.BASE == KStrainUnit.RATIO`)

유형: **구성된 단위**

변형률 `ε = ΔL / L` 은 물체의 상대적 변형입니다. 이는 **무차원**입니다 — 길이를 길이로 나눈 값입니다 — 하지만 그 읽기 (퍼센트, 퍼밀, 마이크로스트레인)는 실질적인 단위 어휘를 이루므로,
KUnit 은 이를 독자적인 그룹으로 모델링합니다.

`KStrainUnitInstance` 는 지수 1 의 단일 `KStrainUnit.BASE` 항으로 된 `KMixedUnitInstance` 를 감싸며, 항상 순수 비율로 정규화됩니다.

!!! note "왜 연산자가 아니라 `toStrain()` 인가"
제네릭 엔진은 `length / length` 를 단위 항이 **없는** 혼합 단위로 표현합니다.
`KLengthUnitInstance.div` 는 멤버 연산자이므로 재정의할 수 없어서, 네이티브 분해는 타입 연산자 대신 형식 인식 훅 `toStrain()` 을 통해 도달합니다.

## 이름이 붙은 단위

| 단위             | 기호 |          토큰 | 비율로 1 단위 |
|------------------|------|--------------:|--------------:|
| 순수 비율(m/m)   | `1`  |       `ratio` |           1.0 |
| 퍼센트           | `%`  |     `percent` |          0.01 |
| 퍼밀             | `‰`  |    `perMille` |          1e-3 |
| 마이크로스트레인 | `µe` | `microstrain` |          1e-6 |

모든 단위는 전체 SI 접두사 범위를 지원하므로, `micro.ratio` 는 마이크로스트레인의 또 다른 표기입니다.

## 변형률 만들기

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.strain.*

// 1 m 길이의 봉이 2 mm 늘어남
val e = ((2 of milli.meters) / (1 of meters)).toStrain()
e into perMille     // 2.0
e into percent      // 0.2
e into microstrain  // 2000.0
e into ratio        // 0.002
```

## 변형률 계산하기

| 식                                       | 결과 타입               | 의미                        |
|------------------------------------------|-------------------------|-----------------------------|
| `(length / length).toStrain()`           | `KStrainUnitInstance`   | `ε = ΔL / L`(네이티브 형식) |
| `stress / strain`                        | `KPressureUnitInstance` | 탄성계수 `E = σ / ε`        |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | 응력 `σ = E · ε`            |
| `strain + strain`, `strain - strain`     | `KStrainUnitInstance`   | 같은 타입 연산              |

훅의 법칙에서 탄성계수 쪽은 [응력](stress.md) 페이지를 참고하세요.

## 실전 예제: 강철 봉의 변형률 게이지

강철 봉 (E = 210 GPa)의 변형률 게이지가 950 µe 를 나타냅니다. 이는 어떤 기계적 응력에 해당하며, 2 m 봉은 얼마나 늘어납니까?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.strain.*
import org.pcsoft.framework.kunit.times

val e = 950 of microstrain
val stress = (210 of giga.pascals) * e
stress into mega.pascals               // ≈ 199.5

val elongation = (2 of meters) * (e into ratio) // 길이의 스칼라 스케일링
elongation into milli.meters                    // 1.9
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

val sum = (3 of perMille) + (1 of perMille) // 4 ‰
(1 of percent) > (5 of perMille)            // true
(1 of percent) == (10 of perMille)          // true
```

## toString 서식

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

(2 of perMille).toString()                 // "0.002 1"(기본 단위: 순수 비율)
"${(2 of perMille) into percent} %"        // "0.2 %"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자 (`²`, `³`, `⁻¹`)로 표기하며, `·`는
곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학         | Kotlin                         | 의미                       |
|--------------|--------------------------------|----------------------------|
| `1`(m/m)     | `ratio`                        | 변형률, 기본 단위(무차원)  |
| `%`          | `percent`                      | 퍼센트 읽기                |
| `‰`          | `perMille`                     | 퍼밀 읽기                  |
| `µe`         | `microstrain`                  | 변형률 게이지 읽기(1 µm/m) |
| `ε = ΔL / L` | `(length / length).toStrain()` | 네이티브 분해              |
| `σ = E · ε`  | `pressure * strain`            | 훅의 법칙                  |
| `E = σ / ε`  | `stress / strain`              | 탄성계수                   |
