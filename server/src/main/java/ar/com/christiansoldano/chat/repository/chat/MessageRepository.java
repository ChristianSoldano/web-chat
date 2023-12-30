package ar.com.christiansoldano.chat.repository.chat;

import ar.com.christiansoldano.chat.model.chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    Page<Message> findByChat_IdOrderByCreatedAtDesc(UUID chatId, Pageable paging);
}
