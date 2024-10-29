package jon.basicspring;
import java.io.Serializable;

public class StandardScaler implements Serializable {
    private double[] mean;
    private double[] scale;

    public StandardScaler(double[] mean, double[] scale) {
        this.mean = mean;
        this.scale = scale;
    }

    public float[][] transform(float[][] data) {
        int nSamples = data.length;
        int nFeatures = data[0].length;
        float[][] transformedData = new float[nSamples][nFeatures];

        for (int i = 0; i < nSamples; i++) {
            for (int j = 0; j < nFeatures; j++) {
                transformedData[i][j] = (float) ((data[i][j] - mean[j]) / scale[j]);
            }
        }

        return transformedData;
    }
}