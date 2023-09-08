package party.morino.carbonjapanizer.listener;

import net.draycia.carbon.api.CarbonChatProvider;
import net.draycia.carbon.common.event.events.CarbonEarlyChatEvent;
import net.draycia.carbon.paper.users.CarbonPlayerPaper;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.carbonjapanizer.japanize.JapanizeManager;

@DefaultQualifier(NonNull.class)
public final class CarbonChatListener {

    private final JapanizeManager japanizeManager;

    private boolean registered = false;

    public CarbonChatListener(final JapanizeManager japanizeManager) {
        this.japanizeManager = japanizeManager;
    }

    /**
     * カーボンチャットのイベントを登録します。
     */
    public void registerEvent() {
        // 何度も登録されないように
        if (!this.registered) {
            this.onChat();
            this.registered = true;
        }
    }

    /**
     * プレイヤーがチャットを送信した時、必要であればかな漢字変換を実行します
     */
    private void onChat() {
        CarbonChatProvider.carbonChat().eventHandler().subscribe(CarbonEarlyChatEvent.class, event -> {
            if (event.sender() instanceof CarbonPlayerPaper paperPlayer) {
                @Nullable Player player = paperPlayer.bukkitPlayer();
                if (player != null) {
                    var japanizedMessage = this.japanizeManager.japanizeIfNeeded(player, event.message());
                    event.message(japanizedMessage);
                }
            }
        });
    }
}
