package com.company.Math;

/**
 * Класс для работы с комплексными числами. КАКОГО ЧЁРТА ТАКОГО ТИПА НЕТ В Math?!           s
 * Class for complex numbers
 */
public class ComplexNumber {
    //Действительная часть
    //Real part
    public float re;

    //Комплексная часть
    //Imagine part
    public float im;

    /**
     * Конструктор комплексного числа из действительной и комплексной части
     * Constructor from real and imagine part
     * @param re Действительная часть. Real part.
     * @param im Комплексная часть. Imagine part.
     */
    public ComplexNumber(float re, float im)
    {
        this.re = re;
        this.im = im;
    }

    /**
     * Конструктор комплексного числа из действительного числа.
     * Constructor from real number.
     * @param re Действительное число. Real number
     */
    public ComplexNumber(float re)
    {
        this.re = re;
        this.im = 0;
    }

    /**
     * Конструктор нулевого комплексного числа
     * Constructor of zero number
     */
    public ComplexNumber()
    {
        this.re = 0;
        this.im = 0;
    }

    /**
     * ВОТ ЧЁРТ! В Java нельзя перегрузать стандартые операторы!
     * Добавить к данному числу ещё одно комплексное
     * HOLY SHIT! It is forbidden to override basic operations in Java
     * Add to this number another one
     * @param whatToAdd Число для добавления. Number for adding.
     * @return Результат сложения. Result.
     */
    public ComplexNumber add(ComplexNumber whatToAdd)
    {
        return new ComplexNumber(this.re + whatToAdd.re, this.im + whatToAdd.im);
    }

    /**
     * Вычитание. Как и сложение.
     * Subtraction. Same as adding.
     * @param whatToSubtract Что вычитать. What to subtract.
     * @return Результат. Result.
     */
    public ComplexNumber subtract(ComplexNumber whatToSubtract)
    {
        return new ComplexNumber(this.re - whatToSubtract.re, this.im - whatToSubtract.im);
    }

    /**
     * Умножение. Как и всё остальное.
     * Multiplying. The same.
     * @param whatToMultiply Точно так же. The same.
     * @return Результат. Result.
     */
    public ComplexNumber multiply(ComplexNumber whatToMultiply)
    {
        ComplexNumber temp = new ComplexNumber();
        temp.re = this.re * whatToMultiply.re - this.im * whatToMultiply.im;
        temp.im = this.re * whatToMultiply.im + this.im * whatToMultiply.re;
        return temp;
    }

    public ComplexNumber divide(ComplexNumber whatToDivide)
    {
        ComplexNumber temp = new ComplexNumber();
        temp.re = this.re / whatToDivide.re - this.im / whatToDivide.im;
        temp.im = this.re / whatToDivide.im + this.im / whatToDivide.re;
        return temp;
    }

    /**
     * Получаем значение экспоненты в комплексной степени. ГРЁБАНЫЙ Math!!!!
     * Value of Math.exp() of complexNumber. HOLY Math!!!
     * @param pok Показатель степени.
     * @return Результат. Result.
     */
    public static ComplexNumber exp(ComplexNumber pok)
    {
        double module = Math.exp(pok.re);
        return new ComplexNumber((float)(module*Math.cos(pok.im)), (float)(module*Math.sin(pok.im)));
    }

    /**
     * Модуль комплексного числа.
     * Absolute value of complex number
     * @param input Входящее комлексное число. Complex number for calculating abs value.
     * @return Результат. Result.
     */
    public static float abs(ComplexNumber input)
    {
        return (float)Math.hypot(input.re, input.im);
    }

}
