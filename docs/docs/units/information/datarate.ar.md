# معدّل البيانات

الحزمة: `org.pcsoft.framework.kunit.datarate`
الوحدة الأساسية: **بايت لكل ثانية** (`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

النوع: **وحدة مركّبة**

معدّل البيانات وحدة **مركّبة** (الثانية بعد [السرعة](../kinematics/speed.md)): فهو ليس كمّية «حقيقية»
واحدة بل تركيب، `storage · time⁻¹` (`B/s`). لذا يغلّف `KDataRateUnitInstance` نسخةَ `KMixedUnitInstance`
من حدّين تحديدًا — `KStorageUnit.BASE` (بايت) عند أُسّ `+1` و`KTimeUnit.BASE` (ثانية) عند أُسّ `-1`.
تُخزَّن القيمة دائمًا مُطبَّعة إلى البايت لكل ثانية، بصرف النظر عن الوحدة أو تركيبة التخزين/الزمن التي
بُنيت منها.

## بناء معدّل بيانات

يُبنى معدّل البيانات كتعبير **تخزين لكل زمن**، مثلًا `100 of bytes / seconds` أو
`5 of mega.bytes / seconds` أو `10 of kibi.bytes / seconds` — كلٌّ يعطي `KDataRateUnitInstance`. اقرأه
ثانيةً في أي قالب تخزين-لكل-زمن (`r into (bits / seconds)`). لا توجد عمدًا رموز مركّبة منطوقة مثل
`bytesPerSecond` (فهي بالضبط `bytes / seconds`).

الوحدة الأساسية: *بايت* لكل ثانية، اتساقًا مع مجموعة التخزين. أمّا بت/الثانية الأصلي في الشبكات (`bps`)
فهو `0.125 B/s`؛ و«ميجابت في الثانية» هو `1 of mega.bits / seconds`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = 100 of bytes / seconds
r.value                  // 100.0 (مُطبَّع إلى B/s)
r into (bits / seconds)  // 800.0 (يُقرأ ثانيةً بـ bit/s)
```

## الحساب بالوحدات الأساسية (التخزين والزمن)

معدّل البيانات *هو* مقدار تخزين مقسوم على زمن. تنقّل بين الكمّيات الثلاث — التخزين والزمن ومعدّل البيانات —
بـ `*` و`/` بسيطين؛ وكل نتيجة **محكومة بالنوع** بقوّة.

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | المعدّل = المقدار / المدّة |
| `data rate * time` | `KStorageUnitInstance` | المقدار = المعدّل × المدّة |
| `time * data rate` | `KStorageUnitInstance` | المقدار (تبادلي) |
| `storage / data rate` | `KTimeUnitInstance` | المدّة = المقدار / المعدّل |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- الوحدات الأساسية -> معدّل بيانات --------------------------------------
val r = (100 of bytes) / (10 of seconds)   // KDataRateUnitInstance (لا حاجة إلى .toDataRate()!)
r.value                  // 10.0 (B/s)
r into (bits / seconds)  // 80.0

// بسط ببادئة، بلا أقواس:
val download = 5 of mega.bytes / seconds   // KDataRateUnitInstance (5 MB/s)

// --- معدّل بيانات -> تخزين (اضرب في زمن) --------------------------
val amount = r * (60 of seconds)   // KStorageUnitInstance
amount into bytes     // 600.0
amount into bits      // 4800.0
(60 of seconds) * r   // النتيجة نفسها (تبادلي)

// --- معدّل بيانات -> زمن (اقسم مقدار تخزين عليه) ------------------
val time = (600 of bytes) / r      // KTimeUnitInstance
time into minutes     // 1.0
```

!!! warning "شكل تخزين / زمن *نقيّ* فقط هو معدّل بيانات"
    يتطلّب `KMixedUnitInstance.toDataRate()` حدّ تخزين واحدًا بالضبط عند أُسّ `+1` وحدّ زمن واحدًا عند أُسّ
    `-1`. أمّا شكل `B²` (تخزين مربّع) أو `B·s⁻²` أو `B·s` فليس معدّل بيانات — ويُطلق التحويل
    `IllegalStateException`. كذلك `storage + data rate` (بُعدان مختلفان) خطأ تصريف.

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// + / - : المجموعة نفسها، تحويل تلقائي بين المعدّلات القائمة على البايت والبت
val a = (1 of bytes / seconds) + (8 of bits / seconds)   // KDataRateUnitInstance، 2 B/s
val b = (2 of bytes / seconds) - (8 of bits / seconds)   // 1 B/s

// المقارنات (بحسب القيمة المُطبَّعة بـ B/s)
(1 of bytes / seconds) > (4 of bits / seconds)           // true
(1 of bytes / seconds) == (8 of bits / seconds)          // true

// * / / بين معدّلَي بيانات يهربان إلى KMixedUnitInstance (لم يعُد معدّلًا نقيًّا)
val squared = (10 of bytes / seconds) * (2 of bytes / seconds) // KMixedUnitInstance، [B^2, s^-2]
```

## بادئات SI والثنائية (IEC)

تعكس مجموعة معدّل البيانات سياسة بادئات مجموعة [التخزين](storage.md) (فبسطها مقدار تخزين): يستخدم البسط
بواني SI **الزائدة** (`kilo`، `mega`، …) أو البواني **الثنائية** (`kibi`، `mebi`، …)؛ أمّا البواني
المتناقصة فليس لها خاصّية `bytes`/`bits`، فـ `milli.bytes / seconds` لا يُصرَّف.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// عشري مقابل ثنائي: 1000 (kilo) != 1024 (kibi)
(1 of kilo.bytes / seconds).value // 1000.0
(1 of kibi.bytes / seconds).value // 1024.0

// قراءة قيمة ثانيةً في قالب تخزين-لكل-زمن
val r = 4096 of bytes / seconds
r into (kilo.bytes / seconds)  // 4.096 (kB/s)
r into (kibi.bytes / seconds)  // 4.0   (KiB/s)
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.storage.kibi
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

(10 of bytes / seconds).toString()  // "10.0 B/s" (الوحدة الأساسية)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `B/s` | `bytes / seconds` | معدّل البيانات، الوحدة الأساسية (بايت لكل ثانية) — صيغة الكسر |
| `B·s⁻¹` | `bytes * (seconds pow -1)` | المعدّل نفسه كحاصل ضرب بأُسّ سالب |
| `bit/s` | `bits / seconds` | بت لكل ثانية |
| `MB/s` | `mega.bytes / seconds` | ميجابايت لكل ثانية |
| `100 B / 10 s` | `(100 of bytes) / (10 of seconds)` | البناء من تخزين ÷ زمن |
