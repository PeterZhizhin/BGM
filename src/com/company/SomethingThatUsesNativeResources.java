package com.company;

public abstract class SomethingThatUsesNativeResources {

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

    public void finalize() {
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (!resourcesReleased) {
            new Exception("Object was garbage-collected, but resources are nor released!").printStackTrace();
            releaseResources();
            System.exit(1);
        }
    }

    protected abstract void dispose();

}
