# الكثافة

الحزمة: `org.pcsoft.framework.kunit.density`
الوحدة الأساسية: **كيلوغرام لكل متر مكعّب** (`KDensityUnit.BASE == KDensityUnit.KILOGRAM_PER_CUBIC_METER`)

النوع: **وحدة مركّبة**

الكثافة (الكثافة الكتلية) وحدة **مركّبة**: التركيب `mass · length⁻³` (`kg/m³`). يغلّف
`KDensityUnitInstance` نسخةَ `KMixedUnitInstance` من حدّين — `KMassUnit.BASE` (غرام) عند `+1`،
و`KDistanceUnit.BASE` (متر) عند `-3`. القيمة المخزّنة هي قيمة المكوّن الخام القائمة على الغرام؛ والقراءات
بـ kg/m³ تُقسَم على معامل ثابت.

## بناء كثافة

كالكثافة السطحية، **لا رمز مجرّد** للكثافة — فكل كتابة (kg/m³، g/cm³، …) نسبة. ابنِها كتعبير أو عبر
المعامِل المحكوم بالنوع `mass / volume`، واقرأها ثانيةً بـ `into` مقابل مثل هذا التعبير:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance، 7850 kg/m³
steel into (kilo.grams / (meters pow 3))   // 7850.0
steel into (kilo.grams / (centi.meters pow 3)) // 0.00785 (= 7.85 g/cm³)

val d = (6 of kilo.grams) / (2 of liters)  // 3 kg/L = 3000 kg/m³
```

## الحساب بالوحدات الأساسية (الكتلة والحجم)

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `mass / volume` | `KDensityUnitInstance` | الكثافة = m / V |
| `density * volume` | `KMassUnitInstance` | الكتلة = ρ · V |
| `volume * density` | `KMassUnitInstance` | الكتلة (تبادلي) |
| `mass / density` | `KVolumeUnitInstance` | الحجم = m / ρ |
| `density * length` | `KAreaDensityUnitInstance` | كثافة سطحية (راجع الكثافة السطحية) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

val d = (2 of kilo.grams) / (1 of liters)  // 2 kg/L
val m = d * (3 of liters)                  // KMassUnitInstance
m into kilo.grams                          // 6.0
val v = (6 of kilo.grams) / d              // KVolumeUnitInstance
v into liters                              // 3.0
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val a = (3 of kilo.grams) / (1 of liters)
val b = (1 of kilo.grams) / (1 of liters)
(a - b) into (kilo.grams / (meters pow 3)) // 2000.0
a > b                                       // true
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

((1 of kilo.grams) / (1 of liters)).toString() // "1000.0 kg/m³" (الوحدة الأساسية)
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `kg/m³` | `kilo.grams / (meters pow 3)` | الكثافة، الوحدة الأساسية (كيلوغرام لكل متر مكعّب) — صيغة الكسر |
| `kg·m⁻³` | `kilo.grams * (meters pow -3)` | الكثافة نفسها كحاصل ضرب بأُسّ سالب |
| `g/cm³` | `grams / (centi.meters pow 3)` | غرام لكل سنتيمتر مكعّب |
| `6 kg / 2 L` | `(6 of kilo.grams) / (2 of liters)` | البناء من كتلة ÷ حجم |
