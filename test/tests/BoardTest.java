package tests;

import models.PostEntity.PostType;

import org.junit.Test;

import protocols.Comment;
import protocols.CommentListResponse;
import protocols.Post;
import protocols.PostListResponse;

import common.TestCommon;

public class BoardTest extends TestCommon {
	@Test
	public void 글목록() throws Exception {
		PostListResponse res = commonApi.apiPostList(1, 0);
		assertEquals(1, res.getPostList().size());
		Post post = res.getPostList().get(0);
		assertEquals(1, post.getId());
		assertEquals(1, post.getUserId());
		assertEquals(1, post.getType());
		assertEquals("testPost", post.getMessage());
		assertEquals("steven", post.getUserName());
	}

	@Test
	public void 글쓰기() throws Exception {
		commonApi.apiWritePost(1, PostType.POST.value(), "test", "");
		PostListResponse res = commonApi.apiPostList(1, 0);
		assertEquals(2, res.getPostList().size());
	}

	@Test
	public void 글삭제() throws Exception {
		commonApi.apiRemovePost(1, 1);
		PostListResponse res = commonApi.apiPostList(1, 0);
		assertEquals(0, res.getPostList().size());
	}

	@Test
	public void 댓글목록() throws Exception {
		CommentListResponse res = commonApi.apiCommentList(1, 1);
		assertEquals(1, res.getCommentList().size());
		Comment comment = res.getCommentList().get(0);
		assertEquals(1, comment.getUserId());
		assertEquals("testComment", comment.getMessage());
		assertEquals("steven", comment.getUserName());
	}

	@Test
	public void 댓글쓰기() throws Exception {
		commonApi.apiWriteComment(1, 1, "comment");
		CommentListResponse res = commonApi.apiCommentList(1, 1);
		assertEquals(2, res.getCommentList().size());
		PostListResponse resP = commonApi.apiPostList(1, 0);
		assertEquals(2, resP.getPostList().get(0).getCommentCount());
	}

	@Test
	public void 댓글삭제() throws Exception {
		commonApi.apiRemoveComment(1, 1);
		CommentListResponse res = commonApi.apiCommentList(1, 1);
		assertEquals(0, res.getCommentList().size());
		PostListResponse resP = commonApi.apiPostList(1, 0);
		assertEquals(0, resP.getPostList().get(0).getCommentCount());
	}
}
