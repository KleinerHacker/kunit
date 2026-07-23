# المنسّقات المخصصة

يعرض الفعل [`format`](formatting.md) و`toString` ذو المعاملات النص عبر `KUnitFormatter` قابل للاستبدال. يُنتج
`KDefaultUnitFormatter` المُرفق نصًا عاديًا مثل `"10.8 km/h"`، لكن يمكنك إدخال عرض مخصص تمامًا — مثل إخراج
**LaTeX** أو **MathML** لعارض صيغ رسومي، أو HTML، أو أي ترميز خاص بمجال. هذا يجعل توسيع kunit نحو أطر
خارجية تحوّل السلسلة إلى صيغة منسّقة أمرًا سهلًا.

## العقد

يستقبل المنسّق كل ما يحتاجه في `KUnitFormatContext` واحد ويُعيد السلسلة النهائية:

```kotlin
interface KUnitFormatter {
    fun format(context: KUnitFormatContext): String
}

data class KUnitFormatContext(
    val value: Double,            // الرقم، مُحوَّل بالفعل إلى الوحدة الهدف
    val units: List<KUnitTerm>,   // حدود البُعد الهدف (ببيانات عرض البادئة/الأس)
    val pattern: String? = null,  // نمط java.util.Formatter اختياري للرقم
    val locale: Locale = Locale.getDefault(),
)
```

يُمرَّر كل شيء في كائن سياق **واحد** حتى تنمو الواجهة إضافيًا (تأخذ الحقول الجديدة قيمًا افتراضية) دون كسر
تنفيذك. تغطي دالتان مساعدتان قابلتان لإعادة الاستخدام اللبنات الشائعة:

- `KUnitFormatContext.renderValue()` — يعرض الرقم: `Double.toString()` عندما يكون `pattern` قيمته `null`،
  وإلا `String.format(locale, pattern, value)`.
- `KUnitTerm.displaySymbol` — رمز الحد كما كُتب (`"km"`، `"h"`)، مع احترام بيانات العرض؛ ويعود إلى رمز أساس
  المجموعة عند عدم وجودها.

يخبرك أس الحد `exponent` بالقوة (موجب = بسط، سالب = مقام)؛ ويقرر منسّقك كيفية عرض الأس.

## خطوة بخطوة: منسّق LaTeX

يعرض المنسّق التالي `\frac{...}{...}` من حدود البسط والمقام، مستخدمًا `\mathrm{...}` لكل رمز وحدة:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.*

object LatexFormatter : KUnitFormatter {
    override fun format(context: KUnitFormatContext): String {
        // 1. تقسيم إلى بسط (أس > 0) ومقام (أس < 0)
        val (numerator, denominator) = context.units.partition { it.exponent > 0 }

        // 2. عرض حد واحد، مثل \mathrm{km} أو \mathrm{s}^{2}
        fun render(terms: List<KUnitTerm>) = terms.joinToString(" ") { term ->
            val magnitude = kotlin.math.abs(term.exponent)
            val base = "\\mathrm{${term.displaySymbol}}"      // يستخدم بيانات العرض
            if (magnitude == 1) base else "$base^{$magnitude}"
        }

        // 3. الرقم عبر الدالة المساعدة (يحترم النمط + الإعدادات المحلية)
        val value = context.renderValue()

        // 4. التجميع
        if (denominator.isEmpty()) return "$value\\,${render(numerator)}".trim()
        return "$value\\,\\frac{${render(numerator)}}{${render(denominator)}}"
    }
}
```

## الاستخدام

مرّر المنسّق صراحةً — لا يتغير السلوك الافتراضي أبدًا ما لم تطلب ذلك:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// التنسيق إلى وحدة هدف بمنسّق مخصص
v.format(kilo.meters / hours, "%.1f", Locale.US, LatexFormatter)
// "10.8\,\frac{\mathrm{km}}{\mathrm{h}}"

// أو عرض الوحدات الأساسية بمنسّق مخصص (بلا هدف)
(5 of meters).toString(pattern = null, formatter = LatexFormatter)
// "5.0\,\mathrm{m}"
```

## ملاحظات

- أبقِ المنسّق **بلا حالة** ومن ثمّ آمنًا للخيوط — فـ `KDefaultUnitFormatter` المُرفق كائن `object` عادي،
  وكذلك `LatexFormatter` أعلاه.
- يستقبل `KUnitFormatContext` القيمة **مُحوَّلة بالفعل** إلى الوحدة الهدف، لذا لا يجري المنسّق تحويل وحدات
  بنفسه — بل يعرض فقط.
- تحمل حدود `units` بيانات عرض (`KUnitTerm.display`)؛ اقرأ الرمز دائمًا عبر `displaySymbol` حتى تُعرض الوحدات
  ذات البادئات والبديلة (`km`، `mi`، `KiB`) بشكل صحيح.
