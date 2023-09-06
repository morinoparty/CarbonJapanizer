package party.morino.carbonjapanizer.config;

import net.kyori.adventure.translation.Translator;
import org.bukkit.configuration.file.FileConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.carbonjapanizer.CarbonJapanizer;

import java.util.Locale;
import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class ConfigManager {

    private final FileConfiguration fileConfig;
    private PrimaryConfig primaryConfig;

    public ConfigManager(final CarbonJapanizer carbonJapanizer) {
        carbonJapanizer.saveDefaultConfig();

        this.fileConfig = carbonJapanizer.getConfig();
        this.primaryConfig = this.primaryConfig();
    }

    /**
     * config.ymlを取得します。
     *
     * @return クラスオブジェクト化されたconfig.yml
     */
    public PrimaryConfig primaryConfig() {
        return Objects.requireNonNullElseGet(this.primaryConfig, this::reloadPrimaryconfig);
    }

    /**
     * config.ymlを再取得します。
     * @return クラスオブジェクト化されたconfig.yml
     */
    public PrimaryConfig reloadPrimaryconfig() {
        this.primaryConfig = new PrimaryConfig(
                Objects.requireNonNullElse(Translator.parseLocale(this.fileConfig.getString("default-Locale", "en_US")), Locale.US),
                this.fileConfig.getBoolean("default-japanize-enabled", true),
                this.fileConfig.getBoolean("force-japanize-enabled", false)
                // this.fileConfig.getBoolean("default-romanize", true)
        );
        return this.primaryConfig;
    }
}
