# الهندسة الكهربائية — نظرة عامة

الحزم: `org.pcsoft.framework.kunit.ec`، `…voltage`، `…resistance`، `…charge`، `…conductance`،
`…magneticfieldstrength`، `…capacitance`، `…inductance`، `…magneticflux`، `…magneticfluxdensity`،
`…currentdensity`، `…chargedensity`، `…resistivity`، `…conductivity`، `…power`، `…energy`

تربط الهندسة الكهربائية بين التيار المتدفّق في دارة، والجهد الذي يدفعه، والمقاومة التي تعارضه. ترتبط هذه
الثلاثة بـ**قانون أوم**، ويُعبّر KUnit عن هذا القانون مباشرةً بمعاملات `*` و`/` محكومة بالنوع: كمّية
أساسية **أصلية** واحدة (التيار الكهربائي) وكمّيات **مركّبة** من الأبعاد الأساسية (الجهد والمقاومة والشحنة
والمواصَلة وشدّة المجال المغناطيسي).

## وحدات هذا الموضوع

| الوحدة | النوع | الوحدة الأساسية | الصفحة |
|---|---|---|---|
| التيار الكهربائي | أصلية | أمبير (`A`) | [التيار الكهربائي](ec.md) |
| الجهد الكهربائي | مركّبة | فولت (`V`) | [الجهد الكهربائي](voltage.md) |
| المقاومة | مركّبة | أوم (`Ω`) | [المقاومة](resistance.md) |
| الشحنة الكهربائية | مركّبة | كولوم (`C`) | [الشحنة الكهربائية](charge.md) |
| المواصَلة | مركّبة | سيمنز (`S`) | [المواصَلة](conductance.md) |
| شدّة المجال المغناطيسي | مركّبة | أمبير لكل متر (`A/m`) | [شدّة المجال المغناطيسي](magneticfieldstrength.md) |
| السعة الكهربائية | مركّبة | فاراد (`F`) | [السعة الكهربائية](capacitance.md) |
| الحثّ الكهربائي | مركّبة | هنري (`H`) | [الحثّ الكهربائي](inductance.md) |
| التدفّق المغناطيسي | مركّبة | ويبر (`Wb`) | [التدفّق المغناطيسي](magneticflux.md) |
| كثافة التدفّق المغناطيسي | مركّبة | تسلا (`T`) | [كثافة التدفّق المغناطيسي](magneticfluxdensity.md) |
| كثافة التيار | مركّبة | أمبير لكل متر مربّع (`A/m²`) | [كثافة التيار](currentdensity.md) |
| كثافة الشحنة | مركّبة | كولوم لكل متر مكعّب (`C/m³`) | [كثافة الشحنة](chargedensity.md) |
| المقاومية | مركّبة | أوم متر (`Ω·m`) | [المقاومية](resistivity.md) |
| الموصّلية | مركّبة | سيمنز لكل متر (`S/m`) | [الموصّلية](conductivity.md) |
| القدرة | مركّبة | واط (`W`) | [القدرة (كهربائية)](power.md) |
| الطاقة | مركّبة | جول (`J`) | [الطاقة (كهربائية)](energy.md) |

القدرة والطاقة هما تقنيًا كمّية **واحدة** لكل منهما، مشتركة مع مجالات موضوعية أخرى؛ وتُوثَّقان لكل مجال مع
إحالة متبادلة بينها ([القدرة (ميكانيكا)](../mechanics/power.md)،
[القدرة (ديناميكا حرارية)](../thermodynamics/power.md)، [الطاقة (ميكانيكا)](../mechanics/energy.md)،
[الطاقة (ديناميكا حرارية)](../thermodynamics/energy.md)).

## قانون أوم كمعاملات محكومة بالنوع

| التعبير | النتيجة | الصيغة |
|---|---|---|
| `resistance * current` | الجهد | `U = R · I` |
| `current * resistance` | الجهد | `U = R · I` (تبادلي) |
| `voltage / current` | المقاومة | `R = U / I` |
| `voltage / resistance` | التيار الكهربائي | `I = U / R` |
| `current / voltage` | المواصَلة | `G = I / U` |
| `1 / resistance` | المواصَلة | `G = 1 / R` |
| `1 / conductance` | المقاومة | `R = 1 / G` |
| `conductance * voltage` | التيار الكهربائي | `I = G · U` |
| `current / conductance` | الجهد | `U = I / G` |

