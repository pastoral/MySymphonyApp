package aboutdevice.com.munir.symphony.mysymphony.utils;

import java.util.List;

/**
 * Created by munirul.hoque on 12/04/2018.
 */

public class ListTraverse {
    private ListTraverse() {
    }

    public static <T> T getFirst(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public static <T> T getLast(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }
}
