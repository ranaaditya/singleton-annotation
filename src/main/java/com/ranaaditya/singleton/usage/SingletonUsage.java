package com.ranaaditya.singleton.usage;

import com.ranaaditya.singleton.annotation.Singleton;

@Singleton
public class SingletonUsage {

    private SingletonUsage() {}

    public static SingletonUsage getInstance() {
        return new SingletonUsage();
    }
}
