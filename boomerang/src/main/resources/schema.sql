CREATE TABLE template (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_column1 VARCHAR(255), -- TemplateColumn1의 필드1
    template_column2 VARCHAR(255) -- TemplateColumn2의 필드1
);

CREATE TABLE likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    board_id BIGINT,
    is_deleted BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (board_id) REFERENCES board(id)
);