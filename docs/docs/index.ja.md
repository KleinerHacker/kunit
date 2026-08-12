# KUnit

**KUnit** は、素の数値の代わりに物理単位で計算するための Kotlin フレームワーク (Java からも利用可能)です。
メートル、マイル、平方メートルを素の `Double` 値として追跡し、すべての呼び出し箇所が単位について一致することを 祈る代わりに、
`kunit` は値とともに単位を持ち運び、変換、乗算、次元の帳簿付けをあなたの代わりに行います。

## なぜ KUnit なのか?

物理量を生の数値で扱うのはエラーが起きやすいものです: 変換せずにうっかりメートルとマイルを足したり、面積と
長さを足したりしがちです。kunit は単位を型の一部にすることでこれを解決します:

- **2つの動詞、`of` と `into`。** `number of <単位>`(`5 of meters`)で作成し、`value into <単位>`
  (`v into kilo.meters`)で読み取ります。数値と単位は厳密に分離されています。
- **型安全な算術。** 互換性のない単位グループや指数の間の `+` と `-` は、こっそり誤った数値を生成する代わりに
  `IllegalStateException` をスローします。
- **自動変換。** `(5 of meters) + (3 of miles)` はそのまま動作します — 両方のオペランドが内部で正規化されるため、
  組み合わせる前に手動で単位を変換する必要は決してありません。
- **自由な乗算と除算。** 単位の乗算や除算は *常に*許可され、結果の物理次元 (指数)を自動的に追跡します。例:
  `length * length` は面積になります。
- **完全な `Number` サポート。** `Int`、`Long`、`Float`、`Double`、その他任意の `Number` 型から値を作成できます。 すべては内部で
  `Double` に正規化されます。
- **完全な SI 接頭辞表**、Quetta (Q)から Quecto (q)まで、接頭辞ビルダー (`kilo.meters`、`milli.seconds`)
  として提供され、単位ごとの接頭辞ポリシーがコンパイル時に強制されます。
- **名前付き特殊単位**(ヘクタール、リットル、エーカーなど)を、`of`/`into` で使用する通常の値1トークンとして。

## 中核概念

kunit は2つの中心的な型を中心に構築されています:

- **`KMixedUnitInstance`** — *混合単位*(「Mischeinheit」): `Double` の基底値と1つ以上の `KUnit`(それぞれ整数の
  指数と対になる、例: 速度なら `m^1 * s^-1`)。これは他のすべてを支える汎用エンジンです。
- **`KUnit`** — 単位グループに属する単一の「純粋な」単位 (例: メートルは長さグループに属する)。具体的な単位 グループは
  `enum class ... : KUnit`(例: `KDistanceUnit`)としてモデル化されます。

各単位グループはさらに、単一の単位グループに制限された `KMixedUnitInstance` をカプセル化し、常にそのグループの 基本単位に正規化された
**ラッパークラス**(例: `KLengthUnitInstance`)を提供します。これはあなたがほとんどの場合 使う型です —
現在提供されている単位については [事前定義された単位](units/kinematics/distance.md) を、いつどのように汎用の
`KMixedUnitInstance` エンジンに直接降りるかについては [混合単位](mixed-units.md) を参照してください。

新しい物理量 (質量や時間など)のサポートを追加したい場合は、完全なステップバイステップの説明について
[カスタム単位の追加](custom-units.md) を参照してください。

!!! note "単位オブジェクトは不変です"
すべての単位値 — `KMixedUnitInstance` エンジンおよび `KLengthUnitInstance` や `KTimeUnitInstance` のような
すべての「純粋な」ラッパー — は **不変**です。どの操作も既存のインスタンスを変更しません。演算子 (`+`、`-`、
`*`、`/`)と変換は常に **新しい**オブジェクトを返し、オペランドはそのまま残します。これにより単位値を自由に
共有したり、キーや定数として使ったりしても安全になります。

## クイックスタート

モジュールを依存関係として追加し (またはプロジェクト/ソースセットとして含め)、必要な単位グループの語彙を インポートします。

### 長さ

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.*

// 任意の Number 型から `of` で純粋な長さの値を作成
val distance = 5 of meters
val trip = 10 of miles

