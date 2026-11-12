# be-ecommerce-apellidonombre

Backend API REST — Spring Boot 3.3 · Java 17 · JPA · Spring Security (JWT stateless) · Swagger · Docker.

## 0. Renombrar con TUS datos (una sola vez)
```bash
unzip be-ecommerce-apellidonombre.zip && cd be-ecommerce-apellidonombre
grep -rl apellidonombre . | xargs sed -i 's/apellidonombre/garciajuan/g'   # tu apellido+nombre
cd .. && mv be-ecommerce-apellidonombre be-ecommerce-garciajuan && cd be-ecommerce-garciajuan
```

## 1. MySQL en Docker (puntos 1.2 y 1.7)
```bash
docker run -d --name mysql-ecommerce -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=db_ecommerce_garciajuan -p 3306:3306 mysql:8
docker exec -i mysql-ecommerce mysql -uroot -proot < script.sql
```
Consultar productos desde la terminal Linux (punto 1.7):
```bash
docker exec -it mysql-ecommerce mysql -uroot -proot db_ecommerce_garciajuan -e "SELECT * FROM productos;"
```

## 2. Ejecutar el backend
```bash
mvn spring-boot:run
```
Swagger: http://localhost:8080/swagger-ui.html

## 3. Probar JWT
```bash
curl -X POST localhost:8080/api/auth/register -H 'Content-Type: application/json' \
  -d '{"username":"juangarcia","password":"clave123"}'
curl -X POST localhost:8080/api/auth/login -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}'
curl -X POST localhost:8080/api/productos -H "Authorization: Bearer TOKEN" -H 'Content-Type: application/json' \
  -d '{"nombre":"Laptop","categoria":"Tecnologia","precioCompra":2000,"precioVenta":2500,"stock":10,"marca":"Lenovo","codigoBarras":"7750001"}'
```
Usuarios iniciales: admin/admin123 (puede DELETE) y user/user123. En Swagger usa el botón **Authorize** con el token.

## 4. Todo con Docker Compose (puntos 2.2 y 2.4)
```bash
docker compose up --build -d
docker ps
docker logs be-ecommerce
```
(Si ya tienes un contenedor `mysql-ecommerce` del paso 1, haz `docker rm -f mysql-ecommerce` antes.)

## 5. GitHub (punto 1.8) — mínimo 2 commits
```bash
git init
git add pom.xml script.sql src && git commit -m "feat: CRUD de productos con JPA"
git add . && git commit -m "feat: seguridad JWT, swagger y docker"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/be-ecommerce-garciajuan.git
git push -u origin main
```

## 6. Render + PostgreSQL (punto 3)
1. Render → **New → PostgreSQL**. Anota Hostname, Database, Username, Password.
2. Render → **New → Web Service** → tu repo de GitHub → **Runtime: Docker**.
3. Variables de entorno del Web Service:

| Variable | Valor |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `DB_URL` | `jdbc:postgresql://HOSTNAME:5432/DATABASE` (formato JDBC) |
| `DB_USER` / `DB_PASSWORD` | credenciales de Render |
| `JWT_SECRET` | salida de `openssl rand -base64 48` |
| `CORS_ORIGINS` | URL de tu frontend |

4. Hibernate crea las tablas en PostgreSQL (`ddl-auto=update`).
5. Video Postman (3.4): `POST /api/auth/register` con tu nombre → `POST /api/productos` → `GET /api/productos` sobre `https://TU-APP.onrender.com`.
6. Video cliente PostgreSQL (3.5): DBeaver/pgAdmin con la *External Database URL* → `SELECT * FROM productos;`

Nota: el plan gratuito de Render duerme la app; la primera petición tarda ~50 s.
