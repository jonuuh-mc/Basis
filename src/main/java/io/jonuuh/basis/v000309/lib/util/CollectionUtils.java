package io.jonuuh.basis.v000309.lib.util;

import java.util.Comparator;
import java.util.List;

public final class CollectionUtils
{
    /**
     * Retrieve the maximum element in the given list, according to the given comparator.
     * <p>
     * In case of multiple elements being the maximum, the last such maximum element is returned.
     *
     * @param list A list.
     * @param comparator A comparator used to compare the elements of the list.
     * @param <T> The type of elements in the list.
     * @return The last maximum element in the list.
     */
    public static <T> T getMax(List<T> list, Comparator<T> comparator)
    {
        if (list == null || list.isEmpty() || comparator == null)
        {
            return null;
        }

        T max = list.get(0);
        for (T item : list)
        {
            if (comparator.compare(item, max) >= 0)
            {
                max = item;
            }
        }
        return max;
    }

    /**
     * Retrieve the minimum element in the given list, according to the given comparator.
     * <p>
     * In case of multiple elements being the minimum, the last such minimum element is returned.
     *
     * @param list A list.
     * @param comparator A comparator used to compare the elements of the list.
     * @param <T> The type of elements in the list.
     * @return The last minimum element in the list.
     */
    public static <T> T getMin(List<T> list, Comparator<T> comparator)
    {
        if (list == null || list.isEmpty() || comparator == null)
        {
            return null;
        }

        T min = list.get(0);
        for (T item : list)
        {
            if (comparator.compare(item, min) <= 0)
            {
                min = item;
            }
        }
        return min;
    }
}
