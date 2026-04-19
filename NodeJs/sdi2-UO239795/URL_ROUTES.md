# URLs del Proyecto - SDI2 UO239795

Este documento lista todas las rutas y URLs disponibles en la aplicación.

## Rutas de Usuarios (Vistas - HTML)

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/signup` | Página de registro de usuarios |
| POST | `/signup` | Envío del formulario de registro |
| GET | `/login` | Página de inicio de sesión |
| POST | `/login` | Envío del formulario de login |
| GET | `/home` | Página principal/inicio |
| GET | `/logout` | Cierre de sesión |
| GET | `/list` | Listado de usuarios (con paginación: `?pg=<número>`) |
| GET | `/requests` | Listado de peticiones de amistad recibidas (con paginación: `?pg=<número>`) |
| GET | `/friends` | Listado de amigos del usuario actual (con paginación: `?pg=<número>`) |

## Rutas de Peticiones de Amistad (Request Handling)

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/send/:id` | Enviar solicitud de amistad al usuario con ID especificado |
| POST | `/accepted/:id` | Aceptar solicitud de amistad por ID |

## Rutas API (REST)

### Autenticación
| Método | URL | Descripción | Token Requerido |
|--------|-----|-------------|-----------------|
| POST | `/api/login/` | Obtener JWT token para autenticación en API | No |

### Usuarios
| Método | URL | Descripción | Token Requerido |
|--------|-----|-------------|-----------------|
| GET | `/api/users/` | Obtener lista de amigos del usuario autenticado | Sí |

### Mensajes
| Método | URL | Descripción | Token Requerido |
|--------|-----|-------------|-----------------|
| GET | `/api/messages/` | Obtener conversación con un usuario (parámetro: `?email=<email>`) | Sí |
| GET | `/api/messages/all` | Obtener todos los mensajes del usuario autenticado | Sí |
| POST | `/api/messages/` | Enviar nuevo mensaje (body: `{email, message}`) | Sí |
| PUT | `/api/messages/:id` | Marcar mensaje como leído por ID | Sí |

## Middleware de Protección API

Las siguientes rutas API requieren un **JWT Token válido** en el header de autorización:
- `/api/users/*`
- `/api/messages/*`

El token debe ser obtenido primero a través de: `POST /api/login/`

## Variables de Sesión

- `req.session.user` - Email del usuario autenticado en la sesión

## Parámetros Comunes

### Paginación
- Parámetro: `?pg=<número>`
- Por defecto: `pg=1`
- Items por página: 5

### Mensajes
- Para obtener conversación específica: `GET /api/messages/?email=<email_del_amigo>`

## Ejemplo de Flujo de Autenticación API

1. **Login API**: `POST /api/login/` → Obtener token JWT
2. **Usar token**: Incluir en headers para acceder a `/api/users/` y `/api/messages/`
3. **Operaciones**: Usar endpoints protegidos con el token

## Ejemplo de Flujo de Aplicación Web

1. **Signup**: `GET /signup` → `POST /signup`
2. **Login**: `GET /login` → `POST /login` → Redirige a `/list`
3. **Listar Usuarios**: `GET /list?pg=1`
4. **Ver Peticiones**: `GET /requests?pg=1`
5. **Enviar Solicitud**: `POST /send/:id` → Redirige a `/list`
6. **Ver Amigos**: `GET /friends?pg=1`
7. **Logout**: `GET /logout`

---

*Última actualización: 2026-04-19*
