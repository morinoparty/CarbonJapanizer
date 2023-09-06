package party.morino.carbonjapanizer.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;
import party.morino.carbonjapanizer.japanize.JapanizeManager;
import party.morino.carbonjapanizer.message.Messages;

import java.util.List;

@DefaultQualifier(NonNull.class)
public final class ToggleCommand extends BukkitCommand {

    private final JapanizeManager japanizeManager;

    public ToggleCommand(final JapanizeManager japanizeManager) {
        super("japanize", "Toggles Romaji to Japanese conversion enable/disable", "/japanize", List.of("jp"));
        this.japanizeManager = japanizeManager;
        this.setPermission("carbonjapanizer.command.toggle");
    }

    /**
     * チャットのかな漢字変換の有効と無効を切り替えるコマンド
     *
     * @param sender Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args All arguments passed to the command, split via ' '
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            var japanizeEnabled = this.japanizeManager.toggleJapanize(player);
            player.sendMessage(japanizeEnabled
                    ? Messages.japanizeToggleEnabled()
                    : Messages.japanizeToggleDisabled()
            );
            return true;
        } else {
            sender.sendMessage(Messages.playerSpecificCommand());
            return false;
        }
    }
}
