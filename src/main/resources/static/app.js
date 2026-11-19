
const API = {
    clientes: "/api/clientes",
    pedidos: "/api/pedidos",
    platillos: "/api/platillos",
    reservas: "/api/reservas"
};

const $ = (id) => document.getElementById(id);

function escapar(valor) {
    return String(valor ?? "").replace(/[&<>"']/g, caracter => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
    })[caracter]);
}

async function solicitar(url, opciones = {}) {
    const respuesta = await fetch(url, {
        ...opciones,
        headers: {
            "Content-Type": "application/json",
            ...opciones.headers
        }
    });

    if (!respuesta.ok) {
        throw new Error(`Error HTTP ${respuesta.status}`);
    }

    if (respuesta.status === 204) return null;

    const texto = await respuesta.text();
    return texto ? JSON.parse(texto) : null;
}

function notificar(mensaje) {
    const elemento = $("mensaje");
    elemento.textContent = mensaje;
    elemento.style.display = "block";

    setTimeout(() => {
        elemento.style.display = "none";
    }, 3000);
}

async function cargarClientes() {
    try {
        const clientes = await solicitar(API.clientes);

        $("total-clientes").textContent = clientes.length;

        $("tabla-clientes").innerHTML = clientes.map(cliente => `
            <tr>
                <td>${escapar(cliente.id)}</td>
                <td>${escapar(cliente.nombre)}</td>
                <td>${escapar(cliente.telefono)}</td>
                <td>
                    <button class="small" onclick="editarCliente(${Number(cliente.id)})">
                        Editar
                    </button>
                    <button class="small danger" onclick="eliminarCliente(${Number(cliente.id)})">
                        Eliminar
                    </button>
                </td>
            </tr>
        `).join("") || '<tr><td colspan="4">No hay clientes registrados.</td></tr>';

    } catch (error) {
        console.error("Error al cargar clientes:", error);
        $("total-clientes").textContent = "--";
        $("tabla-clientes").innerHTML =
            '<tr><td colspan="4">No se pudieron cargar los clientes.</td></tr>';
    }
}

async function cargarPedidos() {
    try {
        const pedidos = await solicitar(API.pedidos);

        $("total-pedidos").textContent = pedidos.length;

        $("tabla-pedidos").innerHTML = pedidos.map(pedido => `
            <tr>
                <td>${escapar(pedido.id)}</td>
                <td>${escapar(pedido.cliente?.nombre || "Sin nombre")}</td>
                <td>${escapar(pedido.fecha)}</td>
                <td>${escapar(pedido.total)}</td>
                <td>${escapar(pedido.estado)}</td>
            </tr>
        `).join("") || '<tr><td colspan="5">No hay pedidos registrados.</td></tr>';

    } catch (error) {
        console.error("Error al cargar pedidos:", error);
        $("total-pedidos").textContent = "--";
        $("tabla-pedidos").innerHTML =
            '<tr><td colspan="5">No se pudieron cargar los pedidos.</td></tr>';
    }
}


async function cargarPlatillos() {
    try {
        const platillos = await solicitar(API.platillos);

        $("total-platillos").textContent = platillos.length;

        $("tabla-platillos").innerHTML = platillos.map(p => `
            <tr>
                <td>${escapar(p.id)}</td>
                <td>${escapar(p.nombre)}</td>
                <td>${escapar(p.descripcion)}</td>
                <td>C$ ${escapar(p.precio)}</td>
                <td>${p.disponible ? "Sí" : "No"}</td>
                <td>
                    <button class="small"
                        onclick="editarPlatillo(${Number(p.id)})">
                        Editar
                    </button>
                    <button class="small danger"
                        onclick="eliminarPlatillo(${Number(p.id)})">
                        Eliminar
                    </button>
                </td>
            </tr>
        `).join("") ||
        '<tr><td colspan="6">No hay platillos registrados.</td></tr>';

    } catch (error) {
        console.error(error);
        $("total-platillos").textContent = "--";
        $("tabla-platillos").innerHTML =
            '<tr><td colspan="6">No se pudieron cargar los platillos.</td></tr>';
    }
}

function mostrarFormularioPlatillo() {
    $("platillo-form").reset();
    $("platillo-id").value = "";
    $("platillo-disponible").checked = true;
    $("titulo-formulario-platillo").textContent = "Registrar platillo";
    $("formulario-platillo").classList.remove("hidden");
}

