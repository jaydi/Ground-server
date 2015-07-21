package controllers;

import java.util.List;

import models.DeviceEntity;
import models.FeedEntity;
import models.UserEntity;
import models.UserInfoEntity;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import play.Logger;
import play.modules.router.Post;
import protocols.ChangePasswordRequest;
import protocols.DefaultResponse;
import protocols.DefaultResponse.ResponseCode;
import protocols.EditProfileRequest;
import protocols.ErrorResponse;
import protocols.FacebookLoginRequest;
import protocols.FacebookLoginResponse;
import protocols.Feed;
import protocols.FeedListRequest;
import protocols.FeedListResponse;
import protocols.LoginRequest;
import protocols.LoginResponse;
import protocols.LogoutRequest;
import protocols.RegisterRequest;
import protocols.RegisterResponse;
import protocols.RemoveFeedRequest;
import protocols.UserInfo;
import protocols.UserInfoRequest;
import protocols.UserInfoResponse;
import protocols.ValidateEmailRequest;
import protocols.ValidateEmailResponse;
import utils.ProtocolCodec;

public class UserController extends ApplicationController {
	@Post("/validate_email")
	public static void validateEmail(String body) throws Exception {
		ValidateEmailRequest request = ProtocolCodec.decode(body, ValidateEmailRequest.class);
		Logger.info(request.toString());

		String email = request.getEmail();

		UserEntity user = UserEntity.find("email = ?", email).first();
		if (user != null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.DUPLICATED_EMAIL));

