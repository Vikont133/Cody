package com.example.cody.algorithm.processing;

import com.example.cody.algorithm.Complex;

public class FFT {

    /**Compute the FFT of x[], assuming its length is a power of 2
     * @param x		array of Complex values ​​of a discrete function
     * @return		converted array
     */
    public static Complex[] fft(Complex[] x) {
        int N = x.length;
        
        if (N == 1) return new Complex[] { x[0] };

        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }

        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        Complex[] odd  = even;
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }


    /**Compute the inverse FFT of x[], assuming its length is a power of 2
     * @param x		array of Complex values ​​of a discrete function
     * @return		converted array
     */
    public static Complex[] ifft(Complex[] x) {
        int N = x.length;
        Complex[] y = new Complex[N];

        for (int i = 0; i < N; i++) {
            y[i] = x[i].conjugate();
        }

        y = fft(y);

        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        for (int i = 0; i < N; i++) {
            y[i] = y[i].times(1.0 / N);
        }

        return y;

    }

    /**Compute the circular convolution of x and y
     * @param x		array of complex values ​​of a discrete function
     * @param y		array of complex values ​​of a discrete function
     * @return		result of the convolution
     */
    public static Complex[] cconvolve(Complex[] x, Complex[] y) {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.length != y.length) { throw new RuntimeException("Dimensions don't agree"); }

        int N = x.length;

        // compute FFT of each sequence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply
        Complex[] c = new Complex[N];
        for (int i = 0; i < N; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT
        return ifft(c);
    }


    /**Compute the linear convolution of x and y
     * @param x		array of complex values ​​of a discrete function
     * @param y		array of complex values ​​of a discrete function
     * @return		result of the convolution
     */
    public static Complex[] convolve(Complex[] x, Complex[] y) {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2*x.length];
        for (int i = 0;        i <   x.length; i++) a[i] = x[i];
        for (int i = x.length; i < 2*x.length; i++) a[i] = ZERO;

        Complex[] b = new Complex[2*y.length];
        for (int i = 0;        i <   y.length; i++) b[i] = y[i];
        for (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;

        return cconvolve(a, b);
    }

    /**Display an array of Complex numbers to standard output
     * @param x			array of Complex numbers
     * @param title
     */
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }

}