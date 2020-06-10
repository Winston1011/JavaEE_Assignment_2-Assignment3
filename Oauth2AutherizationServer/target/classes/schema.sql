--
-- Spring Security Database Schema
--
SET FOREIGN_KEY_CHECKS=0;
drop table if exists users;
create table users (
    username varchar(256) not null primary key,
    password varchar(256) not null,
    enabled boolean not null
) engine = InnoDb;

drop table if exists authorities;
create table authorities (
    username varchar(256) not null,
    authority varchar(256) not null,
    foreign key (username) references users (username),
    unique index authorities_idx_1 (username, authority)
) engine = InnoDb;

drop table if exists persistent_logins;
create table persistent_logins (
    username varchar(256) not null,
    series varchar(256) primary key,
    token varchar(256) not null,
    last_used timestamp not null
) engine = InnoDb;

drop table if exists `groups`;
create table `groups` (
    id bigint unsigned not null auto_increment primary key,
    group_name varchar(256) not null
) engine = InnoDb;

drop table if exists group_authorities;
create table group_authorities (
    group_id bigint unsigned not null,
    authority varchar(256) not null,
    foreign key (group_id) references `groups` (id)
) engine = InnoDb;

drop table if exists group_members;
create table group_members (
    id bigint unsigned not null auto_increment primary key,
    username varchar(256) not null,
    group_id bigint unsigned not null,
    foreign key (group_id) references `groups` (id)
) engine = InnoDb;

--
-- Spring OAuth2 Database Schema
--

DROP TABLE IF EXISTS oauth_client_details;
CREATE TABLE oauth_client_details (
  client_id               VARCHAR(256) PRIMARY KEY,
  resource_ids            VARCHAR(256),
  client_secret           VARCHAR(256),
  scope                   VARCHAR(256),
  authorized_grant_types  VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities             VARCHAR(256),
  access_token_validity   INTEGER,
  refresh_token_validity  INTEGER,
  additional_information  VARCHAR(4096),
  autoapprove             VARCHAR(256)
);

DROP TABLE IF EXISTS oauth_client_token;
CREATE TABLE oauth_client_token (
  token_id          VARCHAR(256),
  token             BLOB,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name         VARCHAR(256),
  client_id         VARCHAR(256)
);

DROP TABLE IF EXISTS oauth_access_token;
CREATE TABLE oauth_access_token (
  token_id          VARCHAR(256),
  token             BLOB,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name         VARCHAR(256),
  client_id         VARCHAR(256),
  authentication    BLOB,
  refresh_token     VARCHAR(256)
);

DROP TABLE IF EXISTS oauth_refresh_token;
CREATE TABLE oauth_refresh_token (
  token_id       VARCHAR(256),
  token          BLOB,
  authentication BLOB
);

DROP TABLE IF EXISTS oauth_code;
CREATE TABLE oauth_code (
  code           VARCHAR(256),
  authentication BLOB
);

DROP TABLE IF EXISTS oauth_approvals;
CREATE TABLE oauth_approvals (
  userId         VARCHAR(256),
  clientId       VARCHAR(256),
  scope          VARCHAR(256),
  status         VARCHAR(10),
  expiresAt      TIMESTAMP,
  lastModifiedAt TIMESTAMP
);

-- customized oauth2_client_details table
DROP TABLE IF EXISTS oauth2_client_details;
CREATE TABLE oauth2_client_details (
  appId                  VARCHAR(256) PRIMARY KEY,
  resourceIds            VARCHAR(256),
  appSecret              VARCHAR(256),
  scope                  VARCHAR(256),
  grantTypes             VARCHAR(256),
  redirectUrl            VARCHAR(256),
  authorities            VARCHAR(256),
  access_token_validity  INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation  VARCHAR(4096),
  autoApproveScopes      VARCHAR(256)
);

-- customized oauth_client_details table
DROP TABLE IF EXISTS ClientDetails;
create table ClientDetails (
  appId VARCHAR(256) PRIMARY KEY,
  resourceIds VARCHAR(256),
  appSecret VARCHAR(256),
  scope VARCHAR(256),
  grantTypes VARCHAR(256),
  redirectUrl VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(256)
);