package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.DeviceEntity;
import models.FeedEntity;
import models.FeedEntity.FeedType;
import models.JoinInfoEntity;
import models.MatchEntity;
import models.MatchEntity.MatchStatus;
import models.MessageEntity;
import models.TeamEntity;
import models.TeamMemberEntity;
import play.Logger;
import play.modules.router.Post;
import protocols.AcceptMatchRequest;
import protocols.AcceptScoreRequest;
import protocols.AcceptScoreResponse;
import protocols.CancelMatchRequest;
import protocols.CancelMatchResponse;
import protocols.CreateMatchRequest;
import protocols.CreateMatchResponse;
import protocols.DefaultResponse;
import protocols.DefaultResponse.ResponseCode;
import protocols.DenyMatchRequest;
import protocols.DenyMatchResponse;
import protocols.ErrorResponse;
import protocols.IMessage;
import protocols.InviteTeamRequest;
import protocols.InviteTeamResponse;
import protocols.JoinMatchRequest;
import protocols.JoinMemberListRequest;
import protocols.JoinMemberListResponse;
import protocols.JoinUser;
import protocols.Match;
import protocols.MatchHistoryRequest;
import protocols.MatchHistoryResponse;
import protocols.MatchInfo;
import protocols.MatchInfoRequest;
import protocols.MatchInfoResponse;
import protocols.MatchListRequest;
import protocols.MatchListResponse;
import protocols.MessageListRequest;
import protocols.MessageListResponse;
import protocols.PushSurveyRequest;
import protocols.PushTargettedSurveyRequest;
import protocols.RequestMatchRequest;
import protocols.RequestMatchResponse;
import protocols.SMatch;
import protocols.SearchMatchRequest;
import protocols.SearchMatchResponse;
import protocols.SetScoreRequest;
import protocols.SetScoreResponse;
import protocols.StartSurveyRequest;
import utils.ProtocolCodec;
import utils.PushManager;
import utils.PushManager.LocKey;

