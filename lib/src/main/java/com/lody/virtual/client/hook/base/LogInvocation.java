package com.lody.virtual.client.hook.base;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface LogInvocation {
    public Condition value() default Condition.ALWAYS;
    static enum Condition {
        NEVER {
            public int getLogLevel(boolean isHooked, boolean isError) {
                return -1;
            }
        },
        ALWAYS {
            public int getLogLevel(boolean isHooked, boolean isError) {
                return isError ? Log.WARN : Log.INFO;
            }
        },
        ON_ERROR {
            public int getLogLevel(boolean isHooked, boolean isError) {
                return isError ? Log.WARN : -1;
            }
        },
        NOT_HOOKED {
            public int getLogLevel(boolean isHooked, boolean isError) {
                return isHooked ? -1 : isError ? Log.WARN : Log.INFO;
            }
        };
        public abstract int getLogLevel(boolean isHooked, boolean isError);
    };
};
