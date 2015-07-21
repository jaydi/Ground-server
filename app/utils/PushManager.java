package utils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import models.DeviceEntity;
import models.DeviceEntity.OS;

import org.apache.commons.lang.StringUtils;

import play.Logger;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class PushManager implements Runnable {
	public static enum LocKey {
		DO_SURVEY,
		DO_SURVEY_RIGHT_NOW,
		REQUEST,
		INVITE,
		DENY,
		MATCHING_COMPLETED;
	};

	private static final String GCM_API_KEY = "AIzaSyBUlCTuXTgn4H_oRP_YkdwEU-KcnmwhShQ";
	private static final String IOS_CERTIFICATE_PATH = "/home/anb/ground/server/cert/APNS_push.p12";
	private static final String IOS_CERTIFICATE_PASSWORD = "GroundIOS0513";
	private static final Sender gcmSender = new Sender(GCM_API_KEY);
	private static final ApnsService apnsService = APNS.newService().withCert(IOS_CERTIFICATE_PATH, IOS_CERTIFICATE_PASSWORD).withSandboxDestination().build();

	private static ExecutorService executorService = Executors.newCachedThreadPool();

	public static void sendMessage(DeviceEntity device, int teamId, String teamName, String teamImageUrl, long matchId, String locKey, List<String> locArgs) {
		executorService.execute(new PushManager(device, teamId, teamName, teamImageUrl, matchId, locKey, locArgs));
	}
	
	private DeviceEntity device;
	private int teamId;
	private String teamName;
	private String teamImageUrl;
	private long matchId;
	private String locKey;
	private List<String> locArgs;

	public PushManager(DeviceEntity device, int teamId, String teamName, String teamImageUrl, long matchId, String locKey, List<String> locArgs) {
		super();
		this.device = device;
		this.teamId = teamId;
		this.teamName = teamName;
		this.teamImageUrl = teamImageUrl;
		this.matchId = matchId;
		this.locKey = locKey;
		this.locArgs = locArgs;
	}

	@Override
	public void run() {
		if (device == null || StringUtils.isEmpty(device.getPushToken()) || !device.isActive())
			return;

		if (!device.isPushAlert()) {
			Logger.info("pushAlert false");
			return;
		}

		String pushToken = device.getPushToken();

		if (device.getOs() == OS.ANDROID.value()) {
			String params = JsonUtils.toJson(locArgs);

			Message.Builder mb = new Message.Builder();
			mb.delayWhileIdle(false);
			mb.addData("anb.ground.extra.pushKey", locKey);
			mb.addData("anb.ground.extra.pushParams", params);
			mb.addData("anb.ground.extra.teamId", String.valueOf(teamId));
			mb.addData("anb.ground.extra.teamName", teamName);
			mb.addData("anb.ground.extra.teamImageUrl", (teamImageUrl == null) ? "" : teamImageUrl);
			mb.addData("anb.ground.extra.matchId", String.valueOf(matchId));

			try {
				Result result = gcmSender.send(mb.build(), pushToken, 1);
				if (result.getMessageId() == null) {
					String error = result.getErrorCodeName();
					Logger.error("send push message error. %s", error);
				}
			} catch (IOException e) {
				Logger.error("send push message error. %s", e.getMessage());
				e.printStackTrace();
			}
		} else if (device.getOs() == OS.IOS.value()) {
			String payload = APNS.newPayload().localizedKey(locKey).localizedArguments(locArgs).sound("default").customField("teamId", teamId).customField("matchId", matchId)
					.build();
			apnsService.push(pushToken, payload);
		}
	}
}
