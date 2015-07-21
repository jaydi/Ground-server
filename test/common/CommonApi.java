package common;

import java.util.List;

import org.junit.Ignore;

import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.test.FunctionalTest;
import protocols.AcceptMemberRequest;
import protocols.AcceptTeamRequest;
import protocols.ChangeManagerRequest;
import protocols.ChangePasswordRequest;
import protocols.CommentListRequest;
import protocols.CommentListResponse;
import protocols.CreateMatchRequest;
import protocols.CreateMatchResponse;
import protocols.DefaultResponse;
import protocols.DefaultResponse.ResponseCode;
import protocols.DenyMemberRequest;
import protocols.DenyTeamRequest;
import protocols.EditTeamProfileRequest;
import protocols.FeedListRequest;
import protocols.FeedListResponse;
import protocols.InviteMemberRequest;
import protocols.JoinMatchRequest;
import protocols.JoinMemberListRequest;
import protocols.JoinMemberListResponse;
import protocols.JoinTeamRequest;
import protocols.LeaveTeamRequest;
import protocols.LoginRequest;
import protocols.LoginResponse;
import protocols.MatchInfoRequest;
import protocols.MatchInfoResponse;
import protocols.MemberListRequest;
import protocols.MemberListResponse;
import protocols.PendingListRequest;
import protocols.PendingListResponse;
import protocols.PendingTeamListResponse;
import protocols.PostListRequest;
import protocols.PostListResponse;
import protocols.RegisterGroundRequest;
import protocols.RegisterGroundResponse;
import protocols.RegisterRequest;
import protocols.RegisterResponse;
import protocols.RegisterTeamRequest;
import protocols.RegisterTeamResponse;
import protocols.RemoveCommentRequest;
import protocols.RemovePostRequest;
import protocols.SearchGroundRequest;
import protocols.SearchGroundResponse;
import protocols.SearchMatchRequest;
import protocols.SearchMatchResponse;
import protocols.SearchTeamRequest;
import protocols.SearchTeamResponse;
import protocols.TeamInfoRequest;
import protocols.TeamInfoResponse;
import protocols.TeamListResponse;
import protocols.UserInfoRequest;
import protocols.UserInfoResponse;
import protocols.WriteCommentRequest;
import protocols.WritePostRequest;
import protocols.WritePostResponse;
import utils.ProtocolCodec;

