# السرعة

الحزمة: `org.pcsoft.framework.kunit.speed`
الوحدة الأساسية: **متر لكل ثانية** (`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

النوع: **وحدة مركّبة**

السرعة هي أوّل وحدة **مركّبة**: فخلافًا للطول أو الزمن ليست كمّية «حقيقية» واحدة بل تركيب،
`length · time⁻¹` (`m/s`). لذا يغلّف `KSpeedUnitInstance` نسخةَ `KMixedUnitInstance` من حدّين تحديدًا —
`KDistanceUnit.BASE` (متر) عند أُسّ `+1` و`KTimeUnit.BASE` (ثانية) عند أُسّ `-1`. تُخزَّن القيمة دائمًا
مُطبَّعة إلى المتر لكل ثانية، بصرف النظر عن الوحدة أو تركيبة الطول/الزمن التي بُنيت منها.

## بناء سرعة

تُبنى السرعة كتعبير **طول لكل زمن**، مثلًا `10 of kilo.meters / hours` أو
`100 of meters / (10 of seconds)` — كلاهما يعطي `KSpeedUnitInstance`. اقرأها في أي قالب طول-لكل-زمن
(`v into (kilo.meters / hours)`). لا توجد عمدًا رموز مركّبة منطوقة مثل `metersPerSecond` أو
`kilometersPerHour` (فهي بالضبط `meters / seconds` / `kilo.meters / hours`).

تبقى فقط السرعات ذات الاسم المفرد الاصطلاحي الحقيقي كرموز قيمتها 1 (تُستخدم مع `of`/`into`):

| السرعة | الرمز | الرمز البرمجي | 1 وحدة بـ m/s |
|---|---|---:|---:|
| عقدة | `kn` | `knots` | 0.514444 (1852/3600) |
| ماخ (ISA عند سطح البحر) | `Ma` | `mach` | 340.29 |
| سرعة الضوء | `c` | `speedOfLight` | 299792458.0 |

> **ماخ** هو سرعة الصوت في الغلاف الجوّي القياسي الدولي عند سطح البحر (15 °C). إنّه نقطة مرجعية ملائمة، لا
> ثابت فيزيائي — فسرعة الصوت الحقيقية تتغيّر مع الحرارة والارتفاع.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.miles
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = 50 of kilo.meters / hours
v.value                        // 13.888...(مُطبَّع إلى m/s)
v into (kilo.meters / hours)   // 50.0 (يُقرأ ثانيةً بـ km/h)
v into (miles / hours)         // ≈ 31.07
v into knots                   // ≈ 26.998
v into mach                    // ≈ 0.0408 (كسر من سرعة الصوت)
```

## الحساب بالوحدات الأساسية (الطول والزمن)

هذا هو مغزى الوحدة المركّبة بأكمله. فالسرعة *هي* طول مقسوم على زمن. يتيح KUnit التنقّل بين الكمّيات الثلاث
— الطول والزمن والسرعة — بـ `*` و`/` بسيطين، وكل نتيجة **محكومة بالنوع** بقوّة. لن تحتاج أبدًا إلى بناء
`KMixedUnitInstance` خام أو فكّه بنفسك.

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | السرعة = المسافة / المدّة |
| `speed * time` | `KLengthUnitInstance` | المسافة = السرعة × المدّة |
| `time * speed` | `KLengthUnitInstance` | المسافة (تبادلي) |
| `length / speed` | `KTimeUnitInstance` | المدّة = المسافة / السرعة |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- الوحدات الأساسية -> سرعة ------------------------------------------
val v = (100 of meters) / (10 of seconds)  // KSpeedUnitInstance (لا حاجة إلى .toSpeed()!)
v.value                    // 10.0 (m/s)
v into (kilo.meters / hours) // 36.0
v into (miles / hours)     // ≈ 22.37
v into knots               // ≈ 19.44

// أضِف بادئة للطول لمعدّل مضغوط (بلا أقواس، `of` يرتبط أضعف من `/`):
val fast = 10 of kilo.meters / hours   // KSpeedUnitInstance

// --- سرعة -> طول (اضرب في زمن) -------------------------------
val distance = v * (60 of seconds)     // KLengthUnitInstance
distance into meters       // 600.0
distance into feet         // ≈ 1968.5
(60 of seconds) * v        // النتيجة نفسها (تبادلي)

// --- سرعة -> زمن (اقسم طولًا عليها) ------------------------------
val time = (600 of meters) / v         // KTimeUnitInstance
time into minutes          // 1.0
```

!!! warning "طول *نقيّ* فقط يُقسَم إلى سرعة"
    يتطلّب `length / time` و`length / speed` أن يكون للطول أُسّ 1. أمّا **المساحة** (`m²`) أو **الحجم**
    (`m³`) فليست طولًا، فـ `area / time` سيكون `m²/s` لا سرعة — ويُطلق المعامِل `IllegalStateException`
    بدلًا من إعادة قيمة خاطئة بصمت. ولبناء مثل هذا الوسيط عمدًا، أنزِل معامِلًا إلى المستوى المختلط بـ
    `toUnit()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val area = (2 of meters) * (2 of meters)         // KAreaUnitInstance
val areaPerTime = area.toUnit() / (2 of seconds).toUnit() // KMixedUnitInstance، [METER^2, SECOND^-1]
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

// + / - : المجموعة نفسها، تحويل تلقائي بين تعابير سرعة مختلفة
val a = (36 of kilo.meters / hours) + (10 of meters / seconds)  // KSpeedUnitInstance، 20 m/s
val b = (20 of meters / seconds) - (36 of kilo.meters / hours)  // 10 m/s

// المقارنات (بحسب القيمة المُطبَّعة بـ m/s)
(50 of kilo.meters / hours) > (10 of meters / seconds)   // true
(36 of kilo.meters / hours) == (10 of meters / seconds)  // true

// * / / بين سرعتين يهربان إلى KMixedUnitInstance (لم تعُد سرعة نقيّة)
val squared = (10 of meters / seconds) * (2 of meters / seconds) // KMixedUnitInstance، [m^2, s^-2]
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

(10 of meters / seconds).toString()   // "10.0 m/s" (الوحدة الأساسية)
"${(10 of meters / seconds) into (kilo.meters / hours)} km/h" // "36.0 km/h"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `m/s` | `meters / seconds` | السرعة، الوحدة الأساسية (متر لكل ثانية) — صيغة الكسر |
| `m·s⁻¹` | `meters * (seconds pow -1)` | السرعة نفسها كحاصل ضرب بأُسّ سالب |
| `km/h` | `kilo.meters / hours` | كيلومتر لكل ساعة |
| `mi/h` | `miles / hours` | ميل لكل ساعة |
| `100 m / 10 s` | `(100 of meters) / (10 of seconds)` | البناء من طول ÷ زمن |
