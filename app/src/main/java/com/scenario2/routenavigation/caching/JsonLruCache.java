package com.scenario2.routenavigation.caching;

import android.util.Log;
import android.util.LruCache;

/**
 * Created by priyag on 26-Apr-16.
 */
public class JsonLruCache extends LruCache<String, String> {
    private LruCache<String, String> mMemoryCache;
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public JsonLruCache(int maxSize) {
        super(maxSize);
        mMemoryCache = new LruCache<String, String>(maxSize);
    }

    public void addJsonToMemoryCache(String key, String json) {

        if (getJsonFromMemCache(key) == null) {
            mMemoryCache.put(key, json);
        } else {
            Log.e("PRI","key ="+getJsonFromMemCache(key));
        }
    }

    public String getJsonFromMemCache(String key) {
        Log.e("PRI","key ="+mMemoryCache.get(key));
        return mMemoryCache.get(key);
    }
}
