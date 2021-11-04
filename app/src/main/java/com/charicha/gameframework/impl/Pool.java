package com.charicha.gameframework.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charicha on 12/22/2017.
 */

public class Pool<T> {

    private List<T> mFreeObjects;
    private PoolObjectFactory<T> mFactory;
    private int maxSize;

    public interface PoolObjectFactory<T>{
        public T createObject();
    }

    public Pool(PoolObjectFactory<T> factory, int maxSize){
        this.mFactory = factory;
        this.maxSize = maxSize;
        this.mFreeObjects = new ArrayList<T>(maxSize);
    }

    public T newObject(){
        if(!mFreeObjects.isEmpty()){
            return mFreeObjects.remove(mFreeObjects.size() - 1);
        } else {
            return mFactory.createObject();
        }
    }

    public void free(T object){
        if(mFreeObjects.size() < maxSize){
            mFreeObjects.add(object);
        }
    }

}
