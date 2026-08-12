# المقاومة الحرارية المطلقة

الحزمة: `org.pcsoft.framework.kunit.thermo.resistance`
الوحدة الأساسية: **كلفن لكل واط** (`KThermalResistanceUnit.BASE == KThermalResistanceUnit.KELVIN_PER_WATT`)

النوع: **وحدة مركّبة**

المقاومة الحرارية المطلقة `R` لأحد المكوّنات هي فرق درجة الحرارة الذي يحافظ عليه لكل وحدة من الحرارة
المارّة عبره: `R = ΔT / P`، تُقاس بـ `K/W`. وهي تصف **جسمًا بأكمله** — هذا المشتّت الحراري، هذا
غلاف الترانزستور، هذا الجدار بهذا الحجم بعينه.

الصيغة القياسية لهذه المجموعة هي `mass⁻¹ · length⁻² · time³ · temperature`.

!!! warning "ليست نفسها المقاومة الحرارية للعزل"
    لا تخلط بين هذه المجموعة وبين [مقاومة العزل الحراري](thermal-insulance.md) `m²·K/W` (قيمة R)،
    وهي الفكرة نفسها لكن مُطبَّعة **لكل وحدة مساحة**. تختلف الاثنتان بمعامل المساحة، ولهما صيغتان
    قياسيتان مختلفتان وبالتالي نوعان مختلفان. حتى الإصدار 0.8.0 وشاملًا إياه، كان الاسم
    `thermo.resistance` / `KThermalResistanceUnit` يشير إلى مقاومة العزل تلك؛ أمّا الآن فهو يشير
    إلى هذه المجموعة.

## الوحدات المسمّاة

| الوحدة                       | الرمز       |                    الرمز البرمجي | 1 وحدة بـ K/W |
|----------------------------|------------|------------------------:|--------------:|
| كلفن لكل واط            | `K/W`      |         `kelvinsPerWatt` |           1.0 |
| درجة مئوية لكل واط    | `°C/W`     |  `degreesCelsiusPerWatt` |           1.0 |
| ساعة·°F لكل Btu                | `h*°F/Btu` |    `hourFahrenheitPerBtu` |     ≈ 1.89563 |

فرق درجة الحرارة البالغ 1 °C يساوي 1 K، لذا فإنّ `degreesCelsiusPerWatt` — وهي الصيغة المستخدَمة في
أوراق بيانات أشباه الموصلات والمشتّتات الحرارية — تُطابق عدديًا `kelvinsPerWatt`. تدعم جميع الرموز
البرمجية نطاق بادئات النظام الدولي الكامل.

## التفكيكات

تحتوي هذه المجموعة على تفكيك واحد، وتُنتج صيغتاه كلتاهما نفس النسخة المحكومة بالنوع والمتساوية القيمة.
تُبنى الصيغة الأصلية من **قوالب الوحدات** لأنّ هذه المجموعة تحمل حدًّا للكتلة: القيمة المختلطة الخام
هي الناتج القائم على الغرام، بينما تُخزِّن النسخة المحكومة بالنوع قيمتها بالوحدة المسمّاة.

| الصيغة             | التعبير                                                            |
|------------------|------------------------------------------------------------------------|
| معامل مكتوب بنوع صريح   | `temperatureDifference / power`                                        |
| تعبير أصلي (`toX()`) | `(2.5 of s³ · K / kilo.grams / m²).toThermalResistance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val typed = KTemperatureDifference.ofKelvin(30) / (12 of watts)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (2.5 of (seconds pow 3) * kelvinTerm / kilo.grams.toUnit() / (meters pow 2))
    .toThermalResistance()

typed == native            // true
typed into kelvinsPerWatt  // 2.5
```

## الحساب باستخدام هذه المجموعة

| التعبير                                | نوع النتيجة                            | المعنى              |
|-------------------------------------------|----------------------------------------|----------------------|
| `temperatureDifference / power`           | `KThermalResistanceUnitInstance`       | `R = ΔT / P`         |
| `thermalResistance * power`               | `KTemperatureDifferenceUnitInstance`   | `ΔT = R · P`         |
| `temperatureDifference / thermalResistance` | `KPowerUnitInstance`                 | التدفّق الحراري الناتج |
| `thermalResistance + …`                   | `KThermalResistanceUnitInstance`       | مقاومات متسلسلة |
| `1 / thermalResistance`                   | `KThermalConductanceUnitInstance`      | `G = 1 / R`          |

المقاومات الحرارية **تُجمَع عند التسلسل** — وهذا بالضبط ما تفعله العملية `+` من النوع نفسه لهذه
المجموعة.

## مثال واقعي — ميزانية مشتّت حراري

يبدّد ترانزستور طاقة **12 W**. السلسلة الحرارية هي 0.5 K/W من الوصلة إلى الغلاف، و0.2 °C/W من
الغلاف إلى المشتّت الحراري، و1.8 K/W من المشتّت الحراري إلى الهواء. كم سترتفع درجة حرارة الوصلة عن
درجة حرارة المحيط؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val chain = (0.5 of kelvinsPerWatt) + (0.2 of degreesCelsiusPerWatt) + (1.8 of kelvinsPerWatt)
chain into kelvinsPerWatt                                   // 2.5

val rise = chain * (12 of watts)                            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1)                // أعلى من المحيط بـ 30.0 K

// كم من الطاقة يمكنه تبديدها ضمن حدّ 25 K؟
val budget = KTemperatureDifference.ofKelvin(25) / chain    // KPowerUnitInstance
budget into watts                                            // 10.0 W
```

## دلالة القيمة

يقارن `equals`/`hashCode` **قيمة K/W المطبَّعة**، لذا
`(1 of kelvinsPerWatt) == (1 of degreesCelsiusPerWatt)`. يعرض `toString()` القيمة بالوحدة
الأساسية: `"2.5 K/W"`.

## انظر أيضًا

* [مقاومة العزل الحراري](thermal-insulance.ar.md) — الفكرة نفسها لكن لكل وحدة مساحة (قيمة R).
* [الموصلية الحرارية](thermal-conductance.ar.md) — الكمّية المقلوبة لها.
* [نظرة عامة على الديناميكا الحرارية](overview.ar.md)
