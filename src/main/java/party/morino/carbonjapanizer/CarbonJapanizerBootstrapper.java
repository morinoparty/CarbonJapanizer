package party.morino.carbonjapanizer;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@SuppressWarnings({"UnstableApiUsage", "unused"})
@DefaultQualifier(NonNull.class)
public final class CarbonJapanizerBootstrapper implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        // 特に何もしない
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new CarbonJapanizer(context);
    }
}
