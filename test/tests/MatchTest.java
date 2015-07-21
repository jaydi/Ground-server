package tests;

import org.junit.Test;

import protocols.CreateMatchResponse;
import protocols.RegisterGroundResponse;
import protocols.SearchMatchResponse;

import common.TestCommon;

public class MatchTest extends TestCommon {
	@Test
	public void matchInfo() throws Exception {
		CreateMatchResponse cmResponse = commonApi.apiCreateMatch(1, 1, 0, 1375001418000l, 1375001618000l, "test", true);
		commonApi.apiMatchInfo(cmResponse.getMatchId(), 1);
	}

	@Test
	public void joinMatch() throws Exception {
		CreateMatchResponse cmResponse = commonApi.apiCreateMatch(1, 1, 0, 1375001418000l, 1375001618000l, "test", true);
		long matchId = cmResponse.getMatchId();
		commonApi.apiJoinMatch(matchId, 1, true);
	}

	@Test
	public void joinMemberList() throws Exception {
		CreateMatchResponse cmResponse = commonApi.apiCreateMatch(1, 1, 0, 1375001418000l, 1375001618000l, "test", true);
		long matchId = cmResponse.getMatchId();
		commonApi.apiJoinMemberList(matchId, 1);
	}

	@Test
	public void createGround() throws Exception {
		commonApi.apiRegisterGround("test", "test", 0, 0);
	}

	@Test
	public void serachMatch() throws Exception {
		RegisterGroundResponse res1 = commonApi.apiRegisterGround("test", "test", 100, 100);
		commonApi.apiCreateMatch(1, res1.getGroundId(), 0, 1375001418000l, 1375001618000l, "test", true);
		SearchMatchResponse res2 = commonApi.apiSearchMatch(1375001410000l, 1375001620000l, 100, 100, 100000);
		assertEquals(1, res2.getMatchList().size());
	}
}
