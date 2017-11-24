package com.example.pocketdevs.pocketcloset.common;

/**

 */
import android.content.Context;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

public interface HttpBackend {

    class RequestDetails {
        private final URI mUrl;
        private final List<PostField> mPostFields;

        public RequestDetails(final URI url, final List<PostField> postFields) {
            mUrl = url;
            mPostFields = postFields;
        }

        public URI getUrl() {
            return mUrl;
        }

        public List<PostField> getPostFields() {
            return mPostFields;
        }
    }

    class PostField {
        public final String name;
        public final String value;

        public PostField(final String name, final String value) {
            this.name = name;
            this.value = value;
        }

        public String encode() {
            try {
                return URLEncoder.encode(name, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            } catch(UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public static String encodeList(final List<PostField> fields) {
            final StringBuilder result = new StringBuilder();
            for(final PostField field : fields) {
                if(result.length() > 0) {
                    result.append('&');
                }

                result.append(field.encode());
            }

            return result.toString();
        }
    }

    interface Request {
        void executeInThisThread(final Listener listener);
    }

    interface Listener {
        void onError(RequestFailureType failureType, Throwable exception, Integer httpStatus);
        void onSuccess(
                String mimetype,
                Long bodyBytes,
                InputStream body);
    }

    Request prepareRequest(
            Context context,
            RequestDetails details);

    enum RequestFailureType {
        CONNECTION, REQUEST
    }
}