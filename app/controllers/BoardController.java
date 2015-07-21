package controllers;

import java.util.List;

import models.CommentEntity;
import models.PostEntity;
import models.TeamEntity;
import models.TeamMemberEntity;
import play.Logger;
import protocols.Comment;
import protocols.CommentListRequest;
import protocols.CommentListResponse;
import protocols.DefaultResponse;
import protocols.DefaultResponse.ResponseCode;
import protocols.ErrorResponse;
import protocols.Post;
import protocols.PostListRequest;
import protocols.PostListResponse;
import protocols.RemoveCommentRequest;
import protocols.RemovePostRequest;
import protocols.WriteCommentRequest;
import protocols.WritePostRequest;
import protocols.WritePostResponse;
import utils.ProtocolCodec;

public class BoardController extends ApplicationController {
	@play.modules.router.Post("/write_post")
	public static void writePost(String body) throws Exception {
		int userId = user.getId();

		WritePostRequest request = ProtocolCodec.decode(body, WritePostRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();

		checkTeamMember(teamId, userId);

		int type = request.getType();
		String extra = request.getExtra();
		String message = request.getMessage();

		PostEntity post = new PostEntity();

		post.setTeamId(teamId);
		post.setUserId(userId);
		post.setType(type);
		post.setMessage(message);
		post.setExtra(extra);
		post.save();

		WritePostResponse response = new WritePostResponse();
		response.setPostId(post.getId());

		renderJSON(ProtocolCodec.encode(response));
	}

	@play.modules.router.Post("/write_comment")
	public static void writeComment(String body) throws Exception {
		int userId = user.getId();

		WriteCommentRequest request = ProtocolCodec.decode(body, WriteCommentRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		long postId = request.getPostId();

		checkTeamMember(teamId, userId);

		PostEntity post = PostEntity.findById(postId);
		if (post == null || post.getTeamId() != teamId)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "no such post"));

		String message = request.getMessage();

		CommentEntity comment = new CommentEntity();
		comment.setUserId(userId);
		comment.setPostId(postId);
		comment.setMessage(message);
		comment.save();
		post.addComment();

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@play.modules.router.Post("/remove_post")
	public static void removePost(String body) throws Exception {
		int userId = user.getId();
		RemovePostRequest request = ProtocolCodec.decode(body, RemovePostRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		long postId = request.getPostId();

		checkTeamMember(teamId, userId);

		PostEntity post = PostEntity.findById(postId);
		if (post == null || post.getTeamId() != teamId)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "no such post"));

		if (userId != post.getUserId())
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "no ownership"));

		post.delete();

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@play.modules.router.Post("/remove_comment")
	public static void removeComment(String body) throws Exception {
		int userId = user.getId();
		RemoveCommentRequest request = ProtocolCodec.decode(body, RemoveCommentRequest.class);
		Logger.info(request.toString());

		long postId = request.getPostId();
		int commentId = request.getCommentId();

		PostEntity post = PostEntity.findById(postId);
		CommentEntity comment = CommentEntity.findById(commentId);
		if (post == null || comment == null || comment.getPostId() != postId)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "no such comment"));

		if (userId != comment.getUserId())
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "no ownership"));

		comment.delete();
		post.removeComment();

		renderJSON(ProtocolCodec.encode(new DefaultResponse()));
	}

	@play.modules.router.Post("/post_list")
	public static void postList(String body) throws Exception {
		int userId = user.getId();

		PostListRequest request = ProtocolCodec.decode(body, PostListRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		TeamEntity team = TeamEntity.findById(teamId);

		if (team == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "no such team"));

		if (TeamMemberEntity.findById(new TeamMemberEntity.PK(teamId, userId)) == null)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "not my team"));

		long cur = request.getCur();
		List<Post> postList = team.getPostList(cur);

		PostListResponse response = new PostListResponse();
		response.setPostList(postList);

		renderJSON(ProtocolCodec.encode(response));
	}

	@play.modules.router.Post("/comment_list")
	public static void commentList(String body) throws Exception {
		int userId = user.getId();

		CommentListRequest request = ProtocolCodec.decode(body, CommentListRequest.class);
		Logger.info(request.toString());

		int teamId = request.getTeamId();
		long postId = request.getPostId();

		checkTeamMember(teamId, userId);

		PostEntity post = PostEntity.findById(postId);
		if (post == null || post.getTeamId() != teamId)
			renderJSON(ErrorResponse.newInstance(ResponseCode.INVALID_PARAMETERS, "no such post"));

		List<Comment> commentList = post.getCommentList();

		CommentListResponse response = new CommentListResponse();
		response.setCommentList(commentList);

		renderJSON(ProtocolCodec.encode(response));
	}
}
