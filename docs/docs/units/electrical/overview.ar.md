# الهندسة الكهربائية — نظرة عامة

الحزم: `org.pcsoft.framework.kunit.ec`، `…voltage`، `…resistance`، `…charge`، `…conductance`،
`…magneticfieldstrength`

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

كل نتيجة هي الكمّية المحكومة بالنوع الصحيحة — دون تجميع وحدة مختلطة خامًا بيدك. كما يتعرّف الجهد والمقاومة
والشحنة والمواصَلة وشدّة المجال المغناطيسي على تفكيكها **الأصلي** الكامل (`kg·m²·s⁻³·A⁻¹` و`kg·m²·s⁻³·A⁻²`
و`A·s` و`kg⁻¹·m⁻²·s³·A²` و`A·m⁻¹`) عبر `toVoltage()` / `toResistance()` / `toCharge()` /
`toConductance()` / `toMagneticFieldStrength()`.

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
