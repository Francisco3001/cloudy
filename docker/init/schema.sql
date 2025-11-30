-- ==========================================================
--  CLOUDY – ESQUEMA PRINCIPAL
--  Base de datos: PostgreSQL
-- ==========================================================

-- =====================
-- 1. TABLA user
-- =====================
CREATE TABLE users (
                         id BIGSERIAL PRIMARY KEY,
                         username VARCHAR(100) NOT NULL UNIQUE,
                         email VARCHAR(200) NOT NULL UNIQUE,
                         password TEXT NOT NULL,
                         enabled BOOLEAN DEFAULT TRUE,
                         created_at TIMESTAMP DEFAULT NOW()
);

-- =====================
-- 2. TABLA cloud
-- =====================
CREATE TABLE clouds (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(150) NOT NULL UNIQUE,
                        max_bytes BIGINT NOT NULL CHECK (max_bytes > 0),
                        used_bytes BIGINT NOT NULL DEFAULT 0 CHECK (used_bytes >= 0),
                        path TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT NOW()
);

-- =====================
-- 3. TABLA role
-- =====================
CREATE TABLE roles (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(150) NOT NULL UNIQUE,
                        can_upload BOOLEAN NOT NULL DEFAULT TRUE,
                        can_download BOOLEAN NOT NULL DEFAULT TRUE,
                        can_delete BOOLEAN NOT NULL DEFAULT FALSE,
                        max_upload_kb INTEGER DEFAULT 0 CHECK (max_upload_kb >= 0),
                        created_at TIMESTAMP DEFAULT NOW()

);

-- ======================================
-- 4. TABLA user_cloud (permisos/roles)
-- ======================================
CREATE TABLE user_cloud (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        cloud_id BIGINT NOT NULL REFERENCES clouds(id) ON DELETE CASCADE,
                        role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                        created_at TIMESTAMP DEFAULT NOW(),
                        UNIQUE (user_id, cloud_id)
);

-- Índices
CREATE INDEX idx_user_cloud_user ON user_cloud(user_id);
CREATE INDEX idx_user_cloud_cloud ON user_cloud(cloud_id);
CREATE INDEX idx_user_cloud_role ON user_cloud(role_id);



-- ============================
-- 5. TABLA file (metadata)
-- ============================
CREATE TABLE files (
                         id BIGSERIAL PRIMARY KEY,
                         cloud_id BIGINT NOT NULL REFERENCES clouds(id) ON DELETE CASCADE,
                         user_id BIGINT REFERENCES users(id),
                         name VARCHAR(255) NOT NULL,
                         bytes BIGINT NOT NULL CHECK (bytes >= 0),
                         path TEXT NOT NULL,
                         hash_sha256 VARCHAR(128),  -- para verificar integridad
                         created_at TIMESTAMP DEFAULT NOW(),
                         UNIQUE (cloud_id, name)
);

-- Índices
CREATE INDEX idx_file_cloud ON files(cloud_id);
CREATE INDEX idx_file_name ON files(name);
CREATE INDEX idx_file_hash ON files(hash_sha256);

