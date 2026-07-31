# التيار الكهربائي

الحزمة: `org.pcsoft.framework.kunit.electric.ec`
الوحدة الأساسية: **أمبير** (`KElectricCurrentUnit.BASE == KElectricCurrentUnit.AMPERE`)

النوع: **وحدة أصلية**

تُنمذج مجموعة التيار الكهربائي تيّارًا كهربائيًا. وهي مجموعة **أصلية بسيطة أحادية البُعد** (بلا أنواع فرعية مخصّصة
للأُسّ، وبلا نتائج محكومة بالنوع عبر المجموعات): يغلّف `KElectricCurrentUnitInstance` حدّ
`KElectricCurrentUnit.AMPERE` واحدًا، مُطبَّعًا دائمًا إلى الأمبيرات.

إضافةً إلى الأمبير في SI، تقدّم المجموعة وحدتَي التيار الكلاسيكيتين في نظام CGS: **البيوت** (أب-أمبير) من النظام
الكهرومغناطيسي (`1 Bi = 10 A`) و **الستات-أمبير** من النظام الكهروستاتيكي (`1 statA ≈ 3.335 641 × 10⁻¹⁰ A`).

## الوحدات

| المجموعة       | الوحدة          | قيمة التعداد                       | الرمز        |        الرمز البرمجي | 1 وحدة بالأمبيرات |
|----------------|-----------------|------------------------------------|--------------|---------------------:|------------------:|
| SI             | أمبير           | `KElectricCurrentUnit.AMPERE`      | `A`          |            `amperes` |               1.0 |
| CGS            | بيوت / أب-أمبير | `KElectricCurrentUnit.BIOT`        | `Bi` (`abA`) | `biot` / `abamperes` |                10 |
| CGS            | ستات-أمبير      | `KElectricCurrentUnit.STATAMPERE`  | `statA`      |        `statamperes` |      3.335641e-10 |
| دارة مغناطيسية | أمبير-لفة       | `KElectricCurrentUnit.AMPERE_TURN` | `At`         |        `ampereTurns` |               1.0 |

كل `Token` هو `KElectricCurrentUnitInstance` قيمته 1 يُستخدم مع `of` (البناء) و`into` (القراءة).

### القوّة المحرّكة المغناطيسية (أمبير-لفة)

**القوّة المحرّكة المغناطيسية** `Θ = N · I` — دافع الدارة المغناطيسية — مطابقة بُعديًا للتيار الكهربائي، لأن عدد اللفات
`N` عدد صرف. لذا فهي لا تحصل على مجموعة خاصّة بها: تستخدم الأمبير-لفة (`At`) داخل هذه المجموعة. يوثّق الرمز المميّز أن
القيمة تصف دافع ملف كامل بدلًا من تيّار موصّل واحد. `1 At = 1 A`، وتعمل البادئات كالمعتاد (`kilo.ampereTurns`).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.*

// ملف بـ 500 لفة يحمل 0.4 A ينتج Θ = 200 At.
val theta = (500 * 0.4) of ampereTurns
theta into ampereTurns          // 200.0
theta == (200 of amperes)       // true (متطابقة بُعديًا)
```

تدفع القوّة المحرّكة المغناطيسية [الفيض المغناطيسي](magneticflux.md) عبر
[الممانعة المغناطيسية](reluctance.md): `Θ = Rm · Φ`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.current.*

val i = 2 of milli.amperes    // 0.002 A
i.value                       // 0.002 (مُطبَّع إلى الأمبيرات)
i into amperes                // 0.002 (يُقرأ ثانيةً بالأمبيرات)
(1 of biot) into amperes      // 10.0
```

## مثال واقعي

قانون أوم: مقاوم `R = 220 Ω` عبر `U = 5 V` يحمل تيّارًا `I = U / R`. مُعبَّرًا عنه عبر وحدة التيار:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.current.*

val voltage = 5.0    // V
val resistance = 220.0 // Ω
val current = (voltage / resistance) of amperes   // ≈ 0.0227 A
current into milli.amperes                         // ≈ 22.7 mA
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.current.*

// + / - : المجموعة نفسها، تحويل تلقائي بين الوحدات
val a = (1 of amperes) + (1 of biot)   // KElectricCurrentUnitInstance: 11.0 A
val b = (1 of biot) - (1 of amperes)   // KElectricCurrentUnitInstance: 9.0 A

// المقارنات
(1 of biot) == (10 of amperes)         // true (المقدار المُطبَّع نفسه)
(1 of biot) > (1 of amperes)           // true
```

### المقارنات والمساواة

تقارن `==`، `!=`، `<`، `<=`، `>`، `>=` القيمة المُطبَّعة (بالأمبيرات) لقيمتَي
`KElectricCurrentUnitInstance`. و`equals` بحسب المقدار المُطبَّع، فـ `(1 of biot) == (10 of amperes)`.

## القوى بـ `pow`

ارفع قيمة لقوّة عددية صحيحة بالمعامِل `pow` (لا يملك Kotlin معامِل `^` قابلًا للتحميل الزائد). لمجموعة التيار الكهربائي
يُعيد `pow` وحدة `KMixedUnitInstance` عامّة (لا نوع قوّة مُبعَّد للتيار):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.current.*

val squared = (2 of amperes) pow 2     // KMixedUnitInstance: 4.0 A²
```

## بادئات SI

يقبل التيار الكهربائي **أي** مقدار، فيمكن دمج كل باني بادئة SI (`quetta` … `quecto`) مع كل وحدة تيار عبر الوصول إلى
الخاصّية. الميلي أمبير هو `milli.amperes`، والكيلوأمبير هو `kilo.amperes`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.current.*

(1 of milli.amperes).value   // 0.001      (ميلي أمبير)
(1 of kilo.amperes).value    // 1000.0     (كيلوأمبير)

(2500 of amperes) into kilo.amperes  // 2.5
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.current.*

(1 of biot).toString()                       // "10.0 A" (تمثيل الوحدة الأساسية)
"${(0.002 of amperes) into milli.amperes} mA" // "2.0 mA"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر.

| الرياضيات | Kotlin             | المعنى                                           |
|-----------|--------------------|--------------------------------------------------|
| `A`       | `amperes`          | التيار الكهربائي، الوحدة الأساسية (أمبير)        |
| `mA`      | `milli.amperes`    | ميلي أمبير (بادئة مطبَّقة على الأمبير)             |
| `kA`      | `kilo.amperes`     | كيلوأمبير                                        |
| `Bi`      | `biot`             | بيوت / أب-أمبير (10 A)                           |
| `At`      | `ampereTurns`      | أمبير-لفة، القوّة المحرّكة المغناطيسية `Θ = N · I` |
| `kAt`     | `kilo.ampereTurns` | أمبير-لفة ببادئة (كيلو أمبير-لفة)                |
| `A²`      | `amperes pow 2`    | أمبير تربيعًا (وحدة مختلطة عامّة)                  |
