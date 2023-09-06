package party.morino.carbonjapanizer;

import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.carbonjapanizer.command.ReloadCommand;
import party.morino.carbonjapanizer.command.ToggleCommand;
import party.morino.carbonjapanizer.config.ConfigManager;
import party.morino.carbonjapanizer.japanize.JapanizeManager;
import party.morino.carbonjapanizer.listener.BukkitJoinListener;
import party.morino.carbonjapanizer.listener.CarbonChatListener;
import party.morino.carbonjapanizer.message.MessageResource;

import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "unused"})
@DefaultQualifier(NonNull.class)
public final class CarbonJapanizer extends JavaPlugin {

    private final PluginProviderContext pluginContext;
    private final ConfigManager configManager;
    private final MessageResource messageResource;
    private final JapanizeManager japanizeManager;

    private final List<Command> commands;

    public CarbonJapanizer(final PluginProviderContext pluginContext) {
        this.pluginContext = pluginContext;
        this.configManager = new ConfigManager(this);
        this.messageResource = new MessageResource(this.pluginContext, this.configManager);
        this.japanizeManager = new JapanizeManager(this, pluginContext.getLogger());

        this.commands = this.commands();
    }

    @Override
    public void onLoad() {
        this.configManager.reloadPrimaryconfig();
        this.messageResource.reload();
    }

    @Override
    public void onEnable() {

        // Commands
        var fallbackPrefix = this.pluginContext.getConfiguration().getName();
        this.commands.forEach(command ->
                this.getServer().getCommandMap().registerAll(fallbackPrefix, commands));

        // Bukkit Listeners
        var joinListener = new BukkitJoinListener(this.configManager, this.japanizeManager);
        this.getServer().getPluginManager().registerEvents(joinListener, this);

        // CarbonChat Listener
        var carbonListener = new CarbonChatListener(this.japanizeManager);
        carbonListener.registerEvent();
    }

    private List<Command> commands() {
        return List.of(
                new ReloadCommand(this.configManager, this.messageResource),
                new ToggleCommand(this.japanizeManager)
        );
    }
}
