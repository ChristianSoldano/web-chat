CREATE TABLE IF NOT EXISTS public.chats
(
    id         UUID                     NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    user1      UUID                     NOT NULL,
    user2      UUID                     NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT UNQ_CHATS_USERS UNIQUE (user1, user2),
    CONSTRAINT FK_CHATS_USER1 FOREIGN KEY (user1) REFERENCES users (id),
    CONSTRAINT FK_CHATS_USER2 FOREIGN KEY (user2) REFERENCES users (id)
);

CREATE TYPE MESSAGE_TYPE AS ENUM ('TEXT', 'AUDIO', 'IMAGE', 'VIDEO', 'FILE');
CREATE CAST (character varying as MESSAGE_TYPE) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS public.messages
(
    id         UUID                     NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    chat_id    UUID                     NOT NULL,
    user_id    UUID                     NOT NULL,
    type       MESSAGE_TYPE             NOT NULL,
    content    TEXT                     NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_MESSAGES_CHAT_ID FOREIGN KEY (chat_id) REFERENCES chats (id),
    CONSTRAINT FK_CHATS_USER_ID FOREIGN KEY (user_id) REFERENCES users (id)
);