# الكثافة السطحية

الحزمة: `org.pcsoft.framework.kunit.areadensity`
الوحدة الأساسية: **كيلوغرام لكل متر مربّع** (`KAreaDensityUnit.BASE == KAreaDensityUnit.KILOGRAM_PER_SQUARE_METER`)

النوع: **وحدة مركّبة**

الكثافة السطحية (الكتلة السطحية / الحمل المساحي، شائعة في إنشاءات البناء) وحدة **مركّبة**: التركيب
`mass · length⁻²` (`kg/m²`). يغلّف `KAreaDensityUnitInstance` نسخةَ `KMixedUnitInstance` من حدّين —
`KMassUnit.BASE` (غرام) عند `+1`، و`KDistanceUnit.BASE` (متر) عند `-2`. القيمة المخزّنة هي قيمة المكوّن
الخام القائمة على الغرام؛ والقراءات بـ kg/m² تُقسَم على معامل ثابت.

## بناء كثافة سطحية

كالكثافة، **لا رمز مجرّد** للكثافة السطحية — فكل كتابة (kg/m²، g/mm²، …) نسبة. ابنِها كتعبير أو عبر
المعامِل المحكوم بالنوع `mass / area`، واقرأها ثانيةً بـ `into` مقابل مثل هذا التعبير:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val q = (25 of kilo.grams) / ((5 of meters) * (1 of meters)) // KAreaDensityUnitInstance، 5 kg/m²
q into (kilo.grams / (meters pow 2))       // 5.0
q into (grams / (milli.meters pow 2))      // 0.005 (= معبَّرًا عنه لكل mm²)
```

## الحساب بالوحدات الأساسية (الكتلة والمساحة والكثافة)

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `mass / area` | `KAreaDensityUnitInstance` | الكثافة السطحية = m / A |
| `area density * area` | `KMassUnitInstance` | الكتلة = q · A |
| `area * area density` | `KMassUnitInstance` | الكتلة (تبادلي) |
| `mass / area density` | `KAreaUnitInstance` | المساحة = m / q |
| `density * length` | `KAreaDensityUnitInstance` | صفيحة بمادّة وسُمك معطيَين |
| `area density / length` | `KDensityUnitInstance` | العودة إلى الكثافة الحجمية |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*
import org.pcsoft.framework.kunit.areadensity.*

// صفيحة فولاذ بسُمك 3 m: الكثافة × السُمك = الكتلة السطحية
val density = (2 of kilo.grams) / (1 of liters)      // 2000 kg/m³
val q = density * (3 of meters)                      // KAreaDensityUnitInstance
q into (kilo.grams / (meters pow 2))                 // 6000.0
val back = q / (3 of meters)                         // KDensityUnitInstance، 2000 kg/m³
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val area = (5 of meters) * (1 of meters)
val a = (15 of kilo.grams) / area   // 3 kg/m²
val b = (5 of kilo.grams) / area    // 1 kg/m²
(a - b) into (kilo.grams / (meters pow 2)) // 2.0
a > b                                       // true
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.areadensity.*

((5 of kilo.grams) / ((5 of meters) * (1 of meters))).toString() // "1.0 kg/m²" (الوحدة الأساسية)
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `kg/m²` | `kilo.grams / (meters pow 2)` | الكثافة السطحية، الوحدة الأساسية (كيلوغرام لكل متر مربّع) — صيغة الكسر |
| `kg·m⁻²` | `kilo.grams * (meters pow -2)` | الكثافة السطحية نفسها كحاصل ضرب بأُسّ سالب |
| `g/mm²` | `grams / (milli.meters pow 2)` | غرام لكل ملّيمتر مربّع |
| `25 kg / (5 m · 1 m)` | `(25 of kilo.grams) / ((5 of meters) * (1 of meters))` | البناء من كتلة ÷ مساحة |
