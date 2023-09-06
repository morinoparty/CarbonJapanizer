package party.morino.carbonjapanizer.message;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.text.MessageFormat;
import java.util.Locale;

// 言語ファイルでMiniMessageを使うためのクラス
// 参考: https://github.com/TheMeinerLP/MiniMessageServerSideTranslations
@DefaultQualifier(NonNull.class)
public final class MiniMessageTransLater implements Translator {
    private final TranslationRegistry registry;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public MiniMessageTransLater(TranslationRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Key name() {
        return this.registry.name();
    }

    @Override
    public MessageFormat translate(String key, Locale locale) {
        return this.registry.translate(key, locale);
    }

    @Override
    public @Nullable Component translate(TranslatableComponent component, Locale locale) {
        @Nullable MessageFormat translated = this.translate(component.key(), locale);
        if (translated == null) {
            return null;
        }

        var values = serializeComponentArgs(component);
        var resultComponent = deserializeMiniMessage(translated, values);
        return this.renderComponent(resultComponent, locale);
    }

    private String[] serializeComponentArgs(TranslatableComponent component) {
        return component.args().stream()
                .map(this.miniMessage::serialize)
                .toArray(String[]::new);
    }

    private Component deserializeMiniMessage(MessageFormat translated, String[] values) {
        var formattedMessage = translated.format(values);
        return this.miniMessage.deserialize(formattedMessage);
    }

    private Component renderComponent(Component component, Locale locale) {
        var renderedComponent = GlobalTranslator.render(component, locale);
        var renderedChildren = component.children().stream()
                .map(child -> GlobalTranslator.render(child, locale))
                .toList();
        return renderedComponent.children(renderedChildren);
    }
}
