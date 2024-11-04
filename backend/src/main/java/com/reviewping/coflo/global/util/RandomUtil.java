package com.reviewping.coflo.global.util;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class RandomUtil {
    // TODO: 운영시에 percent 바꾸기
    private static final int percent = 100;
    private static final Random random = new Random();

    public boolean random() {
        int value = random.nextInt(100);
        return value < percent;
    }
}
