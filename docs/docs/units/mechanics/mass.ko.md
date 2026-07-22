# 질량

패키지: `org.pcsoft.framework.kunit.mass`
기준 단위: **그램** (`KMassUnit.BASE == KMassUnit.GRAM`)

유형: **네이티브 단위**

질량 그룹은 질량의 양을 모델링합니다. 거리 그룹처럼 지수 특화 하위 타입도 없고 시간 그룹처럼 `Duration`
백킹도 없는 **단순한 1차원** 그룹입니다. `KMassUnitInstance` 는 단일 `KMassUnit.GRAM` 항을 감싸며 항상
그램으로 정규화하여 저장합니다.

기준 단위는 의도적으로 **킬로그램이 아니라 그램** 입니다. 킬로그램은 전용 단위가 아니라 단순히 SI 접두사
`kilo` 를 그램에 적용한 `kilo.grams` 입니다. 그램의 모든 십진 자릿수(밀리그램, 킬로그램 등)는 SI 접두사를
통해 동일한 일반적 방식으로 얻어집니다.

## 단위

| 체계 | 단위 | 열거값 | 기호 | 토큰 | 1 단위의 그램 값 |
|---|---|---|---|---:|---:|
| 미터법 | 그램 | `KMassUnit.GRAM` | `g` | `grams` | 1.0 |
| 미터법 | 톤(메트릭 톤) | `KMassUnit.TONNE` | `t` | `tonnes` | 1 000 000 |
| 미터법 | 캐럿(미터) | `KMassUnit.CARAT` | `ct` | `carats` | 0.2 |
| 상형 | 그레인 | `KMassUnit.GRAIN` | `gr` | `grains` | 0.06479891 |
| 상형 | 드램 | `KMassUnit.DRAM` | `dr` | `drams` | 1.7718451953125 |
| 상형 | 온스 | `KMassUnit.OUNCE` | `oz` | `ounces` | 28.349523125 |
| 상형 | 파운드 | `KMassUnit.POUND` | `lb` | `pounds` | 453.59237 |
| 상형 | 스톤 | `KMassUnit.STONE` | `st` | `stones` | 6350.29318 |
| 상형 | 헌드레드웨이트 US(숏) | `KMassUnit.HUNDREDWEIGHT_US` | `cwt(US)` | `hundredweightsUS` | 45 359.237 |
| 상형 | 헌드레드웨이트 UK(롱) | `KMassUnit.HUNDREDWEIGHT_UK` | `cwt(UK)` | `hundredweightsUK` | 50 802.34544 |
| 상형 | 숏 톤(US) | `KMassUnit.SHORT_TON` | `ton(US)` | `shortTons` | 907 184.74 |
| 상형 | 롱 톤(UK) | `KMassUnit.LONG_TON` | `ton(UK)` | `longTons` | 1 016 046.9088 |
| 상형 | 슬러그 | `KMassUnit.SLUG` | `slug` | `slugs` | 14 593.90294 |
| 트로이 | 페니웨이트 | `KMassUnit.PENNYWEIGHT` | `dwt` | `pennyweights` | 1.55517384 |
| 트로이 | 트로이 온스 | `KMassUnit.TROY_OUNCE` | `oz t` | `troyOunces` | 31.1034768 |
| 트로이 | 트로이 파운드 | `KMassUnit.TROY_POUND` | `lb t` | `troyPounds` | 373.2417216 |
| 역사 | 독일 파운드 | `KMassUnit.GERMAN_POUND` | `Pfd` | `germanPounds` | 500 |
| 역사 | 첸트너 | `KMassUnit.ZENTNER` | `Ztr` | `zentners` | 50 000 |
| 역사 | 로트 | `KMassUnit.LOT` | `Lot` | `lots` | 16.6666667 |
| 지역 | 근(斤) | `KMassUnit.JIN` | `斤` | `jin` | 500 |
| 지역 | 량(两) | `KMassUnit.LIANG` | `两` | `liang` | 50 |
| 지역 | 몬메(匁) | `KMassUnit.MOMME` | `匁` | `momme` | 3.75 |
| 지역 | 관(貫) | `KMassUnit.KAN` | `貫` | `kan` | 3750 |
| 과학 | 돌턴(u) | `KMassUnit.DALTON` | `Da` | `daltons` | 1.6605390666e-24 |

각 `토큰` 은 `of`(생성)와 `into`(읽기)에 사용하는 값 1 의 `KMassUnitInstance` 입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

val m = 2 of kilo.grams      // 2000 g(킬로그램은 `kilo.grams`)
m.value                      // 2000.0(그램으로 정규화)
m into pounds                // ≈ 4.409(파운드로 읽기)
(1 of pounds) into grams     // 453.59237
```

## 연산자

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

// + / - : 같은 그룹, 단위 간 자동 변환
val a = (1 of kilo.grams) + (500 of grams)   // KMassUnitInstance: 1500.0 g
val b = (1 of kilo.grams) - (500 of grams)   // KMassUnitInstance: 500.0 g

// 비교
(1 of kilo.grams) == (1000 of grams)         // true(정규화된 양이 같음)
(1 of kilo.grams) > (500 of grams)           // true
```

### 비교와 동등성

`==`, `!=`, `<`, `<=`, `>`, `>=` 는 두 `KMassUnitInstance` 의 정규화된 `value`(그램)를 비교합니다.
`equals` 는 정규화된 양 기준이므로 `(1 of kilo.grams) == (1000 of grams)` 입니다.

## `pow` 로 거듭제곱

중위 연산자 `pow` 로 정수 거듭제곱을 수행합니다(Kotlin 에는 오버로드 가능한 `^` 가 없습니다). 질량 그룹에서
`pow` 는 일반 `KMixedUnitInstance` 를 반환합니다(질량에는 차원이 있는 거듭제곱 타입이 없습니다):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mass.*

val squared = (2 of grams) pow 2     // KMixedUnitInstance: 4.0 g²
```

## SI 접두사

질량은 **모든** 자릿수를 허용하므로 모든 SI 접두사 빌더(`quetta` … `quecto`)를 프로퍼티 접근으로 모든 질량
단위와 결합할 수 있습니다. 킬로그램은 정확히 `kilo.grams`, 밀리그램은 `milli.grams` 입니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).value    // 1000.0     (킬로그램)
(1 of milli.grams).value   // 0.001      (밀리그램)

(2500 of grams) into kilo.grams  // 2.5
```

## toString 형식화

기준 단위 `toString()` 만 존재합니다. 특정 단위로 형식화하려면 `into` 를 사용하세요:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).toString()             // "1000.0 g"(기준 단위 표현)
"${(2000 of grams) into kilo.grams} kg"  // "2.0 kg"
```

## 표기법

아래 표는 이 단위와 그 구성 요소를 수학적으로 어떻게 쓰는지, 그리고 KUnit을 사용해 Kotlin에서 어떻게 쓰는지를 비교합니다. 지수는 유니코드 위 첨자(`²`, `³`, `⁻¹`)로 표기하며, `·`는 곱셈, `/`는 분수를 나타냅니다. 하나의 양을 분수로도, 음의 지수를 사용한 곱으로도 쓸 수 있는 경우 두 가지 동등한 Kotlin 형식을 함께 표시합니다.

| 수학 | Kotlin | 의미 |
|---|---|---|
| `g` | `grams` | 질량, 기본 단위(그램) |
| `kg` | `kilo.grams` | 킬로그램(그램에 접두사 적용) |
| `mg` | `milli.grams` | 밀리그램 |
| `g²` | `grams pow 2` | 그램 제곱(일반 혼합 단위) |
