package com.anudeep.onlineshop.service.impl;

import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anudeep.onlineshop.dto.LoginForm;
import com.anudeep.onlineshop.model.User;
import com.anudeep.onlineshop.model.UserSignupOtp;
import com.anudeep.onlineshop.repository.UserRepository;
import com.anudeep.onlineshop.repository.UserSignupOtpRepository;
import com.anudeep.onlineshop.service.UserService;

/**
 * UserServiceImpl defines the method signatures provided by the UserService
 * interface for the controller to use.
 * <p>
 * Within each method, it uses methods from the UserRepository.
 */
@Service
public class UserServiceImpl implements UserService {

	@Value("${OnetimeCodeLength}")
	private int len;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${server.port}")
	int port;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	private UserSignupOtpRepository userSignupOtpRepository;

	/**
	 * The getAll() retrieves all the users from the db.
	 *
	 * @return All the users.
	 */
	@Override
	public ResponseEntity<List<User>> getAll(HttpSession session,HttpServletRequest request) {

		// is there an active session?
		session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

			// get the user from the session
			User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

			// is the session user valid?
			if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

				// yes, the user is valid

				// is the valid user an admin?
				if (sessionUser.getRole().equals("ROLE_MANAGER")) {

					// yes, the user's an admin; get all users
					return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);

				} else {

					// no, the user is not an admin; deny the request
					return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

				}

			} else {

				// no valid user found
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

			}

		} else {

			// no, there's no active session; return denial response
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

		}

	}

	/**
	 * The getOne() retrieves a user by their email (natural id)from the db.
	 *
	 * @param email
	 * @return The user by email.
	 */
	@Override
	public ResponseEntity<User> getOne(String email, HttpSession session,HttpServletRequest request) {

		// is there an active session?
		session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

			// get the user from the session
			User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

			// is the session user valid?
			if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

				// yes, the user is valid

				// is the valid user an admin?
				if (sessionUser.getRole().equals("ROLE_MANAGER")) {

					// yes, the user's an admin; get all users
					return new ResponseEntity<>(userRepository.findByEmail(email), HttpStatus.OK);

				} else {

					// no, the user is not an admin; deny the request
					return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

				}

			} else {

				// no valid user found
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

			}

		} else {

			// no, there's no active session; return denial response
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

		}

	}

	public ResponseEntity<User> getSessionUser(HttpSession session, HttpServletRequest request) {
		
		// is there an active session?
		System.out.println("Authorization::::::::::::"+request.getHeader("Authorization"));
		session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

			// get the user from the session
			User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

			// is the session user valid?
			if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

				// yes, the user is valid; return the user
				return new ResponseEntity<>(sessionUser, HttpStatus.OK);

			} else {

				// no valid user found
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

			}

		} else {

			// no, there's no active session; return denial response
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

		}

	}

	/**
	 * The register() saves a new user to the db. Used by admins only.
	 *
	 * @param userToAdd
	 * @return The registered user and an http response.
	 */
	@Override
	public ResponseEntity<User> register(User userToAdd, HttpSession session, HttpServletRequest request) {

		// is there an active session?
		session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

			// get the user from the session
			User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

			// is the session user valid?
			if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

				// yes, the user is valid

				// is the valid user an admin?
				if (sessionUser.getRole().equals("ROLE_MANAGER")) {

					// yes, the user's an admin; construct the user and save

					// set the registered user's role
					userToAdd.setRole("ROLE_CUSTOMER");

					return new ResponseEntity<>(userRepository.save(userToAdd), HttpStatus.OK);

				} else {

					// no, the user is not an admin; deny the request
					return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

				}

			} else {

				// no valid user found
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

			}

		} else {

			// no, there's no active session; return denial response
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

		}

	}

	/**
	 * The update() allows admins to update any user in the db, while only letting
	 * registered users and employees update their self.
	 *
	 * @param userToUpdate
	 * @return The updated user and an http response.
	 */
	@Override
	public ResponseEntity<User> update(User userToUpdate, HttpSession session,HttpServletRequest request) {

		// is there an active session?
		session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

			// get the user from the session
			User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

			// is the session user valid?
			if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

				// yes, they're valid

				// is the session user an admin?
				if (sessionUser.getRole().equals("ROLE_MANAGER")) {

					// yes, they're an admin; proceed with request

					// does the user-to-update exist?
					if (userRepository.existsById(userToUpdate.getId())) {

						// yes, the user exists
						return new ResponseEntity<>(userRepository.save(userToUpdate), HttpStatus.OK);

					} else {

						// no, the user-to-update doesn't exist
						return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

					}

					// is the session user registered or an employee?
				} else if (sessionUser.getRole().equals("ROLE_CUSTOMER")
						|| sessionUser.getRole().equals("ROLE_EMPLOYEE")) {

					// yes, they are; they can only update their own profile

					// does the user-to-update exist?
					if (userRepository.existsById(userToUpdate.getId())) {

						// yes, the user exists

						// are they updating their self?
						if (sessionUser.equals(userToUpdate)) {

							// yes, they are; proceed with request
							return new ResponseEntity<>(userRepository.save(userToUpdate), HttpStatus.OK);

						} else {

							// no, they are not; deny the request
							return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

						}

					} else {

						// no, the user-to-update doesn't exist
						return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

					}

				} else {

					// no, they're neither registered nor an employee; deny the request
					return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

				}

			} else {

				// no valid user found
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

			}

		} else {

			// no, there's no active session; return denial response
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

		}

	}

	/**
	 * The delete() deletes a user in the db.
	 *
	 * @param email
	 * @return A http status response verifying or denying the deletion.
	 */
	@Override
	public ResponseEntity<User> delete(String email, HttpSession session,HttpServletRequest request) {

		// is there an active session?
		session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

			// get the user from the session
			User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

			// is the session user valid?
			if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

				// yes, the user is valid

				// is the valid user an admin?
				if (sessionUser.getRole().equals("ROLE_MANAGER")) {

					// does the user with given email exist?
					if (userRepository.findByEmail(email) != null) {

						// yes, the user exists; delete the user
						return new ResponseEntity<>(userRepository.deleteByEmail(email), HttpStatus.OK);

					} else {

						// no, the user doesn't exist; return Not_Found status
						return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

					}

				} else {

					// no, the user is not an admin; deny the request
					return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

				}

			} else {

				// no valid user found
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

			}

		} else {

			// no, there's no active session; return denial response
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

		}

	}

	/**
	 * The login() attempts to find an existing user in the db using the passed
	 * credentials to start an http session.
	 * <p>
	 * An http session is started using the found user data; it returns a negative
	 * http response if the user wasn't found based on the credentials.
	 *
	 * @param credentials
	 * @param session
	 * @return An http status response verifying or denying the start of an http
	 *         session.
	 */
	@Override
	public ResponseEntity<User> login(LoginForm credentials, HttpSession session,HttpServletRequest request) {

		// find the user
		User existingUser = userRepository.findByEmail(credentials.getEmail());

		// was the user found?
		if (existingUser != null) {

			if(existingUser.isUserActive()) {
				// yes, the user was found; is the password valid?]
				if (passwordEncoder.matches(credentials.getPassword(), existingUser.getPassword())) {

					// yes, the password is valid; set the session's user
					session.setAttribute("user", existingUser);

					return new ResponseEntity<>(existingUser, HttpStatus.OK);

				} else {

					// no, the password is not valid

					// create a return-user to display the invalid credentials
					User invalidUser = new User(credentials.getEmail(), credentials.getPassword() + ": not valid");

					return new ResponseEntity<>(invalidUser, HttpStatus.NOT_FOUND);
				}
			}else {
				User invalidUser = new User(credentials.getEmail(), credentials.getPassword() + ": not valid");

				return new ResponseEntity<>(invalidUser, HttpStatus.CONFLICT);
			}
		} else {

			// no, the user was not found

			// create a return user to display the invalid credentials
			User invalidUser = new User(credentials.getEmail() + ": not valid",
					credentials.getPassword() + ": not valid");

			return new ResponseEntity<>(invalidUser, HttpStatus.NOT_FOUND);

		}

	}

	/**
	 * The logout() ends the active http session.
	 *
	 * @param session
	 * @return An http response verifying the session termination.
	 */
	@Override
	public ResponseEntity<String> logout(HttpSession session,HttpServletRequest request) {

		// end the session
		session.invalidate();

		return new ResponseEntity<>("session ended", HttpStatus.OK);

	}

	@Transactional
	public UserSignupOtp generateOTP(String email, User user, HttpServletRequest request) {
		String numbers = "0123456789";
		// Numbers range

		// Randomizer
		Random rndm_method = new Random();

		char[] otp = new char[len];

		for (int i = 0; i < len; i++) {

			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}

		UserSignupOtp userOtp = new UserSignupOtp(email, Integer.parseInt(String.valueOf(otp)));
		userOtp = userSignupOtpRepository.save(userOtp);

		// javaMailSender.send(msg);

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		String htmlMsg = getTemplate("signup", user, String.valueOf(otp), request);
		// mimeMessage.setContent(htmlMsg, "text/html"); /** Use this or below line **/
		try {
			helper.setText(htmlMsg, true);
			helper.setTo(email);
			helper.setSubject("Welcome to Shopping Application");
		} catch (MessagingException e) {

			e.printStackTrace();
		}
		javaMailSender.send(mimeMessage);
		System.out.println("Email Sent");

		return userOtp;
	}

	public String getTemplate(String str1, User user, String otp, HttpServletRequest request) {
		if ("signup".equals(str1)) {

			try {
				/*
				 * ClassLoader cl = this.getClass().getClassLoader(); InputStream inputStream =
				 * cl.getResourceAsStream("com/myname/myapp/config/dao-context.xml");
				 * StringBuilder contentBuilder = new StringBuilder(); try { BufferedReader in =
				 * new BufferedReader(new FileReader(file)); String str; while ((str =
				 * in.readLine()) != null) { contentBuilder.append(str); } in.close(); } catch
				 * (IOException e) { }
				 */
				String content ="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"	xmlns:o=\"urn:schemas-microsoft-com:office:office\"	style=\"width:100%;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\"><head><meta charset=\"UTF-8\">	<meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">		<meta name=\"x-apple-disable-message-reformatting\">			<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">				<meta content=\"telephone=no\" name=\"format-detection\">					<title>New email template 2020-12-12</title> <!--[if (mso 16)]>    <style type=\"text/css\">    a {text-decoration: none;}    </style>    <![endif]-->					<!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]-->					<!--[if gte mso 9]><xml>    <o:OfficeDocumentSettings>    <o:AllowPNG></o:AllowPNG>    <o:PixelsPerInch>96</o:PixelsPerInch>    </o:OfficeDocumentSettings></xml><![endif]-->					<!--[if !mso]><!-- -->					<link						href=\"https://fonts.googleapis.com/css?family=Roboto:400,400i,700,700i\"						rel=\"stylesheet\">						<!--<![endif]-->						<style type=\"text/css\">#outlook a {	padding: 0;}.ExternalClass {	width: 100%;}.ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font,	.ExternalClass td, .ExternalClass div {	line-height: 100%;}.es-button {	mso-style-priority: 100 !important;	text-decoration: none !important;}a[x-apple-data-detectors] {	color: inherit !important;	text-decoration: none !important;	font-size: inherit !important;	font-family: inherit !important;	font-weight: inherit !important;	line-height: inherit !important;}.es-desk-hidden {	display: none;	float: left;	overflow: hidden;	width: 0;	max-height: 0;	line-height: 0;	mso-hide: all;}@media only screen and (max-width:600px) {	p, ul li, ol li, a {		font-size: 16px !important;		line-height: 150% !important	}	h1 {		font-size: 30px !important;		text-align: center;		line-height: 120% !important	}	h2 {		font-size: 26px !important;		text-align: center;		line-height: 120% !important	}	h3 {		font-size: 20px !important;		text-align: center;		line-height: 120% !important	}	h1 a {		font-size: 30px !important	}	h2 a {		font-size: 26px !important	}	h3 a {		font-size: 20px !important	}	.es-menu td a {		font-size: 14px !important	}	.es-header-body p, .es-header-body ul li, .es-header-body ol li,		.es-header-body a {		font-size: 14px !important	}	.es-footer-body p, .es-footer-body ul li, .es-footer-body ol li,		.es-footer-body a {		font-size: 14px !important	}	.es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a		{		font-size: 12px !important	}	*[class=\"gmail-fix\"] {		display: none !important	}	.es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 {		text-align: center !important	}	.es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 {		text-align: right !important	}	.es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 {		text-align: left !important	}	.es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img {		display: inline !important	}	.es-button-border {		display: inline-block !important	}	.es-btn-fw {		border-width: 10px 0px !important;		text-align: center !important	}	.es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right {		width: 100% !important	}	.es-content table, .es-header table, .es-footer table, .es-content,		.es-footer, .es-header {		width: 100% !important;		max-width: 600px !important	}	.es-adapt-td {		display: block !important;		width: 100% !important	}	.adapt-img {		width: 100% !important;		height: auto !important	}	.es-m-p0 {		padding: 0px !important	}	.es-m-p0r {		padding-right: 0px !important	}	.es-m-p0l {		padding-left: 0px !important	}	.es-m-p0t {		padding-top: 0px !important	}	.es-m-p0b {		padding-bottom: 0 !important	}	.es-m-p20b {		padding-bottom: 20px !important	}	.es-mobile-hidden, .es-hidden {		display: none !important	}	tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden {		width: auto !important;		overflow: visible !important;		float: none !important;		max-height: inherit !important;		line-height: inherit !important	}	tr.es-desk-hidden {		display: table-row !important	}	table.es-desk-hidden {		display: table !important	}	td.es-desk-menu-hidden {		display: table-cell !important	}	.es-menu td {		width: 1% !important	}	table.es-table-not-adapt, .esd-block-html table {		width: auto !important	}	table.es-social {		display: inline-block !important	}	table.es-social td {		display: inline-block !important	}	a.es-button, button.es-button {		font-size: 20px !important;		display: inline-block !important	}}</style></head><body	style=\"width: 100%; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; padding: 0; Margin: 0\">	<div class=\"es-wrapper-color\" style=\"background-color: #FFFFFF\">		<!--[if gte mso 9]>			<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">				<v:fill type=\"tile\" color=\"#ffffff\"></v:fill>			</v:background>		<![endif]-->		<table class=\"es-wrapper\"			style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; padding: 0; Margin: 0; width: 100%; height: 100%; background-repeat: repeat; background-position: center top\"			width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">			<tr style=\"border-collapse: collapse\">				<td valign=\"top\" style=\"padding: 0; Margin: 0\">					<table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\"						align=\"center\"						style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; table-layout: fixed !important; width: 100%\">						<tr style=\"border-collapse: collapse\">							<td class=\"es-adaptive\" align=\"center\"								style=\"padding: 0; Margin: 0\">								<table class=\"es-content-body\"									style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: transparent; width: 600px\"									cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\"									align=\"center\">									<tr style=\"border-collapse: collapse\">										<td align=\"left\" style=\"padding: 10px; Margin: 0\">											<!--[if mso]><table style=\"width:580px\"><tr><td style=\"width:280px\" valign=\"top\"><![endif]-->											<table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\"												align=\"left\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; float: left\">												<tr style=\"border-collapse: collapse\">													<td align=\"left\"														style=\"padding: 0; Margin: 0; width: 280px\">														<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															role=\"presentation\"															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">																													</table>													</td>												</tr>											</table> <!--[if mso]></td><td style=\"width:20px\"></td><td style=\"width:280px\" valign=\"top\"><![endif]-->											<table class=\"es-right\" cellspacing=\"0\" cellpadding=\"0\"												align=\"right\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; float: right\">												<tr style=\"border-collapse: collapse\">													<td align=\"left\"														style=\"padding: 0; Margin: 0; width: 280px\">														<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															role=\"presentation\"															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">																													</table>													</td>												</tr>											</table> <!--[if mso]></td></tr></table><![endif]-->										</td>									</tr>								</table>							</td>						</tr>					</table>					<table class=\"es-header\" cellspacing=\"0\" cellpadding=\"0\"						align=\"center\"						style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; table-layout: fixed !important; width: 100%; background-color: transparent; background-repeat: repeat; background-position: center top\">						<tr style=\"border-collapse: collapse\">							<td align=\"center\" style=\"padding: 0; Margin: 0\">								<table class=\"es-header-body\"									style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: transparent; width: 600px\"									cellspacing=\"0\" cellpadding=\"0\" align=\"center\">									<tr style=\"border-collapse: collapse\">										<td align=\"left\" style=\"padding: 0; Margin: 0\">											<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">												<tr style=\"border-collapse: collapse\">													<td valign=\"top\" align=\"center\"														style=\"padding: 0; Margin: 0; width: 600px\">														<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															role=\"presentation\"															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">															<tr style=\"border-collapse: collapse\">																<td align=\"center\"																	style=\"padding: 0; Margin: 0; padding-bottom: 20px; font-size: 0px\"><a																	href=\"https://spacex.com\" target=\"_blank\"																	style=\"-webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; font-size: 14px; text-decoration: none; color: #F6A1B4\"><img																		src=\"https://opfkds.stripocdn.email/content/guids/193ee463-3449-4fb4-920e-51b01c64cceb/images/94861607802853893.png\"																		alt																		style=\"display: block; border: 0; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic\"																		width=\"154\"></a></td>															</tr>														</table>													</td>												</tr>											</table>										</td>									</tr>								</table>							</td>						</tr>					</table>					<table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\"						align=\"center\"						style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; table-layout: fixed !important; width: 100%\">						<tr style=\"border-collapse: collapse\">							<td align=\"center\" style=\"padding: 0; Margin: 0\">								<table class=\"es-content-body\"									style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: transparent; width: 600px\"									cellspacing=\"0\" cellpadding=\"0\" align=\"center\">									<tr style=\"border-collapse: collapse\">										<td align=\"left\" style=\"padding: 0; Margin: 0\">											<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">												<tr style=\"border-collapse: collapse\">													<td valign=\"top\" align=\"center\"														style=\"padding: 0; Margin: 0; width: 600px\">														<table															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: separate; border-spacing: 0px; border-radius: 3px; background-color: #FCFCFC\"															width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															bgcolor=\"#fcfcfc\" role=\"presentation\">															<tr style=\"border-collapse: collapse\">																<td class=\"es-m-txt-l\" align=\"left\"																	style=\"padding: 0; Margin: 0; padding-left: 20px; padding-right: 20px; padding-top: 30px\"><h2																		style=\"Margin: 0; line-height: 31px; mso-line-height-rule: exactly; font-family: roboto, 'helvetica neue', helvetica, arial, sans-serif; font-size: 26px; font-style: normal; font-weight: normal; color: #333333\">Welcome!</h2></td>															</tr>															<tr style=\"border-collapse: collapse\">																<td bgcolor=\"#fcfcfc\" align=\"left\"																	style=\"padding: 0; Margin: 0; padding-top: 10px; padding-left: 20px; padding-right: 20px\"><p																		style=\"Margin: 0; -webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-size: 14px; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; line-height: 21px; color: #333333\">Hi																		NAMETOREPLACE, We’re glad you’re here! You can enjoy purchases																		and discover new products from Shopping App																		Shop&nbsp;every week.&nbsp;</p></td>															</tr>														</table>													</td>												</tr>											</table>										</td>									</tr>									<tr style=\"border-collapse: collapse\">										<td											style=\"padding: 0; Margin: 0; padding-left: 20px; padding-right: 20px; padding-top: 30px; background-color: #FCFCFC\"											bgcolor=\"#fcfcfc\" align=\"left\">											<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">												<tr style=\"border-collapse: collapse\">													<td valign=\"top\" align=\"center\"														style=\"padding: 0; Margin: 0; width: 560px\">														<table															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: separate; border-spacing: 0px; border-color: #EFEFEF; border-style: solid; border-width: 1px; border-radius: 3px; background-color: #FFFFFF\"															width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															bgcolor=\"#ffffff\" role=\"presentation\">															<tr style=\"border-collapse: collapse\">																<td align=\"center\"																	style=\"padding: 0; Margin: 0; padding-bottom: 15px; padding-top: 20px\"><h3																		style=\"Margin: 0; line-height: 22px; mso-line-height-rule: exactly; font-family: roboto, 'helvetica neue', helvetica, arial, sans-serif; font-size: 18px; font-style: normal; font-weight: normal; color: #333333\">Your																		account information:</h3></td>															</tr>															<tr style=\"border-collapse: collapse\">																<td align=\"center\" style=\"padding: 0; Margin: 0\"><p																		style=\"Margin: 0; -webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-size: 16px; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; line-height: 24px; color: #64434A\">Name:																		NAMETOREPLACE</p>																	<p																		style=\"Margin: 0; -webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-size: 16px; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; line-height: 24px; color: #64434A\">Email:																		EMAILTOREPLACE</p></td>															</tr>															<tr style=\"border-collapse: collapse\">																<td align=\"center\"																	style=\"Margin: 0; padding-left: 10px; padding-right: 10px; padding-top: 20px; padding-bottom: 20px\"><span																	class=\"es-button-border\"																	style=\"border-style: solid; border-color: transparent; background: #F8F3EF none repeat scroll 0% 0%; border-width: 0px; display: inline-block; border-radius: 3px; width: auto\"><a																		href=\"ACTIVATIONURLTOREPLACE\" class=\"es-button\"																		target=\"_blank\"																		style=\"mso-style-priority: 100 !important; text-decoration: none; -webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-family: roboto, 'helvetica neue', helvetica, arial, sans-serif; font-size: 17px; color: #64434A; border-style: solid; border-color: #F8F3EF; border-width: 10px 20px 10px 20px; display: inline-block; background: #F8F3EF none repeat scroll 0% 0%; border-radius: 3px; font-weight: normal; font-style: normal; line-height: 20px; width: auto; text-align: center\">Click																			Here to Activate User</a></span></td>															</tr>														</table>													</td>												</tr>											</table>										</td>									</tr>								</table>							</td>						</tr>					</table>					<table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\"						align=\"center\"						style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; table-layout: fixed !important; width: 100%\">						<tr style=\"border-collapse: collapse\">							<td align=\"center\" style=\"padding: 0; Margin: 0\">								<table class=\"es-content-body\"									style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: #FCFCFC; width: 600px\"									cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#fcfcfc\"									align=\"center\">									<tr style=\"border-collapse: collapse\">										<td align=\"left\"											style=\"Margin: 0; padding-left: 20px; padding-right: 20px; padding-bottom: 25px; padding-top: 40px\">											<!--[if mso]><table style=\"width:560px\" cellpadding=\"0\"                             cellspacing=\"0\"><tr><td style=\"width:274px\" valign=\"top\"><![endif]-->											<table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\"												align=\"left\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; float: left\">												<tr style=\"border-collapse: collapse\">													<td class=\"es-m-p0r es-m-p20b\" align=\"center\"														style=\"padding: 0; Margin: 0; width: 254px\">														<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">															<tr style=\"border-collapse: collapse\">																<td align=\"center\"																	style=\"padding: 0; Margin: 0; display: none\"></td>															</tr>														</table>													</td>													<td class=\"es-hidden\"														style=\"padding: 0; Margin: 0; width: 20px\"></td>												</tr>											</table> <!--[if mso]></td><td style=\"width:133px\" valign=\"top\"><![endif]-->											<table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\"												align=\"left\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; float: left\">												<tr style=\"border-collapse: collapse\">													<td class=\"es-m-p20b\" align=\"center\"														style=\"padding: 0; Margin: 0; width: 133px\">														<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">															<tr style=\"border-collapse: collapse\">																<td align=\"center\"																	style=\"padding: 0; Margin: 0; display: none\"></td>															</tr>														</table>													</td>												</tr>											</table> <!--[if mso]></td><td style=\"width:20px\"></td><td style=\"width:133px\" valign=\"top\"><![endif]-->											<table class=\"es-right\" cellspacing=\"0\" cellpadding=\"0\"												align=\"right\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; float: right\">												<tr style=\"border-collapse: collapse\">													<td align=\"center\"														style=\"padding: 0; Margin: 0; width: 133px\">														<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">															<tr style=\"border-collapse: collapse\">																<td align=\"center\"																	style=\"padding: 0; Margin: 0; display: none\"></td>															</tr>														</table>													</td>												</tr>											</table> <!--[if mso]></td></tr></table><![endif]-->										</td>									</tr>								</table>							</td>						</tr>					</table>					<table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\"						align=\"center\"						style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; table-layout: fixed !important; width: 100%\">						<tr style=\"border-collapse: collapse\">							<td align=\"center\" style=\"padding: 0; Margin: 0\">								<table class=\"es-content-body\"									style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: transparent; width: 600px\"									cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\"									align=\"center\">									<tr style=\"border-collapse: collapse\">										<td align=\"left\" style=\"padding: 0; Margin: 0\">											<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">												<tr style=\"border-collapse: collapse\">													<td valign=\"top\" align=\"center\"														style=\"padding: 0; Margin: 0; width: 600px\">														<table															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: #FFF4F7\"															width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															bgcolor=\"#fff4f7\" role=\"presentation\">															<tr style=\"border-collapse: collapse\">																<td align=\"center\"																	style=\"Margin: 0; padding-bottom: 5px; padding-top: 20px; padding-left: 20px; padding-right: 20px\"><h3																		style=\"Margin: 0; line-height: 22px; mso-line-height-rule: exactly; font-family: roboto, 'helvetica neue', helvetica, arial, sans-serif; font-size: 18px; font-style: normal; font-weight: normal; color: #333333\">Let's																		get social</h3></td>															</tr>														</table>													</td>												</tr>											</table>										</td>									</tr>								</table>							</td>						</tr>					</table>					<table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\"						align=\"center\"						style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; table-layout: fixed !important;\">						<tr style=\"border-collapse: collapse\">							<td style=\"padding: 0; Margin: 0; background-color: #666666\"								bgcolor=\"#666666\" align=\"center\">								<table class=\"es-content-body\"									style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px; background-color: transparent; width: 600px\"									cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\"									align=\"center\">									<tr style=\"border-collapse: collapse\">										<td align=\"left\" style=\"padding: 0; Margin: 0\">											<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"												style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">												<tr style=\"border-collapse: collapse\">													<td valign=\"top\" align=\"center\"														style=\"padding: 0; Margin: 0; width: 600px\">														<table															style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: separate; border-spacing: 0px; background-color: #FFF4F7; border-radius: 3px\"															width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"															bgcolor=\"#fff4f7\" role=\"presentation\">															<tr style=\"border-collapse: collapse\">																<td bgcolor=\"#fff4f7\" align=\"center\"																	style=\"Margin: 0; padding-top: 5px; padding-bottom: 5px; padding-left: 20px; padding-right: 20px; font-size: 0\">																	<table width=\"100%\" height=\"100%\" cellspacing=\"0\"																		cellpadding=\"0\" border=\"0\" role=\"presentation\"																		style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">																		<tr style=\"border-collapse: collapse\">																			<td																				style=\"padding: 0; Margin: 0; border-bottom: 1px solid #FFF4F7; background: #FFFFFF none repeat scroll 0% 0%; height: 1px; width: 100%; margin: 0px\"></td>																		</tr>																	</table>																</td>															</tr>															<tr style=\"border-collapse: collapse\">																<td align=\"center\"																	style=\"Margin: 0; padding-top: 5px; padding-left: 20px; padding-right: 20px; padding-bottom: 25px; font-size: 0\">																	<table class=\"es-table-not-adapt es-social\"																		cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\"																		style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-collapse: collapse; border-spacing: 0px\">																		<tr style=\"border-collapse: collapse\">																			<td valign=\"top\" align=\"center\"																				style=\"padding: 0; Margin: 0; padding-right: 10px\"><a																				target=\"_blank\"																				href=\"https://www.facebook.com/anudshetty/\"																				style=\"-webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; font-size: 14px; text-decoration: none; color: #F6A1B4\"><img																					title=\"Facebook\"																					src=\"https://opfkds.stripocdn.email/content/assets/img/social-icons/logo-black/facebook-logo-black.png\"																					alt=\"Fb\" width=\"32\"																					style=\"display: block; border: 0; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic\"></a></td>																			<td valign=\"top\" align=\"center\"																				style=\"padding: 0; Margin: 0; padding-right: 10px\"><a																				target=\"_blank\"																				href=\"https://twitter.com/elonmusk\"																				style=\"-webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; font-size: 14px; text-decoration: none; color: #F6A1B4\"><img																					title=\"Twitter\"																					src=\"https://opfkds.stripocdn.email/content/assets/img/social-icons/logo-black/twitter-logo-black.png\"																					alt=\"Tw\" width=\"32\"																					style=\"display: block; border: 0; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic\"></a></td>																			<td valign=\"top\" align=\"center\"																				style=\"padding: 0; Margin: 0; padding-right: 10px\"><a																				target=\"_blank\"																				href=\"https://www.instagram.com/u/?hl=en\"																				style=\"-webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; font-size: 14px; text-decoration: none; color: #F6A1B4\"><img																					title=\"Instagram\"																					src=\"https://opfkds.stripocdn.email/content/assets/img/social-icons/logo-black/instagram-logo-black.png\"																					alt=\"Inst\" width=\"32\"																					style=\"display: block; border: 0; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic\"></a></td>																			<td valign=\"top\" align=\"center\"																				style=\"padding: 0; Margin: 0; padding-right: 10px\"><a																				target=\"_blank\"																				href=\"https://www.youtube.com/user/spacex\"																				style=\"-webkit-text-size-adjust: none; -ms-text-size-adjust: none; mso-line-height-rule: exactly; font-family: helvetica, 'helvetica neue', arial, verdana, sans-serif; font-size: 14px; text-decoration: none; color: #F6A1B4\"><img																					title=\"Youtube\"																					src=\"https://opfkds.stripocdn.email/content/assets/img/social-icons/logo-black/youtube-logo-black.png\"																					alt=\"Yt\" width=\"32\"																					style=\"display: block; border: 0; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic\"></a></td>																		</tr>																	</table>																</td>															</tr>														</table>													</td>												</tr>											</table>										</td>									</tr>								</table>							</td>						</tr>					</table>													</td>			</tr>		</table>	</div></body></html>";
				//String content = contentBuilder.toString();
				content = content.replaceAll("NAMETOREPLACE", (user.getFirstName() + " " + user.getLastName()));
				content = content.replaceAll("EMAILTOREPLACE", user.getEmail());
				String url = "https://kaustubhenterprises.herokuapp.com"
						+ "/api/users/activateUser/" + String.valueOf(otp);
				content = content.replaceAll("ACTIVATIONURLTOREPLACE", url);
				return content;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		return "";

	}

	@Override
	@Transactional
	public String createUser(User user, HttpServletRequest request) {
		User user1 = userRepository.findByEmail(user.getEmail());
		if (user1 == null) {
			user.setUserActive(false);
			Random rnd = new Random();
			long n = 10000000 + rnd.nextInt(90000000);
			user.setId(n);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole("ROLE_CUSTOMER");
			user = userRepository.save(user);
			generateOTP(user.getEmail(), user, request);
			return "User Registered Successfully";
		} else if (user1.isUserActive())
			return "User with Email:" + user.getEmail() + " Already  Exists";
		else
			return "User is already registered but not active. Please check activation Email";

	}

	@Override
	public String activateUser(int otp) {
		try { 
			UserSignupOtp userOtp = userSignupOtpRepository.findByOtp(otp);
			 if(userOtp != null) {
				 User user = userRepository.findByEmail(userOtp.getEmailId());
				 if(user == null) {
					 return "Invalid Activation URL"; 
				 }else if(user.isUserActive()) {
					 return "User is already Active, Start Shopping"; 
				 }else{
					 user.setUserActive(true);
					 userRepository.save(user);
					 String str = "<html>\n" + "<header><title>Welcome</title></header>\n <body style='font-size:50px'>\n" + "User Registration Successfully Completed,<a href='http://anudeepui.herokuapp.com'> Click Here</a> to Start Shopping\n" + "</body>\n" + "</html>";
					 return str; 
				 } 
			 }else {
				 return "Invalid Activation URL"; 
			 }
		}catch (Exception e) {
			e.printStackTrace();
			return "Invalid Activation URL"; 
		}
		
		
	}
}
