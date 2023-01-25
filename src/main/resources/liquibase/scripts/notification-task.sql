-- liquibase formatted sql

-- changeset nmavro:1
CREATE TABLE notification_task
(
    id                     bigserial primary key,
    notification_chat_id   varchar(255),
    notification_date_time varchar(255),
    notification_text      varchar(255)
);
