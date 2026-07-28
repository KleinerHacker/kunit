# كثافة الشحنة الخطية

الحزمة: `org.pcsoft.framework.kunit.electric.linearchargedensity`
الوحدة الأساسية: **كولوم لكل متر**
(`KLinearChargeDensityUnit.BASE == KLinearChargeDensityUnit.COULOMB_PER_METER`)

النوع: **وحدة مركّبة**

كثافة الشحنة الخطية وحدة **مركّبة**: التركيب `current · time · length⁻¹`
(`A·s·m⁻¹` = `C/m`). يغلّف `KLinearChargeDensityUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود —
`KElectricCurrentUnit.BASE` (أمبير) بالأس `+1`، و`KTimeUnit.BASE` (ثانية) بالأس `+1`، و`KDistanceUnit.BASE`
(متر) بالأس `-1`. لا تحمل هذه المجموعة أي بُعد كتلة، لذا لا حاجة إلى جسر غرام/كيلوغرام؛ وتُطبَّع القيمة
المخزّنة دائمًا إلى كولوم لكل متر.

كثافة الشحنة الخطية `λ` هي الشحنة المحمولة لكل وحدة طول، مثلًا على طول سلك أو خيط مشحون. **لا توجد لها
وحدة مسمّاة خاصة بها**: فكل صياغة هي نسبة (C/m، µC/cm)، ولذلك لا تملك المجموعة رموزًا مجرّدة ولا بناة
بادئات — تُبنى القيم من تعبير أو عبر العمليات المصرَّحة الأنواع. النظيران ثنائي وثلاثي الأبعاد هما
[كثافة التدفق الكهربائي](electricfluxdensity.ar.md) (C/m²) و[كثافة الشحنة](chargedensity.ar.md) (C/m³).

## إنشاء كثافة شحنة خطية

لا توجد رموز مسمّاة. تُبنى القيمة من شحنة على طول:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val lambda = (5 of micro.coulombs) / (2 of meters)  // 2.5e-6 C/m
lambda.value                                        // 2.5e-6 (مطبَّعة إلى C/m)
```

## تفكيكات متعددة

يمكن الوصول إلى كثافة الشحنة الخطية عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج كثافة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `charge / length` | `KLinearChargeDensityUnitInstance` | `λ = Q / l`، الشحنة موزّعة على طول |
| `current·time/length` | عبر `.toLinearChargeDensity()` | التعبير الأصلي القياسي `A·s·m⁻¹` |

تُعيد الصيغة المكتوبة بنوع صريح كثافة شحنة خطية مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toLinearChargeDensity()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). كلا المسارَين متساوٍ في القيمة.

تربط العمليات العكسية بين الشحنة والطول والكثافة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `linearChargeDensity * length` | `KChargeUnitInstance` | `Q = λ · l` (تبادلي) |
| `charge / linearChargeDensity` | `KLengthUnitInstance` | `l = Q / λ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

// مثال واقعي - خيط يحمل 5 µC على طول 2 m له كثافة شحنة خطية قدرها 2.5 µC/m.
val lambda = (5 of micro.coulombs) / (2 of meters)   // 2.5e-6 C/m

// محلولة للشحنة:
val q = lambda * (2 of meters)                       // KChargeUnitInstance، 5 µC
q into micro.coulombs                                // 5.0

// نفس الكثافة كتعبير أصلي A·s·m⁻¹:
val raw = 2.5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 1)
raw.toLinearChargeDensity() == lambda                // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val a = (2 of coulombs) / (1 of meters)
val b = (3 of coulombs) / (1 of meters)
(a + b).value    // 5.0 C/m
b > a            // true
(a * b)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

((2 of coulombs) / (1 of meters)).toString()   // "2.0 C/m" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`⁻¹`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `C/m` | `(1 of coulombs) / (1 of meters)` | كثافة الشحنة الخطية، الوحدة الأساسية (بلا رمز مسمّى) |
| `Q / l` | `(5 of micro.coulombs) / (2 of meters)` | الكثافة من الشحنة على طول |
| `λ · l` | `lambda * (2 of meters)` | الشحنة التي يحملها طول |
| `A·s/m` | `((amperes pow 1) * (seconds pow 1)) / (meters pow 1)` | الكثافة كتيار·زمن / طول (صيغة الكسر) |
| `A·s·m⁻¹` | `(amperes pow 1) * (seconds pow 1) * (meters pow -1)` | نفس الكثافة كحاصل ضرب خالص |
