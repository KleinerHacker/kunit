# المسافة

الحزمة: `org.pcsoft.framework.kunit.distance`
الوحدة الأساسية: **متر** (`KDistanceUnit.BASE == KDistanceUnit.METER`)

النوع: **وحدة أصلية**

تُنمذج مجموعة المسافة الأُسّس كأنواع خاصّة بها آمنة عند التصريف تحت غلاف أساس مفتوح
`KDistanceUnitInstance` (حدّ واحد من `KDistanceUnit.BASE` عند **أي** أُسّ):

* **`KLengthUnitInstance`** — أُسّ 1 (طول)
* **`KAreaUnitInstance`** — أُسّ 2 (مساحة)
* **`KVolumeUnitInstance`** — أُسّ 3 (حجم)

تُخزَّن القيمة دائمًا مُطبَّعة إلى الأمتار (أو الأمتار المربّعة/المكعّبة). ولأنّ الطول والمساحة والحجم
أنواع متمايزة، فإنّ خلطها في `+`/`-`/المقارنة **خطأ تصريف** (لا وجود لمثل هذا المعامِل)، بينما يبقى
`*`/`/` ضمن العائلة حيث أمكن (`length * length = area`، `area / length = length`) ويرجع إلى
`KDistanceUnitInstance`/`KMixedUnitInstance` للأُسّس خارج `{1,2,3}` (أو نتيجة أُسّ-0 عديمة البُعد).

ابنِ كل قيمة بـ `number of <token>` واقرأها بـ `value into <token>`.

## أُسّ 1 — الطول

| الوحدة | قيمة التعداد | الرمز | الرمز البرمجي | 1 وحدة بالأمتار |
|---|---|---|---:|---:|
| متر | `KDistanceUnit.METER` | `m` | `meters` | 1.0 |
| ميل | `KDistanceUnit.MILE` | `mi` | `miles` | 1609.344 |
| ميل بحري | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `nauticalMiles` | 1852.0 |
| ياردة | `KDistanceUnit.YARD` | `yd` | `yards` | 0.9144 |
| قدم | `KDistanceUnit.FOOT` | `ft` | `feet` | 0.3048 |
| إنش | `KDistanceUnit.INCH` | `in` | `inches` | 0.0254 |
| قامة | `KDistanceUnit.FATHOM` | `ftm` | `fathoms` | 1.8288 |
| سلسلة | `KDistanceUnit.CHAIN` | `ch` | `chains` | 20.1168 |
| فرلنغ | `KDistanceUnit.FURLONG` | `fur` | `furlongs` | 201.168 |
| وحدة فلكية | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `astronomicalUnits` | 1.495978707e11 |
| فرسخ فلكي | `KDistanceUnit.PARSEC` | `pc` | `parsecs` | 3.0856775814913673e16 |
| ذراع | `KDistanceUnit.CUBIT` | `cubit` | `cubits` | 0.4572 |
| قدم رومانية (pes) | `KDistanceUnit.ROMAN_FOOT` | `pes` | `romanFeet` | 0.2957 |
| خطوة رومانية (passus) | `KDistanceUnit.ROMAN_PACE` | `passus` | `romanPaces` | 1.4787 |
| ستاديوم | `KDistanceUnit.STADIUM` | `stadium` | `stadia` | 185.0 |
| ميل روماني (mille passus) | `KDistanceUnit.ROMAN_MILE` | `mp` | `romanMiles` | 1481.5 |
| قصبة (perch) | `KDistanceUnit.ROD` | `rod` | `rods` | 5.0292 |
| فرسخ | `KDistanceUnit.LEAGUE` | `lea` | `leagues` | 4828.032 |
| طول حبل | `KDistanceUnit.CABLE_LENGTH` | `cable` | `cableLengths` | 185.2 |
| فرست | `KDistanceUnit.VERST` | `verst` | `versts` | 1066.8 |
| ميل بروسي | `KDistanceUnit.PRUSSIAN_MILE` | `prussian mi` | `prussianMiles` | 7532.5 |

### مسافات انتقال الضوء (مجموعة `light` بلا بادئات)

تُجمَّع مسافات انتقال الضوء خلف الباني `light` الخالي من البادئات وتُقرأ كأنّها نثر، مثلًا
`5 of light.seconds`، `3 of light.years`. وهي عمدًا **لا** تقبل بادئات SI (فـ `kilo.lightYears` بلا
معنى فيزيائي).

| الوحدة | قيمة التعداد | الرمز | الرمز البرمجي | 1 وحدة بالأمتار |
|---|---|---|---:|---:|
| ثانية ضوئية | `KDistanceUnit.LIGHT_SECOND` | `ls` | `light.seconds` | 299792458.0 |
| دقيقة ضوئية | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `light.minutes` | 1.798754748e10 |
| ساعة ضوئية | `KDistanceUnit.LIGHT_HOUR` | `lh` | `light.hours` | 1.0792528488e12 |
| يوم ضوئي | `KDistanceUnit.LIGHT_DAY` | `ld` | `light.days` | 2.59020683712e13 |
| أسبوع ضوئي | `KDistanceUnit.LIGHT_WEEK` | `lw` | `light.weeks` | 1.813144785984e14 |
| سنة ضوئية | `KDistanceUnit.LIGHT_YEAR` | `ly` | `light.years` | 9.4607304725808e15 |

