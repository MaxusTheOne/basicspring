package jon.basicspring;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SerializeScaler {
    public static void main(String[] args) {
        double[] mean = { /* your mean values */ };
        double[] scale = { /* your scale values */ };
        StandardScaler scaler = new StandardScaler(mean, scale);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/scaler.pkl"))) {
            oos.writeObject(scaler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}