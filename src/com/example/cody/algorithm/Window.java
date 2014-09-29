package com.example.cody.algorithm;

/**Class that describes various types of windows
 * @author vikont
 */
public class Window {

	/**Hamming window
	 * @param x		array to convert
	 * @return		converted array
	 */
	public static double[] hamming(double[] x) {
		int l = x.length;
		for (int i = 0; i < l; i++){
			x[i] *= 0.54 - 0.46 * Math.cos((2 * Math.PI * i)/(l - 1));
		}
		return x;
	}
	
	/**Hann window
	 * @param x		array to convert
	 * @return		converted array
	 */
	public static double[] hann(double[] x){
		int l = x.length;
		for (int i = 0; i < l; i++){
			x[i] *= 0.5 - 0.5 * Math.cos((2 * Math.PI * i)/(l - 1));
		}
		return x;	
	}
	
	public static void main(String[] args) {
		double[] x = {1, 2, 3, 4, 5};
		x = Window.hamming(x);
		System.out.println(x[0]);
		System.out.println(x[1]);
		System.out.println(x[2]);
		System.out.println(x[3]);
		System.out.println(x[4]);
	}
}
