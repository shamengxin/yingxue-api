package com.shamengxin.controller;

import com.shamengxin.entity.Comment;
import com.shamengxin.entity.Favorite;
import com.shamengxin.entity.User;
import com.shamengxin.service.CommentService;
import com.shamengxin.service.FavoriteService;
import com.shamengxin.service.UserService;
import com.shamengxin.vo.CommentVO;
import com.shamengxin.vo.Reviewer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
public class UserServiceController {

    private UserService userService;

    private FavoriteService favoriteService;

    private CommentService commentService;

    @Autowired
    public UserServiceController(UserService userService, FavoriteService favoriteService, CommentService commentService) {
        this.userService = userService;
        this.favoriteService = favoriteService;
        this.commentService = commentService;
    }

    @PostMapping("user/comment")
    public void comments(@RequestBody Comment comment){
        commentService.insert(comment);

    }

    /**
     * 查询评论
     * @param page
     * @param rows
     * @param videoId
     * @return
     */
    @GetMapping("user/comments")
    public Map<String,Object> comments(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                @RequestParam(value = "rows",defaultValue = "5") Integer rows,
                                @RequestParam("videoId") Integer videoId){
        Map<String,Object> result = new HashMap<>();
        // 1.获取评论总数
        Long total_counts = commentService.findByVideoIdCounts(videoId);
        result.put("total_count",total_counts);
        List<CommentVO> commentVOS = new ArrayList<>();
        // 2.获取父评论内容
        List<Comment> comments = commentService.findByVideoId(videoId,page,rows);
        // 3.遍历父评论
        comments.forEach(comment -> {
            // 4.将父评论转化为commentVO
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment,commentVO);
            // 5.获取评论作者信息
            Reviewer reviewer = new Reviewer();
            // 6.根据id查询作者信息
            User user = userService.queryById(comment.getUid());
            BeanUtils.copyProperties(user,reviewer);
            // 7.设置评论作者
            commentVO.setReviewer(reviewer);
            // 8.获取子评论
            List<Comment> subComments = commentService.queryByParentId(comment.getId());
            // 9.将子评论转为commentVO
            List<CommentVO> subCommentVOS = new ArrayList<>();
            // 10.遍历子评论
            subComments.forEach(child ->{
                // 11.将子评论转化为commentVO
                CommentVO subCommentVO = new CommentVO();
                BeanUtils.copyProperties(child,subCommentVO);
                // 12.获取评论作者信息
                User subUser = userService.queryById(child.getUid());
                Reviewer subReviewer = new Reviewer();
                BeanUtils.copyProperties(subUser,subReviewer);
                // 13.设置评论者
                subCommentVO.setReviewer(subReviewer);
                subCommentVOS.add(subCommentVO);
            } );
            // 14.设置子评论
            commentVO.setSubComments(subCommentVOS);
            commentVOS.add(commentVO);
        });
        result.put("items",commentVOS);
        return result;
    }

    @GetMapping("user/{id}")
    public User queryById(@PathVariable("id") Integer id) {
        log.info("接收到的用户id：{}", id);
        User user = userService.queryById(id);
        log.info("获取到的用户信息为：{}", user);
        return user;
    }

    @GetMapping("user/favorite")
    public Favorite favorite(@RequestParam("videoId") Integer videoId,@RequestParam("uid") Integer uid) {
        return favoriteService.findByUidAndVideoId(videoId,uid);
    }
}
