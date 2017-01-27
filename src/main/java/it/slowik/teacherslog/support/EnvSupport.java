package it.slowik.teacherslog.support;

/**
 * Created by
 * pawelslowik
 * on 27/01/17.
 */
public class EnvSupport {
    public static String getEnv(String key, String def) {
        String resolved = System.getenv(key);
        if (resolved == null) {
            return def;
        }
        return resolved;
    }
}
