# الكتلة

الحزمة: `org.pcsoft.framework.kunit.mass`
الوحدة الأساسية: **غرام** (`KMassUnit.BASE == KMassUnit.GRAM`)

النوع: **وحدة أصلية**

تُنمذج مجموعة الكتلة مقدارًا من الكتلة. وهي مجموعة **بسيطة أحادية البُعد** (بلا أنواع فرعية مخصّصة للأُسّ
كمجموعة المسافة، وبلا دعامة `Duration` كمجموعة الزمن): يغلّف `KMassUnitInstance` حدّ `KMassUnit.GRAM`
واحدًا، مُطبَّعًا دائمًا إلى الغرامات.

الوحدة الأساسية هي **الغرام** عمدًا، لا الكيلوغرام. الكيلوغرام ليس وحدة مخصّصة إطلاقًا — إنّه ببساطة
`kilo.grams`، أي بادئة SI `kilo` مطبَّقة على الغرام. ويُوصَل إلى كل مقدار عشري للغرام (ميلي غرام،
كيلوغرام، …) بالطريقة العامّة نفسها عبر بادئات SI.

## الوحدات

| المجموعة | الوحدة | قيمة التعداد | الرمز | الرمز البرمجي | 1 وحدة بالغرامات |
|---|---|---|---|---:|---:|
| مترية | غرام | `KMassUnit.GRAM` | `g` | `grams` | 1.0 |
| مترية | طنّ (متري) | `KMassUnit.TONNE` | `t` | `tonnes` | 1 000 000 |
| مترية | قيراط (متري) | `KMassUnit.CARAT` | `ct` | `carats` | 0.2 |
| أفوردوبوا | غرين | `KMassUnit.GRAIN` | `gr` | `grains` | 0.06479891 |
| أفوردوبوا | درام | `KMassUnit.DRAM` | `dr` | `drams` | 1.7718451953125 |
| أفوردوبوا | أونصة | `KMassUnit.OUNCE` | `oz` | `ounces` | 28.349523125 |
| أفوردوبوا | رطل | `KMassUnit.POUND` | `lb` | `pounds` | 453.59237 |
| أفوردوبوا | ستون | `KMassUnit.STONE` | `st` | `stones` | 6350.29318 |
| أفوردوبوا | هندرد-ويت أمريكي (قصير) | `KMassUnit.HUNDREDWEIGHT_US` | `cwt(US)` | `hundredweightsUS` | 45 359.237 |
| أفوردوبوا | هندرد-ويت بريطاني (طويل) | `KMassUnit.HUNDREDWEIGHT_UK` | `cwt(UK)` | `hundredweightsUK` | 50 802.34544 |
| أفوردوبوا | طنّ قصير (أمريكي) | `KMassUnit.SHORT_TON` | `ton(US)` | `shortTons` | 907 184.74 |
| أفوردوبوا | طنّ طويل (بريطاني) | `KMassUnit.LONG_TON` | `ton(UK)` | `longTons` | 1 016 046.9088 |
| أفوردوبوا | سلَغ | `KMassUnit.SLUG` | `slug` | `slugs` | 14 593.90294 |
| تروي | بنيويت | `KMassUnit.PENNYWEIGHT` | `dwt` | `pennyweights` | 1.55517384 |
| تروي | أونصة تروي | `KMassUnit.TROY_OUNCE` | `oz t` | `troyOunces` | 31.1034768 |
| تروي | رطل تروي | `KMassUnit.TROY_POUND` | `lb t` | `troyPounds` | 373.2417216 |
| تاريخية | رطل ألماني | `KMassUnit.GERMAN_POUND` | `Pfd` | `germanPounds` | 500 |
| تاريخية | تسنتنر | `KMassUnit.ZENTNER` | `Ztr` | `zentners` | 50 000 |
| تاريخية | لوت | `KMassUnit.LOT` | `Lot` | `lots` | 16.6666667 |
| إقليمية | جين / كاتي | `KMassUnit.JIN` | `斤` | `jin` | 500 |
| إقليمية | ليانغ / تايل | `KMassUnit.LIANG` | `两` | `liang` | 50 |
| إقليمية | مومّي | `KMassUnit.MOMME` | `匁` | `momme` | 3.75 |
| إقليمية | كان / كانمي | `KMassUnit.KAN` | `貫` | `kan` | 3750 |
| علمية | دالتون (u) | `KMassUnit.DALTON` | `Da` | `daltons` | 1.6605390666e-24 |

كل `Token` هو `KMassUnitInstance` قيمته 1 يُستخدم مع `of` (البناء) و`into` (القراءة).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

val m = 2 of kilo.grams      // 2000 g (الكيلوغرام هو `kilo.grams`)
m.value                      // 2000.0 (مُطبَّع إلى الغرامات)
m into pounds                // ≈ 4.409 (يُقرأ ثانيةً بالأرطال)
(1 of pounds) into grams     // 453.59237
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

// + / - : المجموعة نفسها، تحويل تلقائي بين الوحدات
val a = (1 of kilo.grams) + (500 of grams)   // KMassUnitInstance: 1500.0 g
val b = (1 of kilo.grams) - (500 of grams)   // KMassUnitInstance: 500.0 g

// المقارنات
(1 of kilo.grams) == (1000 of grams)         // true (المقدار المُطبَّع نفسه)
(1 of kilo.grams) > (500 of grams)           // true
```

### المقارنات والمساواة

تقارن `==`، `!=`، `<`، `<=`، `>`، `>=` القيمة المُطبَّعة (بالغرامات) لقيمتَي `KMassUnitInstance`.
و`equals` بحسب المقدار المُطبَّع، فـ `(1 of kilo.grams) == (1000 of grams)`.

## القوى بـ `pow`

ارفع قيمة لقوّة عددية صحيحة بالمعامِل `pow` (لا يملك Kotlin معامِل `^` قابلًا للتحميل الزائد). لمجموعة
الكتلة يُعيد `pow` وحدة `KMixedUnitInstance` عامّة (لا نوع قوّة مُبعَّد للكتلة):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mass.*

val squared = (2 of grams) pow 2     // KMixedUnitInstance: 4.0 g²
```

## بادئات SI

تقبل الكتلة **أي** مقدار، فيمكن دمج كل باني بادئة SI (`quetta` … `quecto`) مع كل وحدة كتلة عبر الوصول إلى
الخاصّية. الكيلوغرام هو بالضبط `kilo.grams`؛ والميلي غرام هو `milli.grams`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).value    // 1000.0     (كيلوغرام)
(1 of milli.grams).value   // 0.001      (ميلي غرام)

(2500 of grams) into kilo.grams  // 2.5
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).toString()             // "1000.0 g" (تمثيل الوحدة الأساسية)
"${(2000 of grams) into kilo.grams} kg"  // "2.0 kg"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `g` | `grams` | الكتلة، الوحدة الأساسية (غرام) |
| `kg` | `kilo.grams` | كيلوغرام (بادئة مطبَّقة على الغرام) |
| `mg` | `milli.grams` | ميلي غرام |
| `g²` | `grams pow 2` | غرام تربيعًا (وحدة مختلطة عامّة) |
