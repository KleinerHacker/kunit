# الشحنة النوعية

الحزمة: `org.pcsoft.framework.kunit.electric.specificcharge`
الوحدة الأساسية: **كولوم لكل كيلوغرام**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

النوع: **وحدة مركّبة**

الشحنة النوعية `q/m` هي الشحنة التي يحملها جسم لكل وحدة من كتلته. وهي الكمّية التي قاسها ج. ج. طومسون لتحديد
الإلكترون، وهي الكمّية التي يفصل بها مطياف الكتلة الجسيمات.

الشكل القياسي للبُعد الأساسي هو `current · time · mass⁻¹`.

!!! note "مجموعة واحدة، قراءتان"
    نفس البُعد يمثّل أيضًا **جرعة التأيّن** (التعرّض) في الوقاية من الإشعاع، وكانت تُقاس تاريخيًا بالرونتغن — انظر
    [التعرّض](../thermodynamics/exposure.ar.md). ولأنّ الشكل القياسي الواحد يقابل نوعًا واحدًا، تشترك القراءتان في
    هذه المجموعة؛ والرونتغن إحدى وحداتها المسمّاة. ميّزهما بتسمية قيمك.

## الوحدات المسمّاة

| الوحدة                 | الرمز |                 الرمز البرمجي | 1 وحدة بـ C/kg |
|----------------------|--------|----------------------:|---------------:|
| كولوم لكل كيلوغرام   | `C/kg` | `coulombsPerKilogram` |            1.0 |
| رونتغن                | `R`    |            `roentgens` |        2.58e-4 |

تدعم كل الرموز البرمجية جميع بادئات SI (`milli.roentgens`، …).

## الثابت

| الثابت                       | القيمة                  | المعنى                                     |
|-----------------------------|---------------------|------------------------------------------|
| `ELECTRON_SPECIFIC_CHARGE`  | `1.75882001076e11 C/kg` | نسبة شحنة الإلكترون إلى كتلته                     |

تُحذف الإشارة: شحنة الإلكترون سالبة، لكن النسبة تُذكَر كمقدار مطلق.

## التفكيك

تمتلك هذه المجموعة تفكيكًا واحدًا، وكلا شكليه ينتجان نسخة متساوية القيمة من النوع نفسه. يُبنى الشكل الأصلي من
**قوالب الوحدات** لأنّ المجموعة تحمل حدّ كتلة.

| الشكل            | التعبير                                               |
|------------------|----------------------------------------------------------|
| معامِل محكوم بالنوع | `charge / mass`                                         |
| أصلي (`toX()`)    | `(2 of A · s / kilo.grams).toSpecificCharge()`          |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val typed = (4 of coulombs) / (2 of kilo.grams)
val native = (2 of amperes.toUnit() * (seconds pow 1) / kilo.grams.toUnit()).toSpecificCharge()

typed == native                   // true
typed into coulombsPerKilogram    // 2.0
```

## الحساب مع المجموعة

| التعبير                       | نوع النتيجة                     | المعنى              |
|-----------------------------|----------------------------------|----------------------|
| `charge / mass`             | `KSpecificChargeUnitInstance`   | `q/m`                |
| `specificCharge * mass`     | `KChargeUnitInstance`           | الشحنة الكلّية     |
| `charge / specificCharge`   | `KMassUnitInstance`             | الكتلة الحاملة    |

## مثال واقعي — الإلكترون، وقراءة تعرّض

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

// نسبة طومسون
val electron = ELECTRON_SPECIFIC_CHARGE of coulombsPerKilogram
electron into coulombsPerKilogram          // ≈ 1.7588e11

// قراءة جهاز مسح للتعرّض، والشحنة التي يحرّرها في 1 كغم من الهواء
val exposure = 1 of roentgens
exposure into coulombsPerKilogram          // 2.58e-4
(exposure * (1 of kilo.grams)) into coulombs   // 2.58e-4
```

## دلالة القيمة

يقارن `equals`/`hashCode` **قيمة C/kg المُطبَّعة**، لذا
`(1 of roentgens) == (2.58e-4 of coulombsPerKilogram)`. تعرض `toString()` القيمة بالوحدة الأساسية:
`"1.0 C/kg"`.

## انظر أيضًا

* [الشحنة](charge.ar.md) و[الكتلة](../mechanics/mass.ar.md) — المعاملان.
* [التعرّض](../thermodynamics/exposure.ar.md) — نفس النوع مقروءًا كجرعة تأيّن.
* [نظرة عامة على الهندسة الكهربائية](overview.ar.md)
