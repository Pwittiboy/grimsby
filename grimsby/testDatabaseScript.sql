-- Dummy DB Script --

-- drop db if exists and create again
DROP TABLE IF EXISTS participants;
DROP TABLE IF EXISTS group_members;
DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS conversations;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS accounts;

-- create accounts table
CREATE TABLE accounts (
	account_id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT current_timestamp,
    username VARCHAR(25) UNIQUE,
    password VARCHAR(25),
    name VARCHAR(255),
    surname VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    image_name VARCHAR(255),
    image BYTEA,
    reminder BOOLEAN,
    first_login BOOLEAN,
    bio VARCHAR(255),
    dark_theme BOOLEAN
); 

-- create groups table
CREATE TABLE groups (
	group_id VARCHAR(255) primary key,
    owner_id integer REFERENCES accounts(account_id) ON DELETE CASCADE NOT NULL,
	group_name varchar(255),
    created_at TIMESTAMP DEFAULT current_timestamp
);

-- create conversations table
CREATE TABLE conversations (
	conversation_id serial primary key,
    owner_id integer REFERENCES accounts(account_id) ON DELETE CASCADE NOT NULL,
    group_id VARCHAR(255) REFERENCES groups(group_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT current_timestamp
);

-- create group_members table
CREATE TABLE group_members (
	group_member_id serial primary key,
    group_id VARCHAR(255) REFERENCES groups(group_id) ON DELETE CASCADE NOT NULL,
	account_id integer REFERENCES accounts(account_id) ON DELETE CASCADE NOT NULL,
    created_at TIMESTAMP DEFAULT current_timestamp
);

-- create messages table
CREATE TABLE messages (
	message_id serial primary key,
    sender_id integer REFERENCES accounts(account_id) ON DELETE CASCADE NOT NULL,
    receiver_id integer REFERENCES accounts(account_id) ON DELETE CASCADE NOT NULL,
    conversation_id integer REFERENCES conversations(conversation_id) ON DELETE CASCADE NOT NULL,
    created_at TIMESTAMP DEFAULT current_timestamp,
    message varchar(255)
);

-- create images table
CREATE TABLE images (
	image_id serial primary key,
    message_id integer REFERENCES messages(message_id) ON DELETE CASCADE NOT NULL,
    sender_id integer REFERENCES accounts(account_id) ON DELETE CASCADE NOT NULL,
    receiver_id integer REFERENCES accounts(account_id) ON DELETE CASCADE NOT NULL,
    conversation_id integer REFERENCES conversations(conversation_id) ON DELETE CASCADE NOT NULL,
    image_name varchar(255),
    image bytea,
    created_at TIMESTAMP DEFAULT current_timestamp
);

-- create participants table
CREATE TABLE participants (
	participant_id serial primary key,
    account_id integer REFERENCES accounts(account_id) ON DELETE CASCADE NOT NULL,
    conversation_id INTEGER REFERENCES conversations(conversation_id) ON DELETE CASCADE NOT NULL,
    created_at TIMESTAMP DEFAULT current_timestamp
);

-- populate with dummy accounts
INSERT INTO accounts (username, password, name, surname, email, reminder, first_login, bio) VALUES
	('dummy1', 'password1', 'name1', 'surname1', 'dummy1@dummy.ac.uk', false, false, 'empty bio'),
    ('dummy2', 'password2', 'name2', 'surname2', 'dummy2@dummy.ac.uk', false, false, 'empty bio'),
    ('dummy3', 'password3', 'name3', 'surname3', 'dummy3@dummy.ac.uk', false, false, 'empty bio'),
    ('dummy4', 'password4', 'name4', 'surname4', 'dummy4@dummy.ac.uk', false, false, 'empty bio'),
    ('cxf798', 'meow', 'Chris', 'Friis', 'cxf798@student.bham.ac.uk', false, false, 'empty bio'),
    ('axz503', 'krabas', 'Akvile', 'Zemgulyte', 'axz503@student.bham.ac.uk', false, false, 'empty bio'),
    ('Muhammad', 'abc123', 'Muhammad', 'Rahim', 'mxr474dstudent.bham.ac.uk', false, false, 'empty bio'),
    ('lcltentacle', 'cjb1219lcl', 'Chenglon', 'Li', '1210035194@qq.com', false, false, 'empty bio'),
    ('master', 'password', 'name', 'surname', 'master@master.com', false, false, 'empty bio')
;

-- populate with dummy contacts
INSERT INTO contacts (owner_id, contact_account_id) VALUES
	(1,2),
    (1,3),
    (2,1),
    (2,3),
    (3,1)
;

-- populate with dummy conversations
INSERT INTO conversations (owner_id) VALUES
	(1),
    (2),
    (1)
;
INSERT INTO conversations (owner_id, group_id) VALUES
    (6, 'dummyGroup2')
;

-- populate with dummy groups
INSERT INTO groups (group_id, owner_id, group_name) VALUES
	('dummyGroup1', 1, 'dummyGroup1'),
    ('dummyGroup2', 2, 'dummyGroup2'),
    ('dummyGroup3', 3, 'dummyGroup3')
;

-- populate with dummy group_members
INSERT INTO group_members (group_id, account_id) VALUES
	('dummyGroup1', 1),
    ('dummyGroup1', 2),
    ('dummyGroup2', 1),
    ('dummyGroup2', 2),
    ('dummyGroup2', 3)
;

-- populate with dummy participants
INSERT INTO participants (account_id, conversation_id) VALUES
	(1,1),
    (2,1),
    (2,2),
    (3,2)
;

-- populate with dummy messages
INSERT INTO messages (sender_id, receiver_id, conversation_id, created_at, message) VALUES
	(1, 2, 1, timestamp '2018-02-01 23:51', 'Getting tired now'),
    (2, 1, 1, timestamp '2018-02-01 23:52', 'Why are you still awake? '),
    (1, 2, 1, timestamp '2018-02-02 03:27', 'Going bed :\ '),
    (6, 7, 2, timestamp '2018-02-02 03:27', 'Going bed :\ '),
    (7, 6, 2, timestamp '2018-02-02 03:27', 'Going bed :\ '),
    (6, 5, 2, timestamp '2018-02-02 03:27', 'Going bed :\ ')
;

