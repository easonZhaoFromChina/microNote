package com.asiainfo.microNote.comments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiainfo.microNote.comments.domain.entity.Comment;
import com.asiainfo.microNote.comments.domain.entity.CommentRecord;
import com.asiainfo.microNote.comments.pojo.CommentInfo;
import com.asiainfo.microNote.comments.repository.ICommentRecordRepository;
import com.asiainfo.microNote.comments.repository.ICommentRepository;
import com.asiainfo.microNote.comments.service.user.IUserService;

/**
 * 
 * @author yi
 *
 */
@Component
public class SimpleCommentService implements ICommentsService{
	@Autowired
	ICommentRecordRepository commentRecordRepository;
	
	@Autowired
	ICommentRepository commentRepository;
	
	@Autowired
	IUserService userService;
	
	@Override
	public CommentInfo getComment(long targetId, String targetType) {
		//查詢評論
		Comment comment = commentRepository.findByCommentTargetIdAndTargetType(targetId, targetType);
		CommentInfo commentInfo = new CommentInfo();
		
		for(CommentRecord commentRecord : comment.getRecords() ){
			commentRecord.getUserId()
		}
		// 查詢用戶信息獲取品論用戶名
		return commentInfo;
	}
	
	
	@Override
	public boolean addComment(long targetId, String targetType, CommentRecord commentRecord) {
		Comment comment;
		// 判断是否已经有的对象的评论
		comment = commentRepository.findByCommentTargetIdAndTargetType(targetId, targetType);
		// 没有增加一个评论
		if(comment == null)
			comment = commentRepository.save(new Comment(targetId, targetType));
		// 对评论进行增加评论项目
		commentRecord.setComment(comment);
		comment.getRecords().add(commentRecord);
		commentRepository.save(comment);
		return true;
	}

	@Override
	public boolean deleteComment(long commentRecordId) {
		commentRecordRepository.delete(commentRecordId);
		return true;
	}


}
