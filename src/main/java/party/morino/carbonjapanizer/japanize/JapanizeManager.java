package party.morino.carbonjapanizer.japanize;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.carbonjapanizer.CarbonJapanizer;

@DefaultQualifier(NonNull.class)
public final class JapanizeManager {

    private final Japanizer japanizer;
    private final NamespacedKey japanizeKey;

    public JapanizeManager(
            final CarbonJapanizer carbonJapanizer,
            final ComponentLogger componentLogger
    ) {
        this.japanizer = new Japanizer(componentLogger);
        this.japanizeKey = new NamespacedKey(carbonJapanizer, "japanize");
    }

    /**
     * 必要であればメッセージをかな漢字変換します。
     *
     * @param player メッセージを送信したプレイヤー
     * @param message 送信されたメッセージ
     * @return かな漢字変換が必要であれば変換したメッセージを返します。そうでなければ引数のメッセージを返します。
     */
    public String japanizeIfNeeded(Player player, String message) {
        if (japanizeEnabled(player)) {
            return this.japanizer.japanize(message);
        } else {
            return message;
        }
    }

    /**
     * このプラグインのタグがついているかどうかを確認します。
     *
     * @param player タグがついているかどうかを確認するプレイヤー。
     * @return タグがついていればtrue、ついていなければfalseを返します。
     */
    public boolean hasJapanizeTag(Player player) {
        var container = player.getPersistentDataContainer();
        return container.has(this.japanizeKey, PersistentDataType.BYTE);
    }

    /**
     * かな漢字変換の有効/無効を設定します。
     *
     * @param player かな漢字変換を設定するプレイヤー。
     * @param bool 有効であればtrue、無効であればfalseを設定します。
     */
    public void japanizeEnabled(Player player, boolean bool) {
        var container = player.getPersistentDataContainer();
        container.set(this.japanizeKey, PersistentDataType.BYTE,  bool ? (byte) 1 : (byte) 0);
    }

    /**
     * かな漢字変換の設定状態を確認します。
     *
     * @param player 設定状態を確認するプレイヤー
     * @return かな漢字変換が有効になっていればtrue、無効になっていればfalseを返します。
     */
    public boolean japanizeEnabled(Player player) {
        var container = player.getPersistentDataContainer();
        if (this.hasJapanizeTag(player)) {
            @Nullable Byte tag = container.get(japanizeKey, PersistentDataType.BYTE);
            return tag != null && tag == (byte) 1;
        } else {
            return false;
        }
    }

    /**
     * かな漢字変換の設定を切り替えます。
     *
     * @param player 設定を切り替えるプレイヤー。
     * @return 無効から有効に切り替えたらtrue、有効から無効に切り替えたらfalseを返します。
     */
    public boolean toggleJapanize(Player player) {
        var container = player.getPersistentDataContainer();
        @Nullable Byte tag = container.get(this.japanizeKey, PersistentDataType.BYTE);
        if (tag == null || tag == (byte) 0) {
            this.japanizeEnabled(player, true);
            return true;
        } else {
            this.japanizeEnabled(player, false);
            return false;
        }
    }
}