@Ignore
public class CommonApi extends FunctionalTest {
	public FeedListResponse apiFeedList(long cur) throws Exception {
		FeedListRequest request = new FeedListRequest();
		request.setCur(cur);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/feed_list", "application/json", body);
		FeedListResponse response = ProtocolCodec.decode(httpResponse.out, FeedListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public FeedListResponse apiFeedListByUserId2(long cur) throws Exception {
		FeedListRequest request = new FeedListRequest();
		request.setCur(cur);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser2(), "/feed_list", "application/json", body);
		FeedListResponse response = ProtocolCodec.decode(httpResponse.out, FeedListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public FeedListResponse apiFeedListByUserId3(long cur) throws Exception {
		FeedListRequest request = new FeedListRequest();
		request.setCur(cur);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser3(), "/feed_list", "application/json", body);
		FeedListResponse response = ProtocolCodec.decode(httpResponse.out, FeedListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public RegisterTeamResponse apiRegisterTeam(String name, double latitude, double longitude, String teamImageUrl) throws Exception {
		RegisterTeamRequest request = new RegisterTeamRequest();
		request.setName(name);
		request.setLatitude(latitude);
		request.setLongitude(longitude);
		request.setTeamImageUrl(teamImageUrl);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/register_team", "application/json", body);
		RegisterTeamResponse response = ProtocolCodec.decode(httpResponse.out, RegisterTeamResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiLeaveTeam(int teamId) throws Exception {
		LeaveTeamRequest request = new LeaveTeamRequest();
		request.setTeamId(teamId);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/leave_team", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public TeamListResponse apiTeamList() throws Exception {
		String body = "";
		Response httpResponse = POST(makeRequestUser1(), "/team_list", "application/json", body);
		TeamListResponse response = ProtocolCodec.decode(httpResponse.out, TeamListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public TeamListResponse apiTeamListByUser3() throws Exception {
		String body = "";
		Response httpResponse = POST(makeRequestUser3(), "/team_list", "application/json", body);
		TeamListResponse response = ProtocolCodec.decode(httpResponse.out, TeamListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public TeamInfoResponse apiTeamInfo(int teamId) throws Exception {
		TeamInfoRequest request = new TeamInfoRequest();
		request.setTeamId(teamId);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/team_info", "application/json", body);
		TeamInfoResponse response = ProtocolCodec.decode(httpResponse.out, TeamInfoResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public CreateMatchResponse apiCreateMatch(int teamId, int groundId, int awayTeamId, long startTime, long endTime, String description, boolean open) throws Exception {
		CreateMatchRequest request = new CreateMatchRequest();
		request.setTeamId(teamId);
		request.setGroundId(groundId);
		request.setAwayTeamId(awayTeamId);
		request.setStartTime(startTime);
		request.setEndTime(endTime);
		request.setDescription(description);
		request.setOpen(open);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/create_match", "application/json", body);
		CreateMatchResponse response = ProtocolCodec.decode(httpResponse.out, CreateMatchResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public RegisterGroundResponse apiRegisterGround(String name, String address, double longitude, double latitude) throws Exception {
		RegisterGroundRequest request = new RegisterGroundRequest();
		request.setName(name);
		request.setAddress(address);
		request.setLongitude(longitude);
		request.setLatitude(latitude);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/register_ground", "application/json", body);
		RegisterGroundResponse response = ProtocolCodec.decode(httpResponse.out, RegisterGroundResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public MatchInfoResponse apiMatchInfo(long matchId, int teamId) throws Exception {
		MatchInfoRequest request = new MatchInfoRequest();
		request.setMatchId(matchId);
		request.setTeamId(teamId);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/match_info", "application/json", body);
		MatchInfoResponse response = ProtocolCodec.decode(httpResponse.out, MatchInfoResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiJoinMatch(long matchId, int teamId, boolean join) throws Exception {
		JoinMatchRequest request = new JoinMatchRequest();
		request.setMatchId(matchId);
		request.setTeamId(teamId);
		request.setJoin(join);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/join_match", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public SearchTeamResponse apiSearchTeam(String name) throws Exception {
		SearchTeamRequest request = new SearchTeamRequest();
		request.setName(name);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/search_team", "application/json", body);
		SearchTeamResponse response = ProtocolCodec.decode(httpResponse.out, SearchTeamResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public SearchGroundResponse apiSearchGround(String name) throws Exception {
		SearchGroundRequest request = new SearchGroundRequest();
		request.setName(name);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/search_ground", "application/json", body);
		SearchGroundResponse response = ProtocolCodec.decode(httpResponse.out, SearchGroundResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public JoinMemberListResponse apiJoinMemberList(long matchId, int teamId) throws Exception {
		JoinMemberListRequest request = new JoinMemberListRequest();
		request.setMatchId(matchId);
		request.setTeamId(teamId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/join_member_list", "application/json", body);
		JoinMemberListResponse response = ProtocolCodec.decode(httpResponse.out, JoinMemberListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public MemberListResponse apiMemberList(int teamId) throws Exception {
		MemberListRequest request = new MemberListRequest();
		request.setTeamId(teamId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/member_list", "application/json", body);
		MemberListResponse response = ProtocolCodec.decode(httpResponse.out, MemberListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiEditTeamProfile(int teamId, String imageUrl, String address, double longitude, double latitude) throws Exception {
		EditTeamProfileRequest request = new EditTeamProfileRequest();
		request.setTeamId(teamId);
		request.setImageUrl(imageUrl);
		request.setAddress(address);
		request.setLongitude(longitude);
		request.setLatitude(latitude);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/edit_team_profile", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiJoinTeam(int teamId) throws Exception {
		JoinTeamRequest request = new JoinTeamRequest();
		request.setTeamId(teamId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/join_team", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiJoinTeamByUserId3(int teamId) throws Exception {
		JoinTeamRequest request = new JoinTeamRequest();
		request.setTeamId(teamId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser3(), "/join_team", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public PendingListResponse apiPendingList(int teamId) throws Exception {
		PendingListRequest request = new PendingListRequest();
		request.setTeamId(teamId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/pending_list", "application/json", body);
		PendingListResponse response = ProtocolCodec.decode(httpResponse.out, PendingListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public PendingListResponse apiPendingListByUserId2(int teamId) throws Exception {
		PendingListRequest request = new PendingListRequest();
		request.setTeamId(teamId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser2(), "/pending_list", "application/json", body);
		PendingListResponse response = ProtocolCodec.decode(httpResponse.out, PendingListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiAcceptMember(int teamId, int memberId) throws Exception {
		AcceptMemberRequest request = new AcceptMemberRequest();
		request.setTeamId(teamId);
		request.setMemberId(memberId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/accept_member", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiDenyMember(int teamId, int memberId) throws Exception {
		DenyMemberRequest request = new DenyMemberRequest();
		request.setTeamId(teamId);
		request.setMemberId(memberId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/deny_member", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiInviteMember(int teamId, int userId) throws Exception {
		InviteMemberRequest request = new InviteMemberRequest();
		request.setTeamId(teamId);
		request.setUserId(userId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/invite_member", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiInviteMemberByUserId2(int teamId, int userId) throws Exception {
		InviteMemberRequest request = new InviteMemberRequest();
		request.setTeamId(teamId);
		request.setUserId(userId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser2(), "/invite_member", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public PendingTeamListResponse apiPendingTeamList() throws Exception {
		String body = "";
		Response httpResponse = POST(makeRequestUser1(), "/pending_team_list", "application/json", body);
		PendingTeamListResponse response = ProtocolCodec.decode(httpResponse.out, PendingTeamListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public PendingTeamListResponse apiPendingTeamListByUserId2() throws Exception {
		String body = "";
		Response httpResponse = POST(makeRequestUser2(), "/pending_team_list", "application/json", body);
		PendingTeamListResponse response = ProtocolCodec.decode(httpResponse.out, PendingTeamListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public PendingTeamListResponse apiPendingTeamListByUserId3() throws Exception {
		String body = "";
		Response httpResponse = POST(makeRequestUser3(), "/pending_team_list", "application/json", body);
		PendingTeamListResponse response = ProtocolCodec.decode(httpResponse.out, PendingTeamListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiAcceptTeam(int teamId) throws Exception {
		AcceptTeamRequest request = new AcceptTeamRequest();
		request.setTeamId(teamId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/accept_team", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiDenyTeam(int teamId) throws Exception {
		DenyTeamRequest request = new DenyTeamRequest();
		request.setTeamId(teamId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/deny_team", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiChangeManager(int teamId, List<Integer> oldManagerIdList, List<Integer> newManagerIdList) throws Exception {
		ChangeManagerRequest request = new ChangeManagerRequest();
		request.setTeamId(1);
		request.setOldManagerIdList(oldManagerIdList);
		request.setNewManagerIdList(newManagerIdList);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/change_manager", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public WritePostResponse apiWritePost(int teamId, int type, String message, String extra) throws Exception {
		WritePostRequest request = new WritePostRequest();
		request.setTeamId(teamId);
		request.setType(type);
		request.setMessage(message);
		request.setExtra(extra);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/write_post", "application/json", body);
		WritePostResponse response = ProtocolCodec.decode(httpResponse.out, WritePostResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public PostListResponse apiPostList(int teamId, long cur) throws Exception {
		PostListRequest request = new PostListRequest();
		request.setTeamId(teamId);
		request.setCur(cur);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/post_list", "application/json", body);
		PostListResponse response = ProtocolCodec.decode(httpResponse.out, PostListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiWriteComment(int teamId, long postId, String message) throws Exception {
		WriteCommentRequest request = new WriteCommentRequest();
		request.setTeamId(teamId);
		request.setPostId(postId);
		request.setMessage(message);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/write_comment", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public CommentListResponse apiCommentList(int teamId, long postId) throws Exception {
		CommentListRequest request = new CommentListRequest();
		request.setTeamId(teamId);
		request.setPostId(postId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/comment_list", "application/json", body);
		CommentListResponse response = ProtocolCodec.decode(httpResponse.out, CommentListResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiRemovePost(int teamId, long postId) throws Exception {
		RemovePostRequest request = new RemovePostRequest();
		request.setTeamId(teamId);
		request.setPostId(postId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/remove_post", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiRemoveComment(long postId, int commentId) throws Exception {
		RemoveCommentRequest request = new RemoveCommentRequest();
		request.setPostId(postId);
		request.setCommentId(commentId);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/remove_comment", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public SearchMatchResponse apiSearchMatch(long startTime, long endTime, double longitude, double latitude, int distance) throws Exception {
		SearchMatchRequest request = new SearchMatchRequest();
		request.setStartTime(startTime);
		request.setEndTime(endTime);
		request.setLongitude(longitude);
		request.setLatitude(latitude);
		request.setDistance(distance);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/search_match", "application/json", body);
		SearchMatchResponse response = ProtocolCodec.decode(httpResponse.out, SearchMatchResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public RegisterResponse apiRegister(String email, String password, String deviceUuid, String pushToken, int os, String appVer) throws Exception {
		RegisterRequest request = new RegisterRequest();
		request.setEmail(email);
		request.setPassword(password);
		request.setPushToken(pushToken);
		request.setDeviceUuid(deviceUuid);
		request.setOs(os);
		request.setAppVer(appVer);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/register", "application/json", body);
		RegisterResponse response = ProtocolCodec.decode(httpResponse.out, RegisterResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public LoginResponse apiLogin(String email, String password, String deviceUuid, String pushToken, int os, String appVer) throws Exception {
		LoginRequest request = new LoginRequest();
		request.setEmail(email);
		request.setPassword(password);
		request.setPushToken(pushToken);
		request.setDeviceUuid(deviceUuid);
		request.setOs(os);
		request.setAppVer(appVer);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/login", "application/json", body);
		LoginResponse response = ProtocolCodec.decode(httpResponse.out, LoginResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public UserInfoResponse apiUserInfo(int userId) throws Exception {
		UserInfoRequest request = new UserInfoRequest();
		request.setUserId(userId);
		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/user_info", "application/json", body);
		UserInfoResponse response = ProtocolCodec.decode(httpResponse.out, UserInfoResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	public DefaultResponse apiChangePassword(String email) throws Exception {
		ChangePasswordRequest request = new ChangePasswordRequest();
		request.setEmail(email);

		String body = ProtocolCodec.encode(request);
		Response httpResponse = POST(makeRequestUser1(), "/change_password", "application/json", body);
		DefaultResponse response = ProtocolCodec.decode(httpResponse.out, DefaultResponse.class);
		assertEquals(ResponseCode.OK.value(), response.getCode());
		return response;
	}

	private Request makeRequestUser1() {
		Request request = newRequest();
		Http.Header header = new Http.Header("sessionkey", "ABCDE");
		request.headers.put("sessionkey", header);
		return request;
	}

	private Request makeRequestUser2() {
		Request request = newRequest();
		Http.Header header = new Http.Header("sessionkey", "ABCDF");
		request.headers.put("sessionkey", header);
		return request;
	}

	private Request makeRequestUser3() {
		Request request = newRequest();
		Http.Header header = new Http.Header("sessionkey", "ABCDG");
		request.headers.put("sessionkey", header);
		return request;
	}
}
