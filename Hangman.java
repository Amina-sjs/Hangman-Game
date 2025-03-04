import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Hangman {
    private static final String WORDS_FILE = "words.txt";
    private static final int MAX_LIVES = 6;
    private String wordToGuess;
    private char[] guessedWord;
    private int lives;
    private Set<Character> guessedLetters;

    private static final String[] HANGMAN_STATES = {
        "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\  |\n      |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\  |\n /    |\n      |\n=========",
        "  +---+\n  |   |\n  O   |\n /|\  |\n / \  |\n      |\n========="
    };

    public Hangman() throws IOException {
        this.wordToGuess = chooseRandomWord();
        this.guessedWord = new char[wordToGuess.length()];
        Arrays.fill(guessedWord, '_');
        this.lives = MAX_LIVES;
        this.guessedLetters = new HashSet<>();
    }

    private String chooseRandomWord() throws IOException {
        List<String> words = Files.readAllLines(Paths.get(WORDS_FILE));
        Random rand = new Random();
        return words.get(rand.nextInt(words.size())).toLowerCase();
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Hangman Game!");
        
        while (lives > 0 && !String.valueOf(guessedWord).equals(wordToGuess)) {
            System.out.println("\n" + HANGMAN_STATES[MAX_LIVES - lives]);
            System.out.println("Current word: " + String.valueOf(guessedWord));
            System.out.println("Remaining lives: " + lives);
            System.out.print("Enter a letter or try to guess the whole word: ");
            String input = scanner.nextLine().toLowerCase();
            
            if (input.length() == 1) {
                processLetter(input.charAt(0));
            } else if (input.length() > 1) {
                processWord(input);
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
        
        System.out.println("\n" + HANGMAN_STATES[MAX_LIVES - lives]);
        
        if (String.valueOf(guessedWord).equals(wordToGuess)) {
            System.out.println("Congratulations! You guessed the word: " + wordToGuess);
        } else {
            System.out.println("You lost! The word was: " + wordToGuess);
        }
    }

    private void processLetter(char letter) {
        if (guessedLetters.contains(letter)) {
            System.out.println("You have already entered this letter.");
            return;
        }
        guessedLetters.add(letter);
        
        if (wordToGuess.indexOf(letter) >= 0) {
            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == letter) {
                    guessedWord[i] = letter;
                }
            }
        } else {
            lives--;
            System.out.println("Wrong letter! You lost one life.");
        }
    }

    private void processWord(String word) {
        if (word.equals(wordToGuess)) {
            guessedWord = wordToGuess.toCharArray();
        } else {
            lives--;
            System.out.println("Wrong word! You lost one life.");
        }
    }

    public static void main(String[] args) {
        try {
            new Hangman().play();
        } catch (IOException e) {
            System.out.println("Error loading words file: " + e.getMessage());
        }
    }
}
