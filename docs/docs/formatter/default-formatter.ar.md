# المُنسِّق الافتراضي

`KDefaultUnitFormatter` هو المُنسِّق الذي يستخدمه kunit جاهزًا. في كل مرة تستدعي فيها
[`format`](formatting.md) أو `toString` ذا المعاملات **دون** تمرير مُنسِّق خاص بك، فإن هذا المُنسِّق هو
الذي ينتج النتيجة — نصًا بسيطًا قابلًا للقراءة مثل `"10.8 km/h"`. توضح هذه الصفحة بدقة **ماذا** يعرض
و**كيف**، مع أمثلة للإخراج، وتبيّن كيفية استخدامه صراحةً.

إنه `object` عديم الحالة (آمن للخيوط) ويقع في الحزمة `org.pcsoft.framework.kunit.formatter`.

## ما الذي يُنتجه

يتكوّن النص المعروض من جزأين: **الرقم** وجزء **الوحدة**، مفصولين بمسافة واحدة (`"<الرقم> <الوحدة>"`).
إذا كانت القيمة عديمة الأبعاد (بلا وحدات)، فيُعرض الرقم فقط.

### الرقم

- بدون نمط، يُطبع `Double` الخام عبر `Double.toString()`.
- مع نمط `java.util.Formatter` (و`Locale` اختياري)، يُعرض الرقم عبر
  `String.format(locale, pattern, value)`. يؤثر النمط على **الرقم فقط**، ولا يؤثر أبدًا على جزء الوحدة.

| الاستدعاء                                         | الرقم المعروض |
|--------------------------------------------------|-----------------|
| `format(kilo.meters / hours)`                    | `10.799999999999999` |
| `format(kilo.meters / hours, "%.1f")`            | `10.8` |
| `format(kilo.meters / hours, "%.1f", Locale.GERMAN)` | `10,8` |

### جزء الوحدة

يُعرض كل حدّ وحدة برمزه **المكتوب الخاص** (مع احترام بيانات عرض البادئة والوحدة البديلة)، لذا تُعرض
`km` و`h` و`mi` و`KiB` كما هي بدلًا من رمز أساس المجموعة. يعتمد الشكل الإجمالي على الحدود:

| الحدود                                    | الناتج                |
|------------------------------------------|-----------------------|
| وحدة واحدة، أُس 1                          | `km`                  |
| أُس ≠ 1                                  | `m^2`                 |
| بسط واحد + مقام واحد بالضبط                | `km/h`, `m/s^2`       |
| أي شيء آخر                                 | `m*s^-3*A^-2`, `s^-1` |
| بلا وحدة (عديم الأبعاد)                    | الرقم فقط              |

يُستخدم شكل الكسر المفرد (`a/b`) فقط عندما يوجد حدّ بسط **واحد بالضبط** وحدّ مقام **واحد بالضبط**. كل ما
عدا ذلك يُعرض كجداء مسطّح بأُسّات صريحة (قد تكون سالبة).

## أمثلة للإخراج

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

(1500 of meters).toString()                          // "1500.0 m"
(3 of meters / seconds).format(kilo.meters / hours)  // "10.799999999999999 km/h"
(3 of meters / seconds).format(meters / seconds, "%.2f") // "3.33 m/s"
(9.81 of meters / (seconds pow 2)).format(meters / (seconds pow 2), "%.2f") // "9.81 m/s^2"
```

## استخدامه صراحةً

يُطبَّق المُنسِّق الافتراضي تلقائيًا، لذا نادرًا ما تحتاج إلى تسميته. ومع ذلك يمكنك تمريره صراحةً — من أجل
التماثل مع مُنسِّق مخصص، أو لجعل الاختيار واضحًا في موضع الاستدعاء:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// مُنسِّق صريح، نفس نتيجة الاستدعاء الافتراضي
v.format(kilo.meters / hours, "%.1f", Locale.US, KDefaultUnitFormatter) // "10.8 km/h"

// عرض الوحدات الأساسية بالمُنسِّق الافتراضي دون هدف
(5 of meters).toString(pattern = null, formatter = KDefaultUnitFormatter) // "5.0 m"
```

لإخراج تدوين مختلف تمامًا، راجع [مُنسِّق مخصص](custom-formatters.md).
