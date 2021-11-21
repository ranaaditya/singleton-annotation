package com.ranaaditya.singleton;

import com.ranaaditya.singleton.usage.SingletonUsage;

public class Main {

    public static void main(String args[]) {
        SingletonUsage su  = SingletonUsage.getInstance();
        System.out.println(su.toString());
        System.out.print("Annotation Run Successfully !");
    }

}
