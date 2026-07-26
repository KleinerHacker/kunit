# كثافة التخزين

الحزمة: `org.pcsoft.framework.kunit.storagedensity`
الوحدة الأساسية: **بايت لكل متر مربع** (`KStorageDensityUnit.BASE == KStorageDensityUnit.BYTES_PER_SQUARE_METER`)

النوع: **وحدة مُركّبة**

كثافة التخزين وحدة **مُركّبة**: فهي ليست كمية "حقيقية" مفردة بل تركيب، `storage · distance⁻²` (`B/m²`).
لذلك يُغلّف `KStorageDensityUnitInstance` نسخة `KMixedUnitInstance` مكوّنة من حدّين بالضبط — حدّ
`KStorageUnit.BASE` (بايت) بالأس `+1` وحدّ `KDistanceUnit.BASE` (متر) بالأس `-2`. تُخزَّن القيمة دائمًا
مُطبَّعة إلى بايت لكل متر مربع، بغضّ النظر عن أيّ وحدة أو تركيبة تخزين/مساحة أُنشئت منها.

## بناء كثافة التخزين

تُبنى كثافة التخزين كـ **تعبير تخزين لكل مساحة**، مثل `100 of bytes / area`، `5 of mega.bytes / area`.
المساحة هي أيّ `KAreaUnitInstance` (مثل `(1 of meters) * (1 of meters)`)، لذا تتركّب بحرية جميع البادئات
العشرية/الثنائية ووحدات الطول. اقرأها مرة أخرى بأيّ قالب تخزين لكل مساحة (`d into (bits / area)`). لا توجد
عمدًا أيّ رموز مركّبة مكتوبة بالحروف.

الوحدة الأساسية: *بايت* لكل متر مربع، بما يتّسق مع مجموعة التخزين. "بت لكل متر مربع" هو `0.125 B/m²`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.storagedensity.*

val area = (1 of meters) * (1 of meters)  // 1 m²
val d = 100 of bytes / area
d.value               // 100.0 (مُطبَّعة إلى B/m²)
d into (bits / area)  // 800.0 (تُقرأ بـ bit/m²)
```

## مثال واقعي: الكثافة السطحية لرقاقة SSD

تخزّن رقاقة فلاش **256 GB** على مساحة **100 mm²**. كثافتها التخزينية السطحية هي كمية البيانات مقسومة على المساحة:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.storagedensity.*

val data = 256 of giga.bytes                       // 256 GB
val side = 10 of milli.meters                      // رقاقة 10 mm × 10 mm = 100 mm²
val area = side * side
val density = data / area                          // KStorageDensityUnitInstance
density.value                                       // 2.56e15 (B/m²)
density into (giga.bytes / (side * side))           // 256.0 (GB لكل 100 mm²)
```

## الحساب بالوحدات الأساسية (التخزين والمساحة)

كثافة التخزين *هي* كمية تخزين مقسومة على مساحة. تنقّل بين الكميات الثلاث — التخزين والمساحة وكثافة
التخزين — باستخدام `*` و`/` العاديين؛ كل نتيجة **قوية النوع**.

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `storage / area` | `KStorageDensityUnitInstance` | الكثافة = الكمية / المساحة |
| `storage density * area` | `KStorageUnitInstance` | الكمية = الكثافة × المساحة |
| `area * storage density` | `KStorageUnitInstance` | الكمية (تبديلي) |
| `storage / storage density` | `KAreaUnitInstance` | المساحة = الكمية / الكثافة |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.storagedensity.*

val area = (1 of meters) * (1 of meters)   // 1 m²

// --- الوحدات الأساسية -> كثافة التخزين --------------------------------------
val d = (100 of bytes) / area   // KStorageDensityUnitInstance (لا حاجة إلى .toStorageDensity()!)
d.value               // 100.0 (B/m²)

// --- كثافة التخزين -> التخزين (اضرب في مساحة) -------------------
val amount = d * area           // KStorageUnitInstance
amount into bytes     // 100.0
area * d              // النتيجة نفسها (تبديلي)

// --- كثافة التخزين -> المساحة (اقسم كمية تخزين عليها) ------------------
val a = (600 of bytes) / d      // KAreaUnitInstance (6 m²)
```

!!! warning "الشكل *النقي* تخزين / مساحة فقط هو كثافة تخزين"
    يتطلّب `KMixedUnitInstance.toStorageDensity()` حدّ تخزين واحدًا بالضبط بالأس `+1` وحدّ مسافة واحدًا بالأس
    `-2`. الأشكال `B²·m⁻²` أو `B·m⁻¹` أو `B·m²` ليست كثافة تخزين — يرمي التحويل `IllegalStateException`.
    وبالمثل، `storage + storage density` (أبعاد مختلفة) هو خطأ ترجمة.

## المعامِلات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.storagedensity.*

val area = (1 of meters) * (1 of meters)

// + / - : المجموعة نفسها، تحويل تلقائي بين الكثافات المبنية على البايت والبت
val a = (1 of bytes / area) + (8 of bits / area)   // KStorageDensityUnitInstance, 2 B/m²
val b = (2 of bytes / area) - (8 of bits / area)   // 1 B/m²

// المقارنات (حسب قيمة B/m² المُطبَّعة)
(1 of bytes / area) > (4 of bits / area)           // true
(1 of bytes / area) == (8 of bits / area)          // true

// العملية * / / بين كثافتَي تخزين تخرج إلى KMixedUnitInstance (لم تعد كثافة نقية)
val squared = (10 of bytes / area) * (2 of bytes / area) // KMixedUnitInstance, [B^2, m^-4]
```

## البادئات العشرية والثنائية (IEC)

تحاكي مجموعة كثافة التخزين سياسة بادئات مجموعة [التخزين](storage.md) (بسطها كمية تخزين): يستخدم البسط
باني البادئات **المُكبِّرة** العشرية (`kilo`، `mega`، …) أو البواني **الثنائية** (`kibi`، `mebi`، …)؛
ويستخدم المقام (المساحة) أيّ وحدة طول وبادئة.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.storagedensity.*

val mm2 = (1 of milli.meters) * (1 of milli.meters)  // 1 mm²
val d = 1 of kilo.bytes / mm2                         // 1 kB/mm²
d into (kilo.bytes / mm2)  // 1.0
```

## التنسيق عبر toString

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into` أو `format`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.storagedensity.*

val area = (1 of meters) * (1 of meters)
((1000 of bytes) / area).toString()  // "1000.0 B/m²" (الوحدة الأساسية)
((1000 of bytes) / area) format (kilo.bytes.toUnit() / area.toUnit()) // "1.0 kB/m^2"
```

## الترميز

يوضّح الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تستخدم الأسس أحرفًا مرتفعة يونيكود (`²`، `³`، `⁻¹`)، ويدلّ `·` على الضرب و`/` على الكسر. وحين يمكن كتابة كمية ككسر وكحاصل ضرب بأُسٍّ سالب معًا، تُدرَج صيغتا Kotlin المتكافئتان.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `B/m²` | `bytes / area` | كثافة التخزين، الوحدة الأساسية (بايت لكل متر مربع) — صيغة الكسر |
| `B·m⁻²` | `bytes * (meters pow -2)` | الكثافة نفسها كحاصل ضرب بأُسٍّ سالب |
| `bit/m²` | `bits / area` | بت لكل متر مربع |
| `kB/mm²` | `kilo.bytes / mm2` | كيلوبايت لكل ملّيمتر مربع |
| `256 GB / 100 mm²` | `(256 of giga.bytes) / (side * side)` | يُبنى من تخزين ÷ مساحة |
