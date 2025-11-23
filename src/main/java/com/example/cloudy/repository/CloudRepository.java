package com.example.cloudy.repository;

import com.example.cloudy.entity.Cloud;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class CloudRepository implements iRepository<Cloud> {
    private final DataSource dataSource;

    public CloudRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Cloud save(Cloud cloud) {
        if (cloud.getId() == null) {
            return insert(cloud);
        }
        return update(cloud);
    }

    private Cloud insert(Cloud cloud) {
        String sql = """
            INSERT INTO clouds (name, max_bytes, used_bytes, path)
            VALUES (?, ?, ?, ?)
            RETURNING id, created_at
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cloud.getName());
            ps.setLong(2, cloud.getMaxBytes());
            ps.setLong(3, cloud.getUsedBytes());
            ps.setString(4, cloud.getPath());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cloud.setId(rs.getLong("id"));
                cloud.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }

            return cloud;

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting cloud", e);
        }
    }

    private Cloud update(Cloud cloud) {
        String sql = """
            UPDATE clouds
            SET name = ?, max_bytes = ?, used_bytes = ?, path = ?
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cloud.getName());
            ps.setLong(2, cloud.getMaxBytes());
            ps.setLong(3, cloud.getUsedBytes());
            ps.setString(4, cloud.getPath());
            ps.setLong(5, cloud.getId());

            ps.executeUpdate();
            return cloud;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating cloud", e);
        }
    }

    @Override
    public Optional<Cloud> findById(Long id) {
        String sql = "SELECT * FROM clouds WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding cloud by id", e);
        }
    }

    public Optional<Cloud> findByName(String name) {
        String sql = "SELECT * FROM clouds WHERE name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding cloud by name", e);
        }
    }

    @Override
    public List<Cloud> findAll() {
        List<Cloud> list = new ArrayList<>();
        String sql = "SELECT * FROM clouds ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Error listing clouds", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM clouds WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting cloud", e);
        }
    }

    private Cloud mapRow(ResultSet rs) throws SQLException {
        return new Cloud(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("max_bytes"),
                rs.getLong("used_bytes"),
                rs.getString("path"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}