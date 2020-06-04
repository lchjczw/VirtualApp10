package com.kook.tools.util;
public class RuntimeCommand {
    public static void exeCommand(String command) {
        Process p;
        int status;
        try {
            DebugKook.e("exeCommand ="+command);
            p = Runtime.getRuntime().exec(command);
            status = p.waitFor();
            if (status == 0) {
                DebugKook.d("exe command success ");
            } else {
                DebugKook.d("exe command failed ");
            }
        }catch (Exception e){
            DebugKook.printException(e);
        }
    }
    public static void chmod(String permission, String path) {
        String command = "chmod " + permission + " -R " + path;
        exeCommand(command);
    }
}
