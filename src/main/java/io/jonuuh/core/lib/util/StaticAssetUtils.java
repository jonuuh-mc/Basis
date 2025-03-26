package io.jonuuh.core.lib.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public abstract class StaticAssetUtils
{
    public static String getStaticHostedAsset(String assetPath)
    {
        String asset = getGithubRepoAssetViaRawApi("jonuuh-mc", "StaticFileStorage", assetPath);

        if (asset == null)
        {
            Log4JLogger.INSTANCE.error("Failed to access static asset ({}) via github api", assetPath);
        }
        return asset;
    }

    /**
     * <pre>
     * Github api docs:
     * You can make unauthenticated requests if you are only fetching public data.
     * Unauthenticated requests are associated with the originating IP address,
     * not with the user or application that made the request.
     * The primary rate limit for unauthenticated requests is 60 requests per hour.
     * </pre>
     *
     * @param user
     * @param repo
     * @param assetPath
     * @return
     */
    private static String getGithubRepoAssetViaRawApi(String user, String repo, String assetPath)
    {
        String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", user, repo, assetPath);

        try
        {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
            httpsURLConnection.setRequestProperty("Accept", "application/vnd.github.raw+json");
            return parseHttpsURLConnectionContent(httpsURLConnection);
        }
        catch (IOException exception)
        {
            Log4JLogger.INSTANCE.error("Failed to connect to {}", url, exception);
            return null;
        }
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
            Log4JLogger.INSTANCE.error("Failed to parse content for {}", httpsURLConnection.getURL().toString(), exception);
            return null;
        }
    }
}
