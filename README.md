# singleton-annotation
Building Simple Singleton annotation for android / JVM

## Usage
~~~java
import <your_package_name>;
import com.ranaaditya.singleton.annotation.Singleton;

@Singleton
public class RandomSingleton {

    private RandomSingleton() {}

    public static RandomSingleton getInstance() {
        return new RandomSingleton();
    }
}
~~~
