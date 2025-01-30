// Validación del formulario
document.addEventListener('DOMContentLoaded', function() {
    // Obtener el formulario
    const form = document.getElementById('vlsmForm');
    const hostsContainer = document.getElementById('hostsContainer');
    const btnAgregarHost = document.getElementById('agregarHost');

    // Validar dirección IP
    function validarIP(ip) {
        if (!ip) return false;
        const octetos = ip.split('.');
        if (octetos.length !== 4) return false;
        
        return octetos.every(octeto => {
            const num = parseInt(octeto);
            return num >= 0 && num <= 255;
        });
    }

    // Validar nombre de subred
    function validarNombreSubred(nombre) {
        if (!nombre) return false;
        if (nombre.length > 50) return false;
        return /^[a-zA-Z0-9_\-. ]+$/.test(nombre);
    }

    // Validar número de hosts
    function validarNumHosts(num) {
        const numero = parseInt(num);
        return numero > 0 && numero <= 16777214;
    }

    // Validar máscara
    function validarMascara(mascara) {
        const num = parseInt(mascara);
        return num >= 1 && num <= 32;
    }

    // Verificar nombres duplicados
    function verificarNombresDuplicados() {
        const nombres = Array.from(document.getElementsByName('nombreHost[]'))
            .map(input => input.value.toLowerCase());
        return nombres.length === new Set(nombres).size;
    }

    // Plantilla para nueva entrada de host
    function crearNuevaEntradaHost() {
        const index = document.querySelectorAll('.host-entry').length + 1;
        return `
            <div class="host-entry">
                <div class="row align-items-center">
                    <div class="col-md-5">
                        <label class="form-label">Nombre de Subred</label>
                        <input type="text" class="form-control" name="nombreHost[]" 
                               pattern="^[a-zA-Z0-9_\\-. ]+$"
                               value="LAN ${index}" required>
                        <div class="invalid-feedback">
                            El nombre solo puede contener letras, números, guiones, puntos y espacios
                        </div>
                    </div>
                    <div class="col-md-5">
                        <label class="form-label">Número de Hosts</label>
                        <input type="number" class="form-control" name="numHosts[]" 
                               min="1" max="16777214" required placeholder="Ej: 50">
                        <div class="invalid-feedback">
                            El número de hosts debe ser entre 1 y 16,777,214
                        </div>
                    </div>
                    <div class="col-md-2 text-end">
                        <label class="form-label d-block">&nbsp;</label>
                        <button type="button" class="btn btn-danger remove-host">
                            <i class="bi bi-x-lg"></i>
                        </button>
                    </div>
                </div>
            </div>
        `;
    }

    // Agregar nuevo host
    if (btnAgregarHost) {
        btnAgregarHost.addEventListener('click', function() {
            const template = crearNuevaEntradaHost();
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = template;
            hostsContainer.appendChild(tempDiv.firstElementChild);
        });
    }

    // Remover host
    if (hostsContainer) {
        hostsContainer.addEventListener('click', function(e) {
            if (e.target.closest('.remove-host')) {
                const hostEntry = e.target.closest('.host-entry');
                if (document.querySelectorAll('.host-entry').length > 1) {
                    hostEntry.remove();
                } else {
                    mostrarError('Debe haber al menos una subred');
                }
            }
        });
    }

    // Función para mostrar mensajes de error
    function mostrarError(mensaje) {
        const alertDiv = document.createElement('div');
        alertDiv.className = 'alert alert-danger alert-dismissible fade show';
        alertDiv.setAttribute('role', 'alert');
        alertDiv.innerHTML = `
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            ${mensaje}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        form.insertBefore(alertDiv, form.firstChild);
    }

    // Validar formulario antes de enviar
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            let isValid = true;

            // Validar IP
            const ipInput = document.getElementById('direccionBase');
            if (!validarIP(ipInput.value)) {
                ipInput.setCustomValidity('Dirección IP inválida');
                isValid = false;
            } else {
                ipInput.setCustomValidity('');
            }

            // Validar máscara
            const mascaraInput = document.getElementById('mascaraBase');
            if (!validarMascara(mascaraInput.value)) {
                mascaraInput.setCustomValidity('Máscara inválida');
                isValid = false;
            } else {
                mascaraInput.setCustomValidity('');
            }

            // Validar hosts
            const nombreInputs = document.getElementsByName('nombreHost[]');
            const numHostsInputs = document.getElementsByName('numHosts[]');

            for (let i = 0; i < nombreInputs.length; i++) {
                if (!validarNombreSubred(nombreInputs[i].value)) {
                    nombreInputs[i].setCustomValidity('Nombre de subred inválido');
                    isValid = false;
                } else {
                    nombreInputs[i].setCustomValidity('');
                }

                if (!validarNumHosts(numHostsInputs[i].value)) {
                    numHostsInputs[i].setCustomValidity('Número de hosts inválido');
                    isValid = false;
                } else {
                    numHostsInputs[i].setCustomValidity('');
                }
            }

            // Verificar nombres duplicados
            if (!verificarNombresDuplicados()) {
                mostrarError('No pueden existir subredes con el mismo nombre');
                isValid = false;
            }

            // Validación de Bootstrap
            form.classList.add('was-validated');

            if (isValid) {
                form.submit();
            }
        });
    }
});
