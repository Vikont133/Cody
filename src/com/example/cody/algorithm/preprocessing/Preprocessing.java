package com.example.cody.algorithm.preprocessing;

public class Preprocessing {


    public static
    public static double[] handle(double [] x){
        return deleteSteadyComponent(x);
    }

    private static double[] deleteSteadyComponent(double[] x){
        double average = 0;
        for (double a : x){
            average += a;
        }
        average /= x.length;
        for (int i = 0; i < x.length; i++) {
            x[i] -= average;
        }
        return x;
    }

    public static void main(String[] args) {
        double[] x = {1, 2, 3, 4, 5, 6};
        x = deleteSteadyComponent(x);
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i] + " ");
        }
    }
}
