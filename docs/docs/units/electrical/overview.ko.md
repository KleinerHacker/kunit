# 전기공학 — 개요

패키지: `org.pcsoft.framework.kunit.ec`, `…voltage`, `…resistance`, `…charge`, `…conductance`,
`…magneticfieldstrength`, `…capacitance`, `…inductance`, `…magneticflux`, `…magneticfluxdensity`,
`…currentdensity`, `…chargedensity`, `…resistivity`, `…conductivity`, `…power`, `…energy`

전기공학은 회로를 흐르는 전류, 그것을 구동하는 전압, 그리고 그것을 방해하는 저항을 함께 묶습니다. 이
셋은 **옴의 법칙**으로 연결되며, KUnit은 그 법칙을 타입이 지정된 `*`와 `/` 연산자로 직접 표현합니다.
1개의 **네이티브** 기본량(전류)과 기본 차원에서 **구성된** 양들(전압, 저항, 전하, 컨덕턴스, 자기장
세기)입니다.

## 이 주제의 단위

| 단위 | 유형 | 기준 단위 | 페이지 |
|---|---|---|---|
| 전류 | 네이티브 | 암페어(`A`) | [전류](ec.md) |
| 전압 | 구성 | 볼트(`V`) | [전압](voltage.md) |
| 저항 | 구성 | 옴(`Ω`) | [저항](resistance.md) |
| 전하 | 구성 | 쿨롬(`C`) | [전하](charge.md) |
| 컨덕턴스 | 구성 | 지멘스(`S`) | [컨덕턴스](conductance.md) |
| 자기장 세기 | 구성 | 미터당 암페어(`A/m`) | [자기장 세기](magneticfieldstrength.md) |
| 정전용량 | 구성 | 패럿(`F`) | [정전용량](capacitance.md) |
| 인덕턴스 | 구성 | 헨리(`H`) | [인덕턴스](inductance.md) |
| 자속 | 구성 | 베버(`Wb`) | [자속](magneticflux.md) |
| 자속밀도 | 구성 | 테슬라(`T`) | [자속밀도](magneticfluxdensity.md) |
| 전류밀도 | 구성 | 제곱미터당 암페어(`A/m²`) | [전류밀도](currentdensity.md) |
| 전하밀도 | 구성 | 세제곱미터당 쿨롬(`C/m³`) | [전하밀도](chargedensity.md) |
| 저항률 | 구성 | 옴미터(`Ω·m`) | [저항률](resistivity.md) |
| 전도율 | 구성 | 미터당 지멘스(`S/m`) | [전도율](conductivity.md) |
| 전력 | 구성 | 와트(`W`) | [전력(전기)](power.md) |
| 에너지 | 구성 | 줄(`J`) | [에너지(전기)](energy.md) |

전력과 에너지는 각각 기술적으로 **하나**의 양이며, 다른 주제 분야와 공유됩니다. 이들은 분야별로
문서화되며 서로 참조합니다([전력(역학)](../mechanics/power.md),
[전력(열역학)](../thermodynamics/power.md), [에너지(역학)](../mechanics/energy.md),
[에너지(열역학)](../thermodynamics/energy.md)).

## 타입 지정 연산자로서의 옴의 법칙

| 식 | 결과 | 공식 |
|---|---|---|
| `resistance * current` | 전압 | `U = R · I` |
| `current * resistance` | 전압 | `U = R · I`(교환 가능) |
| `voltage / current` | 저항 | `R = U / I` |
| `voltage / resistance` | 전류 | `I = U / R` |
| `current / voltage` | 컨덕턴스 | `G = I / U` |
| `1 / resistance` | 컨덕턴스 | `G = 1 / R` |
| `1 / conductance` | 저항 | `R = 1 / G` |
| `conductance * voltage` | 전류 | `I = G · U` |
| `current / conductance` | 전압 | `U = I / G` |

## 그 밖의 타입 지정 연산자

