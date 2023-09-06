package party.morino.carbonjapanizer.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class Messages {

    private Messages() {
        throw new IllegalStateException("Utility class");
    }

    public static TranslatableComponent configReloadSuccess() {
        return Component.translatable("config.reload.success");
    }

    public static TranslatableComponent japanizeToggleEnabled() {
        return Component.translatable("japanize.toggle.enabled");
    }

    public static TranslatableComponent japanizeToggleDisabled() {
        return Component.translatable("japanize.toggle.disabled");
    }

    public static TranslatableComponent playerSpecificCommand() {
        return Component.translatable("player.specific.command");
    }
}
