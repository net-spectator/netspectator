package utils.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CastUtils {
    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> rawCollection) {
        try {
            return rawCollection.stream().map((Function<Object, T>) clazz::cast).collect(Collectors.toList());
        } catch (ClassCastException e) {
            return Collections.emptyList();
        }
    }
}
