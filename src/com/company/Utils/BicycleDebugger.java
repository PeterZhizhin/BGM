package com.company.Utils;


public class BicycleDebugger {
    private static class Log {
        private static void out(String name, String tag, String message) {
            System.out.println(name+" "+tag+": "+message);
        }
        private static void err(String name, String tag, String message) {
            System.err.println(name+" "+tag+": "+message);
        }
        public static void d(String tag, String message)  {
            out("Debug", tag,message);
        }
        public static void i(String tag, String message)  {
            out("Info", tag,message);
        }
        public static void e(String tag, String message)  {
            err("Error", tag,message);
        }
        public static void v(String tag, String message)  {
            out("Verbose", tag,message);
        }
        public static void w(String tag, String message)  {
            err("Warning", tag,message);
        }
    }

    public static final boolean ON = true;
    public static void d(String tag, String message)
    {
        if (ON)
            Log.d(tag, message);
    }
    public static void i(String tag, String message){

        if (ON)
            Log.i(tag, message);
    }
    public static void e(String tag, String message){

        if (ON)
            Log.e(tag, message);
    }
    public static void v(String tag, String message){
        if (ON)
            Log.v(tag, message);
    }
    public static void w(String tag, String message){
        if (ON)
            Log.w(tag, message);
    }
}
