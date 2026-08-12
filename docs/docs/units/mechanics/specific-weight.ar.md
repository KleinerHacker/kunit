# الوزن النوعي

الحزمة: `org.pcsoft.framework.kunit.mechanic.specificweight`
الوحدة الأساسية: **نيوتن لكل متر مكعب**
(`KSpecificWeightUnit.BASE == KSpecificWeightUnit.NEWTON_PER_CUBIC_METER`)

النوع: **وحدة مُركَّبة**

الوزن النوعي `γ` هو **قوة الوزن** لمادة ما لكل وحدة حجم: `γ = F / V = ρ · g`. وهو ما تُكتب به
الاستاتيكا المائية — فالضغط عند عمق معين هو ببساطة `p = γ · h` — وما تعتمده الهندسة المدنية للتربة
ومواد البناء. الماء له وزن نوعي يبلغ نحو 9.81 kN/m³.

الشكل القياسي الأساسي المعياري لأبعادها هو `mass · length⁻² · time⁻²`.

!!! note "وزن، وليس كتلة"
    يعتمد الوزن النوعي على تسارع الجاذبية المحلي، أما [الكثافة](density.ar.md) فلا تعتمد عليه.
    على سطح القمر تحتفظ المادة بكثافتها لكن وزنها النوعي يصبح نحو سدس قيمته على الأرض.

## الوحدات المسمّاة

| الوحدة                        | الرمز      |                     الرمز المميز | 1 وحدة بالـ N/m³ |
|---------------------------------|------------|--------------------------:|---------------:|
| نيوتن لكل متر مكعب                | `N/m^3`    |    `newtonsPerCubicMeter` |            1.0 |
| كيلونيوتن لكل متر مكعب            | `kN/m^3`   | `kilonewtonsPerCubicMeter` |           1000 |
| رطل قوة لكل قدم مكعبة              | `lbf/ft^3` | `poundsForcePerCubicFoot` |     ≈ 157.0875 |

تقبل جميع الرموز المميزة كل بادئات النظام الدولي. كما هو الحال في مجموعات القوة والضغط والكثافة
المجاورة، تُخزّن الحالة **القيمة الأولية للمكوّن على أساس الغرام**؛ وتُقسَّم القراءات بوحدة N/m³
على 1000.

## التفكيكات

تحتوي هذه المجموعة على تفكيكين **اثنين**. كلاهما يصبّان في نفس المصنع الموحِّد:

| الشكل                   | التعبير                                                       |
|--------------------------|-------------------------------------------------------------------|
| مُعامِل مُصنَّف A            | `force / volume`                                                 |
| مُعامِل مُصنَّف B            | `density * acceleration` (`γ = ρ · g`)                            |
| الشكل الأصلي (`toX()`)   | `(1 of kilo.grams / m² / s²).toSpecificWeight()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.acceleration.standardGravities
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val cubicMeter = (1 of meters) * (1 of meters) * (1 of meters)
val water = (1000 of kilo.grams) / cubicMeter

val viaForce = (9806.65 of newtons) / cubicMeter        // A
val viaDensity = water * (1 of standardGravities)       // B

viaForce == viaDensity                                   // true
viaForce into newtonsPerCubicMeter                       // 9806.65
```

## الحساب باستخدام المجموعة

| التعبير                            | نوع النتيجة                    | المعنى                |
|---------------------------------------|-------------------------------------|--------------------------|
| `force / volume`                     | `KSpecificWeightUnitInstance`      | `γ = F / V`              |
| `density * acceleration`             | `KSpecificWeightUnitInstance`      | `γ = ρ · g`              |
| `specificWeight * volume`            | `KForceUnitInstance`               | قوة الوزن                  |
| `force / specificWeight`             | `KVolumeUnitInstance`              | الحجم الذي يملؤه            |
| `specificWeight / acceleration`      | `KDensityUnitInstance`             | العودة إلى `ρ`              |
| `specificWeight / density`           | `KAccelerationUnitInstance`        | العودة إلى `g`              |

## مثال من الواقع — وزن خزان مياه

خزان مياه سعته **300 لتر**، والقوة التي يمارسها محتواه على الأرضية:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val water = 9.80665 of kilonewtonsPerCubicMeter
val weight = water * (300 of liters)      // KForceUnitInstance
weight into newtons                        // ≈ 2942.0 N

// وبالعكس: ما هو الحجم الذي يزن 1 كيلونيوتن؟
val v = (1000 of newtons) / water          // KVolumeUnitInstance
v into liters                               // ≈ 102.0 l
```

## دلالة القيم

يقارن `equals`/`hashCode` **القيمة المُطبَّعة للمكوّن**، لذا
`(1 of kilonewtonsPerCubicMeter) == (1000 of newtonsPerCubicMeter)`. تعرض `toString()` القيمة
بالوحدة الأساسية: `"9807.0 N/m^3"`.

## طالع أيضًا

* [الكثافة](density.ar.md) — النظير المعتمد على الكتلة، المستقل عن الجاذبية.
* [القوة](force.ar.md) و[الضغط](pressure.ar.md) — المجموعتان المجاورتان.
* [نظرة عامة على الميكانيكا](overview.ar.md)
