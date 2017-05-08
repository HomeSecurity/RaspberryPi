/**
 * Created by Armin on 26.12.2016.
 */
public interface RadioListener {
    void onData(int sensorId, boolean data, double voltage);

    void onRegistration(int sensorId, int type, double voltage);
}
