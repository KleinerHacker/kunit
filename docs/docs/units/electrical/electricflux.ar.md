# التدفق الكهربائي

الحزمة: `org.pcsoft.framework.kunit.electric.flux`
الوحدة الأساسية: **فولت متر** (`KElectricFluxUnit.BASE == KElectricFluxUnit.VOLT_METER`)

النوع: **وحدة مركّبة**

التدفق الكهربائي `Φ_E` هو شدّة المجال الكهربائي مكاملةً على مساحة: `Φ_E = E · A`. وهي الكمّية التي يُكتب بها قانون
غاوس — التدفق عبر سطح مغلق يساوي الشحنة المحصورة مقسومةً على السماحية.

الشكل القياسي للبُعد الأساسي هو `mass · length³ · time⁻³ · current⁻¹`.

!!! note "ليست كثافة التدفق الكهربائي"
    [كثافة التدفق الكهربائي](electricfluxdensity.ar.md) `D` (`C/m²`) كمّية مختلفة ذات بُعد مختلف. هذه الصفحة تتناول
    التدفق نفسه، بوحدة `V·m`.

## الوحدات المسمّاة

| الوحدة          | الرمز   |             الرمز البرمجي | 1 وحدة بـ V·m |
|-----------------|---------|------------------:|--------------:|
| فولت متر        | `V*m`   |      `voltMeters` |           1.0 |
| فولت سنتيمتر    | `V*cm`  | `voltCentimeters` |          0.01 |

تدعم كل الرموز البرمجية جميع بادئات SI (`kilo.voltMeters`، …).

## التفكيك

تمتلك هذه المجموعة تفكيكًا واحدًا، وكلا شكليه ينتجان نسخة متساوية القيمة من النوع نفسه. يُبنى الشكل الأصلي من
**قوالب الوحدات** لأنّ المجموعة تحمل حدّ كتلة: القيمة المختلطة الخام هي حاصل الضرب القائم على الغرام، بينما تخزّن
النسخة المحكومة بالنوع قيمتها في الوحدة المسمّاة.

| الشكل            | التعبير                                                     |
|------------------|-----------------------------------------------------------------|
| معامِل محكوم بالنوع | `electricFieldStrength * area`                                 |
| أصلي (`toX()`)    | `(125 of kilo.grams · m³ / s³ / A).toElectricFlux()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)     // 0.125 m²

val typed = (1000 of voltsPerMeter) * plate
val native = (125 of kilo.grams.toUnit() * (meters pow 3) / (seconds pow 3) / amperes.toUnit())
    .toElectricFlux()

typed == native          // true
typed into voltMeters    // 125.0
```

## الحساب مع المجموعة

| التعبير                             | نوع النتيجة                            | المعنى        |
|------------------------------------|----------------------------------------|----------------|
| `electricFieldStrength * area`     | `KElectricFluxUnitInstance`            | `Φ_E = E · A`  |
| `electricFlux / area`              | `KElectricFieldStrengthUnitInstance`   | `E = Φ_E / A`  |
| `electricFlux / electricFieldStrength` | `KAreaUnitInstance`                | المساحة       |

## مثال واقعي — تدفق عبر لوح مكثّف

مجال قدره **1000 V/m** يمرّ عبر لوح أبعاده 0.5 m × 0.25 m:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)
val phi = (1000 of voltsPerMeter) * plate
phi into voltMeters                 // 125.0

// المجال الذي يستلزمه تدفق مُعطى على ذلك اللوح
((125 of voltMeters) / plate) into voltsPerMeter   // 1000.0
```

## دلالة القيمة

يقارن `equals`/`hashCode` **قيمة V·m المُطبَّعة**، لذا `(1 of voltMeters) == (100 of voltCentimeters)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"125.0 V*m"`.

## انظر أيضًا

* [شدّة المجال الكهربائي](electricfieldstrength.ar.md) — المجال الذي يُكامل.
* [كثافة التدفق الكهربائي](electricfluxdensity.ar.md) — المجال `D` ذو البُعد المختلف.
* [نظرة عامة على الهندسة الكهربائية](overview.ar.md)
