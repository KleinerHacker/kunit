# الفعالية الضوئية

الحزمة: `org.pcsoft.framework.kunit.optic.efficacy`
الوحدة الأساسية: **لومن لكل واط**(`KLuminousEfficacyUnit.BASE == KLuminousEfficacyUnit.LUMEN_PER_WATT`)

النوع: **وحدة مُركَّبة**

الفعالية الضوئية `η` هي التدفق الضوئي الذي ينتجه مصباح **لكل واط من الطاقة الكهربائية**: `η = Φ / P`.
إنها الرقم الوحيد الذي يُخبرنا بمدى جودة مصدر الضوء، وهي الجسر بين العائلتين الضوئية والإشعاعية: فهي
تحوِّل الواطات، التي يقيسها الكاشف، إلى لومنات، التي تُدركها العين.

صيغتها القياسية الأساسية بالأبعاد هي `luminousIntensity¹ · solidAngle¹ · mass⁻¹ · distance⁻² · time³`.

## الوحدات

| الوحدة           | قيمة التعداد                              | الرمز |           الرمز المميز | 1 وحدة بـ lm/W |
|----------------|-----------------------------------------|--------|----------------:|---------------:|
| لومن لكل واط | `KLuminousEfficacyUnit.LUMEN_PER_WATT`  | `lm/W` | `lumensPerWatt` |            1.0 |

يقبل هذا الرمز المميز أي بادئة SI (`milli.lumensPerWatt`، `kilo.lumensPerWatt`، إلخ).

## الثابت

| الثابت                | القيمة       | المعنى                                                       |
|-------------------------|-------------|-----------------------------------------------------------------|
| `MAX_LUMINOUS_EFFICACY` | `683 lm/W`  | الحد الفيزيائي الأقصى عند 555 nm، المُستمد من تعريف الشمعة في النظام الدولي |

لا يمكن لأي مصدر ضوء أن يتجاوز 683 lm/W، لأن هذه هي فعالية الضوء الأخضر أحادي اللون عند ذروة دالة
الاستضاءة الضوئية النهارية. كل مصباح حقيقي هو جزء بسيط من هذا الحد الأقصى.

## التحليل

تمتلك هذه المجموعة تحليلًا واحدًا، وتُنتج كلتا صيغتيه نفس الكائن المصنَّف المتساوي في القيمة. لاحظ أن
الصيغة الأصلية تُجمَّع من **قوالب وحدات**: بالنسبة لمجموعة تحمل حد كتلة، تكون القيمة المختلطة الخامة هي
الناتج المبني على الغرام، بينما يُخزِّن الكائن المصنَّف قيمته بالوحدة المُسمَّاة.

| الصيغة             | التعبير                                                                       |
|------------------|-------------------------------------------------------------------------------------|
| العامل المصنَّف   | `luminousFlux / power`                                                            |
| الصيغة الأصلية (`toX()`) | `(120 of (cd·sr) / (kilo.grams · m² / s³)).toLuminousEfficacy()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val typed = (1200 of lumens) / (10 of watts)
val native = (
    120 of (candelas.toUnit() * steradians.toUnit()) /
        (kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3))
).toLuminousEfficacy()

typed == native              // true
typed into lumensPerWatt     // 120.0
```

## الحساب باستخدام المجموعة

| التعبير                          | نوع النتيجة                     | المعنى                |
|--------------------------------------|----------------------------------|-------------------------|
| `luminousFlux / power`              | `KLuminousEfficacyUnitInstance` | `η = Φ / P`            |
| `luminousEfficacy * power`          | `KLuminousFluxUnitInstance`     | `Φ = η · P`            |
| `luminousFlux / luminousEfficacy`   | `KPowerUnitInstance`            | القدرة المطلوبة     |

## مثال من واقع الحياة — مقارنة ثلاثة مصابيح

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val incandescent = (800 of lumens) / (60 of watts)
val halogen      = (800 of lumens) / (42 of watts)
val led          = (800 of lumens) / (7 of watts)

incandescent into lumensPerWatt      // ≈ 13.3
halogen into lumensPerWatt           // ≈ 19.0
led into lumensPerWatt               // ≈ 114.3

led.value / MAX_LUMINOUS_EFFICACY    // ≈ 0.167 — 17% من الحد الفيزيائي الأقصى

// كم من القدرة يحتاجها شريط LED لإنتاج 3000 lm؟
val p = (3000 of lumens) / led       // KPowerUnitInstance
p into watts                          // 26.25
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة بوحدة lm/W**، لذا
`(1 of lumensPerWatt) == (1000 of milli.lumensPerWatt)`. تعرض `toString()` القيمة بالوحدة الأساسية:
`"120.0 lm/W"`.

## انظر أيضًا

* [التدفق الضوئي](luminous-flux.ar.md) — البسط.
* [الشدة الإشعاعية](radiant-intensity.ar.md) و[الإشعاع اللمعاني](radiance.ar.md) — الجانب الإشعاعي من الجسر.
* [القدرة (كهربائية)](../electrical/power.md) — المقام.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
