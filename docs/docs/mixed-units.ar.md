# الوحدات المركبة

**الوحدة المختلطة** قيمة مؤلَّفة من عدّة وحدات `KUnit`، كلّ منها مرفوعة
إلى أُسّها الخاصّ، مثلًا `m^1 * s^-1` لسرعة، أو `m^1 * kg^1 * s^-2` لقوة. في kunit يمثّلها الصنف العامّ
`KMixedUnitInstance`.

بينما تكون أصناف الغلاف الخاصّة بالمجموعة (مثل `KLengthUnitInstance`، راجع
[الوحدات المعرّفة مسبقًا](units/kinematics/distance.md)) ملائمة للعمل ببُعد فيزيائي واحد، فإنّ
`KMixedUnitInstance` هو ما تلجأ إليه حين تحتاج إلى دمج وحدات من مجموعات **مختلفة**، أو حين لا تريد التحويل
التلقائي ضمن المجموعة الذي توفّره أصناف الغلاف.

## البنية

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KMixedUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value` هو المقدار `Double` المُطبَّع، نسبةً دائمًا إلى الوحدات والأُسّس المدرجة في `units` تحديدًا —
  وخلافًا لأغلفة المجموعات، لا يُجري `KMixedUnitInstance` **أي** تطبيع إلى وحدة أساس مجموعة.
- `units` هي قائمة أزواج `(KUnit, exponent)` التي تصف البُعد الفيزيائي.

تكشف كل وحدة «نقيّة» امتداد `toUnit()` للتحويل إلى هذا التمثيل العامّ:

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5 of meters
val mixed = d.toUnit() // KMixedUnitInstance: value=5.0, units=[METER^1]
```

## الضرب والقسمة

`*` و`/` **مسموحان دائمًا** بين وحدتين مختلطتين `KMixedUnitInstance` — لا قيد بُعدي، لأنّ ضرب/قسمة
الوحدات ذو معنى فيزيائي دائمًا.

- يجمع `*` أُسّس الوحدات المتطابقة، ويحمل ببساطة أي وحدة موجودة على جانب واحد فقط.
- يطرح `/` أُسّس الجانب الأيمن من الوحدات المتطابقة (ويعكس إشارة أُسّ الوحدات الموجودة على الجانب الأيمن
  فقط).
- الأُسّ الناتج `0` يزيل تلك الوحدة من النتيجة كليًا.

```kotlin
import org.pcsoft.framework.kunit.distance.*

val distance = (10 of meters).toUnit()   // units=[METER^1]
val width = (4 of meters).toUnit()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

دمج مجموعتَي وحدات مختلفتين (مثلًا الطول والزمن حين يتوفّر) يعمل بالطريقة نفسها تمامًا وينتج وحدة مختلطة
فعليًا:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

val distance = (100 of meters).toUnit()
val time = (10 of seconds).toUnit()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## التحجيم برقم مجرّد

يمكن تحجيم أي قيمة وحدة برقم `Number` مجرّد. هذه عملية **مقدار فقط**: تُغيّر القيمة لكنّها تترك حدود
الوحدات والأُسّس دون تغيير، فتحتفظ النتيجة بنوعها وبُعدها.

- `unit * n` و`n * unit` و`unit / n` تُعيد كلّها **الوحدة المحكومة بالنوع نفسها** (يبقى الطول طولًا،
  والمساحة مساحة).
- `n / unit` **يعكس** البُعد (يُنفى كل أُسّ) وبالتالي يُنتج `KMixedUnitInstance` عامًّا — الطريقة
  الاصطلاحية لبناء مقلوب كتردّد من دور.
- لا يوجد عمدًا `+`/`-` قياسي: جمع رقم لا بُعدي إلى قيمة ذات بُعد بلا معنى.

مثال واقعي — مساحة دائرة، `A = π · r²`، محسوبة بالكامل عبر نظام الوحدات:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.distance.*

val r = 12 of centi.meters       // KLengthUnitInstance، 0.12 m
val area = Math.PI * (r * r)     // KAreaUnitInstance: π·r² ≈ 0.04524 m²
area into (meters * meters)      // ≈ 0.04524 (أمتار مربّعة)
```

تحجيم طول مجرّد أو تقسيم مسار إلى مراحل متساوية يعمل بالطريقة نفسها:

```kotlin
val tripled = (12 of meters) * 3 // KLengthUnitInstance، 36 m
val leg = (10 of kilo.meters) / 4 // KLengthUnitInstance، 2.5 km (رُبع المسار)
```

قسمة رقم **على** وحدة تعكس البُعد، مثلًا تردّد من دور:

```kotlin
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.time.seconds

val frequency = 1 / (2 of seconds) // KMixedUnitInstance: value=0.5, units=[SECOND^-1]  (0.5 Hz)
```

مجموعة **درجة الحرارة المطلقة** الأفينية هي الاستثناء الوحيد: تحجيم درجة حرارة مطلقة برقم بلا معنى فيزيائي
(قيمتها بالكلفن تحمل إزاحة −273.15)، فـ `(20 of celsius) * 2` **خطأ تصريف**. حجّم **فرق درجة حرارة**
خطّيًا بدلًا من ذلك (راجع [فرق درجة الحرارة](units/thermodynamics/temperature-difference.md)).

