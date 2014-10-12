package com.example.cody.algorithm;

public class Mel {
	
	private static int NUMBER_OF_FILTERS = 20;
	private static double LOW_FREQ = 0;
	private static double HIGH_FREQ = 8000;
    private static double[] f_smp = filterBank();

    private static double filter(int i, int k){
        if ((i > 0) && (k < f_smp[i - 1])) return 0;
        if (k < f_smp[i]) return (k - f_smp[i - 1]) / (f_smp[i] - f_smp[i - 1]);
        if ((i < NUMBER_OF_FILTERS) && (k < f_smp[i + 1])) return (f_smp[i + 1] - k) / (f_smp[i + 1] - f_smp[i]);
        return 0;
    }

	public static double hzToMel(double f) {
		return 1125 * Math.log(1 + f / 700);
	}
	
	public static double melToHz(double f) {
		return 700 * (Math.exp(f / 1125) - 1);
	}
	
	public static double[] getMelKrepsCoef(double[] x) {
		Complex[] spectrum = FFT.fft(doubleToComplex(Window.hamming(x)));
        double[] powerSpectrum = spectrumPower(spectrum);
        double[] tmp = new double[NUMBER_OF_FILTERS];
        for(int i = 0; i < NUMBER_OF_FILTERS; i++){
            tmp[i] = 0;
            for(int k = 0; k < MainHandler.FRAME_LENGTH / 2; k++){
                tmp[i] += powerSpectrum[k] * filter(i, k);
            }
            tmp[i] = Math.log(tmp[i]);
        }
        return dct(tmp); //Discrete Cosine Transform
	}

    public static double[] dct(double[] x){
        double[] ret = new double[x.length];
        for (int j = 0; j < x.length; j++){
            ret[j] = 0;
            for (int k = 0; k < NUMBER_OF_FILTERS; k++){
                ret[j] += x[k] * Math.cos((j + 1) * (k + 0.5) * Math.PI / NUMBER_OF_FILTERS);
            }
        }
        return ret;
    }

    public static Complex[] doubleToComplex(double[] x){
        Complex[] ret = new Complex[x.length];
        for (int i = 0; i < x.length; i++){
            ret[i] = new Complex(x[i], 0);
        }
        return ret;
    }
	
    public static double spectrumPower(Complex x) {
		return x.re() * x.re() + x.im() * x.im();
	}
    
    public static double[] spectrumPower(Complex[] x) {
    	double[] res = new double[x.length];
    	for (int i = 0; i < x.length; i++) {
			res[i] = Mel.spectrumPower(x[i]);
		}
		return res;
	}

    private static double[] filterBank() {
        int M = MainHandler.FRAME_LENGTH / 2;
        double melLowFreq = hzToMel(LOW_FREQ);
        double melHighFreq = hzToMel(HIGH_FREQ);
        double len = (melHighFreq - melLowFreq) / (NUMBER_OF_FILTERS + 1);

        f_smp = new double[NUMBER_OF_FILTERS];
        for (int i = 0; i < NUMBER_OF_FILTERS; i++){
            f_smp[i] = (M / MainHandler.SAMPLING_FREQ) * melToHz(melLowFreq + (i + 1) * len);
        }
        return f_smp;
    }
}
