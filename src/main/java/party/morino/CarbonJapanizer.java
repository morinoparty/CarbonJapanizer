package party.morino;

import party.morino.japanize.Japanizer;
import net.draycia.carbon.api.CarbonChatProvider;
import net.draycia.carbon.api.events.CarbonChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;

public class CarbonJapanizer extends JavaPlugin {

    @Override
    public void onEnable() {
        CarbonChatProvider.carbonChat().eventHandler().subscribe(CarbonChatEvent.class, event -> {

            var message = event.message();
            var plainMessage = PlainTextComponentSerializer.plainText().serialize(message);
            var japanizedMessage = Japanizer.japanize(plainMessage);

            event.message(Component.text(japanizedMessage));
        });
    }
}
