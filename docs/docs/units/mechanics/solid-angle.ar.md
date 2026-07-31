# الزاوية المجسّمة

الحزمة: `org.pcsoft.framework.kunit.mechanic.solidangle`
الوحدة الأساسية: **ستراديان** (`KSolidAngleUnit.BASE == KSolidAngleUnit.STERADIAN`)

النوع: **وحدة مركّبة**

الزاوية المجسّمة هي الزاوية ثنائية البُعد: نسبة سطح كرة يقتطعها مخروط. إنّها وحدة **مركّبة** —
`1 sr = 1 rad²` — لكن لأنّ الستراديان وحدة SI مسمّاة بشكل مستقلّ ولها مفرداتها الخاصة (الدرجة المربّعة، السپات)، فهي
تُنمذَج كمجموعتها الخاصة بها بغلاف من حدّ واحد.

يغلّف `KSolidAngleUnitInstance` نسخةَ `KMixedUnitInstance` من حدّ واحد هو `KSolidAngleUnit.BASE` عند الأُسّ 1، مطبَّعًا
دائمًا إلى ستراديان. الجسر إلى مجموعة [الزاوية](angle.md) هو المعامل المحكوم بالنوع
`angle * angle` وخطّاف التعرّف على الصيغة `toSolidAngle()`، الذي يقبل أيضًا الصيغة الأصلية `rad²`.

## الوحدات المسمّاة

| الوحدة             | الرمز  |   الرمز البرمجي |          1 وحدة بـ sr |
|--------------------|--------|----------------:|----------------------:|
| ستراديان           | `sr`   |    `steradians` |                   1.0 |
| درجة مربّعة         | `deg²` | `squareDegrees` | (π/180)² ≈ 3.04617e-4 |
| سپات (الكرة كاملة) | `sp`   |         `spats` |          4π ≈ 12.5664 |

تدعم جميع الوحدات نطاق بادئات SI الكامل (`milli.steradians`، `micro.steradians`).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val full = 1 of spats
full into steradians    // ≈ 12.566
full into squareDegrees // ≈ 41252.96 (السماء بأكملها)
```

## التفكيكات

يمكن الوصول إلى الزاوية المجسّمة بطريقتين متكافئتين؛ وكلتاهما تُختزلان إلى القيمة القياسية نفسها.

| الصيغة                 | Kotlin                                  | نوع النتيجة               |
|------------------------|-----------------------------------------|---------------------------|
| المعامل المحكوم بالنوع | `angle * angle`                         | `KSolidAngleUnitInstance` |
| التعبير الأصلي         | `(angle.toUnit() pow 2).toSolidAngle()` | `KSolidAngleUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val typed = (90 of degrees) * (90 of degrees)
val native = ((90 of degrees).toUnit() pow 2).toSolidAngle()

typed == native            // true - كلاهما (π/2)² sr ≈ 2.4674 sr
typed into steradians      // ≈ 2.4674
```

## الحساب بالزوايا المستوية

| التعبير                   | نوع النتيجة               | المعنى                    |
|---------------------------|---------------------------|---------------------------|
| `angle * angle`           | `KSolidAngleUnitInstance` | زاوية مجسّمة `Ω = φ²`      |
| `solidangle / angle`      | `KAngleUnitInstance`      | الزاوية المستوية المتبقّية |
| `solidangle + solidangle` | `KSolidAngleUnitInstance` | حساب من النوع نفسه        |

## مثال واقعي: زاوية شعاع LED

يُصدر LED شعاعًا مربّعًا مقاسه 30° × 30°. أيّ زاوية مجسّمة يضيئها، وأيّ جزء من الكرة الكاملة تمثّله؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val beam = (30 of degrees) * (30 of degrees)
beam into steradians    // ≈ 0.2742
beam into squareDegrees // 900.0
beam into spats         // ≈ 0.0218 (نحو 2.2 % من الكرة)
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val sum = (3 of steradians) + (1 of steradians) // 4 sr
(1 of spats) > (10 of steradians)               // true
(3 of steradians) * (2 of steradians)           // KMixedUnitInstance (يهرب من المجموعة)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

(2 of steradians).toString()               // "2.0 sr" (الوحدة الأساسية)
"${(1 of spats) into squareDegrees} deg²"  // "41252.96... deg²"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل ضرب بأُسّس سالبة، تُدرَج
الصيغتان المكافئتان في Kotlin.

| الرياضيات     | Kotlin                                    | المعنى                                                        |
|---------------|-------------------------------------------|---------------------------------------------------------------|
| `sr`          | `steradians`                              | الزاوية المجسّمة، الوحدة الأساسية                              |
| `deg²`        | `squareDegrees`                           | درجة مربّعة                                                    |
| `rad²`        | `(radians.toUnit() pow 2).toSolidAngle()` | الزاوية المجسّمة كزاوية مستوية مرفوعة للتربيع (الصيغة الأصلية) |
| `Ω = φ₁ · φ₂` | `angle * angle`                           | التفكيك المحكوم بالنوع                                        |
| `φ = Ω / φ₁`  | `solidangle / angle`                      | محلولة للزاوية المستوية                                       |
| `msr`         | `milli.steradians`                        | زاوية مجسّمة ببادئة                                            |
