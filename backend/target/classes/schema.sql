CREATE DATABASE IF NOT EXISTS ai_chat DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ai_chat;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(128),
    nickname VARCHAR(128),
    avatar VARCHAR(255),
    status TINYINT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_username(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS conversation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_conversation_user(user_id),
    CONSTRAINT fk_conversation_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    tokens INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_message_conversation(conversation_id, created_at),
    CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_id) REFERENCES conversation(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- default admin user (password: admin123)
INSERT INTO user (username, password, email, nickname, status)
VALUES ('admin', '$2a$10$Qq2rWjJDQF9JEAH9qqvrmefB3OxEKVDiQJE5VHfgfkhJxLtAmS060', 'admin@example.com', 'Admin', 1)
ON DUPLICATE KEY UPDATE username = username;
