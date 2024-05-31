package com.livecard.front.common.aws;

import org.apache.commons.lang3.RandomStringUtils;

public class AwsBuckets {
    private AwsBuckets() {
    }

    private static final String USER_PROFILE = "user_profile";

    public static String generateUserProfileKey(String userId) {
        String fileName = RandomStringUtils.randomAlphanumeric(20);
        return String.format(USER_PROFILE + "/%s/" + fileName, userId);
    }

    public static String generateImageKey(long id, String path) {
        return String.format(path + "/%s", id);
    }

    public static String generateImageListKey(long id, long order, String path) {
        return String.format(path + "/%s/%s", id, order);
    }
}
