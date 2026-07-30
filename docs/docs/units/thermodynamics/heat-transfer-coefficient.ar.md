# معامل انتقال الحرارة

الحزمة: `org.pcsoft.framework.kunit.thermo.heattransfercoefficient`
الوحدة الأساسية: **واط لكل متر مربع-كلفن** (`KHeatTransferCoefficientUnit.BASE == KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN`)

النوع: **وحدة مركّبة**

معامل انتقال الحرارة — في فيزياء المباني هو **قيمة U** — هو كثافة التدفّق الحراري التي يمرّرها عنصر
لكل كلفن من فرق درجة الحرارة: `W/(m²·K)`. كلّما قلّت قيمة U، كان العزل أفضل.

يغلّف `KHeatTransferCoefficientUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود بالضبط
بالصيغة القياسية `mass¹ · time⁻³ · temperature⁻¹` (`kg·s⁻³·K⁻¹`)، مطبَّعًا دائمًا إلى W/(m²·K). وكما
في [كثافة التدفّق الحراري](heat-flux-density.md) تُلغي المساحة أبعاد الطول للواط، لذا لا تحمل الصيغة
القياسية حدّ مسافة.

مقلوبها هو [المقاومة الحرارية](thermal-resistance.md) (قيمة R)؛ وضربها في سماكة يجعلها
[موصلية حرارية](thermal-conductivity.md).

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ W/(m²·K) |
|---|---|---:|---:|
| واط لكل متر مربع-كلفن | `W/(m²·K)` | `wattsPerSquareMeterKelvin` | 1.0 |
| وحدة حرارية بريطانية لكل ساعة-قدم مربع-°F | `Btu/(h·ft²·°F)` | `btusPerHourSquareFootFahrenheit` | ≈ 5.678263 |
| سعرة لكل ثانية-سم مربع-كلفن | `cal/(s·cm²·K)` | `caloriesPerSecondSquareCentimeterKelvin` | 41840.0 |

جميعها تدعم نطاق بادئات النظام الدولي الكامل (`milli.wattsPerSquareMeterKelvin`، …).

## قيم U النموذجية

| العنصر | U |
|---|---:|
| زجاج مفرد | ≈ 5.8 W/(m²·K) |
| زجاج مزدوج | ≈ 2.8 W/(m²·K) |
| زجاج ثلاثي | ≈ 0.7 … 1.3 W/(m²·K) |
| جدار منزل موفّر للطاقة | ≈ 0.15 W/(m²·K) |

## مثال واقعي: فقد الحرارة عبر نافذة

نافذة زجاجها ثلاثي بمساحة 2.4 m² وقيمة U = 1.3 W/(m²·K). درجة الحرارة 21 °C داخليًا و1 °C خارجيًا.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val window = 1.3 of wattsPerSquareMeterKelvin
val drop = (21 of celsius) - (1 of celsius)      // 20 K
val glass = (2 of meters) * (1.2 of meters)      // 2.4 m²

val flux = window * drop                          // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter                     // 26.0 W/m²

val loss = flux * glass                           // KPowerUnitInstance
loss into watts                                   // 62.4 W

// What would single glazing cost us?
val single = 5.8 of wattsPerSquareMeterKelvin
((single * drop) * glass) into watts              // 278.4 W - four and a half times as much
```

## الحساب باستخدام الوحدات المجاورة

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `heatFluxDensity / temperatureDifference` | `KHeatTransferCoefficientUnitInstance` | قيمة U من قياس |
| `thermalConductivity / length` | `KHeatTransferCoefficientUnitInstance` | قيمة U من المادّة + السماكة |
| `heatTransferCoefficient * temperatureDifference` | `KHeatFluxDensityUnitInstance` | التدفّق عبر العنصر |
| `temperatureDifference * heatTransferCoefficient` | `KHeatFluxDensityUnitInstance` | نفسه (تبادلي) |
| `heatFluxDensity / heatTransferCoefficient` | `KTemperatureDifferenceUnitInstance` | الفرق الدافع |
| `heatTransferCoefficient * length` | `KThermalConductivityUnitInstance` | موصلية المادّة |
| `length * heatTransferCoefficient` | `KThermalConductivityUnitInstance` | نفسه (تبادلي) |
| `thermalConductivity / heatTransferCoefficient` | `KLengthUnitInstance` | السماكة المطلوبة |

## التفكيكات

جميع التفكيكات الثلاثة تُنتج نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `heatFluxDensity / temperatureDifference` | معامل مكتوب بنوع صريح | `KHeatTransferCoefficientUnitInstance` |
| `thermalConductivity / length` | معامل مكتوب بنوع صريح | `KHeatTransferCoefficientUnitInstance` |
| `mass · time⁻³ · temperature⁻¹` | تعبير أصلي + `toHeatTransferCoefficient()` | `KHeatTransferCoefficientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux         = (1 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(1)
val viaConductivity = (1 of wattsPerMeterKelvin) / (1 of meters)
val native = (
    (1000 of grams).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatTransferCoefficient()

viaFlux == viaConductivity // true
viaFlux == native          // true - all are 1.0 W/(m²·K)
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

val total = (1 of kilo.wattsPerSquareMeterKelvin) + (500 of wattsPerSquareMeterKelvin)  // 1500
(1 of kilo.wattsPerSquareMeterKelvin) > (500 of wattsPerSquareMeterKelvin)              // true
(1 of kilo.wattsPerSquareMeterKelvin) == (1000 of wattsPerSquareMeterKelvin)            // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

(1.3 of wattsPerSquareMeterKelvin).toString()                                             // "1.3 W/(m²·K)"
"${(1.3 of wattsPerSquareMeterKelvin) into btusPerHourSquareFootFahrenheit} Btu/(h·ft²·°F)" // "0.229..."
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `W/(m²·K)` | `wattsPerSquareMeterKelvin` | معامل انتقال الحرارة (قيمة U)، الوحدة الأساسية |
| `kg·s⁻³·K⁻¹` | `grams / (seconds pow 3) / ΔK` | نفس الكمّية بالأبعاد الأساسية |
| `U = q̇ / ΔT` | `(26 of wattsPerSquareMeter) / drop` | قيمة U من التدفّق ÷ فرق درجة الحرارة |
| `U = λ / d` | `(0.04 of wattsPerMeterKelvin) / (0.2 of meters)` | قيمة U من الموصلية ÷ السماكة |
| `q̇ = U · ΔT` | `window * drop` | التدفّق من قيمة U × فرق درجة الحرارة |
| `P = U · A · ΔT` | `(window * drop) * glass` | إجمالي فقد الحرارة |
