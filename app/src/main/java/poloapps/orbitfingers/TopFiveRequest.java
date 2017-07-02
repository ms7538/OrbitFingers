package poloapps.orbitfingers;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
//login
class TopFiveRequest extends StringRequest {
    private static final String TOP10_REQUEST_URL = "http://www.poloapps.com/GetTT.php";
    private Map<String, String> params;

    TopFiveRequest(String username, int Server_Peak, Response.Listener<String> listener) {
        super(Method.POST, TOP10_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username",username);
        params.put("server_peak",Server_Peak + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}