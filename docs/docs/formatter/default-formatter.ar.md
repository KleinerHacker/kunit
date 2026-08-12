# المُنسِّق الافتراضي

`KDefaultUnitFormatter` هو المُنسِّق الذي يستخدمه kunit جاهزًا. في كل مرة تستدعي فيها
[`format`](formatting.md) أو `toString` ذا المعاملات **دون** تمرير مُنسِّق خاص بك، فإن هذا المُنسِّق هو الذي ينتج
النتيجة — نصًا بسيطًا قابلًا للقراءة مثل `"10.8 km/h"`. توضح هذه الصفحة بدقة **ماذا** يعرض و **كيف**، مع أمثلة للإخراج،
وتبيّن كيفية استخدامه صراحةً.

إنه `class` غير قابل للتغيير وآمن للخيوط ويقع في الحزمة `org.pcsoft.framework.kunit.formatter`. أنشئه بدون وسائط للسلوك
التاريخي، أو مرّر `KDefaultFormatConfig` لتغيير طريقة العرض.

## ما الذي يُنتجه

يتكوّن النص المعروض من جزأين: **الرقم** وجزء **الوحدة**، مفصولين بمسافة واحدة (`"<الرقم> <الوحدة>"`). إذا كانت القيمة
عديمة الأبعاد (بلا وحدات)، فيُعرض الرقم فقط.

### الرقم

- بدون نمط، يُطبع `Double` الخام بصيغة kunit النصية القابلة للنقل (`10.8`، `5.0`، `1.0E7`) — وهي مطابقة على أي هدف،
  بخلاف `Double.toString()` الخاصة بالمنصة نفسها.
- مع نمط رقمي (و`KLocale` اختياري)، يُطبَّق النمط على الرقم فقط، ولا يؤثر أبدًا على جزء الوحدة. راجع
  [تنسيق الإخراج](formatting.md) للاطلاع على الأنماط المدعومة.

| الاستدعاء                                            | الرقم المعروض        |
|------------------------------------------------------|----------------------|
| `format(kilo.meters / hours)`                        | `10.799999999999999` |
| `format(kilo.meters / hours, "%.1f")`                | `10.8`               |
| `format(kilo.meters / hours, "%.1f", KLocale.DE_DE)` | `10,8`               |

### جزء الوحدة

يُعرض كل حدّ وحدة برمزه **المكتوب الخاص** (مع احترام بيانات عرض البادئة والوحدة البديلة)، لذا تُعرض
`km` و`h` و`mi` و`KiB` كما هي بدلًا من رمز أساس المجموعة. يعتمد الشكل الإجمالي على الحدود:

| الحدود                      | الناتج                |
|-----------------------------|-----------------------|
| وحدة واحدة، أُس 1            | `km`                  |
| أُس ≠ 1                      | `m^2`                 |
| بسط واحد + مقام واحد بالضبط | `km/h`, `m/s^2`       |
| أي شيء آخر                  | `m*s^-3*A^-2`, `s^-1` |
| بلا وحدة (عديم الأبعاد)     | الرقم فقط             |

يُستخدم شكل الكسر المفرد (`a/b`) فقط عندما يوجد حدّ بسط **واحد بالضبط** وحدّ مقام **واحد بالضبط**. كل ما عدا ذلك يُعرض
كجداء مسطّح بأُسّات صريحة (قد تكون سالبة).

## أمثلة للإخراج

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*

(1500 of meters).toString()                          // "1500.0 m"
(3 of meters / seconds).format(kilo.meters / hours)  // "10.799999999999999 km/h"
(3 of meters / seconds).format(meters / seconds, "%.2f") // "3.33 m/s"
(9.81 of meters / (seconds pow 2)).format(meters / (seconds pow 2), "%.2f") // "9.81 m/s^2"
```

## الإعداد

يغيّر `KDefaultFormatConfig` (وهو نوع قيمي) طريقة العرض دون التأثير على قواعد التخطيط:

| الخيار            | القيم                                                          | الافتراضي  |
|-------------------|----------------------------------------------------------------|------------|
| `exponentStyle`   | `CARET` (`m^2`)، `SUPERSCRIPT` (`m²`)                          | `CARET`    |
| `multiplication`  | `ASTERISK` (`*`)، `MIDDLE_DOT` (`·`)، `CROSS` (`×`)            | `ASTERISK` |
| `division`        | `SLASH` (`/`)، `OBELUS` (`÷`)                                  | `SLASH`    |
| `functionSymbols` | `KDefaultFunctionSymbols` — `UNICODE`، `ASCII` (`√`/`sqrt`، …) | `UNICODE`  |

جدول `functionSymbols` (الجذور `√`/`∛`/`∜`، `±`، `∞`، `°`) هو إعداد مُجهَّز للحالات التي ينطبق فيها تمثيل دالّي؛ ومع
الأُسّات الصحيحة البسيطة لا يُستخدم. الإعدادات الجاهزة: `DEFAULT` (الإخراج التاريخي)،
`SUPERSCRIPT` (أُسّات فوقية حقيقية).

```kotlin
import org.pcsoft.framework.kunit.formatter.KDefaultFormatConfig
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter

(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", KLocale.EN_US, KDefaultUnitFormatter(KDefaultFormatConfig.SUPERSCRIPT))
// "9.81 m/s²"
```

## استخدامه صراحةً

يُطبَّق المُنسِّق الافتراضي تلقائيًا، لذا نادرًا ما تحتاج إلى تسميته. ومع ذلك يمكنك تمريره صراحةً — من أجل التماثل مع
مُنسِّق مخصص، أو لجعل الاختيار واضحًا في موضع الاستدعاء:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.formatter.KLocale

val v = 3 of meters / seconds

// مُنسِّق صريح، نفس نتيجة الاستدعاء الافتراضي
v.format(kilo.meters / hours, "%.1f", KLocale.EN_US, KDefaultUnitFormatter()) // "10.8 km/h"

// عرض الوحدات الأساسية بالمُنسِّق الافتراضي دون هدف
(5 of meters).toString(pattern = null, formatter = KDefaultUnitFormatter()) // "5.0 m"
```

لإخراج تدوين مختلف تمامًا، راجع [مُنسِّق مخصص](custom-formatters.md).
