package csd.auth.callbacks;

import java.awt.*;

/**
 * Created by ilias on 6/1/2017.
 */
public interface OutputCallback {
    /**
     *  OutputCallback callback
     * @param response
     * @return
     */
    void onOutputResponse(String response);

    void oStatusResponse(String label, Color color);
}
