package uk.co.eelpieconsulting.common.email;

import javax.mail.Authenticator;

import com.google.common.base.Strings;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

public class EmailService {

    private final static Logger log = Logger.getLogger(EmailService.class);

    private final String smtpHost, smtpUsername, smtpPassword;
    private final int smtpPort;
    private final boolean sslOn;

    public EmailService(String smtpHost, int smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.sslOn = false;
        this.smtpUsername = null;
        this.smtpPassword = null;
    }

    public EmailService(String smtpHost, int smtpPort, boolean sslOn, String smtpUsername, String smtpPassword) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.sslOn = sslOn;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
    }

    public void sendPlaintextEmail(final String subject, final From from, final String to, final String body) throws EmailException {
        sendPlaintextEmail(subject, from, to, body, null);
    }

    public void sendPlaintextEmail(final String subject, final From from, final String to, final String body, String bcc) throws EmailException {
        log.info("Sending plain text email to: " + to + " using " + smtpHost + " port " + smtpPort);
        final Email email = new SimpleEmail();
        setHeaders(email, to, from, subject, bcc);

        email.setMsg(body);

        applySmtpSettings(email);
        email.send();
        log.info("Email sent");
    }

    public void sendHtmlEmail(final String subject, final From from, final String to, final String plaintext, String html) throws EmailException {
        sendHtmlEmail(subject, from, to, plaintext, html, null);
    }

    public void sendHtmlEmail(final String subject, final From from, final String to, final String plaintext, String html, String bcc) throws EmailException {
        log.info("Sending html email to: " + to + " using " + smtpHost + " port " + smtpPort);

        final HtmlEmail email = new HtmlEmail();
        setHeaders(email, to, from, subject, bcc);

        email.setTextMsg(plaintext);
        email.setHtmlMsg(html);

        applySmtpSettings(email);
        email.send();
        log.info("Email sent");
    }

    public boolean isConfigured() {
        return !Strings.isNullOrEmpty(smtpHost) && smtpPort != 0;
    }

    private void setHeaders(final Email email, final String to, final From from, final String subject, String bcc) throws EmailException {
        email.addTo(to);
        email.setSubject(subject);
        if (bcc != null) {
            email.addBcc(bcc);
        }

        if (from.getName() != null) {
            email.setFrom(from.getAddress(), from.getName());
        } else {
            email.setFrom(from.getAddress());
        }
    }

    private void applySmtpSettings(final Email email) {
        email.setHostName(smtpHost);
        email.setSmtpPort(smtpPort);
        email.setSSLOnConnect(sslOn);

        if (smtpUsername != null && smtpPassword != null) {
            final Authenticator authenticator = new DefaultAuthenticator(smtpUsername, smtpPassword);
            email.setAuthenticator(authenticator);
        }
    }

}