// 演算子: 同じグループと指数の中での自動変換
val total = distance + trip          // KLengthUnitInstance、メートルに正規化
val diff = trip - distance

// 比較
val isFarther = trip > distance      // true

// `into` で特定の単位の値を読み取る
println(total into kilo.meters)      // 例: 21.0467...
println(total into yards)            // 例: 23018.4...

// 純粋な長さを掛けると強く型付けされた面積になる
val area = distance * trip           // KAreaUnitInstance

// 面積(指数2)と体積(指数3)の名前付き特殊単位
val plot = 3 of hectares
println(plot into ares)              // 300.0

val tank = 200 of liters
println(tank into usGallons)
```

### SI 接頭辞

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters

// `5 of kilo.meters` -> KLengthUnitInstance(== 5000 m)
val fiveKm = 5 of kilo.meters
println(fiveKm.value) // 5000.0(メートルに正規化)
```

### 混合単位 / 複合単位

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds

// 値1テンプレートから単位式を組み立て、`of` でスケールする
val accel = 10 of meters / (seconds pow 2)   // KMixedUnitInstance, m·s⁻²
```

## サポートされるプラットフォーム

kunit はランタイム依存関係を持たない **Kotlin マルチプラットフォーム** ライブラリです。すべての単位、演算子、
フォーマッターはコモンソースセットにあり、どのターゲットでも同一に動作します。

| ターゲット      | バリアント                                               |
|-----------------|-----------------------------------------------------------|
| JVM             | ツールチェーン 25                                          |
| JS              | ブラウザ、Node.js                                          |
| Wasm/JS         | ブラウザ、Node.js                                          |
| Native          | `linuxX64`、`mingwX64`、`macosX64`、`macosArm64`           |
| Native (iOS)    | `iosX64`、`iosArm64`、`iosSimulatorArm64`                  |

リリースは **GitHub Packages** で公開されています。リポジトリを宣言し、依存関係を追加してください:

```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/KleinerHacker/kunit")
        credentials {
            username = providers.gradleProperty("gpr.user").get()
            password = providers.gradleProperty("gpr.token").get()
        }
    }
}

dependencies {
    implementation("org.pcsoft.framework:kunit:<version>")
}
```

!!! note "GitHub Packages には認証が必要です"
GitHub Packages は、公開パッケージであっても常に認証情報を必要とします — これはプロジェクトの制約ではなく GitHub の制約です。
`read:packages` スコープを持つ個人アクセストークンを使用してください。例えば `~/.gradle/gradle.properties` の
`gpr.user`/`gpr.token` として設定します。

Gradle はモジュールメタデータを通じて正しいプラットフォームアーティファクトを解決します。**Gradle
モジュールメタデータを読み取らない Maven やその他のビルドツールは、プラットフォームアーティファクトを直接
参照する必要があります** — 公開されているアーティファクト ID は `kunit`(ルートモジュール)、`kunit-jvm`、
`kunit-js`、`kunit-wasm-js`、`kunit-linuxx64`、`kunit-mingwx64`、`kunit-macosx64`、`kunit-macosarm64`、
`kunit-iosx64`、`kunit-iosarm64`、`kunit-iossimulatorarm64` です。

いくつかの JVM 専用の便利機能 — `format`/`toString` の `java.util.Locale` オーバーロード（
[出力の書式設定](formatter/formatting.md) を参照）と、[時間単位](units/kinematics/time.md) の
`java.time.Duration` との相互運用 — は JVM ソースセットにあります。共通 API は代わりに `KLocale` と
`kotlin.time.Duration` を使用します。

## チェックアウトとビルド

```bash
git clone <repository-url>
cd kunit
```

kunit は Gradle を使用します (ラッパーはリポジトリに含まれており、ローカルの Gradle インストールは不要です):

```bash
# ビルド
./gradlew build          # Windows: gradlew.bat build

# テストのみ実行 (マルチプラットフォームの集約タスク; 単純な `test` タスクは存在しません)
./gradlew allTests        # Windows: gradlew.bat allTests
```

ツールチェーン 25 を解決できる JDK が必要です (必要に応じて `foojay-resolver` プラグインが自動的に ダウンロードします)。
