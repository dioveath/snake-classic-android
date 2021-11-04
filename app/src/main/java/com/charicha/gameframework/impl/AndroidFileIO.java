package com.charicha.gameframework.impl;

import android.content.Context;
import android.content.res.AssetManager;

import com.charicha.gameframework.framework.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Charicha on 12/22/2017.
 */


public class AndroidFileIO implements FileIO {

    Context mContext;
    AssetManager mAssetManager;
    String mExternalDir;

    public AndroidFileIO(Context context){
        this.mContext = context;
        this.mAssetManager = context.getAssets();
        this.mExternalDir = context.getExternalFilesDir("").getAbsolutePath() + File.separator;
    }

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return mAssetManager.open(fileName);
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(mExternalDir + fileName);
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(mExternalDir + fileName);
    }
}
