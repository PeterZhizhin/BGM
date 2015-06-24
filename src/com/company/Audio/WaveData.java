package com.company.Audio;

import java.io.BufferedInputStream;
import java.nio.ByteBuffer;

public class WaveData {
    public ByteBuffer data;
    public int format;
    public int samplerate;

    public static WaveData create(BufferedInputStream fileInputStream) {
        return new WaveData();
    }

    private WaveData() {

    }

    public void dispose() {
    }
}
