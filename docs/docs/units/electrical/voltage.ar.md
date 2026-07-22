# الجهد الكهربائي

الحزمة: `org.pcsoft.framework.kunit.voltage`
الوحدة الأساسية: **فولت** (`KVoltageUnit.BASE == KVoltageUnit.VOLT`)

النوع: **وحدة مركّبة**

الجهد (فرق الجهد الكهربائي) وحدة **مركّبة**: التركيب `mass · length² · time⁻³ · current⁻¹`
(`kg·m²·s⁻³·A⁻¹`). يغلّف `KVoltageUnitInstance` نسخةَ `KMixedUnitInstance` من أربعة حدود —
`KMassUnit.BASE` (غرام) عند `+1`، و`KDistanceUnit.BASE` (متر) عند `+2`، و`KTimeUnit.BASE` (ثانية) عند
`-3`، و`KElectricCurrentUnit.BASE` (أمبير) عند `-1`. ولأنّ مكوّن الكتلة في المكتبة مُطبَّع إلى
**الغرامات** (لا الكيلوغرامات)، فإنّ الفولت أكبر 1000× من الأساس الخام للمكوّنات؛ القيمة المخزّنة مُطبَّعة
إلى الفولت.

## بناء جهد

ابنِ جهدًا برمز مسمّى، أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز قيمتها 1 (تُستخدم مع
`of`/`into`):

| الجهد | الرمز | الرمز البرمجي | 1 وحدة بـ V |
|---|---|---:|---:|
| فولت | `V` | `volts` | 1.0 |
| ستات-فولت (CGS-ESU) | `statV` | `statvolts` | 299.792458 |
| أب-فولت (CGS-EMU) | `abV` | `abvolts` | 1.0e-8 |
| خليّة وستون | `V_W` | `westonCells` | 1.0183 |
| خليّة دانييل | `V_Da` | `daniells` | 1.1 |

تدعم الوحدات المسمّاة بادئات SI عبر `KPrefixBuilder` (`kilo.volts`، `mega.volts`، `milli.volts`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u into volts                 // 230.0
u into kilo.volts            // 0.23
(1 of kilo.volts) into volts // 1000.0
```

## تفكيكات متعدّدة

يمكن بلوغ الجهد عبر عدّة **تفكيكات مكافئة**، كلّها تُنتج جهدًا متساوي القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | قانون أوم `U = R · I` (راجع المقاومة) |
| `current * resistance` | `KVoltageUnitInstance` | قانون أوم (تبادلي) |
| `mass·length²/(time³·current)` | عبر `.toVoltage()` | تعبير `kg·m²·s⁻³·A⁻¹` الأصلي القياسي |

تُعيد صيغ المعاملات المحكومة بالنوع جهدًا مباشرةً. أمّا التعبير الأصلي الكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق بـ `toVoltage()` (الذي يتعرّف فقط على الشكل القياسي ويُطلق `IllegalStateException` خلاف
ذلك). كلا المسارين متساوي القيمة.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.voltage.*

// مثال واقعي - قانون أوم: مقاوم 115 Ω يحمل 2 A يُسقِط 230 V.
val u = (115 of ohms) * (2 of amperes)   // KVoltageUnitInstance، 230 V

// الجهد نفسه كتعبير kg·m²·s⁻³·A⁻¹ الأصلي:
val raw = 230 of (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 3))
raw.toVoltage() == (230 of volts)        // true
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

val s = (100 of volts) + (40 of volts)  // 140 V
(100 of volts) > (40 of volts)          // true
(100 of volts) * (40 of volts)          // KMixedUnitInstance (يهرب من المجموعة)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

(230 of volts).toString()    // "230.0 V" (الوحدة الأساسية)
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `V` | `volts` | الجهد، الوحدة الأساسية (رمز مسمّى، فولت) |
| `kg·m²/(s³·A)` | `kilo.grams * (meters pow 2) / (amperes * (seconds pow 3))` | الجهد ككتلة·طول² / (زمن³·تيار) (صيغة الكسر) |
| `kg·m²·s⁻³·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -1)` | الجهد نفسه كحاصل ضرب صرف |
| `kV` | `kilo.volts` | جهد ببادئة (كيلوفولت) |
