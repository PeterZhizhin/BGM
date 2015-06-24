package com.company.Audio;

import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;

import java.util.List;

public class OpenALContextHandler {
    private static ALContext context;
    public static void create() {
        ALDevice device = ALDevice.create(null);

        ALCCapabilities caps = device.getCapabilities();
        System.out.println("OpenALC10: " + caps.OpenALC10);
        System.out.println("OpenALC11: " + caps.OpenALC11);
        System.out.println("caps.ALC_EXT_EFX = " + caps.ALC_EXT_EFX);

        if ( caps.OpenALC11 ) {
            List<String> devices = ALC.getStringList(0L, ALC_ALL_DEVICES_SPECIFIER);
            for ( int i = 0; i < devices.size(); i++ ) {
                System.out.println(i + ": " + devices.get(i));
            }
        }

        String defaultDeviceSpecifier = alcGetString(0L, ALC_DEFAULT_DEVICE_SPECIFIER);

        System.out.println("Default device: " + defaultDeviceSpecifier);

        context = ALContext.create();

        System.out.println("ALC_FREQUENCY: " + alcGetInteger(device.getPointer(), ALC_FREQUENCY) + "Hz");
        System.out.println("ALC_REFRESH: " + alcGetInteger(device.getPointer(), ALC_REFRESH) + "Hz");
        System.out.println("ALC_SYNC: " + (alcGetInteger(device.getPointer(), ALC_SYNC) == ALC_TRUE));
        System.out.println("ALC_MONO_SOURCES: " + alcGetInteger(device.getPointer(), ALC_MONO_SOURCES));
        System.out.println("ALC_STEREO_SOURCES: " + alcGetInteger(device.getPointer(), ALC_STEREO_SOURCES));

        device.destroy();
    }
    public static void destroy() {
        context.destroy();
    }
}
