package poloapps.orbitfingers;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
//login
class ValuesRequest extends StringRequest {
    private static final String VALS_REQUEST_URL = "http://www.poloapps.com/GetVals.php";
    private Map<String, String> params;

    ValuesRequest(String username, Response.Listener<String> listener) {
        super(Method.POST, VALS_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}