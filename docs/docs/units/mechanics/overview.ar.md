# الميكانيكا — نظرة عامة

الحزم: `org.pcsoft.framework.kunit.mass`، `…force`، `…pressure`، `…density`، `…areadensity`

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

## كيف ترتبط الكمّيات

| التعبير | النتيجة | الصيغة |
|---|---|---|
| `mass * acceleration` | القوة | `F = m · a` |
| `force / area` | الضغط | `p = F / A` |
| `pressure * area` | القوة | `F = p · A` |
| `mass / volume` | الكثافة | `ρ = m / V` |
| `density * length` | الكثافة السطحية | `ρ_A = ρ · d` |

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

## إلى أين بعد ذلك

* [الكتلة](mass.md) — الكمّية الأساسية الأصلية (مُطبَّعة بالغرام).
* [القوة](force.md) و[الضغط](pressure.md) — قانون نيوتن والقوة على مساحة.
* [الكثافة](density.md) و[الكثافة السطحية](areadensity.md) — الكتلة لكل حجم ولكل سطح.
