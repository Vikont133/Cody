package com.example.cody.algorithm;


import android.os.Environment;
import com.example.cody.AppLog;
import com.example.cody.algorithm.preprocessing.Preprocessing;
import com.example.cody.algorithm.processing.Mel;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainHandler{

	public static final int FRAME_LENGTH = 2048;
    public static final int SAMPLING_FREQ = 16000;
    public static final String USER_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Cody/users";
    public static final double MAX_DISTANCE = 10.0;
    public static final int CLUSTER_NUMBER = 4;
    public static final int ITERATION_NUMBER = 100;

	public static void addUser(String name, List<double[]> audioBuffer) {
        save(name, cluster(audioBuffer));
	}

	public static void removeUser(String name) {
        try {
            remove(name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String checkVoice(short[] audioBuffer) {
        double[] mels = new double[0];
        try {
            mels = getMelKreps(audioBuffer);
        } catch (IOException e) {
            return null;
        }
        return check(mels);
	}

    public static boolean isUserExist(String name) {
        File file = new File(USER_DIRECTORY, name);
        return file.exists();
    }

    public static String[] getUserList() {
        File file = new File(USER_DIRECTORY);
        return file.list();
    }

    private static List<Double[]> cluster (List<double[]> list){
        KMeans kmeans = new KMeans(CLUSTER_NUMBER, ITERATION_NUMBER);
        Dataset dataset = new DefaultDataset();
        for (double[] vector : list){
            dataset.add(new DenseInstance(vector));
        }
        List<Double[]> result = new ArrayList<Double[]>();
        for (Dataset ds : kmeans.cluster(dataset)){
            List<Double[]> cluster = new ArrayList<Double[]>();
            for (int i = 0; i < ds.size(); i++) {
                DenseInstance instance = (DenseInstance) ds.instance(i);
                Collection<Double> collection = instance.values();
                Double[] tmp = collection.toArray(new Double[collection.size()]);
                cluster.add(tmp);
            }
            result.add(center(cluster));
        }
        return result;
    }

    private static Double[] center(List<Double[]> cluster) {
        List<Double> result = new ArrayList<Double>();
        for (int i = 0; i < Mel.NUMBER_OF_FILTERS; i++) {
            double tmp = 0;
            for (int j = 0; j < cluster.size(); j++) {
                tmp += cluster.get(j)[i];
            }
            result.add(i, tmp / cluster.size());
        }
        return result.toArray(new Double[result.size()]);
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
        try{
            for(File file : dir.listFiles()){
                double tmp = getDistance(file, mels);
                if (tmp < distance){
                    distance = tmp;
                    name = file.getName();
                }
            }
        } catch (FileNotFoundException e){
            AppLog.logString("WTF?");
        }
        if (distance > MAX_DISTANCE) {
            return null;
        }
        return name;
    }

    private static double getDistance(File file, double[] mels) throws FileNotFoundException {
        DataInputStream stream = new DataInputStream( new FileInputStream(file));
        double distance = 99999;
        for (int i = 0; i < CLUSTER_NUMBER; i++) {
            double tmp = getDistance(readMels(stream), mels);
            System.out.println(tmp + " " + file.getName());
            distance = (distance > tmp) ? tmp : distance;
        }
        return distance;
    }

    private static double getDistance(Double[] mels1, double[] mels2) {
        double distance = 0;
        for (int i = 0; i < mels1.length; i++) {
            distance += Math.abs(mels1[i] - mels2[i]);
        }
        return distance;
    }

    private static Double[] readMels(DataInputStream stream) {
        try {
            List<Double> mels = new ArrayList<Double>();
            for (int i = 0; i < Mel.NUMBER_OF_FILTERS; i++) {
                mels.add(stream.readDouble());
            }
            return mels.toArray(new Double[mels.size()]);
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

    private static void save(String name, List<Double[]> mels) {
        File file = new File(USER_DIRECTORY);

        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            String path = USER_DIRECTORY + "/" + name;
            DataOutputStream myfile = new DataOutputStream( new FileOutputStream(path));
            for (Double[] center : mels){
                for (Double mel : center){
                    myfile.writeDouble(mel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double[] getMelKreps(short[] audioBuffer) throws IOException {
        double [] tmp = Preprocessing.handle(normalize(audioBuffer));
        double [][] audioFrames = divToFrames(tmp);
        return Mel.getMelKrepsCoef(audioFrames);
    }
	
	private static double[] normalize(short[] audioBuffer) throws IOException {
		short max = findMax(audioBuffer);
		double[] newBuffer = new double[audioBuffer.length];
		for(int i = 0; i < audioBuffer.length; i++){
			newBuffer[i] = (double)audioBuffer[i] / max;
		}
		return newBuffer;
	}
	
	private static short findMax(short[] audioBuffer) throws IOException {
        if(audioBuffer.length < 1)
            throw new IOException("Empty buffer");
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
}
