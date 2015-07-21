package controllers;

import models.DeviceEntity;
import models.TeamEntity;
import models.TeamMemberEntity;
import models.TeamMemberEntity.MemberStatus;
import models.UserEntity;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Finally;
import protocols.DefaultResponse.ResponseCode;
import protocols.ErrorResponse;

public class ApplicationController extends Controller {
	protected static UserEntity user = null;

	@Before(unless = { "UserController.validateEmail", "UserController.register", "UserController.login", "UserController.facebookLogin" })
	private static void signUp() {
		String sessionKey = null;
		try {
			sessionKey = request.headers.get("sessionkey").values.get(0);
			DeviceEntity device = DeviceEntity.findBySessionKey(sessionKey);
			if (device == null)
				renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_SESSION_KEY, sessionKey));
			user = UserEntity.findByUserId(device.getUserId());
		} catch (Exception e) {
			user = null;
		}
		if (user == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_SESSION_KEY, String.format("INVALID SESSIONKEY. %s", sessionKey)));
	}

	@Finally
	private static void log(Throwable e) {
		if (e != null)
			Logger.info("Request %s throws exception: %s userId=%d", request.action, e.getMessage(), user == null ? 0 : user.getId());
	}

	protected static void checkTeamManager(int teamId, int userId) {
		checkAndFindTeamManager(teamId, userId);
	}

	protected static void checkTeamMember(int teamId, int userId) {
		checkAndFindTeamMember(teamId, userId);
	}

	protected static void checkTeam(int teamId) {
		checkAndFindTeam(teamId);
	}

	protected static TeamMemberEntity checkAndFindTeamManager(int teamId, int userId) {
		return checkAndFindTeamMemberStatus(teamId, userId, MemberStatus.MANAGER);
	}

	protected static TeamMemberEntity checkAndFindTeamMemberStatus(int teamId, int userId, MemberStatus status) {
		TeamMemberEntity member = checkAndFindTeamMember(teamId, userId);
		if (member.getStatus() != status.value())
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS,
					String.format("WRONG MEMBER STATUS. teamId=%d, userId=%d status=%d expected=%d", teamId, userId, member.getStatus(), status.value())));
		return member;
	}

	protected static TeamMemberEntity checkAndFindTeamMember(int teamId, int userId) {
		if (teamId == 0)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "teamId == 0"));
		if (userId == 0)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "userId == 0"));
		TeamMemberEntity member = TeamMemberEntity.findById(new TeamMemberEntity.PK(teamId, userId));
		if (member == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, String.format("NOT MEMBER. teamId=%d, userId=%d", teamId, userId)));
		if (member.getStatus() == MemberStatus.DEACTIVATED.value())
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, String.format("DEACTIVATE MEMBER. teamId=%d, userId=%d", teamId, userId)));
		return member;
	}

	protected static TeamEntity checkAndFindTeam(int teamId) {
		TeamEntity team = TeamEntity.findById(teamId);
		if (team == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, String.format("NOT EXIST TEAM. teamId=%d", teamId)));
		return team;
	}
}
