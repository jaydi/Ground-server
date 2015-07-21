package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.FeedEntity;
import models.FeedEntity.FeedType;
import models.TeamEntity;
import models.TeamMemberEntity;
import models.TeamMemberEntity.MemberStatus;
import models.UserEntity;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.modules.router.Post;
import protocols.AcceptMemberRequest;
import protocols.AcceptTeamRequest;
import protocols.ChangeManagerRequest;
import protocols.DefaultResponse;
import protocols.DefaultResponse.ResponseCode;
import protocols.DenyMemberRequest;
import protocols.DenyTeamRequest;
import protocols.EditTeamProfileRequest;
import protocols.ErrorResponse;
import protocols.InviteMemberRequest;
import protocols.JoinTeamRequest;
import protocols.LeaveTeamRequest;
import protocols.MemberListRequest;
import protocols.MemberListResponse;
import protocols.PendingListRequest;
import protocols.PendingListResponse;
import protocols.PendingTeamListResponse;
import protocols.RegisterTeamRequest;
import protocols.RegisterTeamResponse;
import protocols.SearchTeamNearbyRequest;
import protocols.SearchTeamNearbyResponse;
import protocols.SearchTeamRequest;
import protocols.SearchTeamResponse;
import protocols.TeamHint;
import protocols.TeamHintRequest;
import protocols.TeamHintResponse;
import protocols.TeamInfo;
import protocols.TeamInfoRequest;
import protocols.TeamInfoResponse;
import protocols.TeamListResponse;
import protocols.User;
import utils.ProtocolCodec;