function cerrarFormularioPlatillo() {
    $("formulario-platillo").classList.add("hidden");
}

$("platillo-form").addEventListener("submit", async evento => {
    evento.preventDefault();

    const id = $("platillo-id").value;

    const platillo = {
        nombre: $("platillo-nombre").value.trim(),
        descripcion: $("platillo-descripcion").value.trim(),
        precio: Number($("platillo-precio").value),
        disponible: $("platillo-disponible").checked
    };

    try {
        await solicitar(
            id ? `${API.platillos}/${id}` : API.platillos,
            {
                method: id ? "PUT" : "POST",
                body: JSON.stringify(platillo)
            }
        );

        cerrarFormularioPlatillo();
        notificar(id ? "Platillo actualizado." : "Platillo registrado.");
        cargarPlatillos();

    } catch (error) {
        console.error(error);
        notificar("No se pudo guardar el platillo.");
    }
});

async function editarPlatillo(id) {
    try {
        const p = await solicitar(`${API.platillos}/${id}`);

        $("platillo-id").value = p.id;
        $("platillo-nombre").value = p.nombre;
        $("platillo-descripcion").value = p.descripcion;
        $("platillo-precio").value = p.precio;
        $("platillo-disponible").checked = p.disponible;

        $("titulo-formulario-platillo").textContent = "Editar platillo";
        $("formulario-platillo").classList.remove("hidden");

    } catch (error) {
        console.error(error);
        notificar("No se pudo consultar el platillo.");
    }
}

async function eliminarPlatillo(id) {
    if (!confirm("¿Estás seguro de eliminar este platillo?")) return;

    try {
        await solicitar(`${API.platillos}/${id}`, {
            method: "DELETE"
        });

        notificar("Platillo eliminado.");
        cargarPlatillos();

    } catch (error) {
        console.error(error);
        notificar("No se pudo eliminar el platillo.");
    }
}


async function cargarReservas() {
    try {
        const reservas = await solicitar(API.reservas);

        console.log("Reservas recibidas:", reservas);

        $("total-reservas").textContent = reservas.length;

        $("tabla-reservas").innerHTML = reservas.map(r => `
            <tr>
                <td>${escapar(r.id)}</td>
                <td>${escapar(r.nombreCliente)}</td>
                <td>${escapar(r.fecha)}</td>
                <td>${escapar(r.hora)}</td>
                <td>${escapar(r.cantidadPersonas)}</td>
                <td>${escapar(r.estado)}</td>
                <td>
                    <button class="small"
                        onclick="editarReserva(${Number(r.id)})">
                        Editar
                    </button>
                    <button class="small danger"
                        onclick="eliminarReserva(${Number(r.id)})">
                        Cancelar
                    </button>
                </td>
            </tr>
        `).join("") ||
        '<tr><td colspan="7">No hay reservas registradas.</td></tr>';

    } catch (error) {
        console.error(error);
        $("total-reservas").textContent = "--";
        $("tabla-reservas").innerHTML =
            '<tr><td colspan="7">No se pudieron cargar las reservas.</td></tr>';
    }
}

function mostrarFormularioReserva() {
    $("reserva-form").reset();
    $("reserva-id").value = "";
    $("titulo-formulario-reserva").textContent = "Registrar reserva";
    $("formulario-reserva").classList.remove("hidden");
}

function cerrarFormularioReserva() {
    $("formulario-reserva").classList.add("hidden");
}

$("reserva-form").addEventListener("submit", async evento => {
    evento.preventDefault();

    const id = $("reserva-id").value;

const reserva = {
    nombreCliente: $("reserva-cliente").value.trim(),
    telefono: $("reserva-telefono").value.trim(),
    fecha: $("reserva-fecha").value,
    hora: $("reserva-hora").value,
    cantidadPersonas: Number($("reserva-personas").value)
};

    try {
        await solicitar(
            id ? `${API.reservas}/${id}` : API.reservas,
            {
                method: id ? "PUT" : "POST",
                body: JSON.stringify(reserva)
            }
        );

        cerrarFormularioReserva();
        notificar(id ? "Reserva actualizada." : "Reserva registrada.");
        cargarReservas();

    } catch (error) {
        console.error(error);
        notificar("No se pudo guardar la reserva.");
    }
});


