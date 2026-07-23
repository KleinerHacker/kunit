# تنسيق الإخراج

هذه الصفحة هي نظرة عامة على مجموعة **المُنسِّق**. تشرح الفعل `format` — نقطة الدخول لكل عمليات التنسيق.
وهناك صفحتان مخصصتان تتعمّقان أكثر:

- [المُنسِّق الافتراضي](default-formatter.md) — كيف يعرض `KDefaultUnitFormatter` المُرفَق جزء الوحدة
  (الترميز الجاهز)، مع أمثلة للإخراج.
- [مُنسِّق مخصص](custom-formatters.md) — كيفية توصيل عرضك الخاص (LaTeX وMathML وHTML …).

كل قيمة تعرف كيف تطبع نفسها بوحدتها **الأساسية** عبر `toString()`، ويمكن **قراءتها** إلى وحدة محددة باستخدام
[`into`](../mixed-units.md) — لكن `into` تُعيد فقط `Double` مجردًا دون رمز وحدة. الفعل `format` يسدّ هذه الفجوة:
فهو النظير العرضي لـ `into`، ويُعيد القيمة **ورمز** الوحدة معًا كسلسلة `String`.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val v = 3 of meters / seconds

v format kilo.meters / hours       // "10.799999999999999 km/h"
```

مثل `into` تمامًا، يقرأ `format` القيمة أولًا إلى الوحدة الهدف (بنفس فحص الأبعاد ونفس التحويل الأفيني)، ثم يُلحق
رمز الوحدة الهدف. ولأن الهدف يحمل الوحدة كما كُتبت، تُعرض الوحدات ذات البادئات والوحدات البديلة برمزها **الخاص**
(`km`، `h`، `mi`) بدلًا من رمز أساس المجموعة (`m`، `s`).

## تنسيق الأرقام: النمط والإعدادات المحلية

يعرض الشكل الوسطي قيمة `Double` الخام. لتقريب **الجزء الرقمي** أو تعريبه، استخدم تحميل `format` الزائد مع نمط
[`java.util.Formatter`](https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html) و`Locale` اختياري:

```kotlin
import java.util.Locale

v.format(kilo.meters / hours, "%.1f")                // "10.8 km/h"
v.format(kilo.meters / hours, "%.1f", Locale.GERMAN) // "10,8 km/h"
```

يؤثر النمط في **الرقم فقط**؛ ويبقى جزء الوحدة كما هو. النمط غير الصالح يطلق
`java.util.IllegalFormatException`، والبُعد الهدف غير المتوافق يطلق (كما في `into`) `IllegalStateException`.

## الترميز الكسري مقابل ترميز الجداء

يعرض المنسّق المدمج جزء الوحدة كالآتي:

| الحدود                          | الناتج          |
|---------------------------------|-----------------|
| وحدة واحدة، أس 1                | `km`            |
| أس ≠ 1                         | `m^2`           |
| بسط + مقام واحد بالضبط          | `km/h`، `m/s^2` |
| غير ذلك                         | `m*s^-3*A^-2`، `s^-1` |
| بلا وحدة (عديم البُعد)          | الرقم فقط       |

## `toString` مع نمط

يبقى `toString()` بلا وسائط دون تغيير (عرض بالوحدة الأساسية). يضيف تحميل زائد إضافي نفس نمط/إعدادات الأرقام إلى
الإخراج بالوحدة الأساسية — وهو الفعل `format` دون هدف:

```kotlin
(3 of meters / seconds).toString("%.2f", Locale.US) // "3.00 m/s"
(1500 of meters).toString("%.1f", Locale.US)        // "1500.0 m"
```

## مثال واقعي

حوّل سرعة الجري واطبعها بأناقة:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import java.util.Locale

val distance = 10 of kilo.meters
val time = 50 of minutes
val speed = distance / time                    // KSpeedUnitInstance

println(speed.format(kilo.meters / hours, "%.1f", Locale.US)) // "12.0 km/h"
println(speed.format(meters / seconds, "%.2f", Locale.US))    // "3.33 m/s"
```

## عرض مخصص

يُنتج جزء الوحدة عبر [`KUnitFormatter`](custom-formatters.md) قابل للاستبدال؛ ويُنتج `KDefaultUnitFormatter`
المُرفق النص العادي أعلاه — راجع [المُنسِّق الافتراضي](default-formatter.md) لقواعده الدقيقة وأمثلة
الإخراج. لإخراج ترميز مختلف تمامًا — LaTeX أو MathML لعارض صيغ رسومي، أو HTML، ... — نفّذ
منسّقك الخاص ومرّره صراحةً. راجع [المنسّقات المخصصة](custom-formatters.md).
