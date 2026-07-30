# 엔트로피 (Entropy)

패키지: `org.pcsoft.framework.kunit.thermo.heatcapacity`
기본 단위: **켈빈당 줄** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

유형: **구성된 단위**

엔트로피 `S`는 시스템 내 에너지의 분산을 측정합니다. 그 단위는 `J/K`이며, [열용량](heat-capacity.md)과
**차원적으로 동일**합니다.

## 엔트로피에 자체 타입이 없는 이유

KUnit은 의도적으로 엔트로피를 별도의 `KEntropyUnitInstance`가 아닌 `KHeatCapacityUnitInstance`로
모델링합니다. 그 이유는 이 라이브러리의 형식 인식 계약 때문입니다:

* 모든 표준화된 그룹은 **하나**의 정규 기저 차원 형식을 가지며,
* `toX()`는 정확히 그 형식만 인식합니다.

엔트로피와 열용량은 정규 형식 `mass¹ · distance² · time⁻² · temperature⁻¹`을 공유합니다. 하나의 정규
형식에 두 개의 타입을 두면 네이티브 표현식이 모호해집니다 — `toHeatCapacity()`와 가상의
`toEntropy()`가 모두 같은 혼합 단위와 일치하게 되고, 어느 쪽도 다른 쪽보다 더 옳다고 할 수 없습니다.
단일 타입은 왕복 변환을 결정론적으로 유지합니다.

따라서 두 양의 구분은 라이브러리가 어떤 타입을 건네주는지가 아니라 *변수 이름을 무엇으로 지었는지*의
문제입니다 — 물리학 표기에서 둘 다 J/K로 쓰이는 것과 정확히 같습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val entropyChange = 21.0 of joulesPerKelvin   // ΔS
val heatCapacity = 4184 of joulesPerKelvin    // C
// 둘 다 KHeatCapacityUnitInstance
```

## 실전 예제: 얼음 녹이기

273.15 K에서 1 kg의 얼음을 녹이면 334 kJ의 잠열이 흡수됩니다. 엔트로피 변화는 `ΔS = Q / T`입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val latentHeat = 334 of kilo.joules
val meltingPoint = KTemperatureDifference.ofKelvin(273.15) // 절대 영도로부터의 구간으로

val entropyChange = latentHeat / meltingPoint  // KHeatCapacityUnitInstance, J/K 단위
entropyChange into joulesPerKelvin             // ≈ 1222.8 J/K

// 역방향: 그 엔트로피 변화는 녹는점에서 얼마의 열을 나르는가?
(entropyChange * meltingPoint) into kilo.joules // 334.0 kJ
```

!!! note "`ΔS = Q / T`에서의 절대 온도"
    엔트로피는 **절대** 온도로 나뉘지만, 이 라이브러리의 몫 연산은 온도 *차* 그룹
    (`KTemperatureDifferenceUnit`)을 사용합니다 — 아핀 척도는 분모에 등장할 수 없기 때문입니다.
    위와 같이 절대 켈빈값을 절대 영도로부터의 구간으로 표현하세요:
    `KTemperatureDifference.ofKelvin(273.15)`. 켈빈 척도에서는 이 둘이 수치상 일치하는데, 바로 이 점이
    열역학이 켈빈 척도를 사용하는 이유입니다.

## 함께 보기

* [열용량](heat-capacity.md) — 엔트로피가 공유하는 타입으로, 전체 단위 표와 모든 분해 및 완전한
  연산자 표면을 포함
* [몰 열용량](molar-heat-capacity.md) — 몰당 형태(몰 엔트로피)
* [비열](specific-heat-capacity.md) — 킬로그램당 형태(비엔트로피)
* [에너지](energy.md) — `ΔS = Q / T`의 분자

## 표기법

아래 표는 이 양이 수학적으로 어떻게 표기되는지와 Kotlin/KUnit에서 어떻게 표기되는지를 보여줍니다.
지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)를 사용하며, `·`는 곱셈을, `/`는 분수를 나타냅니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `J/K` | `joulesPerKelvin` | 엔트로피, 기본 단위(열용량과 공유) |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | 기저 차원으로의 동일한 양 |
| `ΔS = Q / T` | `latentHeat / meltingPoint` | 열 ÷ 온도에서 엔트로피 변화 |
| `Q = ΔS · T` | `entropyChange * meltingPoint` | 엔트로피 변화 × 온도에서 열 |
