create table users (
    created_at datetime(6),
    id bigint not null auto_increment,
    email varchar(50) not null,
    name varchar(50) not null,
    encrypted_password varchar(255) not null,
    user_id varchar(255) not null,
    primary key (id)
) engine = InnoDB;