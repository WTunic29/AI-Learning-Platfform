CREATE TABLE usuarios (
	id BIGSERIAL PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	correo VARCHAR(120) NOT NULL UNIQUE,
	password_hash VARCHAR(235) NOT NULL,
	rol VARCHAR(30) NOT NULL DEFAULT 'ESTUDIANTE',
	fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

SELECT*FROM usuarios;

SELECT*FROM cursos;

SELECT*FROM inscripciones;

SELECT id, nombre, correo, rol 
FROM usuarios;

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'usuarios';

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'usuarios'
ORDER BY ordinal_position;

UPDATE usuarios
SET rol = 'SUPERADMIN'
WHERE id = 2;

INSERT INTO cursos (nombre, descripcion, categoria)
VALUES
(
    'Introducción a la Inteligencia Artificial',
    'Curso básico sobre los fundamentos de la inteligencia artificial, sus conceptos principales y aplicaciones.',
    'Tecnología'
),
(
    'Programación en Java',
    'Curso sobre fundamentos de programación orientada a objetos utilizando el lenguaje Java.',
    'Programación'
),
(
    'Bases de Datos Relacionales',
    'Curso introductorio sobre modelado, consultas SQL y administración de bases de datos relacionales.',
    'Bases de Datos'
),
(
    'Desarrollo de Aplicaciones Android',
    'Curso sobre creación de aplicaciones móviles utilizando Android Studio y Java.',
    'Desarrollo Móvil'
),
(
    'Fundamentos de Redes',
    'Curso sobre conceptos básicos de redes, protocolos de comunicación y arquitectura cliente-servidor.',
    'Redes'
);



SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
AND table_name = 'progreso_modulos';

-- Estructura de la tabla progreso_modulos

SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_schema = 'public'
AND table_name = 'progreso_modulos'
ORDER BY ordinal_position;

-- Verificar restricciones de integridad

SELECT tc.constraint_name, tc.constraint_type, kcu.column_name
FROM information_schema.table_constraints tc
LEFT JOIN information_schema.key_column_usage kcu
ON tc.constraint_name = kcu.constraint_name
AND tc.table_schema = kcu.table_schema
WHERE tc.table_schema = 'public'
AND tc.table_name = 'progreso_modulos'
ORDER BY tc.constraint_type, tc.constraint_name, kcu.ordinal_position;

-- Consulta de la estructura de los modulos 

SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'modulos'
ORDER BY ordinal_position;

-- Verifica la secuencia del identificador

SELECT column_name, column_default, is_identity
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'modulos'
  AND column_name = 'id';

-- Para crear un módulo temporal

INSERT INTO modulos (titulo, descripcion, orden, curso_id)
VALUES ('Módulo temporal de prueba HU-11',
    'Registro temporal para verificar el seguimiento del progreso académico.', 1, 1)
RETURNING id, titulo, orden, curso_id;

-- Para verificar el registro en postgres

SELECT pm.id AS progreso_id, pm.usuario_id,
    u.nombre AS estudiante, pm.modulo_id,
    m.titulo AS modulo, pm.fecha_completado
FROM progreso_modulos pm
JOIN usuarios u ON u.id = pm.usuario_id
JOIN modulos m ON m.id = pm.modulo_id
WHERE pm.usuario_id = 3 AND pm.modulo_id = 1;

-- limpiar el módulo temporal

SELECT id, usuario_id, modulo_id, fecha_completado
FROM progreso_modulos
WHERE modulo_id = 1;

DELETE FROM progreso_modulos
WHERE usuario_id = 3 AND modulo_id = 1;

SELECT *
FROM progreso_modulos
WHERE modulo_id = 1;

SELECT *
FROM modulos
WHERE id = 1;

DELETE FROM modulos
WHERE id = 1
  AND titulo = 'Módulo temporal de prueba HU-11'
  AND curso_id = 1;


SELECT id, titulo, descripcion, orden, curso_id
FROM modulos
WHERE curso_id = 1
ORDER BY orden;