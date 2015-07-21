package models;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import models.PostEntity.PostType;
import models.TeamMemberEntity.MemberStatus;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import protocols.Feed;
import utils.JsonUtils;

@Entity
@Table(name = "feeds")
public class FeedEntity extends GenericModel {
	public static enum FeedTarget {
		TEAM,
		MANAGER,
		BOARD,
		O_TEAM,
		O_MANAGER,
		O_BOARD,
		T_MEMBER;
	}

	public static enum FeedType {
		JOIN_TEAM(0, EnumSet.of(FeedTarget.MANAGER)), // 멤버 -> 팀 가입 요청
		ACCEPT_MEMBER(1, EnumSet.of(FeedTarget.T_MEMBER, FeedTarget.BOARD)), // 멤버 -> 팀 요청, 팀이 승락
		DENY_MEMBER(2, EnumSet.of(FeedTarget.T_MEMBER)), // 멤버 -> 팀 요청, 팀이 거절
		DENY_TEAM(3, EnumSet.of(FeedTarget.MANAGER)), // 팀 -> 멤버 요청, 멤버가 거절
		LEAVE_TEAM(4, EnumSet.of(FeedTarget.BOARD)), // 팀 나가기

		REQUEST_MATCH(5, EnumSet.of(FeedTarget.O_MANAGER)), // 어웨이 -> 홈경기 요청
		INVITE_TEAM(6, EnumSet.of(FeedTarget.O_MANAGER)), // 홈 -> 어웨이 요청
		MATCHING_COMPLETED(7, EnumSet.of(FeedTarget.TEAM, FeedTarget.BOARD, FeedTarget.O_TEAM, FeedTarget.O_BOARD)), // 경기 완성
		DENY_MATCH(8, EnumSet.of(FeedTarget.O_MANAGER)), // 요청 거절

		DO_SURVEY(9, EnumSet.of(FeedTarget.T_MEMBER)), // 참불조사
		SET_SCORE(10, EnumSet.of(FeedTarget.MANAGER, FeedTarget.O_MANAGER)), // 결과 입력해라
		ACCEPT_SCORE(11, EnumSet.of(FeedTarget.O_MANAGER)), // 결과 승인해라
		SCORE_COMPLETED(12, EnumSet.of(FeedTarget.BOARD, FeedTarget.O_BOARD)); // 결과 입력 완료

		private final int value;
		private final EnumSet<FeedTarget> target;

		private FeedType(int value, EnumSet target) {
			this.value = value;
			this.target = target;
		}

		public int value() {
			return value;
		}

		public EnumSet<FeedTarget> target() {
			return target;
		}

		public static FeedType getInstance(int value) {
			for (FeedType status : values())
				if (status.value == value)
					return status;
			return null;
		}
	};

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "team_id")
	private int teamId;

	private int target;

	private int type;

	@Column(length = 1024)
	private String message;

	@Required
	@Column(name = "created_at")
	private Date createdAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

	private FeedEntity() {
	}

	public static void makeFeedAndSave(FeedType type, int teamId, int oTeamId, List<Integer> targetMemberIds,
			Map<String, Object> map) {
		String message = JsonUtils.toJson(map);

		if (type.target.contains(FeedTarget.TEAM)) {
			FeedEntity feed = new FeedEntity();
			feed.teamId = teamId;
			feed.type = type.value;
			feed.message = message;
			feed.target = MemberStatus.MEMBER.value();
			feed.save();
		} else if (type.target.contains(FeedTarget.MANAGER)) {
			FeedEntity feed = new FeedEntity();
			feed.teamId = teamId;
			feed.type = type.value;
			feed.message = message;
			feed.target = MemberStatus.MANAGER.value();
			feed.save();
		}

		if (type.target.contains(FeedTarget.O_TEAM)) {
			FeedEntity feed = new FeedEntity();
			feed.teamId = oTeamId;
			feed.type = type.value;
			feed.message = message;
			feed.target = MemberStatus.MEMBER.value();
			feed.save();
		} else if (type.target.contains(FeedTarget.O_MANAGER)) {
			FeedEntity feed = new FeedEntity();
			feed.teamId = teamId;
			feed.type = type.value;
			feed.message = message;
			feed.target = MemberStatus.MANAGER.value();
			feed.save();
		}

		if (type.target.contains(FeedTarget.T_MEMBER) && targetMemberIds != null) {
			for (Integer targetMemberId : targetMemberIds) {
				FeedEntity feed = new FeedEntity();
				feed.userId = targetMemberId;
				feed.type = type.value;
				feed.message = message;
				feed.save();
			}
		}

		map.put("type", type.value);
		message = JsonUtils.toJson(map);

		if (type.target.contains(FeedTarget.BOARD)) {
			PostEntity post = new PostEntity();
			post.setTeamId(teamId);
			post.setType(PostType.FEED.value());
			post.setExtra(message);
			post.save();
		}

		if (type.target.contains(FeedTarget.O_BOARD)) {
			PostEntity post = new PostEntity();
			post.setTeamId(oTeamId);
			post.setType(PostType.FEED.value());
			post.setExtra(message);
			post.save();
		}
	}

	public Feed toFeed() {
		Feed feed = new Feed();
		feed.setId(id);
		feed.setType(type);
		feed.setMessage(message);
		feed.setCreatedAt(createdAt.getTime());
		return feed;
	}
}
