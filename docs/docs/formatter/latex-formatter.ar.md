# مُنسِّق LaTeX

يعرض `KLatexUnitFormatter` القيمة بصيغة **LaTeX** الرياضية، جاهزة لـ MathJax أو KaTeX أو مستند LaTeX. مع
الإعداد الافتراضي تصبح `3 of meters / seconds` المقروءة كـ `km/h` هي `1.5\,\frac{\mathrm{km}}{\mathrm{h}}`.

يوجد في الحزمة `org.pcsoft.framework.kunit.formatter` وهو `class` غير قابل للتغيير وآمن للخيوط.

## ما يُنتجه

يتبع التخطيط القواعد المشتركة: مع نمط `FRACTION` يُرصّ الشكل النظيف ذو المقام الواحد على هيئة `\frac{…}{…}`؛
وكل شكل آخر — وكامل نمط `INLINE` — هو جداء مسطّح مربوط بعلامة الضرب مع أُسٍّ ذات إشارة. القيمة عديمة الأبعاد
تُعرض كرقم فقط.

## الإعداد

`KLatexFormatConfig` نوع قيمة؛ اختر إعدادًا مسبقًا أو أنشئ إعدادك:

| الخيار           | القيم                                     | الافتراضي |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `INLINE`                     | `FRACTION`|
| `unitWrapper`    | `MATHRM`, `TEXT`, `NONE`                 | `MATHRM`  |
| `multiplication` | `CDOT` (`\cdot`), `TIMES` (`\times`), `THIN_SPACE` (`\,`) | `CDOT` |
| `delimiter`      | `DOLLAR` (`$…$`), `PARENTHESES` (`\(…\)`), `NONE` | `NONE` |
| `spacing`        | `THIN` (`\,`), `NORMAL` (مسافة)          | `THIN`    |

الإعدادات المسبقة: `DEFAULT`، `INLINE` (جداء سطري)، `PLAIN` (بدون تغليف ومسافة عادية).

## مثال واقعي

السرعة من المسافة والزمن (`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KLatexUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KLatexUnitFormatter())
// 90.0\,\frac{\mathrm{km}}{\mathrm{h}}
```

التسارع (`a = m/s²`) يُظهر المقام المرفوع إلى أُس داخل الكسر:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KLatexUnitFormatter())
// 9.81\,\frac{\mathrm{m}}{\mathrm{s}^{2}}
```

لإخراج تدوين مختلف تمامًا، راجع [المُنسِّق المخصص](custom-formatters.md).
