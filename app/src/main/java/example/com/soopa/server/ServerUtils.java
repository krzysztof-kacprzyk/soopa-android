package example.com.soopa.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import static android.content.ContentValues.TAG;

public class ServerUtils {
    public interface Callback<A,B> {
        void onSuccess(A obj);
        void onFail(B obj);
    }


    public static boolean isReadyForQuery(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;

        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    static class ServerResponse<A> {

        /**
         * Executes onFail method of the Callback with suitable error message (depending on the type of error)
         * @param callback Callback whose second Object in a constructor is String (for error message)
         * @return ErrorListener that can be used in http requests
         */
        public Response.ErrorListener simpleError(final Callback<A, String> callback) {
            return new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorType = "";
                    Log.d(TAG, "Error on login");
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        errorType = ServerConstants.CONNECTION_ERROR;
                    } else if (error instanceof AuthFailureError) {
                        errorType = ServerConstants.WRONG_CREDENTIALS_ERROR;
                    } else {
                        errorType = ServerConstants.UNKNOWN_ERROR;
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.d(TAG, "Status code: " + String.valueOf(networkResponse.statusCode));
                        } else {
                            Log.d(TAG, "Response is empty");
                        }
                    }
                    callback.onFail(errorType);
                }
            };
        }
    }
}