public class TeamController extends ApplicationController {
	@Post("/register_team")
	public static void registerTeam(String body) throws Exception {
		int userId = user.getId();

		RegisterTeamRequest request = ProtocolCodec.decode(body, RegisterTeamRequest.class);
		Logger.info(request.toString());

		String name = request.getName();
		String address = request.getAddress();
		double longitude = request.getLongitude();
		double latitude = request.getLatitude();
		String teamImageUrl = request.getTeamImageUrl();

		if (StringUtils.isEmpty(name))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		TeamEntity team = new TeamEntity();
		team.setName(name);
		team.setAddress(address);
		team.setLatitude(latitude);
		team.setLongitude(longitude);
		team.setImageUrl(teamImageUrl);
		team.setMemberCount(1);
		team.setTotalBirth(user.getBirthYear());
		team.save();

		TeamMemberEntity member = new TeamMemberEntity();

		member.setTeamId(team.getId());
		member.setUserId(userId);
		member.setJoinedAt(new Date());
		member.setStatus(MemberStatus.MANAGER.value());
		member.save();

		RegisterTeamResponse response = new RegisterTeamResponse();
		response.setTeamId(team.getId());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/edit_team_profile")
	public static void editTeamProfile(String body) throws Exception {
		int userId = user.getId();

		EditTeamProfileRequest request = ProtocolCodec.decode(body, EditTeamProfileRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		String address = request.getAddress();
		double longitude = request.getLongitude();
		double latitude = request.getLatitude();
		String imageUrl = request.getImageUrl();

		TeamEntity team = checkAndFindTeam(teamId);
		checkTeamManager(teamId, userId);

		if (!StringUtils.isEmpty(address))
			team.setAddress(address);
		if (longitude > 0 || latitude > 0) {
			team.setLatitude(latitude);
			team.setLongitude(longitude);
		}
		if (!StringUtils.isEmpty(imageUrl))
			team.setImageUrl(imageUrl);
		team.save();

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/member_list")
	public static void memberList(String body) throws Exception {
		int userId = user.getId();

		MemberListRequest request = ProtocolCodec.decode(body, MemberListRequest.class);
		int teamId = request.getTeamId();
		Logger.info(request.toString());

		checkTeamMember(teamId, userId);

		List<Integer> userIdList = new ArrayList<Integer>();
		Map<Integer, Boolean> statusMap = new HashMap<Integer, Boolean>();

		List<TeamMemberEntity> memberList = TeamMemberEntity
				.find("pk.teamId = ? and (status = ? or status = ?)", teamId, MemberStatus.MANAGER.value(), MemberStatus.MEMBER.value()).fetch();
		for (TeamMemberEntity entity : memberList) {
			userIdList.add(entity.getUserId());
			statusMap.put(entity.getUserId(), entity.getStatus() == MemberStatus.MANAGER.value());
		}

		List<UserEntity> userEntityList = UserEntity.findByIds(userIdList);
		List<User> userList = new ArrayList<User>();
		for (UserEntity entity : userEntityList)
			userList.add(entity.toUser(statusMap.get(entity.getId())));

		MemberListResponse response = new MemberListResponse();
		response.setUserList(userList);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/team_list")
	public static void teamList(String body) throws Exception {
		List<TeamHint> teamHintList = user.getTeamHintList();

		TeamListResponse response = new TeamListResponse();
		response.setTeamList(teamHintList);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/search_team_nearby")
	public static void searchTeamNearby(String body) throws Exception {
		SearchTeamNearbyRequest request = ProtocolCodec.decode(body, SearchTeamNearbyRequest.class);
		Logger.info(request.toString());

		double latitude = request.getLatitude();
		double longitude = request.getLongitude();
		int distance = request.getDistance();

		List<TeamInfo> teamList = TeamEntity.searchTeamNearby(latitude, longitude, distance);
		SearchTeamNearbyResponse response = new SearchTeamNearbyResponse();
		response.setTeamList(teamList);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/search_team")
	public static void searchTeam(String body) throws Exception {
		SearchTeamRequest request = ProtocolCodec.decode(body, SearchTeamRequest.class);
		Logger.info(request.toString());

		String name = request.getName();
		StringBuilder sb = new StringBuilder("%");
		sb.append(name);
		sb.append("%");
		List<TeamEntity> teamEntityList = TeamEntity.find("name like ? order by id desc", sb.toString()).fetch(10);

		List<TeamInfo> teamList = new ArrayList<TeamInfo>();
		for (TeamEntity teamEntity : teamEntityList)
			teamList.add(teamEntity.toTeamInfo());

		SearchTeamResponse response = new SearchTeamResponse();
		response.setTeamList(teamList);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/team_info")
	public static void teamInfo(String body) throws Exception {
		TeamInfoRequest request = ProtocolCodec.decode(body, TeamInfoRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		TeamEntity team = checkAndFindTeam(teamId);

		TeamInfoResponse response = new TeamInfoResponse();
		response.setTeamInfo(team.toTeamInfo());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/team_hint")
	public static void teamHint(String body) throws Exception {
		int userId = user.getId();

		TeamHintRequest request = ProtocolCodec.decode(body, TeamHintRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		TeamEntity team = checkAndFindTeam(teamId);
		TeamMemberEntity member = checkAndFindTeamMember(teamId, userId);

		TeamHintResponse response = new TeamHintResponse();
		response.setTeamHint(team.toTeamHint(member.getStatus()));

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/join_team")
	public static void joinTeam(String body) throws Exception {
		int userId = user.getId();
		JoinTeamRequest request = ProtocolCodec.decode(body, JoinTeamRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		TeamEntity team = checkAndFindTeam(teamId);

		TeamMemberEntity member = TeamMemberEntity.findById(new TeamMemberEntity.PK(teamId, userId));
		if (member == null) {
			member = new TeamMemberEntity();
			member.setTeamId(teamId);
			member.setUserId(userId);
		} else if (member.getStatus() == MemberStatus.MANAGER.value() || member.getStatus() == MemberStatus.MEMBER.value())
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "ALREADY MEMBER"));

		member.setLeavedAt(null);
		member.setJoinedAt(null);
		member.setStatus(MemberStatus.PENDING.value());
		member.save();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", teamId);
		map.put("teamName", team.getName());
		map.put("memberId", userId);
		map.put("memberName", user.getName());
		FeedEntity.makeFeedAndSave(FeedType.JOIN_TEAM, teamId, 0, null, map);

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/pending_list")
	public static void pendingList(String body) throws Exception {
		int userId = user.getId();
		PendingListRequest request = ProtocolCodec.decode(body, PendingListRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		checkTeamMember(teamId, userId);

		List<UserEntity> userEntityList = TeamMemberEntity.findPendingUsersByTeamId(teamId);
		List<User> userList = new ArrayList<User>();
		for (UserEntity entity : userEntityList)
			userList.add(entity.toUser(false));

		PendingListResponse response = new PendingListResponse();
		response.setUserList(userList);
		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/accept_member")
	public static void acceptMember(String body) throws Exception {
		int userId = user.getId();
		AcceptMemberRequest request = ProtocolCodec.decode(body, AcceptMemberRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		int memberId = request.getMemberId();

		TeamEntity team = checkAndFindTeam(teamId);
		checkTeamManager(teamId, userId);
		TeamMemberEntity target = checkAndFindTeamMemberStatus(teamId, memberId, MemberStatus.PENDING);

		target.setJoinedAt(new Date());
		target.setStatus(MemberStatus.MEMBER.value());
		target.save();

		UserEntity targetUser = UserEntity.findByUserId(memberId);
		team.addMember(targetUser.getBirthYear());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", teamId);
		map.put("teamName", team.getName());
		map.put("memberId", memberId);
		map.put("memberName", targetUser.getName());
		FeedEntity.makeFeedAndSave(FeedType.ACCEPT_MEMBER, teamId, 0, Arrays.asList(memberId), map);

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/deny_member")
	public static void denyMember(String body) throws Exception {
		int userId = user.getId();
		DenyMemberRequest request = ProtocolCodec.decode(body, DenyMemberRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		int memberId = request.getMemberId();

		checkTeamManager(teamId, userId);
		TeamMemberEntity target = checkAndFindTeamMemberStatus(teamId, memberId, MemberStatus.PENDING);
		target.delete();

		TeamEntity team = TeamEntity.findById(teamId);
		UserEntity user = UserEntity.findByUserId(memberId);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", teamId);
		map.put("teamName", team.getName());
		map.put("memberId", memberId);
		map.put("memberName", user.getName());
		FeedEntity.makeFeedAndSave(FeedType.DENY_MEMBER, 0, 0, Arrays.asList(memberId), map);

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/invite_member")
	public static void inviteMember(String body) throws Exception {
		InviteMemberRequest request = ProtocolCodec.decode(body, InviteMemberRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		int userId = request.getUserId();

		checkTeam(teamId);
		TeamMemberEntity member = TeamMemberEntity.findById(new TeamMemberEntity.PK(teamId, userId));
		if (member != null && (member.getStatus() == MemberStatus.MANAGER.value() || member.getStatus() == MemberStatus.MEMBER.value()))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "already member"));

		member = new TeamMemberEntity();
		member.setTeamId(teamId);
		member.setUserId(userId);
		member.setLeavedAt(null);
		member.setJoinedAt(null);
		member.setStatus(MemberStatus.INVITED.value());
		member.save();

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/pending_team_list")
	public static void pendingTeamList(String body) throws Exception {
		int userId = user.getId();

		List<TeamEntity> teamEntityList = TeamMemberEntity.findPendingTeamsByUserId(userId);
		List<TeamHint> teamHintList = new ArrayList<TeamHint>();
		for (TeamEntity teamEntity : teamEntityList)
			teamHintList.add(teamEntity.toTeamHint(-1));

		PendingTeamListResponse response = new PendingTeamListResponse();
		response.setTeamHintList(teamHintList);
		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/accept_team")
	public static void acceptTeam(String body) throws Exception {
		int userId = user.getId();
		AcceptTeamRequest request = ProtocolCodec.decode(body, AcceptTeamRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();

		TeamEntity team = checkAndFindTeam(teamId);
		TeamMemberEntity member = new TeamMemberEntity();

		member.setUserId(userId);
		member.setTeamId(teamId);
		member.setLeavedAt(null);
		member.setJoinedAt(new Date());
		member.setStatus(MemberStatus.MEMBER.value());
		member.save();

		UserEntity targetUser = UserEntity.findByUserId(userId);
		team.addMember(targetUser.getBirthYear());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", teamId);
		map.put("teamName", team.getName());
		map.put("memberId", userId);
		map.put("memberName", user.getName());
		FeedEntity.makeFeedAndSave(FeedType.ACCEPT_MEMBER, teamId, 0, Arrays.asList(userId), map);

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/deny_team")
	public static void denyTeam(String body) throws Exception {
		int userId = user.getId();
		DenyTeamRequest request = ProtocolCodec.decode(body, DenyTeamRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();

		// TeamMemberEntity target = checkAndFindTeamMemberStatus(teamId, userId, MemberStatus.INVITED);
		TeamMemberEntity target = TeamMemberEntity.findById(new TeamMemberEntity.PK(teamId, userId));
		if (target != null)
			target.delete();

		TeamEntity team = TeamEntity.findById(teamId);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", teamId);
		map.put("teamName", team.getName());
		map.put("memberId", userId);
		map.put("memberName", user.getName());
		FeedEntity.makeFeedAndSave(FeedType.DENY_TEAM, teamId, 0, null, map);

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/leave_team")
	public static void leaveTeam(String body) throws Exception {
		int userId = user.getId();
		LeaveTeamRequest request = ProtocolCodec.decode(body, LeaveTeamRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();

		TeamMemberEntity member = TeamMemberEntity.findById(new TeamMemberEntity.PK(teamId, userId));
		if (member == null || member.getStatus() == MemberStatus.DEACTIVATED.value())
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		member.setStatus(MemberStatus.DEACTIVATED.value());
		member.setLeavedAt(new Date());
		member.save();

		TeamEntity team = TeamEntity.findById(teamId);
		team.removeMember(user.getBirthYear());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", teamId);
		map.put("teamName", team.getName());
		map.put("memberId", userId);
		map.put("memberName", user.getName());
		FeedEntity.makeFeedAndSave(FeedType.LEAVE_TEAM, teamId, 0, null, map);

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/change_manager")
	public static void changeManager(String body) throws Exception {
		int userId = user.getId();
		ChangeManagerRequest request = ProtocolCodec.decode(body, ChangeManagerRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		List<Integer> newManagerIdList = request.getNewManagerIdList();
		List<Integer> oldManagerIdList = request.getOldManagerIdList();

		checkTeamManager(teamId, userId);

		List<TeamMemberEntity> newManagerList = TeamMemberEntity.findByIds(teamId, newManagerIdList);
		for (TeamMemberEntity entity : newManagerList) {
			entity.setStatus(MemberStatus.MANAGER.value());
			entity.save();
		}

		List<TeamMemberEntity> oldManagerList = TeamMemberEntity.findByIds(teamId, oldManagerIdList);
		for (TeamMemberEntity entity : oldManagerList) {
			entity.setStatus(MemberStatus.MEMBER.value());
			entity.save();
		}

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}
}
