# الديناميكا الحرارية — نظرة عامة

الحزم: `org.pcsoft.framework.kunit.thermo.temperature`، `…energy`، `…power`

الديناميكا الحرارية هي فيزياء **الحرارة ودرجة الحرارة**. في KUnit يتمحور هذا المجال حول درجة الحرارة،
التي تُنمذَج بـ**مجموعتين أصليتين مترابطتين** — لأن *قراءة* درجة الحرارة و*تغيّرها* كمّيتان مختلفتان
فيزيائيًا، والتمييز بينهما هو ما يجعل الحساب صحيحًا. وحول هاتين المجموعتين تقع الكمّيتان **المركّبتان**
لكل موازنة حرارية: الحرارة نفسها (الطاقة) والمعدّل الذي تتدفّق به (القدرة).

## وحدات هذا الموضوع

| الوحدة | النوع | الطبيعة | الوحدة الأساسية | الصفحة |
|---|---|---|---|---|
| درجة الحرارة المطلقة | أصلية | **نقطة** أفينية | كلفن (`K`) | [درجة الحرارة المطلقة](temperature.md) |
| فرق درجة الحرارة | أصلية | **فترة** خطّية | كلفن (`ΔK`) | [فرق درجة الحرارة](temperature-difference.md) |
| الطاقة | مركّبة | كمّية خطّية | جول (`J`) | [الطاقة (ديناميكا حرارية)](energy.md) |
| القدرة | مركّبة | كمّية خطّية | واط (`W`) | [القدرة (ديناميكا حرارية)](power.md) |

الطاقة (الحرارة) والقدرة (معدّل تدفّق الحرارة) هما تقنيًا كمّية **واحدة** لكل منهما، مشتركة مع مجالات
موضوعية أخرى؛ وتُوثَّقان لكل مجال مع إحالة متبادلة بينها ([الطاقة (كهربائية)](../electrical/energy.md)،
[الطاقة (ميكانيكا)](../mechanics/energy.md)، [القدرة (كهربائية)](../electrical/power.md)،
[القدرة (ميكانيكا)](../mechanics/power.md)).

توضّح [نظرة عامة على درجة الحرارة](temperature-overview.md) المخصّصة التمييز بين النقطة والفترة بعمق؛
هذه الصفحة هي مدخل مجال الديناميكا الحرارية بأكمله.

## نقطة مقابل فترة — قواعد المعاملات

| العملية | النتيجة |
|---|---|
| `درجة مطلقة − درجة مطلقة` | **فرق درجة الحرارة** |
| `درجة مطلقة + فرق` | درجة الحرارة المطلقة |
| `درجة مطلقة − فرق` | درجة الحرارة المطلقة |
| `فرق ± فرق` | فرق درجة الحرارة |
| `درجة مطلقة + درجة مطلقة` | **خطأ تصريف** (بلا معنى فيزيائي) |

## الحرارة وتدفّق الحرارة كمعاملات محكومة بالنوع

| التعبير | النتيجة | الصيغة |
|---|---|---|
| `power * time` | الطاقة (حرارة) | `Q = Φ · t` |
| `energy / time` | القدرة (تدفّق حراري) | `Φ = Q / t` |
| `energy / power` | الزمن | `t = Q / Φ` |
| `power / frequency` | الطاقة | `Q = Φ / f` |

## مثال واقعي — خطوة تسخين

يُسخَّن ماء من **10 °C** إلى **30 °C**. هذا *التغيّر* هو **فرق** في درجة الحرارة (`ΔT`)، وهو الكمّية
التي تدخل في صيغ الحرارة مثل `Q = m · c · ΔT`؛ تُلغى نقطة الصفر، فتتّفق `°C` و`K` على مقدار الخطوة:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.*

val start = 10 of celsius
val end   = 30 of celsius

val deltaT = end - start                     // KTemperatureDifferenceUnitInstance: 20 ΔK
deltaT.value                                 // 20.0 (فترة كلفن)

val back = start + KTemperatureDifference.ofKelvin(20) // KTemperatureUnitInstance: 303.15 K
```

## مثال واقعي — حرارة ووقت تسخين سخّان ماء

يعمل سخّان ماء بقدرة **2 kW** لمدّة **10 دقائق**. الحرارة المُسلَّمة هي `Q = Φ · t`؛ وقسمتها على تدفّق
الحرارة يُعيد وقت التسخين:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.common.energy.*

val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0
q into kilo.calories                          // ≈ 286.8 (kcal)

val t = q / (2 of kilo.watts)                 // KTimeUnitInstance
t into seconds                                // 600.0
```

## طباعة قيمة (`toString`)

تُخرج `toString()` القيمة بالوحدة **الأساسية** لمجموعتها (كلفن): تُطبَع درجة الحرارة المطلقة كـ `K`،
والفرق بالرمز المميّز `ΔK`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.*

(25 of celsius).toString()                       // "298.15 K" (مطلقة، الوحدة الأساسية)
KTemperatureDifference.ofKelvin(20).toString()   // "20.0 ΔK" (فترة)
```

## الترميز

يعرض الجدول علاقات درجة الحرارة بالترميز الرياضي مقابل ترميز Kotlin في KUnit. يرمز `Δ` إلى كمّية فترة،
متمايزة عمدًا عن نقطة مطلقة.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `ΔT = T₂ − T₁` | `(30 of celsius) - (10 of celsius)` | فرق من درجتَي حرارة مطلقتين |
| `T + ΔT` | `(10 of celsius) + KTemperatureDifference.ofKelvin(20)` | درجة حرارة مطلقة مُزاحة بفترة |
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | فترة درجة حرارة صريحة |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | مجموع فترتين |
| `Q = Φ · t` | `(2 of kilo.watts) * (10 of minutes)` | الحرارة من تدفّق الحرارة × الزمن |
| `Φ = Q / t` | `(1200 of kilo.joules) / (10 of minutes)` | تدفّق الحرارة من الحرارة ÷ الزمن |

## إلى أين بعد ذلك

* [نظرة عامة على درجة الحرارة](temperature-overview.md) — النقاش الكامل بين النقطة والفترة ولماذا يهمّ
  فيزيائيًا (الطاقة الحرارية، الإشعاع، قانون الغاز المثالي).
* [درجة الحرارة المطلقة](temperature.md) — كلفن، سلسيوس، فهرنهايت، رانكن والمعاملات الأفينية.
* [فرق درجة الحرارة](temperature-difference.md) — مجموعة فترة الكلفن الخطّية.
* [الطاقة (ديناميكا حرارية)](energy.md) — الجول كحرارة، إضافة إلى السعرة الحرارية ووحدة BTU.
* [القدرة (ديناميكا حرارية)](power.md) — الواط كمعدّل تدفّق حراري، `Q / t`.
