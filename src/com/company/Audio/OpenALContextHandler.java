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
        context = ALContext.create();
    }
    public static void destroy() {
        context.getDevice().destroy();
        context.destroy();
    }
}
