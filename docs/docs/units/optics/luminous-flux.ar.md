# التدفق الضوئي

الحزمة: `org.pcsoft.framework.kunit.optic.luminousflux`
الوحدة الأساسية: **لومن**(`KLuminousFluxUnit.BASE == KLuminousFluxUnit.LUMEN`)

النوع: **وحدة مُركَّبة**

التدفق الضوئي `Φ` هو **إجمالي مقدار الضوء المرئي** الذي يصدره مصدر ما في جميع الاتجاهات التي يغطيها —
وهو الرقم المطبوع على كل عبوة مصباح. إنه شدة الإضاءة مُكامَلة عبر زاوية مجسمة: `Φ = I · Ω`، أي
`1 lm = 1 cd·sr`.

صيغتها القياسية الأساسية بالأبعاد هي `luminousIntensity¹ · solidAngle¹`.

## الوحدات

| الوحدة               | قيمة التعداد                            | الرمز  |               الرمز المميز | 1 وحدة باللومن |
|--------------------|---------------------------------------|---------|--------------------:|-----------------:|
| لومن              | `KLuminousFluxUnit.LUMEN`             | `lm`    |            `lumens` |              1.0 |
| شمعة ستراديان  | `KLuminousFluxUnit.CANDELA_STERADIAN` | `cd·sr` | `candelaSteradians` |              1.0 |

`candelaSteradians` هي كتابة صريحة لتعريف اللومن — متطابقة عدديًا، لكنها تسمح لصيغة ما بتوضيح مصدر
الوحدة. يقبل كلا الرمزين المميزين أي بادئة SI (`kilo.lumens`، `milli.lumens`، إلخ).

## التحليل

تمتلك هذه المجموعة تحليلًا واحدًا، وتُنتج كلتا صيغتيه نفس الكائن المصنَّف المتساوي في القيمة:

| الصيغة                | التعبير                                                       |
|---------------------|--------------------------------------------------------------------|
| العامل المصنَّف      | `luminousIntensity * solidAngle`                                  |
| الصيغة الأصلية (`toX()`)    | `((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val typed = (100 of candelas) * (2 of steradians)
val native = ((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()

typed == native          // true
typed into lumens        // 200.0
```

## الحساب باستخدام المجموعة

| التعبير                       | نوع النتيجة                      | المعنى                       |
|----------------------------------|-----------------------------------|--------------------------------|
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`      | `Φ = I · Ω`                   |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | `I = Φ / Ω`                   |
| `luminousFlux / luminousIntensity` | `KSolidAngleUnitInstance`      | الزاوية المخروطية التي ينتشر فيها التدفق |
| `luminousFlux / area`            | `KIlluminanceUnitInstance`       | `E = Φ / A`                   |
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance`    | `Q = Φ · t`                   |
| `luminousFlux / power`           | `KLuminousEfficacyUnitInstance`  | `η = Φ / P`                   |

## مثال من واقع الحياة — مصباح متناحي الخواص

يُشِعّ مصباح عارٍ بالتساوي في جميع الاتجاهات. الكرة الكاملة تساوي `4π sr`، لذا فإن مصدرًا بقوة 100 cd
يصدر تدفقًا قدره:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val phi = (100 of candelas) * ((4 * Math.PI) of steradians)
phi into lumens          // ≈ 1256.6 lm — تقريبًا مصباح متوهج بقدرة 100 W
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة باللومن**، لذا `(1 of lumens) == (1000 of milli.lumens)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"800.0 lm"`.

## انظر أيضًا

* [شدة الإضاءة](luminous-intensity.ar.md) — التدفق لكل زاوية مجسمة.
* [الاستضاءة](illuminance.ar.md) — التدفق لكل مساحة مُضاءة.
* [الفعالية الضوئية](luminous-efficacy.ar.md) — التدفق لكل واط من الطاقة الكهربائية.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
