# 전류

패키지: `org.pcsoft.framework.kunit.ec`
기준 단위: **암페어** (`KElectricCurrentUnit.BASE == KElectricCurrentUnit.AMPERE`)

전류 그룹은 전류를 모델링합니다. 이는 **단순한 1차원** 네이티브 그룹이며(지수 특화 하위 타입도, 교차 단위
타입 결과도 없음), `KElectricCurrentUnitInstance` 는 단일 `KElectricCurrentUnit.AMPERE` 항을 감싸고 항상
암페어로 정규화하여 저장합니다.

SI 암페어 외에도 이 그룹은 두 가지 고전적인 CGS 전류 단위를 제공합니다: 전자기 단위계(EMU)의
**비오**(abampere, `1 Bi = 10 A`)와 정전기 단위계(ESU)의 **스탯암페어**(`1 statA ≈ 3.335 641 × 10⁻¹⁰ A`)입니다.

## 단위

| 계열 | 단위 | 열거값 | 기호 | 토큰 | 1 단위의 암페어 값 |
|---|---|---|---|---:|---:|
| SI | 암페어 | `KElectricCurrentUnit.AMPERE` | `A` | `amperes` | 1.0 |
| CGS | 비오 / abampere | `KElectricCurrentUnit.BIOT` | `Bi`(`abA`) | `biot` / `abamperes` | 10 |
| CGS | 스탯암페어 | `KElectricCurrentUnit.STATAMPERE` | `statA` | `statamperes` | 3.335641e-10 |

각 `토큰` 은 `of`(생성)와 `into`(읽기)에 사용하는 값 1 의 `KElectricCurrentUnitInstance` 입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val i = 2 of milli.amperes    // 0.002 A
i.value                       // 0.002 (암페어로 정규화)
i into amperes                // 0.002 (암페어로 다시 읽기)
(1 of biot) into amperes      // 10.0
```

## 실제 예시

옴의 법칙: `R = 220 Ω` 저항 양단에 `U = 5 V` 를 걸면 전류 `I = U / R` 가 흐릅니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val voltage = 5.0       // V
val resistance = 220.0  // Ω
val current = (voltage / resistance) of amperes   // ≈ 0.0227 A
current into milli.amperes                         // ≈ 22.7 mA
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.ec.*

// + / - : 같은 그룹, 단위 간 자동 변환
val a = (1 of amperes) + (1 of biot)   // KElectricCurrentUnitInstance: 11.0 A
val b = (1 of biot) - (1 of amperes)   // KElectricCurrentUnitInstance: 9.0 A

// 비교
(1 of biot) == (10 of amperes)         // true (정규화된 양이 동일)
(1 of biot) > (1 of amperes)           // true
```

### 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=` 는 두 `KElectricCurrentUnitInstance` 의 정규화된 `value`(암페어)를
비교합니다. `equals` 는 정규화된 양 기준이므로 `(1 of biot) == (10 of amperes)` 입니다.

## `pow` 를 이용한 거듭제곱

중위 연산자 `pow` 로 정수 거듭제곱을 계산합니다(Kotlin 에는 오버로드 가능한 `^` 가 없습니다). 전류
그룹에서 `pow` 는 일반 `KMixedUnitInstance` 를 반환합니다(전류에는 차원이 있는 거듭제곱 타입이 없음):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.ec.*

val squared = (2 of amperes) pow 2     // KMixedUnitInstance: 4.0 A²
```

## SI 접두사

전류는 **임의의** 크기를 허용하므로, 모든 SI 접두사 빌더(`quetta` … `quecto`)를 프로퍼티 접근으로 각 전류
단위와 조합할 수 있습니다. 밀리암페어는 `milli.amperes`, 킬로암페어는 `kilo.amperes` 입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of milli.amperes).value   // 0.001      (밀리암페어)
(1 of kilo.amperes).value    // 1000.0     (킬로암페어)

(2500 of amperes) into kilo.amperes  // 2.5
```

## toString 형식

기준 단위 `toString()` 만 존재합니다. 특정 단위로 형식화하려면 `into` 를 사용하세요:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of biot).toString()                       // "10.0 A" (기준 단위 표현)
"${(0.002 of amperes) into milli.amperes} mA" // "2.0 mA"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학 표기와 KUnit 의 Kotlin 표기로 어떻게 쓰는지 보여줍니다. 지수는
유니코드 위첨자(`²`, `³`, `⁻¹`)를 사용하며, `·` 는 곱셈, `/` 는 분수를 나타냅니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `A` | `amperes` | 전류, 기준 단위(암페어) |
| `mA` | `milli.amperes` | 밀리암페어(암페어에 접두사 적용) |
| `kA` | `kilo.amperes` | 킬로암페어 |
| `Bi` | `biot` | 비오 / abampere(10 A) |
| `A²` | `amperes pow 2` | 암페어 제곱(일반 혼합 단위) |
