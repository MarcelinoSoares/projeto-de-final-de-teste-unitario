package school.cesar.eta.unit;

/**
 * Email service interface for sending notifications. Used to demonstrate mocking in tests.
 */
public interface EmailService {

    /**
     * Sends a welcome email to a new person.
     *
     * @param email
     *            the recipient email address
     * @param name
     *            the person's name
     */
    void sendWelcomeEmail(String email, String name);

    /**
     * Sends a birthday greeting email.
     *
     * @param email
     *            the recipient email address
     * @param name
     *            the person's name
     * @param age
     *            the person's age
     */
    void sendBirthdayGreeting(String email, String name, int age);

    /**
     * Sends a generic notification email.
     *
     * @param email
     *            the recipient email address
     * @param subject
     *            the email subject
     * @param body
     *            the email body
     */
    void sendNotification(String email, String subject, String body);
}