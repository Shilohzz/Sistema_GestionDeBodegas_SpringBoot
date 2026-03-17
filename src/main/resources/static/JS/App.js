const BASE = '' ;
let TOKEN = '';
let USER_EMAIL = '';

// ── UTILIDADES ───────────────────────────────────────────────────────────────

function headers() {
    return {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + TOKEN
    };
}

function showMsg(id, text, type) {
    const el = document.getElementById(id);
    el.textContent = text;
    el.className = 'msg ' + type;
    setTimeout(() => el.className = 'msg', 4000);
}

const sectionTitles = {
    bodegas: 'Bodegas', productos: 'Productos', clientes: 'Clientes',
    categorias: 'Categorías', usuarios: 'Usuarios', movimientos: 'Movimientos',
    inventario: 'Inventario', auditoria: 'Auditoría'
};

function showSection(name, btn) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(b => b.classList.remove('active'));
    document.getElementById('sec-' + name).classList.add('active');
    btn.classList.add('active');
    document.getElementById('topbar-title').textContent = sectionTitles[name];
    if (window.innerWidth < 768) closeSidebar();
    if (name === 'bodegas')     cargarBodegas();
    if (name === 'productos')   cargarProductos();
    if (name === 'clientes')    cargarClientes();
    if (name === 'categorias')  cargarCategorias();
    if (name === 'usuarios')    cargarUsuarios();
    if (name === 'movimientos') cargarMovimientos();
    if (name === 'inventario')  cargarInventario();
    if (name === 'auditoria')   cargarAuditoria();
}

function toggleSidebar() {
    const sidebar  = document.getElementById('sidebar');
    const overlay  = document.getElementById('sidebar-overlay');
    const isOpen   = sidebar.classList.toggle('open');
    overlay.classList.toggle('show', isOpen);
}

function closeSidebar() {
    document.getElementById('sidebar').classList.remove('open');
    document.getElementById('sidebar-overlay').classList.remove('show');
}

function badge(text, color) {
    return `<span class="badge badge-${color}">${text}</span>`;
}

function activo(val) {
    return val ? badge('ACTIVO', 'green') : badge('INACTIVO', 'red');
}

function tipoBadge(tipo) {
    const map = { ENTRADA: 'green', SALIDA: 'red', TRANSFERENCIA: 'blue', INSERT: 'green', UPDATE: 'amber', DELETE: 'red' };
    return badge(tipo, map[tipo] || 'blue');
}

// ── AUTH ─────────────────────────────────────────────────────────────────────

