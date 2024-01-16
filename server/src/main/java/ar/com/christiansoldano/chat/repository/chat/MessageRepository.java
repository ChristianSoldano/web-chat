package ar.com.christiansoldano.chat.repository.chat;

import ar.com.christiansoldano.chat.model.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m " +
            "WHERE m.chat.id = :chatId " +
            "AND m.createdAt < (SELECT msg.createdAt FROM Message msg WHERE msg.id = :messageId) " +
            "ORDER BY m.createdAt DESC " +
            "LIMIT 20"
    )
    List<Message> findPreviousByChatIdAndMessageId(UUID chatId, UUID messageId);

    List<Message> findFirst20ByChat_IdOrderByCreatedAtDesc(UUID chatId);

    Optional<Message> findFirstByChat_IdOrderByCreatedAtDesc(UUID chatId);
}
