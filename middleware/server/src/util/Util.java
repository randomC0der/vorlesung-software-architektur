package util;

public class Util {
    public static void log(Object msg) {
        System.out.print (msg + " ");

        try {
            throw new Exception();
        } catch (Exception e) {
            System.out.println(e.getStackTrace()[e.getStackTrace().length - 2]);
        }
    }
}
