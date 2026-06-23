CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE rol_usuario AS ENUM ('CLIENTE', 'ADMIN', 'COCINA');
CREATE TYPE estado_pedido AS ENUM ('RECIBIDO', 'EN_PREPARACION', 'LISTO', 'ENTREGADO', 'CANCELADO');
CREATE TYPE estado_reserva AS ENUM ('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'COMPLETADA');

CREATE TABLE categorias (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            nombre VARCHAR(100) NOT NULL,
                            slug VARCHAR(100) NOT NULL UNIQUE,
                            created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE productos (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           nombre VARCHAR(150) NOT NULL,
                           descripcion TEXT,
                           precio_base NUMERIC(8,2) NOT NULL,
                           categoria_id UUID NOT NULL REFERENCES categorias(id),
                           disponible BOOLEAN NOT NULL DEFAULT TRUE,
                           imagen_url VARCHAR(500),
                           created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE ingredientes (
                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              nombre VARCHAR(100) NOT NULL,
                              precio_extra NUMERIC(6,2) NOT NULL DEFAULT 0,
                              stock INT NOT NULL DEFAULT 0,
                              es_alergeno BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE producto_ingredientes (
                                       producto_id UUID NOT NULL REFERENCES productos(id),
                                       ingrediente_id UUID NOT NULL REFERENCES ingredientes(id),
                                       incluido_por_defecto BOOLEAN NOT NULL DEFAULT TRUE,
                                       PRIMARY KEY (producto_id, ingrediente_id)
);

CREATE TABLE usuarios (
                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          nombre VARCHAR(150) NOT NULL,
                          rol rol_usuario NOT NULL DEFAULT 'CLIENTE',
                          created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE mesas (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       numero INT NOT NULL UNIQUE,
                       capacidad INT NOT NULL,
                       zona VARCHAR(100)
);

CREATE TABLE pedidos (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         usuario_id UUID NOT NULL REFERENCES usuarios(id),
                         estado estado_pedido NOT NULL DEFAULT 'RECIBIDO',
                         total NUMERIC(10,2) NOT NULL DEFAULT 0,
                         created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE lineas_pedido (
                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               pedido_id UUID NOT NULL REFERENCES pedidos(id),
                               producto_id UUID NOT NULL REFERENCES productos(id),
                               cantidad INT NOT NULL,
                               precio_unitario NUMERIC(8,2) NOT NULL,
                               personalizaciones JSONB
);

CREATE TABLE reservas (
                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          usuario_id UUID NOT NULL REFERENCES usuarios(id),
                          mesa_id UUID NOT NULL REFERENCES mesas(id),
                          fecha DATE NOT NULL,
                          hora_inicio TIME NOT NULL,
                          comensales INT NOT NULL,
                          estado estado_reserva NOT NULL DEFAULT 'PENDIENTE',
                          created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE recomendaciones_ia (
                                    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                    usuario_id UUID NOT NULL REFERENCES usuarios(id),
                                    prompt_hash VARCHAR(64) NOT NULL,
                                    sugerencias JSONB NOT NULL,
                                    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);