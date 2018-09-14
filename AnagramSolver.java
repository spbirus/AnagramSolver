import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AnagramSolver {

    public static void main(String[] args) throws IOException {
        DictInterface dict = null;
        ArrayList<String> permList = new ArrayList();
        ArrayList<String> allSolvedWords = new ArrayList();
        ArrayList<String> phrase = new ArrayList();
        String input = args[0];
        String output = args[1];
        String choice = args[2];

        if (choice.toLowerCase().equals("orig")) {
            dict = new MyDictionary();
        } else if (choice.toLowerCase().equals("dlb")) {
            dict = new DLB();
        } else {
            System.out.println("Didn't input a correct dictionary");
        }

        addWordsToDict(dict);

        ArrayList inAnagrams = inputFile(input);

        for (Object anagram : inAnagrams) {

            ArrayList<String> solvedWords = new ArrayList();
            System.out.print("\nHere are the results for ");
            System.out.print(anagram.toString() + ": \n");

            //remove all the spaces from the inpupt string
            anagram = anagram.toString().replaceAll("\\s+", "");

            //start recurssing
            genPerm("", anagram.toString(), solvedWords, phrase, dict);

            //sort list of solved words alphabetically
            Collections.sort(solvedWords);

            //print list of solved words
            for (String word : solvedWords) {
                System.out.println(word);
            }

            allSolvedWords.add("Here are the results for " + anagram.toString() + ": ");
            allSolvedWords.addAll(solvedWords);
            allSolvedWords.add("\n");
        }
        //output to the file
        outputFile(allSolvedWords, output);

    }

    static void genPerm(String prefix, String suffix, ArrayList solvedWords, ArrayList phrase, DictInterface dict) {

        if (suffix.length() == 0) {
            switch (dict.searchPrefix(new StringBuilder(prefix))) {
                //not a prefix or word
                case 0:
                    //System.out.println("Is Neither\n");
                    return;
                //prefix
                case 1:
                    //System.out.println("Is Prefix\n");
                    break;
                //word
                case 2:
                    //System.out.println("Is Word\n");
                    //prevents adding the same word
                    if (!solvedWords.contains(phrase.toString())) {
                        phrase.add(prefix);
                        if (!solvedWords.contains(phrase.toString())) {
                            solvedWords.add(phrase.toString());
                        }
                        phrase.remove(phrase.size()-1);
                    }
                    //break;
                    return;
                //prefix and word
                case 3:
                    //System.out.println("Is Both\n");
                    if (!solvedWords.contains(phrase.toString())) {
                        phrase.add(prefix);
                        if (!solvedWords.contains(phrase.toString())) {
                            solvedWords.add(phrase.toString());
                        }
                        phrase.remove(phrase.size()-1);
                        return;
                    }
                    //return;
                    break;

            }

        } else if (prefix.length() == 0) {
            //for loop to iterate through the first letters
            for (int i = 0; i < suffix.length(); i++) {
                genPerm(prefix + suffix.charAt(i), suffix.substring(0, i) + suffix.substring(i + 1, suffix.length()), solvedWords, phrase, dict);
            }
        } else {
            //for loop prevents suffixes with length 0 from being entered in hence why we have to check if the suffix length is 0 in the if statement
            for (int i = 0; i < suffix.length(); i++) {

                //System.out.println("Prefix: " + prefix.toString());
                //System.out.println("Prefix Length: " + prefix.length());
                //System.out.println("Suffix: " + suffix.toString());
                //System.out.println("Suffix Length: " + suffix.length());
                switch (dict.searchPrefix(new StringBuilder(prefix))) {
                    //not a prefix or word
                    case 0:
                        //System.out.println("Is Neither\n");
                        return;
                    //prefix
                    case 1:
                        //System.out.println("Is Prefix\n");
                        //prevents out of bounds exception
                        if (suffix.length() != 0) {
                            genPerm(prefix + suffix.charAt(i), suffix.substring(0, i) + suffix.substring(i + 1, suffix.length()), solvedWords, phrase, dict);
                        }
                        //return;
                        break;
                    //word
                    case 2:
                        //System.out.println("Is Word\n");
                        phrase.add(prefix);
                        genPerm("", suffix, solvedWords, phrase, dict);
                        phrase.remove(prefix);
                        return;
                    //prefix and word
                    case 3:
                        //System.out.println("Is Both\n");

                        //try using it as a prefix
                        genPerm(prefix + suffix.charAt(i), suffix.substring(0, i) + suffix.substring(i + 1, suffix.length()), solvedWords, phrase, dict);

                        //try using it as a word 
                        phrase.add(prefix);
                        genPerm("", suffix, solvedWords, phrase, dict);
                        phrase.remove(prefix);

                        break;

                }
            }
        }
    }

    private static ArrayList inputFile(String input) throws FileNotFoundException, IOException {
        //prompt user for a input file with strings of anagrams to be solved
        //Scanner reader = new Scanner(System.in);
        //System.out.println("Enter file name with list of anagram strings\n");
        //String fileName = reader.nextLine();
        File file = new File(input);

        ArrayList<String> everything = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            String line = br.readLine();
            while (line != null) {
                everything.add(line);
                line = br.readLine();
            }
        } finally {
            br.close();
        }

        return everything;
    }

    private static void outputFile(ArrayList list, String output) throws IOException {
        //prompt user for a output file to put solved anagrams into
        //Scanner reader = new Scanner(System.in);
        //System.out.println("\nEnter file name for the output file\n");
        //String fileName = reader.nextLine();
        File file = new File(output);

        FileWriter fw = new FileWriter(file);

        for (Object word : list) {
            fw.write(word.toString());
            fw.write(System.lineSeparator());
        }

        fw.close();

    }

    private static void addWordsToDict(DictInterface dict) throws FileNotFoundException {
        //adds the words from the dictionary.txt file to the dictionary
        Scanner fileScan = new Scanner(new FileInputStream("dictionary.txt"));
        String newWord;

        while (fileScan.hasNext()) {
            newWord = fileScan.nextLine();
            dict.add(newWord);
        }
    }
}