## معاملات محكومة بالنوع إضافية

| التعبير | النتيجة | الصيغة |
|---|---|---|
| `current * time` | الشحنة الكهربائية | `Q = I · t` |
| `current / frequency` | الشحنة الكهربائية | `Q = I / f` |
| `charge / time` | التيار الكهربائي | `I = Q / t` |
| `charge / current` | الزمن | `t = Q / I` |
| `current / length` | شدّة المجال المغناطيسي | `H = I / l` |
| `field strength * length` | التيار الكهربائي | `I = H · l` |
| `charge / voltage` | السعة الكهربائية | `C = Q / U` |
| `capacitance * voltage` | الشحنة الكهربائية | `Q = C · U` |
| `voltage * time` | التدفّق المغناطيسي | `Φ = U · t` |
| `flux / time` | الجهد | `U = Φ / t` |
| `flux / current` | الحثّ الكهربائي | `L = Φ / I` |
| `inductance * current` | التدفّق المغناطيسي | `Φ = L · I` |
| `resistance / frequency` | الحثّ الكهربائي | `L = X / ω` |
| `flux / area` | كثافة التدفّق المغناطيسي | `B = Φ / A` |
| `flux density * area` | التدفّق المغناطيسي | `Φ = B · A` |
| `current / area` | كثافة التيار | `J = I / A` |
| `current density * area` | التيار الكهربائي | `I = J · A` |
| `charge / volume` | كثافة الشحنة | `ρ = Q / V` |
| `charge density * volume` | الشحنة الكهربائية | `Q = ρ · V` |
| `resistance * length` | المقاومية | `ρ = R · A / l` |
| `1 / resistivity` | الموصّلية | `σ = 1 / ρ` |
| `1 / conductivity` | المقاومية | `ρ = 1 / σ` |
| `conductance / length` | الموصّلية | `σ = G · l / A` |
| `conductivity * length` | المواصَلة | `G = σ · A / l` |
| `voltage * current` | القدرة | `P = U · I` |
| `power / voltage` | التيار الكهربائي | `I = P / U` |
| `power / current` | الجهد | `U = P / I` |
| `power * time` | الطاقة | `W = P · t` |
| `energy / time` | القدرة | `P = W / t` |
| `charge * voltage` | الطاقة | `W = Q · U` |
| `energy / charge` | الجهد | `U = W / Q` |

كل نتيجة هي الكمّية المحكومة بالنوع الصحيحة — دون تجميع وحدة مختلطة خامًا بيدك. كما يتعرّف الجهد والمقاومة
والشحنة والمواصَلة وشدّة المجال المغناطيسي على تفكيكها **الأصلي** الكامل (`kg·m²·s⁻³·A⁻¹` و`kg·m²·s⁻³·A⁻²`
و`A·s` و`kg⁻¹·m⁻²·s³·A²` و`A·m⁻¹`) عبر `toVoltage()` / `toResistance()` / `toCharge()` /
`toConductance()` / `toMagneticFieldStrength()`. وينطبق الأمر نفسه على المجموعات الأحدث:
`toCapacitance()` (`kg⁻¹·m⁻²·s⁴·A²`)، و`toInductance()` (`kg·m²·s⁻²·A⁻²`)، و`toMagneticFlux()`
(`kg·m²·s⁻²·A⁻¹`)، و`toMagneticFluxDensity()` (`kg·s⁻²·A⁻¹`)، و`toCurrentDensity()` (`A·m⁻²`)،
و`toChargeDensity()` (`A·s·m⁻³`)، و`toResistivity()` (`kg·m³·s⁻³·A⁻²`)، و`toConductivity()`
(`kg⁻¹·m⁻³·s³·A²`)، و`toPower()` (`kg·m²·s⁻³`) و`toEnergy()` (`kg·m²·s⁻²`).

## مثال واقعي — قانون أوم حول دارة واحدة

