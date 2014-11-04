package com.example.cody.algorithm;


import com.example.cody.VoiceHandler;
import com.example.cody.algorithm.preprocessing.Preprocessing;
import com.example.cody.algorithm.processing.Mel;

import java.util.Arrays;

public class MainHandler implements VoiceHandler{

	public static final int FRAME_LENGTH = 2048;
    public static final int SAMPLING_FREQ = 16000;
	
	@Override
	public void addUser(String name, short[] audioBuffer) {

	}

	@Override
	public void removeUser(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkVoice(short[] audioBuffer) {
		// TODO Auto-generated method stub
		
	}

    public static double[] getMelKreps(short[] audioBuffer){
        double [] tmp = Preprocessing.handle(normalize(audioBuffer));

        double [][] audioFrames = divToFrames(tmp);
        return Mel.getMelKrepsCoef(audioFrames);
    }
	
	private static double[] normalize(short[] audioBuffer) {
		short max = findMax(audioBuffer);
		double[] newBuffer = new double[audioBuffer.length];
		for(int i = 0; i < audioBuffer.length; i++){
			newBuffer[i] = (double)audioBuffer[i] / max;
		}
		return newBuffer;
	}
	
	private static short findMax(short[] audioBuffer) {
		short max = (short)Math.abs(audioBuffer[0]);
		for (int i = 1; i < audioBuffer.length; i++){
			if (Math.abs(audioBuffer[i]) > max){
				max = audioBuffer[i];
			}
		}
		return max;
	}

	private static double[][] divToFrames(double[] buffer) {
		if (buffer.length < FRAME_LENGTH) {
			throw new ArithmeticException("too short signal");
		}
		int N = 2 * buffer.length / FRAME_LENGTH - 1;
		double[][] frames = new double[N][];
		for (int i = 0; i < N; i++){
			frames[i] = Arrays.copyOfRange(buffer, i * (FRAME_LENGTH / 2), (i + 2) * (FRAME_LENGTH / 2));
		}
		return frames;
	}
	
	
	public static void main(String[] args) {
		short [] buffer = {1,2,3,4,5,6,7,8,9,10};

//        buffer = Preprocessing.handle(buffer);

		double [][] audioFrames = divToFrames(normalize(buffer));
        double result [] = Mel.getMelKrepsCoef(audioFrames);
        System.out.println(result);
    }
}
