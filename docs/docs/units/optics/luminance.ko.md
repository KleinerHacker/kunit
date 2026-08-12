# 휘도

패키지: `org.pcsoft.framework.kunit.optic.luminance`
기본 단위: **칸델라 매 제곱미터**(`KLuminanceUnit.BASE == KLuminanceUnit.CANDELA_PER_SQUARE_METER`)

종류: **구성 단위**

휘도 `L`은 **발광 면적당** 광도입니다: `L = I / A`, 즉 `1 cd/m² = 1 nit`. 이는 눈이 실제로 표면의
"밝기"로 지각하는 물리량이며, 모든 디스플레이 사양에 표시되는 수치입니다 — 일반적인 사무용 모니터는
250~350니트, HDR TV는 1000니트 이상입니다.

이 물리량의 정준 기본 차원 표준형은 `luminousIntensity¹ · distance⁻²`입니다.

## 단위

| 단위                     | 열거값                                | 기호   |                    토큰 | cd/m² 1단위당 |
|--------------------------|---------------------------------------------|----------|-------------------------:|----------------:|
| 칸델라 매 제곱미터 | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` | `candelasPerSquareMeter` |             1.0 |
| 니트                      | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` |                   `nits` |             1.0 |
| 스틸브                    | `KLuminanceUnit.STILB`                    | `sb`     |                 `stilbs` |          10 000 |
| 아포스틸브                 | `KLuminanceUnit.APOSTILB`                 | `asb`    |              `apostilbs` |           1 / π |
| 램버트                  | `KLuminanceUnit.LAMBERT`                  | `L`      |               `lamberts` |        10⁴ / π  |
| 풋램버트             | `KLuminanceUnit.FOOT_LAMBERT`             | `fL`     |           `footLamberts` |      ≈ 3.426259 |

`nits`는 기본 단위의 또 다른 표기이며 독자적인 단위가 아닙니다 — 이는 디스플레이 업계가 칸델라 매
제곱미터를 부르는 이름입니다. 아포스틸브, 램버트, 풋램버트는 *람베르시안* 계열에 속하며, 이상적인
확산 발광체의 조도를 휘도로 변환하는 계수 `1/π`를 포함합니다. 모든 토큰은 모든 SI 접두사를 사용할 수
있습니다.

## 분해

이 그룹에는 **두 가지** 분해가 있습니다. 두 형식 모두 동일한 정규화 팩토리로 수렴하므로 동일한 타입이
지정된 값이 같은 인스턴스를 만들어 냅니다.

| 형식                   | 표현식                                                     |
|------------------------|------------------------------------------------------------------------|
| 타입이 지정된 연산자A       | `luminousIntensity / area`                                     |
| 타입이 지정된 연산자B       | `illuminance / solidAngle`                                     |
| 네이티브 형식(`toX()`)       | `((250 of candelas).toUnit() / area.toUnit()).toLuminance()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val squareMeter = (1 of meters) * (1 of meters)

val viaIntensity  = (250 of candelas) / squareMeter      // A
val viaIlluminance = (500 of lux) / (2 of steradians)    // B
val native = ((250 of candelas).toUnit() / squareMeter.toUnit()).toLuminance()

viaIntensity == viaIlluminance   // true
viaIntensity == native           // true
viaIntensity into nits           // 250.0
```

## 그룹으로 계산하기

| 표현식                     | 결과 타입                      | 의미                    |
|--------------------------------|-----------------------------------|-----------------------------|
| `luminousIntensity / area`     | `KLuminanceUnitInstance`         | `L = I / A`                |
| `illuminance / solidAngle`     | `KLuminanceUnitInstance`         | `L = E / Ω`                |
| `luminance * area`             | `KLuminousIntensityUnitInstance` | `I = L · A`                |
| `luminance * solidAngle`       | `KIlluminanceUnitInstance`       | `E = L · Ω`                |
| `luminousIntensity / luminance` | `KAreaUnitInstance`             | 발광 면적          |
| `illuminance / luminance`      | `KSolidAngleUnitInstance`        | 빛이 퍼지는 원뿔각 |

## 실전 예제 — 모니터의 니트 등급

**0.21 m²** 패널을 가진 27인치 모니터의 정격은 **300니트**입니다. 이는 축상 전체 광도로 환산하면
다음과 같습니다.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val panel = (0.6 of meters) * (0.35 of meters)   // ≈ 0.21 m²
val l = 300 of nits

val i = l * panel                                 // KLuminousIntensityUnitInstance
i into candelas                                   // 63.0 cd

l into footLamberts                               // ≈ 87.6（영국식 단위 값）
```

## 값 시맨틱

`equals`/`hashCode`는 **정규화된 cd/m² 값**을 비교하므로 `(1 of stilbs) == (10000 of candelasPerSquareMeter)`입니다.
`toString()`은 값을 기본 단위로 표시합니다: `"250.0 cd/m^2"`.

## 관련 항목

* [광도](luminous-intensity.ko.md) — 휘도의 분자.
* [조도](illuminance.ko.md) — 표면을 떠나는 빛이 아니라 표면에 도달하는 빛.
* [복사휘도](radiance.ko.md) — 방사량 체계의 대응 물리량.
* [광학 개요](overview.ko.md)
</content>
