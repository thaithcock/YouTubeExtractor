package com.commit451.youtubeextractor;

import android.security.NetworkSecurityPolicy;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * Remove this when this issue gets fixed
 * https://github.com/square/okhttp/issues/2533
 */
@Implements(NetworkSecurityPolicy.class)
public class NoBreakNetworkSecurityPolicy {
    @Implementation
    public static NetworkSecurityPolicy getInstance() {
        try {
            Class<?> shadow = Class.forName("android.security.NetworkSecurityPolicy");
            return (NetworkSecurityPolicy) shadow.newInstance();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Implementation
    public boolean isCleartextTrafficPermitted() {
        return true;
    }
}
