# شدة الإضاءة

الحزمة: `org.pcsoft.framework.kunit.optic.luminousintensity`
الوحدة الأساسية: **الشمعة**(`KLuminousIntensityUnit.BASE == KLuminousIntensityUnit.CANDELA`)

النوع: **وحدة أصلية**

شدة الإضاءة `I` هي التدفق الضوئي الذي يصدره مصدر ما **لكل زاوية مجسمة** في اتجاه معين. وحدتها، الشمعة،
هي **الوحدة الأساسية السابعة في النظام الدولي** — والوحدة الأساسية الوحيدة التي تُعرَّف من خلال الإدراك
البشري: 1 cd هي شدة مصدر يصدر إشعاعًا أحادي اللون بتردد 540 THz وشدة إشعاعية قدرها 1/683 W/sr في ذلك
الاتجاه.

هذه المجموعة هي مجموعة أصلية **بسيطة، أحادية البُعد** (بدون أنواع فرعية مُتخصِّصة بالأس):
تُغلِّف `KLuminousIntensityUnitInstance` حدًّا واحدًا من `KLuminousIntensityUnit.CANDELA`، ويُخزَّن
دائمًا بعد تطبيعه إلى شمعات.

## الوحدات

| الوحدة            | قيمة التعداد                                | الرمز   |          الرمز المميز | 1 وحدة بالشمعة |
|-----------------|-------------------------------------------|----------|---------------:|-------------------:|
| شمعة         | `KLuminousIntensityUnit.CANDELA`          | `cd`     |     `candelas` |                1.0 |
| شمعة هيفنر   | `KLuminousIntensityUnit.HEFNER_CANDLE`    | `HK`     | `hefnerCandles` |              0.903 |
| قدرة الشمعة     | `KLuminousIntensityUnit.CANDLEPOWER`      | `cp`     |  `candlepower` |              0.981 |
| كارسيل          | `KLuminousIntensityUnit.CARCEL`           | `carcel` |      `carcels` |               9.74 |

هذه المُدخلات غير التابعة للنظام الدولي الثلاثة هي المعايير الوطنية التاريخية التي سبقت الشمعة —
مصباح هيفنر الألماني، والشمعة الدولية البريطانية، ومصباح كارسيل الزيتي الفرنسي. تم الاحتفاظ بها ليتسنى
قراءة صحائف البيانات القديمة مباشرة.

كل رمز مميز هو `KLuminousIntensityUnitInstance` بقيمة 1 يُستخدم مع `of` (للبناء) و`into` (للقراءة).
تقبل جميع الرموز المميزة أي بادئة SI (`milli.candelas`، `kilo.candelas`، إلخ).

## الحساب باستخدام المجموعة

| التعبير                       | نوع النتيجة                     | المعنى                          |
|----------------------------------|----------------------------------|-----------------------------------|
| `luminousIntensity + …`          | `KLuminousIntensityUnitInstance` | جمع من النوع نفسه               |
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`     | `Φ = I · Ω`، التدفق الصادر    |
| `luminousIntensity / area`       | `KLuminanceUnitInstance`        | `L = I / A`، توهُّج السطح  |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | الرجوع من التدفق                   |

يتم تحويل الصيغة الأصلية باستخدام `toLuminousIntensity()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.optic.luminousintensity.*

val raw = (1200 of candelas).toUnit()   // KMixedUnitInstance
raw.toLuminousIntensity() into candelas // 1200.0
```

## مثال من واقع الحياة — مصباح أمامي لسيارة

مصباح أمامي بضوء منخفض مُحدَّد بـ **1200 cd** على محوره البصري. عند انتشاره في مخروط قدره 0.05 sr،
يكون التدفق الضوئي الموجَّه فعليًا إلى الطريق:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.*
import org.pcsoft.framework.kunit.optic.luminousflux.*

val i = 1200 of candelas
i into kilo.candelas                     // 1.2

val beam = i * (0.05 of steradians)      // KLuminousFluxUnitInstance
beam into lumens                         // 60.0 lm ضمن مخروط الحزمة
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة بالشمعة**، لذا `(1 of candelas) == (1000 of milli.candelas)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"1200.0 cd"`.

## انظر أيضًا

* [التدفق الضوئي](luminous-flux.ar.md) — الشدة مُكامَلة عبر زاوية مجسمة.
* [السطوع](luminance.ar.md) — الشدة لكل مساحة مُصدِرة.
* [الشدة الإشعاعية](radiant-intensity.ar.md) — النظير الإشعاعي، غير المُرجَّح بالعين.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
