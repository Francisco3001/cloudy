package com.example.cloudy.repository;

import com.example.cloudy.entity.UserCloud;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class UserCloudRepository implements iRepository<UserCloud> {

    private final DataSource dataSource;

    public UserCloudRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserCloud save(UserCloud uc) {
        if (uc.getId() == null) return insert(uc);
        return update(uc);
    }

    private UserCloud insert(UserCloud uc) {
        String sql = """
            INSERT INTO user_cloud (user_id, cloud_id, role_id)
            VALUES (?, ?, ?)
            RETURNING id, created_at
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, uc.getUserId());
            ps.setLong(2, uc.getCloudId());
            ps.setLong(3, uc.getRoleId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                uc.setId(rs.getLong("id"));
                uc.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return uc;

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting user_cloud", e);
        }
    }

    private UserCloud update(UserCloud uc) {
        String sql = """
            UPDATE user_cloud
            SET user_id = ?, cloud_id = ?, role_id = ?
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, uc.getUserId());
            ps.setLong(2, uc.getCloudId());
            ps.setLong(3, uc.getRoleId());
            ps.setLong(4, uc.getId());

            ps.executeUpdate();
            return uc;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating user_cloud", e);
        }
    }

    @Override
    public Optional<UserCloud> findById(Long id) {
        String sql = "SELECT * FROM user_cloud WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user_cloud", e);
        }
    }

    @Override
    public List<UserCloud> findAll() {
        List<UserCloud> result = new ArrayList<>();
        String sql = "SELECT * FROM user_cloud ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) result.add(mapRow(rs));
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Error listing user_cloud", e);
        }
    }

    public List<UserCloud> findByCloud(Long cloudId) {
        List<UserCloud> result = new ArrayList<>();
        String sql = "SELECT * FROM user_cloud WHERE cloud_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cloudId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) result.add(mapRow(rs));
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Error listing users in cloud", e);
        }
    }

    public List<UserCloud> findByUser(Long userId) {
        List<UserCloud> result = new ArrayList<>();
        String sql = "SELECT * FROM user_cloud WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) result.add(mapRow(rs));
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Error listing clouds of user", e);
        }
    }

    public Optional<UserCloud> findRoleInCloud(Long userId, Long cloudId) {
        String sql = "SELECT * FROM user_cloud WHERE user_id = ? AND cloud_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, cloudId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding role in cloud", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM user_cloud WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user_cloud", e);
        }
    }

    private UserCloud mapRow(ResultSet rs) throws SQLException {
        return new UserCloud(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("cloud_id"),
                rs.getLong("role_id"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
