# تدرّج درجة الحرارة

الحزمة: `org.pcsoft.framework.kunit.thermo.temperaturegradient`
الوحدة الأساسية: **كلفن لكل متر** (`KTemperatureGradientUnit.BASE == KTemperatureGradientUnit.KELVIN_PER_METER`)

النوع: **وحدة مركّبة**

تدرّج درجة الحرارة هو تغيّر درجة الحرارة لكل وحدة طول: `temperatureDifference / length`
(`K/m`). وهو الكمّية الدافعة للتوصيل الحراري — وضربه في [موصلية حرارية](thermal-conductivity.md)
يعطي [كثافة تدفّق حراري](heat-flux-density.md).

يغلّف `KTemperatureGradientUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّين بالضبط بالصيغة
القياسية `temperature¹ · distance⁻¹` (`K·m⁻¹`)، مطبَّعًا دائمًا إلى K/m.

!!! note "التدرّج هو *تغيّر* لكل طول"
    بُعد درجة الحرارة هو مجموعة **الفرق** (`KTemperatureDifferenceUnit`). لا معنى لمقياس مطلق يحمل
    إزاحة (°C، °F) في تدرّج — إذ الفترات وحدها ذات معنى. لهذا أيضًا تُحوَّل `°F/ft` بعامل فترة
    فهرنهايت 5/9، وليس بإزاحة −32.

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ K/m |
|---|---|---:|---:|
| كلفن لكل متر | `K/m` | `kelvinPerMeter` | 1.0 |
| كلفن لكل كيلومتر | `K/km` | `kelvinPerKilometer` | 0.001 |
| درجة فهرنهايت لكل قدم | `°F/ft` | `fahrenheitPerFoot` | ≈ 1.822689 |

جميعها تدعم نطاق بادئات النظام الدولي الكامل (`milli.kelvinPerMeter`، …).

## مثال واقعي: التدرّج الجيوحراري

تسخن القشرة الأرضية بمقدار 25 K تقريبًا لكل كيلومتر من العمق. يصل بئر حفر إلى 3.5 km. كم أكثر سخونة
تكون الصخور في القاع، وما العمق اللازم للوصول إلى ارتفاع 100 K؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val geothermal = 25 of kelvinPerKilometer
val borehole = 3.5 of kilo.meters

val rise = geothermal * borehole            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1) // 87.5 K warmer at the bottom

val depthFor100K = KTemperatureDifference.ofKelvin(100) / geothermal // KLengthUnitInstance
depthFor100K into kilo.meters               // 4.0 km
depthFor100K into meters                    // 4000.0 m
```

## الحساب باستخدام الوحدات الأساسية (فرق درجة الحرارة والطول)

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `temperatureDifference / length` | `KTemperatureGradientUnitInstance` | التدرّج |
| `temperatureGradient * length` | `KTemperatureDifferenceUnitInstance` | الارتفاع عبر الطول |
| `length * temperatureGradient` | `KTemperatureDifferenceUnitInstance` | الارتفاع (تبادلي) |
| `temperatureDifference / temperatureGradient` | `KLengthUnitInstance` | الطول الممتدّ |

## التفكيكات

كلا التفكيكين يُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `temperatureDifference / length` | معامل مكتوب بنوع صريح | `KTemperatureGradientUnitInstance` |
| `temperature · distance⁻¹` | تعبير أصلي + `toTemperatureGradient()` | `KTemperatureGradientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = KTemperatureDifference.ofKelvin(1) / (1 of meters)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() / (1 of meters).toUnit()).toTemperatureGradient()

typed == native // true - both are 1.0 K/m
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

val total = (1 of kelvinPerMeter) + (500 of kelvinPerKilometer)  // 1.5 K/m
(1 of kelvinPerMeter) > (500 of kelvinPerKilometer)              // true
(1 of kelvinPerMeter) == (1000 of kelvinPerKilometer)            // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

(25 of kelvinPerKilometer).toString()                        // "0.025 K/m"
"${(25 of kelvinPerKilometer) into kelvinPerKilometer} K/km" // "25.0 K/km"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `K/m` | `kelvinPerMeter` | تدرّج درجة الحرارة، الوحدة الأساسية |
| `K·m⁻¹` | `ΔK / meters` | نفس الكمّية بالأبعاد الأساسية |
| `K/km` | `kelvinPerKilometer` | كلفن لكل كيلومتر (التدرّج الجيوحراري) |
| `°F/ft` | `fahrenheitPerFoot` | درجة فهرنهايت لكل قدم |
| `∇T = ΔT / L` | `KTemperatureDifference.ofKelvin(25) / (1 of kilo.meters)` | التدرّج من الارتفاع ÷ الطول |
| `ΔT = ∇T · L` | `geothermal * borehole` | الارتفاع من التدرّج × الطول |
| `L = ΔT / ∇T` | `KTemperatureDifference.ofKelvin(100) / geothermal` | الطول من الارتفاع ÷ التدرّج |
