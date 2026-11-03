-- =====================================================================
-- 班级学生成绩管理系统  数据库初始化脚本
-- 数据库：MySQL 8.0
-- 字符集：utf8mb4
-- =====================================================================

DROP DATABASE IF EXISTS student_score;
CREATE DATABASE student_score DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE student_score;

-- ---------------------------------------------------------------------
-- 1. 用户表（教师 / 学生共用登录账号）
--    role: 1 = 教师, 0 = 学生
--    password 为 MD5 值，默认密码均为 123456
-- ---------------------------------------------------------------------
CREATE TABLE t_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username    VARCHAR(50)  NOT NULL COMMENT '登录名',
    password    CHAR(32)     NOT NULL COMMENT 'MD5加密后的密码',
    role        TINYINT      NOT NULL DEFAULT 0 COMMENT '角色：1教师 0学生',
    name        VARCHAR(50)  NOT NULL COMMENT '姓名',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB COMMENT='用户表';

-- ---------------------------------------------------------------------
-- 2. 学生信息表
-- ---------------------------------------------------------------------
CREATE TABLE t_student (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    student_no  VARCHAR(20)  NOT NULL COMMENT '学号',
    name        VARCHAR(50)  NOT NULL COMMENT '姓名',
    class_name  VARCHAR(50)  NOT NULL COMMENT '班级',
    user_id     BIGINT       NOT NULL COMMENT '关联登录账号ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_no (student_no),
    KEY idx_class (class_name),
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES t_user (id)
) ENGINE=InnoDB COMMENT='学生信息表';

-- ---------------------------------------------------------------------
-- 3. 课程表
-- ---------------------------------------------------------------------
CREATE TABLE t_course (
    id           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    course_name  VARCHAR(100)  NOT NULL COMMENT '课程名称',
    credit       DECIMAL(2,1)  NOT NULL DEFAULT 1.0 COMMENT '学分',
    teacher      VARCHAR(50)   NOT NULL COMMENT '任课教师',
    PRIMARY KEY (id)
) ENGINE=InnoDB COMMENT='课程表';

-- ---------------------------------------------------------------------
-- 4. 成绩表
--    一名学生在同一学期同一课程只能有一条成绩记录
-- ---------------------------------------------------------------------
CREATE TABLE t_score (
    id          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    student_id  BIGINT        NOT NULL COMMENT '学生ID',
    course_id   BIGINT        NOT NULL COMMENT '课程ID',
    score       DECIMAL(5,2)  NOT NULL COMMENT '成绩（0~100）',
    term        VARCHAR(20)   NOT NULL COMMENT '学期，如 2025-2026-1',
    create_time DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_stu_course_term (student_id, course_id, term),
    KEY idx_course (course_id),
    CONSTRAINT fk_score_student FOREIGN KEY (student_id) REFERENCES t_student (id),
    CONSTRAINT fk_score_course  FOREIGN KEY (course_id)  REFERENCES t_course (id),
    CONSTRAINT chk_score_range  CHECK (score >= 0 AND score <= 100)
) ENGINE=InnoDB COMMENT='成绩表';

-- =====================================================================
-- 测试数据
-- =====================================================================

-- 登录账号（密码均为 123456 的 MD5 值 e10adc3949ba59abbe56e057f20f883e）
INSERT INTO t_user (username, password, role, name) VALUES
('teacher01', 'e10adc3949ba59abbe56e057f20f883e', 1, '张老师'),
('stu001',    'e10adc3949ba59abbe56e057f20f883e', 0, '王小明'),
('stu002',    'e10adc3949ba59abbe56e057f20f883e', 0, '李小红'),
('stu003',    'e10adc3949ba59abbe56e057f20f883e', 0, '赵大勇'),
('stu004',    'e10adc3949ba59abbe56e057f20f883e', 0, '陈静怡'),
('stu005',    'e10adc3949ba59abbe56e057f20f883e', 0, '刘子豪');

-- 学生信息
INSERT INTO t_student (student_no, name, class_name, user_id) VALUES
('20230101', '王小明', '软件2301班', 2),
('20230102', '李小红', '软件2301班', 3),
('20230103', '赵大勇', '软件2301班', 4),
('20230104', '陈静怡', '软件2302班', 5),
('20230105', '刘子豪', '软件2302班', 6);

-- 课程
INSERT INTO t_course (course_name, credit, teacher) VALUES
('JavaEE开发技术', 3.0, '张老师'),
('数据库原理',     3.0, '李老师'),
('操作系统',       2.5, '王老师'),
('计算机网络',     2.5, '赵老师');

-- 成绩（包含及格与不及格数据，用于展示平均分、及格率统计）
INSERT INTO t_score (student_id, course_id, score, term) VALUES
(1, 1, 88.00, '2025-2026-1'),
(1, 2, 92.50, '2025-2026-1'),
(1, 3, 76.00, '2025-2026-1'),
(1, 4, 85.00, '2025-2026-1'),
(2, 1, 79.00, '2025-2026-1'),
(2, 2, 65.50, '2025-2026-1'),
(2, 3, 90.00, '2025-2026-1'),
(2, 4, 58.00, '2025-2026-1'),
(3, 1, 55.00, '2025-2026-1'),
(3, 2, 73.00, '2025-2026-1'),
(3, 3, 82.00, '2025-2026-1'),
(3, 4, 61.50, '2025-2026-1'),
(4, 1, 95.00, '2025-2026-1'),
(4, 2, 88.00, '2025-2026-1'),
(4, 3, 91.00, '2025-2026-1'),
(4, 4, 77.00, '2025-2026-1'),
(5, 1, 67.00, '2025-2026-1'),
(5, 2, 59.00, '2025-2026-1'),
(5, 3, 84.50, '2025-2026-1'),
(5, 4, 90.00, '2025-2026-1');
