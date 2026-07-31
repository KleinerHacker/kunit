# كثافة الشحنة

الحزمة: `org.pcsoft.framework.kunit.electric.chargedensity`
الوحدة الأساسية: **كولوم لكل متر مكعب** (`KChargeDensityUnit.BASE == KChargeDensityUnit.COULOMB_PER_CUBIC_METER`)

النوع: **وحدة مركّبة**

كثافة الشحنة (الحجمية) وحدة **مركّبة**: التركيب `current¹ · time¹ · length⁻³`
(`A·s·m⁻³` = `C/m³`). يغلّف `KChargeDensityUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود —
`KElectricCurrentUnit.BASE` (أمبير) بالأس `+1`، و`KTimeUnit.BASE` (ثانية) بالأس `+1`، و
`KDistanceUnit.BASE` (متر) بالأس `-3`. ولأن جميع المكوّنات مخزّنة بوحدتها الأساسية في المجموعة، فإن القيمة المخزّنة هي
مباشرةً القراءة بوحدة C/m³.

## إنشاء كثافة شحنة

لا تملك كثافة الشحنة **أي رمز خام ولا بادئات خاصة بها** — كل صياغة (C/m³، mC/cm³، …) هي نسبة. تُنشأ كتعبير أو عبر عامل
`charge / volume` المكتوب بنوعه صريحًا، وتُقرأ باستخدام `into` مقابل تعبير كهذا. تأتي البادئات من رموز المكوّنات
(`milli.coulombs`، `centi.meters`):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val rho = (6 of coulombs) / (2 of liters)  // KChargeDensityUnitInstance، 3 C/L = 3000 C/m³
rho into (coulombs / (meters pow 3))       // 3000.0
rho into (coulombs / (centi.meters pow 3)) // 0.003 (= 3 mC/cm³)
rho into (milli.coulombs / (meters pow 3)) // 3000000.0
```

## تفكيكات متعددة

يمكن الوصول إلى كثافة الشحنة عبر **تفكيكات مكافئة**، وجميعها تُنتج كثافة شحنة متساوية القيمة:

| التعبير                | نوع النتيجة                  | المعنى                           |
|------------------------|------------------------------|----------------------------------|
| `charge / volume`      | `KChargeDensityUnitInstance` | التعريف `ρ = Q / V`              |
| `current·time/length³` | عبر `.toChargeDensity()`     | التعبير الأصلي القياسي `A·s·m⁻³` |

تُعيد الصيغة المكتوبة بنوعها صريحًا كثافة شحنة مباشرة. أما التعبير الأصلي بالكامل فيبقى
`KMixedUnitInstance` عامًّا ويُضيَّق عبر `toChargeDensity()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). كلا المسارين متساويا القيمة.

تربط العمليات العكسية بين الشحنة والحجم وكثافة الشحنة:

| التعبير                  | نوع النتيجة           | المعنى               |
|--------------------------|-----------------------|----------------------|
| `chargeDensity * volume` | `KChargeUnitInstance` | `Q = ρ · V`          |
| `volume * chargeDensity` | `KChargeUnitInstance` | `Q = V · ρ` (تبادلي) |
| `charge / chargeDensity` | `KVolumeUnitInstance` | `V = Q / ρ`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

// مثال واقعي - شحنة فضائية في محلول كهرلي: 12 mC من الشحنة الصافية مذابة في 4 لترات من المحلول تعطي
// كثافة شحنة قدرها 3 C/m³.
val rho = (0.012 of coulombs) / (4 of liters)   // KChargeDensityUnitInstance، 3 C/m³

// نفس كثافة الشحنة كتعبير أصلي A·s·m⁻³:
val raw = (0.012 of coulombs).toUnit() / (0.004 of (meters pow 3))
raw.toChargeDensity() == rho                    // true

// العودة إلى الشحنة المحتواة في 4 لترات، وإلى الحجم الذي يحتوي 12 mC:
val q = rho * (4 of liters)                     // KChargeUnitInstance
q into coulombs                                 // 0.012
val v = (0.012 of coulombs) / rho               // KVolumeUnitInstance
v into liters                                   // 4.0
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val a = (3 of coulombs) / (1 of liters)     // 3000 C/m³
val b = (1 of coulombs) / (1 of liters)     // 1000 C/m³
(a + b) into (coulombs / (meters pow 3))    // 4000.0
(a - b) into (coulombs / (meters pow 3))    // 2000.0
a > b                                       // true
a * b                                       // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

((1 of coulombs) / (1 of liters)).toString() // "1000.0 C/m³" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات     | Kotlin                                   | المعنى                                                          |
|---------------|------------------------------------------|-----------------------------------------------------------------|
| `C/m³`        | `coulombs / (meters pow 3)`              | كثافة الشحنة، الوحدة الأساسية (كولوم لكل متر مكعب) — صيغة الكسر |
| `C·m⁻³`       | `coulombs * (meters pow -3)`             | نفس كثافة الشحنة كحاصل ضرب بأس سالب                             |
| `A·s/m³`      | `amperes * seconds / (meters pow 3)`     | الصيغة الأصلية القياسية (تيار·زمن / طول³)                       |
| `mC/cm³`      | `milli.coulombs / (centi.meters pow 3)`  | ميلي كولوم لكل سنتيمتر مكعب                                     |
| `12 mC / 4 L` | `(12 of milli.coulombs) / (4 of liters)` | البناء من الشحنة ÷ الحجم                                        |
