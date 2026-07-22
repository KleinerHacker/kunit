# تقنية المعلومات — نظرة عامة

الحزم: `org.pcsoft.framework.kunit.storage`، `…datarate`

تتعامل تقنية المعلومات مع **كمّيات البيانات الرقمية** ومدى سرعة انتقالها. يُنمذج KUnit الكمّية المخزّنة
ككمّية أساسية **أصلية** (التخزين، بالبايت) والإنتاجية ككمّية **مركّبة** منها (معدّل البيانات = تخزين لكل
زمن)، فيتحوّل السؤال اليومي «كم يستغرق هذا التنزيل؟» إلى تعبير محكوم بالنوع.

## وحدات هذا الموضوع

| الوحدة | النوع | الوحدة الأساسية | الصفحة |
|---|---|---|---|
| التخزين | أصلية | بايت (`B`) | [التخزين](storage.md) |
| معدّل البيانات | مركّبة | بايت لكل ثانية (`B/s`) | [معدّل البيانات](datarate.md) |

تتشارك المجموعتان السياسة نفسها للبادئات: **لا بادئات متناقصة** (كسر البت بلا معنى)، وإلى جانب بادئات
SI العشرية (`kilo` = 1000) عائلة **ثنائية (IEC)** ثانية (`kibi` = 1024).

## كيف ترتبط الكمّيات

| التعبير | النتيجة | الصيغة |
|---|---|---|
| `storage / time` | معدّل البيانات | `r = الكمّية / t` |
| `data rate * time` | التخزين | `الكمّية = r · t` |
| `time * data rate` | التخزين | `الكمّية = r · t` (تبادلي) |
| `storage / data rate` | الزمن | `t = الكمّية / r` |

## مثال واقعي — زمن التنزيل

يُنزَّل ملف بحجم **500 MB** عبر وصلة سرعتها **10 MB/s**. الزمن هو `t = الكمّية / المعدّل`، وضرب المعدّل
في هذا الزمن يُعيد إنتاج الكمّية `الكمّية = r · t`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

val amount = 500 of mega.bytes
val rate   = 10 of mega.bytes / seconds        // KDataRateUnitInstance، 10 MB/s

val time = amount / rate                        // KTimeUnitInstance
time into seconds                               // 50.0 (s)

val transferred = rate * (50 of seconds)        // KStorageUnitInstance
transferred into mega.bytes                     // 500.0 (MB)
```

## مثال واقعي — عشري مقابل ثنائي

الكمّية العددية نفسها تُقرأ بشكل مختلف مقابل قالب عشري (`kB`) وثنائي (`KiB`) — 1000 مقابل 1024:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val size = 4096 of bytes
size into kilo.bytes    // 4.096 (kB، عشري 1000)
size into kibi.bytes    // 4.0   (KiB، ثنائي 1024)
```

## طباعة قيمة (`toString`)

تُخرج `toString()` القيمة بالوحدة **الأساسية** لمجموعتها (القيمة + الرمز)؛ ولأي وحدة أخرى، اقرأها بـ
`into` داخل قالب نصّي وأضِف الرمز بنفسك:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = (10 of bytes) / (1 of seconds)   // KDataRateUnitInstance
r.toString()                             // "10.0 B/s" (الوحدة الأساسية)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## الترميز

يعرض الجدول التالي العلاقات الأساسية لهذا المجال بالترميز الرياضي مقابل ترميز Kotlin في KUnit. تُكتب
الأُسّس بحروف Unicode المرتفعة (`⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `r = الكمّية / t` | `(500 of mega.bytes) / (50 of seconds)` | معدّل البيانات من الكمّية ÷ الزمن |
| `الكمّية = r · t` | `rate * (50 of seconds)` | الكمّية من المعدّل × الزمن |
| `t = الكمّية / r` | `amount / rate` | الزمن من الكمّية ÷ المعدّل |
| `1 kB = 1000 B` | `kilo.bytes` | بايت ببادئة عشرية |
| `1 KiB = 1024 B` | `kibi.bytes` | بايت ببادئة ثنائية |

## إلى أين بعد ذلك

* [التخزين](storage.md) — مجموعة البايت الأصلية، البادئات العشرية والثنائية.
* [معدّل البيانات](datarate.md) — تخزين لكل زمن، ومعاملات التخزين ↔ الزمن ↔ المعدّل.
