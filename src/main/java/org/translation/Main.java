package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
        // Translator translator = new InLabByHandTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {

        String quit = "quit";

        while (true) {
            String country = promptForCountry(translator);
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
            if (country.equals(quit)) {
                break;
            }
            String language = promptForLanguage(translator, country);
            if (language.equals(quit)) {
                break;
            }
            String output = country + " in " + language + " is "
                    + translator.translate(countryCodeConverter.fromCountry(country),
                    languageCodeConverter.fromLanguage(language)) + "\n" + "Press enter to continue or quit to exit.";
            System.out.print(output);
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (quit.equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();

        List<String> newCountries = new ArrayList<>(Collections.nCopies(countries.size(), null));

        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();

        for (int i = 0; i < countries.size(); i++) {
            String country = countryCodeConverter.fromCountryCode(countries.get(i));
            newCountries.set(i, country);
        }

        Collections.sort(newCountries);

        // Build the output for country selection
        StringBuilder output = new StringBuilder();
        for (String country : newCountries) {
            output.append(country).append("\n");
        }
        output.append("select a country from above:\n");
        System.out.print(output.toString());

        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

        String countryCode = countryCodeConverter.fromCountry(country);

        List<String> languages = translator.getCountryLanguages(countryCode);

        List<String> newLanguages = new ArrayList<>(Collections.nCopies(languages.size(), null));

        for (int i = 0; i < languages.size(); i++) {
            String language = languageCodeConverter.fromLanguageCode(languages.get(i));
            newLanguages.set(i, language);
        }

        Collections.sort(newLanguages);

        // Build the output for language selection
        StringBuilder output = new StringBuilder();
        for (String language : newLanguages) {
            output.append(language).append("\n");
        }
        output.append("select a language from above:\n");
        System.out.print(output.toString());

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
