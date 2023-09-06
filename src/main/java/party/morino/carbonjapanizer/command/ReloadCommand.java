package party.morino.carbonjapanizer.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;
import party.morino.carbonjapanizer.config.ConfigManager;
import party.morino.carbonjapanizer.message.MessageResource;
import party.morino.carbonjapanizer.message.Messages;

import java.util.List;

@DefaultQualifier(NonNull.class)
public final class ReloadCommand extends BukkitCommand {

    private final ConfigManager configManager;
    private final MessageResource messageResource;

    public ReloadCommand(
            final ConfigManager configManager,
            final MessageResource messageResource
    ) {
        super("carbonjapanizer", "Reload the config and message files", "/carbonjapanizer reload", List.of("cj"));
        this.configManager = configManager;
        this.messageResource = messageResource;
        this.setPermission("carbonjapanizer.command.reload");
    }

    /**
     * config.ymlとmessages_<locale>.propertiesを再取得します。
     *
     * @param sender Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args All arguments passed to the command, split via ' '
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        this.configManager.reloadPrimaryconfig();
        this.messageResource.reload();
        sender.sendMessage(Messages.configReloadSuccess());
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return List.of("reload");
        } else {
            return List.of();
        }
    }
}
