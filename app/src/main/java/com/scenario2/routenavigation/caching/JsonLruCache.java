package com.scenario2.routenavigation.caching;

import android.util.Log;
import android.util.LruCache;

/**
 * LRU Cache implementation to save json string in the LRU cache.
 * key-value mapping
 */
public class JsonLruCache extends LruCache<String, String> {
    private LruCache<String, String> mMemoryCache;
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    private static JsonLruCache mInstance = null;
    /* A private Constructor prevents any other
   * class from instantiating.
   */
    private JsonLruCache(int maxSize){
        super(maxSize);
        mMemoryCache = new LruCache<String, String>(maxSize);
    }
    /* Static 'instance' method */
    public static JsonLruCache getInstance() {
        if(mInstance == null){
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            // Use 1/4th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 4;
            Log.e("PRI","cacheSize "+cacheSize);
            mInstance = new JsonLruCache(cacheSize);
        }
        return mInstance;
    }

    public void addJsonToMemoryCache(String key, String json) {
       
        if (getJsonFromMemCache(key) == null) {
            mMemoryCache.put(key, json);
        }
    }

    public String getJsonFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
