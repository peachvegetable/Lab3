package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final Map<String, JSONObject> countryDataMap;
    private final Map<String, List<String>> countryLanguagesMap;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {

        countryDataMap = new HashMap<>();
        countryLanguagesMap = new HashMap<>();

        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            Set<String> knownNonLanguageKeys = new HashSet<>(Arrays.asList("id", "alpha2", "alpha3"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);
                String countryCode = countryObject.getString("alpha3");

                countryDataMap.put(countryCode, countryObject);

                List<String> languages = new ArrayList<>();
                for (String key : countryObject.keySet()) {
                    if (!knownNonLanguageKeys.contains(key)) {
                        languages.add(key);
                    }
                }
                countryLanguagesMap.put(countryCode, languages);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String countryCode) {
        return countryLanguagesMap.get(countryCode);
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countryDataMap.keySet());
    }

    @Override
    public String translate(String countryCode, String languageCode) {
        // Return the translation if it exists, otherwise return null
        if (countryDataMap.containsKey(countryCode)) {
            JSONObject countryObject = countryDataMap.get(countryCode);
            if (countryObject.has(languageCode)) {
                return countryObject.getString(languageCode);
            }
        }
        return "Country not found";
    }
}
