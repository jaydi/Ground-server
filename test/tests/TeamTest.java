package tests;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import protocols.Feed;
import protocols.FeedListResponse;
import protocols.MemberListResponse;
import protocols.PendingListResponse;
import protocols.PendingTeamListResponse;
import protocols.RegisterTeamResponse;
import protocols.SearchTeamResponse;
import protocols.TeamHint;
import protocols.TeamInfo;
import protocols.TeamInfoResponse;
import protocols.TeamListResponse;

import common.TestCommon;

public class TeamTest extends TestCommon {
	@Test
	public void 팀등록() throws Exception {
		commonApi.apiRegisterTeam("team", 10, 10, "abcd");
	}

	@Test
	public void 팀정보() throws Exception {
		TeamInfoResponse res = commonApi.apiTeamInfo(1);
		assertEquals("anb", res.getTeamInfo().getName());
	}

	@Test
	public void 팀탈퇴() throws Exception {
		commonApi.apiLeaveTeam(1);
	}

	@Test
	public void 팀검색() throws Exception {
		SearchTeamResponse res = commonApi.apiSearchTeam("anb2");
		assertEquals(1, res.getTeamList().size());
	}

	@Test
	public void 팀리스트() throws Exception {
		RegisterTeamResponse registerTeamResponse;
		registerTeamResponse = commonApi.apiRegisterTeam("Test1", 10.0, 11.0, null);
		int teamId1 = registerTeamResponse.getTeamId();
		registerTeamResponse = commonApi.apiRegisterTeam("Test2", 10.0, 11.0, null);
		int teamId2 = registerTeamResponse.getTeamId();
		registerTeamResponse = commonApi.apiRegisterTeam("Test3", 10.0, 11.0, null);
		int teamId3 = registerTeamResponse.getTeamId();
		TeamListResponse teamListResponse = commonApi.apiTeamList();
		List<TeamHint> teamList = teamListResponse.getTeamList();
		assertEquals(4, teamList.size());
		assertEquals(1, teamList.get(0).getId());
		assertEquals(teamId1, teamList.get(1).getId());
		assertEquals(teamId2, teamList.get(2).getId());
		assertEquals(teamId3, teamList.get(3).getId());
	}

	@Test
	public void 멤버리스트() throws Exception {
		MemberListResponse response = commonApi.apiMemberList(1);
		assertEquals(2, response.getUserList().size());
		assertEquals(1, response.getUserList().get(0).getId());
		assertEquals(3, response.getUserList().get(1).getId());
	}

	@Test
	public void 팀정보수정() throws Exception {
		commonApi.apiEditTeamProfile(1, "bb", "cc", 12, 12);
		TeamInfoResponse res = commonApi.apiTeamInfo(1);
		TeamInfo teamInfo = res.getTeamInfo();
		assertEquals(1, teamInfo.getId());
		assertEquals(2, teamInfo.getMembersCount());
		assertEquals("anb", teamInfo.getName());
		assertEquals("bb", teamInfo.getImageUrl());
	}

	@Test
	public void 팀가입신청() throws Exception {
		commonApi.apiJoinTeamByUserId3(2);
		PendingListResponse res = commonApi.apiPendingListByUserId2(2);
		assertEquals(1, res.getUserList().size());
		assertEquals(3, res.getUserList().get(0).getId());

		FeedListResponse res2 = commonApi.apiFeedListByUserId2(0);
		assertEquals(1, res2.getFeedList().size());
		Feed feed = res2.getFeedList().get(0);
		assertEquals(0, feed.getType());
		assertEquals("{\"teamName\":\"anb2\",\"memberId\":3,\"memberName\":\"kim\",\"teamId\":2}", feed.getMessage());

		res2 = commonApi.apiFeedListByUserId3(0);
		assertEquals(0, res2.getFeedList().size());
	}

	@Test
	public void 팀가입승인() throws Exception {
		commonApi.apiAcceptMember(1, 2);
		PendingListResponse res = commonApi.apiPendingList(1);
		assertEquals(0, res.getUserList().size());
		TeamInfoResponse res2 = commonApi.apiTeamInfo(1);
		assertEquals(3, res2.getTeamInfo().getMembersCount());
		assertEquals(1980, res2.getTeamInfo().getAvgBirth(), 1e-7);
	}

	@Test
	public void 팀가입거절() throws Exception {
		commonApi.apiDenyMember(1, 2);
		PendingListResponse res = commonApi.apiPendingList(1);
		assertEquals(0, res.getUserList().size());
	}

	@Test
	public void 멤버에게팀가입요청() throws Exception {
		commonApi.apiInviteMemberByUserId2(2, 3);
		PendingTeamListResponse res = commonApi.apiPendingTeamListByUserId3();
		assertEquals(1, res.getTeamHintList().size());
	}

	@Test
	public void 멤버가요청승인() throws Exception {
		commonApi.apiAcceptTeam(2);
		PendingTeamListResponse res = commonApi.apiPendingTeamList();
		assertEquals(0, res.getTeamHintList().size());
		TeamListResponse res2 = commonApi.apiTeamList();
		assertEquals(2, res2.getTeamList().size());
		TeamInfoResponse res3 = commonApi.apiTeamInfo(2);
		assertEquals(2, res3.getTeamInfo().getMembersCount());
		assertEquals(1982, res3.getTeamInfo().getAvgBirth(), 1e-7);
	}

	@Test
	public void 멤버가요청거절() throws Exception {
		commonApi.apiDenyTeam(2);
		PendingTeamListResponse res = commonApi.apiPendingTeamList();
		assertEquals(0, res.getTeamHintList().size());
	}

	@Test
	public void 매니저변경() throws Exception {
		commonApi.apiChangeManager(1, Arrays.asList(1), Arrays.asList(3));
		TeamListResponse res = commonApi.apiTeamList();
		assertEquals(1, res.getTeamList().size());
		assertEquals(false, res.getTeamList().get(0).isManaged());

		TeamListResponse res2 = commonApi.apiTeamListByUser3();
		assertEquals(1, res2.getTeamList().size());
		assertEquals(true, res2.getTeamList().get(0).isManaged());
	}
}
