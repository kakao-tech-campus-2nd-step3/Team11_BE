CREATE TABLE template (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_column1 VARCHAR(255), -- TemplateColumn1의 필드1
    template_column2 VARCHAR(255) -- TemplateColumn2의 필드1
);

CREATE TABLE board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    subtitle VARCHAR(255),
    content VARCHAR(255),
    board_type VARCHAR(255),
    location VARCHAR(255),
    anonymous_status VARCHAR(255)
);