		ValidateEmailResponse response = new ValidateEmailResponse();
		response.setValidEmail(true);
		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/register")
	public static void register(String body) throws Exception {
		RegisterRequest request = ProtocolCodec.decode(body, RegisterRequest.class);
		Logger.info(request.toString());

		String email = request.getEmail();
		String password = request.getPassword();
		String pushToken = request.getPushToken();
		int os = request.getOs();
		String appVer = request.getAppVer();
		String deviceUuid = request.getDeviceUuid();

		if (email == null || password == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		UserEntity user = UserEntity.find("email = ?", email).first();
		if (user != null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.DUPLICATED_EMAIL));

		user = new UserEntity();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(email);
		user.save();

		UserInfoEntity userInfo = new UserInfoEntity();
		userInfo.setUserId(user.getId());
		userInfo.save();

		DeviceEntity device = DeviceEntity.find("deviceUuid = ?", deviceUuid).first();
		if (device == null) {
			device = new DeviceEntity();
			device.setDeviceUuid(deviceUuid);
		}

		device.setSessionKey(RandomStringUtils.randomAlphanumeric(40));
		device.setUserId(user.getId());
		device.setOs(os);
		device.setAppVer(appVer);
		device.setPushToken(pushToken);
		device.setActive(true);
		device.save();

		RegisterResponse response = new RegisterResponse();
		response.setSessionKey(device.getSessionKey());
		response.setUserId(user.getId());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/logout")
	public static void logout(String body) throws Exception {
		LogoutRequest request = ProtocolCodec.decode(body, LogoutRequest.class);
		String deviceUuid = request.getDeviceUuid();
		DeviceEntity device = DeviceEntity.find("deviceUuid = ?", deviceUuid).first();
		device.setActive(false);
		device.save();
		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/login")
	public static void login(String body) throws Exception {
		LoginRequest request = ProtocolCodec.decode(body, LoginRequest.class);
		Logger.info(request.toString());

		String email = request.getEmail();
		String password = request.getPassword();
		String pushToken = request.getPushToken();
		int os = request.getOs();
		String appVer = request.getAppVer();
		String deviceUuid = request.getDeviceUuid();

		if (email == null || password == null || deviceUuid == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		UserEntity user = UserEntity.find("email = ?", email).first();
		if (user == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.EMAIL_NOT_EXIST));

		if (!password.equals(user.getPassword()))
			renderJSON(ErrorResponse.newInstance(ResponseCode.WRONG_PASSWORD));

		DeviceEntity device = DeviceEntity.find("deviceUuid = ?", deviceUuid).first();
		if (device == null) {
			device = new DeviceEntity();
			device.setDeviceUuid(deviceUuid);
		}

		device.setSessionKey(RandomStringUtils.randomAlphanumeric(40));
		device.setUserId(user.getId());
		device.setOs(os);
		device.setAppVer(appVer);
		device.setPushToken(pushToken);
		device.setActive(true);
		device.save();

		LoginResponse response = new LoginResponse();
		response.setSessionKey(device.getSessionKey());
		response.setUserId(user.getId());
		response.setName(user.getName());
		response.setImageUrl(user.getProfileImageUrl());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/facebook_login")
	public static void facebookLogin(String body) throws Exception {
		FacebookLoginRequest request = ProtocolCodec.decode(body, FacebookLoginRequest.class);
		Logger.info(request.toString());

		String email = request.getEmail();
		String name = request.getName();
		String imageUrl = request.getImageUrl();
		String pushToken = request.getPushToken();
		int os = request.getOs();
		String appVer = request.getAppVer();
		String deviceUuid = request.getDeviceUuid();
		boolean firstLogin = false;

		if (email == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		UserEntity user = UserEntity.find("email = ?", email).first();
		if (user == null) {
			user = new UserEntity();
			user.setEmail(email);
			user.setPassword("");
			user.setName(name);
			user.setProfileImageUrl(imageUrl);
			user.save();

			UserInfoEntity userInfo = new UserInfoEntity();
			userInfo.setUserId(user.getId());
			userInfo.save();

			firstLogin = true;
		}

		DeviceEntity device = DeviceEntity.find("deviceUuid = ?", deviceUuid).first();
		if (device == null) {
			device = new DeviceEntity();
			device.setDeviceUuid(deviceUuid);
		}

		device.setSessionKey(RandomStringUtils.randomAlphanumeric(40));
		device.setUserId(user.getId());
		device.setOs(os);
		device.setAppVer(appVer);
		device.setPushToken(pushToken);
		device.setActive(true);
		device.save();

		FacebookLoginResponse response = new FacebookLoginResponse();
		response.setSessionKey(device.getSessionKey());
		response.setUserId(user.getId());
		response.setFirstLogin(firstLogin);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/edit_profile")
	public static void editProfile(String body) throws Exception {
		EditProfileRequest request = ProtocolCodec.decode(body, EditProfileRequest.class);
		Logger.info(request.toString());

		String name = request.getName();
		int birthYear = request.getBirthYear();
		int position = request.getPosition();
		int height = request.getHeight();
		int weight = request.getWeight();
		int mainFoot = request.getMainFoot();
		String location1 = request.getLocation1();
		String location2 = request.getLocation2();
		int occupation = request.getOccupation();
		String phoneNumber = request.getPhoneNumber();
		String profileImageUrl = request.getProfileImageUrl();

		UserEntity userEntity = UserEntity.findById(user.getId());
		userEntity.setName(name);
		userEntity.setPhoneNumber(phoneNumber);
		userEntity.setProfileImageUrl(profileImageUrl);
		userEntity.setBirthYear(birthYear);
		userEntity.save();

		UserInfoEntity userInfo = UserInfoEntity.find("userId = ?", user.getId()).first();
		if (userInfo == null) {
			userInfo = new UserInfoEntity();
			userInfo.setUserId(user.getId());
		}
		userInfo.setPosition(position);
		userInfo.setHeight(height);
		userInfo.setWeight(weight);
		userInfo.setMainFoot(mainFoot);
		userInfo.setLocation1(location1);
		userInfo.setLocation2(location2);
		userInfo.setOccupation(occupation);
		userInfo.save();

		DefaultResponse response = new DefaultResponse();
		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/user_info")
	public static void userInfo(String body) throws Exception {
		UserInfoRequest request = ProtocolCodec.decode(body, UserInfoRequest.class);
		Logger.info(request.toString());

		int userId = request.getUserId();
		UserEntity userEntity = UserEntity.findByUserId(userId);
		UserInfoEntity userInfoEntity = UserInfoEntity.findById(userId);
		if (userEntity == null || userInfoEntity == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "no such user"));

		UserInfo userInfo = userEntity.toUserInfo(userInfoEntity);

		UserInfoResponse response = new UserInfoResponse();
		response.setUserInfo(userInfo);
		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/remove_account")
	public static void removeAccount(String body) throws Exception {
		if (!user.isActive())
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "already deactivated"));

		UserEntity userEntity = UserEntity.findById(user.getId());
		userEntity.setActive(false);
		userEntity.save();

		DefaultResponse response = new DefaultResponse();
		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/feed_list")
	public static void feedList(String body) throws Exception {
		FeedListRequest request = ProtocolCodec.decode(body, FeedListRequest.class);
		Logger.info(request.toString());

		long cur = request.getCur();
		List<Feed> feedList = user.getFeedList(cur);

		FeedListResponse response = new FeedListResponse();
		response.setFeedList(feedList);
		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/change_password")
	public static void changePassword(String body) throws Exception {
		ChangePasswordRequest request = ProtocolCodec.decode(body, ChangePasswordRequest.class);
		Logger.info(request.toString());

		String emailAddr = request.getEmail();
		if (emailAddr != null && !emailAddr.equals(user.getEmail())) {
			renderJSON(ErrorResponse.newInstance(ResponseCode.WRONG_EMAIL));
		}

		Email email = new SimpleEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(587);
		email.setTLS(true);
		email.setAuthenticator(new DefaultAuthenticator("altairSoft2013@gmail.com", "altairrnrmfrPwjd"));
		email.setFrom("altairSoft2013@gmail.com");
		email.setSubject("TestMail");
		email.setMsg("This is a test mail ... :-)");
		email.addTo(emailAddr);
		email.send();

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/remove_feed")
	public static void removeFeed(String body) throws Exception {
		int userId = user.getId();
		RemoveFeedRequest request = ProtocolCodec.decode(body, RemoveFeedRequest.class);
		Logger.info(request.toString());

		long feedId = request.getFeedId();

		FeedEntity feed = FeedEntity.findById(feedId);
		if (feed == null || feed.getUserId() != userId)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		feed.delete();

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}
}
