package party.morino.carbonjapanizer.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.carbonjapanizer.config.ConfigManager;
import party.morino.carbonjapanizer.japanize.JapanizeManager;

import java.util.Locale;

@DefaultQualifier(NonNull.class)
public final class BukkitJoinListener implements Listener {

    private final ConfigManager configManager;
    private final JapanizeManager japanizeManager;

    public BukkitJoinListener(
            final ConfigManager configManager,
            final JapanizeManager japanizeManager
    ) {
        this.configManager = configManager;
        this.japanizeManager = japanizeManager;
    }

    /**
     * プレイヤーがサーバーにログインした時、このプラグインのタグが付いていない場合に実行します。
     * クライアント側の言語が日本語のプレイヤーはコンフィグで設定されたdefault-japanize-enabledの値を設定します。
     * それ以外の言語に設定されているプレイヤーはコンフィグで設定されたforce-japanize-enabledの値を設定します。
     *
     * @param event プレイヤーがサーバーにログインした時。
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (this.japanizeManager.hasJapanizeTag(player)) {
            return;
        }

        if (player.locale() == Locale.JAPAN) {
            var defaultJapanize = this.configManager.primaryConfig().defaultJapanizeEnabled();
            this.japanizeManager.japanizeEnabled(player, defaultJapanize);
        } else {
            var forceEnabled = this.configManager.primaryConfig().forceJapanizeEnabled();
            this.japanizeManager.japanizeEnabled(player, forceEnabled);
        }
    }
}
