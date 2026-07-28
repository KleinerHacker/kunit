# كثافة التدفق المغناطيسي

الحزمة: `org.pcsoft.framework.kunit.electric.magneticfluxdensity`
الوحدة الأساسية: **تسلا** (`KMagneticFluxDensityUnit.BASE == KMagneticFluxDensityUnit.TESLA`)

النوع: **وحدة مركّبة**

كثافة التدفق المغناطيسي (الحث المغناطيسي `B`) وحدة **مركّبة**: التركيب
`mass · time⁻² · current⁻¹` (`kg·s⁻²·A⁻¹`). يغلّف `KMagneticFluxDensityUnitInstance` كائن
`KMixedUnitInstance` مكوّن من ثلاثة حدود — `KMassUnit.BASE` (غرام) بالأس `+1`، و`KTimeUnit.BASE`
(ثانية) بالأس `-2`، و`KElectricCurrentUnit.BASE` (أمبير) بالأس `-1`. ولأن مكوّن الكتلة في المكتبة
مطبَّع إلى **غرامات** (وليس كيلوغرامات)، فإن التسلا تساوي 1000× قيمة المكوّن الأساسي الخام؛ وتُطبَّع
القيمة المخزّنة إلى التسلا.

## إنشاء كثافة تدفق مغناطيسي

تُنشأ كثافة التدفق برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| كثافة التدفق | الرمز | الرمز البرمجي | 1 وحدة بالتسلا |
|---|---|---:|---:|
| تسلا | `T` | `teslas` | 1.0 |
| ويبر لكل متر مربع | `Wb/m²` | `webersPerSquareMeter` | 1.0 |
| غاوس (CGS-EMU) | `G` | `gauss` | 1.0e-4 |
| غاما | `γ` | `gammas` | 1.0e-9 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`milli.teslas`، `micro.teslas`،
`nano.teslas`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val b = 50 of micro.teslas
b into teslas                 // 5.0e-5
b into gauss                  // 0.5
(1 of teslas) into gammas     // 1.0e9
```

## تفكيكات متعددة

يمكن الوصول إلى كثافة التدفق المغناطيسي عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج كثافة تدفق متساوية
القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `flux / area` | `KMagneticFluxDensityUnitInstance` | التعريف `B = Φ / A` |
| `mass/(time²·current)` | عبر `.toMagneticFluxDensity()` | التعبير الأصلي القياسي `kg·s⁻²·A⁻¹` |

تُعيد الصيغة المكتوبة بنوعها صريحًا كثافة تدفق مباشرة. أما التعبير الأصلي بالكامل فيبقى
`KMixedUnitInstance` عامًّا ويُضيَّق عبر `toMagneticFluxDensity()` (الذي يتعرّف فقط على الصيغة
القياسية ويرمي `IllegalStateException` خلاف ذلك). كلا المسارين متساويا القيمة.

تربط العمليات العكسية بين التدفق وكثافة التدفق والمساحة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `fluxDensity * area` | `KMagneticFluxUnitInstance` | `Φ = B · A` |
| `area * fluxDensity` | `KMagneticFluxUnitInstance` | `Φ = A · B` (تبادلي) |
| `flux / fluxDensity` | `KAreaUnitInstance` | `A = Φ / B` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

// مثال واقعي - جهاز تصوير بالرنين المغناطيسي: 18 Wb من التدفق عبر ملف مساحته 6 m² يعطي مجالًا 3 T.
val b = (18 of webers) / ((2 of meters) * (3 of meters))  // KMagneticFluxDensityUnitInstance، 3 T

// نفس كثافة التدفق كتعبير أصلي kg·s⁻²·A⁻¹:
val raw = 3 of (kilo.grams / ((seconds pow 2) * (amperes pow 1)))
raw.toMagneticFluxDensity() == (3 of teslas)              // true

// المجال المغناطيسي الأرضي البالغ 50 µT عبر حلقة مساحتها 2 m² يعطي تدفقًا مقداره 1e-4 Wb.
val flux = (50 of micro.teslas) * ((2 of meters) * (1 of meters))  // KMagneticFluxUnitInstance، 1e-4 Wb
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val s = (3 of teslas) + (1 of teslas)  // 4 T
(3 of teslas) > (1 of teslas)          // true
(3 of teslas) * (1 of teslas)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

(3 of teslas).toString()     // "3.0 T" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `T` | `teslas` | كثافة التدفق، الوحدة الأساسية (رمز مسمّى، تسلا) |
| `Wb/m²` | `webersPerSquareMeter` | كثافة التدفق كتدفق لكل مساحة (رمز مسمّى) |
| `kg/(s²·A)` | `kilo.grams / ((seconds pow 2) * (amperes pow 1))` | كثافة التدفق ككتلة / (زمن²·تيار) (صيغة الكسر) |
| `kg·s⁻²·A⁻¹` | `kilo.grams * (seconds pow -2) * (amperes pow -1)` | نفس كثافة التدفق كحاصل ضرب خالص |
| `µT` | `micro.teslas` | كثافة تدفق ببادئة (ميكروتسلا) |
