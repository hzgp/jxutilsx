/*
 *     (C) Copyright 2019, ForgetSky.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.jxkj.utils;


import android.text.TextUtils;


import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Cache with Offline
 * Created by ForgetSky on 2019/3/17.
 */
public class OfflineCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!OSUtils.isNetworkConnected(BaseApplication.getContext())) {
            int offlineCacheTime = 60 * 60 * 24 * 28;//离线的时候的缓存的过期时间,4周
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
                    .removeHeader("Pragma")
                    .build();
        }else {
            Map<String,String> headers = getHeaders();
            if (headers!=null &&!headers.isEmpty()){
                Request.Builder builder = request.newBuilder();
                String value;
                for (String key:headers.keySet()){
                    value = headers.get(key);
                    if (TextUtils.isEmpty(value))
                        continue;
                    builder.addHeader(key,value);
                }
                request = builder.build();
            }
        }
        return chain.proceed(request);
    }
    public Map<String,String> getHeaders(){
        return null;
    }
}
