# المُنسِّق الرسومي

يعرض `KGraphicalConsoleUnitFormatter` القيمة **رسوميًا عبر عدة أسطر** لطرفية تدعم ANSI: يُرسم الكسر ككومة
ثنائية الأبعاد حقيقية — البسط، خط أفقي، المقام — مع وضع القيمة على سطر الخط (الأوسط). تُكتب الأُسّ دائمًا
كأرقام علوية Unicode حقيقية، ويُلوَّن كل دور مرئي عبر `KGraphicalConsoleColorPalette`.

يوجد في الحزمة `org.pcsoft.framework.kunit.formatter` وهو `class` غير قابل للتغيير وآمن للخيوط.

## ما يُنتجه

الشكل النظيف ذو المقام الواحد يُرصّ في ثلاثة أسطر؛ وكل شكل آخر هو جداء من سطر واحد مربوط بعلامة الضرب مع
أُسّ علوية؛ والقيمة عديمة الأبعاد هي الرقم الملوَّن وحده. يُوسَّط البسط والمقام فوق الخط باستخدام عرضهما
**المرئي** (تسلسلات ألوان ANSI لا تُحسب ضمن العرض). التسارع `9.81 m/s²` يُرسم (بلا تلوين) هكذا:

```
     m
9.81 ──
     s²
```

## الإعداد

`KGraphicalConsoleFormatConfig` نوع قيمة؛ اختر الإعداد `DEFAULT` أو أنشئ إعدادك:

| الخيار            | القيم / النوع                                    | الافتراضي  |
|-------------------|-------------------------------------------------|------------|
| `palette`         | `KGraphicalConsoleColorPalette` — `CLASSIC`, `VIVID`, `MONOCHROME` | `CLASSIC` |
| `fractionBar`     | `LINE` (`─`), `HEAVY` (`━`), `ASCII` (`-`)      | `LINE`     |
| `multiplication`  | `ASTERISK` (`*`), `MIDDLE_DOT` (`·`), `CROSS` (`×`) | `MIDDLE_DOT` |
| `functionSymbols` | `KGraphicalFunctionSymbols` — `UNICODE`, `ASCII` | `UNICODE`  |

تُلوِّن اللوحة خمسة أدوار: الرقم والرمز والمعامل والأُس والخط. الدور الذي لونه سلسلة فارغة يبقى بلا تلوين
(هكذا يترك `MONOCHROME` الأُس بلا تلوين).

## مثال واقعي

السرعة والتسارع في الطرفية:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KGraphicalConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

// لوحة CLASSIC الافتراضية (حُذفت الألوان هنا)؛ التخطيط:
//      km
// 90.0 ──
//      h
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter()))

// خط أفقي سميك
val config = KGraphicalConsoleFormatConfig(fractionBar = KGraphicalFractionBar.HEAVY)
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter(config)))
```

لإخراج تدوين مختلف تمامًا، راجع [المُنسِّق المخصص](custom-formatters.md).
