package io.github.winnpixie.btgui.utilities;

import java.util.Arrays;
import java.util.Collection;

public class Extensions {
    public static <T> void addAll(Collection<T> collection, T... elements) {
        collection.addAll(Arrays.asList(elements));
    }
}
