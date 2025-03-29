package io.jonuuh.core.lib.update;

import io.jonuuh.core.lib.util.Log4JLogger;

final class Version implements Comparable<Version>
{
    private int[] parts = new int[3];

    Version(String versionStr)
    {
        if (versionStr == null || !versionStr.matches("^[0-9]+\\.[0-9]+\\.[0-9]+$"))
        {
            Log4JLogger.INSTANCE.error("Semver string '{}' is invalid", versionStr);
            parts = new int[]{-1, -1, -1};
            return;
        }

        String[] strParts = versionStr.split("\\.");

        parts[0] = Integer.parseInt(strParts[0]);
        parts[1] = Integer.parseInt(strParts[1]);
        parts[2] = Integer.parseInt(strParts[2]);
    }

    @Override
    public int compareTo(Version version)
    {
        for (int i = 0; i < parts.length; i++)
        {
            int partComparison = Integer.compare(this.parts[i], version.parts[i]);

            if (partComparison == 1 || partComparison == -1)
            {
                return partComparison;
            }
        }
        return 0;
    }

    @Override
    public String toString()
    {
        return parts[0] + "." + parts[1] + "." + parts[2];
    }
}

