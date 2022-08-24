package com.jxkj.utils;

/**
 * Desc:  主要用于图片下载进度监听
 * Author:zhuJb
 * Date:2020/9/3
 */
public interface DownLoadProgressListener {
    int CODE_DOWNLOADING = 1;
    int CODE_DOWNLOAD_NON = 2;
    int CODE_DOWNLOAD_FAIL = 3;
    int CODE_FILE_EXPIRED = 5;
    void progress(int progress);

    void onFail(int errorCode);

    void onSuccess(byte[] resource);
    void onStart();
}
