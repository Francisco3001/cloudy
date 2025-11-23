package com.example.cloudy.repository;

import com.example.cloudy.entity.Role;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class RoleRepository implements iRepository<Role> {

    private final DataSource dataSource;

    public RoleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Role save(Role role) {
        if (role.getId() == null) {
            return insert(role);
        }
        return update(role);
    }

    private Role insert(Role role) {
        String sql = """
            INSERT INTO roles (name, can_upload, can_download, can_delete, max_upload_kb)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id, created_at
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role.getName());
            ps.setBoolean(2, role.getCanUpload());
            ps.setBoolean(3, role.getCanDownload());
            ps.setBoolean(4, role.getCanDelete());
            ps.setInt(5, role.getMaxUploadKb());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                role.setId(rs.getLong("id"));
                role.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }

            return role;

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting role", e);
        }
    }

    private Role update(Role role) {
        String sql = """
            UPDATE roles
            SET name = ?, can_upload = ?, can_download = ?, can_delete = ?, max_upload_kb = ?
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role.getName());
            ps.setBoolean(2, role.getCanUpload());
            ps.setBoolean(3, role.getCanDownload());
            ps.setBoolean(4, role.getCanDelete());
            ps.setInt(5, role.getMaxUploadKb());
            ps.setLong(6, role.getId());

            ps.executeUpdate();
            return role;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating role", e);
        }
    }

    @Override
    public Optional<Role> findById(Long id) {
        String sql = "SELECT * FROM roles WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding role", e);
        }
    }

    public Optional<Role> findByName(String name) {
        String sql = "SELECT * FROM roles WHERE name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding role by name", e);
        }
    }

    @Override
    public List<Role> findAll() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM roles ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Error listing roles", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM roles WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting role", e);
        }
    }

    private Role mapRow(ResultSet rs) throws SQLException {
        return new Role(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getBoolean("can_upload"),
                rs.getBoolean("can_download"),
                rs.getBoolean("can_delete"),
                rs.getInt("max_upload_kb"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
