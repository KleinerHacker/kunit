# مُنسِّق الطرفية (Console Formatter)

يعرض `KConsoleUnitFormatter` قيمةً من أجل **طرفية تدعم ANSI**. يُنتج نفس الترميز تمامًا الذي يُنتجه
[المُنسِّق الافتراضي](default-formatter.md) (`"10.8 km/h"`، `"m^2"`، `"m*s^-3*A^-2"`)، لكنه يُحيط كل
عنصر مرئي — الرقم ورموز الوحدات والعوامل والأُسّ — بتسلسلات ألوان ANSI حتى تبرز الأجزاء على الطرفية.

يوجد في الحزمة `org.pcsoft.framework.kunit.formatter`. وعلى خلاف المُنسِّق الافتراضي فهو `class` عادي
(غير قابل للتغيير وآمن للخيوط)، لأنه يحمل لوحة ألوان.

## ما الذي يُنتجه

التخطيط **مطابق** لـ[المُنسِّق الافتراضي](default-formatter.md): صيغة `"<الرقم> <الوحدة>"`، وصيغة
الكسر المفرد `a/b` تُستخدم فقط عند وجود بسط واحد بالضبط ومقام واحد بالضبط، وإلا فحاصل ضرب مسطّح بأُسّ
مُوقَّع، أما القيمة عديمة الأبعاد فتُعرض بالرقم وحده. الفارق الوحيد أن كل جزء يُحاط بلون ANSI SGR ويُغلق
بتسلسل إعادة الضبط `ESC[0m`.

### العناصر المُلوَّنة

تُلوَّن أربعة عناصر مرئية بشكل مستقل عبر [`KConsoleColorPalette`](#لوحات-الألوان):

| العنصر     | حقل اللوحة       | مثال             |
|------------|------------------|------------------|
| الرقم      | `numberColor`    | `10.8`           |
| رمز الوحدة | `symbolColor`    | `km`، `h`، `m`   |
| العامل     | `operatorColor`  | `*`، `/`         |
| الأُسّ      | `exponentColor`  | `^2`، `^-3`      |

العنصر الذي لونه **سلسلة فارغة** يُخرَج دون أي تسلسل هروب (يبقى الجزء بلا لون) — وهكذا تترك
`MONOCHROME` الأُسّ بلا لون.

## لوحات الألوان

الألوان نوع قيمي `KConsoleColorPalette`. توجد ثلاث لوحات مُعرَّفة مسبقًا:

| اللوحة       | الرقم                          | الرمز                 | العامل          | الأُسّ                    |
|--------------|--------------------------------|-----------------------|-----------------|--------------------------|
| `CLASSIC`    | سماوي `ESC[36m`               | أصفر `ESC[33m`        | رمادي `ESC[90m` | أرجواني `ESC[35m`        |
| `VIVID`      | أخضر فاتح عريض `ESC[92;1m`    | أزرق فاتح `ESC[94m`   | أبيض `ESC[97m`  | أرجواني فاتح `ESC[95m`   |
| `MONOCHROME` | عريض `ESC[1m`                | خافت `ESC[2m`         | خافت `ESC[2m`   | بلا لون (فارغ)           |

- `CLASSIC` هادئة وسهلة القراءة على طرفية داكنة، وهي **الافتراضية**.
- `VIVID` عالية التباين ولافتة.
- `MONOCHROME` تستخدم السطوع فقط (بلا ألوان) للطرفيات محدودة الألوان.

## طريقة الاستخدام

أنشئه دون وسائط لاستخدام لوحة `CLASSIC` الافتراضية، أو مرِّر لوحة مُعرَّفة مسبقًا أو مخصَّصة. ثم يُسلَّم
إلى الفعل [`format`](formatting.md) (أو `toString` ذي المعاملات) مثل أي مُنسِّق آخر:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// لوحة CLASSIC الافتراضية
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter())

// لوحة مُعرَّفة مسبقًا
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter(KConsoleColorPalette.VIVID))

// العرض بالوحدة الأساسية دون هدف
(5 of meters).toString(pattern = "%.1f", formatter = KConsoleUnitFormatter(KConsoleColorPalette.MONOCHROME))
```

## تعريف لوحتك الخاصة

`KConsoleColorPalette` صنف بيانات بسيط، فيمكنك تزويده بتسلسلات ألوانك الخاصة. يحمل كل حقل **تسلسل
البدء** لـ ANSI (مثل `ESC[31m` للأحمر، حيث `ESC` هو محرف الهروب برمز 27)؛ ويُضاف `reset` المشترك
(الافتراضي `ESC[0m`) بعد كل جزء مُلوَّن:

```kotlin
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter

val esc = 27.toChar()   // محرف الهروب ANSI (ESC)
val myPalette = KConsoleColorPalette(
    numberColor = "$esc[31m",   // أحمر
    symbolColor = "$esc[32m",   // أخضر
    operatorColor = "$esc[34m", // أزرق
    exponentColor = "$esc[35m", // أرجواني
)
val formatter = KConsoleUnitFormatter(myPalette)
```

لإخراج ترميز مختلف تمامًا (وليس مجرد ألوان)، نفِّذ بدلًا من ذلك
[مُنسِّقًا مخصصًا](custom-formatters.md) خاصًا بك.
