package sing.shop;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class ShopApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
