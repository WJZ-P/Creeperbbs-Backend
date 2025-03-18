package me.wjz.creeperhub.mapper;

import me.wjz.creeperhub.entity.Comment;
import me.wjz.creeperhub.entity.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface PostMapper {
    @Insert("INSERT INTO posts (title, content, user_id,category_id,create_time) " +
            "VALUES (#{title}, #{content}, #{userId},#{categoryId}, #{createTime})")
    void insertPost(Post post);

    @SelectProvider(type = PostSqlBuilder.class, method = "buildQuerySql")
    List<Post> getPost(
            @Param("userId") Long userId,
            @Param("categoryId") Integer categoryId,
            @Param("sortField") String sortField,
            @Param("postType") Integer postType,
            @Param("desc") boolean desc,
            @Param("offset") int offset,
            @Param("size") int size

    );

    @Select("SELECT * FROM posts WHERE id = #{id}")
    Post getPostById(Long id);
    @Select("SELECT MAX(id) FROM posts")
    int getMaxPostId();

    @Insert("INSERT INTO comments (id,post_id, user_id,parent_comment_id, content, create_time) "+
            "VALUES (#{id},#{postId}, #{userId},#{parentCommentId}, #{content}, #{createTime})")
    void insertComment(Comment comment);

    @Select("SELECT user_id FROM posts WHERE id = #{postId}")
    Long getUserIdByPostId(Long postId);

    class PostSqlBuilder {
        private static final Set<String> ALLOW_SORT_FIELDS =
                Set.of("create_time", "like_count", "comment_count");

        public String buildQuerySql(Map<String, Object> params) {
            return new SQL() {{
                SELECT("*");
                FROM("posts");

                // 用户过滤
                if (params.get("userId") != null) {
                    WHERE("user_id = #{userId}");
                }

                // 分类过滤
                if (params.get("categoryId") != null) {
                    WHERE("category_id = #{categoryId}");
                }

                // 帖子类型过滤
                int postType = (int) params.get("postType");
                switch (postType) {
                    case 1:
                        WHERE("status = 1"); // 假设2表示精华帖
                        break;
                    case 2:
                        WHERE("status = 2"); // 假设3表示置顶帖
                        break;
                    default:
                        WHERE("status IN (1,2,3)"); // 普通帖包含所有状态
                }

                // 排序字段校验（防SQL注入）
                String sortField = (String) params.get("sortField");
                if (!ALLOW_SORT_FIELDS.contains(sortField)) {
                    sortField = "create_time"; // 默认排序字段
                }

                // 排序方向
                String direction = (boolean) params.get("desc") ? "DESC" : "ASC";
                ORDER_BY(sortField + " " + direction);

                // 分页参数
                LIMIT("#{size} OFFSET #{offset}");
            }}.toString();
        }
    }
}