يُسقِط حِمل جهدًا قدره **230 V** بينما يسحب تيارًا قدره **2 A**. مقاومته `R = U / I`، وإعادة إدخال هذه
المقاومة مع التيار تُعيد إنتاج الجهد `U = R · I`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

val r = (230 of volts) / (2 of amperes)   // KResistanceUnitInstance، 115 Ω
r into ohms                               // 115.0

val u = r * (2 of amperes)                // KVoltageUnitInstance
u into volts                              // 230.0

val i = (230 of volts) / (115 of ohms)    // KElectricCurrentUnitInstance
i into amperes                            // 2.0
```

## مثال واقعي — من قدرة الشبكة الكهربائية إلى الطاقة المستهلكة

مأخذ **230 V** يغذّي حِملًا بتيار **10 A** يُوصِل `P = U · I`؛ وتشغيله لمدّة ثلاث ساعات يستهلك
`W = P · t`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val p = (230 of volts) * (10 of amperes)  // KPowerUnitInstance
p into kilo.watts                         // 2.3

val w = p * (3 of hours)                  // KEnergyUnitInstance
w into kilo.joules                        // 24840.0
```

## طباعة قيمة (`toString`)

تُخرج `toString()` القيمة بالوحدة **الأساسية** لمجموعتها (القيمة + الرمز)؛ ولأي وحدة أخرى، اقرأها بـ
`into` داخل قالب نصّي وأضِف الرمز بنفسك:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u.toString()               // "230.0 V" (الوحدة الأساسية)
"${u into kilo.volts} kV"  // "0.23 kV"
```

## الترميز

يعرض الجدول قانون أوم بالترميز الرياضي مقابل ترميز Kotlin في KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `R = U / I` | `(230 of volts) / (2 of amperes)` | المقاومة من الجهد ÷ التيار |
| `U = R · I` | `r * (2 of amperes)` | الجهد من المقاومة × التيار |
| `I = U / R` | `(230 of volts) / (115 of ohms)` | التيار من الجهد ÷ المقاومة |
| `Ω = kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | المقاومة بصيغتها الأصلية القياسية |

## إلى أين بعد ذلك

* [التيار الكهربائي](ec.md) — مجموعة الأمبير الأصلية (إضافة إلى البيوت والستات-أمبير من نظام CGS).
* [الجهد الكهربائي](voltage.md) — الفولت وتفكيكاه `R · I` والصيغة الأصلية.
* [المقاومة](resistance.md) — الأوم، و`U / I`، ومعاملات قانون أوم العكسية.
* [الشحنة الكهربائية](charge.md) — الكولوم، و`I · t`، والأمبير-ساعة لسعة البطاريات.
* [المواصَلة](conductance.md) — السيمنز، و`1 / R`، و`I / U`.
* [شدّة المجال المغناطيسي](magneticfieldstrength.md) — الأمبير لكل متر، و`I / l`، والأورستد.
* [السعة الكهربائية](capacitance.md) — الفاراد، و`Q / U`، والأبفاراد/الستاتفاراد من نظام CGS.
* [الحثّ الكهربائي](inductance.md) — الهنري، و`Φ / I`، وصيغة الممانعة الحثّية `X / ω`.
* [التدفّق المغناطيسي](magneticflux.md) — الويبر، و`U · t`، والماكسويل.
* [كثافة التدفّق المغناطيسي](magneticfluxdensity.md) — التسلا، و`Φ / A`، والغاوس.
* [كثافة التيار](currentdensity.md) — أمبير لكل متر مربّع، و`I / A`، لتحديد مقطع الأسلاك.
* [كثافة الشحنة](chargedensity.md) — كولوم لكل متر مكعّب، و`Q / V`.
* [المقاومية](resistivity.md) — أوم متر، و`R · A / l`، الخاصّية المادّية وراء المقاومة.
* [الموصّلية](conductivity.md) — سيمنز لكل متر، و`1 / ρ`، و`G · l / A`.
* [القدرة (كهربائية)](power.md) — الواط، و`U · I`، ووحدات الحصان.
* [الطاقة (كهربائية)](energy.md) — الجول، و`Q · U`، و`P · t`، والكيلوواط ساعة كصيغة `kilo.watts * hours`.
