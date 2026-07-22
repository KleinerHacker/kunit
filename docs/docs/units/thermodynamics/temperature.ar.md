# درجة الحرارة المطلقة

> جزء من موضوع **درجة الحرارة** — راجع [النظرة العامة](temperature-overview.md) والنظير الخطّي
> [فرق درجة الحرارة](temperature-difference.md).

الحزمة: `org.pcsoft.framework.kunit.temperature`
الوحدة الأساسية: **كلفن** (`KTemperatureUnit.BASE == KTemperatureUnit.KELVIN`)

النوع: **وحدة أصلية**

تُنمذج مجموعة درجة الحرارة درجة حرارة ترموديناميكية. وهي **أوّل استثناء أفيني (ودائم بحكم التصميم)** في
الإطار: فخلافًا لكل مجموعة أخرى، التحويل بين وحدات الحرارة ليس معامل ضرب واحدًا بل تحويل **إزاحة-وتحجيم**
(أفيني) — فـ `25 °C` *ليست* `25 × 1 °C`. تُخزَّن القيم مُطبَّعة إلى **كلفن المطلق**، فيبقى
`*`/`/`/`pow` يعمل عبر المحرّك العامّ دون تغيير.

أمران يجعلان هذه المجموعة خاصّة:

* **تحويلات أفينية عبر الخطّافات، لا التحميل الزائد.** يبقى المحرّك المشترك ضربيًا صرفًا. يُحقَن التحويل
  الأفيني عبر الخطّافين القابلين للقياس `scaledBy` (البناء، خلف `of`) و`readBaseValue` (القراءة، خلف
  `into`)، فتعمل `25 of celsius` و`t into fahrenheit` عبر الأفعال العادية — دون تحميل زائد خاصّ بالمجموعة
  لـ `of`/`into` (الذي كان فِعل عامّ مستورد صراحةً سيحجبه).
* **بلا بادئات.** تقدّم مجموعة درجة الحرارة عمدًا **بلا** بواني بادئات (فـ `milli.celsius` غير مُنمذج). لا
  وجود لـ `KTemperatureUnitExtensions.kt`.

## الوحدات

| الوحدة | قيمة التعداد | الرمز | الرمز البرمجي | من/إلى كلفن |
|---|---|---|---:|---|
| كلفن | `KTemperatureUnit.KELVIN` | `K` | `kelvin` | هويّة |
| درجة سلسيوس | `KTemperatureUnit.CELSIUS` | `°C` | `celsius` | `K = °C + 273.15` |
| درجة فهرنهايت | `KTemperatureUnit.FAHRENHEIT` | `°F` | `fahrenheit` | `K = (°F − 32)·5/9 + 273.15` |
| درجة رانكن | `KTemperatureUnit.RANKINE` | `°R` | `rankine` | `K = °R·5/9` |

كل `Token` هو `KTemperatureUnitInstance` قيمته 1 يُستخدم مع `of` (البناء) و`into` (القراءة).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

val t = 25 of celsius
t.value             // 298.15 (مُطبَّع إلى كلفن المطلق)
t into fahrenheit   // 77.0
t into kelvin       // 298.15

(0 of celsius) into kelvin       // 273.15
(100 of celsius) into fahrenheit // 212.0
(32 of fahrenheit) into celsius  // 0.0
(-40 of celsius) into fahrenheit // -40.0 (نقطة تقاطع C/F)
```

## المعاملات

درجة الحرارة المطلقة **نقطة** أفينية لا متّجه. لذا فحسابها غير متماثل عمدًا — وهو السلوك الصحيح فيزيائيًا
(راجع أيضًا [فرق درجة الحرارة](temperature-difference.md)):

* `درجة مطلقة − درجة مطلقة` ← **`KTemperatureDifferenceUnitInstance`** (فترة الكلفن بينهما، مثلًا
  `30 °C − 10 °C = 20 ΔK`، **لا** `20 °C`).
* `درجة مطلقة ± فرق` ← درجة حرارة مطلقة ثانيةً.
* `درجة مطلقة + درجة مطلقة` ← **خطأ تصريف** (جمع درجتَي حرارة مطلقتين بلا معنى فيزيائي).
* `درجة مطلقة * رقم` / `درجة مطلقة / رقم` ← **خطأ تصريف**: تحجيم نقطة أفينية برقم مجرّد بلا معنى (قيمتها
  بالكلفن تحمل إزاحة −273.15). حجّم [فرق درجة حرارة](temperature-difference.md) بدلًا من ذلك، فهو خطّي.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

// مطلقة − مطلقة = فرق درجة حرارة (بالكلفن)
val d = (30 of celsius) - (10 of celsius)          // KTemperatureDifferenceUnitInstance: 20 ΔK
d.value                                             // 20.0

// مطلقة ± فرق = درجة حرارة مطلقة
val a = (25 of celsius) + KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 303.15 K
val b = (25 of celsius) - KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 293.15 K

// (30 of celsius) + (10 of celsius)               // لا يُصرَّف

// المقارنات (بالكلفن المطلق)
(0 of celsius) == (273.15 of kelvin)      // true (درجة الحرارة المطلقة نفسها)
(100 of celsius) > (100 of fahrenheit)    // true
```

### المقارنات والمساواة

تقارن `==`، `!=`، `<`، `<=`، `>`، `>=` قيمة كلفن المطلق المُطبَّعة. و`equals` بحسب درجة الحرارة المطلقة،
بصرف النظر عن وحدة البناء، فـ `(0 of celsius) == (273.15 of kelvin)`.

## القوى بـ `pow`

ارفع قيمة لقوّة عددية صحيحة بالمعامِل `pow`. لمجموعة درجة الحرارة يُعيد `pow` وحدة `KMixedUnitInstance`
عامّة (لا نوع قوّة مُبعَّد لدرجة الحرارة)، يعمل خطّيًا على حدّ الكلفن المطلق:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.temperature.*

val squared = (2 of kelvin) pow 2   // KMixedUnitInstance: 4.0 K²
```

## الدمج مع وحدات أخرى

ضرب درجة حرارة أو قسمتها بمجموعة أخرى يُنتج `KMixedUnitInstance` عامًّا (لا تركيب قياسي لدرجة الحرارة)،
محسوبًا على قيمة الكلفن المطلق:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (2 of kelvin) / (1 of seconds)   // KMixedUnitInstance: 2.0 K·s⁻¹
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()               // "298.15 K" (تمثيل الوحدة الأساسية)
"${(25 of celsius) into fahrenheit} °F"  // "77.0 °F"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. درجة الحرارة أفينية، فلا صيغة حاصل
ضرب/أُسّ — فقط الوحدات المسمّاة وتحويل الإزاحة.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `K` | `kelvin` | درجة الحرارة المطلقة، الوحدة الأساسية (كلفن) |
| `°C` | `celsius` | درجة سلسيوس (`K = °C + 273.15`) |
| `°F` | `fahrenheit` | درجة فهرنهايت |
| `25 °C` | `25 of celsius` | بناء درجة حرارة مطلقة |
