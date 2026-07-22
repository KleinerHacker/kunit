# الزمن

الحزمة: `org.pcsoft.framework.kunit.time`
الوحدة الأساسية: **ثانية** (`KTimeUnit.BASE == KTimeUnit.SECOND`)

النوع: **وحدة أصلية**

`KTimeUnitInstance` غلاف بنسبة 100 % حول [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html):
فالـ `Duration` هو المصدر الوحيد للحقيقة (دقيق حتى النانوثانية)، وتُمرَّر واجهة `Duration` كاملةً. وفوق
ذلك يقدّم السطح نفسه مثل كل غلاف وحدة «نقيّة» آخر
(`value`/`+`/`-`/`*`/`/`/`toString`/`toUnit` إضافةً إلى فِعلَي `of`/`into`)، فتنخرط قيمة الزمن في محرّك
الوحدات المختلطة العامّ (مثلًا `length / time` = سرعة). تُخزَّن القيمة دائمًا مُطبَّعة إلى الثواني.

ولأنّ الـ `Duration` لا يمثّل إلّا مدّة بسيطة، تكون قيمة الزمن دائمًا عند أُسّ 1 — لا وجود لغلاف زمن²
أو 1/زمن (الضرب/القسمة «يهرب» إلى `KMixedUnitInstance` خام، تمامًا كالطول). وبالتالي فإنّ
`KMixedUnitInstance.toTime()` يقبل فقط حدّ `KTimeUnit` واحدًا **عند أُسّ 1**.

## الوحدات

| الوحدة | قيمة التعداد | الرمز | الرمز البرمجي | 1 وحدة بالثواني |
|---|---|---|---:|---:|
| ثانية | `KTimeUnit.SECOND` | `s` | `seconds` | 1.0 |
| دقيقة | `KTimeUnit.MINUTE` | `min` | `minutes` | 60.0 |
| ساعة | `KTimeUnit.HOUR` | `h` | `hours` | 3600.0 |
| يوم | `KTimeUnit.DAY` | `d` | `days` | 86 400.0 |

تُنمذَج المقاييس الزمنية الفيزيائية فقط؛ أمّا الوحدات المبنيّة على التقويم (أسبوع، سنة) فمحذوفة عمدًا،
لأنّها مُعرَّفة بالتقاويم لا بكمّية فيزيائية ثابتة. كل `Token` هو `KTimeUnitInstance` قيمته 1 يُستخدم مع
`of` (البناء) و`into` (القراءة).

