# اللزوجة الحركية

الحزمة: `org.pcsoft.framework.kunit.common.diffusivity`
الوحدة الأساسية: **متر مربع لكل ثانية**
(`KDiffusivityUnit.BASE == KDiffusivityUnit.SQUARE_METER_PER_SECOND`)

النوع: **وحدة مركّبة**

اللزوجة الحركية `ν = η / ρ` هي [اللزوجة الديناميكية](viscosity.md) مقسومة على
[الكثافة](density.md) — الكمّية التي تحكم كيفية انتشار الزخم عبر مائع. بُعدها هو
`length² · time⁻¹` (`m²/s`).

هذا هو بالضبط بُعد وكمّية مجموعة **الانتشارية**، المُشتركة مع
[الانتشارية الحرارية](../thermodynamics/thermal-diffusivity.md) في الديناميكا الحرارية. لذا لا تُقدّم KUnit مجموعة ثانية
له: اللزوجة الحركية هي **قراءة** لِـ `KDiffusivityUnitInstance`، ولهذا تعيش المجموعة في `common`. توثّق هذه الصفحة
القراءة الميكانيكية.

!!! note "مجموعة واحدة، مجالان"
تحمل `KDiffusivityUnit` كلتا المفردتين: القراءات المترية (m²/s، mm²/s) المشتركة بين المجالين، والكتابتين التقليديتين
للّزوجة الحركية: الستوكس والسنتيستوكس.

## الوحدات المسمّاة

| الوحدة                | الرمز   |                الرمز البرمجي | 1 وحدة بـ m²/s |
|-----------------------|---------|-----------------------------:|---------------:|
| متر مربع لكل ثانية    | `m²/s`  |      `squareMetersPerSecond` |            1.0 |
| ملّيمتر مربع لكل ثانية | `mm²/s` | `squareMillimetersPerSecond` |           1e-6 |
| ستوكس                 | `St`    |                     `stokes` |           1e-4 |
| سنتيستوكس             | `cSt`   |                `centistokes` |           1e-6 |
| قدم مربّع لكل ساعة     | `ft²/h` |          `squareFeetPerHour` |   ≈ 2.58064e-5 |

`1 cSt = 1 mm²/s` بالضبط — الماء عند 20 °C نحو 1 cSt. تدعم جميع الوحدات نطاق بادئات النظام الدولي الكامل، لذا
`centi.stokes` كتابة أخرى للسنتيستوكس.

## التفكيكات

| الصيغة                  | Kotlin                                                      | نوع النتيجة                |
|-------------------------|-------------------------------------------------------------|----------------------------|
| لزوجة ديناميكية / كثافة | `viscosity / density`                                       | `KDiffusivityUnitInstance` |
| تعبير أصلي              | `((length.toUnit() pow 2) / time.toUnit()).toDiffusivity()` | `KDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val water = (1000 of kilo.grams) / (1 of (meters pow 3))
val typed = (1 of milli.pascalSeconds) / water
val native = (((1 of milli.meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()

typed == native          // true - كلاهما 1e-6 m²/s
typed into centistokes   // 1.0
```

## الحساب باستخدام الوحدات الأساسية

| التعبير                                          | نوع النتيجة                | المعنى      |
|--------------------------------------------------|----------------------------|-------------|
| `viscosity / density`                            | `KDiffusivityUnitInstance` | `ν = η / ρ` |
| `diffusivity * density`, `density * diffusivity` | `KViscosityUnitInstance`   | `η = ν · ρ` |
| `viscosity / diffusivity`                        | `KDensityUnitInstance`     | `ρ = η / ν` |

## مثال واقعي: اختيار زيت هيدروليكي

زيت هيدروليكي مُصنَّف ISO VG 46، أي 46 cSt عند 40 °C، وكثافته 870 kg/m³. أيّ لزوجة ديناميكية تقابل ذلك؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val nu = 46 of centistokes
nu into squareMillimetersPerSecond // 46.0

val rho = (870 of kilo.grams) / (1 of (meters pow 3))
val eta = nu * rho                 // KViscosityUnitInstance
eta into pascalSeconds             // ≈ 0.04002
eta into centi.poises              // ≈ 40.02
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

val sum = (10 of centistokes) + (4 of centistokes) // 14 cSt
(1 of stokes) > (10 of centistokes)                // true
(1 of centistokes) == (1 of squareMillimetersPerSecond) // true (نفس القيمة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

(46 of centistokes).toString()                  // "4.6E-5 m²/s" (وحدة أساسية)
"${(46 of centistokes) into centistokes} cSt"   // "46.0 cSt"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                     | المعنى                            |
|-------------|----------------------------|-----------------------------------|
| `m²/s`      | `squareMetersPerSecond`    | اللزوجة الحركية، الوحدة الأساسية  |
| `m²·s⁻¹`    | `(meters pow 2) / seconds` | نفس الكمّية بالأبعاد الأساسية      |
| `cSt`       | `centistokes`              | سنتيستوكس (= 1 mm²/s)             |
| `ν = η / ρ` | `viscosity / density`      | التفكيك المكتوب                   |
| `η = ν · ρ` | `diffusivity * density`    | محلولة من أجل اللزوجة الديناميكية |
| `ρ = η / ν` | `viscosity / diffusivity`  | محلولة من أجل الكثافة             |
