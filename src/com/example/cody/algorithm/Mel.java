package com.example.cody.algorithm;

public class Mel {
	
	private static int NUMBER_OF_FILTERS = 20;
	private static double LOW_FREQ = 0;
	private static double HIGH_FREQ = 8000;
	
	public static double hzToMel(double f) {
		return 1125 * Math.log(1 + f / 700);
	}
	
	public static double melToHz(double f) {
		return 700 * (Math.exp(f / 1125) - 1);
	}
	
	public static double[] getMelKrepsCoef(double[] x) {
		return null;
	}
	
    public static double specterPower(Complex x) {
		return x.re() * x.re() + x.im() * x.im();
	}
    
    public static double[] specterPower(Complex[] x) {
    	double[] res = new double[x.length];
    	for (int i = 0; i < x.length; i++) {
			res[i] = Mel.specterPower(x[i]);
		}
		return res;
	}
}
