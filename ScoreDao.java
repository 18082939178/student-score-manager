package com.score.dao;

import com.score.entity.Score;
import com.score.entity.StatItem;
import com.score.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 成绩数据访问对象
 */
public class ScoreDao {

    /**
     * 查询成绩列表（支持按课程、按班级筛选）
     *
     * @param courseId  课程ID，可为 null
     * @param className 班级名，可为 null
     */
    public List<Score> findList(Long courseId, String className) {
        StringBuilder sql = new StringBuilder(
                "SELECT sc.id, sc.student_id, sc.course_id, sc.score, sc.term, "
                        + "st.student_no, st.name AS student_name, st.class_name, "
                        + "c.course_name "
                        + "FROM t_score sc "
                        + "JOIN t_student st ON sc.student_id = st.id "
                        + "JOIN t_course c ON sc.course_id = c.id WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (courseId != null) {
            sql.append("AND sc.course_id = ? ");
            params.add(courseId);
        }
        if (className != null && !className.isEmpty()) {
            sql.append("AND st.class_name = ? ");
            params.add(className);
        }
        sql.append("ORDER BY c.id, st.class_name, st.student_no");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Score> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("查询成绩列表失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 查询某学生的全部成绩（带课程名）
     */
    public List<Score> findByStudentId(Long studentId) {
        String sql = "SELECT sc.id, sc.student_id, sc.course_id, sc.score, sc.term, "
                + "st.student_no, st.name AS student_name, st.class_name, "
                + "c.course_name "
                + "FROM t_score sc "
                + "JOIN t_student st ON sc.student_id = st.id "
                + "JOIN t_course c ON sc.course_id = c.id "
                + "WHERE sc.student_id = ? ORDER BY c.id";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Score> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, studentId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("查询学生成绩失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 检查是否已存在同一学生同一课程同一学期的成绩
     */
    public boolean exists(Score score) {
        String sql = "SELECT COUNT(*) FROM t_score WHERE student_id = ? AND course_id = ? AND term = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, score.getStudentId());
            ps.setLong(2, score.getCourseId());
            ps.setString(3, score.getTerm());
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("检查成绩记录失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 新增成绩
     */
    public void insert(Score score) {
        String sql = "INSERT INTO t_score (student_id, course_id, score, term) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, score.getStudentId());
            ps.setLong(2, score.getCourseId());
            ps.setDouble(3, score.getScore());
            ps.setString(4, score.getTerm());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("录入成绩失败", e);
        } finally {
            DBUtil.close(conn, ps);
        }
    }

    /**
     * 修改成绩
     */
    public void update(Score score) {
        String sql = "UPDATE t_score SET score = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, score.getScore());
            ps.setLong(2, score.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("修改成绩失败", e);
        } finally {
            DBUtil.close(conn, ps);
        }
    }

    /**
     * 删除成绩
     */
    public void delete(Long id) {
        String sql = "DELETE FROM t_score WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("删除成绩失败", e);
        } finally {
            DBUtil.close(conn, ps);
        }
    }

    /**
     * 按课程统计：人数、平均分、最高分、最低分、及格率、优秀率、不及格人数、分数段分布
     */
    public List<StatItem> statByCourse(String term) {
        String sql = "SELECT c.course_name AS name, c.teacher AS extra, "
                + "COUNT(*) AS cnt, "
                + "ROUND(AVG(sc.score), 2) AS avg_score, "
                + "MAX(sc.score) AS max_score, "
                + "MIN(sc.score) AS min_score, "
                + "ROUND(SUM(CASE WHEN sc.score >= 60 THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) AS pass_rate, "
                + "ROUND(SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) AS excellent_rate, "
                + "SUM(CASE WHEN sc.score < 60 THEN 1 ELSE 0 END) AS fail_cnt, "
                + "SUM(CASE WHEN sc.score < 60 THEN 1 ELSE 0 END) AS b0, "
                + "SUM(CASE WHEN sc.score >= 60 AND sc.score < 70 THEN 1 ELSE 0 END) AS b1, "
                + "SUM(CASE WHEN sc.score >= 70 AND sc.score < 80 THEN 1 ELSE 0 END) AS b2, "
                + "SUM(CASE WHEN sc.score >= 80 AND sc.score < 90 THEN 1 ELSE 0 END) AS b3, "
                + "SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) AS b4 "
                + "FROM t_score sc JOIN t_course c ON sc.course_id = c.id "
                + "WHERE sc.term = ? "
                + "GROUP BY c.id, c.course_name, c.teacher ORDER BY c.id";
        return statQuery(sql, term);
    }

    /**
     * 按班级统计：人数、平均分、及格率、优秀率、不及格人数、分数段分布
     */
    public List<StatItem> statByClass(String term) {
        String sql = "SELECT st.class_name AS name, '' AS extra, "
                + "COUNT(DISTINCT sc.student_id) AS cnt, "
                + "ROUND(AVG(sc.score), 2) AS avg_score, "
                + "MAX(sc.score) AS max_score, "
                + "MIN(sc.score) AS min_score, "
                + "ROUND(SUM(CASE WHEN sc.score >= 60 THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) AS pass_rate, "
                + "ROUND(SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) AS excellent_rate, "
                + "SUM(CASE WHEN sc.score < 60 THEN 1 ELSE 0 END) AS fail_cnt, "
                + "SUM(CASE WHEN sc.score < 60 THEN 1 ELSE 0 END) AS b0, "
                + "SUM(CASE WHEN sc.score >= 60 AND sc.score < 70 THEN 1 ELSE 0 END) AS b1, "
                + "SUM(CASE WHEN sc.score >= 70 AND sc.score < 80 THEN 1 ELSE 0 END) AS b2, "
                + "SUM(CASE WHEN sc.score >= 80 AND sc.score < 90 THEN 1 ELSE 0 END) AS b3, "
                + "SUM(CASE WHEN sc.score >= 90 THEN 1 ELSE 0 END) AS b4 "
                + "FROM t_score sc JOIN t_student st ON sc.student_id = st.id "
                + "WHERE sc.term = ? "
                + "GROUP BY st.class_name ORDER BY st.class_name";
        return statQuery(sql, term);
    }

    private List<StatItem> statQuery(String sql, String term) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<StatItem> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, term);
            rs = ps.executeQuery();
            while (rs.next()) {
                StatItem item = new StatItem();
                item.setName(rs.getString("name"));
                item.setExtra(rs.getString("extra"));
                item.setStudentCount(rs.getLong("cnt"));
                item.setAvgScore(rs.getDouble("avg_score"));
                item.setMaxScore(rs.getDouble("max_score"));
                item.setMinScore(rs.getDouble("min_score"));
                item.setPassRate(rs.getDouble("pass_rate"));
                item.setExcellentRate(rs.getDouble("excellent_rate"));
                item.setFailCount(rs.getLong("fail_cnt"));
                item.setBands(new long[]{
                        rs.getLong("b0"),
                        rs.getLong("b1"),
                        rs.getLong("b2"),
                        rs.getLong("b3"),
                        rs.getLong("b4")
                });
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("统计查询失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 学生个人统计：平均分、最高分、最低分、课程门数、及格门数
     */
    public double[] statForStudent(Long studentId) {
        String sql = "SELECT COUNT(*) AS cnt, "
                + "ROUND(AVG(score), 2) AS avg_score, "
                + "MAX(score) AS max_score, "
                + "MIN(score) AS min_score, "
                + "SUM(CASE WHEN score >= 60 THEN 1 ELSE 0 END) AS pass_cnt "
                + "FROM t_score WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, studentId);
            rs = ps.executeQuery();
            if (rs.next()) {
                // [课程门数, 平均分, 最高分, 最低分, 及格门数]
                return new double[]{
                        rs.getInt("cnt"),
                        rs.getDouble("avg_score"),
                        rs.getDouble("max_score"),
                        rs.getDouble("min_score"),
                        rs.getInt("pass_cnt")
                };
            }
            return new double[]{0, 0, 0, 0, 0};
        } catch (SQLException e) {
            throw new RuntimeException("学生统计失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 计算某学生在某课程中的名次（同分时学号小者排名靠前）
     *
     * @return 名次，从 1 开始
     */
    public int rankInCourse(Long courseId, Long studentId, double score) {
        String sql = "SELECT COUNT(*) FROM t_score "
                + "WHERE course_id = ? AND (score > ? OR (score = ? AND student_id < ?))";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, courseId);
            ps.setDouble(2, score);
            ps.setDouble(3, score);
            ps.setLong(4, studentId);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            throw new RuntimeException("计算排名失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    private Score mapRow(ResultSet rs) throws SQLException {
        Score s = new Score();
        s.setId(rs.getLong("id"));
        s.setStudentId(rs.getLong("student_id"));
        s.setCourseId(rs.getLong("course_id"));
        s.setScore(rs.getDouble("score"));
        s.setTerm(rs.getString("term"));
        s.setStudentNo(rs.getString("student_no"));
        s.setStudentName(rs.getString("student_name"));
        s.setClassName(rs.getString("class_name"));
        s.setCourseName(rs.getString("course_name"));
        return s;
    }
}
