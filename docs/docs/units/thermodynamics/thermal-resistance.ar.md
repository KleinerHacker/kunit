# المقاومة الحرارية (قيمة R)

الحزمة: `org.pcsoft.framework.kunit.thermo.resistance`
الوحدة الأساسية: **متر مربع-كلفن لكل واط** (`KThermalResistanceUnit.BASE == KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT`)

النوع: **وحدة مركّبة**

المقاومة الحرارية — **قيمة R** — تُبيّن مدى مقاومة طبقة ما لتدفّق الحرارة: `m²·K/W`. وهي المقلوب
التام لـ [معامل انتقال الحرارة](heat-transfer-coefficient.md) (قيمة U)، وهي الصيغة التي تُباع بها
منتجات العزل فعليًا، لأنّ قيم R للطبقات المتسلسلة تُجمَع ببساطة **جمعًا**.

يغلّف `KThermalResistanceUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود بالضبط بالصيغة
القياسية `mass⁻¹ · time³ · temperature¹` (`kg⁻¹·s³·K`)، مطبَّعًا دائمًا إلى m²·K/W.

!!! note "اسم الحزمة مقابل اسم الصنف"
    الحزمة هي `thermo.resistance`، وليست `thermo.thermalresistance` — إذ يجب ألّا تُكرِّر حزمة
    الوحدة اسم حزمة مجالها. تحتفظ **الأصناف** بالمصطلح التقني الكامل
    (`KThermalResistanceUnitInstance`)، وهو ما يميّزها عن `electric.resistance`.

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ m²·K/W |
|---|---|---:|---:|
| متر مربع-كلفن لكل واط (RSI) | `m²·K/W` | `squareMeterKelvinPerWatt` | 1.0 |
| قيمة R الإمبراطورية | `h·ft²·°F/Btu` | `hourSquareFootFahrenheitPerBtu` | ≈ 0.176110 |
| كلو | `clo` | `clo` | 0.155 |
| توغ | `tog` | `tog` | 0.1 |

لُفافة عزل أمريكية "R-30" تعادل `30 of hourSquareFootFahrenheitPerBtu` ≈ 5.28 m²·K/W. بدلة عمل
تعادل نحو 1 clo؛ وتُصنَّف اللحف بوحدة توغ (1 clo = 1.55 tog). تدعم جميع الوحدات نطاق بادئات
النظام الدولي الكامل.

## مثال واقعي: جدار معزول، طبقة بطبقة

يتكوّن جدار من 20 cm صوف معدني (λ = 0.04 W/(m·K)) و12 cm طوب (λ = 0.8 W/(m·K)). ما إجمالي قيمة R،
وقيمة U الناتجة، والفقد الحراري عند ΔT = 25 K؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.wattsPerSquareMeterKelvin
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val wool  = (20 of centi.meters) / (0.04 of wattsPerMeterKelvin)  // 5.0 m²·K/W
val brick = (12 of centi.meters) / (0.8 of wattsPerMeterKelvin)   // 0.15 m²·K/W

val total = wool + brick                    // الطبقات المتسلسلة تُجمَع
total into squareMeterKelvinPerWatt         // 5.15 m²·K/W
total into hourSquareFootFahrenheitPerBtu   // ≈ 29.2 (جدار "R-29")

val u = 1 / total                           // KHeatTransferCoefficientUnitInstance
u into wattsPerSquareMeterKelvin            // ≈ 0.194 W/(m²·K)

val drop = KTemperatureDifference.ofKelvin(25)
val flux = drop / total                     // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter               // ≈ 4.85 W/m²

val wall = (10 of meters) * (2.5 of meters) // 25 m²
(flux * wall) into watts                    // ≈ 121 W
```