| 식 | 결과 | 공식 |
|---|---|---|
| `current * time` | 전하 | `Q = I · t` |
| `current / frequency` | 전하 | `Q = I / f` |
| `charge / time` | 전류 | `I = Q / t` |
| `charge / current` | 시간 | `t = Q / I` |
| `current / length` | 자기장 세기 | `H = I / l` |
| `field strength * length` | 전류 | `I = H · l` |
| `charge / voltage` | 정전용량 | `C = Q / U` |
| `capacitance * voltage` | 전하 | `Q = C · U` |
| `voltage * time` | 자속 | `Φ = U · t` |
| `flux / time` | 전압 | `U = Φ / t` |
| `flux / current` | 인덕턴스 | `L = Φ / I` |
| `inductance * current` | 자속 | `Φ = L · I` |
| `resistance / frequency` | 인덕턴스 | `L = X / ω` |
| `flux / area` | 자속밀도 | `B = Φ / A` |
| `flux density * area` | 자속 | `Φ = B · A` |
| `current / area` | 전류밀도 | `J = I / A` |
| `current density * area` | 전류 | `I = J · A` |
| `charge / volume` | 전하밀도 | `ρ = Q / V` |
| `charge density * volume` | 전하 | `Q = ρ · V` |
| `resistance * length` | 저항률 | `ρ = R · A / l` |
| `1 / resistivity` | 전도율 | `σ = 1 / ρ` |
| `1 / conductivity` | 저항률 | `ρ = 1 / σ` |
| `conductance / length` | 전도율 | `σ = G · l / A` |
| `conductivity * length` | 컨덕턴스 | `G = σ · A / l` |
| `voltage * current` | 전력 | `P = U · I` |
| `power / voltage` | 전류 | `I = P / U` |
| `power / current` | 전압 | `U = P / I` |
| `power * time` | 에너지 | `W = P · t` |
| `energy / time` | 전력 | `P = W / t` |
| `charge * voltage` | 에너지 | `W = Q · U` |
| `energy / charge` | 전압 | `U = W / Q` |

각 결과는 올바른 타입의 양이 됩니다 — 원시 혼합 단위를 손으로 조립하지 않습니다. 또한 전압, 저항, 전하,
컨덕턴스, 자기장 세기는 완전히 **네이티브**한 분해(`kg·m²·s⁻³·A⁻¹`, `kg·m²·s⁻³·A⁻²`, `A·s`,
`kg⁻¹·m⁻²·s³·A²`, `A·m⁻¹`)를 `toVoltage()` / `toResistance()` / `toCharge()` / `toConductance()` /
`toMagneticFieldStrength()`로 인식합니다. 더 새로운 그룹들도 마찬가지입니다: `toCapacitance()`
(`kg⁻¹·m⁻²·s⁴·A²`), `toInductance()`(`kg·m²·s⁻²·A⁻²`), `toMagneticFlux()`(`kg·m²·s⁻²·A⁻¹`),
`toMagneticFluxDensity()`(`kg·s⁻²·A⁻¹`), `toCurrentDensity()`(`A·m⁻²`), `toChargeDensity()`
(`A·s·m⁻³`), `toResistivity()`(`kg·m³·s⁻³·A⁻²`), `toConductivity()`(`kg⁻¹·m⁻³·s³·A²`), `toPower()`
(`kg·m²·s⁻³`), `toEnergy()`(`kg·m²·s⁻²`).

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

## 실전 예제 — 상용 전력에서 소비 에너지까지

**230 V** 콘센트가 **10 A** 부하에 전력을 공급하면 `P = U · I`가 됩니다. 3시간 동안 가동하면
`W = P · t`만큼 소비합니다:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val p = (230 of volts) * (10 of amperes)  // KPowerUnitInstance
p into kilo.watts                         // 2.3

val w = p * (3 of hours)                  // KEnergyUnitInstance
w into kilo.joules                        // 24840.0
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
* [전하](charge.md) — 쿨롬, `I · t`, 그리고 배터리 용량 단위 암페어시.
* [컨덕턴스](conductance.md) — 지멘스, `1 / R`, 그리고 `I / U`.
* [자기장 세기](magneticfieldstrength.md) — 미터당 암페어, `I / l`, 그리고 외르스테드.
* [정전용량](capacitance.md) — 패럿, `Q / U`, 그리고 CGS의 앱패럿/스탯패럿.
* [인덕턴스](inductance.md) — 헨리, `Φ / I`, 그리고 리액턴스 형태 `X / ω`.
* [자속](magneticflux.md) — 베버, `U · t`, 그리고 맥스웰.
* [자속밀도](magneticfluxdensity.md) — 테슬라, `Φ / A`, 그리고 가우스.
* [전류밀도](currentdensity.md) — 제곱미터당 암페어, `I / A`, 전선 규격 산정용.
* [전하밀도](chargedensity.md) — 세제곱미터당 쿨롬, `Q / V`.
* [저항률](resistivity.md) — 옴미터, `R · A / l`, 저항의 배후에 있는 재료 물성.
* [전도율](conductivity.md) — 미터당 지멘스, `1 / ρ`, 그리고 `G · l / A`.
* [전력(전기)](power.md) — 와트, `U · I`, 그리고 마력 단위.
* [에너지(전기)](energy.md) — 줄, `Q · U`, `P · t`, 그리고 `kilo.watts * hours`로 표현되는 킬로와트시.
