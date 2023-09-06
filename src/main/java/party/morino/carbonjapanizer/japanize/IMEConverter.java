/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package party.morino.carbonjapanizer.japanize;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * ひらがなのみの文章を、IMEを使用して変換します。
 * 使用される変換候補は全て第1候補のため、正しくない結果が含まれることもよくあります。
 *
 * @author ucchy
 */
@DefaultQualifier(NonNull.class)
public final class IMEConverter {

    private final ComponentLogger componentLogger;

    private static final String GOOGLE_IME_URL = "https://www.google.com/transliterate?langpair=ja-Hira%7Cja&text=";

    public IMEConverter(ComponentLogger componentLogger) {
        this.componentLogger = componentLogger;
    }

    /**
     * GoogleIMEを使って変換する
     *
     * @param org 変換元
     * @return 変換後
     */
    public String convert(String org) {
        if (org.isEmpty()) {
            return "";
        }

        var httpClient = HttpClient.newBuilder().build();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(GOOGLE_IME_URL + URLEncoder.encode(org, StandardCharsets.UTF_8)))
                .GET()
                .build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() == 200) {
                String json = response.body();

                return this.parseJson(json);
            } else {
                var responseMessage = "Invalid Response: " + response.statusCode();
                this.componentLogger.warn(responseMessage);
            }
        } catch (IOException | InterruptedException e) {
            this.componentLogger.error("An error occurred while sending the HTTP request: ", e);
            Thread.currentThread().interrupt();
        }

        return "";
    }

    private String parseJson(String json) {
        StringBuilder result = new StringBuilder();
        for (JsonElement response : new Gson().fromJson(json, JsonArray.class)) {
            result.append(response.getAsJsonArray().get(1).getAsJsonArray().get(0).getAsString());
        }
        return result.toString();
    }
}