public class MatchController extends ApplicationController {
	@Post("/create_match")
	public static void createMatch(String body) throws Exception {
		int userId = user.getId();

		CreateMatchRequest request = ProtocolCodec.decode(body, CreateMatchRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		int awayTeamId = request.getAwayTeamId();
		int groundId = request.getGroundId();
		long startTime = request.getStartTime();
		long endTime = request.getEndTime();
		String description = request.getDescription();
		boolean open = request.isOpen();

		checkTeamMember(teamId, userId);

		MatchEntity match = new MatchEntity();
		match.setHomeTeamId(teamId);
		match.setGroundId(groundId);
		match.setStartTime(new Date(startTime));
		match.setEndTime(new Date(endTime));

		match.setDescription(description);
		match.setOpen(open);
		if (awayTeamId > 0) {
			match.setAwayTeamId(awayTeamId);
			match.setStatus(MatchStatus.INVITE.value());
		} else
			match.setStatus(MatchStatus.HOME_ONLY.value());

		match.save();

		CreateMatchResponse response = new CreateMatchResponse();
		response.setMatchId(match.getId());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/match_list")
	public static void matchList(String body) throws Exception {
		int userId = user.getId();

		MatchListRequest request = ProtocolCodec.decode(body, MatchListRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();

		checkTeamMember(teamId, userId);

		List<MatchEntity> matchEntityList = MatchEntity.find("(home_team_id = ? or away_team_id = ?) and status < ?", teamId, teamId, MatchStatus.SCORE_COMPLETED.value()).fetch();
		List<Match> matchList = new ArrayList<Match>();
		for (MatchEntity matchEntity : matchEntityList)
			matchList.add(matchEntity.toMatch());

		MatchListResponse response = new MatchListResponse();
		response.setMatchList(matchList);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/match_history")
	public static void matchHistory(String body) throws Exception {
		MatchHistoryRequest request = ProtocolCodec.decode(body, MatchHistoryRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		long cur = request.getCur();
		boolean order = request.isOrder();

		if (teamId == 0)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		List<MatchEntity> matchEntityList;
		if (order)
			matchEntityList = MatchEntity.find("id > ? and (home_team_id = ? or away_team_id = ?) and status = ? order by start_time", cur, teamId, teamId,
					MatchStatus.SCORE_COMPLETED.value()).fetch();
		else
			matchEntityList = MatchEntity.find("id < ? and (home_team_id = ? or away_team_id = ?) and status = ? order by start_time desc", cur, teamId, teamId,
					MatchStatus.SCORE_COMPLETED.value()).fetch();

		List<Match> matchList = new ArrayList<Match>();
		for (MatchEntity matchEntity : matchEntityList)
			matchList.add(matchEntity.toMatch());

		MatchHistoryResponse response = new MatchHistoryResponse();
		response.setMatchList(matchList);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/match_info")
	public static void matchInfo(String body) throws Exception {
		int userId = user.getId();

		MatchInfoRequest request = ProtocolCodec.decode(body, MatchInfoRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();

		if (matchId == 0)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		MatchEntity matchEntity = MatchEntity.findById(matchId);
		if (matchEntity == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		MatchInfo matchInfo = matchEntity.toMatchInfo(userId, teamId);

		MatchInfoResponse response = new MatchInfoResponse();
		response.setMatchInfo(matchInfo);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/invite_team")
	public static void inviteTeam(String body) throws Exception {
		int userId = user.getId();

		InviteTeamRequest request = ProtocolCodec.decode(body, InviteTeamRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int homeTeamId = request.getHomeTeamId();
		int awayTeamId = request.getAwayTeamId();

		checkTeamManager(homeTeamId, userId);

		if (matchId == 0)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();

		if (matchEntity == null || matchEntity.getHomeTeamId() != homeTeamId)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		matchEntity.setStatus(MatchStatus.INVITE.value());
		matchEntity.setAwayTeamId(awayTeamId);
		matchEntity.save();

		TeamEntity homeTeam = TeamEntity.findById(homeTeamId);
		TeamEntity awayTeam = TeamEntity.findById(awayTeamId);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", awayTeam.getId());
		map.put("teamName", awayTeam.getName());
		map.put("requestedTeamId", homeTeam.getId());
		map.put("requestedTeamName", homeTeam.getName());
		map.put("matchId", matchId);
		FeedEntity.makeFeedAndSave(FeedType.INVITE_TEAM, homeTeamId, awayTeamId, null, map);

		List<Integer> awayManagerIds = TeamMemberEntity.findManagerIdsByTeamId(awayTeam.getId());
		List<DeviceEntity> awayManagerDeviceList = DeviceEntity.findByUserIds(awayManagerIds);
		ArrayList<String> locArgs = new ArrayList<String>();
		locArgs.add(awayTeam.getName());
		for (DeviceEntity device : awayManagerDeviceList)
			PushManager.sendMessage(device, awayTeam.getId(), awayTeam.getName(), awayTeam.getImageUrl(), matchId, LocKey.INVITE.name(), locArgs);

		InviteTeamResponse response = new InviteTeamResponse();
		response.setMatchStatus(matchEntity.getStatus());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/request_match")
	public static void requestMatch(String body) throws Exception {
		int userId = user.getId();

		RequestMatchRequest request = ProtocolCodec.decode(body, RequestMatchRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int awayTeamId = request.getAwayTeamId();

		checkTeamManager(awayTeamId, userId);

		if (matchId == 0)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		if (matchEntity.getHomeTeamId() == awayTeamId)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		matchEntity.setStatus(MatchStatus.REQUEST.value());
		matchEntity.setAwayTeamId(awayTeamId);
		matchEntity.save();

		TeamEntity homeTeam = TeamEntity.findById(matchEntity.getHomeTeamId());
		TeamEntity awayTeam = TeamEntity.findById(matchEntity.getAwayTeamId());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", homeTeam.getId());
		map.put("teamName", homeTeam.getName());
		map.put("requestedTeamId", awayTeam.getId());
		map.put("requestedTeamName", awayTeam.getName());
		map.put("matchId", matchId);
		FeedEntity.makeFeedAndSave(FeedType.REQUEST_MATCH, homeTeam.getId(), awayTeam.getId(), null, map);

		List<Integer> homeManagerIds = TeamMemberEntity.findManagerIdsByTeamId(homeTeam.getId());
		List<DeviceEntity> homeManagerDeviceList = DeviceEntity.findByUserIds(homeManagerIds);

		ArrayList<String> locArgs = new ArrayList<String>();
		locArgs.add(homeTeam.getName());
		for (DeviceEntity device : homeManagerDeviceList)
			PushManager.sendMessage(device, homeTeam.getId(), homeTeam.getName(), homeTeam.getImageUrl(), matchId, LocKey.REQUEST.name(), locArgs);

		RequestMatchResponse response = new RequestMatchResponse();
		response.setMatchStatus(matchEntity.getStatus());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/accept_match")
	public static void acceptMatch(String body) throws Exception {
		AcceptMatchRequest request = ProtocolCodec.decode(body, AcceptMatchRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();

		if (matchId == 0)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();

		MatchStatus matchStatus = MatchStatus.getInstance(matchEntity.getStatus());
		if (matchStatus != MatchStatus.INVITE && matchStatus != MatchStatus.REQUEST)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		matchEntity.setStatus(MatchStatus.MATCHING_COMPLETED.value());
		matchEntity.save();

		TeamEntity homeTeam = TeamEntity.findById(matchEntity.getHomeTeamId());
		List<Integer> homeUserIds = TeamMemberEntity.findActiveUserIdsByTeamId(homeTeam.getId());
		TeamEntity awayTeam = TeamEntity.findById(matchEntity.getAwayTeamId());
		List<Integer> awayTeamUserIds = TeamMemberEntity.findActiveUserIdsByTeamId(awayTeam.getId());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("homeTeamId", homeTeam.getId());
		map.put("homeTeamName", homeTeam.getName());
		map.put("awayTeamId", awayTeam.getId());
		map.put("awayTeamName", awayTeam.getName());
		map.put("matchId", matchId);
		FeedEntity.makeFeedAndSave(FeedType.MATCHING_COMPLETED, matchEntity.getHomeTeamId(), matchEntity.getAwayTeamId(), null, map);

		List<DeviceEntity> homeDeviceList = DeviceEntity.findByUserIds(homeUserIds);
		ArrayList<String> locArgs = new ArrayList<String>();
		locArgs.add(homeTeam.getName());
		for (DeviceEntity device : homeDeviceList)
			PushManager.sendMessage(device, homeTeam.getId(), homeTeam.getName(), homeTeam.getImageUrl(), matchId, LocKey.MATCHING_COMPLETED.name(), locArgs);

		List<DeviceEntity> awayDeviceList = DeviceEntity.findByUserIds(awayTeamUserIds);
		locArgs.clear();
		locArgs.add(awayTeam.getName());
		for (DeviceEntity device : awayDeviceList)
			PushManager.sendMessage(device, awayTeam.getId(), awayTeam.getName(), awayTeam.getImageUrl(), matchId, LocKey.MATCHING_COMPLETED.name(), locArgs);

		RequestMatchResponse response = new RequestMatchResponse();
		response.setMatchStatus(matchEntity.getStatus());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/deny_match")
	public static void denyMatch(String body) throws Exception {
		int userId = user.getId();
		DenyMatchRequest request = ProtocolCodec.decode(body, DenyMatchRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();

		checkTeamManager(teamId, userId);

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		int otherTeamId = teamId == matchEntity.getHomeTeamId() ? matchEntity.getAwayTeamId() : matchEntity.getHomeTeamId();
		TeamEntity team = TeamEntity.findById(teamId);
		TeamEntity otherTeam = TeamEntity.findById(otherTeamId);

		MatchStatus matchStatus = MatchStatus.getInstance(matchEntity.getStatus());
		if (matchStatus != MatchStatus.INVITE && matchStatus != MatchStatus.REQUEST)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "invalid match status"));

		matchEntity.setAwayTeamId(0);
		matchEntity.setStatus(MatchStatus.HOME_ONLY.value());
		matchEntity.save();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", otherTeamId);
		map.put("teamName", otherTeam.getName());
		map.put("requestedTeamId", teamId);
		map.put("requestedTeamName", team.getName());
		map.put("matchId", matchId);
		FeedEntity.makeFeedAndSave(FeedType.DENY_MATCH, 0, otherTeamId, null, map);

		List<Integer> otherManagerIds = TeamMemberEntity.findManagerIdsByTeamId(otherTeamId);
		List<DeviceEntity> otherManagerDeviceList = DeviceEntity.findByUserIds(otherManagerIds);
		List<String> locArgs = new ArrayList<String>();
		locArgs.add(otherTeam.getName());
		locArgs.add(team.getName());
		for (DeviceEntity device : otherManagerDeviceList)
			PushManager.sendMessage(device, otherTeam.getId(), otherTeam.getName(), otherTeam.getImageUrl(), matchId, LocKey.DENY.name(), locArgs);

		DenyMatchResponse response = new DenyMatchResponse();
		response.setMatchStatus(matchEntity.getStatus());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/join_match")
	public static void joinMatch(String body) throws Exception {
		int userId = user.getId();

		JoinMatchRequest request = ProtocolCodec.decode(body, JoinMatchRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();
		boolean join = request.isJoin();

		checkTeamMember(teamId, userId);

		if (matchId == 0)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		JoinInfoEntity joinInfo = JoinInfoEntity.findById(new JoinInfoEntity.PK(matchId, teamId, userId));
		if (joinInfo == null) {
			joinInfo = new JoinInfoEntity();
			joinInfo.setMatchId(matchId);
			joinInfo.setTeamId(teamId);
			joinInfo.setUserId(userId);
		}
		joinInfo.setJoined(join);
		joinInfo.save();

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/cancel_match")
	public static void cancelMatch(String body) throws Exception {
		int userId = user.getId();

		CancelMatchRequest request = ProtocolCodec.decode(body, CancelMatchRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();

		checkTeamManager(teamId, userId);

		if (matchId == 0)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null || (matchEntity.getHomeTeamId() != teamId && matchEntity.getAwayTeamId() != teamId)
				|| (MatchStatus.getInstance(matchEntity.getStatus()) != MatchStatus.INVITE && MatchStatus.getInstance(matchEntity.getStatus()) != MatchStatus.REQUEST))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		matchEntity.setAwayTeamId(0);
		matchEntity.setStatus(MatchStatus.HOME_ONLY.value());
		matchEntity.save();

		CancelMatchResponse response = new CancelMatchResponse();
		response.setMatchStatus(matchEntity.getStatus());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/start_survey")
	public static void startSurvey(String body) throws Exception {
		int userId = user.getId();

		StartSurveyRequest request = ProtocolCodec.decode(body, StartSurveyRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();

		checkTeamManager(teamId, userId);

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null || (matchEntity.getHomeTeamId() != teamId && matchEntity.getAwayTeamId() != teamId))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		if (matchEntity.getHomeTeamId() == teamId)
			matchEntity.setHomeSurvey(true);
		else
			matchEntity.setAwaySurvey(true);

		matchEntity.save();

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/set_score")
	public static void setScore(String body) throws Exception {
		int userId = user.getId();

		SetScoreRequest request = ProtocolCodec.decode(body, SetScoreRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();
		int homeScore = request.getHomeScore();
		int awayScore = request.getAwayScore();

		checkTeamManager(teamId, userId);

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null || (matchEntity.getHomeTeamId() != teamId && matchEntity.getAwayTeamId() != teamId))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "not my match"));

		int otherTeamId = 0;
		matchEntity.setHomeScore(homeScore);
		matchEntity.setAwayScore(awayScore);
		if (teamId == matchEntity.getHomeTeamId()) {
			matchEntity.setStatus(MatchStatus.HOME_SCORE.value());
			otherTeamId = matchEntity.getAwayTeamId();
		} else {
			matchEntity.setStatus(MatchStatus.AWAY_SCORE.value());
			otherTeamId = matchEntity.getHomeTeamId();
		}
		matchEntity.save();

		TeamEntity team = TeamEntity.findById(teamId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", teamId);
		map.put("teamName", team.getName());
		map.put("matchId", matchId);
		FeedEntity.makeFeedAndSave(FeedType.ACCEPT_SCORE, 0, otherTeamId, null, map);

		SetScoreResponse response = new SetScoreResponse();
		response.setMatchStatus(matchEntity.getStatus());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/accept_score")
	public static void acceptScore(String body) throws Exception {
		int userId = user.getId();

		AcceptScoreRequest request = ProtocolCodec.decode(body, AcceptScoreRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();

		checkTeamManager(teamId, userId);

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null || (matchEntity.getHomeTeamId() != teamId && matchEntity.getAwayTeamId() != teamId))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		MatchStatus matchStatus = MatchStatus.getInstance(matchEntity.getStatus());
		if (!((matchStatus == MatchStatus.HOME_SCORE && matchEntity.getAwayTeamId() == teamId) || (matchStatus == MatchStatus.AWAY_SCORE && matchEntity.getHomeTeamId() == teamId)))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		matchEntity.setStatus(MatchStatus.SCORE_COMPLETED.value());
		matchEntity.save();

		TeamEntity team = TeamEntity.findById(teamId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", teamId);
		map.put("teamName", team.getName());
		map.put("matchId", matchId);
		FeedEntity.makeFeedAndSave(FeedType.SCORE_COMPLETED, matchEntity.getHomeTeamId(), matchEntity.getAwayTeamId(), null, map);

		AcceptScoreResponse response = new AcceptScoreResponse();
		response.setMatchStatus(matchEntity.getStatus());

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/join_member_list")
	public static void joinMemberList(String body) throws Exception {
		JoinMemberListRequest request = ProtocolCodec.decode(body, JoinMemberListRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null || (matchEntity.getHomeTeamId() != teamId && matchEntity.getAwayTeamId() != teamId))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		List<JoinUser> userList = matchEntity.getJoinUserList(teamId);

		JoinMemberListResponse response = new JoinMemberListResponse();
		response.setUserList(userList);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/search_match")
	public static void searchMatch(String body) throws Exception {
		SearchMatchRequest request = ProtocolCodec.decode(body, SearchMatchRequest.class);
		Logger.info(request.toString());

		long startTime = request.getStartTime();
		long endTime = request.getEndTime();
		double longitude = request.getLongitude();
		double latitude = request.getLatitude();
		int distance = request.getDistance();

		List<SMatch> matchList = MatchEntity.searchMatch(startTime, endTime, longitude, latitude, distance);

		SearchMatchResponse response = new SearchMatchResponse();
		response.setMatchList(matchList);

		renderJSON(ProtocolCodec.encode(response));
	}

	@Post("/push_survey")
	public static void pushSurvey(String body) throws Exception {
		int userId = user.getId();

		PushSurveyRequest request = ProtocolCodec.decode(body, PushSurveyRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();

		checkTeamManager(teamId, userId);

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null || (matchEntity.getHomeTeamId() != teamId && matchEntity.getAwayTeamId() != teamId))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "not my match"));

		TeamEntity team = TeamEntity.findById(teamId);
		if (team == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS));

		List<Integer> userIds = TeamMemberEntity.findNotYetSurveyMemberIds(teamId, matchId);
		List<DeviceEntity> deviceList = DeviceEntity.findByUserIds(userIds);
		ArrayList<String> locArgs = new ArrayList<String>();
		locArgs.add(team.getName());
		for (DeviceEntity device : deviceList)
			PushManager.sendMessage(device, teamId, team.getName(), team.getImageUrl(), matchId, LocKey.DO_SURVEY.name(), locArgs);

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/push_targetted_survey")
	public static void pushTargettedSurvey(String body) throws Exception {
		int userId = user.getId();

		PushTargettedSurveyRequest request = ProtocolCodec.decode(body, PushTargettedSurveyRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();
		List<Integer> pushIds = request.getPushIds();

		checkTeamManager(teamId, userId);

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null || (matchEntity.getHomeTeamId() != teamId && matchEntity.getAwayTeamId() != teamId))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "not my match"));

		TeamEntity team = TeamEntity.findById(teamId);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("teamId", teamId);
		map.put("teamName", team.getName());
		map.put("matchId", matchId);
		FeedEntity.makeFeedAndSave(FeedType.DO_SURVEY, 0, 0, pushIds, map);

		List<DeviceEntity> deviceList = DeviceEntity.findByUserIds(pushIds);
		ArrayList<String> locArgs = new ArrayList<String>();
		locArgs.add(team.getName());
		for (DeviceEntity device : deviceList)
			PushManager.sendMessage(device, teamId, team.getName(), team.getImageUrl(), matchId, LocKey.DO_SURVEY_RIGHT_NOW.name(), locArgs);

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@Post("/message_list")
	public static void messageList(String body) throws Exception {
		MessageListRequest request = ProtocolCodec.decode(body, MessageListRequest.class);
		Logger.info(request.toString());

		long matchId = request.getMatchId();
		int teamId = request.getTeamId();
		long cur = request.getCur();

		MatchEntity matchEntity = MatchEntity.find("id = ?", matchId).first();
		if (matchEntity == null || (matchEntity.getHomeTeamId() != teamId && matchEntity.getAwayTeamId() != teamId))
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "not my match"));

		List<IMessage> messageList = MessageEntity.getIMessageList(matchId, cur);

		MessageListResponse response = new MessageListResponse();
		response.setMessageList(messageList);
		renderJSON(ProtocolCodec.encode(response));
	}
}
