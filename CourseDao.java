package com.score.dao;

import com.score.entity.Course;
import com.score.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程数据访问对象
 */
public class CourseDao {

    /**
     * 查询全部课程
     */
    public List<Course> findAll() {
        String sql = "SELECT id, course_name, credit, teacher FROM t_course ORDER BY id";
        List<Course> list = new ArrayList<>();
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
            throw new RuntimeException("查询课程列表失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 根据 ID 查询课程
     */
    public Course findById(Long id) {
        String sql = "SELECT id, course_name, credit, teacher FROM t_course WHERE id = ?";
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
            throw new RuntimeException("查询课程失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 新增课程
     */
    public void insert(Course course) {
        String sql = "INSERT INTO t_course (course_name, credit, teacher) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, course.getCourseName());
            ps.setDouble(2, course.getCredit());
            ps.setString(3, course.getTeacher());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("新增课程失败", e);
        } finally {
            DBUtil.close(conn, ps);
        }
    }

    /**
     * 删除课程（级联删除该课程成绩）
     */
    public void delete(Long id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("DELETE FROM t_score WHERE course_id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
            ps = conn.prepareStatement("DELETE FROM t_course WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ignored) {
            }
            throw new RuntimeException("删除课程失败", e);
        } finally {
            DBUtil.close(conn, ps);
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setId(rs.getLong("id"));
        c.setCourseName(rs.getString("course_name"));
        c.setCredit(rs.getDouble("credit"));
        c.setTeacher(rs.getString("teacher"));
        return c;
    }
}