async function editarReserva(id) {
    try {
        const r = await solicitar(`${API.reservas}/${id}`);

        $("reserva-id").value = r.id;
        $("reserva-cliente").value = r.nombreCliente || "";
        $("reserva-telefono").value = r.telefono || "";
        $("reserva-fecha").value = r.fecha || "";
        $("reserva-hora").value = r.hora || "";
        $("reserva-personas").value = r.cantidadPersonas ?? "";

        $("titulo-formulario-reserva").textContent = "Editar reserva";
        $("formulario-reserva").classList.remove("hidden");

    } catch (error) {
        console.error(error);
        notificar("No se pudo consultar la reserva.");
    }
}

async function eliminarReserva(id) {
    if (!confirm("¿Estás seguro de cancelar esta reserva?")) return;

    try {
        await solicitar(`${API.reservas}/${id}/cancelar`, {
            method: "PATCH"
        });

        notificar("Reserva cancelada correctamente.");
        await cargarReservas();

    } catch (error) {
        console.error(error);
        notificar("No se pudo cancelar la reserva.");
    }
}


function mostrarSeccion(nombre) {
    document.querySelectorAll(".content-section").forEach(seccion => {
        seccion.classList.remove("active-section");
    });

    document.querySelectorAll(".nav-link").forEach(enlace => {
        enlace.classList.remove("active");
    });

    $(nombre).classList.add("active-section");

    const enlace = document.querySelector(`[data-section="${nombre}"]`);
    if (enlace) enlace.classList.add("active");

    const titulos = {
        inicio: "Panel principal",
        clientes: "Gestión de clientes",
        pedidos: "Gestión de pedidos",
        platillos: "Gestión de platillos",
        reservas: "Gestión de reservas"
    };

    $("page-title").textContent = titulos[nombre];

    if (nombre === "clientes") cargarClientes();
    if (nombre === "pedidos") cargarPedidos();
    if (nombre === "platillos") cargarPlatillos();
    if (nombre === "reservas") cargarReservas();
}

document.querySelectorAll(".nav-link").forEach(enlace => {
    enlace.addEventListener("click", evento => {
        evento.preventDefault();
        mostrarSeccion(enlace.dataset.section);
    });
});

function mostrarFormularioCliente() {
    $("cliente-form").reset();
    $("cliente-id").value = "";
    $("titulo-formulario-cliente").textContent = "Registrar cliente";
    $("formulario-cliente").classList.remove("hidden");
}

function cerrarFormularioCliente() {
    $("formulario-cliente").classList.add("hidden");
}

$("cliente-form").addEventListener("submit", async evento => {
    evento.preventDefault();

    const id = $("cliente-id").value;

    const cliente = {
        nombre: $("cliente-nombre").value.trim(),
        telefono: $("cliente-telefono").value.trim()
    };

    try {
        await solicitar(
            id ? `${API.clientes}/${id}` : API.clientes,
            {
                method: id ? "PUT" : "POST",
                body: JSON.stringify(cliente)
            }
        );

        cerrarFormularioCliente();
        notificar(id ? "Cliente actualizado." : "Cliente registrado.");
        cargarClientes();

    } catch (error) {
        console.error(error);
        notificar("No se pudo guardar el cliente.");
    }
});

async function editarCliente(id) {
    try {
        const cliente = await solicitar(`${API.clientes}/${id}`);

        $("cliente-id").value = cliente.id;
        $("cliente-nombre").value = cliente.nombre;
        $("cliente-telefono").value = cliente.telefono;

        $("titulo-formulario-cliente").textContent = "Editar cliente";
        $("formulario-cliente").classList.remove("hidden");

    } catch (error) {
        console.error(error);
        notificar("No se pudo consultar el cliente.");
    }
}

async function eliminarCliente(id) {
    if (!confirm("¿Estás seguro de eliminar este cliente?")) return;

    try {
        await solicitar(`${API.clientes}/${id}`, {
            method: "DELETE"
        });

        notificar("Cliente eliminado.");
        cargarClientes();

    } catch (error) {
        console.error(error);
        notificar("No se pudo eliminar el cliente.");
    }
}

async function cargarPanel() {
    await Promise.all([
        cargarClientes(),
        cargarPedidos(),
        cargarPlatillos(),
        cargarReservas()
    ]);
}

cargarPanel();