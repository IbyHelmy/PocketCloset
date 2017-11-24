package com.example.pocketdevs.pocketcloset.common;

/**
 */

import android.content.Context;

import com.example.pocketdevs.pocketcloset.App;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class OKHttpBackend implements HttpBackend {

    @Override
    public Request prepareRequest(final Context context, final RequestDetails details) {
        final com.squareup.okhttp.Request.Builder builder = new com.squareup.okhttp.Request.Builder();
        final List<PostField> postFields = details.getPostFields();

        if(postFields != null) {
            builder.post(RequestBody.create(
                    MediaType.parse("application/x-www-form-urlencoded"),
                    PostField.encodeList(postFields)));

        } else {
            builder.get();
        }

        builder.url(details.getUrl().toString());
        builder.cacheControl(CacheControl.FORCE_NETWORK);

        final AtomicReference<Call> callRef = new AtomicReference<>();

        return new Request() {
            public void executeInThisThread(final Listener listener) {
                final Call call = App.getOkHttpClientInstance(false).newCall(builder.build());
                callRef.set(call);

                try {
                    final Response response;

                    try {
                        response = call.execute();
                    } catch(IOException e) {
                        listener.onError(RequestFailureType.CONNECTION, e, null);
                        return;
                    }

                    final int status = response.code();
                    if(status == 200 || status == 202) {
                        final ResponseBody body = response.body();
                        final InputStream bodyStream;
                        final Long bodyBytes;

                        if(body != null) {
                            bodyStream = body.byteStream();
                            bodyBytes = body.contentLength();

                        } else {
                            bodyStream = null;
                            bodyBytes = null;
                        }

                        final String contentType = response.header("Content-Type");
                        listener.onSuccess(contentType, bodyBytes, bodyStream);

                        if(body!=null)
                            body.close();

                    } else {
                        listener.onError(RequestFailureType.REQUEST, null, status);
                    }

                } catch(Throwable t) {
                    listener.onError(RequestFailureType.CONNECTION, t, null);
                }
            }
        };
    }
}