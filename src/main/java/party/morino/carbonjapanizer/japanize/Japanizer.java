/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2014
 */
package party.morino.carbonjapanizer.japanize;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * ローマ字表記を漢字変換して返すユーティリティ
 *
 * @author ucchy
 */
@DefaultQualifier(NonNull.class)
public final class Japanizer {

    private final IMEConverter imeConverter;

    public Japanizer(ComponentLogger componentLogger) {
        this.imeConverter = new IMEConverter(componentLogger);
    }

    public String japanize(String message) {

        // 変換不要ならパラメータをそのまま返す
        if (!this.isNeedToJapanize(message)) {
            return message;
        }

        // カナ変換
        String japanized = YukiKanaConverter.convert(message);

        // IME変換
        japanized = this.imeConverter.convert(japanized);

        // 返す
        return japanized.trim();
    }

    /**
     * 日本語化が必要かどうかを判定する
     */
    private boolean isNeedToJapanize(String org) {
        return (org.getBytes().length == org.length()
                && !org.matches("[ \\uFF61-\\uFF9F]+"));
    }
}
