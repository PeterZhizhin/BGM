package com.company.Audio;

import com.company.Math.ComplexNumber;

public class FFT {
    private static final int magicNumber = 2;

    private static ComplexNumber[] complexNumbers = new ComplexNumber[1];
    private static ComplexNumber[] tableExp = new ComplexNumber[1];
    private static int[] indexes = new int[1];

    public static float[] fft(float[] samples)
    {
        int numberOfElem;
        if ((samples.length & (samples.length-1)) != 0)
        {
            throw new IllegalArgumentException("Sound array length must be power of 2 due to FFT algorithm");
        }
        else
        {
            numberOfElem = samples.length;

            float[] mySound = doTheWindowing(samples, numberOfElem);

            if (numberOfElem != complexNumbers.length)
            {
                complexNumbers = new ComplexNumber[numberOfElem];
                getFFTExpTable(false);
                indexes = getArrayIndexes(magicNumber);
            }
            for (int i = 0; i<complexNumbers.length; i++)
                complexNumbers[i] = new ComplexNumber(mySound[i]);

            calculateFFT();
            normalizeFFT();

            float[] amplitudes = new float[complexNumbers.length];
            for (int i=0; i<amplitudes.length;i++)
                amplitudes[i]=complexNumbers[i].re;
            return amplitudes;
        }
    }


    public static float[] fft(int[] samples)
    {
        float[] tempSamples = new float[samples.length];
        for (int i=0; i<tempSamples.length; i++)
            tempSamples[i] = (int)(samples[i]);
        return fft(tempSamples);
    }

    public static float[] ifft(float[] samples)
    {
        int numberOfElem;
        if ((samples.length & (samples.length-1)) != 0)
        {
            throw new IllegalArgumentException("Frequencies array length must be power of 2 due to IFFT algorithm");
        }
        else {
            numberOfElem = samples.length;

            if (numberOfElem != complexNumbers.length)
            {
                complexNumbers = new ComplexNumber[numberOfElem];
                getFFTExpTable(true);
                indexes = getArrayIndexes(magicNumber);
            }

            float[] mySound = samples;


            for (int i = 0; i<complexNumbers.length; i++)
                complexNumbers[i] = new ComplexNumber(mySound[i]);

            calculateFFT();
            normalizeFFT();

            float[] amplitudes = new float[complexNumbers.length];
            for (int i=0; i<amplitudes.length;i++)
                amplitudes[i]=complexNumbers[i].re;
            return amplitudes;
        }

    }

    private static float[] doTheWindowing(float[] points, int numberOfNeededPoints)
    {
        int N = numberOfNeededPoints;
        float[] arrayToReturn = new  float[N];

        //Окно Хэмминга

        /*for (int i = 0; i<N; i++)
            arrayToReturn[i] = points[i] * (float)(0.54 - 0.46*Math.cos(2 * Math.PI * i / (N-1))); */

        //Окно Блэкмена-Наттала
        double cosArgument, cosArgument2, cosArgument3;
        double a0 = 0.3635819;
        double a1 = 0.4891775;
        double a2 = 0.1365995;
        double a3 = 0.0106411;
        for (int i = 0; i<N; i++)
        {
            cosArgument = (2 * Math.PI * i / (N-1));
            cosArgument2 = cosArgument * 2;
            cosArgument3 = cosArgument * 3;
            arrayToReturn[i] = (float)(points[i] *(a0 - a1*Math.cos(cosArgument) + a2*Math.cos(cosArgument2) - a3*Math.cos(cosArgument3)));
        }
        return arrayToReturn;
    }

    private static void getFFTExpTable(boolean inverseFFT)
    {
        int CountFFTPoints = complexNumbers.length;
        tableExp = new ComplexNumber[CountFFTPoints];

        int startIndex,n,blockLength;
        ComplexNumber w, wn;
        float mnozhitel;
        if (inverseFFT)
            mnozhitel = (float)(2 * Math.PI);
        else
            mnozhitel = (float)(-2 * Math.PI);

        blockLength = 1;

        while (blockLength<CountFFTPoints)
        {
            n = blockLength;
            blockLength = blockLength << 1;

            wn = new ComplexNumber(0, mnozhitel / blockLength);
            wn = ComplexNumber.exp(wn);

            w = new ComplexNumber(1);

            startIndex = n;
            for (int i=0; i<n; i++)
            {
                tableExp[startIndex+i]=w;
                w = w.multiply(wn);
            }
        }
    }

    private static int[] getArrayIndexes(int minIteration)
    {
        int[] tempIndexes, arrayToReturn;

        int startIndex, ChIndex, NChIndex, EndIndex;
        int countPoints = complexNumbers.length;

        arrayToReturn = new int[countPoints];
        for (int i = 0; i<countPoints; i++)
            arrayToReturn[i] = i;

        int lenBlock = countPoints;
        int halfBlock = lenBlock >> 1;
        int halfBlock_1 = halfBlock - 1;

        while (halfBlock > minIteration)
        {
            startIndex = 0;
            tempIndexes = new int[countPoints];
            for (int i = 0; i<countPoints; i++)
                tempIndexes[i] = arrayToReturn[i];

            do {
                ChIndex = startIndex;
                NChIndex = ChIndex+1;
                EndIndex = startIndex + halfBlock_1;

                for (int i = startIndex; i<=EndIndex; i++)
                {
                    arrayToReturn[i] = tempIndexes[ChIndex];
                    arrayToReturn[i+halfBlock] = tempIndexes[NChIndex];

                    ChIndex = ChIndex + 2;
                    NChIndex = NChIndex + 2;
                }
                startIndex = startIndex+lenBlock;
            }
            while (startIndex<countPoints);

            lenBlock = lenBlock >> 1;
            halfBlock = lenBlock >> 1;
            halfBlock_1 = halfBlock - 1;
        }

        return arrayToReturn;
    }

    private static void makeArrayGood()
    {
        ComplexNumber[] arrayToReturn = new ComplexNumber[complexNumbers.length];
        for (int i = 0; i<indexes.length; i++)
            arrayToReturn[i] = complexNumbers[indexes[i]];
        complexNumbers = arrayToReturn;
    }

    private static void calculateFFT()
    {
        int tableExpIndex,j, i, startIndex, NChIndex;
        ComplexNumber tempBn;

        makeArrayGood();

        int length = complexNumbers.length;
        int lengthBlock;
        int halfOfBlock;

        for (lengthBlock = 2, halfOfBlock = 1; lengthBlock <= length; halfOfBlock = lengthBlock, lengthBlock = lengthBlock << 1)
        {
            tableExpIndex = halfOfBlock;

            for (j = 0; j<halfOfBlock; j++)
            {
                for (i = 0; i<length; i+=lengthBlock)
                {
                    startIndex = i+j;
                    NChIndex = startIndex+halfOfBlock;

                    tempBn = complexNumbers[NChIndex].multiply(tableExp[tableExpIndex]);
                    complexNumbers[NChIndex] = complexNumbers[startIndex].subtract(tempBn);
                    complexNumbers[startIndex] = complexNumbers[startIndex].add(tempBn);

                }
                tableExpIndex++;
            }
        }
    }

    private static void normalizeFFT()
    {
        normalizeFFT((double)2 / (double)complexNumbers.length);
    }


    private static void normalizeFFT(double mnozhitel)
    {
        int length = complexNumbers.length;
        length = length >> 1;

        float number;
        for (int i = 0; i<length; i++)
        {
            complexNumbers[i].re = (float)(ComplexNumber.abs(complexNumbers[i])*mnozhitel);
        }
    }

}
