
-- 초기화
delete from ast_user_balance;
delete from mbr_user;



-- mbr_user 데이터 삽입
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider, role, social_id) VALUES (1, 'N', '2024-06-12 12:00:00.000000', 'admin', '2024-06-12 12:00:00.000000', 'admin', 'John Doe', 'Google', 'User', 'jd123');
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider, role, social_id) VALUES (2, 'Y', '2024-06-11 10:30:00.000000', 'user1', '2024-06-11 10:30:00.000000', 'user1', 'Alice Smith', 'Facebook', 'Admin', 'asmith');
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider, role, social_id) VALUES (3, 'N', '2024-06-10 09:45:00.000000', 'user2', '2024-06-10 09:45:00.000000', 'user2', 'Bob Johnson', 'Twitter', 'User', 'bj123');
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider, role, social_id) VALUES (4, 'Y', '2024-06-09 08:15:00.000000', 'admin', '2024-06-09 08:15:00.000000', 'admin', 'Jane Doe', 'LinkedIn', 'User', 'jd456');
INSERT INTO mbr_user (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, name, provider, role, social_id) VALUES (5, 'N', '2024-06-08 14:20:00.000000', 'user3', '2024-06-08 14:20:00.000000', 'user3', 'Michael Brown', 'Instagram', 'Admin', 'mbrown');


-- ast_user_balance 데이터 삽입
INSERT INTO ast_user_balance (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, point, user_id)
VALUES (1, 'N', '2024-06-12 08:30:00', 'Admin', '2024-06-12 08:30:00', 'Admin', 100, 1);

INSERT INTO ast_user_balance (id, delete_yn, first_input_dtm, first_input_user, last_change_dtm, last_change_user, point, user_id)
VALUES (2, 'N', '2024-06-11 15:45:00', 'JohnDoe', '2024-06-12 10:00:00', 'Admin', 150, 2);
