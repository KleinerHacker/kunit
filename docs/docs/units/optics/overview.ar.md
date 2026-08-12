# البصريات — نظرة عامة

الحزم: `org.pcsoft.framework.kunit.optic.luminousintensity`، `…luminousflux`، `…illuminance`،
`…luminance`، `…luminousenergy`، `…luminousexposure`، `…efficacy`، `…radiantintensity`، `…radiance`،
و `org.pcsoft.framework.kunit.common.reciprocallength`

البصريات هي وصف لـ **الضوء** — مقدار الضوء الذي يصدره مصدر ما، ومقدار ما يصل منه إلى سطح، ومدى كفاءة
تحويل الطاقة الكهربائية إليه. يقوم هذا المجال على **الشمعة (candela)**، وهي الوحدة الأساسية السابعة
والأخيرة في النظام الدولي للوحدات، وهي الوحدة الأساسية الوحيدة التي تُعرَّف بدلالة الإدراك البشري: فهي
تُرجِّح الطاقة الإشعاعية وفق حساسية العين.

لهذا السبب يحتوي هذا المجال على عائلتين متوازيتين. الكميات **الضوئية (photometric)** (الشمعة، اللومن،
اللوكس، النِّت) تصف الضوء *كما تراه العين*؛ بينما الكميات **الإشعاعية (radiometric)** (واط لكل ستراديان،
واط لكل ستراديان متر مربع) تصف الإشعاع نفسه *كما يقيسه الكاشف*، دون ترجيح العين. الجسر بينهما هو
[الفعالية الضوئية](luminous-efficacy.ar.md)، والتي يبلغ حدها الأقصى 683 lm/W.

## الوحدات في هذا الموضوع

| الوحدة               | النوع        | الوحدة الأساسية                              | الصفحة                                     |
|--------------------|-------------|----------------------------------------|--------------------------------------------|
| شدة الإضاءة | أصلية      | شمعة (`cd`)                         | [شدة الإضاءة](luminous-intensity.ar.md) |
| التدفق الضوئي      | مُركَّبة | لومن (`lm`)                           | [التدفق الضوئي](luminous-flux.ar.md)        |
| الاستضاءة        | مُركَّبة | لوكس (`lx`)                             | [الاستضاءة](illuminance.ar.md)            |
| السطوع (اللمعان)          | مُركَّبة | شمعة لكل متر مربع (`cd/m²`)     | [السطوع](luminance.ar.md)                |
| الطاقة الضوئية    | مُركَّبة | لومن ثانية (`lm·s`)                  | [الطاقة الضوئية](luminous-energy.ar.md)    |
| التعرض الضوئي  | مُركَّبة | لوكس ثانية (`lx·s`)                    | [التعرض الضوئي](luminous-exposure.ar.md) |
| الفعالية الضوئية  | مُركَّبة | لومن لكل واط (`lm/W`)                | [الفعالية الضوئية](luminous-efficacy.ar.md) |
| الشدة الإشعاعية  | مُركَّبة | واط لكل ستراديان (`W/sr`)            | [الشدة الإشعاعية](radiant-intensity.ar.md) |
| الإشعاع اللمعاني (radiance)           | مُركَّبة | واط لكل ستراديان متر مربع (`W/(sr·m²)`)    | [الإشعاع اللمعاني](radiance.ar.md)                  |
| القوة الانكسارية   | مُركَّبة | ديوبتر (`dpt` = `m⁻¹`)                | [الديوبتر](dioptre.ar.md)                    |

الزاوية المجسمة التي تربط كميات الشدة بكميات التدفق **لا** تنتمي إلى هذا المجال — فهي تنتمي إلى موضوع
[الميكانيكا](../mechanics/solid-angle.md) ويُعاد استخدامها هنا كما هي.

## كيف ترتبط هذه الكميات ببعضها

كل علاقة أدناه تُعيد الكمية **المصنَّفة** الصحيحة؛ لن تحتاج أبدًا إلى تجميع وحدة مختلطة خامة يدويًا:

