package uk.co.eelpieconsulting.common.email;

import javax.mail.Authenticator;

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

	public void sendPlaintextEmail(final String subject, final String from, final String body, final String to) throws EmailException {
		log.info("Sending plain text email to: " + to + " using " + smtpHost + " port " + smtpPort);
		final Email email = new SimpleEmail();
		setHeaders(email, to, from, subject);

		email.setMsg(body);

		applySmtpSettings(email);
		email.send();
		log.info("Email sent");
	}

	public void sendHtmlEmail(final String subject, final String from, final String to, final String plaintext, String html) throws EmailException {
		log.info("Sending html email to: " + to + " using " + smtpHost + " port " + smtpPort);

		final HtmlEmail email = new HtmlEmail();
		setHeaders(email, to, from, subject);

		email.setTextMsg(plaintext);
		email.setHtmlMsg(html);

		applySmtpSettings(email);
		email.send();
		log.info("Email sent");
	}

	private void setHeaders(final Email email, final String to, final String from, final String subject) throws EmailException {
		email.addTo(to);
		email.setFrom(from);
		email.setSubject(subject);
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