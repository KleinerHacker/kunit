# كثافة التدفق الكهربائي

الحزمة: `org.pcsoft.framework.kunit.electricfluxdensity`
الوحدة الأساسية: **كولوم لكل متر مربع**
(`KElectricFluxDensityUnit.BASE == KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER`)

النوع: **وحدة مركّبة**

كثافة التدفق الكهربائي وحدة **مركّبة**: التركيب `current · time · length⁻²`
(`A·s·m⁻²` = `C/m²`). يغلّف `KElectricFluxDensityUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود —
`KElectricCurrentUnit.BASE` (أمبير) بالأس `+1`، و`KTimeUnit.BASE` (ثانية) بالأس `+1`، و`KDistanceUnit.BASE`
(متر) بالأس `-2`. لا تحمل هذه المجموعة أي بُعد كتلة، لذا لا حاجة إلى جسر غرام/كيلوغرام؛ وتُطبَّع القيمة
المخزّنة دائمًا إلى كولوم لكل متر مربع.

كثافة التدفق `D` (وتُسمّى أيضًا الإزاحة الكهربائية) هي الشحنة لكل وحدة مساحة. **كثافة الشحنة السطحية**
`σ` هي نفس الكمية من حيث الأبعاد، ولذلك تُمثَّل بهذه المجموعة نفسها بدلًا من مجموعة منفصلة. وترتبط `D`
بـ[شدة المجال الكهربائي](electricfieldstrength.ar.md) عبر [السماحية](permittivity.ar.md)
(`D = ε · E`). النظير أحادي البُعد هو [كثافة الشحنة الخطية](linearchargedensity.ar.md)، والنظير
ثلاثي الأبعاد هو [كثافة الشحنة](chargedensity.ar.md).

## إنشاء كثافة تدفق كهربائي

تُنشأ كثافة التدفق برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| كثافة التدفق | الرمز | الرمز البرمجي | 1 وحدة بـ C/m² |
|---|---|---:|---:|
| كولوم لكل متر مربع | `C/m²` | `coulombsPerSquareMeter` | 1.0 |
| كولوم لكل سنتيمتر مربع | `C/cm²` | `coulombsPerSquareCentimeter` | 1.0e4 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`micro.coulombsPerSquareMeter`،
`milli.coulombsPerSquareMeter`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electricfluxdensity.*

val d = 5 of micro.coulombsPerSquareMeter   // لوحة مكثّف مشحونة
d into micro.coulombsPerSquareMeter         // 5.0
d into coulombsPerSquareMeter               // 5.0e-6
(1 of coulombsPerSquareCentimeter) into coulombsPerSquareMeter // 10000.0
```

## تفكيكات متعددة

يمكن الوصول إلى كثافة التدفق الكهربائي عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج كثافة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `charge / area` | `KElectricFluxDensityUnitInstance` | `D = Q / A`، الشحنة موزّعة على مساحة |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E` (تبادلي، انظر [السماحية](permittivity.ar.md)) |
| `current·time/length²` | عبر `.toElectricFluxDensity()` | التعبير الأصلي القياسي `A·s·m⁻²` |

تُعيد الصيغ المكتوبة بأنواع صريحة كثافة تدفق مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toElectricFluxDensity()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين الشحنة والمساحة وكثافة التدفق:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `electricFluxDensity * area` | `KChargeUnitInstance` | `Q = D · A` (تبادلي) |
| `charge / electricFluxDensity` | `KAreaUnitInstance` | `A = Q / D` |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.distance.ares
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.electricfluxdensity.*

// مثال واقعي - 20 µC موزّعة على لوحة مكثّف مساحتها 4 m² تعطي 5 µC/m².
val plate: KAreaUnitInstance = 0.04 of ares            // 4 m²
val d = (20 of micro.coulombs) / plate                 // 5e-6 C/m²

// نفس كثافة التدفق كتعبير أصلي A·s·m⁻²:
val raw = 5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 2)
raw.toElectricFluxDensity() == d                       // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfluxdensity.*

val s = (1 of coulombsPerSquareMeter) + (1 of coulombsPerSquareCentimeter)  // 10001 C/m²
(1 of coulombsPerSquareCentimeter) > (1 of coulombsPerSquareMeter)          // true
(2 of coulombsPerSquareMeter) * (3 of coulombsPerSquareMeter)               // KMixedUnitInstance
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfluxdensity.*

(1 of coulombsPerSquareCentimeter).toString()   // "10000.0 C/m²" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `⁻²`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `C/m²` | `coulombsPerSquareMeter` | كثافة التدفق الكهربائي، الوحدة الأساسية (رمز مسمّى) |
| `Q / A` | `(20 of micro.coulombs) / plate` | كثافة التدفق من الشحنة على مساحة |
| `ε · E` | `(1 of vacuumPermittivity) * (1 of voltsPerMeter)` | كثافة التدفق من السماحية وشدة المجال |
| `A·s/m²` | `((amperes pow 1) * (seconds pow 1)) / (meters pow 2)` | كثافة التدفق كتيار·زمن / طول² (صيغة الكسر) |
| `A·s·m⁻²` | `(amperes pow 1) * (seconds pow 1) * (meters pow -2)` | نفس كثافة التدفق كحاصل ضرب خالص |
| `µC/m²` | `micro.coulombsPerSquareMeter` | كثافة تدفق ببادئة (ميكروكولوم لكل متر مربع) |
