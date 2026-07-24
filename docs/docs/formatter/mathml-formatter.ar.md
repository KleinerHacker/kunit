# مُنسِّق MathML

يعرض `KMathMlUnitFormatter` القيمة بصيغة **MathML التقديمية**، التي تعرضها المتصفحات و MathJax أصلًا. مع
الإعداد الافتراضي تصبح `3 of meters / seconds` المقروءة كـ `km/h` عنصر `<math>` سطري يحتوي على `<mfrac>`
لـ `<mi>km</mi>` على `<mi>h</mi>`.

يوجد في الحزمة `org.pcsoft.framework.kunit.formatter` وهو `class` غير قابل للتغيير وآمن للخيوط.

## ما يُنتجه

نمط `MFRAC` يرصّ الشكل النظيف ذا المقام الواحد في `<mfrac>`؛ وكل شكل آخر — وكامل نمط `EXPONENT` — هو جداء
مسطّح مربوط بـ `<mo>` للضرب مع أُسٍّ `<msup>` ذات إشارة. القيمة عديمة الأبعاد تُعرض كـ `<mn>` فقط.

## الإعداد

`KMathMlFormatConfig` نوع قيمة؛ اختر إعدادًا مسبقًا أو أنشئ إعدادك:

| الخيار           | القيم                                     | الافتراضي      |
|------------------|------------------------------------------|----------------|
| `fractionStyle`  | `MFRAC`, `EXPONENT`                      | `MFRAC`        |
| `unitTag`        | `MI`, `MTEXT`                            | `MI`           |
| `multiplication` | `MIDDLE_DOT` (`·`), `TIMES` (`×`), `INVISIBLE_TIMES` | `INVISIBLE_TIMES` |
| `wrapper`        | `MATH_INLINE`, `MATH_BLOCK`, `FRAGMENT`  | `MATH_INLINE`  |

الإعدادات المسبقة: `DEFAULT`، `INLINE` (أُسّ `<msup>` سطري)، `FRAGMENT` (بدون جذر `<math>`).

## مثال واقعي

السرعة من المسافة والزمن (`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KMathMlUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>90.0</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>km</mi></mrow><mrow><mi>h</mi></mrow></mfrac></math>
```

التسارع (`a = m/s²`) يضع الأُس في `<msup>` داخل الكسر:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>9.81</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>m</mi></mrow><mrow><msup><mi>s</mi><mn>2</mn></msup></mrow></mfrac></math>
```

لإخراج تدوين مختلف تمامًا، راجع [المُنسِّق المخصص](custom-formatters.md).
