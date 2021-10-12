package util;

public class Util {
    public static void log(Object msg) {
        System.out.print (msg + " ");

        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        System.out.println(stackTrace[stackTrace.length - 2]);
    }
}
