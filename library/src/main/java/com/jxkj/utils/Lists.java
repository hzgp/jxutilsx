package com.jxkj.utils;

import java.util.List;

/**
 * Created by admin on 2018/9/3.
 */

public class Lists {
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }
}
