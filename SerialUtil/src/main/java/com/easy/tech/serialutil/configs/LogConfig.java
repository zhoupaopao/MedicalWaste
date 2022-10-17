package com.easy.tech.serialutil.configs;

import com.easy.tech.serialutil.BuildConfig;
import com.easy.tech.serialutil.engine.SerialEngine;
import com.easy.tech.serialutil.assist.SystemPropertiesUtil;

public class LogConfig {
    public static boolean DEBUG = BuildConfig.DEBUG || SystemPropertiesUtil.getProperty("persist.sys.unis.app.debug", "false").equalsIgnoreCase("true");

    private void test() {
        SerialEngine.Status.valueOf("UNINITIALIZED");
    }


}
