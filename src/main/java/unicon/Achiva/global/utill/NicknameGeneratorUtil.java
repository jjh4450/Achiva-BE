package unicon.Achiva.global.utill;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * NicknameGeneratorUtil: A utility class for generating readable nicknames.
 * Java 11+ compatible.
 * This is a stateless utility class. All generation methods are static.
 */
public final class NicknameGeneratorUtil {

    private static final int DEFAULT_MIN_LEN = 4;
    private static final int DEFAULT_MAX_LEN = 12;
    private static final int GENERATION_GUARD = 10_000;

    // Strategies are stateless, so we can reuse a single instance.
    private static final Strategy SYLLABIC_STRATEGY = new SyllabicStrategy();
    private static final Strategy ADJECTIVE_NOUN_STRATEGY = new AdjectiveNounStrategy();

    /**
     * Private constructor to prevent instantiation.
     */
    private NicknameGeneratorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Generates a single nickname with default settings.
     *
     * @return a randomly generated nickname.
     */
    public static String generate() {
        return generate(DEFAULT_MIN_LEN, DEFAULT_MAX_LEN, true, true, null, ThreadLocalRandom.current());
    }

    /**
     * Generates a single nickname with custom settings.
     *
     * @param minLen      Minimum length of the nickname.
     * @param maxLen      Maximum length of the nickname.
     * @param pascalCase  True to enable PascalCase for multi-token names.
     * @param allowHyphen True to allow hyphens between tokens.
     * @param seen        A set of already used nicknames to ensure uniqueness. Can be null if uniqueness is not required.
     * @param random      The Random instance to use.
     * @return a randomly generated nickname.
     * @throws IllegalStateException if a unique nickname cannot be generated within constraints.
     */
    public static String generate(int minLen, int maxLen, boolean pascalCase, boolean allowHyphen, Set<String> seen, Random random) {
        if (minLen > maxLen) {
            throw new IllegalArgumentException("minLen must be <= maxLen");
        }

        for (int i = 0; i < GENERATION_GUARD; i++) {
            Strategy strategy = pickStrategy(random);
            String raw = strategy.generate(random);
            String shaped = shape(raw, pascalCase, allowHyphen);

            if (shaped.length() >= minLen && shaped.length() <= maxLen) {
                if (seen == null) {
                    return shaped;
                }
                if (seen.add(shaped.toLowerCase(Locale.ROOT))) {
                    return shaped;
                }
            }
        }
        throw new IllegalStateException("Could not generate a unique nickname within constraints");
    }