async function login() {
    const email    = document.getElementById('login-email').value.trim();
    const password = document.getElementById('login-password').value;
    if (!email || !password) { showMsg('login-msg', 'Completa todos los campos', 'error'); return; }
    try {
        const res = await fetch(BASE + '/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        if (!res.ok) throw new Error('Credenciales inválidas');
        const data = await res.json();
        TOKEN      = data.token;
        USER_EMAIL = email;
        document.getElementById('login-page').style.display  = 'none';
        document.getElementById('app-page').style.display    = 'flex';
        document.getElementById('user-info-sidebar').textContent = email;
        document.getElementById('user-info-top').textContent     = email;
        cargarBodegas();
    } catch (e) {
        showMsg('login-msg', e.message, 'error');
    }
}

function logout() {
    TOKEN = ''; USER_EMAIL = '';
    document.getElementById('login-page').style.display = 'flex';
    document.getElementById('app-page').style.display   = 'none';
}

// ── BODEGAS ──────────────────────────────────────────────────────────────────

async function cargarBodegas() {
    const res  = await fetch(BASE + '/api/bodega', { headers: headers() });
    const data = await res.json();
    document.getElementById('tabla-bodegas').innerHTML = data.map(b => `
        <tr>
            <td><span class="badge badge-blue">#${b.id}</span></td>
            <td>${b.nombreComercial}</td>
            <td>${b.ciudadUbicacion}</td>
            <td>${b.capacidadMaximaUnidades?.toLocaleString()}</td>
            <td>${b.encargado?.nombreCompleto || '—'}</td>
            <td>${activo(b.estaActiva)}</td>
            <td class="td-actions">
                <button class="btn-edit" onclick="editarBodega(${b.id},'${b.nombreComercial}','${b.ciudadUbicacion}','${b.direccionExacta}',${b.capacidadMaximaUnidades},${b.encargado?.id})">Editar</button>
                <button class="btn-del"  onclick="eliminarBodega(${b.id})">Eliminar</button>
            </td>
        </tr>`).join('');
}

async function guardarBodega() {
    const id   = document.getElementById('bodega-id').value;
    const body = {
        nombreComercial:          document.getElementById('bodega-nombre').value,
        ciudadUbicacion:          document.getElementById('bodega-ciudad').value,
        direccionExacta:          document.getElementById('bodega-direccion').value,
        capacidadMaximaUnidades:  parseInt(document.getElementById('bodega-capacidad').value),
        encargadoId:              parseInt(document.getElementById('bodega-encargado').value)
    };
    const url    = id ? `${BASE}/api/bodega/${id}` : `${BASE}/api/bodega`;
    const method = id ? 'PUT' : 'POST';
    try {
        const res = await fetch(url, { method, headers: headers(), body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Error al guardar la bodega');
        showMsg('bodega-msg', id ? 'Bodega actualizada correctamente' : 'Bodega creada correctamente', 'success');
        limpiarBodega(); cargarBodegas();
    } catch (e) { showMsg('bodega-msg', e.message, 'error'); }
}

function editarBodega(id, nombre, ciudad, direccion, capacidad, encargado) {
    document.getElementById('bodega-id').value        = id;
    document.getElementById('bodega-nombre').value    = nombre;
    document.getElementById('bodega-ciudad').value    = ciudad;
    document.getElementById('bodega-direccion').value = direccion;
    document.getElementById('bodega-capacidad').value = capacidad;
    document.getElementById('bodega-encargado').value = encargado;
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function eliminarBodega(id) {
    if (!confirm('¿Eliminar esta bodega?')) return;
    await fetch(`${BASE}/api/bodega/${id}`, { method: 'DELETE', headers: headers() });
    cargarBodegas();
}

function limpiarBodega() {
    ['bodega-id','bodega-nombre','bodega-ciudad','bodega-direccion','bodega-capacidad','bodega-encargado']
        .forEach(id => document.getElementById(id).value = '');
}

// ── PRODUCTOS ────────────────────────────────────────────────────────────────

async function cargarProductos() {
    const res  = await fetch(BASE + '/api/producto', { headers: headers() });
    const data = await res.json();
    document.getElementById('tabla-productos').innerHTML = data.map(p => `
        <tr>
            <td><span class="badge badge-blue">#${p.id}</span></td>
            <td>${p.nombreComercial}</td>
            <td>${p.descripcion || '—'}</td>
            <td>${p.categoria?.nombre || '—'}</td>
            <td>${p.clientePropietario?.nombreCompleto || '—'}</td>
            <td class="td-actions">
                <button class="btn-edit" onclick="editarProducto(${p.id},'${p.nombreComercial}','${p.descripcion||''}',${p.categoria?.id},${p.clientePropietario?.id})">Editar</button>
                <button class="btn-del"  onclick="eliminarProducto(${p.id})">Eliminar</button>
            </td>
        </tr>`).join('');
}

async function guardarProducto() {
    const id   = document.getElementById('producto-id').value;
    const body = {
        nombreComercial:      document.getElementById('producto-nombre').value,
        descripcion:          document.getElementById('producto-descripcion').value,
        categoriaId:          parseInt(document.getElementById('producto-categoria').value),
        clientePropietarioId: parseInt(document.getElementById('producto-cliente').value)
    };
    const url    = id ? `${BASE}/api/producto/${id}` : `${BASE}/api/producto`;
    const method = id ? 'PUT' : 'POST';
    try {
        const res = await fetch(url, { method, headers: headers(), body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Error al guardar el producto');
        showMsg('producto-msg', id ? 'Producto actualizado' : 'Producto creado', 'success');
        limpiarProducto(); cargarProductos();
    } catch (e) { showMsg('producto-msg', e.message, 'error'); }
}

function editarProducto(id, nombre, descripcion, categoriaId, clienteId) {
    document.getElementById('producto-id').value          = id;
    document.getElementById('producto-nombre').value      = nombre;
    document.getElementById('producto-descripcion').value = descripcion;
    document.getElementById('producto-categoria').value   = categoriaId;
    document.getElementById('producto-cliente').value     = clienteId;
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function eliminarProducto(id) {
    if (!confirm('¿Eliminar este producto?')) return;
    await fetch(`${BASE}/api/producto/${id}`, { method: 'DELETE', headers: headers() });
    cargarProductos();
}

function limpiarProducto() {
    ['producto-id','producto-nombre','producto-descripcion','producto-categoria','producto-cliente']
        .forEach(id => document.getElementById(id).value = '');
}

// ── CLIENTES ─────────────────────────────────────────────────────────────────

async function cargarClientes() {
    const res  = await fetch(BASE + '/api/cliente', { headers: headers() });
    const data = await res.json();
    document.getElementById('tabla-clientes').innerHTML = data.map(c => `
        <tr>
            <td><span class="badge badge-blue">#${c.id}</span></td>
            <td>${c.nombreCompleto}</td>
            <td>${badge(c.tipo, c.tipo === 'EMPRESA' ? 'amber' : 'blue')}</td>
            <td>${c.numeroDocumento}</td>
            <td>${c.emailContacto || '—'}</td>
            <td class="td-actions">
                <button class="btn-edit" onclick="editarCliente(${c.id},'${c.nombreCompleto}','${c.tipo}','${c.numeroDocumento}','${c.emailContacto||''}','${c.telefonoContacto||''}','${c.direccionPrincipal||''}')">Editar</button>
                <button class="btn-del"  onclick="eliminarCliente(${c.id})">Eliminar</button>
            </td>
        </tr>`).join('');
}

async function guardarCliente() {
    const id   = document.getElementById('cliente-id').value;
    const body = {
        nombreCompleto:     document.getElementById('cliente-nombre').value,
        tipo:               document.getElementById('cliente-tipo').value,
        numeroDocumento:    document.getElementById('cliente-documento').value,
        emailContacto:      document.getElementById('cliente-email').value,
        telefonoContacto:   document.getElementById('cliente-telefono').value,
        direccionPrincipal: document.getElementById('cliente-direccion').value
    };
    const url    = id ? `${BASE}/api/cliente/${id}` : `${BASE}/api/cliente`;
    const method = id ? 'PUT' : 'POST';
    try {
        const res = await fetch(url, { method, headers: headers(), body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Error al guardar el cliente');
        showMsg('cliente-msg', id ? 'Cliente actualizado' : 'Cliente creado', 'success');
        limpiarCliente(); cargarClientes();
    } catch (e) { showMsg('cliente-msg', e.message, 'error'); }
}

function editarCliente(id, nombre, tipo, documento, email, telefono, direccion) {
    document.getElementById('cliente-id').value        = id;
    document.getElementById('cliente-nombre').value    = nombre;
    document.getElementById('cliente-tipo').value      = tipo;
    document.getElementById('cliente-documento').value = documento;
    document.getElementById('cliente-email').value     = email;
    document.getElementById('cliente-telefono').value  = telefono;
    document.getElementById('cliente-direccion').value = direccion;
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function eliminarCliente(id) {
    if (!confirm('¿Eliminar este cliente?')) return;
    await fetch(`${BASE}/api/cliente/${id}`, { method: 'DELETE', headers: headers() });
    cargarClientes();
}

function limpiarCliente() {
    ['cliente-id','cliente-nombre','cliente-documento','cliente-email','cliente-telefono','cliente-direccion']
        .forEach(id => document.getElementById(id).value = '');
}

// ── CATEGORIAS ───────────────────────────────────────────────────────────────

async function cargarCategorias() {
    const res  = await fetch(BASE + '/api/categoria', { headers: headers() });
    const data = await res.json();
    document.getElementById('tabla-categorias').innerHTML = data.map(c => `
        <tr>
            <td><span class="badge badge-blue">#${c.id}</span></td>
            <td>${c.nombre}</td>
            <td>${c.descripcion || '—'}</td>
            <td class="td-actions">
                <button class="btn-edit" onclick="editarCategoria(${c.id},'${c.nombre}','${c.descripcion||''}')">Editar</button>
                <button class="btn-del"  onclick="eliminarCategoria(${c.id})">Eliminar</button>
            </td>
        </tr>`).join('');
}

async function guardarCategoria() {
    const id   = document.getElementById('categoria-id').value;
    const body = {
        nombre:      document.getElementById('categoria-nombre').value,
        descripcion: document.getElementById('categoria-descripcion').value
    };
    const url    = id ? `${BASE}/api/categoria/${id}` : `${BASE}/api/categoria`;
    const method = id ? 'PUT' : 'POST';
    try {
        const res = await fetch(url, { method, headers: headers(), body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Error al guardar la categoría');
        showMsg('categoria-msg', id ? 'Categoría actualizada' : 'Categoría creada', 'success');
        limpiarCategoria(); cargarCategorias();
    } catch (e) { showMsg('categoria-msg', e.message, 'error'); }
}

function editarCategoria(id, nombre, descripcion) {
    document.getElementById('categoria-id').value          = id;
    document.getElementById('categoria-nombre').value      = nombre;
    document.getElementById('categoria-descripcion').value = descripcion;
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function eliminarCategoria(id) {
    if (!confirm('¿Eliminar esta categoría?')) return;
    await fetch(`${BASE}/api/categoria/${id}`, { method: 'DELETE', headers: headers() });
    cargarCategorias();
}

function limpiarCategoria() {
    ['categoria-id','categoria-nombre','categoria-descripcion']
        .forEach(id => document.getElementById(id).value = '');
}

// ── USUARIOS ─────────────────────────────────────────────────────────────────

async function cargarUsuarios() {
    const res  = await fetch(BASE + '/api/usuario', { headers: headers() });
    const data = await res.json();
    document.getElementById('tabla-usuarios').innerHTML = data.map(u => `
        <tr>
            <td><span class="badge badge-blue">#${u.id}</span></td>
            <td>${u.nombreCompleto}</td>
            <td>${u.emailInstitucional}</td>
            <td>${badge(u.rol, u.rol === 'ADMIN' ? 'amber' : 'blue')}</td>
            <td>${activo(u.estaActivo)}</td>
            <td class="td-actions">
                <button class="btn-del" onclick="eliminarUsuario(${u.id})">Eliminar</button>
            </td>
        </tr>`).join('');
}

async function guardarUsuario() {
    const id   = document.getElementById('usuario-id').value;
    const body = {
        nombreCompleto:      document.getElementById('usuario-nombre').value,
        emailInstitucional:  document.getElementById('usuario-email').value,
        contrasena:          document.getElementById('usuario-password').value,
        rol:                 document.getElementById('usuario-rol').value
    };
    const url    = id ? `${BASE}/api/usuario/${id}` : `${BASE}/api/usuario`;
    const method = id ? 'PUT' : 'POST';
    try {
        const res = await fetch(url, { method, headers: headers(), body: JSON.stringify(body) });
        if (!res.ok) throw new Error('Error al guardar el usuario');
        showMsg('usuario-msg', id ? 'Usuario actualizado' : 'Usuario creado', 'success');
        limpiarUsuario(); cargarUsuarios();
    } catch (e) { showMsg('usuario-msg', e.message, 'error'); }
}

async function eliminarUsuario(id) {
    if (!confirm('¿Eliminar este usuario?')) return;
    await fetch(`${BASE}/api/usuario/${id}`, { method: 'DELETE', headers: headers() });
    cargarUsuarios();
}

function limpiarUsuario() {
    ['usuario-id','usuario-nombre','usuario-email','usuario-password']
        .forEach(id => document.getElementById(id).value = '');
}

// ── MOVIMIENTOS ──────────────────────────────────────────────────────────────

function toggleBodegas() {
    const tipo = document.getElementById('mov-tipo').value;
    document.getElementById('campo-origen').style.display  = tipo === 'ENTRADA' ? 'none' : 'block';
    document.getElementById('campo-destino').style.display = tipo === 'SALIDA'  ? 'none' : 'block';
}

function agregarDetalle() {
    const div = document.createElement('div');
    div.className = 'form-grid detalle-row';
    div.innerHTML = `
        <div class="field-group"><label>ID Producto</label><input class="det-producto" type="number" placeholder="1"></div>
        <div class="field-group"><label>Cantidad</label><input class="det-cantidad" type="number" placeholder="10"></div>`;
    document.getElementById('detalles-container').appendChild(div);
}

async function registrarMovimiento() {
    const tipo      = document.getElementById('mov-tipo').value;
    const origenId  = document.getElementById('mov-origen').value;
    const destinoId = document.getElementById('mov-destino').value;

    const detalles = [];
    document.querySelectorAll('.detalle-row').forEach(row => {
        const pid  = row.querySelector('.det-producto').value;
        const cant = row.querySelector('.det-cantidad').value;
        if (pid && cant) detalles.push({ productoId: parseInt(pid), cantidadUnidades: parseInt(cant) });
    });

    const body = {
        tipo,
        emailUsuario:     USER_EMAIL,
        bodegaOrigenId:   origenId  ? parseInt(origenId)  : null,
        bodegaDestinoId:  destinoId ? parseInt(destinoId) : null,
        observacion:      document.getElementById('mov-observacion').value,
        detalles
    };

    try {
        const res = await fetch(BASE + '/api/movimiento', {
            method: 'POST', headers: headers(), body: JSON.stringify(body)
        });
        if (!res.ok) { const e = await res.json(); throw new Error(e.message || 'Error'); }
        showMsg('movimiento-msg', 'Movimiento registrado correctamente', 'success');
        cargarMovimientos();
    } catch (e) { showMsg('movimiento-msg', 'No se pudo registrar el movimiento. Verifica los datos e intenta de nuevo.', 'error'); }
}

async function cargarMovimientos() {
    const res  = await fetch(BASE + '/api/movimiento', { headers: headers() });
    const data = await res.json();
    document.getElementById('tabla-movimientos').innerHTML = data.map(m => `
        <tr>
            <td><span class="badge badge-blue">#${m.id}</span></td>
            <td>${m.fechaHora?.replace('T',' ').substring(0,16)}</td>
            <td>${tipoBadge(m.tipo)}</td>
            <td>${m.usuarioResponsable?.nombreCompleto || '—'}</td>
            <td>${m.bodegaOrigen?.nombreComercial  || '—'}</td>
            <td>${m.bodegaDestino?.nombreComercial || '—'}</td>
        </tr>`).join('');
}

// ── INVENTARIO ───────────────────────────────────────────────────────────────

async function cargarInventario() {
    const res  = await fetch(BASE + '/api/inventario', { headers: headers() });
    const data = await res.json();
    renderInventario(data);
}

async function cargarStockBajo() {
    const res  = await fetch(BASE + '/api/inventario/stock-bajo', { headers: headers() });
    const data = await res.json();
    renderInventario(data);
}

function renderInventario(data) {
    document.getElementById('tabla-inventario').innerHTML = data.map(i => `
        <tr>
            <td><span class="badge badge-blue">#${i.id}</span></td>
            <td>${i.bodega?.nombreComercial  || '—'}</td>
            <td>${i.producto?.nombreComercial || '—'}</td>
            <td>${i.stockActual < 10
                ? `<span style="color:var(--danger);font-weight:600">${i.stockActual} ⚠</span>`
                : `<span style="color:var(--success);font-weight:600">${i.stockActual}</span>`}
            </td>
            <td>${i.stockMinimoAlerta}</td>
        </tr>`).join('');
}

// ── AUDITORIA ────────────────────────────────────────────────────────────────

async function cargarAuditoria() {
    const tipo      = document.getElementById('audit-tipo').value;
    const usuarioId = document.getElementById('audit-usuario').value;

    let url = BASE + '/api/auditoria';
    if (tipo)      url = `${BASE}/api/auditoria/tipo?operacion=${tipo}`;
    else if (usuarioId) url = `${BASE}/api/auditoria/usuario/${usuarioId}`;

    const res  = await fetch(url, { headers: headers() });
    const data = await res.json();
    document.getElementById('tabla-auditoria').innerHTML = data.map(a => `
        <tr>
            <td><span class="badge badge-blue">#${a.id}</span></td>
            <td>${tipoBadge(a.tipoOperacion)}</td>
            <td>${a.fechaHoraExacta?.replace('T',' ').substring(0,16)}</td>
            <td>${a.usuarioEjecutor?.nombreCompleto || 'Sistema'}</td>
            <td>${a.nombreEntidadAfectada}</td>
            <td style="font-size:11px;color:var(--text-muted)">${a.valoresAnteriores || '—'}</td>
            <td style="font-size:11px;color:var(--text-muted)">${a.valoresNuevos    || '—'}</td>
        </tr>`).join('');
}

// ── INIT ─────────────────────────────────────────────────────────────────────
toggleBodegas();