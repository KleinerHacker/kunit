# السعة الكهربائية

الحزمة: `org.pcsoft.framework.kunit.capacitance`
الوحدة الأساسية: **فاراد** (`KCapacitanceUnit.BASE == KCapacitanceUnit.FARAD`)

النوع: **وحدة مركّبة**

السعة الكهربائية وحدة **مركّبة**: التركيب `mass⁻¹ · length⁻² · time⁴ · current²` (`kg⁻¹·m⁻²·s⁴·A²`).
يغلّف `KCapacitanceUnitInstance` نسخةَ `KMixedUnitInstance` من أربعة حدود — `KMassUnit.BASE` (غرام) عند
`-1`، و`KDistanceUnit.BASE` (متر) عند `-2`، و`KTimeUnit.BASE` (ثانية) عند `+4`،
و`KElectricCurrentUnit.BASE` (أمبير) عند `+2`. ولأنّ مكوّن الكتلة في المكتبة مُطبَّع إلى **الغرامات** (لا
الكيلوغرامات) ولأنّ أُسّ الكتلة *سالب*، فإنّ الفاراد يختلف عن الأساس الخام للمكوّنات بمعامل 1000 في الاتجاه
المعاكس؛ القيمة المخزّنة مُطبَّعة إلى الفاراد.

## بناء سعة

ابنِ سعة برمز مسمّى، أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز قيمتها 1 (تُستخدم مع
`of`/`into`):

| السعة | الرمز | الرمز البرمجي | 1 وحدة بـ F |
|---|---|---:|---:|
| فاراد | `F` | `farads` | 1.0 |
| أب-فاراد (CGS-EMU) | `abF` | `abfarads` | 1.0e9 |
| ستات-فاراد (CGS-ESU) | `statF` | `statfarads` | 1.112650056e-12 |
| جرّة (جرّة ليدن) | `jar` | `jars` | 1.11265e-9 |

تدعم الوحدات المسمّاة بادئات SI عبر `KPrefixBuilder` (`micro.farads`، `nano.farads`، `pico.farads`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.capacitance.*

val c = 470 of micro.farads
c into micro.farads            // 470.0
c into farads                  // 4.7e-4
(1 of milli.farads) into farads // 0.001
```

## تفكيكات متعدّدة

يمكن بلوغ السعة عبر عدّة **تفكيكات مكافئة**، كلّها تُنتج سعة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `charge / voltage` | `KCapacitanceUnitInstance` | التعريف `C = Q / U` |
| `current²·time⁴/(mass·length²)` | عبر `.toCapacitance()` | تعبير `kg⁻¹·m⁻²·s⁴·A²` الأصلي القياسي |

تُعيد صيغة المعامِل المحكومة بالنوع سعةً مباشرةً. أمّا التعبير الأصلي الكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق بـ `toCapacitance()` (الذي يتعرّف فقط على الشكل القياسي ويُطلق `IllegalStateException` خلاف
ذلك). كلا المسارين متساوي القيمة.

تربط المعاملات العكسية الشحنة والجهد والسعة معًا:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `capacitance * voltage` | `KChargeUnitInstance` | `Q = C · U` (تبادلي) |
| `charge / capacitance` | `KVoltageUnitInstance` | `U = Q / C` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.capacitance.*

// مثال واقعي - مكثّف مشحون: 470 µF عند 12 V يخزّن 5.64 mC.
val q = (470 of micro.farads) * (12 of volts)  // KChargeUnitInstance، 0.00564 C

// التعريف محلولًا من أجل السعة:
val c = (10 of coulombs) / (5 of volts)        // KCapacitanceUnitInstance، 2 F

// السعة نفسها كتعبير kg⁻¹·m⁻²·s⁴·A² الأصلي:
val raw = 2 of ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 2))
raw.toCapacitance() == (2 of farads)           // true
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.capacitance.*

val s = (100 of farads) + (40 of farads)  // 140 F
(100 of farads) > (40 of farads)          // true
(100 of farads) * (40 of farads)          // KMixedUnitInstance (يهرب من المجموعة)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.capacitance.*

(470 of farads).toString()     // "470.0 F" (الوحدة الأساسية)
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `⁴`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `F` | `farads` | السعة، الوحدة الأساسية (رمز مسمّى، فاراد) |
| `A²·s⁴/(kg·m²)` | `(amperes pow 2) * (seconds pow 4) / (kilo.grams * (meters pow 2))` | السعة كتيار²·زمن⁴ / (كتلة·طول²) (صيغة الكسر) |
| `kg⁻¹·m⁻²·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 4) * (amperes pow 2)` | السعة نفسها كحاصل ضرب صرف |
| `µF` | `micro.farads` | سعة ببادئة (ميكروفاراد) |
