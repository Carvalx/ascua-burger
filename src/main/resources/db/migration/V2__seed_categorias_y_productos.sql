INSERT INTO categorias (id, nombre, slug, created_at) VALUES
                                                          (uuid_generate_v4(), 'Clásicas', 'clasicas', NOW()),
                                                          (uuid_generate_v4(), 'Picantes', 'picantes', NOW()),
                                                          (uuid_generate_v4(), 'Premium', 'premium', NOW()),
                                                          (uuid_generate_v4(), 'Veganas', 'veganas', NOW());

INSERT INTO ingredientes (id, nombre, precio_extra, stock, es_alergeno) VALUES
                                                                            (uuid_generate_v4(), 'Cheddar curado', 1.50, 50, true),
                                                                            (uuid_generate_v4(), 'Bacon ahumado', 1.50, 40, false),
                                                                            (uuid_generate_v4(), 'Jalapeños', 0.50, 60, false),
                                                                            (uuid_generate_v4(), 'Cebolla caramelizada', 0.50, 45, false),
                                                                            (uuid_generate_v4(), 'Foie mi-cuit', 3.00, 20, false),
                                                                            (uuid_generate_v4(), 'Trufa rallada', 3.00, 15, false),
                                                                            (uuid_generate_v4(), 'Queso ahumado', 1.50, 35, true),
                                                                            (uuid_generate_v4(), 'Chipotle', 0.50, 55, false);

INSERT INTO mesas (id, numero, capacidad, zona) VALUES
                                                    (uuid_generate_v4(), 1, 2, 'Interior'),
                                                    (uuid_generate_v4(), 2, 4, 'Interior'),
                                                    (uuid_generate_v4(), 3, 4, 'Terraza'),
                                                    (uuid_generate_v4(), 4, 6, 'Terraza'),
                                                    (uuid_generate_v4(), 5, 2, 'Barra');

INSERT INTO productos (id, nombre, descripcion, precio_base, categoria_id, disponible, imagen_url, created_at)
SELECT
    uuid_generate_v4(),
    'Roble',
    'Doble de res madurada, queso ahumado, cebolla a la brasa y salsa de la casa',
    12.50,
    id,
    true,
    null,
    NOW()
FROM categorias WHERE slug = 'clasicas';

INSERT INTO productos (id, nombre, descripcion, precio_base, categoria_id, disponible, imagen_url, created_at)
SELECT
    uuid_generate_v4(),
    'Brasa',
    'Chistorra, jalapeños frescos, salsa chipotle y lechuga romana',
    13.90,
    id,
    true,
    null,
    NOW()
FROM categorias WHERE slug = 'picantes';

INSERT INTO productos (id, nombre, descripcion, precio_base, categoria_id, disponible, imagen_url, created_at)
SELECT
    uuid_generate_v4(),
    'Forjada',
    'Buey madurado 30 días, foie mi-cuit, cebolla caramelizada en oporto y brioche tostado',
    18.90,
    id,
    true,
    null,
    NOW()
FROM categorias WHERE slug = 'premium';

INSERT INTO productos (id, nombre, descripcion, precio_base, categoria_id, disponible, imagen_url, created_at)
SELECT
    uuid_generate_v4(),
    'Encina',
    'Hamburguesa vegetal de setas y nueces, aguacate, tomate seco y alioli de limón',
    11.90,
    id,
    true,
    null,
    NOW()
FROM categorias WHERE slug = 'veganas';

INSERT INTO productos (id, nombre, descripcion, precio_base, categoria_id, disponible, imagen_url, created_at)
SELECT
    uuid_generate_v4(),
    'Ascua',
    'Bacon ahumado, huevo frito, cheddar curado y salsa barbacoa artesana',
    14.50,
    id,
    true,
    null,
    NOW()
FROM categorias WHERE slug = 'clasicas';