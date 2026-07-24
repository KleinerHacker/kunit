# مُنسِّق AsciiMath

يعرض `KAsciiMathUnitFormatter` القيمة بصيغة **AsciiMath**، وهي صيغة الإدخال المختصرة لـ MathJax. مع الإعداد
الافتراضي تصبح `3 of meters / seconds` المقروءة كـ `km/h` هي `10.8 "km"/"h"`.

يوجد في الحزمة `org.pcsoft.framework.kunit.formatter` وهو `class` غير قابل للتغيير وآمن للخيوط.

## ما يُنتجه

نمط `FRACTION` يستخدم صيغة الكسر `a/b` للشكل النظيف ذي المقام الواحد (مع تجميع البسط أو المقام المرفوع إلى
أُس بين قوسين عند الحاجة)؛ وكل شكل آخر — وكامل نمط `EXPONENT` — هو جداء مسطّح مربوط بعلامة الضرب مع أُسٍّ
ذات إشارة. القيمة عديمة الأبعاد تُعرض كرقم فقط.

## الإعداد

`KAsciiMathFormatConfig` نوع قيمة؛ اختر إعدادًا مسبقًا أو أنشئ إعدادك:

| الخيار           | القيم                                     | الافتراضي |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `EXPONENT`                   | `FRACTION`|
| `quoting`        | `QUOTED` (`"km"`), `BARE` (`km`)         | `QUOTED`  |
| `multiplication` | `ASTERISK` (`*`), `TIMES` (`xx`), `SPACE` (مسافة) | `SPACE` |

الإعدادات المسبقة: `DEFAULT`، `PLAIN` (رموز بلا اقتباس مربوطة بـ `*`).

## مثال واقعي

السرعة من المسافة والزمن (`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KAsciiMathUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KAsciiMathUnitFormatter())
// 90.0 "km"/"h"
```

التسارع (`a = m/s²`) يجمع المقام المرفوع إلى أُس بين قوسين:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KAsciiMathUnitFormatter())
// 9.81 "m"/("s"^2)
```

لإخراج تدوين مختلف تمامًا، راجع [المُنسِّق المخصص](custom-formatters.md).
