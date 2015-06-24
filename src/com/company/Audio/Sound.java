package com.company.Audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SEC_OFFSET;
import static org.lwjgl.openal.AL11.alGetBufferi;

import it.sauronsoftware.jave.*;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import java.io.*;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Sound {
    private int soundSource;
    private int soundBuffer;

    private float duration;

    /**
     *
     * @return Продолжительность звука в секундах. Sound duration in seconds.
     */
    public float getDuration()
    {
        return duration;
    }

    private int sampleRate;
    public int getSampleRate()
    {
        return sampleRate;
    }

    private boolean isMono;
    private boolean is8Bit;

    /**
     * Конструктор. Вызывается методом getSound()
     * Constructor. It will be called by getSound() method.
     * @param soundSource Источник звука, созданный OpenAL. Sound source which was created by OpenAL.
     * @param sampleRate Сэплрейт (кол-во сэмплов в секунду). Samplerate (number of samples in one second)
     * @param format Звуковой формат в OpenAL. Sound format like it OpenAL.
     * @param soundBuffer Звуковой буфер, созданый OpenAL. Sound buffer which was created by OpenAL.
     * @param soundData Звуковые данные. Будет передан null если contain==false. Sound data. It will be null if contain==false
     */
    private Sound(int soundSource, int sampleRate, int format, int soundBuffer, ByteBuffer soundData)
    {
        this.soundSource = soundSource;
        this.sampleRate = sampleRate;

        //Парсинг звукового формата на предмет моно/стерео 8/16 бит.
        //Parsing sound format for mono/stereo 8/16 bit information.
        switch (format)
        {
            case AL_FORMAT_MONO16:
                isMono = true;
                is8Bit = false;
                break;
            case AL_FORMAT_STEREO16:
                isMono = false;
                is8Bit = false;
                break;
            case AL_FORMAT_MONO8:
                isMono = true;
                is8Bit = true;
                break;
            case AL_FORMAT_STEREO8:
                isMono = false;
                is8Bit = true;
                break;
            default:
                isMono = false;
                is8Bit = false;
                break;
        }

        //Если класс должен содержать звуковые данные - он будет их содержать.
        //If class should contain data we will contain it.
        if (contain)
            waveData = soundData;

        this.soundBuffer = soundBuffer;

        //Вычисление продолжительности в секундах
        //Get duration in seconds
        this.duration = getDurationFromBuffer();
    }

    /**
     * Получить продолжительность по информации о буфере
     * Get duration by information in buffer
     * @return Продолжительность звука в секундах. Sound duration in seconds.
     */
    private float getDurationFromBuffer()
    {
        int sizeInBytes, channels, bits,frequency;
        sizeInBytes = alGetBufferi(soundBuffer, AL_SIZE);
        channels = alGetBufferi(soundBuffer, AL_CHANNELS);
        bits = alGetBufferi(soundBuffer, AL_BITS);
        int lengthInSamples = sizeInBytes * 8 / (channels * bits);
        frequency = AL10.alGetBufferi(soundBuffer, AL_FREQUENCY);
        return  (float)lengthInSamples / (float)frequency;
    }

    /**
     * Очистка нативной памяти
     * Clearing of native memory
     */
    public void dispose()
    {
        alDeleteBuffers(soundBuffer);
        alDeleteSources(soundSource);
    }
    public boolean contain;
    public ByteBuffer waveData;

    //
    public int[][] getNextSamples(int samplesCount)
    {
        return getNextSamples((int)getCurrentSampleCount(), samplesCount);
    }

    /**
     * Получение массива звуковых данных для правого и левого канала отдельно.
     * Get sound data for left and right channels separately
     * @param samplesCount Количество сэмплов для обработки. Samples count for returning.
     *        from Откуда нужно считать сэмплы.
     * @return Данные о следующих сэплах. Первый массив [0] - левый канал. Второй [1] - правый. | Data about next samples. First array [0] - left channel. Second [1] - правый.
     *         null - Если класс данные не хранит. null - if there is no data
     *         Массив нулей - если достигли конца. Array with zeros if we got to the end of array.
     */
    public int[][] getNextSamples(int from, int samplesCount)
    {
        //Получаем данные только если они у нас есть.
        //Get data only if we have it.
        if (!contain)
            return null;

        int[][] arrayToReturn;
        if (isMono)
            arrayToReturn = new int[1][samplesCount];
        else
            arrayToReturn = new int[2][samplesCount];

        try
        {
            if (isMono)
            {
                if (is8Bit)
                {
                    waveData.position(from);
                    for (int i = 0; i<samplesCount; i++)
                        arrayToReturn[0][i] = waveData.get();
                }
                else
                {
                    waveData.position(from*2);
                    for (int i = 0; i<samplesCount; i++)
                        arrayToReturn[0][i] = waveData.getShort();
                }
            }
            else
            {
                if (is8Bit)
                {
                    waveData.position(from * 2);
                    for (int i = 0; i<samplesCount; i++)
                    {
                        arrayToReturn[0][i] = waveData.get();
                        arrayToReturn[1][i] = waveData.get();
                    }
                }
                else
                {
                    waveData.position(from * 4);
                    for (int i = 0; i<samplesCount; i++)
                    {
                        //System.out.println(i);
                        arrayToReturn[0][i] = waveData.getShort();
                        arrayToReturn[1][i] = waveData.getShort();
                    }
                }
            }
        }
        //Если достигли конца буффера и получили об этом исключение - возвращаем нули.
        //If we get any info from end - return zeros.
        catch (BufferUnderflowException e)
        {
            for (int i = 0; i<samplesCount; i++)
                arrayToReturn[0][i]=0;
            if (!isMono)
                for (int i = 0; i<samplesCount; i++)
                    arrayToReturn[1][i]=0;
        }

        return arrayToReturn;

    }

    /**
     * Вместо сэмплов можно использовать и время.
     * Смотри public int[][] getNextSamples(int samplesCount)
     *
     * You can get info about data for time.
     * See public int[][] getNextSamples(int samplesCount)
     * @param seconds
     * @return
     */
    public int[][] getNextSamples(float seconds)
    {
        int samplesCount = (int)(seconds * sampleRate);
        return getNextSamples(samplesCount);
    }


    public void setVolume(float volume)
    {
        alSourcef(soundSource, AL_VELOCITY, volume);
    }
    public void setPosition(float timePosition)
    {
        alSourcef(soundSource, AL_SEC_OFFSET, timePosition);
    }
    public float getPlayingTime()
    {
        return alGetSourcef(soundSource, AL_SEC_OFFSET);
    }
    public void playSound()
    {
        alSourcePlay(soundSource);
    }
    public void pauseSound()
    {
        alSourcePause(soundSource);
    }
    public void stopSound()
    {
        alSourceStop(soundSource);
    }
    public float getCurrentSampleCount()
    {
        return alGetSourcef(soundSource, AL11.AL_SAMPLE_OFFSET);
    }

    public static Sound getSoundUsingJAVE(String path) throws FileNotFoundException {
        File source = new File(path);
        String targetName = "tempWave.s16le";
        File target = new File(targetName);
        AudioAttributes audio = new AudioAttributes();
        audio.setSamplingRate(44100);
        audio.setChannels(2);

        EncodingAttributes attributes = new EncodingAttributes();
        attributes.setFormat("s16le");
        attributes.setAudioAttributes(audio);

        Encoder encoder = new Encoder();
        try {
            encoder.encode(source, target, attributes);
        } catch (EncoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Sound returnSound = getSound(targetName);

        target.delete();

        return returnSound;

    }
    public static byte[] readBytes(String name) {
        ByteArrayOutputStream ous;
        InputStream ios = null;
        try {
            ios = new FileInputStream(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] buffer = new byte[4096];
        ous = new ByteArrayOutputStream();
        int read;
        try {
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ios.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ous.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ous.toByteArray();
    }

    private static final int samplerate = 44100;
    private static final int format = AL_FORMAT_STEREO16;

    public static Sound getSound(String targetName) {
        byte[] bytes = readBytes(targetName);
        ByteBuffer data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder());
        data.put(bytes);
        int buffer = alGenBuffers();
        alBufferData(buffer, format, data, samplerate);
        int source = alGenSources();
        alSourcei(source, AL_BUFFER, buffer);
            return new Sound(source, samplerate, format, buffer, data);
    }

}
