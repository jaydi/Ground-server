package tests;

import org.junit.Test;

import protocols.LoginResponse;
import protocols.UserInfoResponse;

import common.TestCommon;

public class UserTest extends TestCommon {
	// private static final String IOS_CERTIFICATE_PATH = "/Users/Steven/workspace/server/cert/aps_development.cer";
	private static final String IOS_CERTIFICATE_PATH = "/Users/Steven/workspace/server/cert/APNS_push.p12";
	private static final String IOS_CERTIFICATE_PASSWORD = "GroundIOS0513";

	@Test
	public void push() throws Exception {
		/*String pushToken = "b32ff6fc93e984f0b6cb536de1fdd820f20a16640041d25b680ec9ca0b724731";

		ApnsService service = APNS.newService().withCert(IOS_CERTIFICATE_PATH, IOS_CERTIFICATE_PASSWORD).withSandboxDestination().build();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("A", "Jenna");
		map.put("B", "Frank");
		String payload = APNS.newPayload().localizedKey("GAME_PLAY_REQUEST_FORMAT").localizedArguments("tom", "jerry").build();
		//String payload = APNS.newPayload().localizedKey("GAME_PLAY_REQUEST_FORMAT").customFields(map).build();
		service.push(pushToken, payload);*/
	}

	@Test
	public void userRegister() throws Exception {
		commonApi.apiRegister("testtest", "123", "123", "", 0, "1.0");
	}

	@Test
	public void login() throws Exception {
		LoginResponse res = commonApi.apiLogin("kyungryeol@gmail.com", "1", "ABCD", "", 0, "1.0");
		assertEquals("steven", res.getName());
	}

	@Test
	public void userInfo() throws Exception {
		UserInfoResponse res = commonApi.apiUserInfo(2);
		assertEquals("lee", res.getUserInfo().getName());
	}

	@Test
	public void 메일보내기() throws Exception {
		// commonApi.apiChangePassword("kyungryeol@gmail.com");
	}

}
