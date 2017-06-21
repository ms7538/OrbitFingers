package poloapps.orbitfingers;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
//login
class UpdateRequest extends StringRequest {
    private static final String UPDATE_REQUEST_URL = "http://www.poloapps.com/UpdateDB1.php";
    private Map<String, String> params;

    UpdateRequest(String username, int peak, int min, int smp, Response.Listener<String> listener) {
        super(Method.POST, UPDATE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("peak" ,peak + "");
        params.put("min"  ,min  + "");
        params.put("smp"  ,smp  + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

