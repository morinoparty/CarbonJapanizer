package party.morino.carbonjapanizer.message;

import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import party.morino.carbonjapanizer.config.ConfigManager;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings({"UnstableApiUsage", "unused"})
@DefaultQualifier(NonNull.class)
public final class MessageResource {

    private final Path localeDirectory;
    private final Path pluginSource;
    private final ComponentLogger componentLogger;
    private final String pluginName;
    private final ConfigManager configManager;

    private @MonotonicNonNull TranslationRegistry translationRegistry;

    public MessageResource(
            final PluginProviderContext bootstrapContext,
            final ConfigManager configManager
    ) {
        this.localeDirectory = bootstrapContext.getDataDirectory().resolve("locale");
        this.pluginSource = bootstrapContext.getPluginSource();
        this.componentLogger = bootstrapContext.getLogger();
        this.pluginName = bootstrapContext.getConfiguration().getName().toLowerCase();
        this.configManager = configManager;
    }

    /**
     * 言語ファイルをリロードします
     */
    public void reload() {
        try {
            if (!Files.exists(this.localeDirectory)) {
                Files.createDirectories(this.localeDirectory);
            }

            if (this.isPathEmpty(this.localeDirectory)) {
                this.saveDefaultLocales();
            }
        } catch (IOException exception) {
            this.componentLogger.error("Failed to write default locale file", exception);
        }

        // remove any previous registry
        if (this.translationRegistry != null) {
            GlobalTranslator.translator().removeSource(this.translationRegistry);
        }

        // create a translation registry
        this.translationRegistry = TranslationRegistry.create(Key.key(this.pluginName, "main"));
        this.translationRegistry.defaultLocale(this.configManager.primaryConfig().defaultLocale());

        // load custom translations first, then the base (built-in) translations after.
        this.loadLocales();

        // register it to the global source, so our translations can be picked up by adventure-platform
        GlobalTranslator.translator().addSource(new MiniMessageTransLater(this.translationRegistry));

    }

    /**
     * すべての言語ファイルを読み込みます。
     */
    private void loadLocales() {
        try (var pathStream = Files.walk(this.localeDirectory)) {
            pathStream
                    .filter(Files::isRegularFile)
                    .filter(file -> {
                        var pathString = file.toString();
                        this.componentLogger.info(pathString);
                        return pathString.endsWith(".properties");
                    })
                    .forEach(file -> {
                        var fileName = file.getFileName();
                        @Nullable Locale locale = Translator.parseLocale(fileName.toString().substring(9, 14));
                        if (locale != null) {
                            this.translationRegistry.registerAll(locale, file, false);
                        } else {
                            this.componentLogger.warn("Failed to load locale file: " + file);
                        }
                    });
        } catch (IllegalArgumentException | IOException e) {
            this.componentLogger.warn("Failed to load locale file", e);
        }
    }

    /**
     * jarファイルに保存されている言語ファイルをプラグインディレクトリにコピーします。
     */
    private void saveDefaultLocales() throws IOException {
        this.walkPluginJar(stream -> stream
                .filter(Files::isRegularFile)
                .filter(file -> {
                    var pathString = file.toString();
                    return pathString.startsWith("/messages") && pathString.endsWith(".properties");
                })
                .forEach(file -> {
                    try {
                        this.saveLocale(file);
                    } catch (IOException e) {
                        this.componentLogger.error("Failed to load locale.", e);
                    }
                }));
    }

    /**
     * 引数に受け取ったファイルをplugins/carbonjapanizer/localeディレクトリに保存します。
     * @param resourceFile 保存するファイル
     */
    private void saveLocale(Path resourceFile) throws IOException {
        var destination = this.localeDirectory.resolve(resourceFile.toString().substring(1));
        if (!Files.exists(destination)) {
            Files.copy(resourceFile, destination);
        }
    }

    /**
     * 引数のパスが存在するかどうかを確認します。
     * @param path 確認するパス
     * @return 存在すればtrueを返します。
     */
    private boolean isPathEmpty(final Path path) {
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
            return !directory.iterator().hasNext();
        } catch (final IOException exception) {
            this.componentLogger.error("", exception);
        }

        return false;
    }

    /**
     * pluginのjarファイルをwalkします。jarファイルはFiles.walkできないため、FileSystemからiteratorを回す必要がある。
     */
    private void walkPluginJar(final Consumer<Stream<Path>> user) throws IOException {
        if (Files.isDirectory(this.pluginSource)) {
            try (final var stream = Files.walk(this.pluginSource)) {
                user.accept(stream.map(path -> path.relativize(this.pluginSource)));
            }
            return;
        }
        try (final FileSystem jar = FileSystems.newFileSystem(this.pluginSource, this.getClass().getClassLoader())) {
            final Path root = jar.getRootDirectories()
                    .iterator()
                    .next();
            try (final var stream = Files.walk(root)) {
                user.accept(stream);
            }
        }
    }
}
