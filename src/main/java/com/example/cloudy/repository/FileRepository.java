package com.example.cloudy.repository;

import com.example.cloudy.entity.File;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class FileRepository implements iRepository<File> {

    private final DataSource dataSource;

    public FileRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public File save(File file) {
        if (file.getId() == null) {
            return insert(file);
        }
        return update(file);
    }

    private File insert(File file) {
        String sql = """
            INSERT INTO files (cloud_id, user_id, name, bytes, path, hash_sha256)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id, created_at
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, file.getCloudId());
            if (file.getUserId() != null) {
                ps.setLong(2, file.getUserId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setString(3, file.getName());
            ps.setLong(4, file.getBytes());
            ps.setString(5, file.getPath());
            ps.setString(6, file.getHashSha256());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                file.setId(rs.getLong("id"));
                file.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return file;

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting file", e);
        }
    }

    private File update(File file) {
        String sql = """
            UPDATE files
            SET cloud_id = ?, user_id = ?, name = ?, bytes = ?, path = ?, hash_sha256 = ?
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, file.getCloudId());
            if (file.getUserId() != null) {
                ps.setLong(2, file.getUserId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            ps.setString(3, file.getName());
            ps.setLong(4, file.getBytes());
            ps.setString(5, file.getPath());
            ps.setString(6, file.getHashSha256());
            ps.setLong(7, file.getId());

            ps.executeUpdate();
            return file;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating file", e);
        }
    }

    @Override
    public Optional<File> findById(Long id) {
        String sql = "SELECT * FROM files WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding file by id", e);
        }
    }

    public Optional<File> findByNameInCloud(Long cloudId, String name) {
        String sql = "SELECT * FROM files WHERE cloud_id = ? AND name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cloudId);
            ps.setString(2, name);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding file by name", e);
        }
    }

    public List<File> findByCloudId(Long cloudId) {
        String sql = "SELECT * FROM files WHERE cloud_id = ? ORDER BY id";
        List<File> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cloudId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) result.add(mapRow(rs));
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Error listing files by cloud id", e);
        }
    }

    @Override
    public List<File> findAll() {
        String sql = "SELECT * FROM files ORDER BY id";
        List<File> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) result.add(mapRow(rs));
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Error listing all files", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM files WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting file", e);
        }
    }

    private File mapRow(ResultSet rs) throws SQLException {
        return new File(
                rs.getLong("id"),
                rs.getLong("cloud_id"),
                rs.getObject("user_id") == null ? null : rs.getLong("user_id"),
                rs.getString("name"),
                rs.getLong("bytes"),
                rs.getString("path"),
                rs.getString("hash_sha256"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

