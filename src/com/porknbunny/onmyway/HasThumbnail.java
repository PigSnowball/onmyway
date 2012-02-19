package com.porknbunny.onmyway;

import java.io.File;

public interface HasThumbnail {
    String getThumbnailURL();

    File getThumbnail();

    void setThumbDownloading(boolean isDownloading);

    boolean isThumbDownloading();

    void setThumbnail(File cacheFile);
}