المقاييس دون الثانية (ميلي، مايكرو، نانو، ...) ليست وحدات مخصّصة — يُوصَل إليها عامًّا عبر بواني بادئات
SI على `seconds` (راجع [بادئات SI](#si) أدناه).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 2 of hours
t.value          // 7200.0 (مُطبَّع إلى الثواني)
t into hours     // 2.0 (يُقرأ ثانيةً بالساعات)
t into minutes   // 120.0
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.*

// + / - : المجموعة نفسها، تحويل تلقائي بين وحدات زمن مختلفة (حساب Duration دقيق)
val a = (1 of hours) + (30 of minutes)   // KTimeUnitInstance، مُطبَّع إلى الثواني (5400.0)
val b = (2 of hours) - (30 of minutes)

// المقارنات
(2 of hours) > (90 of minutes)           // true
(1 of hours) == (60 of minutes)          // true (القيمة المُطبَّعة نفسها)

// * / / : مسموح دائمًا، يُنتج KMixedUnitInstance بأُسّ جديد
val secondsSquared = (3 of seconds) * (4 of seconds)   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = (10 of seconds) / (2 of seconds)           // KMixedUnitInstance: value=5.0، عديم البُعد
```

## المقارنات والمساواة

تقارن `==`، `!=`، `<`، `<=`، `>`، `>=` قيمتَي `KTimeUnitInstance` عبر `Duration` الأساسي (دقيق حتى
النانوثانية). وبما أنّ قيمة الزمن دائمًا عند أُسّ 1، فلا خطأ عدم تطابق أُسّ كما في مساحات/أحجام الطول.

## غلاف `java.time.Duration`

`KTimeUnitInstance` واجهة بديلة مباشرة فوق `Duration`: احصل على الـ `Duration` المُغلَّف، أو غلِّف واحدًا
قائمًا، واستخدم طرق `Duration` المُمرَّرة مباشرةً (التي تُعيد `Duration` تُعيد `KTimeUnitInstance`؛ وطرق
الاستعلام تمرّ كما هي).

```kotlin
import java.time.Duration
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 90 of minutes
t.toDuration()                  // PT1H30M
Duration.ofMinutes(90).toTime() into hours // 1.5

// الطرق المُعدِّلة المُمرَّرة تُعيد KTimeUnitInstance
t.plusHours(1) into hours       // 2.5
t.negated().isNegative()        // true

// طرق الاستعلام المُمرَّرة تمرّ كما هي
t.toHours()             // 1
t.toMinutesPart()       // 30
t.dividedBy(30 of minutes) // 3
```

## بادئات SI

يمكن دمج أي وحدة زمن مع أيٍّ من بواني بادئات SI الـ 24 (`kilo`، `milli`، `micro`، …؛ الحزمة الجذر) عبر
الوصول إلى الخاصّية، مُنتِجًا قالبًا قيمته 1 لـ `of`/`into`. هكذا تُعبَّر المقاييس دون الثانية. لاحظ أنّ
دعامة `Duration` تحدّ المدى القابل للتمثيل (راجع الملاحظة أدناه)، فالبادئات المتطرّفة على أساس بعدّة
ثوانٍ غير قابلة للتمثيل:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.time.*

// البناء: "5 of milli.seconds" -> KTimeUnitInstance
val fiveMillis = 5 of milli.seconds
fiveMillis.value // 0.005 (ثانية)

// قراءة قيمة بوحدة ذات بادئة
val t = 2 of hours
t into milli.seconds  // 7 200 000.0 (ms)
```

!!! note "مدى Duration"
    لأنّ القيمة مدعومة بـ `java.time.Duration` (ثوانٍ صحيحة مخزّنة كـ `Long`، بدقّة نانوثانية)، لا يمكن
    لـ `KTimeUnitInstance` أن يمثّل بأمانة إلّا مقادير ضمن نحو `[1 ns, Long.MAX seconds]`
    (≈ 292 مليار سنة). البادئات المتطرّفة مثل `quetta` مطبَّقة على الأيّام تتجاوز هذا المدى، والقيم دون
    النانوثانية تُقرّب إلى الصفر. أمّا طبقة `KMixedUnitInstance`/البادئات العامّة نفسها فمبنيّة على
    `Double` وغير متأثّرة — التحويل إلى الغلاف المدعوم بـ Duration فقط هو المحدود المدى.

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

(2 of hours).toString()          // "7200.0 s" (تمثيل الوحدة الأساسية)
"${(2 of hours) into hours} h"   // "2.0 h"
"${(2 of hours) into minutes} min" // "120.0 min"
```

## الدمج مع وحدات أخرى

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val speed = (10 of meters) / (1 of seconds)  // KSpeedUnitInstance
speed into (kilo.meters / hours)             // 36.0 (km/h)

// ضرب السرعة ثانيةً بزمن يستعيد طولًا نقيًّا
val distance = speed * (2 of seconds)
distance into meters // 20.0
```

وحدتان نقيّتان من مجموعتين **بلا** معامِل عبر-مجموعات مخصّص (مثلًا `(2 of hours) * (5 of bytes)`) تتّحدان
مباشرةً في `KMixedUnitInstance`، دون حاجة إلى `.toUnit()`.

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `s` | `seconds` | الزمن، الوحدة الأساسية (ثانية) |
| `min` | `minutes` | دقيقة |
| `h` | `hours` | ساعة |
| `ms` | `milli.seconds` | زمن ببادئة (ميلي ثانية) |
| `s⁻¹` | `seconds pow -1` | مقلوب الزمن (يهرب إلى وحدة مختلطة) |
