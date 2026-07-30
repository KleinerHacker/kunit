# معامل التمدّد الحراري

الحزمة: `org.pcsoft.framework.kunit.thermo.expansion`
الوحدة الأساسية: **لكل كلفن** (`KThermalExpansionUnit.BASE == KThermalExpansionUnit.PER_KELVIN`)

النوع: **وحدة مركّبة**

معامل التمدّد الحراري `α` هو التغيّر *النسبي* في الطول (أو المساحة، أو الحجم) لكل كلفن: `1/K`.
وهو مقلوب فرق درجة الحرارة.

يغلّف `KThermalExpansionUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّ واحد بالضبط بالصيغة
القياسية `temperature⁻¹` (`K⁻¹`)، مطبَّعًا دائمًا إلى 1/K. بُعد درجة الحرارة هنا هو مجموعة
**الفرق** — إذ يصف المعامل تغيّرًا لكل *فترة* حرارية.

!!! note "اسم الحزمة مقابل اسم الصنف"
    الحزمة هي `thermo.expansion`، وليست `thermo.thermalexpansion` — إذ يجب ألّا تُكرِّر حزمة
    الوحدة اسم حزمة مجالها. تحتفظ الأصناف بالمصطلح التقني الكامل
    (`KThermalExpansionUnitInstance`).

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ 1/K |
|---|---|---:|---:|
| لكل كلفن | `1/K` | `perKelvin` | 1.0 |
| لكل درجة فهرنهايت | `1/°F` | `perFahrenheit` | 1.8 |
| جزء من مليون لكل كلفن | `ppm/K` | `ppmPerKelvin` | 1e-6 |

تسرد جداول المواد قيمة `α` بوحدة ppm/K، وهي بالضبط `micro.perKelvin`. تدعم جميع الوحدات نطاق
بادئات النظام الدولي الكامل.

## قيم نموذجية

| المادّة | α |
|---|---:|
| الفولاذ | ≈ 12 ppm/K |
| الخرسانة | ≈ 12 ppm/K |
| الألومنيوم | ≈ 23 ppm/K |
| الزجاج (بوروسيليكات) | ≈ 3.3 ppm/K |

## مثال واقعي: عارضة فولاذية في الصيف

تسخن عارضة فولاذية طولها 10 m (α = 12 ppm/K) من 0 °C إلى 50 °C. كم تزداد طولًا؟ هذا هو سبب وجود
فواصل التمدّد في الجسور.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val steel = 12 of ppmPerKelvin
val beam = 10 of meters
val rise = (50 of celsius) - (0 of celsius)   // 50 K

// التغيّر النسبي عديم الأبعاد
val strain = steel * rise                      // 6.0e-4

// التغيّر المطلق، محكوم بالنوع
val growth = steel.elongationOf(beam, rise)    // KLengthUnitInstance
growth into milli.meters                       // 6.0 mm

// جسر بطول 100 m تحت نفس التأرجح
steel.elongationOf(100 of meters, rise) into milli.meters // 60.0 mm
```

## العمليات

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `1 / temperatureDifference` | `KThermalExpansionUnitInstance` | المعامل من فترة |
| `1 / thermalExpansion` | `KTemperatureDifferenceUnitInstance` | الفترة من معامل |
| `thermalExpansion * temperatureDifference` | `Double` | التغيّر **النسبي** (عديم الأبعاد) |
| `temperatureDifference * thermalExpansion` | `Double` | نفسه (تبادلي) |
| `thermalExpansion.elongationOf(length, temperatureDifference)` | `KLengthUnitInstance` | التغيّر **المطلق** |

يُعرَّف المعاملان المقلوبان بدقّة، بحيث تُعيد `1 / d` و`1 / α` قيمةً **محكومة بالنوع** بدلًا من
الوحدة المختلطة العامّة التي كان سيُنتجها `Number.div` غير المرتبط بالمجموعة.

!!! warning "`elongationOf` بدلًا من سلسلة `*`"
    `α · ΔT` هي عن قصد `Double` صِرف — التغيّر النسبي عديم الأبعاد. ضرب هذا الـ `Double` في طول
    يتطلّب دالّة `times` العددية العامّة من الحزمة الجذرية، واستيرادها صراحةً **يُظلِّل**
    معامل `times` الخاص بهذه المجموعة. `elongationOf` دالّة بسيطة تحديدًا لكي لا يمكن تظليلها؛
    فضِّلها كلّما أردت التغيّر المطلق.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.expansion.*

val sum = (12 of ppmPerKelvin) + (5 of ppmPerKelvin)   // 17 ppm/K
(12 of ppmPerKelvin) > (5 of ppmPerKelvin)             // true
(1 of perKelvin) == (1_000_000 of ppmPerKelvin)        // true
```

## التفكيكات

كلا التفكيكين يُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `1 / temperatureDifference` | معامل مكتوب بنوع صريح | `KThermalExpansionUnitInstance` |
| `temperature⁻¹` | تعبير أصلي + `toThermalExpansion()` | `KThermalExpansionUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = 1 / KTemperatureDifference.ofKelvin(1)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() pow -1).toThermalExpansion()

typed == native // true - كلاهما 1.0 1/K
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.expansion.*

(12 of ppmPerKelvin).toString()                    // "1.2E-5 1/K"
"${(12 of ppmPerKelvin) into ppmPerKelvin} ppm/K"  // "12.0 ppm/K"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `1/K` | `perKelvin` | معامل التمدّد الحراري، الوحدة الأساسية |
| `K⁻¹` | `ΔK pow -1` | نفس الكمّية كأسّ سالب |
| `ppm/K` | `ppmPerKelvin` | جزء من مليون لكل كلفن (جداول المواد) |
| `α = 1 / ΔT` | `1 / KTemperatureDifference.ofKelvin(2)` | المعامل من فترة |
| `ε = α · ΔT` | `steel * rise` | التغيّر النسبي (عديم الأبعاد) |
| `Δl = α · l · ΔT` | `steel.elongationOf(beam, rise)` | تغيّر الطول المطلق |
