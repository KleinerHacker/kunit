# التخزين

الحزمة: `org.pcsoft.framework.kunit.storage`
الوحدة الأساسية: **بايت** (`KStorageUnit.BASE == KStorageUnit.BYTE`)

النوع: **وحدة أصلية**

تُنمذج مجموعة التخزين مقدار بيانات رقمية. وهي مجموعة **بسيطة أحادية البُعد** (بلا أنواع فرعية مخصّصة
للأُسّ كمجموعة المسافة، وبلا دعامة `Duration` كمجموعة الزمن): يغلّف `KStorageUnitInstance` حدّ
`KStorageUnit.BASE` (بايت) واحدًا، مُطبَّعًا دائمًا إلى البايتات.

أمران يجعلان هذه المجموعة خاصّة:

* **بلا بادئات متناقصة.** كسر من بت ليس مقدار بيانات ذا معنى، فبادئات SI المتناقصة (`deci`، `centi`،
  `milli`، … — معامل `< 1`) **غير** متاحة لـ `bytes`/`bits`. كتابة `milli.bytes` **خطأ تصريف** لا فشل
  وقت التشغيل: تُعلَّق خاصّيتا `bytes`/`bits` فقط على باني SI الزائد (`KAugmentingPrefixBuilder`) وباني
  الثنائي، لا على الباني المتناقص أبدًا.
* **بادئات ثنائية (IEC).** إضافةً إلى بواني SI العشرية (`kilo` = 1000) يوجد نظام بانٍ ثنائيّ ثانٍ
  (`kibi` = 1024، `mebi` = 1024²، …)، فيمكن لقيمة أن تميّز الخطوة العشرية 1000 عن الثنائية 1024.

## الوحدات

| الوحدة | قيمة التعداد | الرمز | الرمز البرمجي | 1 وحدة بالبايتات |
|---|---|---|---:|---:|
| بايت | `KStorageUnit.BYTE` | `B` | `bytes` | 1.0 |
| بت | `KStorageUnit.BIT` | `bit` | `bits` | 0.125 |

البايت الواحد ثمانية بتات. كل `Token` هو `KStorageUnitInstance` قيمته 1 يُستخدم مع `of` (البناء)
و`into` (القراءة).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

val size = 5 of bytes
size.value          // 5.0 (مُطبَّع إلى البايتات)
size into bits      // 40.0 (يُقرأ ثانيةً بالبتات)
(1 of bytes) into bits   // 8.0
(8 of bits) into bytes   // 1.0
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

// + / - : المجموعة نفسها، تحويل تلقائي بين البت والبايت
val a = (1 of bytes) + (8 of bits)   // KStorageUnitInstance: 2.0 B
val b = (4 of bytes) - (16 of bits)  // KStorageUnitInstance: 2.0 B

// المقارنات
(1 of bytes) == (8 of bits)          // true (المقدار المُطبَّع نفسه)
(2 of bytes) > (1 of bytes)          // true

// storage / time هو معدّل بيانات محكوم بالنوع (راجع صفحة معدّل البيانات)
val rate = (1000 of bytes) / (2 of seconds)  // KDataRateUnitInstance: 500 B/s
```

### المقارنات والمساواة

تقارن `==`، `!=`، `<`، `<=`، `>`، `>=` القيمة المُطبَّعة (بالبايتات) لقيمتَي `KStorageUnitInstance`.
و`equals` بحسب المقدار المُطبَّع، فـ `(1 of bytes) == (8 of bits)`.

## القوى بـ `pow`

ارفع قيمة لقوّة عددية صحيحة بالمعامِل `pow` (لا يملك Kotlin معامِل `^` قابلًا للتحميل الزائد). لمجموعة
التخزين يُعيد `pow` وحدة `KMixedUnitInstance` عامّة (لا نوع قوّة مُبعَّد للتخزين):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = (2 of bytes) pow 2     // KMixedUnitInstance: 4.0 B²
```

## بادئات SI العشرية

يمكن دمج أي وحدة تخزين مع بواني بادئات SI **الزائدة** (فوق الواحدية) (`deca`، `hecto`، `kilo`، `mega`،
`giga`، `tera`، `peta`، `exa`، `zetta`، `yotta`، `ronna`، `quetta`) عبر الوصول إلى الخاصّية. أمّا
البواني المتناقصة (`deci` وما دونها) فليس لها خاصّية `bytes`/`bits`، فـ `milli.bytes` لا يُصرَّف.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 of kilo.bytes         // KStorageUnitInstance (== 5000 B)
fiveKb.value                         // 5000.0

(3 of bytes) into kilo.bytes         // 0.003 (kB)

// 5 of milli.bytes                  // لا يُصرَّف: لا خاصّية `bytes` على الباني المتناقص
```

## بادئات ثنائية (IEC)

بواني البادئات الثنائية هي قوى 1024 وتتيح لقيمة أن تميّز 1000 (`kilo`) عن 1024 (`kibi`): `kibi`، `mebi`،
`gibi`، `tebi`، `pebi`، `exbi`، `zebi`، `yobi`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*

(1 of kilo.bytes).value   // 1000.0     (عشري)
(1 of kibi.bytes).value   // 1024.0     (ثنائي)
(1 of mega.bytes).value   // 1_000_000.0
(1 of mebi.bytes).value   // 1_048_576.0

val file = 4 of mebi.bytes
file into kibi.bytes      // 4096.0 (KiB)
```

| الباني الثنائي | الرمز | 1 وحدة (بايت) |
|---|---|---:|
| `kibi` | `Ki` | 1024 |
| `mebi` | `Mi` | 1024² |
| `gibi` | `Gi` | 1024³ |
| `tebi` | `Ti` | 1024⁴ |
| `pebi` | `Pi` | 1024⁵ |
| `exbi` | `Ei` | 1024⁶ |
| `zebi` | `Zi` | 1024⁷ |
| `yobi` | `Yi` | 1024⁸ |

## الدمج مع وحدات أخرى

قيمة تخزين مدموجة مع زمن تُشكّل معدّل بيانات (`byte·second⁻¹`)، ويمكن تفكيكها ثانيةً:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (1000 of bytes) / (1 of seconds)  // 1000 B/s (KDataRateUnitInstance محكوم بالنوع)
val amount = rate * (60 of seconds)          // 60000 B (KStorageUnitInstance محكوم بالنوع)
amount into kibi.bytes                        // ≈ 58.59 (KiB)
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

(1024 of bytes).toString()               // "1024.0 B" (تمثيل الوحدة الأساسية)
"${(5 of bits) into bits} bit"           // "5.0 bit"
"${(2048 of bytes) into kibi.bytes} KiB" // "2.0 KiB"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `B` | `bytes` | مقدار البيانات، الوحدة الأساسية (بايت) |
| `bit` | `bits` | بت (`1 B = 8 bit`) |
| `kB` | `kilo.bytes` | بايت ببادئة عشرية (1000 B) |
| `KiB` | `kibi.bytes` | بايت ببادئة ثنائية (1024 B) |
