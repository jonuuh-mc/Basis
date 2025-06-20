package io.jonuuh.core.lib.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public final class StaticAssetUtils
{
    /** Prevents instantiation */
    private StaticAssetUtils()
    {
    }

    /**
     * Attempt to parse a json string as a Gson JsonObject.
     *
     * @param jsonStr The json string
     * @return The JsonObject, if parseable, else null
     */
    public static JsonObject parseJson(String jsonStr)
    {
        JsonElement jsonElement = new JsonParser().parse(jsonStr);
        return jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
    }

    /**
     * Attempt to parse a top level member from a JsonObject as a string.
     *
     * @param jsonObject The JsonObject
     * @return The member as a string if it exists in top level and is a primitive, else null
     */
    public static String parseMemberAsString(JsonObject jsonObject, String member)
    {
        JsonElement element = jsonObject.get(member);
        JsonPrimitive primitive = element != null && element.isJsonPrimitive() ? element.getAsJsonPrimitive() : null;
        String versionStr = primitive != null ? primitive.getAsString() : null;

        if (versionStr == null)
        {
            System.out.printf("Failed to parse %s from JsonObject (%s)%n", member, jsonObject.toString());
        }
        return versionStr;
    }

    /**
     * Fetches an asset with {@link StaticAssetUtils#getStaticHostedRawAsset(String)},
     * then attempts to parse it as a JsonObject.
     *
     * @param assetPath The path to the asset, from the root of the repo directory
     * @return The raw asset content, parsed as a JsonObject
     */
    public static JsonObject getStaticHostedAssetAsJsonObject(String assetPath)
    {
        JsonObject jsonObject = parseJson(getStaticHostedRawAsset(assetPath));

        if (jsonObject == null)
        {
            System.out.printf("Failed to parse static asset (%s) as json object%n", assetPath);
        }

        return jsonObject;
    }

    /**
     * Fetch an asset hosted on the jonuuh-mc StaticAssetStorage GitHub repo.
     * <p>
     * Uses the GitHub repos api, response should only be the raw un-encoded content without any metadata
     * <p>
     * GitHub api docs:
     * <pre>
     * You can make unauthenticated requests if you are only fetching public data.
     * Unauthenticated requests are associated with the originating IP address ...
     * The primary rate limit for unauthenticated requests is 60 requests per hour.
     * </pre>
     *
     * @param assetPath The path to the asset, from the root of the repo directory
     * @return The raw asset content
     */
    public static String getStaticHostedRawAsset(String assetPath)
    {
        String url = String.format("https://api.github.com/repos/jonuuh-mc/StaticAssetStorage/contents/%s", assetPath);
        String asset = null;

        try
        {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
            httpsURLConnection.setRequestProperty("Accept", "application/vnd.github.raw+json");
            asset = parseHttpsURLConnectionContent(httpsURLConnection);
        }
        catch (IOException exception)
        {
            System.out.printf("Failed to connect to %s: %s%n", url, exception.getMessage());
        }

        if (asset == null)
        {
            System.out.printf("Failed to access static asset (%s) via github api%n", assetPath);
        }
        return asset;
    }

    private static String parseHttpsURLConnectionContent(HttpsURLConnection httpsURLConnection)
    {
        try
        {
            InputStream inputStream = httpsURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String urlContent = scanner.next();

            scanner.close();
            inputStream.close();
            httpsURLConnection.disconnect();

            return urlContent;
        }
        catch (IOException exception)
        {
            System.out.printf("Failed to parse content for %s: %s%n", httpsURLConnection.getURL().toString(), exception.getMessage());
            return null;
        }
    }
}
