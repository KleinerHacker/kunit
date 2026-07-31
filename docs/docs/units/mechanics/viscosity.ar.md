# اللزوجة الديناميكية

الحزمة: `org.pcsoft.framework.kunit.mechanic.viscosity`
الوحدة الأساسية: **باسكال ثانية** (`KViscosityUnit.BASE == KViscosityUnit.PASCAL_SECOND`)

النوع: **وحدة مركّبة**

تصف اللزوجة الديناميكية `η` مقاومة مائع للقصّ. إنّها وحدة **مركّبة** — التركيبة
`pressure · time`، أي `mass · length⁻¹ · time⁻¹` (`Pa·s`).

يغلّف `KViscosityUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود بالضبط بالصيغة القياسية:
`KMassUnit.BASE` (غرام) بأس `+1`، و`KDistanceUnit.BASE` (متر) بأس `-1`، و`KTimeUnit.BASE` (ثانية)
بأس `-1`. ولأنّ مكوّن الكتلة في هذه المكتبة مطبَّع إلى الغرام، فإنّ القيمة المخزَّنة هي قيمة المكوّن الخام المبنيّ على
الغرام، وتُقسَم القراءات بوحدة Pa·s على عامل ثابت.

!!! note "اللزوجة الديناميكية مقابل الحركية"
اللزوجة **الحركية** `ν = η / ρ` (`m²/s`) كمّية مختلفة وتعيش في مجموعة الانتشارية — انظر
[اللزوجة الحركية](kinematic-viscosity.md).

## الوحدات المسمّاة

| الوحدة                     | الرمز        |                    الرمز البرمجي | 1 وحدة بـ Pa·s |
|----------------------------|--------------|---------------------------------:|---------------:|
| باسكال ثانية               | `Pa*s`       |                  `pascalSeconds` |            1.0 |
| بواز                       | `P`          |                         `poises` |            0.1 |
| رطل-قوّة ثانية لكل قدم مربّع | `lbf*s/ft^2` | `poundForceSecondsPerSquareFoot` |      ≈ 47.8803 |
| رَيْن (lbf·s/in²)            | `reyn`       |                          `reyns` |     ≈ 6894.757 |

الكتابتان اليوميتان للموائع الشبيهة بالماء صيغتان ببادئة وليستا رموزًا خاصّة: **الملّيباسكال ثانية** هي
`milli.pascalSeconds` و **السنتيبواز** هو `centi.poises` — وهما متساويتان (`1 mPa·s = 1 cP`, الماء عند 20 °C).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val water = 1 of milli.pascalSeconds
water into centi.poises  // 1.0
water into pascalSeconds // 0.001
(1 of poises) into pascalSeconds // 0.1
```

## الحساب باستخدام الوحدات الأساسية (الضغط والزمن)

| التعبير                              | نوع النتيجة                | المعنى                      |
|--------------------------------------|----------------------------|-----------------------------|
| `pressure * time`, `time * pressure` | `KViscosityUnitInstance`   | `η = p · t`                 |
| `viscosity / pressure`               | `KTimeUnitInstance`        | `t = η / p`                 |
| `viscosity / time`                   | `KPressureUnitInstance`    | `p = η / t`                 |
| `viscosity / density`                | `KDiffusivityUnitInstance` | اللزوجة الحركية `ν = η / ρ` |
| `viscosity / diffusivity`            | `KDensityUnitInstance`     | `ρ = η / ν`                 |

تتحوّل الصيغة الأصلية عبر `toViscosity()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val typed = (2 of pascals) * (3 of seconds)
val native = ((2 of pascals).toUnit() * (3 of seconds).toUnit()).toViscosity()

typed == native            // true - كلاهما 6 Pa·s
typed into pascalSeconds   // 6.0
```

## مثال واقعي: زيت محرّك عند درجة حرارة التشغيل

زيت SAE 30 يقيس 9.3 cP عند 100 °C بكثافة 850 kg/m³. ما ذلك بوحدة Pa·s، وأيّ لزوجة حركية تقابله؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.diffusivity.centistokes
import org.pcsoft.framework.kunit.common.diffusivity.div
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.*
import org.pcsoft.framework.kunit.pow

val oil = 9.3 of centi.poises
oil into pascalSeconds        // 0.0093

val rho = (850 of kilo.grams) / (1 of (meters pow 3))
val nu = oil / rho            // KDiffusivityUnitInstance
nu into centistokes           // ≈ 10.94
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val sum = (10 of pascalSeconds) + (4 of pascalSeconds) // 14 Pa·s
(1 of poises) > (1 of milli.pascalSeconds)             // true
(1 of poises) == (100 of milli.pascalSeconds)          // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mechanic.viscosity.*

(2 of pascalSeconds).toString()                    // "2.0 Pa*s" (وحدة أساسية)
"${(2 of pascalSeconds) into centi.poises} cP"     // "2000.0 cP"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات    | Kotlin                                            | المعنى                                          |
|--------------|---------------------------------------------------|-------------------------------------------------|
| `Pa·s`       | `pascalSeconds`                                   | اللزوجة الديناميكية، الوحدة الأساسية (رمز مسمّى) |
| `kg·m⁻¹·s⁻¹` | `kilo.grams * (meters pow -1) * (seconds pow -1)` | نفس الكمّية كحاصل ضرب صرف                        |
| `cP`         | `centi.poises`                                    | سنتيبواز (= 1 mPa·s)                            |
| `η = p · t`  | `pressure * time`                                 | التفكيك المكتوب                                 |
| `ν = η / ρ`  | `viscosity / density`                             | اللزوجة الحركية                                 |
| `mPa·s`      | `milli.pascalSeconds`                             | لزوجة ببادئة                                    |
