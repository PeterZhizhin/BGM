package com.company;

public abstract class SomethingThatUsesNativeResources {

    private StackTraceElement[] constructorStackTrace;

    public SomethingThatUsesNativeResources() {
        constructorStackTrace=Thread.currentThread().getStackTrace();
    }

    private boolean resourcesReleased=false;

    public void releaseResources() {
        if (resourcesReleased) {
            new Exception("Resources are already released").printStackTrace();
            System.exit(1);
        } else {
            dispose();
            resourcesReleased=true;
        }
    }

    /*public void finalize() {
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (!resourcesReleased) {

            System.err.println("Not-cleaned object stat:\n" +
                "   type: "+this.getClass()+"\n" +
                "   objectid: "+this+"\n" +
                "   hash: "+this.hashCode()
            );
            Exception e=new Exception("Creation stacktrace:");
            e.setStackTrace(constructorStackTrace);
            e.printStackTrace();

            new Exception("Object was garbage-collected, but resources are nor released!").printStackTrace();

            releaseResources();
            System.exit(1);
        }
    }*/

    protected abstract void dispose();

}
