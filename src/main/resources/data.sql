
-- 초기화
delete from ast_user_balance;
delete from mbr_user;



-- mbr_user 데이터 삽입
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider_cd, role, social_id) VALUES (1001, 'N', '2024-06-12 12:00:00.000000', 'admin', '2024-06-12 12:00:00.000000', 'admin', 'John Doe', '0', 'User', 'jd123');
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider_cd, role, social_id) VALUES (1002, 'Y', '2024-06-11 10:30:00.000000', 'user1', '2024-06-11 10:30:00.000000', 'user1', 'Alice Smith', '1', 'Admin', 'asmith');
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider_cd, role, social_id) VALUES (1003, 'N', '2024-06-10 09:45:00.000000', 'user2', '2024-06-10 09:45:00.000000', 'user2', 'Bob Johnson', '2', 'User', 'bj123');
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider_cd, role, social_id) VALUES (1004, 'Y', '2024-06-09 08:15:00.000000', 'admin', '2024-06-09 08:15:00.000000', 'admin', 'Jane Doe', '1', 'User', 'jd456');
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider_cd, role, social_id) VALUES (1005, 'N', '2024-06-08 14:20:00.000000', 'user3', '2024-06-08 14:20:00.000000', 'user3', 'Michael Brown', '1', 'Admin', 'mbrown');


-- ast_user_balance 데이터 삽입
INSERT INTO ast_user_balance (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, point, user_id)
VALUES (1, 'N', '2024-06-12 08:30:00', 'Admin', '2024-06-12 08:30:00', 'Admin', 100, 1001);

INSERT INTO ast_user_balance (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, point, user_id)
VALUES (2, 'N', '2024-06-11 15:45:00', 'JohnDoe', '2024-06-12 10:00:00', 'Admin', 150, 1002);
