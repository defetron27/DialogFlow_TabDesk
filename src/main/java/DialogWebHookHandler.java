import Models.WordResponse;
import Utils.Constant;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class DialogWebHookHandler implements RequestStreamHandler
{
    private JSONParser jsonParser = new JSONParser();

    private String[] a = {"Adorable","Adventurous","Aggressive","Agreeable","Alert","Alive","Amused","Angry","Annoyed","Annoying","Anxious","Arrogant","Ashamed","Attractive","Average","Awful"};

    private String[] b = {"Bad","Beautiful","Better","Bewildered","Black","Bloody","Blue","Blue-eyed","Blushing","Bored","Brainy","Brave","Breakable","Bright","BusyBad","Beautiful","Better","Bewildered","Black","Bloody","Blue","Blue-eyed","Blushing","Bored","Brainy","Brave","Breakable","Bright","Busy"};

    private String[] c = {"Calm", "Careful", "Cautious", "Charming", "Cheerful", "Clean", "Clear", "Clever", "Cloudy", "Clumsy", "Colorful", "Combative", "Comfortable", "Concerned", "Condemned", "Confused", "Cruel", "Curious", "Cute"};

    private String[] d = {"Dangerous","Dark","Dead","Defeated","Defiant","Delightful","Depressed","Determined","Different","Difficult","Disgusted","Distinct","Disturbed","Dizzy","Doubtful","Drab","Dull"};

    private String[] e = {"Eager","Easy","Elated","Elegant","Embarrassed","Enchanting","Encouraging","Energetic","Enthusiastic","Envious","Evil","Excited","Expensive","Exuberant"};

    private String[] f = {"Fair","Faithful","Famous","Fancy","Fantastic","Fierce","Filthy","Fine","Foolish","Fragile","Frail","Frantic","Friendly","Frightened","Funny"};

    private String[] g = {"Gentle","Gifted","Glamorous","Gleaming","Glorious","Good","Gorgeous","Graceful","Grieving","Grotesque","Grumpy"};

    private String[] h = {"Handsome","Happy","Healthy","Helpful","Helpless","Hilarious","Homeless","Homely","Horrible","Hungry","Hurt"};

    private String[] i = {"Ill","Important","Impossible","Inexpensive","Innocent","Inquisitive","Itchy"};

    private String[] j = {"Jealous","Jittery","Jolly","Joyous"};

    private String[] k = {"kind","king"};

    private String[] l = {"Lazy","Light","Lively","Lonely","Long","Lovely","Lucky"};

    private String[] m = {"Magnificent","Misty","Modern","Motionless","Muddy","Mushy","Mysterious"};

    private String[] n = {"Nasty","Naughty","Nervous","Nice","Nutty"};

    private String[] o = {"Obedient","Obnoxious","Odd","Old-fashioned","Open","Outrageous","Outstanding"};

    private String[] p = {"Panicky","Perfect","Plain","Pleasant","Poised","Poor","Powerful","Precious","Prickly","Proud","Puzzled"};

    private String[] q = {"Quant"};

    private String[] r = {"Real","Relieved","Repulsive","Rich"};

    private String[] s = {"Scary","Selfish","Shiny","Shy","Silly","Sleepy","Smiling","Smoggy","Sore","Sparkling","Splendid","Spotless","Stormy","Strange","Stupid","Successful","Super"};

    private String[] t = {"Talented","Tame","Tender","Tense","Terrible","Tasty","Thankful","Thoughtful","Thoughtless","Tired","Tough","Troubled"};

    private String[] u = {"Ugliest","Ugly","Uninterested","Unsightly","Unusual","Upset","Uptight"};

    private String[] v = {"Victorious","Vivacious"};

    private String[] w = {"Wandering","Weary","Wicked","Wide-eyed","Wild","Witty","Worrisome","Worried","Wrong"};

    private String[] x = {"xeroxed","xeroxes","xiphoid","xanthic","xylidin","xerarch","xenopus","xylitol","xylenes"};

    private String[] y = {"youthfulnesses","yellowhammers","yellowthroats","yellowhammer","yellowthroat","yeastinesses","yesternights","yoctoseconds","youthfulness","youngberries"};

    private String[] z = {"Zany","Zealous"};

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String jsonResponseString;

        String fallbackMessage = "Oops.. there was some server or internal problem, don't worry please say that name again.";

        try
        {
            JSONObject jsonRequestObject = (JSONObject) jsonParser.parse(bufferedReader);

            if (jsonRequestObject.get(Constant.queryResult) != null)
            {
                JSONObject jsonQueryResult = (JSONObject) jsonRequestObject.get(Constant.queryResult);

                if (jsonQueryResult.get(Constant.intent) != null)
                {
                    JSONObject jsonIntent = (JSONObject) jsonQueryResult.get(Constant.intent);

                    if (jsonIntent.get(Constant.displayName) != null)
                    {
                        String jsonDisplayName = (String) jsonIntent.get(Constant.displayName);

                        switch (jsonDisplayName)
                        {
                            case "GetWordIntent" :

                                if (jsonQueryResult.get(Constant.parameters) != null)
                                {
                                    JSONObject jsonParameters = (JSONObject) jsonQueryResult.get(Constant.parameters);

                                    if (jsonParameters.get(Constant.word) != null)
                                    {
                                        String word = (String) jsonParameters.get(Constant.word);

                                        if (word != null && !word.equals("") && !word.equals("null"))
                                        {
                                            String getWord = getResponseForWord(word);

                                            WordResponse[] wordResponseList = new Gson().fromJson(getWord, WordResponse[].class);

                                            ArrayList<String> wordsList = new ArrayList<>();

                                            for (WordResponse aWordResponseList : wordResponseList)
                                            {
                                                wordsList.add(aWordResponseList.getWord());
                                            }

                                            if (wordsList.size() > 0)
                                            {
                                                String returnWord = wordsList.get(new Random().nextInt(wordsList.size()));

                                                jsonResponseString = getTextResponseForName(returnWord + ". Ok now you want say a word that begin with a letter " + returnWord.substring(returnWord.length() - 1));
                                            }
                                            else
                                            {
                                                String randomWord = getFallBackRandomWord(word);

                                                String finalRandomWord = randomWord + ". Ok now you want say a word that begin with a letter " + randomWord.substring(randomWord.length() - 1);

                                                jsonResponseString = getTextResponseForName(finalRandomWord);
                                            }
                                        }
                                        else
                                        {
                                            jsonResponseString = getTextResponseForName(fallbackMessage);
                                        }
                                    }
                                    else
                                    {
                                        jsonResponseString = getTextResponseForName(fallbackMessage);
                                    }
                                }
                                else
                                {
                                    jsonResponseString = getTextResponseForName(fallbackMessage);
                                }

                                break;
                            case "Default Welcome Intent" :

                                String welcomeResponse = "Hi, welcome to Tap Alpha. " +
                                        "It's a pleasure to talk to you." +
                                        "Ok, what is tab alpha?. " +
                                        "Tab Alpha is a simple alphabetic word game." +
                                        "Ok, i will give short examples to how to play the game." +
                                        "First you tell a one word,second i will tell another word, which that word begin with your word end letter." +
                                        "Example, you say a word \"compare\" immediately i say \"exam\" again you want to say a another word that begin with my word end letter." +
                                        "Ok, i trust above instructions are useful to you. If you have more help or instructions, say 'help'" +
                                        "Ok, if you ready to play, say any name to start the game.";

                                jsonResponseString = getTextResponseForName(welcomeResponse);

                                break;

                            case "Default Help Intent" :

                                String  helpResponse = "It pleasure to help you. \n" +
                                        "If you have any doubts or you don't know how to ask to 'Tab Alpha', don't worry. \n" +
                                        "Ok, i will give short examples to how to play the game." +
                                        "First you tell a one word,second i will tell another word, which that word begin with your word end letter, again you want to say a another word that begin with my word ent letter. till you cancel or stop the game" +
                                        "Example, you tell: Danger, i tell: range, again you tell: exam, again i tell: mother, the game will continue till you say stop." +
                                        "Ok, i trust above instructions are useful to you. If you have more help or instructions, say 'help'" +
                                        "Ok, if you ready to play, say any name to start the game.";

                                jsonResponseString = getTextResponseForName(helpResponse);

                                break;
                            default:

                                jsonResponseString = getTextResponseForName(fallbackMessage);

                                break;
                        }
                    }
                    else
                    {
                        jsonResponseString = getTextResponseForName(fallbackMessage);
                    }
                }
                else
                {
                    jsonResponseString = getTextResponseForName(fallbackMessage);
                }
            }
            else
            {
                jsonResponseString = getTextResponseForName(fallbackMessage);
            }
        }
        catch (ParseException exception)
        {
            jsonResponseString = getTextResponseForName(fallbackMessage);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        outputStreamWriter.write(jsonResponseString);
        outputStreamWriter.close();
    }

    private String getFallBackRandomWord(String word)
    {
        String letter = word.substring(word.length() - 1).toLowerCase();

        switch (letter)
        {
            case "a":
                return a[(int) (Math.random() * a.length)];
            case "b":
                return b[(int) (Math.random() * b.length)];
            case "c":
                return c[(int) (Math.random() * c.length)];
            case "d":
                return d[(int) (Math.random() * d.length)];
            case "e":
                return e[(int) (Math.random() * e.length)];
            case "f":
                return f[(int) (Math.random() * f.length)];
            case "g":
                return g[(int) (Math.random() * g.length)];
            case "h":
                return h[(int) (Math.random() * h.length)];
            case "i":
                return i[(int) (Math.random() * i.length)];
            case "j":
                return j[(int) (Math.random() * j.length)];
            case "k":
                return k[(int) (Math.random() * k.length)];
            case "l":
                return l[(int) (Math.random() * l.length)];
            case "m":
                return m[(int) (Math.random() * m.length)];
            case "n":
                return n[(int) (Math.random() * n.length)];
            case "o":
                return o[(int) (Math.random() * o.length)];
            case "p":
                return p[(int) (Math.random() * p.length)];
            case "q":
                return q[(int) (Math.random() * q.length)];
            case "r":
                return r[(int) (Math.random() * r.length)];
            case "s":
                return s[(int) (Math.random() * s.length)];
            case "t":
                return t[(int) (Math.random() * t.length)];
            case "u":
                return u[(int) (Math.random() * u.length)];
            case "v":
                return v[(int) (Math.random() * v.length)];
            case "w":
                return w[(int) (Math.random() * w.length)];
            case "x":
                return x[(int) (Math.random() * x.length)];
            case "y":
                return y[(int) (Math.random() * y.length)];
            case "z":
                return z[(int) (Math.random() * z.length)];
            default:
                return "";
        }
    }

    private String getResponseForWord(String word)
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
        catch (IOException exception)
        {
            return getFallBackRandomWord(word);
        }
    }

    private String getTextResponseForName(String speechText)
    {
        JSONObject responseForName = new JSONObject();

        responseForName.put(Constant.fulfillmentText,speechText);

        JSONObject fulFillMessageObject = new JSONObject();

        JSONArray fulFillMessageArray = new JSONArray();

        JSONObject textResponseObject = new JSONObject();

        JSONArray textResponseArray = new JSONArray();

        textResponseArray.add(speechText);

        textResponseObject.put(Constant.text,textResponseArray);

        fulFillMessageObject.put(Constant.text,textResponseObject);

        fulFillMessageArray.add(fulFillMessageObject);

        responseForName.put(Constant.fulfillmentMessages,fulFillMessageArray);

        responseForName.put("source","");
        responseForName.put("payload", null);
        responseForName.put("outputContexts", null);
        responseForName.put("followupEventInput", null);

        return responseForName.toJSONString();
    }
}
