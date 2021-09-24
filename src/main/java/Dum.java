import Models.WordResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Dum
{
    public static void main(String[] args)
    {
        String res = response("bird");

        WordResponse[] jsonObject = new Gson().fromJson(res,WordResponse[].class);

        System.out.println(jsonObject);
    }

    private static String response(String word)
    {
        try
        {
            String letter = word.substring(word.length() - 1);

            URL urlDetail = new URL("https://api.datamuse.com/words?sp=" + letter + "*");

            HttpsURLConnection connection = (HttpsURLConnection) urlDetail.openConnection();

            connection.setDoOutput(true);

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/json");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            StringBuilder resultBuilder = new StringBuilder();

            String jsonOutput;

            while ((jsonOutput = bufferedReader.readLine()) != null)
            {
                resultBuilder.append(jsonOutput);
            }

            return resultBuilder.toString();
        }
        catch (IOException e)
        {
            return "";
        }
    }
}
