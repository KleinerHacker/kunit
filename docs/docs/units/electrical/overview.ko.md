# 전기공학 — 개요

패키지: `org.pcsoft.framework.kunit.ec`, `…voltage`, `…resistance`

전기공학은 회로를 흐르는 전류, 그것을 구동하는 전압, 그리고 그것을 방해하는 저항을 함께 묶습니다. 이
셋은 **옴의 법칙**으로 연결되며, KUnit은 그 법칙을 타입이 지정된 `*`와 `/` 연산자로 직접 표현합니다.
1개의 **네이티브** 기본량(전류)과 기본 차원에서 **구성된** 2개의 양(전압과 저항)입니다.

## 이 주제의 단위

| 단위 | 유형 | 기준 단위 | 페이지 |
|---|---|---|---|
| 전류 | 네이티브 | 암페어(`A`) | [전류](ec.md) |
| 전압 | 구성 | 볼트(`V`) | [전압](voltage.md) |
| 저항 | 구성 | 옴(`Ω`) | [저항](resistance.md) |

## 타입 지정 연산자로서의 옴의 법칙

| 식 | 결과 | 공식 |
|---|---|---|
| `resistance * current` | 전압 | `U = R · I` |
| `current * resistance` | 전압 | `U = R · I`(교환 가능) |
| `voltage / current` | 저항 | `R = U / I` |
| `voltage / resistance` | 전류 | `I = U / R` |

각 결과는 올바른 타입의 양이 됩니다 — 원시 혼합 단위를 손으로 조립하지 않습니다. 또한 전압과 저항은
완전히 **네이티브**한 분해(`kg·m²·s⁻³·A⁻¹` 및 `kg·m²·s⁻³·A⁻²`)를 `toVoltage()` / `toResistance()`로
인식합니다.

## 실전 예제 — 한 회로에서의 옴의 법칙

부하가 **2 A**를 끌어당기면서 **230 V**를 강하시킵니다. 저항은 `R = U / I`이며, 그 저항에 전류를 다시
넣으면 전압 `U = R · I`를 재현합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

val r = (230 of volts) / (2 of amperes)   // KResistanceUnitInstance, 115 Ω
r into ohms                               // 115.0

val u = r * (2 of amperes)                // KVoltageUnitInstance
u into volts                              // 230.0

val i = (230 of volts) / (115 of ohms)    // KElectricCurrentUnitInstance
i into amperes                            // 2.0
```

## 값 출력(`toString`)

`toString()`은 값을 해당 그룹의 **기준 단위**(값 + 기호)로 출력합니다. 다른 단위는 문자열 템플릿 안에서
`into`로 읽고 기호를 직접 붙이세요:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u.toString()               // "230.0 V" (기준 단위)
"${u into kilo.volts} kV"  // "0.23 kV"
```

## 표기법

아래 표는 옴의 법칙을 수학 표기와 KUnit의 Kotlin 표기로 대비합니다. 지수는 유니코드 위 첨자
(`²`, `⁻¹`), `·`는 곱셈, `/`는 분수를 나타냅니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `R = U / I` | `(230 of volts) / (2 of amperes)` | 전압÷전류에서 저항 |
| `U = R · I` | `r * (2 of amperes)` | 저항×전류에서 전압 |
| `I = U / R` | `(230 of volts) / (115 of ohms)` | 전압÷저항에서 전류 |
| `Ω = kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | 네이티브 정규형으로서의 저항 |

## 다음에 볼 것

* [전류](ec.md) — 네이티브 암페어 그룹(및 CGS의 비오와 스탯암페어).
* [전압](voltage.md) — 볼트와 그 분해 `R · I` 및 네이티브 형식.
* [저항](resistance.md) — 옴, `U / I`, 그리고 역 옴의 법칙 연산자.
