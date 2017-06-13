package poloapps.orbitfingers;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
//login
class RankingRequest extends StringRequest {
    private static final String RANK_REQUEST_URL = "http://www.poloapps.com/GetRank.php";
    private Map<String, String> params;

    RankingRequest(String username, int server_peak, Response.Listener<String> listener) {
        super(Method.POST, RANK_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("server_peak", server_peak + "");
        params.put("username",username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}