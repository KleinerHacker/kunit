# الضغط

الحزمة: `org.pcsoft.framework.kunit.pressure`
الوحدة الأساسية: **باسكال** (`KPressureUnit.BASE == KPressureUnit.PASCAL`)

النوع: **وحدة مركّبة**

الضغط وحدة **مركّبة**: التركيب `mass · length⁻¹ · time⁻²` (`kg/(m·s²)` = `N/m²`). يغلّف
`KPressureUnitInstance` نسخةَ `KMixedUnitInstance` من ثلاثة حدود — `KMassUnit.BASE` (غرام) عند `+1`،
و`KDistanceUnit.BASE` (متر) عند `-1`، و`KTimeUnit.BASE` (ثانية) عند `-2`. وكما في القوة، القيمة المخزّنة
هي قيمة المكوّن الخام القائمة على الغرام، والقراءات بالباسكال تُقسَم على معامل ثابت.

## بناء ضغط

ابنِ ضغطًا من `force / area`، أو برمز مسمّى. تبقى الوحدات المسمّاة كرموز قيمتها 1 (تُستخدم مع `of`/`into`):

| الضغط | الرمز | الرمز البرمجي | 1 وحدة بـ Pa |
|---|---|---:|---:|
| باسكال | `Pa` | `pascals` | 1.0 |
| بار | `bar` | `bars` | 100000.0 |
| جوّ (أتموسفير) | `atm` | `atmospheres` | 101325.0 |
| رطل لكل إنش مربّع | `psi` | `psis` | 6894.757 |
| تور (mmHg) | `Torr` | `torrs` | 133.322 |

الكتابات القابلة للاشتقاق من البادئات ليست رموزًا مخصّصة: **hPa** = `hecto.pascals`، **kPa** =
`kilo.pascals`، ووحدة الإنشاءات **N/mm² = MPa** = `mega.pascals` (أو التعبير `newtons / (milli.meters pow 2)`).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.pressure.*

val p = 2 of bars
p into pascals               // 200000.0
p into atmospheres           // ≈ 1.974
(1 of mega.pascals) into pascals // 1000000.0 (= 1 N/mm²)
```

## الحساب بالوحدات الأساسية (القوة والمساحة)

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `force / area` | `KPressureUnitInstance` | الضغط = F / A |
| `pressure * area` | `KForceUnitInstance` | القوة = p · A |
| `area * pressure` | `KForceUnitInstance` | القوة (تبادلي) |
| `force / pressure` | `KAreaUnitInstance` | المساحة = F / p |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.pressure.*

val area = (2 of meters) * (1 of meters)   // KAreaUnitInstance، 2 m²
val p = (100 of newtons) / area            // KPressureUnitInstance، 50 Pa
val f = p * area                           // KForceUnitInstance، 100 N
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

val s = (10 of pascals) + (4 of pascals)  // 14 Pa
(2 of bars) > (1 of atmospheres)          // true
(10 of pascals) * (2 of pascals)          // KMixedUnitInstance (يهرب من المجموعة)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

(50 of pascals).toString()   // "50.0 Pa" (الوحدة الأساسية)
"${(1 of bars) into pascals} Pa" // "100000.0 Pa"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `Pa` | `pascals` | الضغط، الوحدة الأساسية (رمز مسمّى، باسكال) |
| `N/m²` | `newtons / (meters pow 2)` | الضغط كقوّة / مساحة (صيغة الكسر) |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | الضغط نفسه كحاصل ضرب صرف |
| `kPa` | `kilo.pascals` | ضغط ببادئة (كيلوباسكال) |
