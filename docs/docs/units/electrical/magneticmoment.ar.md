# العزم المغناطيسي ثنائي القطب

الحزمة: `org.pcsoft.framework.kunit.electric.magneticmoment`
الوحدة الأساسية: **أمبير متر مربع**
(`KMagneticMomentUnit.BASE == KMagneticMomentUnit.AMPERE_SQUARE_METER`)

النوع: **وحدة مركّبة**

العزم المغناطيسي ثنائي القطب `m` لحلقة تيار هو حاصل ضرب التيار في المساحة التي يحيط بها: `m = I · A`.
وهو ما يحدّد عزم الدوران الذي يبذله المجال المغناطيسي على الحلقة، والكمّية التي يُعبَّر بها عن المغناطيسية الذرّية
والنووية (مغنطون بور ومغنطون نووي).

الشكل القياسي للبُعد الأساسي هو `current · length²`.

## الوحدات المسمّاة

| الوحدة                | الرمز   |                الرمز البرمجي |   1 وحدة بـ A·m² |
|---------------------|---------|---------------------:|-------------------:|
| أمبير متر مربع       | `A*m^2` | `ampereSquareMeters` |                1.0 |
| جول لكل تسلا        | `J/T`   |      `joulesPerTesla` |                1.0 |
| مغنطون بور          | `μB`    |       `bohrMagnetons` | 9.2740100783e-24   |
| مغنطون نووي         | `μN`    |    `nuclearMagnetons` | 5.0507837461e-27   |

`joulesPerTesla` هي التهجئة القائمة على الطاقة لنفس الوحدة — الطاقة التي يكتسبها ثنائي القطب لكل وحدة من كثافة
التدفق المغناطيسي. تدعم كل الرموز البرمجية جميع بادئات SI.

## التفكيك

تمتلك هذه المجموعة تفكيكًا واحدًا، وكلا شكليه ينتجان نسخة متساوية القيمة من النوع نفسه:

| الشكل            | التعبير                                                       |
|------------------|-------------------------------------------------------------------|
| معامِل محكوم بالنوع | `current * area`                                                 |
| أصلي (`toX()`)    | `((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)      // 0.005 m²

val typed = (2 of amperes) * loop
val native = ((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()

typed == native                 // true
typed into ampereSquareMeters   // 0.01
```

## الحساب مع المجموعة

| التعبير                       | نوع النتيجة                      | المعنى          |
|-----------------------------|-----------------------------------|------------------|
| `current * area`            | `KMagneticMomentUnitInstance`    | `m = I · A`      |
| `magneticMoment / area`     | `KElectricCurrentUnitInstance`   | تيار الحلقة |
| `magneticMoment / current`  | `KAreaUnitInstance`              | مساحة الحلقة    |

## مثال واقعي — حلقة ملف وذرّة

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)
val m = (2 of amperes) * loop
m into ampereSquareMeters          // 0.01

// كم عدد مغنطونات بور المكافئة؟
m into bohrMagnetons                // ≈ 1.078e21

// وبالعكس: أي تيار تحتاجه حلقة مساحتها 1 cm² لتبلغ 1 A·m²؟
val small = (0.01 of meters) * (0.01 of meters)
((1 of ampereSquareMeters) / small) into amperes   // 10 000 A
```

## دلالة القيمة

يقارن `equals`/`hashCode` **قيمة A·m² المُطبَّعة**، لذا
`(1 of ampereSquareMeters) == (1 of joulesPerTesla)`. تعرض `toString()` القيمة بالوحدة الأساسية:
`"0.01 A*m^2"`.

## انظر أيضًا

* [كثافة التدفق المغناطيسي](magneticfluxdensity.ar.md) — المجال الذي يتفاعل معه هذا العزم.
* [التيار الكهربائي](ec.ar.md) و[المسافة](../kinematics/distance.ar.md) — العاملان.
* [نظرة عامة على الهندسة الكهربائية](overview.ar.md)
