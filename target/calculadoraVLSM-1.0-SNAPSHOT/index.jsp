<%-- 
    Document   : index
    Created on : 27 dic 2024, 9:04:26 p. m.
    Author     : fabian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Calculadora VLSM</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <h1 class="text-center mb-4">Calculadora VLSM</h1>
            
            <div class="card">
                <div class="card-body">
                    <form action="SvRed" method="POST">
                        <!-- Dirección IP y Máscara Base -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <label for="direccionBase" class="form-label">Dirección IP Base:</label>
                                <input type="text" class="form-control" id="direccionBase" name="direccionBase" 
                                       placeholder="Ej: 192.168.1.0" required 
                                       pattern="^(\d{1,3}\.){3}\d{1,3}$"
                                       title="Ingrese una dirección IP válida (ej: 192.168.1.0)">
                            </div>
                            <div class="col-md-6">
                                <label for="mascaraBase" class="form-label">Máscara Base:</label>
                                <input type="number" class="form-control" id="mascaraBase" name="mascaraBase" 
                                       min="0" max="32" required
                                       placeholder="Ej: 24">
                            </div>
                        </div>

                        <!-- Contenedor para hosts dinámicos -->
                        <div id="hostsContainer">
                            <div class="host-entry row mb-3">
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="nombreHost[]" 
                                           placeholder="Nombre de la LAN" required>
                                </div>
                                <div class="col-md-5">
                                    <input type="number" class="form-control" name="numHosts[]" 
                                           placeholder="Número de hosts" min="1" required>
                                </div>
                                <div class="col-md-1">
                                    <button type="button" class="btn btn-danger btn-sm remove-host">X</button>
                                </div>
                            </div>
                        </div>

                        <!-- Botones -->
                        <div class="row mt-3">
                            <div class="col-md-6">
                                <button type="button" class="btn btn-secondary" id="agregarHost">
                                    Agregar Host
                                </button>
                            </div>
                            <div class="col-md-6 text-end">
                                <button type="submit" class="btn btn-primary">Calcular</button>
                            </div>
                        </div>

                        <!-- Resultado -->
                        <div class="row mt-3">
                            <div class="col-md-12">
                                <label for="resultado" class="form-label">Resultado:</label>
                                <h1><%= request.getSession().getAttribute("red") %></h1>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.getElementById('agregarHost').addEventListener('click', function() {
                const container = document.getElementById('hostsContainer');
                const newEntry = document.createElement('div');
                newEntry.className = 'host-entry row mb-3';
                newEntry.innerHTML = `
                    <div class="col-md-6">
                        <input type="text" class="form-control" name="nombreHost[]" 
                               placeholder="Nombre de la LAN" required>
                    </div>
                    <div class="col-md-5">
                        <input type="number" class="form-control" name="numHosts[]" 
                               placeholder="Número de hosts" min="1" required>
                    </div>
                    <div class="col-md-1">
                        <button type="button" class="btn btn-danger btn-sm remove-host">X</button>
                    </div>
                `;
                container.appendChild(newEntry);
            });

            document.getElementById('hostsContainer').addEventListener('click', function(e) {
                if (e.target.classList.contains('remove-host')) {
                    const hostEntry = e.target.closest('.host-entry');
                    if (document.getElementsByClassName('host-entry').length > 1) {
                        hostEntry.remove();
                    }
                }
            });
        </script>
    </body>
</html>
