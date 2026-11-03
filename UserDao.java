package com.score.dao;

import com.score.entity.User;
import com.score.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户数据访问对象
 */
public class UserDao {

    /**
     * 根据登录名查询用户
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, role, name FROM t_user WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));
                user.setName(rs.getString("name"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("查询用户失败", e);
        } finally {
            DBUtil.close(conn, ps, rs);
        }
    }

    /**
     * 更新密码（MD5 摘要）
     */
    public void updatePassword(Long id, String md5Password) {
        String sql = "UPDATE t_user SET password = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, md5Password);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("更新密码失败", e);
        } finally {
            DBUtil.close(conn, ps);
        }
    }
}
