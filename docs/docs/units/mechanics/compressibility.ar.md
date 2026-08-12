# قابلية الانضغاط

الحزمة: `org.pcsoft.framework.kunit.mechanic.compressibility`
الوحدة الأساسية: **مقلوب الباسكال**
(`KCompressibilityUnit.BASE == KCompressibilityUnit.RECIPROCAL_PASCAL`)

النوع: **وحدة مُركَّبة**

قابلية الانضغاط `κ = −(1/V)·(∂V/∂p)` تُبيّن مقدار انكماش حجم المادة لكل وحدة ضغط.
وهي المقلوب الدقيق **لمعامل الانضغاط الحجمي** `K`، وهو معامل مرونة وبالتالي يُعدّ
[ضغطًا](pressure.ar.md). الماء له قيمة تبلغ نحو 4.5 × 10⁻¹⁰ Pa⁻¹ — وهذا هو السبب في أن
الهيدروليكا يمكنها معاملته على أنه غير قابل للانضغاط.

الشكل القياسي الأساسي المعياري لأبعادها هو `mass⁻¹ · length · time²`.

## الوحدات المسمّاة

| الوحدة                             | الرمز    |                   الرمز المميز | 1 وحدة بالـ 1/Pa |
|--------------------------------------|---------|------------------------:|---------------:|
| مقلوب الباسكال                        | `1/Pa`  |     `reciprocalPascals` |            1.0 |
| مقلوب البار                           | `1/bar` |        `reciprocalBars` |           1e-5 |
| مقلوب الضغط الجوي القياسي              | `1/atm` | `reciprocalAtmospheres` |      1/101 325 |

تقبل جميع الرموز المميزة كل بادئات النظام الدولي (`pico.reciprocalPascals`، وغيرها). كما هو الحال
في مجموعة الضغط المجاورة، تُخزّن الحالة **القيمة الأولية للمكوّن على أساس الغرام**.

## الحساب باستخدام المجموعة

| التعبير                        | نوع النتيجة                         | المعنى                            |
|-----------------------------------|-----------------------------------------|--------------------------------------|
| `1 / pressure`                    | `KCompressibilityUnitInstance`         | `κ = 1 / K`                          |
| `1 / compressibility`             | `KPressureUnitInstance`                | `K = 1 / κ`                          |
| `compressibility * pressure`      | `Double`                               | التغير النسبي في الحجم `ΔV/V`         |

المقلوبان دقيقان: أسس المكوّنات (`g·m⁻¹·s⁻²` للضغط، و`g⁻¹·m·s²` هنا) هي مقلوبات بعضها البعض،
لذا لا حاجة إلى أي معامل ربط.

## مثال من الواقع — كم ينضغط الماء؟

معامل الانضغاط الحجمي للماء يبلغ نحو **2.2 غيغا باسكال**. فما هي قابلية انضغاطه، وكم ينكمش تحت
ضغط 10 ميغا باسكال (يعادل تقريبًا عمق 1000 متر تحت الماء)؟

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.compressibility.*

val kappa = 1 / (2.2 of giga.pascals)          // KCompressibilityUnitInstance
kappa into reciprocalPascals                    // ≈ 4.545e-10

val shrink = kappa * (10 of mega.pascals)       // Double
shrink                                           // ≈ 0.00455 — فقدان 0.45٪ من الحجم

// والعودة إلى معامل الانضغاط الحجمي
(1 / kappa) into giga.pascals                    // ≈ 2.2
```

## دلالة القيم

يقارن `equals`/`hashCode` **القيمة المُطبَّعة للمكوّن**، لذا
`(1 of reciprocalBars) == (1e-5 of reciprocalPascals)`. تعرض `toString()` القيمة بالوحدة
الأساسية: `"1.0 1/Pa"`.

## طالع أيضًا

* [الضغط](pressure.ar.md) — الكمية المقلوبة (معامل الانضغاط الحجمي).
* [الإجهاد ومعامل المرونة](stress.ar.md) — النوع نفسه عند قراءته كخاصية للمادة.
* [نظرة عامة على الميكانيكا](overview.ar.md)
