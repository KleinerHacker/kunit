# 압축률

패키지: `org.pcsoft.framework.kunit.mechanic.compressibility`
기본 단위: **파스칼의 역수**
(`KCompressibilityUnit.BASE == KCompressibilityUnit.RECIPROCAL_PASCAL`)

유형: **구성 단위**

압축률 `κ = −(1/V)·(∂V/∂p)`는 압력 단위당 재료의 부피가 얼마나 수축하는지를 나타냅니다.
이는 탄성 계수이자 [압력](pressure.ko.md)인 **체적 탄성률** `K`의 정확한 역수입니다. 물은 약
4.5 × 10⁻¹⁰ Pa⁻¹로, 이 때문에 유체역학에서는 물을 비압축성으로 취급할 수 있습니다.

정준 기저 차원 표준형은 `mass⁻¹ · length · time²`입니다.

## 명명된 단위

| 단위                             | 기호    |                   토큰 | 1 단위 (1/Pa 기준) |
|-----------------------------------|---------|------------------------:|---------------:|
| 파스칼의 역수                       | `1/Pa`  |     `reciprocalPascals` |            1.0 |
| 바의 역수                          | `1/bar` |        `reciprocalBars` |           1e-5 |
| 표준 대기압의 역수                  | `1/atm` | `reciprocalAtmospheres` |      1/101 325 |

모든 토큰은 모든 SI 접두사를 받아들입니다 (`pico.reciprocalPascals` 등). 인접한 압력 그룹과
마찬가지로 이 인스턴스는 **그램 기반의 원시 성분 값**을 저장합니다.

## 그룹으로 계산하기

| 표현식                          | 결과 타입                          | 의미                              |
|------------------------------------|---------------------------------------|--------------------------------------|
| `1 / pressure`                    | `KCompressibilityUnitInstance`       | `κ = 1 / K`                          |
| `1 / compressibility`             | `KPressureUnitInstance`              | `K = 1 / κ`                          |
| `compressibility * pressure`      | `Double`                             | 상대 부피 변화량 `ΔV/V`              |

이 두 역수는 정확합니다: 성분의 기본 단위 (압력의 `g·m⁻¹·s⁻²`와 여기서의 `g⁻¹·m·s²`)는
서로의 역수이므로 별도의 연결 계수가 필요하지 않습니다.

## 실제 사례 — 물은 얼마나 압축되는가

물의 체적 탄성률은 약 **2.2 GPa**입니다. 그 압축률은 얼마이며, 10 MPa (대략 수심 1000 m에
해당) 압력에서 얼마나 수축할까요?

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.compressibility.*

val kappa = 1 / (2.2 of giga.pascals)          // KCompressibilityUnitInstance
kappa into reciprocalPascals                    // ≈ 4.545e-10

val shrink = kappa * (10 of mega.pascals)       // Double
shrink                                           // ≈ 0.00455 — 부피 손실 0.45 %

// 다시 체적 탄성률로
(1 / kappa) into giga.pascals                    // ≈ 2.2
```

## 값 의미론

`equals`/`hashCode`는 **정규화된 성분 값**을 비교하므로
`(1 of reciprocalBars) == (1e-5 of reciprocalPascals)`입니다. `toString()`은 기본 단위로
값을 표현합니다: `"1.0 1/Pa"`.

## 참고

* [압력](pressure.ko.md) — 역수 관계의 양 (체적 탄성률).
* [응력 및 탄성 계수](stress.ko.md) — 재료 특성으로 읽히는 동일한 타입.
* [역학 개요](overview.ko.md)
