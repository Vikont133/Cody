package com.example.cody.algorithm;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.cody.VoiceHandler;

/**
 * @author vikont
 *
 */
public class MainHandler implements VoiceHandler{

	public static final int FRAME_LENGTH = 2048;
	
	@Override
	public void addUser(String name, short[] audioBuffer) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeUser(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkVoice(short[] audioBuffer) {
		// TODO Auto-generated method stub
		
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
		short max = audioBuffer[0];
		for (int i = 1; i < audioBuffer.length; i++){
			if (audioBuffer[i] > max){
				max = audioBuffer[i];
			}
		}
		return max;
	}

	private static double[][] divToFrames(double[] buffer) {
		double[] newBuffer = expandBuffer(buffer);
		int N = newBuffer.length / FRAME_LENGTH;
		double[][] frames = new double[N][];
		for (int i = 0; i < N; i++){
			frames[i] = Arrays.copyOfRange(newBuffer, i * FRAME_LENGTH, (i + 1) * FRAME_LENGTH);
		}
		return frames;
	}
	
	private static double[] expandBuffer(double[] buffer){
		if (buffer.length % FRAME_LENGTH == 0){
			return buffer;
		}
		int newLength = (buffer.length / FRAME_LENGTH + 1) * FRAME_LENGTH;
		double[] newBuffer = new double[newLength];
		System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
		return newBuffer;
	}
	
	public static void main(String[] args) {
		short [] buffer = {1, 2, 3, 4, 5, 6, 7, 8};
		double [][] audioFrames = divToFrames(normalize(buffer));
		for (int i = 0; i < audioFrames.length; i++) {
			for (int j = 0; j < audioFrames[i].length; j++) {
				System.out.print(audioFrames[i][j] + " ");
			}
			System.out.println();
		}
	}	
}
