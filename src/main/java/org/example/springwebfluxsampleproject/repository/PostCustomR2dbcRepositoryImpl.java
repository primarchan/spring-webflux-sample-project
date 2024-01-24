package org.example.springwebfluxsampleproject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

@Repository
@RequiredArgsConstructor
public class PostCustomR2dbcRepositoryImpl implements PostCustomR2dbcRepository {

    private final DatabaseClient databaseClient;
    @Override
    public Flux<Post> findAllByUserId(Long userId) {
        String sql = """
                SELECT
                p.id as pid, p.user_id as userId, p.title as title, p.content as content, p.created_at as pCreatedAt, p.updated_at as pUpdatedAt,
                u.id as uid, u.name as name, u.email as email, u.created_at as uCreatedAt, u.updated_at as uUpdatedAt
                FROM posts p
                LEFT JOIN users u
                ON p.user_id = u.id
                WHERE p.user_id = :userId
                """;

        return databaseClient.sql(sql)
                .bind("userId", userId)
                .fetch()
                .all()
                .map(row -> Post.builder()
                        .id((Long) row.get("pid"))
                        .userId((Long) row.get("userId"))
                        .title(row.get("title").toString())
                        .content(row.get("content").toString())
                        .createdAt(((ZonedDateTime) row.get("pCreatedAt")).toLocalDateTime())
                        .createdAt(((ZonedDateTime) row.get("pUpdatedAt")).toLocalDateTime())
                        .user(User.builder()
                                .id((Long) row.get("uid"))
                                .name(row.get("name").toString())
                                .email(row.get("email").toString())
                                .createdAt(((ZonedDateTime) row.get("uCreatedAt")).toLocalDateTime())
                                .createdAt(((ZonedDateTime) row.get("uUpdatedAt")).toLocalDateTime())
                                .build())
                        .build());
    }

}