    /**
     * Generates a batch of nicknames with custom settings.
     *
     * @param count       The number of nicknames to generate.
     * @param minLen      Minimum length of the nicknames.
     * @param maxLen      Maximum length of the nicknames.
     * @param pascalCase  True to enable PascalCase.
     * @param allowHyphen True to allow hyphens.
     * @param ensureUnique True to ensure all nicknames in the batch are unique.
     * @return A list of generated nicknames.
     */
    public static List<String> generateBatch(int count, int minLen, int maxLen, boolean pascalCase, boolean allowHyphen, boolean ensureUnique) {
        if (count <= 0) {
            return Collections.emptyList();
        }

        List<String> out = new ArrayList<>(count);
        Set<String> seen = ensureUnique ? new HashSet<>() : null;
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < count; i++) {
            out.add(generate(minLen, maxLen, pascalCase, allowHyphen, seen, random));
        }
        return out;
    }


    /**
     * Picks a strategy by a fixed weight (3 for Syllabic, 2 for AdjectiveNoun).
     *
     * @param random The Random instance.
     * @return a strategy.
     */
    private static Strategy pickStrategy(Random random) {
        // Default weights: Syllabic(3), AdjectiveNoun(2)
        return random.nextInt(5) < 3 ? SYLLABIC_STRATEGY : ADJECTIVE_NOUN_STRATEGY;
    }

    /**
     * Shapes raw tokens into final output, applying casing and separators.
     *
     * @param raw         raw output (tokens separated by space or hyphen)
     * @param pascalCase  whether to apply PascalCase.
     * @param allowHyphen whether to join with a hyphen.
     * @return final nickname.
     */
    private static String shape(String raw, boolean pascalCase, boolean allowHyphen) {
        String[] tokens = raw.split("[\\s\\-]+");
        if (tokens.length == 0) {
            return "";
        }

        if (pascalCase) {
            return Arrays.stream(tokens)
                    .map(NicknameGeneratorUtil::toTitleCase)
                    .collect(Collectors.joining(allowHyphen ? "-" : ""));
        } else {
            if (tokens.length > 1) {
                String separator = allowHyphen ? "-" : "";
                return String.join(separator, tokens).toLowerCase(Locale.ROOT);
            }
            return tokens[0].toLowerCase(Locale.ROOT);
        }
    }

    /**
     * Converts a string to TitleCase.
     */
    private static String toTitleCase(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT);
    }

    /**
     * Main for quick demo.
     */
    public static void main(String[] args) {
        System.out.println("--- Default Batch (20) ---");
        List<String> nicknames = generateBatch(20, 4, 14, true, true, true);
        nicknames.forEach(System.out::println);

        System.out.println("\n--- Custom Batch (5) ---");
        // Custom generation without PascalCase and hyphens, ensuring uniqueness
        Set<String> seen = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            String name = generate(6, 10, false, false, seen, random);
            System.out.println(name);
        }
    }

    // --- Inner Strategy Classes ---

    /**
     * Strategy interface.
     */
    public interface Strategy {
        String generate(Random rnd);
    }

    /**
     * SyllabicStrategy builds pronounceable pseudo-words.
     */
    public static class SyllabicStrategy implements Strategy {
        private static final String[] VOWELS = {"a", "e", "i", "o", "u", "y"};
        private static final String[] LONG_VOWELS = {"ai", "ea", "ee", "ie", "oa", "oo", "ou", "ue"};
        private static final String[] CONS_SOFT = {"b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "r", "s", "t", "v", "w", "z"};
        private static final String[] CLUSTERS_ONSET = {"bl", "br", "ch", "cl", "cr", "dr", "fl", "fr", "gl", "gr", "pl", "pr", "qu", "sc", "sh", "sk", "sl", "sm", "sn", "sp", "st", "sw", "th", "tr", "wh"};
        private static final String[] CLUSTERS_CODA = {"ch", "ck", "ft", "ld", "lf", "lk", "lm", "lp", "lt", "mp", "nd", "ng", "nk", "nt", "pt", "rd", "rk", "rl", "rm", "rn", "rp", "rt", "sh", "sk", "sp", "st", "th"};

        @Override
        public String generate(Random rnd) {
            int syllables = weighted(rnd, new int[]{1, 2, 3}, new int[]{3, 5, 2});
            StringBuilder sb = new StringBuilder();
            String prevCoda = "";
            for (int i = 0; i < syllables; i++) {
                String onset = pickOnset(rnd, prevCoda);
                String nucleus = pickNucleus(rnd);
                String coda = pickCoda(rnd);
                if (i == syllables - 1 && rnd.nextInt(5) == 0) coda = "";
                sb.append(onset).append(nucleus).append(coda);
                prevCoda = coda;
            }
            String s = sb.toString();
            s = s.replaceAll("([a-z])\\1{2,}", "$1$1"); // remove triple letters
            return s;
        }

        private static String pickOnset(Random rnd, String prevCoda) {
            if (!prevCoda.isEmpty() && rnd.nextInt(4) == 0) return "";
            int r = rnd.nextInt(10);
            if (r < 5) return oneOf(rnd, CONS_SOFT);
            if (r < 9) return oneOf(rnd, CLUSTERS_ONSET);
            return ""; // occasionally start with a vowel
        }

        private static String pickNucleus(Random rnd) {
            return rnd.nextInt(4) == 0 ? oneOf(rnd, LONG_VOWELS) : oneOf(rnd, VOWELS);
        }

        private static String pickCoda(Random rnd) {
            int r = rnd.nextInt(10);
            if (r < 4) return "";
            if (r < 8) return oneOf(rnd, CONS_SOFT);
            return oneOf(rnd, CLUSTERS_CODA);
        }

        private static <T> T oneOf(Random rnd, T[] arr) {
            return arr[rnd.nextInt(arr.length)];
        }

        private static int weighted(Random rnd, int[] values, int[] weights) {
            int total = 0;
            for (int w : weights) total += w;
            int r = rnd.nextInt(total);
            int acc = 0;
            for (int i = 0; i < values.length; i++) {
                acc += weights[i];
                if (r < acc) return values[i];
            }
            return values[0];
        }
    }

    /**
     * AdjectiveNounStrategy combines pleasant adjectives and concrete nouns.
     */
    public static class AdjectiveNounStrategy implements Strategy {
        private static final String[] ADJ = {"brisk", "bright", "calm", "clever", "cosmic", "crisp", "dapper", "eager", "fuzzy", "gentle", "glossy", "humble", "ivory", "jolly", "lofty", "lucky", "mellow", "mint", "nimble", "peachy", "plucky", "prime", "quick", "quiet", "rapid", "rosy", "silky", "snug", "spry", "stellar", "sunny", "tidy", "velvet", "vivid", "whimsical", "zesty"};
        private static final String[] NOUN = {"acorn", "badger", "beacon", "breeze", "candle", "cedar", "comet", "coyote", "dawn", "ember", "finch", "flame", "fox", "harbor", "ivory", "kelp", "kestrel", "lemur", "linx", "lotus", "maple", "meadow", "nebula", "otter", "papaya", "pebble", "piper", "plume", "quartz", "raven", "reef", "rocket", "sable", "sprout", "swift", "thistle", "tiger", "willow", "zephyr"};

        @Override
        public String generate(Random rnd) {
            String a = ADJ[rnd.nextInt(ADJ.length)];
            String n = NOUN[rnd.nextInt(NOUN.length)];
            // Let the shape() method handle the separator
            return a + " " + n;
        }
    }
}