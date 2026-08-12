# النشاط التحفيزي

الحزمة: `org.pcsoft.framework.kunit.thermo.catalyticactivity`
الوحدة الأساسية: **كاتال** (`KCatalyticActivityUnit.BASE == KCatalyticActivityUnit.KATAL`)

النوع: **وحدة مركّبة (constructed unit)**

النشاط التحفيزي `z` لمستحضر إنزيمي هو مقدار ما يحوّله من الركيزة **لكل وحدة زمن**:
`z = n / t`. وحدته في النظام الدولي هي **الكاتال** (1 kat = 1 mol/s) — وهي وحدة كبيرة جدًا،
لذا يُستخدم في الممارسة العملية الميكروكاتال أو **وحدة الإنزيم** التقليدية `U`
(ميكرومول واحد في الدقيقة).

صيغتها القياسية للبُعد الأساسي هي `substance¹ · time⁻¹`.

## الوحدات المسمّاة

| الوحدة          | الرمز  |          الرمز البرمجي | 1 وحدة بالـ kat        |
|-----------------|--------|-----------------------:|------------------------:|
| كاتال           | `kat`  |               `katals` |                    1.0 |
| وحدة إنزيم       | `U`    |          `enzymeUnits` | 1/60 × 10⁻⁶ ≈ 1.667e-8 |

1 U = 1 µmol/min، لذا 1 kat = 60,000,000 U و 1 U ≈ 16.67 nkat. تقبل جميع الرموز البرمجية
كل بادئات النظام الدولي (`micro.katals`، `nano.katals`، ...).

## التفكيك

تمتلك هذه المجموعة تفكيكًا واحدًا، وكلا الشكلين ينتجان نفس المثيل المكتوب النوع والمتساوي القيمة:

| الشكل                  | التعبير                                                                   |
|------------------------|------------------------------------------------------------------------------|
| المُشغّل المكتوب النوع    | `amountOfSubstance / time`                                                   |
| الأصلي (`toX()`)        | `((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val typed = (2 of moles) / (4 of seconds)
val native = ((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()

typed == native      // true
typed into katals    // 0.5
```

## الحساب باستخدام المجموعة

| التعبير                                     | نوع النتيجة                        | المعنى                |
|-----------------------------------------------|-------------------------------------|------------------------|
| `amountOfSubstance / time`                    | `KCatalyticActivityUnitInstance`    | `z = n / t`            |
| `catalyticActivity * time`                    | `KAmountOfSubstanceUnitInstance`    | `n = z · t`            |
| `amountOfSubstance / catalyticActivity`       | `KTimeUnitInstance`                 | الوقت المستغرق          |

## مثال من الواقع — تحليل إنزيمي

يحوّل أحد التحاليل **0.5 مليمول** من الركيزة خلال **10 ثوانٍ**. مُعبَّرًا عنه بالطريقتين،
والوقت الذي تستغرقه دفعة أصغر:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val z = (0.5 of milli.moles) / (10 of seconds)
z into micro.katals        // 50.0
z into enzymeUnits         // ≈ 3000.0 U

// The enzyme unit by definition: one micromole per minute
val one = (1 of micro.moles) / (1 of minutes)
one into enzymeUnits       // 1.0

// How long for 2 mmol at that activity?
val t = (2 of milli.moles) / z
t into seconds             // 40.0
```

## دلالات القيمة

تقارن `equals`/`hashCode` **قيمة الـ kat المُطبَّعة**، لذا `(1 of katals) == (1000 of milli.katals)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"5.0E-5 kat"`.

## انظر أيضًا

* [كمية المادة](amount-of-substance.ar.md) — البسط.
* [تركيز كمية المادة](concentration.ar.md) — ما يقيسه التحليل عادةً.
* [نظرة عامة على الديناميكا الحرارية](overview.ar.md)
