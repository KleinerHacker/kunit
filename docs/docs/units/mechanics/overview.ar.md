# الميكانيكا — نظرة عامة

الحزم: `org.pcsoft.framework.kunit.mass`، `…force`، `…pressure`، `…density`، `…areadensity`، `…power`،
`…energy`

تسأل الميكانيكا (الديناميكا) **لماذا** تتحرّك الأجسام وكيف تتوزّع المادة: التفاعل بين الكتلة، والقوى
المؤثّرة فيها، والضغط الذي تُحدثه قوة على مساحة، ومقدار الكتلة المحشورة في حجم أو على سطح. بناءً على
معدّلات [علم الحركة](../kinematics/overview.md)، يضيف هذا الموضوع كمّية أساسية **أصلية** واحدة (الكتلة)
وأربع كمّيات **مركّبة** من الكتلة والطول والزمن.

## وحدات هذا الموضوع

| الوحدة | النوع | الوحدة الأساسية | الصفحة |
|---|---|---|---|
| الكتلة | أصلية | غرام (`g`) | [الكتلة](mass.md) |
| القوة | مركّبة | نيوتن (`N`) | [القوة](force.md) |
| الضغط | مركّبة | باسكال (`Pa`) | [الضغط](pressure.md) |
| الكثافة | مركّبة | كيلوغرام لكل متر مكعّب (`kg/m³`) | [الكثافة](density.md) |
| الكثافة السطحية | مركّبة | كيلوغرام لكل متر مربّع (`kg/m²`) | [الكثافة السطحية](areadensity.md) |
| القدرة | مركّبة | واط (`W`) | [القدرة (ميكانيكا)](power.md) |
| الطاقة | مركّبة | جول (`J`) | [الطاقة (ميكانيكا)](energy.md) |

القدرة والطاقة هما تقنيًا كمّية **واحدة** لكل منهما، مشتركة مع مجالات موضوعية أخرى؛ وتُوثَّقان لكل مجال مع
إحالة متبادلة بينها ([القدرة (كهربائية)](../electrical/power.md)،
[القدرة (ديناميكا حرارية)](../thermodynamics/power.md)، [الطاقة (كهربائية)](../electrical/energy.md)،
[الطاقة (ديناميكا حرارية)](../thermodynamics/energy.md)).

## كيف ترتبط الكمّيات

| التعبير | النتيجة | الصيغة |
|---|---|---|
| `mass * acceleration` | القوة | `F = m · a` |
| `force / area` | الضغط | `p = F / A` |
| `pressure * area` | القوة | `F = p · A` |
| `mass / volume` | الكثافة | `ρ = m / V` |
| `density * length` | الكثافة السطحية | `ρ_A = ρ · d` |
| `force * speed` | القدرة | `P = F · v` |
| `power / speed` | القوة | `F = P / v` |
| `power / force` | السرعة | `v = P / F` |
| `force * length` | الطاقة (شغل) | `W = F · s` |
| `power * time` | الطاقة | `W = P · t` |
| `energy / time` | القدرة | `P = W / t` |
| `energy / power` | الزمن | `t = W / P` |

## مثال واقعي — قانون نيوتن الثاني وضغط التماس مع الأرض

يُسرَّع جسم كتلته **2 kg** بالجاذبية القياسية، وتُوزّع قوة الوزن الناتجة على مساحة قدم قدرها **0.5 m²**.
القوة هي `F = m · a`، والضغط هو `p = F / A`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*
import org.pcsoft.framework.kunit.pressure.*

val f = (2 of kilo.grams) * (1 of standardGravities)  // KForceUnitInstance
f into newtons                                         // ≈ 19.61 (N)

val area = (1 of meters) * (0.5 of meters)             // KAreaUnitInstance، 0.5 m²
val p = f / area                                       // KPressureUnitInstance
p into pascals                                         // ≈ 39.23 (Pa)
```

## مثال واقعي — كتلة قطعة فولاذية من كثافتها

للفولاذ كثافة **7850 kg/m³**. كتلة قطعة حجمها **2 L** هي `m = ρ · V`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance، 7850 kg/m³
val mass = steel * (2 of liters)                          // KMassUnitInstance
mass into kilo.grams                                      // 15.7 (kg لكل 2 L)
```

## مثال واقعي — شغل وقدرة رافعة

تسحب رافعة بقوّة **100 N** على مسافة **5 m** خلال **5 s**. الشغل هو `W = F · s`، والقدرة `P = W / t` —
وهي تساوي الصيغة الميكانيكية المباشرة `P = F · v`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val w = (100 of newtons) * (5 of meters)                    // KEnergyUnitInstance
w into joules                                                // 500.0

val p = w / (5 of seconds)                                   // KPowerUnitInstance
p into watts                                                 // 100.0

val direct = (100 of newtons) * ((1 of meters) / (1 of seconds)) // P = F · v، 100 W
p == direct                                                  // true
```

## طباعة قيمة (`toString`)

تُخرج `toString()` القيمة بالوحدة **الأساسية** لمجموعتها (القيمة + الرمز)؛ ولأي وحدة أخرى، اقرأها بـ
`into` داخل قالب نصّي وأضِف الرمز بنفسك:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f.toString()                 // "10.0 N" (الوحدة الأساسية)
"${f into kilo.newtons} kN"  // "0.01 kN"
```

## الترميز

يعرض الجدول التالي العلاقات الأساسية لهذا المجال بالترميز الرياضي مقابل ترميز Kotlin في KUnit. تُكتب
الأُسّس بحروف Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `F = m · a` | `(2 of kilo.grams) * (1 of standardGravities)` | القوة من الكتلة × التسارع |
| `p = F / A` | `f / area` | الضغط من القوة ÷ المساحة |
| `F = p · A` | `p * area` | القوة من الضغط × المساحة |
| `ρ = m / V` | `(6 of kilo.grams) / (2 of liters)` | الكثافة من الكتلة ÷ الحجم |
| `m = ρ · V` | `steel * (2 of liters)` | الكتلة من الكثافة × الحجم |
| `W = F · s` | `(100 of newtons) * (5 of meters)` | الشغل من القوة × الطول |
| `P = F · v` | `(100 of newtons) * ((1 of meters) / (1 of seconds))` | القدرة من القوة × السرعة |
| `P = W / t` | `w / (5 of seconds)` | القدرة من الشغل ÷ الزمن |

## إلى أين بعد ذلك

* [الكتلة](mass.md) — الكمّية الأساسية الأصلية (مُطبَّعة بالغرام).
* [القوة](force.md) و[الضغط](pressure.md) — قانون نيوتن والقوة على مساحة.
* [الكثافة](density.md) و[الكثافة السطحية](areadensity.md) — الكتلة لكل حجم ولكل سطح.
* [القدرة (ميكانيكا)](power.md) — الواط، و`F · v`، ووحدات الحصان.
* [الطاقة (ميكانيكا)](energy.md) — الجول كشغل ميكانيكي `F · s`.
