package com.example.a01375554.aptdemo;

import android.util.Log;

import com.example.test_annotation.Test;


/**
 * Created by liyunlei
 * on 2018/4/19.
 * email: YunLeiLi@sf-express.com
 */

@Test(id = "dog", type = Animal.class)
public class Dog extends Animal {
    @Override
    public void move() {
        Log.e(TAG, "dog move");
    }
}
