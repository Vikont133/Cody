package com.example.cody.algorithm;


import android.os.Environment;
import com.example.cody.AppLog;
import com.example.cody.algorithm.preprocessing.Preprocessing;
import com.example.cody.algorithm.processing.Mel;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainHandler{

	public static final int FRAME_LENGTH = 2048;
    public static final int SAMPLING_FREQ = 16000;
    public static final String USER_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Cody/users";

	public static void addUser(String name, short[] audioBuffer) {
        double[] mels = getMelKreps(audioBuffer);
        save(name, mels);
	}
	public static void removeUser(String name) {
        try {
            remove(name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String checkVoice(short[] audioBuffer) {
        double[] mels = getMelKreps(audioBuffer);
        return check(mels);
	}

    private static String check(double[] mels) {
        File dir = new File(USER_DIRECTORY);

        if(!dir.isDirectory()){
            AppLog.logString("No user data directory");
            return null;
        }
        if (dir.listFiles().length == 0){
            AppLog.logString("No added users");
            return null;
        }
        String name = null;
        double distance = 99999;
        for(File file : dir.listFiles()){
            double tmp = getDistance(readMels(file), mels);
            if (tmp < distance){
                distance = tmp;
                name = file.getName();
            }
        }
        return name;
    }

    private static double getDistance(double[] mels1, double[] mels2) {
        double distance = 0;
        for (int i = 0; i < mels1.length; i++) {
            distance += Math.abs(mels1[i] - mels2[i]);
        }
        return distance;
    }

    private static double[] readMels(File file) {
        try {
            DataInputStream stream = new DataInputStream( new FileInputStream(file));
            List<Double> mels = new ArrayList<Double>();
            while(stream.available() > 0)
            {
                double c = stream.readDouble();
                mels.add(c);
            }
            double[] array = new double[mels.size()];
            for(int i = 0; i < mels.size(); i++) {
                array[i] = mels.get(i);
            }
            return array;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void remove(String name) throws IllegalAccessException {
        File file = new File(USER_DIRECTORY, name);
        if (!file.exists()){
            throw new IllegalAccessException("User doesn't exist");
        }
        file.delete();
    }

    private static void save(String name, double[] mels) {
        File file = new File(USER_DIRECTORY);

        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            String path = USER_DIRECTORY + "/" + name;
            DataOutputStream myfile = new DataOutputStream( new FileOutputStream(path));
            for(double x : mels){
                myfile.writeDouble(x);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double[] getMelKreps(short[] audioBuffer){
        double [] tmp = Preprocessing.handle(normalize(audioBuffer));
        double [][] audioFrames = divToFrames(tmp);
        return Mel.getMelKrepsCoef(audioFrames);
    }
	
	private static double[] normalize(short[] audioBuffer) {
		short max = findMax(audioBuffer);
        AppLog.logString("Max size = " + max);
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
        File dir = new File(USER_DIRECTORY);

        for(File file : dir.listFiles()){
            double[] mels = readMels(file);
            for (double x : mels){
                System.out.print(x + " ");
            }
            System.out.println();
        }
    }
}
