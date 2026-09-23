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

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'usuarios';

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'usuarios'
ORDER BY ordinal_position;

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
