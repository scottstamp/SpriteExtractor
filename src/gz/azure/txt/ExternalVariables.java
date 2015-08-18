package gz.azure.txt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Scott on 8/18/2015.
 */
public class ExternalVariables {
    // https://www.habbo.com/gamedata/external_variables/1
    private final URL varsLocation;
    private final HashMap<String, String> variables;

    public ExternalVariables(URL varsLocation) throws Throwable {
        this.varsLocation = varsLocation;
        variables = new HashMap<>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(varsLocation.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("=")) {
                String key = inputLine.split("=")[0];
                String value = inputLine.substring(key.length() + 1);
                variables.put(key, value);
            }
        }
    }

    public List<String> getPets() {
        return Arrays.asList(variables.get("pet.configuration").split(","));
    }

    public String getFlashClientURL() {
        return variables.get("flash.client.url");
    }

    public String getFlashDynamicDownloadURL() {
        return variables.get("flash.dynamic.download.url");
    }
}