كل `Token` هو `KLengthUnitInstance` قيمته 1 يُستخدم مع كلٍّ من `of` (البناء) و`into` (القراءة).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val d = 5 of miles
d.value               // 8046.72 (مُطبَّع إلى الأمتار)
d into miles          // 5.0 (يُقرأ ثانيةً بالأميال)
d into feet           // 26400.0
d into nauticalMiles  // ≈ 4.3452
```

### المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// + / - : المجموعة نفسها، تحويل تلقائي بين وحدات طول مختلفة
val a = (1 of miles) + (500 of meters)   // KLengthUnitInstance، مُطبَّع إلى الأمتار
val b = (2 of miles) - (800 of meters)

// المقارنات
(2 of miles) > (1 of miles)              // true
(1 of miles) == (1609.344 of meters)     // true (القيمة المُطبَّعة نفسها)
// (5 of hectares) > (5 of meters)       // لا يُصرَّف: المساحة والطول نوعان مختلفان

// * / / يبقيان ضمن عائلة الطول عندما يكون المعامِلان مُبعَّدين ستاتيكيًا
val area = (200 of meters) * (50 of meters)   // KAreaUnitInstance: value=10000.0 (m²)
val lengthAgain = area / (50 of meters)       // KLengthUnitInstance: value=200.0 (m)
val ratio = (10 of meters) / (2 of meters)    // KMixedUnitInstance (عديم البُعد)، value=5.0
```

### التحجيم برقم

يمكن تحجيم قيمة مسافة برقم `Number` مجرّد مع الحفاظ على نوعها (يبقى الطول طولًا، والمساحة مساحة). هذا
يجعل الحسابات على هيئة صيغ تُقرأ بطبيعية — مثلًا مساحة دائرة `A = π · r²` بالكامل عبر نظام الوحدات:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.distance.*

val r = 12 of centi.meters       // KLengthUnitInstance، 0.12 m
val area = Math.PI * (r * r)     // KAreaUnitInstance: π·r² ≈ 0.04524 m²

val tripled = (12 of meters) * 3 // KLengthUnitInstance، 36 m
val half = area / 2              // KAreaUnitInstance، نصف مساحة الدائرة
```

### المقارنات والمساواة

تقارن `==`، `!=`، `<`، `<=`، `>`، `>=` القيمة المُطبَّعة لقيمتين من **النوع نفسه** (البُعد نفسه). خلط
الأبعاد (مثلًا طول ومساحة) يرفضه المصرّف — لا وجود لمثل هذا المعامِل — اتساقًا مع قواعد `+`/`-`. وتُعيد
`equals` عبر الأبعاد `false` ببساطة.

## أُسّ 2 — المساحة

يمثّل `KAreaUnitInstance` مساحة، مثلًا نتيجة `length * length` أو رفع طول للقوّة الثانية بالمعامِل `pow`
(`(2 of meters) pow 2` == `(2 m)²` == 4 m²، `(2 of kilo.meters) pow 2` == 4 000 000 m²). لا توجد رموز
`squareXxx` — `pow` هي صيغة القوّة الوحيدة (راجع [القوى بـ `pow`](#pow)). تتوفّر الرموز الخاصّة
المسمّاة التالية:

| الوحدة الخاصّة | الرمز | الرمز البرمجي | 1 وحدة بـ m² |
|---|---:|---:|---:|
| آر | `a` | `ares` | 100.0 |
| هكتار | `ha` | `hectares` | 10 000.0 |
| فدّان (acre) | `ac` | `acres` | 4046.8564224 |
| رود | `ro` | `roods` | 1011.7141056 |
| قصبة مربّعة | `perch²` | `squarePerches` | 25.29285264 |
| مورغن (بروسي) | `Mg` | `morgens` | 2553.22 |
| يوخ (نمساوي) | `Joch` | `jochs` | 5754.642 |
| تاغفيرك (بافاري) | `Tw` | `tagwerks` | 3407.27 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val plot = 3 of hectares
plot.value        // 30000.0 (m²)
plot into ares    // 300.0
plot into acres   // ≈ 7.4132

val computed = (200 of meters) * (50 of meters)  // KAreaUnitInstance (10 000 m²)
computed into hectares                           // 1.0

plot + computed   // مسموح: كلاهما مساحة -> KAreaUnitInstance
// plot + (5 of meters)  // لا يُصرَّف: مساحة مقابل طول
```

## أُسّ 3 — الحجم