| التعبير                     | النتيجة             | الصيغة        |
|--------------------------------|--------------------|----------------|
| `luminousIntensity * solidAngle` | التدفق الضوئي    | `Φ = I · Ω`    |
| `luminousFlux / area`          | الاستضاءة        | `E = Φ / A`    |
| `luminousIntensity / area`     | السطوع          | `L = I / A`    |
| `illuminance / solidAngle`     | السطوع          | `L = E / Ω`    |
| `luminousFlux * time`          | الطاقة الضوئية    | `Q = Φ · t`    |
| `illuminance * time`           | التعرض الضوئي  | `H = E · t`    |
| `luminousFlux / power`         | الفعالية الضوئية  | `η = Φ / P`    |
| `power / solidAngle`           | الشدة الإشعاعية  | `Iₑ = P / Ω`   |
| `radiantIntensity / area`      | الإشعاع اللمعاني           | `Lₑ = Iₑ / A`  |
| `1 / length`                   | القوة الانكسارية   | `D = 1 / f`    |

## مثال عملي — هل هذه اللمبة ساطعة بما يكفي لمكتبي؟

مصباح LED تبلغ قدرته الاسمية **800 lm** بقدرة استهلاك **7 W**. يتدلى فوق مكتب مساحته **2 m²**. يتطلب
العمل المكتبي حوالي 500 lx. هل هذا كافٍ، وما مدى كفاءة هذا المصباح؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.*
import org.pcsoft.framework.kunit.optic.illuminance.*
import org.pcsoft.framework.kunit.optic.efficacy.*

val flux = 800 of lumens
val desk = (2 of meters) * (1 of meters)          // KAreaUnitInstance, 2 m²

val e = flux / desk                                // KIlluminanceUnitInstance
e into lux                                         // 400.0 — أقل قليلاً من هدف 500 lx

val eta = flux / (7 of watts)                      // KLuminousEfficacyUnitInstance
eta into lumensPerWatt                             // ≈ 114.3
eta.value / MAX_LUMINOUS_EFFICACY                  // ≈ 0.167 — 17% من الحد الفيزيائي الأقصى
```

## مثال عملي — نظارات القراءة

عدسة بعدها البؤري **40 cm** تمتلك قوة انكسارية `D = 1 / f`. عدستان رقيقتان متلامستان تجمعان قوتيهما
ببساطة، وهذا بالضبط ما يفعله عامل `+` على الكمية المصنَّفة:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)   // KReciprocalLengthUnitInstance
d into dioptres                     // 2.5

val combined = d + (1.5 of dioptres) // العدسات المتلامسة تجمع قوتيهما
combined into dioptres               // 4.0
1 / combined into meters             // 0.25 — البعد البؤري المُجمَّع
```

## الترميز

يوضح الجدول العلاقات الأساسية لهذا المجال رياضيًا مقابل Kotlin باستخدام KUnit. تستخدم الأُسس رموزًا
علوية يونيكود (`²`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر.

| الرياضيات   | Kotlin                                    | المعنى                             |
|---------------|---------------------------------------------|--------------------------------------|
| `Φ = I · Ω`   | `(100 of candelas) * (2 of steradians)`   | التدفق الضوئي من الشدة × الزاوية المجسمة |
| `E = Φ / A`   | `(800 of lumens) / desk`                  | الاستضاءة من التدفق ÷ المساحة        |
| `L = I / A`   | `(250 of candelas) / screen`              | السطوع من الشدة ÷ المساحة     |
| `Q = Φ · t`   | `(800 of lumens) * (2 of hours)`          | الطاقة الضوئية من التدفق × الزمن    |
| `H = E · t`   | `(50 of lux) * (8 of hours)`              | جرعة الضوء من الاستضاءة × الزمن  |
| `η = Φ / P`   | `(800 of lumens) / (7 of watts)`          | الفعالية الضوئية                   |
| `Iₑ = P / Ω`  | `(20 of watts) / (4 of steradians)`       | الشدة الإشعاعية                   |
| `D = 1 / f`   | `1 / (40 of centi.meters)`                | القوة الانكسارية من البعد البؤري  |

## إلى أين تذهب بعد ذلك

* [شدة الإضاءة](luminous-intensity.ar.md) — الشمعة، الكمية الأساسية الأصلية لهذا المجال.
* [التدفق الضوئي](luminous-flux.ar.md) و[الاستضاءة](illuminance.ar.md) — ما يصدره المصباح وما يستقبله السطح.
* [السطوع](luminance.ar.md) — الكمية التي يشير إليها تصنيف "النِّت" لشاشات العرض.
* [الفعالية الضوئية](luminous-efficacy.ar.md) — الجسر بين العائلتين الضوئية والإشعاعية.
* [الديوبتر](dioptre.ar.md) — القوة الانكسارية، وتوأمها الطيفي [العدد الموجي](../mechanics/wavenumber.md).
</content>
