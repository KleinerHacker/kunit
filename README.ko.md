<p align="center">
  <img src="docs/docs/assets/images/logo.png" alt="KUnit logo" width="320">
</p>

# kunit

> 🌐 [English](README.md) · **한국어** · [中文](README.zh.md) · [日本語](README.ja.md)
>
> 전체 문서는 [GitHub Pages](https://kleinerhacker.github.io/kunit/)에서 네 가지 언어로도 제공됩니다
> ([EN](https://kleinerhacker.github.io/kunit/) ·
> [KO](https://kleinerhacker.github.io/kunit/ko/) ·
> [ZH](https://kleinerhacker.github.io/kunit/zh/) ·
> [JA](https://kleinerhacker.github.io/kunit/ja/)).

단순한 숫자 대신 실제 물리 단위를 `Double` 정밀도로 계산할 수 있게 해 주는, Kotlin(및 Java)용
단위 계산 프레임워크입니다.

## 체크아웃 & 빌드

```bash
git clone <repository-url>
cd kunit
```

이 프로젝트는 Gradle을 사용합니다(래퍼가 저장소에 포함되어 있으므로 별도의 Gradle 설치가 필요 없습니다):

```bash
# 빌드
./gradlew build          # Windows: gradlew.bat build

# 테스트만 실행
./gradlew test            # Windows: gradlew.bat test
```

툴체인 25를 해석할 수 있는 JDK가 필요합니다(`foojay-resolver` 플러그인이 필요 시 자동으로 다운로드합니다).

## 문서 사이트

📖 **[GitHub Pages에서 문서 보기](https://kleinerhacker.github.io/kunit/)**

전체 문서(개요, 빠른 시작, 혼합 단위, 사용자 정의 단위 추가, 미리 정의된 단위)는
[MkDocs Material](https://squidfunk.github.io/mkdocs-material/)로 작성되었으며,
[mkdocs-static-i18n](https://github.com/ultrabug/mkdocs-static-i18n)을 통해 영어, 한국어,
중국어, 일본어로 제공되고 라이트/다크 모드 전환을 지원합니다.

```bash
pip install -r docs/requirements.txt

# 라이브 리로드로 로컬 서빙
mkdocs serve

# 정적 사이트를 ./site 로 빌드
mkdocs build
```

## 아키텍처

* **`KMixedUnitInstance`** - *혼합 단위*를 나타냅니다: 정규화된 `Double` 기준값과, 각각 지수(양수 = 분자,
  음수 = 분모)와 결합된 `KUnit` 집합으로 구성되며 서로 곱해진 것으로 간주됩니다.
* **`KUnit`** - 하나의 "순수" 단위(기호 + 그룹 기준 단위로의 변환 계수)를 위한 인터페이스입니다.
  단위 그룹별로 `enum class ... : KUnit`(예: `KLengthUnit`)로 구현됩니다.
* **래퍼 클래스**(예: `KLengthUnitInstance`) - 특정 그룹을 위해 `KMixedUnitInstance`를 위임으로 캡슐화하며,
  값을 항상 해당 그룹의 기준 단위로 정규화하여 보관합니다. 지수 1에 국한되지 않고 같은 그룹의 파생량
  (예: 넓이 = 길이², 부피 = 길이³)도 다룹니다.
* **`KUnitPrefix`** - 완전한 SI 접두어 표(Quetta/Q ~ Quecto/q)를 담은 루트 패키지의 제네릭 열거형입니다.
  접두어는 `KUnit` 자체의 일부가 아니라 값의 읽기/쓰기 시에만 의미가 있으며, 그룹별 `infix` 함수
  (예: `5 kilo meters`)로 결합되어 해당 그룹의 구체 단위를 바로 반환합니다(`5 kilo meters` 는
  `KLengthUnitInstance`, `5000.meters` 와 동일).
* **특수 단위**(`KDerivedUnit` / `KScaledDerivedUnit`) - 고유한 이름/기호를 가진, 그룹 및 지수에 종속된
  추가 변환 대상(예: 넓이의 헥타르, 부피의 리터)으로, 기본 메커니즘을 대체하지 않고 보완합니다.

```mermaid
classDiagram
    class KUnit {
        <<interface>>
        +symbol: String
        +baseValue: Double
    }
    class KMixedUnitInstance {
        +value: Double
        +units: List~KUnitTerm~
        +valueAs(...)
        +toString()
        +plus() minus() times() div()
    }
    class KUnitTerm {
        +unit: KUnit
        +exponent: Int
    }
    class KUnitPrefix {
        <<enum>>
        Quetta ... Quecto
    }
    class KDerivedUnit {
        <<interface>>
        +referenceUnit: KUnit
        +exponent: Int
        +baseValue: Double
    }

    KMixedUnitInstance "1" o-- "many" KUnitTerm
    KUnitTerm --> KUnit
    KDerivedUnit --> KUnit : referenceUnit

    class KLengthUnit {
        <<enum>>
        METER, MILE, YARD, ...
    }
    class KLengthUnitInstance {
        +value: Double
        +valueAs(unit)
        +plus() minus() times() div()
    }
    class KLengthDerivedUnit {
        <<enum>>
        HECTARE, ARE, ACRE, LITER, ...
    }

    KUnit <|.. KLengthUnit
    KDerivedUnit <|.. KLengthDerivedUnit
    KLengthUnitInstance *-- KMixedUnitInstance : delegates to
    KLengthDerivedUnit --> KLengthUnit : referenceUnit
```

### 패키지 구조

* 루트 패키지 `org.pcsoft.framework.kunit`는 기본 타입 `KUnit`, `KMixedUnitInstance`, `KUnitPrefix`,
  `KDerivedUnit`, ... 를 포함합니다. 각 단위 하위 패키지는 자체 접두어 `infix` 함수(예:
  `KLengthUnitPrefix.kt`)를 추가로 선언합니다.
* 모든 "순수" 단위 그룹은 자체 하위 패키지(예: `org.pcsoft.framework.kunit.length`)를 가지며 고유한
  `KXxxUnit`, `KXxxUnitInstance`, `KXxxDerivedUnit` 및 관련 생성 확장 함수를 포함합니다.

### 연산자

* `+`, `-`, `*`, `/`는 순수 단위, 혼합 단위, 그리고 둘의 혼합에 대해 지원됩니다.
* `==`, `!=`, `<`, `<=`, `>`, `>=`는 순수 단위에 대해 지원됩니다. 혼합 단위는 추가로 순수 단위/지수
  확인 메서드(`hasSameUnits`)를 제공합니다.
* `+`/`-`는 같은 단위 그룹이면서 같은 지수(순수 단위)일 때, 또는 지수를 포함해 완전히 동일한 `KUnit`
  (혼합 단위)일 때만 허용됩니다 - 그렇지 않으면 `IllegalStateException`이 발생합니다.

## 이 프레임워크가 현재 지원하는 것

현재 구현 상태(자세한 내용은 [STATUS.md](STATUS.md) 참조):

### 루트 엔진

* 완전한 연산자 및 `toString` 변환을 갖춘 `KMixedUnitInstance`/`KUnitTerm` 혼합 단위 엔진
* `KUnitPrefix`를 통한 완전한 SI 접두어 표(24개 값, Quetta/Q ~ Quecto/q)
* 구체 단위를 바로 반환하는 그룹별 접두어 생성(`5 kilo meters`)
* 특수/파생 단위를 위한 제네릭 메커니즘(`KScaledUnit`, `KDerivedUnit`, `KScaledDerivedUnit`)

### 단위 그룹

| 그룹 | 하위 패키지 | 기준 단위 |
|---|---|---|
| 길이 | `org.pcsoft.framework.kunit.length` | 미터 (`KLengthUnit.BASE`) |

#### 길이 (`KLengthUnit`)

미터, 마일, 해리, 야드, 피트, 인치, 패덤, 체인, 펄롱, 천문단위, 광년, 파섹.

#### 다차원 지원 (지수 > 1)

`KLengthUnitInstance`는 `KLengthUnit.BASE`의 임의 지수를 캡슐화하며, 다음을 포함합니다:

* **지수 2 (넓이)** - 특수 단위 포함(`KLengthDerivedUnit`): 아르, 헥타르, 에이커
* **지수 3 (부피)** - 특수 단위 포함(`KLengthDerivedUnit`): 리터, 미국 갤런, 영국 갤런,
  미국 액량 온스, 오일 배럴

### 아직 미완성

* `length` 패턴을 따르는 추가 단위 그룹(예: 질량, 시간, 온도)
* 그 자체가 혼합 단위로 구성된 복합 "순수" 단위(예: 뉴턴)

## 빠른 시작

필요한 단위 그룹을 의존성으로 추가(또는 프로젝트/소스 세트로 포함)하고 어휘를 import 하세요.

### 길이

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

// 임의의 Number 타입에서 순수 길이 값 생성
val distance = 5.meters
val trip = 10.miles

// 연산자: 같은 그룹과 지수 내에서 자동 변환
val total = distance + trip          // KLengthUnitInstance, 미터로 정규화됨
val diff = trip - distance

// 비교
val isFarther = trip > distance      // true

// 특정 단위로 값 읽기
println(total.valueAs(KUnitPrefix.KILO with meters)) // 예: 21.0467...
println(total.valueAs(yards))         // 예: 23018.4...

// 순수 단위의 곱셈/나눗셈은 혼합 단위(KMixedUnitInstance)를 만듭니다
val area = distance.toKMixedUnitInstance() * trip.toKMixedUnitInstance()

// 넓이(지수 2)와 부피(지수 3)를 위한 특수 단위
val plot = 3.hectares
println(plot.valueAs(KLengthDerivedUnit.ARE))   // 300.0

val tank = 200.liters
println(tank.valueAs(KLengthDerivedUnit.US_GALLON))
```

### SI 접두어

```kotlin
import org.pcsoft.framework.kunit.length.kilo
import org.pcsoft.framework.kunit.length.meters

// "5 kilo meters" -> KLengthUnitInstance (direct, == 5000.meters)
val fiveKm = 5 kilo meters
println(fiveKm.value) // 5000.0 (미터로 정규화됨)
```

### 혼합 단위

```kotlin
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

// 혼합 단위를 수동으로 구성, 예: 초당 미터 (시간 그룹이 존재하면 length^1 * time^-1)
val speed = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val doubled = speed * speed // 지수가 더해짐 -> length^2
```