يمثّل `KVolumeUnitInstance` حجمًا، مثلًا `length * length * length` أو `area * length` أو طولًا مرفوعًا
للقوّة الثالثة (`(2 of meters) pow 3` == 8 m³). كما في المساحة، لا توجد رموز `cubicXxx` — استخدم `pow`
(راجع [القوى بـ `pow`](#pow)). تتوفّر الرموز الخاصّة المسمّاة التالية:

| الوحدة الخاصّة | الرمز | الرمز البرمجي | 1 وحدة بـ m³ |
|---|---:|---:|---:|
| لتر | `L` | `liters` | 0.001 |
| غالون أمريكي سائل | `gal (US)` | `usGallons` | 0.003785411784 |
| غالون إمبراطوري | `gal (UK)` | `imperialGallons` | 0.00454609 |
| أونصة سائلة أمريكية | `fl oz` | `usFluidOunces` | 2.95735295625e-5 |
| برميل نفط | `bbl` | `oilBarrels` | 0.158987294928 |
| بوشل إمبراطوري | `bu (UK)` | `imperialBushels` | 0.03636872 |
| هوغسهيد إمبراطوري | `hhd` | `hogsheads` | 0.32731785 |
| باينت إمبراطوري | `pt (UK)` | `imperialPints` | 0.00056826125 |
| كوارت إمبراطوري | `qt (UK)` | `imperialQuarts` | 0.0011365225 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val tank = 200 of liters
tank.value          // 0.2 (m³)
tank into usGallons // ≈ 52.834

val cube = (2 of meters) * (2 of meters) * (2 of meters)  // KVolumeUnitInstance (8 m³)
cube into liters                                          // 8000.0

tank + cube         // مسموح: كلاهما حجم -> KVolumeUnitInstance
```

## القوى بـ `pow`

ارفع قيمة لقوّة عددية صحيحة بالمعامِل `pow`. لا يملك Kotlin معامِل `^` قابلًا للتحميل الزائد (ولا `^=`)،
فـ `pow` هي صيغة القوّة الوحيدة على مستوى المجموعة — لا رموز `squareXxx`/`cubicXxx`.

يرفع `pow` القيمة **و**يضرب كل أُسّ في `n`، فـ `(2 of meters) pow 2` هو `(2 m)² = 4 m²` (تُرفع القيمة،
لا مجرّد الأُسّ). لمجموعة المسافة النتيجة مُبعَّدة: `pow 2` يعطي `KAreaUnitInstance`، و`pow 3` يعطي
`KVolumeUnitInstance`، والأُسّس الأخرى تعطي `KDistanceUnitInstance` العامّ.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

val area = (2 of meters) pow 2         // KAreaUnitInstance: 4.0 m²
val big = (2 of kilo.meters) pow 2     // KAreaUnitInstance: 4 000 000 m²  ((2000 m)²)
val volume = (2 of meters) pow 3       // KVolumeUnitInstance: 8.0 m³
val m4 = (2 of meters) pow 2 pow 2     // KDistanceUnitInstance: 16.0 m⁴  ((4 m²)²)
val inverse = (2 of meters) pow -1     // KDistanceUnitInstance: 0.5 m⁻¹
```

يرتبط `pow` **أضعف** من `* / + -`؛ ضَع أقواسًا في التعبيرات المختلطة (`(a * b) pow 2`). وهو متاح على كل
مجموعة وحدات — مثلًا `(2 of hours) pow 2` (وحدة `KMixedUnitInstance` عامّة، إذ لا نوع قوّة مُبعَّد للزمن).

## بادئات SI

يمكن دمج أي وحدة طول مع أيٍّ من بواني بادئات SI الـ 24 (`kilo`، `milli`، …؛ الحزمة الجذر) عبر الوصول
إلى الخاصّية، مُنتِجًا قالبًا قيمته 1 لـ `of`/`into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.distance.*

// البناء: "5 of kilo.meters" -> KLengthUnitInstance (== 5000 m)
val fiveKm = 5 of kilo.meters
fiveKm.value // 5000.0

// قراءة قيمة بوحدة ذات بادئة
val d = 5 of miles
d into kilo.meters  // 8.04672 (km)

// تتركّب البادئات أيضًا مع رموز المساحة/الحجم المسمّاة
val tank = 200 of liters
tank into milli.liters  // 200000.0 (mL)
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

(5 of meters).toString()               // "5.0 m" (تمثيل الوحدة الأساسية)
"${(5 of miles) into miles} mi"        // "5.0 mi"
"${((200 of meters) * (50 of meters)) into hectares} ha" // "1.0 ha"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر
وكحاصل ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `m` | `meters` | الطول، الوحدة الأساسية (متر) |
| `km` | `kilo.meters` | طول ببادئة (كيلومتر) |
| `m²` | `meters pow 2` | مساحة (متر مربّع) |
| `m³` | `meters pow 3` | حجم (متر مكعّب) |
| `m⁻¹` | `meters pow -1` | مقلوب الطول |
| `2 m · 2 m` | `(2 of meters) * (2 of meters)` | مساحة مبنيّة من طول × طول |
| `π · A` | `Math.PI * area` | عدد × مساحة (تحجيم مقدار، يبقى مساحة) |
| `A / 2` | `area / 2` | مساحة مقسومة على رقم مجرّد (تبقى مساحة) |
