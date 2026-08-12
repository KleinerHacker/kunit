# المطاوعة الكهربائية (الإيلاستانس)

الحزمة: `org.pcsoft.framework.kunit.electric.elastance`
الوحدة الأساسية: **معكوس الفاراد** (`KElastanceUnit.BASE == KElastanceUnit.RECIPROCAL_FARAD`)

النوع: **وحدة مركّبة**

المطاوعة `S = U / Q = 1 / C` هي المعكوس الدقيق لـ [السعة الكهربائية](capacitance.ar.md). وهي الصيغة المناسبة عندما تكون
المكثّفات موصولة على **التوالي**: تُجمع المطاوعات على التوالي ببساطة، تمامًا كما تُجمع المقاومات على التوالي. وحدتها،
معكوس الفاراد، تُسمّى تقليديًا **daraf** — أي كلمة "farad" مكتوبة بالعكس.

الشكل القياسي للبُعد الأساسي هو `mass · length² · time⁻⁴ · current⁻²`.

## الوحدات المسمّاة

| الوحدة            | الرمز   |              الرمز البرمجي | 1 وحدة بـ F⁻¹ |
|-------------------|---------|-------------------:|--------------:|
| معكوس الفاراد     | `1/F`   | `reciprocalFarads` |           1.0 |
| daraf             | `daraf` |            `darafs` |           1.0 |

`darafs` هي تهجئة ثانية للوحدة الأساسية، وليست وحدة مستقلة. تدعم كل الرموز البرمجية جميع بادئات SI
(`mega.reciprocalFarads`، …).

## التفكيك

تمتلك هذه المجموعة تفكيكًا واحدًا، وكلا شكليه ينتجان نسخة متساوية القيمة من النوع نفسه. يُبنى الشكل الأصلي من
**قوالب الوحدات** لأنّ المجموعة تحمل حدّ كتلة.

| الشكل            | التعبير                                                    |
|------------------|----------------------------------------------------------------|
| معامِل محكوم بالنوع | `voltage / charge`                                            |
| أصلي (`toX()`)    | `(1 of kilo.grams · m² / s⁴ / A²).toElastance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.elastance.*

val typed = (10 of volts) / (10 of milli.coulombs)
val native = (1000 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 4) / (amperes.toUnit() pow 2))
    .toElastance()

typed == native              // true
typed into reciprocalFarads  // 1000.0
```

## الحساب مع المجموعة

| التعبير                 | نوع النتيجة                     | المعنى                    |
|------------------------|---------------------------------|----------------------------|
| `voltage / charge`     | `KElastanceUnitInstance`        | `S = U / Q`                |
| `elastance * charge`   | `KVoltageUnitInstance`          | `U = S · Q`                |
| `voltage / elastance`  | `KChargeUnitInstance`           | الشحنة المخزّنة           |
| `1 / capacitance`      | `KElastanceUnitInstance`        | `S = 1 / C`                |
| `1 / elastance`        | `KCapacitanceUnitInstance`      | `C = 1 / S`                |
| `elastance + …`        | `KElastanceUnitInstance`        | مكثّفات على التوالي       |

## مثال واقعي — مكثّفان على التوالي

مكثّفان بسعة 1 mF لكلٍّ منهما موصولان على التوالي يتصرّفان كمكثّف واحد بسعة 0.5 mF. من منظور المطاوعة هذا مجرد جمع:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.elastance.*

val total = (1 / (1 of milli.farads)) + (1 / (1 of milli.farads))
total into reciprocalFarads       // 2000.0

(1 / total) into milli.farads     // 0.5 — السعة المكافئة
```

## دلالة القيمة

يقارن `equals`/`hashCode` **قيمة F⁻¹ المُطبَّعة**، لذا `(1 of reciprocalFarads) == (1 of darafs)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"1000.0 1/F"`.

## انظر أيضًا

* [السعة الكهربائية](capacitance.ar.md) — الكمّية المعكوسة.
* [الجهد](voltage.ar.md) و[الشحنة](charge.ar.md) — معاملا التفكيك.
* [نظرة عامة على الهندسة الكهربائية](overview.ar.md)
