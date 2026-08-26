CREATE TABLE usuarios (
	id BIGSERIAL PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	correo VARCHAR(120) NOT NULL UNIQUE,
	password_hash VARCHAR(235) NOT NULL,
	rol VARCHAR(30) NOT NULL DEFAULT 'ESTUDIANTE',
	fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

SELECT*FROM usuarios;

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'usuarios';

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'usuarios'
ORDER BY ordinal_position;