## الجمع والطرح

خلافًا لـ `*`/`/`، لا يُسمح بـ `+` و`-` إلّا بين وحدتين مختلطتين تصفان **البُعد الفيزيائي نفسه**: لكل حدّ
على جانب يجب أن يوجد حدّ واحد بالضبط على الجانب الآخر ينتمي إلى مجموعة الوحدات نفسها (مثلًا كل قيم
`KDistanceUnit`) بالأُسّ نفسه (بصرف النظر عن الترتيب). لا يلزم أن تكون وحدات `KUnit` نفسها متطابقة —
تُحوَّل الحدود المتطابقة تلقائيًا عبر التطبيع، تمامًا كما تفعل أصناف الغلاف الخاصّة بالمجموعة
(`KLengthUnitInstance`، إلخ) للوحدات «النقيّة». تُعبَّر النتيجة بـ `units` المعامِل الأيسر.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.miles

val a = (5 of meters).toUnit()
val b = (3 of meters).toUnit()
(a + b).value // 8.0

val c = (3 of miles).toUnit()
(a + c).value // 4832.032 (تُحوَّل 3 أميال إلى أمتار ثم تُجمع)، units=[METER^1]
```

اختلاف مجموعات الوحدات أو اختلاف الأُسّس ما زال يفشل:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

a + (3 of seconds).toUnit()       // يُطلق IllegalStateException: لا مجموعة وحدات مطابقة لحدّ زمني
a + ((2 of meters) pow 2).toUnit() // يُطلق IllegalStateException: أُسّس غير متطابقة (1 مقابل 2)
```

استخدم `hasSameUnits` للتحقّق مسبقًا من تطابق **تامّ** (وحدات `KUnit` نفسها، لا مجرّد المجموعة نفسها):

```kotlin
val x = (5 of meters).toUnit()
val y = (3 of meters).toUnit()
x.hasSameUnits(y) // يقارن توقيع (unit -> exponent)، بصرف النظر عن الترتيب
```

## قراءة القيم

يقرأ `into` القيمة في قالب وحدة هدف (رمز مجرّد، أو قالب باني ببادئة، أو نسخة خاصّة قيمتها 1)، مُعيدًا
`Double` بسيطًا. يجب أن يصف الجانبان البُعد الفيزيائي نفسه. لا يوجد `valueAs` ولا `toString` لوحدة مخصصة؛
نسّق وحدة محدّدة كـ `"${v into kilo.meters} km"`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (1 of seconds)

speed into (kilo.meters / hours)   // 36.0 (km/h)

val area = (200 of meters) * (50 of meters)
area into hectares                 // 1.0
```

تستخدم `toString()` الافتراضية (بلا وسائط) دائمًا رمز `KUnit.symbol` الخاصّ بكل حدّ، مفصولةً بـ `*`،
مثلًا `"5.0 m*s^-1"`.

## دمج الوحدات النقيّة والمختلطة

يدعم كل صنف غلاف وحدة نقيّة `*`/`/` مباشرةً مع `KMixedUnitInstance`، فنادرًا ما تحتاج إلى استدعاء
`toUnit()` صراحةً لهذه المعاملات:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*

val distance = 100 of meters        // KLengthUnitInstance
val mixed = distance.toUnit()       // KMixedUnitInstance

val combined = distance * mixed              // KMixedUnitInstance: METER^2
```

## التحويل عائدًا إلى وحدة نقيّة

بمجرّد أن يمثّل `KMixedUnitInstance` مجدّدًا حدًّا واحدًا بالضبط من مجموعة وحدات واحدة، يمكن تحويله عائدًا
إلى صنف غلاف تلك المجموعة عبر امتداد `toXxxUnit()` الخاصّ بالمجموعة (مثلًا `toDistance()`):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (2 of seconds)    // KSpeedUnitInstance
val distanceAgain = speed.toUnit() * (2 of seconds).toUnit() // units=[METER^1]
distanceAgain.toDistance().value               // 10.0

val area = (200 of meters) * (50 of meters)    // KAreaUnitInstance
area.toUnit().toDistance().value               // 10000.0 (مساحة، أُسّ 2)
```

إذا لم يتألّف `KMixedUnitInstance` من حدّ واحد بالضبط من تلك المجموعة (مثلًا ما زال قيمة طول/زمن مختلطة)،
فإنّ التحويل يُطلق `IllegalStateException`.

التضييق نفسه متاح **مباشرةً على قيمة مسافة** (لا على `KMixedUnitInstance` فقط): يمكن تضييق
`KDistanceUnitInstance` عامّ — أو أي ورقة — إلى بُعد محدّد بـ `toLength()` أو `toArea()` أو `toVolume()`،
وهي مُتحقَّقة من الأُسّ وتُطلق `IllegalStateException` عند عدم التطابق:

```kotlin
val area = (200 of meters) * (50 of meters)  // KAreaUnitInstance (أُسّ 2)
area.toArea().value                          // 10000.0
area.toDistance().toArea().value             // 10000.0 (تُوسَّع ثم تُضيَّق ثانيةً)
area.toLength()                              // IllegalStateException (أُسّ 2، لا 1)
```
