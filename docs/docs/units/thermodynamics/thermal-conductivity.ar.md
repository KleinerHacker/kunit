# الموصلية الحرارية

الحزمة: `org.pcsoft.framework.kunit.thermo.conductivity`
الوحدة الأساسية: **واط لكل متر-كلفن** (`KThermalConductivityUnit.BASE == KThermalConductivityUnit.WATT_PER_METER_KELVIN`)

النوع: **وحدة مركّبة**

الموصلية الحرارية `λ` (تُسمّى أيضًا `k`) هي خاصّية المادّة في قانون فورييه: تساوي
[كثافة التدفّق الحراري](heat-flux-density.md) عبر مادّة موصليتها مضروبة في
[تدرّج درجة الحرارة](temperature-gradient.md). الوحدة: `W/(m·K)`.

يغلّف `KThermalConductivityUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود بالضبط بالصيغة
القياسية `mass¹ · distance¹ · time⁻³ · temperature⁻¹` (`kg·m·s⁻³·K⁻¹`)، مطبَّعًا دائمًا إلى
W/(m·K).

!!! note "اسم الحزمة مقابل اسم الصنف"
    الحزمة هي `thermo.conductivity`، وليست `thermo.thermalconductivity` — إذ يجب ألّا تُكرِّر حزمة
    الوحدة اسم حزمة مجالها. تحتفظ **الأصناف** بالمصطلح التقني الكامل
    (`KThermalConductivityUnitInstance`)، وهو ما يميّزها عن `electric.conductivity`.

وعند القسمة على سماكة تصبح [معامل انتقال الحرارة](heat-transfer-coefficient.md)؛ والسماكة مقسومة
عليها تعطي [المقاومة الحرارية](thermal-resistance.md) (قيمة R).

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ W/(m·K) |
|---|---|---:|---:|
| واط لكل متر-كلفن | `W/(m·K)` | `wattsPerMeterKelvin` | 1.0 |
| وحدة حرارية بريطانية لكل ساعة-قدم-°F | `Btu/(h·ft·°F)` | `btusPerHourFootFahrenheit` | ≈ 1.730735 |
| سعرة لكل ثانية-سم-كلفن | `cal/(s·cm·K)` | `caloriesPerSecondCentimeterKelvin` | 418.4 |

جميعها تدعم نطاق بادئات النظام الدولي الكامل — تُكتب موادّ العزل عادةً كـ
`40 of milli.wattsPerMeterKelvin`.

## قيم نموذجية

| المادّة | λ |
|---|---:|
| النحاس | 401 W/(m·K) |
| الفولاذ | ≈ 50 W/(m·K) |
| الزجاج | ≈ 1 W/(m·K) |
| الصوف المعدني | ≈ 0.04 W/(m·K) = 40 mW/(m·K) |

## مثال واقعي: فقد الحرارة عبر جدار معزول

تفصل طبقة صوف معدني بسماكة 30 سم (λ = 0.04 W/(m·K)) غرفةً بدرجة 21 °C عن هواء خارجي بدرجة −5 °C.
مساحة الجدار 12 m². كم من الحرارة تُفقد؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.celsius
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val wool = 40 of milli.wattsPerMeterKelvin      // 0.04 W/(m·K)
val thickness = 30 of centi.meters
val drop = (21 of celsius) - (-5 of celsius)    // 26 K

val gradient = drop / thickness                 // KTemperatureGradientUnitInstance, ≈ 86.7 K/m
gradient into kelvinPerMeter                    // 86.666...

val flux = wool * gradient                      // KHeatFluxDensityUnitInstance (Fourier's law)
flux into wattsPerSquareMeter                   // ≈ 3.47 W/m²

val wall = (4 of meters) * (3 of meters)        // 12 m²
val loss = flux * wall                          // KPowerUnitInstance
loss into watts                                 // ≈ 41.6 W
```

## الحساب باستخدام الوحدات المجاورة

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `heatFluxDensity / temperatureGradient` | `KThermalConductivityUnitInstance` | حلّ قانون فورييه لـ λ |
| `thermalConductivity * temperatureGradient` | `KHeatFluxDensityUnitInstance` | قانون فورييه |
| `temperatureGradient * thermalConductivity` | `KHeatFluxDensityUnitInstance` | نفسه (تبادلي) |
| `heatFluxDensity / thermalConductivity` | `KTemperatureGradientUnitInstance` | التدرّج الضمني |

## التفكيكات

كلا التفكيكين يُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `heatFluxDensity / temperatureGradient` | معامل مكتوب بنوع صريح | `KThermalConductivityUnitInstance` |
| `mass · distance · time⁻³ · temperature⁻¹` | تعبير أصلي + `toThermalConductivity()` | `KThermalConductivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val typed = (1 of wattsPerSquareMeter) / (1 of kelvinPerMeter)
val native = (
    (1000 of grams).toUnit() *
        (1 of meters).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toThermalConductivity()

typed == native // true - both are 1.0 W/(m·K)
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.conductivity.*

val total = (1 of kilo.wattsPerMeterKelvin) + (500 of wattsPerMeterKelvin)  // 1500 W/(m·K)
(1 of kilo.wattsPerMeterKelvin) > (500 of wattsPerMeterKelvin)              // true
(1 of kilo.wattsPerMeterKelvin) == (1000 of wattsPerMeterKelvin)            // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.conductivity.*

(401 of wattsPerMeterKelvin).toString()                                          // "401.0 W/(m·K)"
"${(401 of wattsPerMeterKelvin) into btusPerHourFootFahrenheit} Btu/(h·ft·°F)"   // "231.7..."
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `W/(m·K)` | `wattsPerMeterKelvin` | الموصلية الحرارية، الوحدة الأساسية |
| `kg·m·s⁻³·K⁻¹` | `grams * meters / (seconds pow 3) / ΔK` | نفس الكمّية بالأبعاد الأساسية |
| `mW/(m·K)` | `milli.wattsPerMeterKelvin` | ميلي واط لكل متر-كلفن (عزل) |
| `q̇ = λ · ∇T` | `wool * gradient` | قانون فورييه |
| `λ = q̇ / ∇T` | `(80 of wattsPerSquareMeter) / gradient` | الموصلية من التدفّق ÷ التدرّج |
| `∇T = q̇ / λ` | `flux / wool` | التدرّج من التدفّق ÷ الموصلية |
