package school.cesar.eta.unit;

/**
 * Utility class for Brazilian CPF (Cadastro de Pessoas Físicas) validation.
 *
 * <p>
 * This class provides methods to validate and format Brazilian CPF numbers. CPF is the Brazilian individual taxpayer
 * registry identification.
 *
 * <p>
 * A valid CPF has 11 digits in the format XXX.XXX.XXX-XX where the last two digits are check digits calculated from the
 * first nine.
 *
 * <p>
 * <b>Example usage:</b>
 *
 * <pre>{@code
 * // Validate CPF
 * boolean isValid = CpfValidator.isValid("123.456.789-09");
 *
 * // Format CPF
 * String formatted = CpfValidator.format("12345678909");
 * // Returns: "123.456.789-09"
 *
 * // Remove formatting
 * String unformatted = CpfValidator.unformat("123.456.789-09");
 * // Returns: "12345678909"
 * }</pre>
 *
 * @author CESAR School
 * @since 1.0
 */
public class CpfValidator {

    private CpfValidator() {
        // Utility class, prevent instantiation
    }

    /**
     * Validates a Brazilian CPF number.
     *
     * <p>
     * This method accepts CPF in both formatted (XXX.XXX.XXX-XX) and unformatted (XXXXXXXXXXX) formats.
     *
     * @param cpf
     *            the CPF to validate
     * @return true if the CPF is valid, false otherwise
     */
    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return false;
        }

        // Remove formatting
        String cleanCpf = unformat(cpf);

        // Check length
        if (cleanCpf.length() != 11) {
            return false;
        }

        // Check if all digits are the same (invalid CPFs like 111.111.111-11)
        if (cleanCpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Check if contains only digits
        if (!cleanCpf.matches("\\d{11}")) {
            return false;
        }

        // Calculate first check digit
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cleanCpf.charAt(i)) * (10 - i);
        }
        int firstCheckDigit = 11 - (sum % 11);
        if (firstCheckDigit >= 10) {
            firstCheckDigit = 0;
        }

        // Verify first check digit
        if (firstCheckDigit != Character.getNumericValue(cleanCpf.charAt(9))) {
            return false;
        }

        // Calculate second check digit
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cleanCpf.charAt(i)) * (11 - i);
        }
        int secondCheckDigit = 11 - (sum % 11);
        if (secondCheckDigit >= 10) {
            secondCheckDigit = 0;
        }

        // Verify second check digit
        return secondCheckDigit == Character.getNumericValue(cleanCpf.charAt(10));
    }

    /**
     * Formats a CPF number to the standard format XXX.XXX.XXX-XX.
     *
     * @param cpf
     *            the CPF to format
     * @return the formatted CPF or null if invalid
     */
    public static String format(String cpf) {
        if (cpf == null) {
            return null;
        }

        String cleanCpf = unformat(cpf);

        if (cleanCpf.length() != 11) {
            return null;
        }

        return cleanCpf.substring(0, 3) + "." + cleanCpf.substring(3, 6) + "." + cleanCpf.substring(6, 9) + "-"
                + cleanCpf.substring(9, 11);
    }

    /**
     * Removes formatting from a CPF number.
     *
     * @param cpf
     *            the CPF to unformat
     * @return the CPF with only digits
     */
    public static String unformat(String cpf) {
        if (cpf == null) {
            return "";
        }
        return cpf.replaceAll("[^0-9]", "");
    }

    /**
     * Generates a valid random CPF for testing purposes.
     *
     * <p>
     * <b>Warning:</b> This method should be used only for testing. Never use generated CPFs for real-world
     * applications.
     *
     * @return a valid formatted CPF
     */
    public static String generateRandom() {
        int[] cpf = new int[11];

        // Generate first 9 random digits
        for (int i = 0; i < 9; i++) {
            cpf[i] = (int) (Math.random() * 10);
        }

        // Calculate first check digit
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += cpf[i] * (10 - i);
        }
        cpf[9] = 11 - (sum % 11);
        if (cpf[9] >= 10) {
            cpf[9] = 0;
        }

        // Calculate second check digit
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += cpf[i] * (11 - i);
        }
        cpf[10] = 11 - (sum % 11);
        if (cpf[10] >= 10) {
            cpf[10] = 0;
        }

        // Build string
        StringBuilder result = new StringBuilder();
        for (int digit : cpf) {
            result.append(digit);
        }

        return format(result.toString());
    }
}