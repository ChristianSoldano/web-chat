package ar.com.christiansoldano.chat.repository.chat;

import ar.com.christiansoldano.chat.model.chat.Chat;
import ar.com.christiansoldano.chat.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {

    boolean existsByUser1AndUser2(User user1, User user2);

    @Query(value = "SELECT COUNT(*) > 0 FROM Chat c WHERE c = :chat AND (c.user1 = :user OR c.user2 = :user)")
    boolean userBelongsToChat(Chat chat, User user);

    @Query(value = "SELECT c " +
            "FROM Chat c " +
            "LEFT JOIN Message m ON c = m.chat " +
            "WHERE (c.user1 = :user OR c.user2 = :user) " +
            "AND m IS NOT NULL " +
            "GROUP BY c " +
            "ORDER BY MAX(m.createdAt) DESC")
    List<Chat> findChatsByUser(User user);
}
