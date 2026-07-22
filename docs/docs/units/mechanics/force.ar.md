# القوة

الحزمة: `org.pcsoft.framework.kunit.force`
الوحدة الأساسية: **نيوتن** (`KForceUnit.BASE == KForceUnit.NEWTON`)

النوع: **وحدة مركّبة**

القوة وحدة **مركّبة**: التركيب `mass · length · time⁻²` (`kg·m/s²`). يغلّف `KForceUnitInstance` نسخةَ
`KMixedUnitInstance` من ثلاثة حدود — `KMassUnit.BASE` (غرام) عند `+1`، و`KDistanceUnit.BASE` (متر) عند
`+1`، و`KTimeUnit.BASE` (ثانية) عند `-2`. ولأنّ مكوّن الكتلة في المكتبة مُطبَّع إلى **الغرامات** (لا
الكيلوغرامات)، فإنّ النيوتن أكبر 1000× من الأساس الخام للمكوّنات؛ القيمة المخزّنة هي قيمة المكوّن الخام،
والقراءات بالنيوتن تُقسَم على ذلك المعامل الثابت.

## بناء قوة

ابنِ قوة من `mass * acceleration`، أو برمز مسمّى. تبقى الوحدات المسمّاة كرموز قيمتها 1 (تُستخدم مع
`of`/`into`):

| القوة | الرمز | الرمز البرمجي | 1 وحدة بـ N |
|---|---|---:|---:|
| نيوتن | `N` | `newtons` | 1.0 |
| داين | `dyn` | `dynes` | 1.0e-5 |
| رطل-قوة | `lbf` | `poundsForce` | 4.4482216152605 |
| بوند (غرام-قوة) | `p` | `ponds` | 9.80665e-3 |

**الكيلوبوند / الكيلوغرام-قوة (kgf) ليس رمزًا مخصّصًا** — إنّه `kilo.ponds`، تمامًا كما أنّ الكيلونيوتن هو
`kilo.newtons`. تدعم الوحدات المسمّاة بادئات SI عبر `KPrefixBuilder` (`kilo.newtons`، `mega.newtons`،
`kilo.ponds`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f into newtons               // 10.0
f into poundsForce           // ≈ 2.248
(1 of kilo.ponds) into newtons // 9.80665 (1 kp = 1 kgf)
```

## الحساب بالوحدات الأساسية (الكتلة والتسارع)

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `mass * acceleration` | `KForceUnitInstance` | القوة = m · a (قانون نيوتن الثاني) |
| `acceleration * mass` | `KForceUnitInstance` | القوة (تبادلي) |
| `force / mass` | `KAccelerationUnitInstance` | التسارع = F / m |
| `force / acceleration` | `KMassUnitInstance` | الكتلة = F / a |
| `force / area` | `KPressureUnitInstance` | الضغط (راجع الضغط) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*

val f = (2 of kilo.grams) * (3 of standardGravities) // KForceUnitInstance
f into newtons               // ≈ 58.84
val a = (10 of newtons) / (2 of kilo.grams)          // KAccelerationUnitInstance، 5 m/s²
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

val s = (10 of newtons) + (4 of newtons)  // 14 N
(10 of newtons) > (4 of newtons)          // true
(10 of newtons) * (2 of newtons)          // KMixedUnitInstance (يهرب من المجموعة)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

(10 of newtons).toString()   // "10.0 N" (الوحدة الأساسية)
"${(1 of kilo.ponds) into newtons} N" // "9.80665 N"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `N` | `newtons` | القوة، الوحدة الأساسية (رمز مسمّى، نيوتن) |
| `kg·m/s²` | `kilo.grams * meters / (seconds pow 2)` | القوة ككتلة·طول / زمن² (صيغة الكسر) |
| `kg·m·s⁻²` | `kilo.grams * meters * (seconds pow -2)` | القوة نفسها كحاصل ضرب صرف |
| `kN` | `kilo.newtons` | قوة ببادئة (كيلونيوتن) |
