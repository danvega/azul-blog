package dev.danvega.azulblog.post;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {

    private final JdbcClient jdbcClient;

    public PostRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Post> findAll() {
        return jdbcClient.sql("SELECT * FROM Post")
                .query(Post.class)
                .list();
    }

    public Optional<Post> findById(String id) {
        return jdbcClient.sql("SELECT * FROM Post WHERE id = :id")
                .param("id", id)
                .query(Post.class)
                .optional();
    }

    public void create(Post post) {
        var updated = jdbcClient.sql("INSERT INTO Post(id,user_id,title,body) values(?,?,?,?)")
                .params(List.of(post.id(), post.userId(), post.title(), post.body()))
                .update();

        Assert.state(updated == 1, "Failed to create post " + post.title());
    }

    public void update(Post post, String id) {
        var updated = jdbcClient.sql("update Post set user_id = ?, title = ?, body = ? where id = ?")
                .params(List.of(post.userId(), post.title(), post.body(), id))
                .update();

        Assert.state(updated == 1, "Failed to update post " + post.title());
    }

    public void delete(String id) {
        var updated = jdbcClient.sql("delete from Post where id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete post " + id);
    }

}
