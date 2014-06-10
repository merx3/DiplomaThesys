package mmarinov.copy.nodeclipse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nodeclipse.ui.util.Constants;
import org.nodeclipse.ui.util.NodeclipseConsole;
import org.nodeclipse.ui.util.ProcessUtils;

/**
 * @author LambGao
 * @author Paul Verest
 * TODO don't use static {}
 */
public class ContentProvider {

    public static JSONArray COMPLETIONS = null;
    /*
     * 
     */

    static {
        try {
        	InputStream is = null;
        	// option to have completions.json as external file
        	String completionJsonPath = ProcessUtils.getCompletionsJsonPath();
        	if (completionJsonPath == null || completionJsonPath.equals("")) {
        		completionJsonPath = Constants.COMPLETIONS_JSON;
        		is = ContentProvider.class.getClassLoader().getResourceAsStream(completionJsonPath);
        	} else {
    			File file = new File(completionJsonPath);
    			if (!file.exists()) {
    				NodeclipseConsole.write("File "+completionJsonPath+" does not exist! \n");
    			}else{
    				is = new FileInputStream(file);
    			}
        	}
            
            if (is==null){
            	NodeclipseConsole.write("Error while reading file "+completionJsonPath+"! \n");
            }else{
                JSONObject object = new JSONObject(inputStream2String(is));
                COMPLETIONS = object.getJSONArray(Constants.COMPLETIONS_KEY);
            }
        } catch (JSONException e) {
        	NodeclipseConsole.write(e.getLocalizedMessage()+"\n");
        } catch (IOException e) {
        	NodeclipseConsole.write(e.getLocalizedMessage()+"\n");
        }
    }

    public static String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

}
