// MyApplication.java
package finki.nichk;

import android.app.Application;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    // Create a thread pool with 4 threads
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
