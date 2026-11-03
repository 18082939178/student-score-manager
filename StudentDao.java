package com.score.dao;

import com.score.entity.Student;
import com.score.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生信息数据访问对象
 */
public class StudentDao {

    /**
     * 查询全部学生
     */
    public List<Student> findAll() {
        String sql = "SELECT id, student_no, name, class_name, user_id FROM t_student ORDER BY class_name, student_no";
        List<Student> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("查询学生列表失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 根据 ID 查询学生
     */
    public Student findById(Long id) {
        String sql = "SELECT id, student_no, name, class_name, user_id FROM t_student WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("查询学生失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 根据登录账号 ID 查询学生
     */
    public Student findByUserId(Long userId) {
        String sql = "SELECT id, student_no, name, class_name, user_id FROM t_student WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("查询学生失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 查询全部班级名称（去重）
     */
    public List<String> findAllClasses() {
        String sql = "SELECT DISTINCT class_name FROM t_student ORDER BY class_name";
        List<String> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("查询班级列表失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 检查学号是否已存在
     */
    public boolean existsByStudentNo(String studentNo, Long excludeId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM t_student WHERE student_no = ?");
        if (excludeId != null) {
            sql.append(" AND id <> ?");
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, studentNo);
            if (excludeId != null) {
                ps.setLong(2, excludeId);
            }
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("检查学号失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 新增学生（同时创建其登录账号）
     */
    public void insert(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            // 1. 创建登录账号（默认密码 123456）
            String userSql = "INSERT INTO t_user (username, password, role, name) VALUES (?, ?, 0, ?)";
            ps = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getStudentNo());
            ps.setString(2, com.score.util.MD5Util.md5("123456"));
            ps.setString(3, student.getName());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            long userId = -1;
            if (keys.next()) {
                userId = keys.getLong(1);
            }
            // 2. 创建学生信息
            String stuSql = "INSERT INTO t_student (student_no, name, class_name, user_id) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(stuSql);
            ps.setString(1, student.getStudentNo());
            ps.setString(2, student.getName());
            ps.setString(3, student.getClassName());
            ps.setLong(4, userId);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ignored) {
            }
            throw new RuntimeException("新增学生失败（学号可能已存在）", e);
        } finally {
            DBUtil.close(conn, ps);
        }
    }

    /**
     * 更新学生信息
     */
    public void update(Student student) {
        String sql = "UPDATE t_student SET student_no = ?, name = ?, class_name = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, student.getStudentNo());
            ps.setString(2, student.getName());
            ps.setString(3, student.getClassName());
            ps.setLong(4, student.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("更新学生失败", e);
        } finally {
            DBUtil.close(conn, ps);
        }
    }

    /**
     * 删除学生（同时删除其登录账号与成绩）
     */
    public void delete(Long id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            Student student = findById(id);
            // 1. 删除成绩
            ps = conn.prepareStatement("DELETE FROM t_score WHERE student_id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
            // 2. 删除学生信息
            ps = conn.prepareStatement("DELETE FROM t_student WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
            // 3. 删除登录账号
            if (student != null && student.getUserId() != null) {
                ps = conn.prepareStatement("DELETE FROM t_user WHERE id = ?");
                ps.setLong(1, student.getUserId());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ignored) {
            }
            throw new RuntimeException("删除学生失败", e);
        } finally {
            DBUtil.close(conn, ps);
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getLong("id"));
        s.setStudentNo(rs.getString("student_no"));
        s.setName(rs.getString("name"));
        s.setClassName(rs.getString("class_name"));
        s.setUserId(rs.getLong("user_id"));
        return s;
    }
}
