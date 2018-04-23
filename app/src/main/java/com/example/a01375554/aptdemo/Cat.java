package com.example.a01375554.aptdemo;

import android.util.Log;

import com.example.test_annotation.Test;


/**
 * Created by liyunlei
 * on 2018/4/19.
 * email: YunLeiLi@sf-express.com
 */

@Test(id = "cat", type = Animal.class)
public class Cat extends Animal {
    @Override
    public void move() {
        Log.e(TAG, "cat move");
    }
}
