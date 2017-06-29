package poloapps.orbitfingers;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//login
class MessageRequest extends StringRequest {
    private static final String UPDATE_REQUEST_URL = "http://www.poloapps.com/UpdateMessages.php";
    private Map<String, String> params;

    MessageRequest( String username ,String message, Response.Listener<String> listener) {
        super(Method.POST, UPDATE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username",username);
        params.put("message", message);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

