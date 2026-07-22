# التسارع

الحزمة: `org.pcsoft.framework.kunit.acceleration`
الوحدة الأساسية: **متر لكل ثانية تربيعًا** (`KAccelerationUnit.BASE == KAccelerationUnit.METERS_PER_SECOND_SQUARED`)

النوع: **وحدة مركّبة**

التسارع وحدة **مركّبة**: التركيب `length · time⁻²` (`m/s²`). يغلّف `KAccelerationUnitInstance` نسخةَ
`KMixedUnitInstance` من حدّين تحديدًا — `KDistanceUnit.BASE` (متر) عند أُسّ `+1` و`KTimeUnit.BASE`
(ثانية) عند أُسّ `-2`. تُخزَّن القيمة دائمًا مُطبَّعة إلى m/s². ولأنّ الوحدة الأساسية تتطابق مع الوحدات
الأساسية للمكوّنات (متر، ثانية)، لا يوجد معامل تحجيم إضافي.

## بناء تسارع

يُبنى التسارع عادةً من `speed / time`، أو برمز مسمّى. لا يوجد عمدًا رمز `metersPerSecondSquared` (فهو
بالضبط `meters / (seconds pow 2)`). تبقى فقط الوحدات المسمّاة حقًّا كرموز قيمتها 1 (تُستخدم مع `of`/`into`):

| التسارع | الرمز | الرمز البرمجي | 1 وحدة بـ m/s² |
|---|---|---:|---:|
| غال (غاليليو) | `Gal` | `gals` | 0.01 (1 cm/s²) |
| جاذبية قياسية | `g₀` | `standardGravities` | 9.80665 |

يدعم كلا الرمزين جدول بادئات SI الكامل (مثلًا `milli.gals` = 1 mGal، وحدة قياس الجاذبية اليومية).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.acceleration.*

val a = 5 of gals               // KAccelerationUnitInstance
a.value                         // 0.05 (مُطبَّع إلى m/s²)
a into standardGravities        // ≈ 0.0051
(1 of milli.gals).value         // 0.00001 (1 mGal)
```

## الحساب بالوحدات الأساسية (السرعة والزمن)

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `speed / time` | `KAccelerationUnitInstance` | التسارع = Δسرعة / المدّة |
| `acceleration * time` | `KSpeedUnitInstance` | السرعة = التسارع × المدّة |
| `time * acceleration` | `KSpeedUnitInstance` | السرعة (تبادلي) |
| `speed / acceleration` | `KTimeUnitInstance` | المدّة = السرعة / التسارع |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((100 of meters) / (10 of seconds)) / (5 of seconds) // KAccelerationUnitInstance، 2 m/s²
val v = a * (3 of seconds)      // KSpeedUnitInstance، 6 m/s
val t = ((100 of meters) / (10 of seconds)) / a             // KTimeUnitInstance
t into seconds                  // 5.0
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.acceleration.*

// + / - : المجموعة نفسها، تحويل تلقائي بين تعابير تسارع مختلفة
val s = (10 of gals) + (4 of gals)   // 0.14 m/s²
(10 of gals) > (4 of gals)           // true
// * / / بين تسارعين يهربان إلى KMixedUnitInstance
(10 of gals) * (2 of gals)           // KMixedUnitInstance
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.acceleration.*

(1 of gals).toString()               // "0.01 m/s²" (الوحدة الأساسية)
"${(1 of standardGravities) into gals} Gal" // "980.665 Gal"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `m/s²` | `meters / (seconds pow 2)` | التسارع، الوحدة الأساسية (متر لكل ثانية تربيعًا) — صيغة الكسر |
| `m·s⁻²` | `meters * (seconds pow -2)` | التسارع نفسه كحاصل ضرب بأُسّ سالب |
| `Gal` | `gals` | وحدة مسمّاة (1 cm/s²) |
| `v / t` | `speed / time` | البناء من سرعة ÷ زمن |
