package party.morino.carbonjapanizer.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Locale;

@DefaultQualifier(NonNull.class)
public record PrimaryConfig(
        Locale defaultLocale,
        boolean defaultJapanizeEnabled,
        boolean forceJapanizeEnabled
        // boolean defaultRomanize
) {
}