## الحساب باستخدام الوحدات المجاورة

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `temperatureDifference / heatFluxDensity` | `KThermalResistanceUnitInstance` | R من القياس |
| `length / thermalConductivity` | `KThermalResistanceUnitInstance` | R من المادّة + السماكة |
| `thermalResistance * heatFluxDensity` | `KTemperatureDifferenceUnitInstance` | الفرق المستمرّ |
| `heatFluxDensity * thermalResistance` | `KTemperatureDifferenceUnitInstance` | نفسه (تبادلي) |
| `temperatureDifference / thermalResistance` | `KHeatFluxDensityUnitInstance` | التدفّق الناتج |
| `thermalResistance * thermalConductivity` | `KLengthUnitInstance` | السماكة المطلوبة |
| `thermalConductivity * thermalResistance` | `KLengthUnitInstance` | نفسه (تبادلي) |
| `length / thermalResistance` | `KThermalConductivityUnitInstance` | الموصلية الضمنية |
| `1 / heatTransferCoefficient` | `KThermalResistanceUnitInstance` | R من U |
| `1 / thermalResistance` | `KHeatTransferCoefficientUnitInstance` | U من R |

يُعرَّف المعاملان المقلوبان بدقّة، بحيث تُعيد `1 / u` و`1 / r` قيمةً **محكومة بالنوع** بدلًا من
الوحدة المختلطة العامّة التي كان سيُنتجها `Number.div` غير المرتبط بالمجموعة.

## التفكيكات

كلّ التفكيكات الثلاثة تُنتج نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `temperatureDifference / heatFluxDensity` | معامل مكتوب بنوع صريح | `KThermalResistanceUnitInstance` |
| `length / thermalConductivity` | معامل مكتوب بنوع صريح | `KThermalResistanceUnitInstance` |
| `mass⁻¹ · time³ · temperature¹` | تعبير أصلي + `toThermalResistance()` | `KThermalResistanceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux      = KTemperatureDifference.ofKelvin(1) / (1 of wattsPerSquareMeter)
val viaThickness = (1 of meters) / (1 of wattsPerMeterKelvin)
val native = (
    ((1 of seconds).toUnit() pow 3) *
        KTemperatureDifference.ofKelvin(1).toUnit() /
        (1000 of grams).toUnit()
    ).toThermalResistance()

viaFlux == viaThickness // true
viaFlux == native       // true - جميعها 1.0 m²·K/W
```

## العمليات

`+` و`-` هما تمامًا العملية ذات المعنى الفيزيائي هنا: الطبقات المتسلسلة تجمع قيم R الخاصّة بها.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.resistance.*

val series = (5 of squareMeterKelvinPerWatt) + (0.15 of squareMeterKelvinPerWatt) // 5.15
(1 of squareMeterKelvinPerWatt) > (5 of tog)      // true (5 tog = 0.5 m²·K/W)
(1 of squareMeterKelvinPerWatt) == (10 of tog)    // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.resistance.*

(5 of squareMeterKelvinPerWatt).toString()                                        // "5.0 m²·K/W"
"R-${(5 of squareMeterKelvinPerWatt) into hourSquareFootFahrenheitPerBtu}"        // "R-28.39..."
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `m²·K/W` | `squareMeterKelvinPerWatt` | المقاومة الحرارية (قيمة R)، الوحدة الأساسية |
| `kg⁻¹·s³·K` | `(seconds pow 3) * ΔK / grams` | نفس الكمّية بالأبعاد الأساسية |
| `h·ft²·°F/Btu` | `hourSquareFootFahrenheitPerBtu` | قيمة R الإمبراطورية |
| `R = d / λ` | `(20 of centi.meters) / (0.04 of wattsPerMeterKelvin)` | R من السماكة ÷ الموصلية |
| `R = ΔT / q̇` | `drop / (4 of wattsPerSquareMeter)` | R من الفرق ÷ التدفّق |
| `R_total = R₁ + R₂` | `wool + brick` | الطبقات المتسلسلة |
| `U = 1 / R` | `1 / total` | قيمة U من قيمة R |
| `q̇ = ΔT / R` | `drop / total` | التدفّق من الفرق ÷ R |
